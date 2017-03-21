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


public class GameFinishListener implements IEventListener {

	
	private IEventSource gamePlayerEvent;
	private UserTaskInfo userTaskInfo;
	private GamePlayer gamePlayer;
	
	
	/**
     * 初始化 
     * @param
     * @param
     */
    public GameFinishListener(IEventSource gamePlayerEvent,UserTaskInfo userTask, GamePlayer gamePlayer)
    {
    	this.gamePlayerEvent = gamePlayerEvent;
    	this.userTaskInfo = userTask;
    	this.gamePlayer = gamePlayer;
    }
    
    
	@Override
	public void onEvent(Object EventArgs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(int para1, int para2) {
		// TODO Auto-generated method stub

		
		
		if(userTaskInfo.getTaskStatus() != TaskMgr.TASK_STATUS_NOT_FINISH)
			return;
		
		
		

		boolean isAddOne = false;
		if((userTaskInfo.getTaskInfo().getTaskPara1() == 0 || userTaskInfo.getTaskInfo().getTaskPara1() == para1)
				&& (userTaskInfo.getTaskInfo().getTaskPara2() == 0 || userTaskInfo.getTaskInfo().getTaskPara2() == para2))
		{
			isAddOne = true;
		}
		
		
		
		
		int finishNum = 0; // 最后设置的数值
		boolean isChange = false;
		boolean isFinish = false;
		if (isAddOne) {//如果标志位被设置成可以加一
			finishNum = userTaskInfo.getTaskFinishNum();//当前完成的次数
			++finishNum;
			//统一更新用户数值			
			if (finishNum >= userTaskInfo.getTaskInfo().getTaskNum()) {// 如果加上这次操作数值还不够完成数量就更新+1并且return
				isFinish = true;			//完成了任务可以继续更新任务状态
				finishNum = userTaskInfo.getTaskInfo().getTaskNum();
			}
			userTaskInfo.setTaskFinishNum(finishNum);
			userTaskInfo.setOp(Option.UPDATE);
			isChange = true;
		} else { // 说明不能讲数值加一
			if (userTaskInfo.getTaskInfo().getTaskIsContinuous() == 1) {//如果这里是连胜任务,需要重置数值
				finishNum = 0;//需要重置标志位数值
				//统一更新用户数值
				userTaskInfo.setTaskFinishNum(finishNum);
				userTaskInfo.setOp(Option.UPDATE);
				
				isChange = true;
			}
		}
		
		
		if (isFinish) {
			
			userTaskInfo.setTaskStatus(TaskMgr.TASK_STATUS_ALREADY_FINISH);
    		gamePlayerEvent.removeListener(TaskConditionType.FinishGame, this);

		}
		
		
		if(isChange)
		{
			Packet packet = new Packet(UserCmdOutType.USER_FINISH_TASK);
    		gamePlayer.getOut().sendUserFinishOrNextTask(packet, userTaskInfo);
		}
		
	}

}
