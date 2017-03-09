/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.room.action;

import java.sql.Timestamp;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.room.BaseRoom;

/**
 * 玩家退出房间
 * @author Dream
 * @date 2011-12-22
 * @version 
 *
 */
public class ExitRoomAction extends AbstractRoomAction
{
	
    BaseRoom room;
    GamePlayer player;
    boolean isStageGame;
    int isPengQuit;
    
    public ExitRoomAction(BaseRoom room, GamePlayer player, boolean isStageGame, int pengQuitType)
    {
        this.room = room;
        this.player = player;
        this.isStageGame = isStageGame;
        this.isPengQuit = pengQuitType;
    }

    public void execute()
    {
        room.removePlayer(player, false, false, this.isStageGame, this.isPengQuit);
        player.addAlreadyRoomId(room.getRoomId(), new Timestamp(System.currentTimeMillis()));
    }
}
