/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.define;

/**
 * 管理客户端与服务器之间的协议
 * @author Dream
 * @date 2011-9-18
 * @version 
 *
 */
public interface ManagerPacketType
{
    /**
     *  管理登陆
     */
    public static final short MANAGER_LOGIN = 0xFF;

    /**
     *  管理命令
     */
    public static final short MANAGER_CMD = 0xFE;
    
    /**
     * 管理消息
     */
    public final static short MANAGER_MSG = 0xFD;
}
