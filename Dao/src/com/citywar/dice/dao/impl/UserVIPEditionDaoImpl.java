package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserVIPEditionDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserVIPEditionInfo;

public class UserVIPEditionDaoImpl extends BaseDaoImpl implements UserVIPEditionDao {

	private static final Logger logger = Logger
			.getLogger(UserVIPEditionDaoImpl.class.getName());

	private final static String USER_VIP_EDITION_TABLE = "t_u_vip_edition";//

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserVIPEditionInfo UserVIPEditionInfo = new UserVIPEditionInfo(rs);
		return UserVIPEditionInfo;
	}

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserVIPEditionInfo userVIPEditionInfo = (UserVIPEditionInfo) obj;
		String sqlText = " INSERT INTO "
				+ USER_VIP_EDITION_TABLE
				+ "(`user_id`,`machinery_id`,`award_time`) values (?,?,?); ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userVIPEditionInfo.getUserId());
		params.put(Types.VARCHAR, userVIPEditionInfo.getMachineryId());
		params.put(Types.TIMESTAMP, userVIPEditionInfo.getAwardTime());

		try {
			intResult = getDbManager().executeNoneQuery(sqlText,
					params.getParams());// 获得id号
		} catch (SQLException e) {
			logger.error("[ UserVIPEditionInfoDaoImpl : insert ]", e);
		}
		return intResult;
	}

	@Override
	public int update(Object obj) {
		int intResult = -1;
		UserVIPEditionInfo userVIPEditionInfo = (UserVIPEditionInfo) obj;
		String sqlText = " UPDATE "
				+ USER_VIP_EDITION_TABLE
				+ " SET `user_id`=?,`machinery_id`=?,`award_time`=? where id = ?; ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userVIPEditionInfo.getUserId());
		params.put(Types.VARCHAR, userVIPEditionInfo.getMachineryId());
		params.put(Types.TIMESTAMP, userVIPEditionInfo.getAwardTime());
		params.put(Types.INTEGER, userVIPEditionInfo.getId());

		try {
			intResult = getDbManager().executeNoneQuery(sqlText,
					params.getParams());
		} catch (SQLException e) {
			logger.error("[ UserVIPEditionInfoDaoImpl : update ]", e);
		}
		return intResult;
	}
}
