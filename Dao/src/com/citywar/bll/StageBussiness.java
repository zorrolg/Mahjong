package com.citywar.bll;

import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.PlayerStage;
import com.citywar.dice.entity.StageInfo;
import com.citywar.dice.entity.StageRoundInfo;

public class StageBussiness {
	
//	private final static String STAGE_TABLE = "t_s_stage";//静态的任务数据表
	
	
	
	
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<StageInfo> getAllStegesInfo()
	{
		return DaoManager.getStagesDao().getStageList();
	}
	
	
	public static List<PlayerStage> getUserStegesInfo(int userID)
    {
        return DaoManager.getStagesDao().getUserStegesInfo(userID);
    }
	
	public static List<StageRoundInfo> getAllStegesRoundInfo()
	{
		return DaoManager.getStagesDao().getStageRoundList();
	}
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<PlayerStage> getUserStegesAll()
	{
		return DaoManager.getStagesDao().getTopStageAll();
	}

	
	 /**
     * 更新玩家背包物品属性
     * 
     * @param userId
     * @param items
     * @return
     */
    public static boolean updateStages(List<PlayerStage> playerStage)
    {
        boolean result = true;
        for (PlayerStage info : playerStage)
        {
        	if( ! (DaoManager.getStagesDao().update(info) > 0)) {
        		result = false;
				break;
        	}
        }

        return result;
    }

    public static boolean addStages(List<PlayerStage> infos)
    {
    	boolean result = true;        

        for (PlayerStage info : infos)
        {
        	
//        	System.out.println("StagesInfo==========333==============" + info.getUserId() + "=========" + info.getStageId());
        	
            int userItemId = DaoManager.getStagesDao().insert(info);
            if (userItemId <= 0)
            {
                result = false;
                break;
            } else {
//            	info.setItemId(userItemId);
            }

        }
        return result;
    }
	
    
    
    
    public static boolean addStages(PlayerStage infos)
    {
    	boolean result = false;        
    	result = DaoManager.getStagesDao().insert(infos) > 0 ;
        return result;
    }
}
