package com.citywar.bll;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.DayRecordInfo;

public class DayRecordBussiness {
	
	public static int addDayRecordInfo(DayRecordInfo info)
	{
		return DaoManager.getDayRecordDao().insert(info);
	}
}
