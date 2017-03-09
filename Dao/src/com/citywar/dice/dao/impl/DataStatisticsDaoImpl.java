/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.DataStatisticsDao;
import com.citywar.dice.db.DBParamWrapper;

/**
 * 统计DAO -- 实现
 * 
 * @author shanfeng.cao
 * @date 2012-08-14
 * @version
 * 
 */
public class DataStatisticsDaoImpl extends BaseDaoImpl implements DataStatisticsDao
{
    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        return null;
    }

	@Override
	public int getSQLCount(String SQLString) {
		int result = 0;
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	try {
    		pstmt = getDbManager().executeQuery(SQLString);
    		if (null != pstmt)
    		{
    			rs = pstmt.executeQuery();
    			while (rs.next())
    			{
    				result = rs.getInt("count");
    			}
    		}
		} 
    	catch (SQLException e) 
    	{
			e.printStackTrace();
		}
		finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }
    	return result;
	}

	@Override
	public String getRetainedRateDaySQLString(int countDay) {
		String SQLString = "SELECT COUNT(*) as count from t_u_player where" +
				" LastLoginDate > DATE_SUB(NOW(),INTERVAL 1 DAY) AND RegisterDate > DATE_SUB(NOW(),INTERVAL " 
				+ (countDay + 1) + " DAY) AND RegisterDate <= DATE_SUB(NOW(),INTERVAL "+ (countDay) + " DAY);";
		return SQLString;
	}

	@Override
	public int getRetainedRateDayLoginCount(int countDay) {
		String SQLString = getRetainedRateDaySQLString(countDay);
		return getSQLCount(SQLString);
	}
    
    public int getFiveMinSummaryRegisterCount() {
		String SQLString = "SELECT count(*) as count FROM `t_u_player` where RegisterDate >= DATE_SUB(NOW(), interval 5 minute) and t_u_player.UDID is not null;";
		return getSQLCount(SQLString);
    }

	@Override
	public int getRegMemberCount() {
		String SQLString = "SELECT count(*) as count from t_u_player where DATE_SUB(NOW(),INTERVAL 1 DAY) < t_u_player.RegisterDate and t_u_player.UDID is null;";
		return getSQLCount(SQLString);
	}

	@Override
	public int getDayRegUserCount() {
		String SQLString = "SELECT count(*) as count from t_u_player where DATE_SUB(NOW(),INTERVAL 1 DAY) < t_u_player.RegisterDate and t_u_player.UDID is not null;";
		return getSQLCount(SQLString);
	}

	@Override
	public int getDayLoginUserCount() {
		String SQLString = "SELECT count(*) as count from t_u_player where DATE_SUB(NOW(),INTERVAL 1 DAY) < t_u_player.LastLoginDate;";
		return getSQLCount(SQLString);
	}

	@Override
	public int getDayMaxOnlineCount() {
		String SQLString = "SELECT MAX(`online`) as count from t_5_min where count_date > DATE_SUB(NOW(),INTERVAL 1 DAY);";
		return getSQLCount(SQLString);
	}

	@Override
	public long getPerCapitaTime() {
		String SQLString = "SELECT AVG(`online_time`) as count from t_u_day_activity where count_date > DATE_SUB(NOW(),INTERVAL 1 DAY) AND online_time > 0;;";
		return getSQLCount(SQLString);
	}

	@Override
	public int getUsageCounterTeaCount() {
		String SQLString = "SELECT SUM(`usage_counter_tea`) as count from t_u_day_activity where count_date > DATE_SUB(NOW(),INTERVAL 1 DAY) AND online_time > 0;";
		return getSQLCount(SQLString);
	}

	@Override
	public int getUsageCounterHornCount() {
		String SQLString = "SELECT SUM(`usage_counter_horn`) as count from t_u_day_activity where count_date > DATE_SUB(NOW(),INTERVAL 1 DAY) AND online_time > 0;";
		return getSQLCount(SQLString);
	}

	@Override
	public int copyInsertETL() {
		int result = 0;
		DBParamWrapper params = new DBParamWrapper();
		try {
			result = getDbManager().executeNoneQuery("INSERT INTO t_u_etl_day_activity SELECT 0,user_id,count_date,online_time,usage_counter_tea,usage_counter_horn,increase_coins FROM t_u_day_activity where online_time > 0;", params.getParams());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
