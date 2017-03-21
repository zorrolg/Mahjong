package com.citywar.gameutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.citywar.bll.TaskBussiness;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.TaskInfo;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.event.BaseMultiEventSource;
import com.citywar.event.IEventSource;
import com.citywar.game.GamePlayer;
import com.citywar.manager.TaskMgr;
import com.citywar.task.AddFriendListener;
import com.citywar.task.CardDeveloperListener;
import com.citywar.task.CardFactoryListener;
import com.citywar.task.ChargeMoneyListener;
import com.citywar.task.CharmValueListener;
import com.citywar.task.DrunkUserListener;
import com.citywar.task.FinishBuildListener;
import com.citywar.task.FinishStageListener;
import com.citywar.task.GameCountListener;
import com.citywar.task.GameFinishListener;
import com.citywar.task.InviteGameListener;
import com.citywar.task.LoginGameListener;
import com.citywar.task.UpdateInfoListener;
import com.citywar.task.UseMoneyListener;
import com.citywar.task.WinCoinListener;
import com.citywar.type.TaskConditionType;
import com.citywar.type.TaskType;


/**
 * 玩家正在做的任务--专门用户管理用户任务系统
 * @author Jacky.zheng
 *
 */
public class PlayerTask {
	
	private static final Logger logger = Logger.getLogger(PlayerTask.class.getName());
	
	private Map<Integer, UserTaskInfo> taskMap;//Key-UserTaskInfo的id
	
	private GamePlayer gamePlayer;
	
	private Object lock = new Object();
	
	private Object lockEvent = new Object();
	/**
     * 用户事件处理接口
     */
    private IEventSource gamePlayerEvent;
    
    public IEventSource getEvent()
    {
        return gamePlayerEvent;
    }
    
    
	public PlayerTask() {
	}
	
	public PlayerTask(GamePlayer gamePlayer) {
		this.taskMap = new LinkedHashMap<Integer, UserTaskInfo>();
		this.gamePlayer = gamePlayer;
		
		// 玩家事件初始化
        this.gamePlayerEvent = new BaseMultiEventSource();
	}
	
	
	
	public void onTaskUpdate(int taskType, int para1, int para2)
    {
		synchronized (lockEvent) {
			gamePlayerEvent.notifyListener(taskType, para1, para2);
		}
    }
	
	
	/**
	 * 添加用户正在完成的任务信息
	 * @param UserTaskInfo userTaskInfo
	 */
	public UserTaskInfo addPlayerTask(TaskInfo tempInfo) {
		
		
		UserTaskInfo userTaskInfo = null;
		try {
			if (null == tempInfo) {
				return userTaskInfo;
			}
			synchronized (lock) {
				
				
				if(taskMap.containsKey(tempInfo.getTaskId()))
				{
					if(tempInfo.getTaskType() == TaskType.DayTask.getValue())
					{
						userTaskInfo = taskMap.get(tempInfo.getTaskId());
						userTaskInfo.setTaskFinishNum(0);
						userTaskInfo.setTaskStatus(TaskMgr.TASK_STATUS_NOT_FINISH);
						userTaskInfo.setOp(Option.UPDATE);
						
						addLister(userTaskInfo);
					
					}
				}
				else
				{
					userTaskInfo = new UserTaskInfo(gamePlayer.getUserId(), tempInfo);
					TaskBussiness.insertUserCounldCompletedTask(userTaskInfo);
					userTaskInfo.setOp(Option.UPDATE);
					
					insertUserTask(userTaskInfo,true);
				}
				
				
				if(userTaskInfo != null && userTaskInfo.getId() <= 0)
					System.out.println("userTaskInfo=====================================" + userTaskInfo.getId());
				
			}
		} catch (Exception e) {
			logger.error("[ PlayerTask : addPlayTask ]", e);
			
		} finally {
			
		}
		return userTaskInfo;
	}
	

	
	
