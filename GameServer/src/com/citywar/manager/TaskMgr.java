package com.citywar.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.TaskBussiness;
import com.citywar.dice.entity.TaskInfo;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.type.TaskType;
/**
 * 任务管理类
 * @author Jacky.zheng
 *
 */
public class TaskMgr {
	
    private static Map<Integer, TaskInfo> allTaskMap;
    private static Map<Integer, TaskInfo> activityTaskMap;

    private static ReadWriteLock rwLock;

    private static final Logger logger = Logger.getLogger(TaskMgr.class.getName());
    
    public static int[] freshUserTaskIds = {1,2,3};//新手任务id数组
    
    //---任务状态---
    public static final int TASK_STATUS_NOT_FINISH = 0;//
    public static final int TASK_STATUS_ALREADY_FINISH = 1;
    public static final int TASK_STATUS_ALREADY_GET_PRIZE = 2;
    public static final int TASK_STATUS_DROP = 3;
    
    //---任务状态--- end
    /**
     * 任务管理初始化
     * 
     * @return
     */
    public static boolean init()
    {
        rwLock = new ReentrantReadWriteLock();
        allTaskMap = new LinkedHashMap<Integer, TaskInfo>();
        activityTaskMap = new LinkedHashMap<Integer, TaskInfo>();
        return reload();
    }
    
    
    /**
     * 重新载入任务信息
     * 
     * @return
     */
    public static boolean reload()
    {
        try
        {
            Map<Integer, TaskInfo> tempMap = new LinkedHashMap<Integer, TaskInfo>();
            Map<Integer, TaskInfo> activityMap = new LinkedHashMap<Integer, TaskInfo>();
            if (loadLevelFromDb(tempMap, activityMap))
            {
                rwLock.writeLock().lock();
                try
                {
                	allTaskMap = tempMap;
                	activityTaskMap = activityMap;                	
                    return true;
                }
                finally
                {
                    rwLock.writeLock().unlock();
                }

            }
            return true;
        }
        catch (Exception e)
        {
            logger.error("[ TaskMgr : reload ]", e);
        }
        return false;
    }
    
    /**
     * 从数据库加载所有的任务信息
     * 
     * @param tempMap
     *            存储等级信息的 map, 任务id为键, TaskInfo 为值
     * @return
     */
    private static boolean loadLevelFromDb(Map<Integer, TaskInfo> tempMap, Map<Integer, TaskInfo> activityMap)
    {
        try
        {
            List<TaskInfo> tempInfos = TaskBussiness.getAllTasksInfo();
            
            for (TaskInfo info : tempInfos)
            {
                if (!tempMap.containsKey(info.getTaskId())) 
                {
                	tempMap.put(info.getTaskId(), info);
                }
                
                if (!activityMap.containsKey(info.getTaskId()) && info.getTaskType() == TaskType.ActivityTask.getValue()) 
                {
                	activityMap.put(info.getTaskId(), info);
                }
            }
            return true;
        }
        catch (Exception e)
        {
            logger.error("[ TaskMgr : loadLevelFromDb ]", e);
        }
        return false;
    }
    
    /**
     * 新建用户可完成任务信息
     * @param userId
     * @param taskId
     * @return
     */
    public static UserTaskInfo getUserTaskInfoByTaskId(int userId, int taskId) {
    	return new UserTaskInfo(userId, allTaskMap.get(taskId));
    }

    public static List<TaskInfo> getALLTasks() {
    	List<TaskInfo> list = new ArrayList<TaskInfo>();
    	rwLock.readLock().lock();
        try
        {
            for (TaskInfo t : allTaskMap.values())
            {
                if (t == null)
                    continue;

                list.add(t);
            }
        }
        finally
        {
        	rwLock.readLock().unlock();
        }
        return list;
    }
    
    public static TaskInfo getTaskInfoByTaskId(int taskId) {
    	return allTaskMap.get(taskId);
    }
    
