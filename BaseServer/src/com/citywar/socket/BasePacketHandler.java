package com.citywar.socket;

import java.util.HashMap;

import javax.websocket.Session;

import org.apache.mina.core.session.IoSession;

/**
 * 抽象的协议处理类，用于会收到的数据包查找对应的指令处理器
 * 
 */
public abstract class BasePacketHandler implements Handler
{
    protected final HashMap<Integer, Command> commands;
    protected final HashMap<Integer, WebCommand> webCommands;

    public BasePacketHandler()
    {
        commands = new HashMap<Integer, Command>();
        webCommands = new HashMap<Integer, WebCommand>();
    }

    public final void handlePacket(IoSession session, Packet packet)
    {
        int code = packet.getCode();
        
        if (!commands.containsKey(code))
        {
            System.err.println(getHandlerName() + "没有找到对应的Command code : "
                    + code + "(0x" + Integer.toHexString(code) + ")");
            return;
        }
        commands.get(code).execute(session, packet);
    }

    public final void handlePacket(Session session, Packet packet)
    {
        int code = packet.getCode();
        
        if (!webCommands.containsKey(code))
        {
            System.err.println(getHandlerName() + "没有找到对应的Command code : "
                    + code + "(0x" + Integer.toHexString(code) + ")");
            return;
        }
        webCommands.get(code).execute(session, packet);
    }
    
    public abstract String getHandlerName();
}
