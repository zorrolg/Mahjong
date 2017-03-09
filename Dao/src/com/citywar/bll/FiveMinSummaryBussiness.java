package com.citywar.bll;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.FiveMinSummaryInfo;

public class FiveMinSummaryBussiness {
	
	public static int addFiveMinSummaryInfo(FiveMinSummaryInfo info)
	{
		return DaoManager.getFiveMinSummaryDao().insert(info);
	}
}
