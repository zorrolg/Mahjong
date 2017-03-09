/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * tracy
 * 
 * @author 道具相关
 * @date 2012-1-4
 * @version
 * 
 */
public enum GamePlayerEndState
{
    NoJoin(0),
    Win(1), 
    Lose(2), 
    Draw(3);

    private final byte value;

    private GamePlayerEndState(int value)
    {
        this.value = (byte) value;
    }

    public byte getValue()
    {
        return value;
    }
}



