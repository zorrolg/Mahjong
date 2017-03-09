package com.citywar.dice.dao.impl;


import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.RobotLevelDao;
import com.citywar.dice.entity.RobotLevelInfo;


public class RobotLevelDaoImpl extends BaseDaoImpl implements RobotLevelDao {


	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		RobotLevelInfo robotChatInfo = new RobotLevelInfo(rs);
		return robotChatInfo;
	}
		
	
}