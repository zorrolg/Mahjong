/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.define;

/**
 * 服务器相关事件定义
 * 
 * @author sky
 * @date 2011-6-16
 * @version
 * 
 */
public enum ServerEventType
{
    /**
     * 连接事件
     */
    CONNECT(0),

    /**
     * 断开事件
     */
    DISCONNECT(1);

    private final byte eventId;

    private ServerEventType(int eventId)
    {
        this.eventId = (byte) eventId;
    }

    public byte getValue()
    {
        return eventId;
    }
}
