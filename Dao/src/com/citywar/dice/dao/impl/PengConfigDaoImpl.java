package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.PengConfigDao;
import com.citywar.dice.entity.PengConfigInfo;


public class PengConfigDaoImpl extends BaseDaoImpl implements PengConfigDao {
	
//	private final static String BUILD_TABLE = "t_c_build";//静态的任务数据表
	
//	private final static String BUILD_TYPE_TABLE = "t_s_peng_config";//随时更新的用户已经完成任务表

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		PengConfigInfo info = new PengConfigInfo(rs);
		return info;
	}

}
