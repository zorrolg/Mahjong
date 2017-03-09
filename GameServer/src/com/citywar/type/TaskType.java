/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * @author : Cookie
 * @date : 2011-5-13
 * @version 游戏内事件
 */

public enum TaskType
{
    TrainTask(0),
    NormalTask(1), 
    DayTask(2), 
    ActivityTask(3);

    private final byte value;

    private TaskType(int value)
    {
        this.value = (byte) value;
    }

    public byte getValue()
    {
        return value;
    }
}

