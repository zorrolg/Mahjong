package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserPropertyDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserPropertyInfo;

public class UserPropertyDaoImpl extends BaseDaoImpl implements UserPropertyDao {

	private static final Logger logger = Logger
			.getLogger(UserPropertyDaoImpl.class.getName());

	private final static String USER_PROPERTY_TABLE = "t_u_property";//

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserPropertyInfo UserPropertyInfo = new UserPropertyInfo(rs);
		return UserPropertyInfo;
	}

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserPropertyInfo userPropertyInfo = (UserPropertyInfo) obj;
		String sqlText = " INSERT INTO "
				+ USER_PROPERTY_TABLE
				+ "(`user_id`,`identity_card`,`real_name`) values (?,?,?); ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userPropertyInfo.getUserId());
		params.put(Types.VARCHAR, userPropertyInfo.getIdentityCard());
		params.put(Types.VARCHAR, userPropertyInfo.getRealName());

		try {
			intResult = getDbManager().executeNoneQuery(sqlText, params.getParams());
		} catch (SQLException e) {
			logger.error("[ UserPropertyInfoDaoImpl : insert ]", e);
		}
		return intResult;
	}

	@Override
	public int update(Object obj) {
		int intResult = -1;
		UserPropertyInfo userPropertyInfo = (UserPropertyInfo) obj;
		String sqlText = " UPDATE "
				+ USER_PROPERTY_TABLE
				+ " SET `identity_card`=?,`real_name`=? where user_id = ?; ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR, userPropertyInfo.getIdentityCard());
		params.put(Types.VARCHAR, userPropertyInfo.getRealName());
		params.put(Types.INTEGER, userPropertyInfo.getUserId());

		try {
			intResult = getDbManager().executeNoneQuery(sqlText, params.getParams());
		} catch (SQLException e) {
			logger.error("[ UserPropertyInfoDaoImpl : update ]", e);
		}
		return intResult;
	}
	
	
}
