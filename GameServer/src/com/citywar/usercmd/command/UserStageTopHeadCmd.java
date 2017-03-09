package com.citywar.usercmd.command;


import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.UserTopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.STAGE_TOP_HEAD, desc = "玩家当前正在做的任务")
public class UserStageTopHeadCmd extends AbstractUserCmd{

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		
		Packet pkg = new Packet(UserCmdOutType.STAGE_TOP_HEAD);
		UserTopMgr.getAllStageHead(pkg);
        player.getOut().sendTCP(pkg);
         
         
		return 0;
	}
}
