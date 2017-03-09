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
 * 普通模式数据包解码器，仅用于非加密数据包处理
 * 
 * @author sky
 * @date 2011-9-22
 * @version
 * 
 */
public class PacketCommonDecoder extends CumulativeProtocolDecoder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PacketCommonDecoder.class);

    /*
     * 解码接收到的数据包
     * 
     * @see
     * org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.apache
     * .mina.core.session.IoSession, org.apache.mina.core.buffer.IoBuffer,
     * org.apache.mina.filter.codec.ProtocolDecoderOutput)
     */
    protected boolean doDecode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception
    {
        if (in.remaining() < 4)
        {
            // 剩余不足4字节，不足以解析数据包头，暂不处理
            return false;
        }

        int data1, data2;

        // 读取两字节header
        data1 = in.get() & 0xff;
        data2 = in.get() & 0xff;

        int header = ((data1 << 8) + data2);

        if (header != Packet.HEADER)
            // 非数据包头部，跳过，继续解码
            return true;

        // 读取两字节length
        data1 = in.get() & 0xff;
        data2 = in.get() & 0xff;

        int packetLength = (data1 << 8) + data2;

        if (packetLength < Packet.HDR_SIZE)
        {
            // 数据包长度错误
            if (LOGGER.isErrorEnabled())
            {
                LOGGER.error("error packet length: packetlength=%d Packet.HDR_SIZE=%d",
                             packetLength, Packet.HDR_SIZE);
            }
        }

        // 回溯位置
        in.position(in.position() - 4);

        if (in.remaining() < packetLength)
            // 数据长度不足，等待下次接收
            return false;

        // 读取数据
        byte[] data = new byte[packetLength];
        in.get(data, 0, packetLength);

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

        return true;
    }
}
