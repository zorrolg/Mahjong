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
import com.citywar.util.TimeUtil;



public class LoginGameListener implements IEventListener
{
	
	private IEventSource gamePlayerEvent;
	private UserTaskInfo userTaskInfo;
	private GamePlayer gamePlayer;
	
	
	/**
     * 初始化 
     * @param
     * @param
     */
    public LoginGameListener(IEventSource gamePlayerEvent,UserTaskInfo userTask, GamePlayer gamePlayer)
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
	@SuppressWarnings("deprecation")
	public void onEvent(int para1, int para2) {
		// TODO Auto-generated method stub
		
		
		if(userTaskInfo.getTaskStatus() != TaskMgr.TASK_STATUS_NOT_FINISH)
			return;
		
		
		
		
		boolean isFinished = false;
		if(userTaskInfo.getTaskInfo().getTaskPara1() == 0)
			isFinished = true;
		
		if(userTaskInfo.getTaskInfo().getTaskPara1() == 1 && gamePlayer.getPlayerInfo().getVipLevel() >= userTaskInfo.getTaskInfo().getTaskPara2())
			isFinished = true;
				 
		if(userTaskInfo.getTaskInfo().getTaskPara1() == 2)
		{
			
			int nowHour = TimeUtil.getSysteCurTime().getHours();
			int beginHour = userTaskInfo.getTaskInfo().getBeginDate().getHours();
			int endHour = userTaskInfo.getTaskInfo().getEndDate().getHours();
			if(nowHour >= beginHour && nowHour <= endHour)
				isFinished = true;
		}
		
		
		
		
		
		if(isFinished)
		{
			//统一更新用户数值
			userTaskInfo.setTaskFinishNum(userTaskInfo.getTaskInfo().getTaskNum());
			userTaskInfo.setOp(Option.UPDATE);
			userTaskInfo.setTaskStatus(TaskMgr.TASK_STATUS_ALREADY_FINISH);
			
		    gamePlayerEvent.removeListener(TaskConditionType.LoginGame, this);
		    		
			Packet packet = new Packet(UserCmdOutType.USER_FINISH_TASK);
	    	gamePlayer.getOut().sendUserFinishOrNextTask(packet, userTaskInfo);
		}
		
	}
}





