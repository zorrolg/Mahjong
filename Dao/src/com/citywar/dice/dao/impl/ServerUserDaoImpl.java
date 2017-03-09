package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.ServerUserDao;
import com.citywar.dice.entity.ServerUser;


public class ServerUserDaoImpl extends BaseDaoImpl implements ServerUserDao {
	
//	private final static String BUILD_TABLE = "t_s_city";//静态的任务数据表
	


	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		ServerUser info = new ServerUser(rs);
		return info;
	}



}
