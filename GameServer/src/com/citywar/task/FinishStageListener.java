package com.citywar.task;

import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.event.IEventListener;
import com.citywar.event.IEventSource;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.TaskMgr;
import com.citywar.socket.Packet;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdOutType;



public class FinishStageListener implements IEventListener
{
	
	private IEventSource gamePlayerEvent;
	private UserTaskInfo userTaskInfo;
	private GamePlayer gamePlayer;
	
	
	/**
     * 初始化 
     * @param
     * @param
     */
    public FinishStageListener(IEventSource gamePlayerEvent,UserTaskInfo userTask, GamePlayer gamePlayer)
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
		
		
		
		
		int finishedTaskNum = 0;
		String[] typeArray = userTaskInfo.getTaskInfo().getTaskPara3().split(",");
		for (int i = 0; i < typeArray.length; i++) {
			int taskId = Integer.valueOf(typeArray[i]);
			
			UserTaskInfo task = gamePlayer.getPlayerTask().getUserTaskInfoByTaskId(taskId);
			
			if(task != null && task.getTaskStatus() == TaskMgr.TASK_STATUS_ALREADY_GET_PRIZE)
			{
				finishedTaskNum += 1;
			}
		}
		
		
		
		
		
		if(finishedTaskNum > userTaskInfo.getTaskFinishNum())
		{

				//统一更新用户数值
				userTaskInfo.setTaskFinishNum(finishedTaskNum);
				userTaskInfo.setOp(Option.UPDATE);
				if (finishedTaskNum >= typeArray.length) {// 如果加上这次操作数值还不够完成数量就更新+1并且return
						
					userTaskInfo.setTaskStatus(TaskMgr.TASK_STATUS_ALREADY_FINISH);
			    	gamePlayerEvent.removeListener(TaskConditionType.FinishStage, this);
			    	
			    	
//			    	int newStageId = userTaskInfo.getTaskInfo().getStage() + 1;
//			    	gamePlayer.getPlayerInfo().setStageId(newStageId);			    	
//			    	PlayerStage newStage = new PlayerStage(gamePlayer.getUserId(),newStageId,0);
//			    	newStage.setOp(Option.INSERT);
//			    	gamePlayer.stageAdd(newStageId , newStage);
//			    	   	
//			    	
//			    	List<TaskInfo> taskStage = TaskMgr.getALLStageTasks(newStageId);
//	                for(TaskInfo task : taskStage)
//	                {
//	                	UserTaskInfo userTask = new UserTaskInfo(gamePlayer.getUserId(), task);
//	                    TaskBussiness.insertUserCounldCompletedTask(userTask);
//	                }
//	                
//			    	gamePlayer.getOut().sendStageCharmValve(newStageId, 0);
//			    	gamePlayer.getOut().sendUpdateBaseInfo();
				}
				
				Packet packet = new Packet(UserCmdOutType.USER_FINISH_TASK);
		    	gamePlayer.getOut().sendUserFinishOrNextTask(packet, userTaskInfo);
		    	
			
		}
		
		
		
		
	}
}





