package com.citywar.socket;


import org.apache.mina.core.session.IoSession;

/**
 * 游戏指令处理接口，所有游戏指令的处理类需实现本接口
 * 
 */
public interface Command
{
    public void execute(IoSession session, Packet packet);
}
