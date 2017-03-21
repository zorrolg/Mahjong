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



public class ChargeMoneyListener implements IEventListener
{
	
	private IEventSource gamePlayerEvent;
	private UserTaskInfo userTaskInfo;
	private GamePlayer gamePlayer;
	
	
	/**
     * 初始化 
     * @param
     * @param
     */
    public ChargeMoneyListener(IEventSource gamePlayerEvent,UserTaskInfo userTask, GamePlayer gamePlayer)
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
		
		
		
		if(userTaskInfo.getTaskInfo().getTaskPara1() == 1)
		{
			userTaskInfo.setPara(para1 + userTaskInfo.getPara());
			int chargeIndex = userTaskInfo.getTaskFinishNum() + 1;
			userTaskInfo.setTaskFinishNum(chargeIndex > userTaskInfo.getTaskInfo().getTaskNum() ? userTaskInfo.getTaskInfo().getTaskNum() : chargeIndex);
			userTaskInfo.setOp(Option.UPDATE);
			
			if (chargeIndex >= userTaskInfo.getTaskInfo().getTaskNum()) {
				userTaskInfo.setTaskStatus(TaskMgr.TASK_STATUS_ALREADY_FINISH);
			    gamePlayerEvent.removeListener(TaskConditionType.ChargeMoney, this);
			}
		}
		else if(userTaskInfo.getTaskInfo().getTaskPara1() == 2)
		{
			int useMoneyTotal = userTaskInfo.getTaskFinishNum() + para1;
			if(useMoneyTotal > userTaskInfo.getTaskFinishNum())
			{
				//统一更新用户数值
				userTaskInfo.setTaskFinishNum(useMoneyTotal > userTaskInfo.getTaskInfo().getTaskNum() ? userTaskInfo.getTaskInfo().getTaskNum() : useMoneyTotal);
				userTaskInfo.setOp(Option.UPDATE);
				if (useMoneyTotal >= userTaskInfo.getTaskInfo().getTaskNum()) {// 如果加上这次操作数值还不够完成数量就更新+1并且return
						
					userTaskInfo.setTaskStatus(TaskMgr.TASK_STATUS_ALREADY_FINISH);
			    	gamePlayerEvent.removeListener(TaskConditionType.ChargeMoney, this);		
				}
			}
		}
		
		Packet packet = new Packet(UserCmdOutType.USER_FINISH_TASK);
    	gamePlayer.getOut().sendUserFinishOrNextTask(packet, userTaskInfo);
		
	}
}





