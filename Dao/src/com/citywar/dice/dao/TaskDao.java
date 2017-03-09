package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.TaskInfo;

public interface TaskDao extends BaseDao{
	
	public List<TaskInfo> getUserNotCompletedTasks(int userId, int userLevel);
	
}
