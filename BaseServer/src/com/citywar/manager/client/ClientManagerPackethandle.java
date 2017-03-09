/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager.client;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

import com.citywar.define.ManagerPacketType;
import com.citywar.manager.handle.ManagerReciveHandle;
import com.citywar.socket.Command;
import com.citywar.socket.Handler;
import com.citywar.socket.Packet;

/**
 * 客户端---协议处理连接的Handle
 * @author Dream
 * @date 2011-9-16
 * @version
 * 
 */
public class ClientManagerPackethandle implements Handler
{
    protected final HashMap<Short, Command> commands;

    /**
     * 管理的协议很少直接实例化
     */
    public ClientManagerPackethandle()
    {
        commands = new HashMap<Short, Command>();
        commands.put(ManagerPacketType.MANAGER_MSG,new ManagerReciveHandle());
    }

    @Override
    public void handlePacket(IoSession session, Packet packet)
    {
        short code = packet.getCode();
        Command cmd=commands.get(code);
        if(cmd!=null)
            cmd.execute(session, packet);
    }
}
