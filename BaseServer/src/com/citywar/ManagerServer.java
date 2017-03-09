/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar;

import com.citywar.manager.ManagerClient;
import com.citywar.manager.ManagerHandle;
import com.citywar.socket.Handler;
import com.citywar.util.Config;

/**
 * 管理客户端监听服务器
 * 
 * @author sky
 * @date 2011-9-23
 * @version
 * 
 */
public class ManagerServer extends BaseServer
{
    private Handler handler;

    public ManagerServer()
    {
        super();
        handler = new ManagerHandle();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dice.BaseServer#getHandler()
     */
    @Override
    public Handler getHandler()
    {
        return handler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dice.BaseServer#getClient()
     */
    @Override
    public BaseClient getClient()
    {
        return new ManagerClient();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dice.BaseServer#start()
     */
    @Override
    public boolean start()
    {
        try
        {
            int port = Config.getValue("manager.port") == null ? 8008
                    : Integer.valueOf(Config.getValue("manager.port"));
            startListen(port);
            return true;
        }
        catch (Exception ex)
        {
            LOGGER.error("ManagerServer::start", ex);
            return false;
        }
    }
}
