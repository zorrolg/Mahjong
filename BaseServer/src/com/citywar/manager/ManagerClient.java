/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import com.citywar.BaseClient;
import com.citywar.define.ManagerPacketType;
import com.citywar.socket.Packet;

/**
 * 服务器端--管理者登录客户端
 * 
 * @author Dream
 * @date 2011-9-16
 * @version
 * 
 */
public class ManagerClient extends BaseClient
{
    private boolean isManager;

    public void setIsManager(boolean manager)
    {
        isManager = manager;
    }

    public boolean getIsManager()
    {
        return isManager;
    }

    
    public void displayMessage(String str)
    {
        // 管理客户端已经连接，将消息发送到管理客户端
        Packet pkg = new Packet(ManagerPacketType.MANAGER_MSG);
        pkg.putStr(str);
        sendTCP(pkg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dice.BaseClient#onConnect()
     */
    @Override
    public void onConnect()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dice.BaseClient#onDisconnect()
     */
    @Override
    public void onDisconnect()
    {
    }
}
