package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.CityDao;
import com.citywar.dice.entity.BuildInfo;

public class CityDaoImpl extends BaseDaoImpl implements CityDao {
	
//	private final static String BUILD_TABLE = "t_s_city";//静态的任务数据表
	


	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		BuildInfo info = new BuildInfo(rs);
		return info;
	}



}