    public static List<TaskInfo> getALLStageTasks(int stage) {
    	List<TaskInfo> list = new ArrayList<TaskInfo>();
    	rwLock.readLock().lock();
        try
        {
            for (TaskInfo t : allTaskMap.values())
            {
                if (t != null && t.getStage() == stage)
                	list.add(t);
            }
        }
        finally
        {
        	rwLock.readLock().unlock();
        }
        return list;
    }
    
    /**
     * 随机获得一个用户没有的普通任务
     * @param List<UserTaskInfo> list -- 用户当前未完成的任务集合
     * @return
     */
//    public static TaskInfo getOneRandomCommonTask(List<UserTaskInfo> list) {
//    	Map<Integer, TaskInfo> result = new ConcurrentHashMap<Integer, TaskInfo>();
//    	//把所有普通任务放在备选集合中
//    	for (TaskInfo info : allTaskMap.values()) {
//    		if (info.getTaskType() == 1) {
//    			result.put(info.getTaskId(), info);
//    		}
//    	}
//    	//过滤用户已经在做的任务
//    	for (UserTaskInfo userTaskInfo : list) {
//    		result.remove(userTaskInfo.getTaskInfo().getTaskId());
//    	}
//    	
//    	//随机出结果
//    	List<TaskInfo> temp = new ArrayList<TaskInfo>(); 
//    	for (TaskInfo info: result.values()) {
//    		temp.add(info);
//    	}
//    	
//    	int index = Math.abs(new Random().nextInt() % temp.size());
//    	if (index > temp.size() || index < 0) {
//    		index = temp.size(); // 处理异常
//    	}
//    	//System.out.println("index===" + index);
//    	TaskInfo tempinfo = temp.get(index);
//    	//System.out.println("tempinfo=====" + tempinfo.getTaskName());
//    	return tempinfo;
//    }
	
	/**
	 * taskStatus表示日常任务种类，preTaskId 前置任务的日常任务
	 * 根据任务类型和任务id获取下一个日常任务
	 * @param taskStatus,pre_task_id
	 * @return List<TaskInfo>
	 */
	public static TaskInfo getUserOneTasksOfDay(int taskStatus, int preTaskId) 
	{
		//把所有普通任务放在备选集合中
    	for (TaskInfo info : allTaskMap.values()) {
    		if (info.getTaskStatus() == taskStatus && info.getPreTaskId() == preTaskId
    				&& info.getTaskType() == TaskType.DayTask.getValue()) {
    			return info;
    		}
    	}
		return null;
	}

	/**
	 * taskStatus表示日常任务种类，preTaskId 前置任务的日常任务
	 * 根据任务类型和任务id获取下一个日常任务
	 * @param taskStatus,pre_task_id
	 * @return List<TaskInfo>
	 */
	public static List<TaskInfo> getUserNextTasks(int preTaskId) 
	{
		List<TaskInfo> temp = new ArrayList<TaskInfo>(); 
		
		//把所有普通任务放在备选集合中
    	for (TaskInfo info : allTaskMap.values()) {
    		if (info.getPreTaskId() == preTaskId) {
    			temp.add(info);
    		}
    	}
		return temp;
	}
	
	
	/**
	 * taskStatus表示日常任务种类，preTaskId 前置任务的日常任务
	 * 根据任务类型和任务id获取下一个日常任务
	 * @param taskStatus,pre_task_id
	 * @return List<TaskInfo>
	 */
	public static TaskInfo getTasksByCondition(int condition) 
	{
		//把所有普通任务放在备选集合中
    	for (TaskInfo info : allTaskMap.values()) {
    		if (info.getTaskCondition() == condition) {
    			return info;
    		}
    	}
		return null;
	}
	
	
	/**
	 * taskStatus表示日常任务种类，preTaskId 前置任务的日常任务
	 * 根据任务类型和任务id获取下一个日常任务
	 * @param taskStatus,pre_task_id
	 * @return List<TaskInfo>
	 */
	public static List<TaskInfo> getActivityTasks() 
	{
		
		List<TaskInfo> temp = new ArrayList<TaskInfo>(); 
		
		//把所有普通任务放在备选集合中
    	for (TaskInfo info : activityTaskMap.values()) {
    			temp.add(info);
    	}
		
		return temp;
	}
	
	
}
