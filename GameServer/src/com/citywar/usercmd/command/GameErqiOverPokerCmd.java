/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.DiZhuCmdType;
import com.citywar.type.PlayerState;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 游戏内部逻辑处理
 * @author Dream
 * @date 2011-12-19
 * @version 
 *
 */
@UserCmdAnnotation(code = UserCmdType.GAME_ERQIOVER, desc = "游戏叫")
public class GameErqiOverPokerCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        BaseRoom curRoom = player.getCurrentRoom();
        if (curRoom == null || curRoom.getGame() == null)
            return 0;
        Player p = curRoom.getGame().getPlayers().get(player.getUserId());
        if (null == p || p.getPlayerState() != PlayerState.Playing) {
        	//添加逻辑如果不是正在游戏中的玩家不能开叫
        	return 0;
        }
        packet.setClientId(player.getUserId());
        packet.setParam1(DiZhuCmdType.SURRENDER);
        curRoom.processData(packet);
        return 0;
    }

}
