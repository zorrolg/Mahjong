/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.PlayerState;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 保存用户超时之前保存的预设值
 * @author shanfeng.cao
 * @date 2012-04-20
 * @version 
 *
 */
@UserCmdAnnotation(code = UserCmdType.GAME_USE_TIMEOUT_CALL, desc = "用户超时之前保存的预设值")
public class GameUseTimeOutCallCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        BaseRoom curRoom = player.getCurrentRoom();
        if (curRoom == null || curRoom.getGame() == null)
            return 0;
        Player p = curRoom.getGame().getPlayers().get(player.getUserId());
        if (null == p || p.getPlayerState() != PlayerState.Playing) {
        	//添加逻辑如果不是正在游戏中的玩家不能保存用户超时之前保存的预设值
        	return 0;
        }
        //Player player = players.get(packet.getClientId());
        p.setDiceNumber(packet.getByte());
        p.setDicePoint(packet.getByte());
        p.setIsCallOne(packet.getBoolean());
        return 0;
    }

}