	/**
	 * 加载玩家所有未完成的任务信息
	 * @param list
	 * @author shanfeng.cao
	 * @date 2012-04-28
	 */
	public void loadPlayerTask(List<UserTaskInfo> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		
		
		try {
			
			boolean haveStageTask = false;
			boolean haveTrainTask = false;
			Map<Integer, Integer> dayTaskMap = new HashMap<Integer, Integer>();			
            for (UserTaskInfo userTaskInfo : list) {
            	
            	TaskInfo tempTask = TaskMgr.getTaskInfoByTaskId(userTaskInfo.getTaskId());
            	
            	if(tempTask != null)
            	{
            		userTaskInfo.setTaskInfo(tempTask);
                	insertUserTask(userTaskInfo,false);//taskMap.put(userTaskInfo.getTaskId(), userTaskInfo);
                	
                	//判断任务完成状态
                	if(userTaskInfo.getTaskStatus() == TaskMgr.TASK_STATUS_NOT_FINISH || userTaskInfo.getTaskStatus() == TaskMgr.TASK_STATUS_ALREADY_FINISH)    
                	{
                		addLister(userTaskInfo);
                		
                		if(userTaskInfo.getTaskInfo().getTaskId() <= TaskMgr.freshUserTaskIds[TaskMgr.freshUserTaskIds.length - 1])
                    		haveTrainTask = true;
                	}
                		
                	
            		if(userTaskInfo.getTaskInfo().getTaskType() == TaskType.DayTask.getValue() && userTaskInfo.getTaskStatus() != TaskMgr.TASK_STATUS_ALREADY_GET_PRIZE)
            			dayTaskMap.put(userTaskInfo.getTaskInfo().getTaskStatus(), 0);
                	
            		
            		
                	if(userTaskInfo.getTaskInfo().getStage() > 0)
                	{
                		haveStageTask = true;            		
                		if(userTaskInfo.getTaskInfo().getTaskCondition() == TaskConditionType.FinishStage 
                				&& userTaskInfo.getTaskStatus() == TaskMgr.TASK_STATUS_ALREADY_GET_PRIZE
                				&& userTaskInfo.getTaskInfo().getStage() + 1 > gamePlayer.getPlayerInfo().getStageId())
                			gamePlayer.getPlayerInfo().setStageId(userTaskInfo.getTaskInfo().getStage() + 1);
                	}
            	}
            }
            
            

            if(gamePlayer.getPlayerInfo().getLastQiutDate() == null             		
            		|| gamePlayer.getPlayerInfo().getLastLoginDate().before(GamePlayerUtil.getTaskResetTime()))
            {
            	
            	int dayTaskIndex = haveTrainTask ? 0 : 6;
            	UserTaskInfo userTaskInfo = null;
            	for (int i = 0; i < dayTaskIndex; i++) {       		
            		            		            		
            		if(!dayTaskMap.containsKey(i))
            		{
            			
            			//设置下一个日常任务
						int preTaskId = 0;//0 表示没有前置任务的日常任务，就是说是第一个
						TaskInfo tempInfo = TaskMgr.getUserOneTasksOfDay(i, preTaskId);// i 表示日常任务种类
						if(tempInfo != null) {//如果此类任务还没完成，今天还有此类任
														
							if(taskMap.containsKey(tempInfo.getTaskId()))
							{
								userTaskInfo = taskMap.get(tempInfo.getTaskId());
								
								if(userTaskInfo.getTaskStatus() == TaskMgr.TASK_STATUS_ALREADY_GET_PRIZE
										&& userTaskInfo.getFinishTime().before(GamePlayerUtil.getTaskResetTime()))
								{
									userTaskInfo.setTaskFinishNum(0);
									userTaskInfo.setTaskStatus(TaskMgr.TASK_STATUS_NOT_FINISH);
									userTaskInfo.setOp(Option.UPDATE);
									
									addLister(userTaskInfo);
								}
							}
							else
							{
								userTaskInfo = addPlayerTask(tempInfo);
//								userTaskInfo = new UserTaskInfo(gamePlayer.getUserId(), tempInfo);								
//								TaskBussiness.insertUserCounldCompletedTask(userTaskInfo);
//								insertUserTask(userTaskInfo,true);
//			            		userTaskInfo.setOp(Option.UPDATE);//新加进去的任务一定要记得设置状态
							}
						
						}
            		}
				}
            }
            
            
            
            if(!haveStageTask)
            {
            	List<TaskInfo> taskStage = TaskMgr.getALLStageTasks(1);
                for(TaskInfo task : taskStage)
                {
                	addPlayerTask(task);
//                	UserTaskInfo userTask = new UserTaskInfo(gamePlayer.getUserId(), task);
//                    TaskBussiness.insertUserCounldCompletedTask(userTask);
//                    insertUserTask(userTask,true);
                }
            }
            
            
            
            if(haveTrainTask == false)
            {
            	List<TaskInfo> taskActivity =  TaskMgr.getActivityTasks();         
                for(TaskInfo task : taskActivity)
                {                		
                	addPlayerTask(task);
                }
            }
            
            
            
            
            
            
            
        } catch (Exception e) {
            logger.error("[ PlayerTask : loadPlayerTask ]", e);
        } finally {
        	
        }
	}
	
	
	public void insertUserTask(UserTaskInfo userTask, boolean isAddListen)
	{
		taskMap.put(userTask.getTaskId(), userTask);
		
		if(isAddListen)
			addLister(userTask);
		
		
	}
	
