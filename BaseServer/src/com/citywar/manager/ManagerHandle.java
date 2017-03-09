/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

import com.citywar.define.ManagerPacketType;
import com.citywar.manager.handle.ManagerCmdHandle;
import com.citywar.manager.handle.ManagerLoginHandle;
import com.citywar.socket.Command;
import com.citywar.socket.Handler;
import com.citywar.socket.Packet;

/**
 * 服务器端---管理者登录，处理命令的Handle
 * 
 * @author Dream
 * @date 2011-9-16
 * @version
 * 
 */
public class ManagerHandle implements Handler
{
    private final HashMap<Short, Command> commands;

    /**
     * 管理者协议很少，直接实例化
     */
    public ManagerHandle()
    {
        commands = new HashMap<Short, Command>();
        // 管理者登录
        commands.put(ManagerPacketType.MANAGER_LOGIN, new ManagerLoginHandle());
        // 管理者命令
        commands.put(ManagerPacketType.MANAGER_CMD, new ManagerCmdHandle());
    }

    @Override
    public void handlePacket(IoSession session, Packet packet)
    {
        short code = packet.getCode();
        Command cmd = commands.get(code);
        if (cmd != null)
            cmd.execute(session, packet);
    }
}
