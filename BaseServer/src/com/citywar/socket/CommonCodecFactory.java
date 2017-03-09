/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.socket;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 普通协议编解码工厂
 * @author sky
 * @date 2011-9-22
 * @version
 * 
 */
public class CommonCodecFactory implements ProtocolCodecFactory
{
    private final PacketCommonEncoder encoder;

    private final PacketCommonDecoder decoder;

    public CommonCodecFactory()
    {
        encoder = new PacketCommonEncoder();
        decoder = new PacketCommonDecoder();
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
