package com.citywar.socket;

import org.apache.mina.core.session.IoSession;

/**
 * 游戏协议处理器接口，所有协议处理类需实现本接口
 * 
 */
public interface Handler
{
    public void handlePacket(IoSession session, Packet packet);
}
