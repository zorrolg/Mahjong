package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.DayRecordDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.DayRecordInfo;

public class DayRecordDaoImpl extends BaseDaoImpl implements DayRecordDao  {

	private final static String DAY_RECORD_TABLE = "t_s_day_record";//日统计数据表
	
    private static final Logger logger = Logger.getLogger(DayRecordDaoImpl.class.getName());

	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		DayRecordInfo info = new DayRecordInfo(rs);
		return info;
	}
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		DayRecordInfo DayRecordInfo = (DayRecordInfo) obj;
        String sqlText = "insert into " + DAY_RECORD_TABLE + "(count_date, sub_id , reg_member_count, reg_user_count, login_user_count, max_online_count, per_capita_time, usage_counter_tea, usage_counter_horn) values(?,?,?,?,?,?,?,?,?)";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.TIMESTAMP, DayRecordInfo.getCountDate());
        params.put(Types.INTEGER, DayRecordInfo.getSubId());
        params.put(Types.INTEGER, DayRecordInfo.getRegMemberCount());
        params.put(Types.INTEGER, DayRecordInfo.getRegUserCount());
        params.put(Types.INTEGER, DayRecordInfo.getLoginUserCount());
        params.put(Types.INTEGER, DayRecordInfo.getMaxOnlineCount());
        params.put(Types.BIGINT, DayRecordInfo.getPerCapitaTime());
        params.put(Types.INTEGER, DayRecordInfo.getUsageCounterTea());
        params.put(Types.INTEGER, DayRecordInfo.getUsageCounterHorn());
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ DayRecordDaoImpl : insert ]", e);
        }
		return intResult;
	}
}
