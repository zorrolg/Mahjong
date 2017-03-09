/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 严格模式协议包编码器，用于对协议包进行数据加密
 * 
 * @author sky
 * @date 2011-9-20
 * @version
 * 
 */
public class PacketStrictEncoder extends ProtocolEncoderAdapter
{
    /*
     * 加密数据包
     * 
     * @see
     * org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.core
     * .session.IoSession, java.lang.Object,
     * org.apache.mina.filter.codec.ProtocolEncoderOutput)
     */
    @Override
    public void encode(IoSession session, Object message,
            ProtocolEncoderOutput out) throws Exception
    {
        // 存在不同线程给同一玩家发送数据的情况，因此加密过程需要同步处理
        int lastCipherByte = 0;
        Packet packet = (Packet) message;

        // 写入协议包头
        packet.clearCheckSum();
        packet.writeHeader();
        packet.flip();

        int[] encryptKey = getContext(session);
        byte[] plainText = packet.getBytes();
        int length = plainText.length;
        IoBuffer cipherBuffer = IoBuffer.allocate(length);

        
//        String strPrint = "doDecode===========1======";
//        for (int i = 0; i < 8; i++)
//        	strPrint += encryptKey[i] + ",";        	
//       	 //System.out.println(strPrint);
       	 
       	 
        // 加密首字节
        lastCipherByte = (plainText[0] ^ encryptKey[0]) & 0xff;
        cipherBuffer.put((byte) lastCipherByte);

        // 循环加密
        int keyIndex = 0;
        for (int i = 1; i < length; i++)
        {
            keyIndex = i & 0x7;
            encryptKey[keyIndex] = ((encryptKey[keyIndex] + lastCipherByte) ^ i) & 0xff;
            lastCipherByte = (((plainText[i] ^ encryptKey[keyIndex]) & 0xff) + lastCipherByte) & 0xff;
            cipherBuffer.put((byte) lastCipherByte);
        }

        out.write(cipherBuffer.flip());
        
        
//        String strPrint1 = "doDecode===========1======";
//        for (int i = 0; i < 8; i++)
//        	strPrint1 += encryptKey[i] + ",";        	
//       	 //System.out.println(strPrint1);
//       	 
//        //System.out.println("PacketStrictEncoder===============" + length);
    }

    // 获取当前加密密钥
    private int[] getContext(IoSession session)
    {
        int[] encryptKey = (int[]) session.getAttribute(PacketStrictEncoder.class);

        if (encryptKey == null)
        {
            encryptKey = new int[] { 0xae, 0xbf, 0x56, 0x78, 0xab, 0xcd, 0xef,
                    0xf1 };
            session.setAttribute(PacketStrictEncoder.class, encryptKey);
        }

        return encryptKey;
    }
}
