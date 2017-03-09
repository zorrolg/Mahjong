package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserAwardDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserAwardInfo;

public class UserAwardDaoImpl extends BaseDaoImpl implements UserAwardDao {

	private static final Logger logger = Logger
			.getLogger(UserAwardDaoImpl.class.getName());

	private final static String USER_AWARD_TABLE = "t_u_award";//

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserAwardInfo UserAward = new UserAwardInfo(rs);
		return UserAward;
	}

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserAwardInfo userAward = (UserAwardInfo) obj;
		String sqlText = " INSERT INTO "
				+ USER_AWARD_TABLE
				+ "(`user_id`,`award_type`,`award_count`,`award_last_time`) values (?,?,?,?); ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userAward.getUserId());
		params.put(Types.INTEGER, userAward.getAwardType());
		params.put(Types.INTEGER, userAward.getAwardCount());
		params.put(Types.TIMESTAMP, userAward.getAwardLastTtime());

		try {
			intResult = getDbManager().executeLastId(sqlText,
					params.getParams());// 获得id号
		} catch (SQLException e) {
			logger.error("[ UserAwardDaoImpl : insert ]", e);
		}
		return intResult;
	}

	@Override
	public int update(Object obj) {
		int intResult = -1;
		UserAwardInfo userAward = (UserAwardInfo) obj;
		String sqlText = " UPDATE "
				+ USER_AWARD_TABLE
				+ " SET `user_id`=?,`award_type`=?,`award_count`=?,`award_last_time`=? where id = ?; ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userAward.getUserId());
		params.put(Types.INTEGER, userAward.getAwardType());
		params.put(Types.INTEGER, userAward.getAwardCount());
		params.put(Types.TIMESTAMP, userAward.getAwardLastTtime());
		params.put(Types.INTEGER, userAward.getId());

		try {
			intResult = getDbManager().executeNoneQuery(sqlText,
					params.getParams());
		} catch (SQLException e) {
			logger.error("[ UserAwardDaoImpl : update ]", e);
		}
		return intResult;
	}
}
