package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class TaskInfo {
	
	private int taskId;
	
	private String taskName;
	/**
	 *  任务的类型--0-新手,1-普通,2-特殊
	 */
	private int taskType;
	/**
	 *  日常任务种类（1 表示完成多少局， 2 表示胜利多少局， 3 表示连胜多少局）
	 */
	private int taskStatus;
	/**
	 * 任务的前置任务id
	 */
	private int preTaskId;
	/**
	 * 任务的等级限制--暂时可以不用
	 */
	private int taskLimitLevel;
	/**
	 * 任务的等级限制--暂时可以不用
	 */
	private int taskLimitLevelHigh;
	/**
	 * 任务的奖励类型可以为1个或者多个用数字标识用逗号风格
	 * @example taskPrizeType = "1,2,3";
	 */
	private String taskPrizeType;
	/**
	 * 任务的奖励对应的数值可以为1个或者多个用数字标识用逗号风格
	 * 跟taskPrizeType以逗号分割后的字符串数组长度相等
	 * @example taskPrizeNum = "100,200,500";
	 */
	private String taskPrizeNum;
	/**
	 * 任务描述
	 */
	private String taskDesc;
	
	private int isNumTask;//是否是数值任务0-不是,1-是
	/**
	 * 如果 isNumTask = 1;则该属性表示完成这个任务所需要的数值数是多少
	 */
	private int TaskNum;//数值任务完成所需的总数值
	
	private List<TaskPrize> prizeList;

	

	/**
	 * 是否是需要连续达成的任务(0-非连续,1-连续)
	 */
	private int TaskIsContinuous;

	
	/**
	 * 任务完成的条件字段,如果是填充任务需要填充的字段名--暂时可以不用
	 */
	private int taskCondition;
	
	/**
	 * 任务完成的条件字段,如果是填充任务需要填充的字段名--暂时可以不用
	 */
	private int taskPara1;
	
	/**
	 * 任务完成的条件字段,如果是填充任务需要填充的字段名--暂时可以不用
	 */
	private int taskPara2;
	
	private String taskPara3;
	
	private int stageId;
	
	private Timestamp beginDate;
	private Timestamp endDate;
	
	private int vipLevel;
	
	
	
	public TaskInfo() {}
	
	public TaskInfo(ResultSet rs) throws SQLException{
		this.setTaskId(rs.getInt("task_id"));
		this.setTaskName(rs.getString("task_name"));
		this.setTaskType(rs.getInt("task_type"));
		this.setTaskStatus(rs.getInt("task_status"));
		this.setPreTaskId(rs.getInt("pre_task_id"));
		
		this.setTaskLimitLevel(rs.getInt("task_limit_level"));
		this.setTaskLimitLevelHigh(rs.getInt("task_limit_level_high"));
		
		this.setTaskPrizeType(rs.getString("task_prize_type"));
		this.setTaskPrizeNum(rs.getString("task_prize_num"));
		this.setTaskDesc(rs.getString("task_desc"));
		this.setIsNumTask(rs.getInt("is_num_task"));
		this.setTaskNum(rs.getInt("task_num"));
		if (!this.taskPrizeType.isEmpty() && !this.taskPrizeNum.isEmpty()) {
			String[] typeArray = this.taskPrizeType.split(",");
			String[] numArray = this.taskPrizeNum.split(",");
			List<TaskPrize> list = new ArrayList<TaskPrize>();
			if (typeArray.length == numArray.length) {
				for (int i = 0; i < typeArray.length; i++) {
					TaskPrize tp = new TaskPrize(typeArray[i], numArray[i]);
					list.add(tp);
				}
			}
			this.setPrizeList(list);
		}
		
		this.setTaskCondition(rs.getInt("task_condition"));
		this.setTaskIsContinuous(rs.getInt("task_is_continuous"));

		
		this.setTaskCondition(rs.getInt("task_condition"));
		this.setTaskPara1(rs.getInt("task_para1"));
		this.setTaskPara2(rs.getInt("task_para2"));
		this.setTaskPara3(rs.getString("task_para3"));
		
		
		this.setStage(rs.getInt("stage"));
		this.setBeginDate(rs.getTimestamp("begin_date"));
		this.setEndDate(rs.getTimestamp("end_date"));
		this.setVipLevel(rs.getInt("vip_level"));
		
	}
	
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public int getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}
	public int getPreTaskId() {
		return preTaskId;
	}
	public void setPreTaskId(int preTaskId) {
		this.preTaskId = preTaskId;
	}
	public int getTaskLimitLevel() {
		return taskLimitLevel;
	}
	public void setTaskLimitLevel(int taskLimitLevel) {
		this.taskLimitLevel = taskLimitLevel;
	}
	public int getTaskLimitLevelHigh() {
		return taskLimitLevelHigh;
	}
	public void setTaskLimitLevelHigh(int taskLimitLevelHigh) {
		this.taskLimitLevelHigh = taskLimitLevelHigh;
	}
	
	public String getTaskPrizeType() {
		return taskPrizeType;
	}
	public void setTaskPrizeType(String taskPrizeType) {
		this.taskPrizeType = taskPrizeType;
	}
	public String getTaskPrizeNum() {
		return taskPrizeNum;
	}
	public void setTaskPrizeNum(String taskPrizeNum) {
		this.taskPrizeNum = taskPrizeNum;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	public int getIsNumTask() {
		return isNumTask;
	}
	public void setIsNumTask(int isNumTask) {
		this.isNumTask = isNumTask;
	}
	public int getTaskNum() {
		return TaskNum;
	}
	public void setTaskNum(int taskNum) {
		TaskNum = taskNum;
	}
	public List<TaskPrize> getPrizeList() {
		return prizeList;
	}
	public void setPrizeList(List<TaskPrize> prizeList) {
		this.prizeList = prizeList;
	}


	public int getTaskIsContinuous() {
		return TaskIsContinuous;
	}

	public void setTaskIsContinuous(int taskIsContinuous) {
		TaskIsContinuous = taskIsContinuous;
	}
	
	public int getTaskCondition() {
		return taskCondition;
	}

	public void setTaskCondition(int taskCondition) {
		this.taskCondition = taskCondition;
	}
	
	public int getTaskPara1() {
		return taskPara1;
	}

	public void setTaskPara1(int taskPara1) {
		this.taskPara1 = taskPara1;
	}
	
	public int getTaskPara2() {
		return taskPara2;
	}

	public void setTaskPara2(int taskPara2) {
		this.taskPara2 = taskPara2;
	}
	
	
	
	
	
	public int getStage() {
		return stageId;
	}

	public void setStage(int stageId) {
		this.stageId = stageId;
	}
	
	
	public String getTaskPara3() {
		return taskPara3;
	}
	
	public void setTaskPara3(String taskPara3) {
		this.taskPara3 = taskPara3;
	}
	
	
	/**
     * @return LastQiutDate
     */
    public Timestamp getBeginDate() {
		return beginDate;
	}
    /**
     * @param lastQiutDate
     */
	public void setBeginDate(Timestamp beginDate) {
		this.beginDate = beginDate;
	}
	
	
	/**
     * @return LastQiutDate
     */
    public Timestamp getEndDate() {
		return endDate;
	}
    /**
     * @param lastQiutDate
     */
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	
	
}
