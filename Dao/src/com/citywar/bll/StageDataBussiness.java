package com.citywar.bll;


import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.StageDataInfo;


public class StageDataBussiness {
	
	private final static String BUILD_TABLE = "t_s_stage_data";//静态的任务数据表
	
	
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<StageDataInfo> getAllStageDatasInfo()
	{
		return DaoManager.getStageDataDao().queryList(BUILD_TABLE, null, null);
	}
	
	public static List<StageDataInfo> getAllStageDataTypeInfo()
	{
		return DaoManager.getStageDataDao().getStageDataList();
	}
	
	public static int addStageDataInfo(StageDataInfo dataInfo)
    {
        return DaoManager.getStageDataDao().insert(dataInfo);
    }

}
