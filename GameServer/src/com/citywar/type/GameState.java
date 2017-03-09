/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * 当前游戏状态
 * @author Dream
 * @date 2011-12-15
 * @version 
 *
 */
public enum GameState
{
    Created(0),
    Prepared(1), 
    Beting(2),
    AfterBeting(3),
    ExPlaying(4), 
    Playing(5), 
    GameOver(6), 
    Stopped(7),
    Wait(8);

    private final byte value;

    private GameState(int value)
    {
        this.value = (byte) value;
    }

    public byte getValue()
    {
        return value;
    }
    
    public boolean isInPlay()
    {
    	if(this.value == 2 || this.value == 3 || this.value == 4 || this.value == 5)
    		return true;
    	else 
    		return false;
    }
}
