/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * @author : Cookie
 * @date : 2011-5-13
 * @version 游戏内事件
 */
public enum GameEventType
{
    /**
     * 房间重置
     */
    RoomRest(0),

    /**
     * 游戏开始
     */
    GameStarted(1),

    /**
     * 游戏停止
     */
    GameStopped(2),

    /**
     * 
     */
    GameOverred(3),

    /**
     * 开始下一轮
     */
    BeginNewTurn(4),

    /**
     * 游戏结束日志
     */
    GameOverLog(5),

    /**
     * 师徒站，夫妻站日志
     */
    SpecialFightLogEvent(6),

    /**
     * NPC死亡
     */
    GameNpcDie(7);

    private final byte value;

    private GameEventType(int value)
    {
        this.value = (byte) value;
    }

    public byte getValue()
    {
        return value;
    }
}
