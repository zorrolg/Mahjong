/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar;

/**
 * shutdown hooker
 * 
 * @author sky
 * @date 2011-5-30
 * @version
 * 
 */
public class BaseShutdownHooker extends Thread
{
    private BaseServer server;

    public BaseShutdownHooker(BaseServer server)
    {
        this.server = server;
    }

    /**
     * 退出回调，停止服务器
     */
    public void run()
    {
        if(server != null){
            server.stop();
        }
    }
}
