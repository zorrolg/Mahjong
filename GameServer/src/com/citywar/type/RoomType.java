/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * 房间类型定义
 * @author dream
 * @date 2012-2-2
 * @version 
 *
 */
public interface RoomType
{
    /**
     * 竞技(快速加入，随机加入房间)
     */
    public static final int Match=0;
    
    /**
     * 房间等级-啤酒
     */
    public static final int Beer=3;
    
    /**
     * 房间等级-红酒
     */
    public static final int Wine=4;
    
    /**
     * 房间等级-烈酒
     */
    public static final int FireWater=5;
    
    /**
     * 机器人 专用房间
     */
    public static final int ROB_ROOM = 6;
}

