/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * the all kinds of state during the game process.
 * @author Dream
 * @date 2011-12-20
 * @version 
 */
public class PlayerState
{
    /**
     * player is idle
     */
    public static final byte Idle = 0;
    /**
     * the state during player is shaking the cellphone
     */
    public static final byte BeginShake = 1;
    /**
     * the state after player shaked the cellphone
     */
    public static final byte EndShaked = 2;
    /**
     * the state player is playing game.
     */
    public static final byte Beting = 3;
    /**
     * the state player is playing game.
     */
    public static final byte Playing = 4;
    /**
     * the state that player outlines and trustee the machine to play.
     */
    public static final byte Trustee=5;
    /**
     * the state that player on game, and game is open.
     */
    public static final byte Open=6;
    /**
     * the state that player on game, and game is open.
     */
    public static final byte AllIn=7;
}
