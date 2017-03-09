package com.citywar.bll;

import java.sql.Timestamp;
import java.sql.Types;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.DayRetainedRateInfo;

public class DayRetainedRateBussiness {
	
	private final static String DAY_RETAINED_RATE_TABLE = "t_s_day_retained_rate";
	
	public static int addDayRetainedRateInfo(DayRetainedRateInfo info)
	{
		return DaoManager.getDayRetainedRateDao().insert(info);
	}

	public static int updateDayRetainedRateInfo(
			DayRetainedRateInfo dayRetainedRateInfo) {
		return DaoManager.getDayRetainedRateDao().update(dayRetainedRateInfo);
	}
	
	/**
	 * 根据日期和服务器Id获取 周留存率
	 * @param countDay
	 * @return
	 */
	public static DayRetainedRateInfo getDayRetainedRate(Timestamp retainedRateDay, int subId) {

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.TIMESTAMP, retainedRateDay);
        params.put(Types.INTEGER, subId);
        return DaoManager.getDayRetainedRateDao().query(DAY_RETAINED_RATE_TABLE,
                                                     "count_date = ? AND sub_id = ?", params);
	}
}
