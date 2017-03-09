package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserDayActivityDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserDayActivityInfo;

public class UserDayActivityDaoImpl extends BaseDaoImpl implements UserDayActivityDao {

	private static final Logger logger = Logger
			.getLogger(UserDayActivityDaoImpl.class.getName());

	private final static String USER_DAY_ACTIVITY_TABLE = "t_u_day_activity";//

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserDayActivityInfo UserDayActivity = new UserDayActivityInfo(rs);
		return UserDayActivity;
	}

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserDayActivityInfo UserDayActivity = (UserDayActivityInfo) obj;
		String sqlText = " INSERT INTO "
				+ USER_DAY_ACTIVITY_TABLE
				+ "(`user_id`,`count_date`,`online_time`,`usage_counter_tea`,`usage_counter_horn`,`increase_coins`) values (?,?,?,?,?,?); ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, UserDayActivity.getUserId());
		params.put(Types.TIMESTAMP, UserDayActivity.getCountDate());
		params.put(Types.BIGINT, UserDayActivity.getPerCapitaTime());
		params.put(Types.INTEGER, UserDayActivity.getUsageCounterTea());
		params.put(Types.INTEGER, UserDayActivity.getUsageCounterHorn());
		params.put(Types.INTEGER, UserDayActivity.getIncreaseCoins());

		try {
			intResult = getDbManager().executeLastId(sqlText,
					params.getParams());// 获得id号
		} catch (SQLException e) {
			logger.error("[ UserDayActivityDaoImpl : insert ]", e);
		}
		return intResult;
	}

	@Override
	public int update(Object obj) {
		int intResult = -1;
		UserDayActivityInfo UserDayActivity = (UserDayActivityInfo) obj;
		String sqlText = " UPDATE "
				+ USER_DAY_ACTIVITY_TABLE
				+ " SET `user_id`=?,`count_date`=?,`online_time`=?,`usage_counter_tea`=? ,`usage_counter_horn`=?,`increase_coins`=? where id = ?; ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, UserDayActivity.getUserId());
		params.put(Types.TIMESTAMP, UserDayActivity.getCountDate());
		params.put(Types.BIGINT, UserDayActivity.getPerCapitaTime());
		params.put(Types.INTEGER, UserDayActivity.getUsageCounterTea());
		params.put(Types.INTEGER, UserDayActivity.getUsageCounterHorn());
		params.put(Types.INTEGER, UserDayActivity.getIncreaseCoins());
		params.put(Types.INTEGER, UserDayActivity.getId());

		try {
			intResult = getDbManager().executeNoneQuery(sqlText,
					params.getParams());
		} catch (SQLException e) {
			logger.error("[ UserDayActivityDaoImpl : update ]", e);
		}
		return intResult;
	}
	
	@Override
	public boolean delete(int id)
    {
        if (id <= 0)
        {
            return false;
        }
        boolean delSuccess = false;
        String sqlText = "delete from " + USER_DAY_ACTIVITY_TABLE + " where id = ?";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, id);

        try
        {
            delSuccess = getDbManager().executeNoneQuery(sqlText,
                                                         params.getParams()) > -1 ? true
                    : false;
        }
        catch (SQLException e)
        {
            logger.error("[ UserDayActivityDaoImpl : deleteFriend ]", e);
        }

        return delSuccess;
    }
}
