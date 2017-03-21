package com.citywar.usercmd.command;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.TaskInfo;
import com.citywar.dice.entity.TaskPrize;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.TaskMgr;
import com.citywar.socket.Packet;
import com.citywar.type.TaskConditionType;
import com.citywar.type.TaskType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.GET_TASK_PRIZE, desc = "用户领取奖励")
public class UserGetTaskPrize extends AbstractUserCmd {

	@Override
	public int execute(GamePlayer player, Packet packet) {
		List<UserTaskInfo> nextTasksList = new ArrayList<UserTaskInfo>();
		boolean getPrize = false;
		//int taskId = packet.getInt();
		int totalCoin = 0;
		int userTaskId = packet.getInt();
		if (userTaskId > 0) {
//			PacketLib packetLib = new PacketLib(player);
			
				UserTaskInfo userTaskInfo = player.getPlayerTask().getUserTaskInfoByUserTaskId(userTaskId);
				if (null != userTaskInfo && userTaskInfo.getTaskStatus() == TaskMgr.TASK_STATUS_ALREADY_FINISH) {
					//判断任务是否已经完成
					TaskInfo taskInfo = userTaskInfo.getTaskInfo();
					if (null != taskInfo) {
						//添加任务奖励
						PlayerInfo playerInfo = (player != null ? player.getPlayerInfo() : null);
						if (null != playerInfo) {
							List<TaskPrize> list  = taskInfo.getPrizeList();
				    		if (null != list && list.size() > 0) {
				    			for (TaskPrize prize : list) {
				    				if (prize.getPrizeType() == 0) {//游戏币奖励
				    					totalCoin += prize.getPrizeNum();
				    					player.addCoins(prize.getPrizeNum());
				    				} else if (prize.getPrizeType() == -1) { //经验奖励
				    					player.addMoney(prize.getPrizeNum());
				    				} else if (prize.getPrizeType() == -2) { //经验奖励
				    					player.addMoney(userTaskInfo.getPara() * prize.getPrizeNum() / 100);
				    				}else if (prize.getPrizeType() > 10000) { //醒酒茶
				    					player.getPropBag().addPrizeItemCount(prize.getPrizeType(), prize.getPrizeNum());
				    				} 
				    			}
				    		}
						}
						
						
//						System.out.println("UserGetTaskPrize======" + totalCoin);
						
						//设置任务状态为已经领取
						userTaskInfo.setTaskStatus(TaskMgr.TASK_STATUS_ALREADY_GET_PRIZE);
						userTaskInfo.setFinishTime(new Timestamp(System.currentTimeMillis()));
						//设置为更新
						getPrize = true;
	            		userTaskInfo.setOp(Option.UPDATE);
	            		
						player.getOut().sendUpdateBaseInfo();
						player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
						
						//设置下一个任务--这里移到cmd命令里领取任务奖励的时候再设置下一个任务
						UserTaskInfo userNextTaskInfo = null;
						if (taskInfo.getTaskType() == TaskType.TrainTask.getValue()) { // 领取的是新手任务
							if (taskInfo.getTaskId() != TaskMgr.freshUserTaskIds[TaskMgr.freshUserTaskIds.length - 1]) { 
								//如果不是最后一个新手任务，则下一个任务领取的也是新手任务
								int nextTaskId = taskInfo.getTaskId();
								
								TaskInfo tempInfo = TaskMgr.getTaskInfoByTaskId(TaskMgr.freshUserTaskIds[nextTaskId]);
								if(player.getPlayerTask().getUserTaskInfoByTaskId(tempInfo.getTaskId()) == null)
								{
									
									userNextTaskInfo = player.getPlayerTask().addPlayerTask(tempInfo);									
									if(userNextTaskInfo != null)
										nextTasksList.add(userNextTaskInfo);
																		
								}
							
							} 
							else {
								
								List<TaskInfo> tempList = TaskMgr.getUserNextTasks(taskInfo.getTaskId());//
								for(TaskInfo tempInfo : tempList)
								{
									if(tempInfo != null && player.getPlayerTask().getUserTaskInfoByTaskId(tempInfo.getTaskId()) == null) {//如果此类任务还没完成，今天还有此类任务
										
										userNextTaskInfo = player.getPlayerTask().addPlayerTask(tempInfo);									
										if(userNextTaskInfo != null)
											nextTasksList.add(userNextTaskInfo);

										
									}//如果此类任务已经完成了，那就没今天没有此类任务了
								}
								
								
								//领取的是最后一个新手任务,推三个普通任务给用户		日常任务种类（1 表示完成多少局， 2 表示胜利多少局， 3 表示连胜多少局）
								for (int i = 0; i < 6; i++) {//所以int i=1
									//设置下一个日常任务
									int preTaskId = 0;//0 表示没有前置任务的日常任务，就是说是第一个
									TaskInfo tempInfo = TaskMgr.getUserOneTasksOfDay(i, preTaskId);// i 表示日常任务种类
									if(tempInfo != null) {//如果此类任务还没完成，今天还有此类任务
										
										userNextTaskInfo = player.getPlayerTask().addPlayerTask(tempInfo);									
										if(userNextTaskInfo != null)
											nextTasksList.add(userNextTaskInfo);
									}
								}
								
								
								List<TaskInfo> taskActivity =  TaskMgr.getActivityTasks();         
				                for(TaskInfo task : taskActivity)
				                {		
				                	userNextTaskInfo = player.getPlayerTask().addPlayerTask(task);									
									if(userNextTaskInfo != null)
										nextTasksList.add(userNextTaskInfo);
//				                	if(!player.getPlayerTask().isHaveUserTaskInfoKey(task.getTaskId()))                	
//				                		player.getPlayerTask().addPlayerTask(task);
				                }
				                
							}
						} else {
							//设置下一个普通任务或者特殊任务
							if(taskInfo.getTaskType() == TaskType.NormalTask.getValue())
							{
								List<TaskInfo> tempList = TaskMgr.getUserNextTasks(taskInfo.getTaskId());//
								for(TaskInfo tempInfo : tempList)
								{
									if(tempInfo != null && player.getPlayerTask().getUserTaskInfoByTaskId(tempInfo.getTaskId()) == null) {//如果此类任务还没完成，今天还有此类任务
										
										userNextTaskInfo = player.getPlayerTask().addPlayerTask(tempInfo);									
										if(userNextTaskInfo != null)
											nextTasksList.add(userNextTaskInfo);
										
										
						        		if(tempInfo.getTaskStatus() == 3 && tempInfo.getTaskIsContinuous() == 1) {//是日常的连续胜利任务
						        			//数值任务完成所需的总数值为前面连续完成胜利任务的次数，就是说前面的有效计入后面的
						        			userNextTaskInfo.setTaskFinishNum(taskInfo.getTaskNum());
						        		}

										
									}//如果此类任务已经完成了，那就没今天没有此类任务了
								}
							}
							else if(taskInfo.getTaskType() == TaskType.DayTask.getValue()) {//是日常任务
								TaskInfo tempInfo = TaskMgr.getUserOneTasksOfDay(taskInfo.getTaskStatus(), taskInfo.getTaskId());// i 表示日常任务种类
								if(tempInfo != null && player.getPlayerTask().getUserTaskInfoByTaskId(tempInfo.getTaskId()) == null) {//如果此类任务还没完成，今天还有此类任务
									
									userNextTaskInfo = player.getPlayerTask().addPlayerTask(tempInfo);									
									if(userNextTaskInfo != null)
										nextTasksList.add(userNextTaskInfo);
																		
					        		if(tempInfo.getTaskStatus() == 3 && tempInfo.getTaskIsContinuous() == 1) {//是日常的连续胜利任务
					        			//数值任务完成所需的总数值为前面连续完成胜利任务的次数，就是说前面的有效计入后面的
					        			userNextTaskInfo.setTaskFinishNum(taskInfo.getTaskNum());
					        		}

								}//如果此类任务已经完成了，那就没今天没有此类任务了
							}
							
						}
					}
			} else {
				//这个用户未接受这个任务
				getPrize = false;
			}
		} else {
			//System.out.println("玩家领取奖励异常！userTaskId == 0 异常  playerId：" + player.getUserId());
			logger.debug("玩家领取奖励异常！userTaskId == 0 异常  playerId：" + player.getUserId());
		}
		
		
		checkTaskState(player,nextTasksList);
		
		
		
		
		Packet response = new Packet(UserCmdOutType.SEND_SUBMIT_TASK);
		response.putInt(userTaskId);
		response.putBoolean(getPrize);
		player.getOut().sendUserFinishOrNextTask(response, nextTasksList);
		
		
		if(player.getCurrentRoom() != null)
			player.getCurrentRoom().sendRoomPlayerCoin(player, totalCoin);
		
		
		return 0;
	}
	
	
	private void checkTaskState(GamePlayer player,List<UserTaskInfo> nextTasksList)
	{
		
		for(UserTaskInfo userTask : nextTasksList)
		{
			
			switch(userTask.getTaskInfo().getTaskCondition())
			{
				case TaskConditionType.AddFriend:
					player.isFinishTask(TaskConditionType.AddFriend, 0, 0);
					break;
				case TaskConditionType.LoginGame:
					player.isFinishTask(TaskConditionType.LoginGame, 0, 0);
					break;				
				case TaskConditionType.CharmValue:
					player.isFinishTask(TaskConditionType.CharmValue, 0, player.getPlayerInfo().getCharmValve());
					break;			
					
				case TaskConditionType.GameCount:
					player.isFinishTask(TaskConditionType.GameCount, 0, 0);
					break;

					
				default:
						break;
			}
		}
		
		
		player.isFinishTask(TaskConditionType.FinishStage, 0, 0);

	}
	
	
}
