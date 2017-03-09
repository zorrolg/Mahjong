/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.manager.handle;
import org.apache.mina.core.session.IoSession;

import com.citywar.socket.Command;
import com.citywar.socket.Packet;

/**
 * 返回给客户端命令执行结果
 * @author Dream
 * @date 2011-9-16
 * @version 
 *
 */
public class ManagerReciveHandle implements Command
{
    @Override
    public void execute(IoSession session, Packet packet)
    {
        System.out.println(packet.getStr());
        System.out.print(">");
    }
}
