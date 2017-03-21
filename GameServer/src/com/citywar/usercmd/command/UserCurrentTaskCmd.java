package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_CURRENT_TASK, desc = "玩家当前正在做的任务")
public class UserCurrentTaskCmd extends AbstractUserCmd{

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		player.isFinishTask(TaskConditionType.LoginGame, 0, 0);
		
		List<UserTaskInfo> list = player.getPlayerTask().getUserCurrentTasks();
		player.getOut().sendUserCurrentTask(list, false);
		
		return 0;
	}
}
