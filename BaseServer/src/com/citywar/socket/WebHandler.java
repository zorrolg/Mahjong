package com.citywar.socket;

import javax.websocket.Session;


/**
 * 游戏协议处理器接口，所有协议处理类需实现本接口
 * 
 */
public interface WebHandler
{
    public void handlePacket(Session session, Packet packet);
}
