/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * 游戏难度等级定义
 * 
 * @author sky
 * @date 2011-05-04
 * @version
 * 
 */
public enum GameHardType
{
    /**
     * 简单
     */
    Simple(0),

    /**
     * 普通
     */
    Normal(1),

    /**
     * 困难
     */
    Hard(2),

    /**
     * 地域
     */
    Terror(3);

    private final byte value;

    private GameHardType(int value)
    {
        this.value = (byte) value;
    }

    public byte getValue()
    {
        return value;
    }

    public static GameHardType parseRoomType(int type)
    {
        for (GameHardType hardType : GameHardType.values())
        {
            if (type == hardType.value)
                return hardType;
        }

        return Simple;
    }
}
