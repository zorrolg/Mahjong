/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager.handle;

import org.apache.mina.core.session.IoSession;

import com.citywar.commands.ConsoleCommandMgr;
import com.citywar.manager.ManagerClient;
import com.citywar.socket.Command;
import com.citywar.socket.Packet;

/**
 * @author Dream
 * @date 2011-9-16
 * @version
 * 
 */
public class ManagerCmdHandle implements Command
{
    @Override
    public void execute(IoSession session, Packet packet)
    {
        ManagerClient client = (ManagerClient) session.getAttribute(ManagerClient.class);

        if (!client.getIsManager())
            // 管理客户端尚未登录
            return;

        String cmd = packet.getStr();
        try
        {
            // 取得，执行命令
            boolean res = ConsoleCommandMgr.handleCommand(client, cmd);
            if (!res)
            {
                client.displayMessage("Unknown command: " + cmd);
            }
        }
        catch (Exception ex)
        {
            client.displayMessage("error:" + ex.getMessage());
        }
    }
}
