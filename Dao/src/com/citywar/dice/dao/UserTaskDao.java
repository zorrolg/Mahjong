package com.citywar.dice.dao;

import java.util.List;

public interface UserTaskDao extends BaseDao{
	
	public List<Integer> getUserAlreadyCompletedTaskIds(int userId);

}
