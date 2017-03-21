package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_EXIT_HALL, desc = "玩家退出大厅")
public class UserExitHallCmd extends AbstractUserCmd{

	@Override
	public int execute(GamePlayer player, Packet packet) {
		//
//		BaseHall hall = player.getCurrentHall();
//		Packet response = new Packet(UserCmdOutType.USER_EXIT_HALL);
//		if(null != hall) {
//			player.setCurrenetHall(null);
//			response.putBoolean(true);
//		} else {
//			response.putBoolean(false);
//		}
//		BaseRoom room = player.getCurrentRoom();
//		if (null != room)
//		{
//			room.removePlayer(player);
//		}
//		player.getOut().sendTCP(response);
		return 0;
	}
}
