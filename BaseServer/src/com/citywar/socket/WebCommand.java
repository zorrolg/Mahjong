package com.citywar.socket;


import javax.websocket.Session;

/**
 * 游戏指令处理接口，所有游戏指令的处理类需实现本接口
 * 
 */
public interface WebCommand
{
    public void execute(Session session, Packet packet);
}
