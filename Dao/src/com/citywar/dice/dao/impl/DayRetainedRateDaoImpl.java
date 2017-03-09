package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.DayRetainedRateDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.DayRetainedRateInfo;

public class DayRetainedRateDaoImpl extends BaseDaoImpl implements DayRetainedRateDao  {

	private final static String DAY_RETAINED_RATE_TABLE = "t_s_day_retained_rate";
	
    private static final Logger logger = Logger.getLogger(DayRetainedRateDaoImpl.class.getName());

	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		DayRetainedRateInfo info = new DayRetainedRateInfo(rs);
		return info;
	}
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		DayRetainedRateInfo DayRetainedRateInfo = (DayRetainedRateInfo) obj;
        String sqlText = "insert into " + DAY_RETAINED_RATE_TABLE + "(count_date, sub_id , day_reg_count, day_1_login_count, day_2_login_count, day_3_login_count, day_4_login_count, day_5_login_count, day_6_login_count, day_7_login_count) values(?,?,?,?,?,?,?,?,?,?)";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.TIMESTAMP, DayRetainedRateInfo.getCountDate());
        params.put(Types.INTEGER, DayRetainedRateInfo.getSubId());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDayRegCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay1LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay2LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay3LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay4LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay5LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay6LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay7LoginCount());
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ DayRetainedRateDaoImpl : insert ]", e);
        }
		return intResult;
	}
    @Override
    public int update(Object obj) {
    	int intResult = -1;
		DayRetainedRateInfo DayRetainedRateInfo = (DayRetainedRateInfo) obj;
		String sqlText = " UPDATE "
				+ DAY_RETAINED_RATE_TABLE
				+ " SET `day_reg_count`=?,`day_1_login_count`=?,`day_2_login_count`=?,`day_3_login_count`=?" +
				",`day_4_login_count`=?,`day_5_login_count`=?,`day_6_login_count`=?,`day_7_login_count`=?" +
				" where count_date = ? AND sub_id = ?; ";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, DayRetainedRateInfo.getDayRegCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay1LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay2LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay3LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay4LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay5LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay6LoginCount());
        params.put(Types.INTEGER, DayRetainedRateInfo.getDay7LoginCount());
        params.put(Types.TIMESTAMP, DayRetainedRateInfo.getCountDate());
        params.put(Types.INTEGER, DayRetainedRateInfo.getSubId());
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ DayRetainedRateDaoImpl : insert ]", e);
        }
		return intResult;
    }
}
