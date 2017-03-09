/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.manager.handle;

import org.apache.mina.core.session.IoSession;

import com.citywar.manager.ManagerClient;
import com.citywar.socket.Command;
import com.citywar.socket.Packet;

/**
 * @author Dream
 * @date 2011-9-18
 * @version 
 *
 */
public class ManagerLoginHandle implements Command
{

    @Override
    public void execute(IoSession session, Packet packet)
    {
        String key = packet.getStr();
        ManagerClient client=(ManagerClient)session.getAttribute(ManagerClient.class);
        
        // 验证管理客户端
        if (key.equals(client.getKey()))
        {
         // 标记管理客户端已经登录
            client.setIsManager(true);
        }
        else
        {
            client.disconnect();
        }
    }
}
