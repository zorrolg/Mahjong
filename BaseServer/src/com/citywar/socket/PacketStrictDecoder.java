/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 严格模式数据包解析器，用于数据解密及数据包创建
 * 
 * @author sky
 * @date 2011-9-20
 * @version
 * 
 */
public class PacketStrictDecoder extends CumulativeProtocolDecoder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PacketStrictDecoder.class);

    private static final String POLICY = "<?xml version=\"1.0\"?><!DOCTYPE cross-domain-policy SYSTEM \"http://www.adobe.com/xml/dtds/cross-domain-policy.dtd\"><cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>\0";

    private static byte[] POLICY_BYTES;

    static
    {
        try
        {
            POLICY_BYTES = POLICY.getBytes("utf8");
        }
        catch (Exception e)
        {
            POLICY_BYTES = null;
            if (LOGGER.isErrorEnabled())
                LOGGER.error("fail to encode POLICY string");
        }
    }

    /*
     * 解码接收到的数据包
     * 
     * @see
     * org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.apache
     * .mina.core.session.IoSession, org.apache.mina.core.buffer.IoBuffer,
     * org.apache.mina.filter.codec.ProtocolDecoderOutput)
     */
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception
    {
        if (in.remaining() < 4)
        {
            // 剩余不足4字节，不足以解析数据包头，暂不处理
            return false;
        }

        int[] decryptKey = getContext(session);

//        String strPrint1 = "doDecode===========1======";
//        for (int i = 0; i < 8; i++)
//        	strPrint1 += decryptKey[i] + ",";        	
//       	 //System.out.println(strPrint1);
        
        // 此处4字节头部的解码使用直接解码形式，规避频繁的对象创建
        int cipherByte1, cipherByte2, cipherByte3, cipherByte4;
        int plainByte1, plainByte2, plainByte3, plainByte4;
        int key;

        // 解密两字节header
        cipherByte1 = in.get() & 0xff;
        key = decryptKey[0];
        plainByte1 = (cipherByte1 ^ key) & 0xff;

        cipherByte2 = in.get() & 0xff;
        key = ((decryptKey[1] + cipherByte1) ^ 1) & 0xff;
        plainByte2 = ((cipherByte2 - cipherByte1) ^ key) & 0xff;

        int header = ((plainByte1 << 8) + plainByte2);

        if (header != Packet.HEADER)
        {
            if (cipherByte1 == '<')
            {
                // 客户端请求授权文件，直接返回即可
                if (POLICY_BYTES != null)
                    session.write(IoBuffer.wrap(POLICY_BYTES));
                else
                    session.write(IoBuffer.wrap(POLICY.getBytes("utf8")));

                return false;
            }

            // 非数据包头部，跳过，继续解密
            return true;
        }

        // 解密两字节length
        cipherByte3 = in.get() & 0xff;
        key = ((decryptKey[2] + cipherByte2) ^ 2) & 0xff;
        plainByte3 = ((cipherByte3 - cipherByte2) ^ key) & 0xff;

        cipherByte4 = in.get() & 0xff;
        key = ((decryptKey[3] + cipherByte3) ^ 3) & 0xff;
        plainByte4 = ((cipherByte4 - cipherByte3) ^ key) & 0xff;

        int packetLength = (plainByte3 << 8) + plainByte4;

        if (packetLength < Packet.HDR_SIZE)
        {
            // 数据包长度错误，断开连接
            if (LOGGER.isErrorEnabled())
            {
                LOGGER.error("error packet length: packetlength=%d Packet.HDR_SIZE=%d",
                             packetLength, Packet.HDR_SIZE);
                LOGGER.error("Disconnect the client:%s",
                             session.getRemoteAddress());
            }

            session.close(true);
        }

        // 预解密长度信息成功，回溯位置
        in.position(in.position() - 4);

        if (in.remaining() < packetLength)
            // 数据长度不足，等待下次接收
            return false;

        // 读取数据并解密数据
        byte[] data = new byte[packetLength];
        in.get(data, 0, packetLength);
        data = decrypt(data, decryptKey);

        // 构造数据包
        Packet packet = new Packet(data.length);
        packet.writePacket(data, 0, data.length);
        packet.readHeader();
        
        if (packet.getChecksum() == packet.calcChecksum())
        {
            out.write(packet);
        }
        else
        {
            LOGGER.warn(String.format("数据包校验失败，数据包将被丢弃。协议ID 0x%x%n",
                                      packet.getCode()));
            LOGGER.warn(String.format("校验和应为%d，实际接收校验和为%d%n",
                                      packet.calcChecksum(),
                                      packet.getChecksum()));
        }

        
//        String strPrint = "doDecode===========2======";
//        for (int i = 0; i < 8; i++)
//        	strPrint += decryptKey[i] + ",";        	
//       	 //System.out.println(strPrint);
       	 
        return true;
    }

    // 获取密钥上下文
    private int[] getContext(IoSession session)
    {
        int[] decryptKey = (int[]) session.getAttribute(PacketStrictDecoder.class);

        if (decryptKey == null)
        {
            decryptKey = new int[] { 0xae, 0xbf, 0x56, 0x78, 0xab, 0xcd, 0xef,
                    0xf1 };
            session.setAttribute(PacketStrictDecoder.class, decryptKey);
        }

        return decryptKey;
    }

    // 解密整段数据
    private byte[] decrypt(byte[] data, int[] decryptKey) throws Exception
    {
        if (data.length == 0)
            return data;

        if (decryptKey.length < 8)
            throw new Exception("The decryptKey must be 64bits length!");

        int length = data.length;
        int lastCipherByte;
        int plainText;
        int key;

        // 解密首字节
        lastCipherByte = data[0] & 0xff;
        data[0] ^= decryptKey[0];

        for (int index = 1; index < length; index++)
        {
            // 解密当前字节
            key = ((decryptKey[index & 0x7] + lastCipherByte) ^ index);
            plainText = (((data[index] & 0xff) - lastCipherByte) ^ key) & 0xff;

            // 更新变量值
            lastCipherByte = data[index] & 0xff;
            data[index] = (byte) plainText;
            decryptKey[index & 0x7] = key & 0xff;
        }

        return data;
    }
}
