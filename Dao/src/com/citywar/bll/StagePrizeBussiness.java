package com.citywar.bll;


import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.StagePrizeInfo;


public class StagePrizeBussiness {
	
	private final static String BUILD_TABLE = "t_s_stage_prize";//静态的任务数据表
		
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<StagePrizeInfo> getStagePrizesInfo()
	{
		return DaoManager.getStagePrizeDao().queryList(BUILD_TABLE, null, null);
	}
	
	public static List<StagePrizeInfo> getStagePrizeList()
	{
		return DaoManager.getStagePrizeDao().getStagePrizeList();
	}
	
}
