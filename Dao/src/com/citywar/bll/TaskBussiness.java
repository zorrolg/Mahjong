package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.UserTaskDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.TaskInfo;
import com.citywar.dice.entity.UserTaskInfo;

public class TaskBussiness {
	
	private final static String TASK_TABLE = "t_s_task";//静态的任务数据表
	
	private final static String USER_TASK_TABLE = "t_u_task";//随时更新的用户已经完成任务表
	
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<TaskInfo> getAllTasksInfo()
	{
		return DaoManager.getTaskDao().queryList(TASK_TABLE, "(begin_date is null or now() > begin_date) and (end_date is null or now() < end_date)", null, "task_id", "");
	}
	
	/**
	 * 根据用户id查询近两天的任务集合
	 * @param userId
	 * @return
	 */
	public static List<UserTaskInfo> getUserYesterdayTasks(int userId) 
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		return DaoManager.getUserTaskDao().queryList(USER_TASK_TABLE, "user_id=? and task_status in(0,1,2) ", params, "task_id asc", null);//alex 2013-12-30           task_status in(0,1,2)
	}
	/**
	 * 根据用户id查询所有的任务集合
	 * @param userId
	 * @return
	 */
	public static List<UserTaskInfo> getAllUserTasks(int userId) 
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		return DaoManager.getUserTaskDao().queryList(USER_TASK_TABLE, "user_id=? ", params);
	}
	
	/**
	 * 根据用户id 和 用户等级查询 还没有完成的任务集合(需要有嵌套查询慎用--嵌套表为静态数据表)
	 * @return
	 */
	public static List<UserTaskInfo> getUserNotCompeletedTasks(int userId) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		params.put(Types.INTEGER, 0);//0-任务未完成状态
		return DaoManager.getUserTaskDao().queryList(USER_TASK_TABLE, "user_id=? and get_time > DATE_SUB(NOW(),INTERVAL 2 DAY) ", params);
	}
	
	/**
	 * 插入用户可以做的任务数据
	 * @param userTaskInfo--task_status = 0;
	 * @return
	 */
	public static boolean insertUserCounldCompletedTask(UserTaskInfo userTaskInfo) {
		
		if(userTaskInfo.getTaskId() == 2201)
			System.out.println("insertUserCounldCompletedTask=======" + userTaskInfo.getTaskId());
		int id = DaoManager.getUserTaskDao().insert(userTaskInfo);		
		if(id > 0)
			userTaskInfo.setId(id);
		return id > 0;
	}
	
	public static boolean addUserTasks(List<UserTaskInfo> list) {
		boolean result = true;
		UserTaskDao dao = DaoManager.getUserTaskDao();
		for (UserTaskInfo info : list) {
			int id = dao.insert(info);
			if (id <= 0) {
				break;
			}
			info.setId(id);
			
			//System.out.println("addUserTasks=============="+info.getTaskInfo().getTaskName() + "=======" + info.getTaskStatus());
			
		}
		return result;
	}
	
	/**
	 * 更新用户任务的状态
	 * @param status
	 * @param userId
	 * @param taskId
	 * @return
	 */
	public static int updateUserTaskStatus(int status, int userId, int taskId) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, status);
		params.put(Types.INTEGER, userId);
		params.put(Types.INTEGER, taskId);
		return DaoManager.getUserTaskDao().update(USER_TASK_TABLE, "task_status=?", "user_id=? and task_id=?", params);
	}
	/**
	 * 更新用户数值任务的进度
	 * @param num
	 * @param userId
	 * @param taskId
	 * @return
	 */
	public static int updateUserTaskNum(int num, int userId, int taskId) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, num);
		params.put(Types.INTEGER, userId);
		params.put(Types.INTEGER, taskId);
		return DaoManager.getUserTaskDao().update(USER_TASK_TABLE, "task_finish_num=?", "user_id=? and task_id=?", params);
	}
	
	/**
	 * 更新用户数值任务的进度
	 * @param num
	 * @param userId
	 * @param taskId
	 * @return
	 */
	public static int updateChargeTask(int para1, int userId, int taskId) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, para1);
		params.put(Types.INTEGER, userId);
		params.put(Types.INTEGER, taskId);
				
		return DaoManager.getUserTaskDao().update(USER_TASK_TABLE, "task_status=1,task_finish_num=1,task_para=?", "task_status=0 and task_finish_num=0 and user_id=? and task_id=?", params);
	}
	
	/**
	 * 支持更新任务数值和状态
	 * @param list
	 * @return
	 */
	public static boolean updateUserTaskStatusAndNum(List<UserTaskInfo> list) {
		boolean result = true;
		for (UserTaskInfo info : list) {
			if (updateUserTaskInfo(info) <= 0) {
				result = false;
				//System.out.println("updateUserTaskStatusAndNum=============="+info.getTaskInfo().getTaskName() + "=======" + info.getTaskStatus());
//				break;
			}
		}
		return result;
	}
	
	public static int updateUserTaskInfo(UserTaskInfo info) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, info.getTaskStatus());
		params.put(Types.INTEGER, info.getTaskFinishNum());
		params.put(Types.TIMESTAMP, info.getFinishTime());
		params.put(Types.INTEGER, info.getPara());
		params.put(Types.INTEGER, info.getId());//在更新的时候，可以直接用主键来确定用户任务
//		params.put(Types.INTEGER, info.getUserId());
//		params.put(Types.INTEGER, info.getTaskInfo().getTaskId());
		
//		System.out.println("updateUserTaskInfo======" + info.getTaskStatus() + "===="+ info.getTaskFinishNum() + "===="+ info.getFinishTime() + "===="+ info.getId() + "====");
		return DaoManager.getUserTaskDao().update(USER_TASK_TABLE, "task_status=?,task_finish_num=?,finish_time=?,task_para=?", "id=?", params);
	}
	/**
	 * 查询某个任务是否完成
	 * @param userId
	 * @param taskId
	 * @param taskStatus
	 * @return
	 */
	public static boolean getUserTaskStatus(int userId, int taskId, int taskStatus) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		params.put(Types.INTEGER, taskId);
		params.put(Types.INTEGER, taskStatus);
		return DaoManager.
					getUserTaskDao().
						query(USER_TASK_TABLE, 
								"user_id=? and task_id=? and task_status=?", params) != null;
	}
	/**
	 * 根据taskId 获取任务信息
	 * @param taskId
	 * @return
	 */
	public static TaskInfo getTaskInfoById(int taskId) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, taskId);
		return DaoManager.getTaskDao().query(TASK_TABLE, "task_id=?", params);
	}
	
	/**
	 * 获取数值任务的进度情况
	 * @return getTaskFinishNum
	 */
	public static int getTaskFinishNum(int userId, int taskId) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		params.put(Types.INTEGER, taskId);
		UserTaskInfo userTaskInfo = DaoManager.getUserTaskDao().query(USER_TASK_TABLE, "user_id=? and task_id=?", params);
		return userTaskInfo != null ? userTaskInfo.getTaskFinishNum() : 0;
	}
}
