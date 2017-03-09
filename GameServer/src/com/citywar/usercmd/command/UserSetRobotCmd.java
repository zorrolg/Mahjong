package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.RoomMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.GAME_ROBOT_STATE, desc = "玩家当前正在做的任务")
public class UserSetRobotCmd extends AbstractUserCmd{

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
						
		boolean isRobotState = packet.getBoolean();		
		        
        RoomMgr.handlePlayerTrustee(player.getCurrentRoom(), player, isRobotState);

        
		return 0;
	}
}
