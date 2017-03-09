/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.socket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 普通模式数据包编码器，仅用于非加密数据包处理
 * @author sky
 * @date 2011-9-22
 * @version 
 *
 */
public class PacketCommonEncoder extends ProtocolEncoderAdapter
{
    /*
     * 编码数据包
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
        Packet packet = (Packet) message;

        // 写入协议包头
        packet.clearCheckSum();
        packet.writeHeader();
        packet.flip();
        
        byte[] data = packet.getBytes();

        out.write(IoBuffer.wrap(data));
    }
}
