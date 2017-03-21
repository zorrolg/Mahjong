package com.citywar.task;

import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.event.IEventListener;
import com.citywar.event.IEventSource;
import com.citywar.game.GamePlayer;
import com.citywar.manager.TaskMgr;
import com.citywar.socket.Packet;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdOutType;



public class InviteGameListener implements IEventListener
{
	
	private IEventSource gamePlayerEvent;
	private UserTaskInfo userTaskInfo;
	private GamePlayer gamePlayer;
	
	
	/**
     * 初始化 
     * @param
     * @param
     */
    public InviteGameListener(IEventSource gamePlayerEvent,UserTaskInfo userTask, GamePlayer gamePlayer)
    {
    	this.gamePlayerEvent = gamePlayerEvent;
    	this.userTaskInfo = userTask;
    	this.gamePlayer = gamePlayer;
    }
    
    
    @Override
    public void onEvent(Object EventArgs){
    	// TODO Auto-generated method stub
    }

	@Override
	public void onEvent(int para1, int para2) {
		// TODO Auto-generated method stub
		
		
		if(userTaskInfo.getTaskStatus() != TaskMgr.TASK_STATUS_NOT_FINISH)
			return;
		
		
		
		int finishNum = userTaskInfo.getTaskFinishNum();//当前完成的次数
		++finishNum;
		
		//统一更新用户数值
		userTaskInfo.setTaskFinishNum(finishNum > userTaskInfo.getTaskInfo().getTaskNum() ? userTaskInfo.getTaskInfo().getTaskNum() : finishNum);
		userTaskInfo.setOp(Option.UPDATE);
		if (finishNum >= userTaskInfo.getTaskInfo().getTaskNum()) {// 如果加上这次操作数值还不够完成数量就更新+1并且return
				
			userTaskInfo.setTaskStatus(TaskMgr.TASK_STATUS_ALREADY_FINISH);
	    	gamePlayerEvent.removeListener(TaskConditionType.InviteGame, this);
	    		
		}
		
		Packet packet = new Packet(UserCmdOutType.USER_FINISH_TASK);
    	gamePlayer.getOut().sendUserFinishOrNextTask(packet, userTaskInfo);
		
	}
}





