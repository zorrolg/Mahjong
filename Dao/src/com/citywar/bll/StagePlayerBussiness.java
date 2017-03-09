package com.citywar.bll;


import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.StagePlayerInfo;



public class StagePlayerBussiness {
	
	private final static String BUILD_TABLE = "t_s_stage_player";//静态的任务数据表
	

	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<StagePlayerInfo> getAllStagePlayersInfo()
	{
		return DaoManager.getStagePlayerDao().queryList(BUILD_TABLE, null, null);
	}
	
	public static List<StagePlayerInfo> getAllStagePlayerTypeInfo()
	{
		return DaoManager.getStagePlayerDao().getStagePlayerList();
	}
	
	public static int addStagePlayerInfo(StagePlayerInfo playerInfo)
    {
        return DaoManager.getStagePlayerDao().insert(playerInfo);
    }
	
}
