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
public class PacketWebSocketEncoder extends ProtocolEncoderAdapter
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
    	Packet packet = (Packet) message;

        // 写入协议包头
        packet.clearCheckSum();
        packet.writeHeader();
        packet.flip();
        
        byte[] data = packet.getBytes();

        out.write(IoBuffer.wrap(data));
    }

}