	private void addLister(UserTaskInfo userTask)
	{
		
		//System.out.println("addLister" + "========" + gamePlayer.getPlayerInfo().getUserId() + "===" + userTask.getTaskId());

		switch (userTask.getTaskInfo().getTaskCondition()) {
		
	    	case TaskConditionType.FinishGame:
	    		gamePlayerEvent.addListener(TaskConditionType.FinishGame, new GameFinishListener(gamePlayerEvent,userTask,gamePlayer));
	    		break;
	    		
	    	case TaskConditionType.LoginGame:
	    		gamePlayerEvent.addListener(TaskConditionType.LoginGame, new LoginGameListener(gamePlayerEvent,userTask,gamePlayer));
	    		break;
	
	    	case TaskConditionType.UpdateInfo:
	    		gamePlayerEvent.addListener(TaskConditionType.UpdateInfo, new UpdateInfoListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
	
	    	case TaskConditionType.AddFriend:
	    		gamePlayerEvent.addListener(TaskConditionType.AddFriend, new AddFriendListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
	
	    	case TaskConditionType.InviteGame:
	    		gamePlayerEvent.addListener(TaskConditionType.InviteGame,new InviteGameListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
	
	    	case TaskConditionType.DrunkUser:
	    		gamePlayerEvent.addListener(TaskConditionType.DrunkUser,new DrunkUserListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
	
	    	case TaskConditionType.FinishBuild:
	    		gamePlayerEvent.addListener(TaskConditionType.FinishBuild,new FinishBuildListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
	
	    	case TaskConditionType.CharmValue:
	    		gamePlayerEvent.addListener(TaskConditionType.CharmValue,new CharmValueListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
		    
	    	case TaskConditionType.ChargeMoney:
	    		gamePlayerEvent.addListener(TaskConditionType.ChargeMoney,new ChargeMoneyListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
	    		
	    	case TaskConditionType.UseMoney:
	    		gamePlayerEvent.addListener(TaskConditionType.UseMoney,new UseMoneyListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
		    
	    	case TaskConditionType.CardDeveloper:
	    		gamePlayerEvent.addListener(TaskConditionType.CardDeveloper,new CardDeveloperListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
		    
	    	case TaskConditionType.CardFactory:
	    		gamePlayerEvent.addListener(TaskConditionType.CardFactory,new CardFactoryListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
		    
	    	case TaskConditionType.FinishStage:
	    		gamePlayerEvent.addListener(TaskConditionType.FinishStage,new FinishStageListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
	    		
	    	case TaskConditionType.WinCoin:
	    		gamePlayerEvent.addListener(TaskConditionType.WinCoin,new WinCoinListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
	    		
	    	case TaskConditionType.GameCount:
	    		gamePlayerEvent.addListener(TaskConditionType.GameCount,new GameCountListener(gamePlayerEvent,userTask,gamePlayer));
	    		break ;
	    		
		    
	    	default:
	            break;
	    }
		
	}
	
	
	
	/**
	 * 根据用户任务Map是否有某个KeyId
	 * @param taskId
	 * @return
	 */
	public boolean isHaveUserTaskInfoKey(int userTaskKeyId) {
		synchronized (lock) {
            return taskMap.containsKey(userTaskKeyId);
        }
	}
	
	/**
	 * 获取用户当前所有任务
	 * @return
	 */
	public List<UserTaskInfo> getUserALLTasks() {
		List<UserTaskInfo> list = new ArrayList<UserTaskInfo>();
        synchronized (lock) {
            for (Entry<Integer, UserTaskInfo> entry : taskMap.entrySet()) {
            	if(entry.getValue().getTaskInfo().getStage() == 0)
            		list.add(entry.getValue());
            }
        }
        return list;
	}
	
	
	
	
	/**
	 * 获取用户当前拥有的任务
	 * @return
	 */
	public List<UserTaskInfo> getUserCurrentTasks() {
		List<UserTaskInfo> list = new ArrayList<UserTaskInfo>();
        synchronized (lock) {
            for (Entry<Integer, UserTaskInfo> entry : taskMap.entrySet()) {
            	
            	if(entry.getValue().getTaskInfo().getStage() == 0)
            	{
            		if (entry.getValue().getTaskStatus() == TaskMgr.TASK_STATUS_NOT_FINISH
                			|| entry.getValue().getTaskStatus() == TaskMgr.TASK_STATUS_ALREADY_FINISH ) {
                		list.add(entry.getValue());
                	}
            	}
            	
            }
        }
        return list;
	}
	
	/**
	 * 获取用户当前拥有的任务
	 * @return
	 */
	public List<UserTaskInfo> getUserStageTasks(int stage) {
		List<UserTaskInfo> list = new ArrayList<UserTaskInfo>();
        synchronized (lock) {
            for (Entry<Integer, UserTaskInfo> entry : taskMap.entrySet()) {
            	
            	if(entry.getValue().getTaskInfo().getStage() == stage)
            	{
            		list.add(entry.getValue());	            	
            	}
            }
        }
        return list;
	}
	
	
	public void saveIntoDB() {
		List<UserTaskInfo> updateTasks = new ArrayList<UserTaskInfo>();
        List<UserTaskInfo> insertTasks = new ArrayList<UserTaskInfo>();
        synchronized (lock) {
            for (UserTaskInfo info : taskMap.values()) {
                if (info == null)
                    continue;
                switch (info.getOp()) {
                    case Option.INSERT:
                    	insertTasks.add(info);
                        break;

                    case Option.UPDATE:
                    	updateTasks.add(info);
                        break;
                    default:
                        break;
                }
                info.setOp(Option.NONE);
            }
        }
        TaskBussiness.addUserTasks(insertTasks);
//        changeUserTaskIdToKey();
        TaskBussiness.updateUserTaskStatusAndNum(updateTasks);
	}
	

	
	
	/**
	 * 通过用户任务id查找用户做过的任务信息
	 * @return
	 */
	public UserTaskInfo getUserTaskInfoByUserTaskId(int taskId) {
		
		UserTaskInfo info = null;
		
        synchronized (lock) {
        	
        	if(taskId != 0)
        	{
	            for (Entry<Integer, UserTaskInfo> entry : taskMap.entrySet()) {
	            	if (entry.getValue().getTaskInfo() != null && entry.getValue().getId() == taskId) {
	            		info = entry.getValue();
	            		break ;
	            	}
	            }
        	}
        }
        return info;
	}
		
	
	
	/**
	 * 根据用户任务Id获取玩家任务信息
	 * @param taskId
	 * @return
	 */
	public UserTaskInfo getUserTaskInfoByTaskId(int userTaskId) {
		
		synchronized (lock) {
			return taskMap.get(userTaskId);
		}
		
	}
	
}
