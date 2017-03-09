/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.socket;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 带协议包加解密功能的解码工厂
 * @author sky
 * @date 2011-9-21
 * @version
 * 
 */
public class WebSocketCodecFactory implements ProtocolCodecFactory
{
    private final PacketWebSocketEncoder encoder;

    private final PacketWebSocketDecoder decoder;

    public WebSocketCodecFactory()
    {
        encoder = new PacketWebSocketEncoder();
        decoder = new PacketWebSocketDecoder();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.mina.filter.codec.ProtocolCodecFactory#getEncoder(org.apache
     * .mina.core.session.IoSession)
     */
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception
    {
        return encoder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.mina.filter.codec.ProtocolCodecFactory#getDecoder(org.apache
     * .mina.core.session.IoSession)
     */
    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception
    {
        return decoder;
    }
}
