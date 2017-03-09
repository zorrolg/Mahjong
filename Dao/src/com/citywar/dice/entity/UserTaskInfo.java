package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 用户完成的任务列表
 * @author Jacky.zheng
 * @date 2012-03-23
 *
 */
public class UserTaskInfo extends DataObject{
	
	private int id;
	
	private int userId;
	
	private int taskId;
	
	private TaskInfo taskInfo;
	
	private int taskStatus;//任务状态 0-未完成 1-完成 2-领取3-过时、无效
	
	private Timestamp finishTime;// 任务完成的时间
	
	private Timestamp getTime; //任务接受的时间
	
	private int para;
	
	/**
	 * 如果TaskInfo.isNumTask = 1;那么这个任务是一个数值累计任务,需要达到固定数值条件才能完成
	 * @example taskFinishNum="30";--完成任务情况30/100
	 */
	private int taskFinishNum;
	
	public UserTaskInfo() {}
	
	public UserTaskInfo(int userId, TaskInfo taskInfo) {
		this.id = 0; // 生成的未入库新对象的ID全部为0
		this.userId = userId;
		this.taskInfo = taskInfo;
		this.taskId = taskInfo.getTaskId();
		this.taskStatus = 0;// 生成用户任务数据时默认为0--未完成任务状态
		this.getTime = new Timestamp(System.currentTimeMillis());
		this.taskFinishNum = 0;
		this.para = 0;
	}
	
	public UserTaskInfo(ResultSet rs) throws SQLException{
		this.id = rs.getInt("id");
		this.userId = rs.getInt("user_id");
		this.taskId = rs.getInt("task_id");
		this.taskInfo = null; //TaskBussiness.getTaskInfoById(rs.getInt("task_id"));
		this.taskStatus = rs.getInt("task_status");
		this.finishTime = rs.getTimestamp("finish_time");
		this.taskFinishNum = rs.getInt("task_finish_num");
		this.getTime = rs.getTimestamp("get_time");		
		this.para = rs.getInt("task_para");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	public TaskInfo getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Timestamp getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}

	public int getTaskFinishNum() {
		return taskFinishNum;
	}

	public void setTaskFinishNum(int taskFinishNum) {
		this.taskFinishNum = taskFinishNum;
	}

	public Timestamp getGetTime() {
		return getTime;
	}

	public void setGetTime(Timestamp getTime) {
		this.getTime = getTime;
	}
	

	public int getPara() {
		return para;
	}

	public void setPara(int para) {
		this.para = para;
	}
	
}
