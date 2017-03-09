package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.PengGame;
import com.citywar.dice.entity.PengGameDetail;

public class PengGameBussiness {
	
	private final static String PENG_TABLE = "t_u_peng";//静态的任务数据表
	
	private final static String PENGDETAIL_TABLE = "t_u_peng_detail";//静态的任务数据表

	
	
	/**
     * 添加一个玩家信息入库
     * 
     * @param playerInfo
     * @return
     */
    public static int addPengGame(PengGame dateInfo)
    {
    	int id = DaoManager.getPengGameDao().insert(dateInfo);
    	if(id > 0)
    		dateInfo.setId(id);
        return id;
    }
	
    
    public static int addPengGameDetail(PengGameDetail dateInfo)
    {
        return DaoManager.getPengGameDetailDao().insert(dateInfo);
    }
    
    
	public static List<PengGame> getPengGame(int pengId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, pengId);
        return DaoManager.getPengGameDao().queryList(PENG_TABLE, "id=?",
                                                     params);
    }
	
	
	public static List<PengGame> getPengGameList(int userId, int pageIndex)
    {
		String strLimit = String.format("%d,%d", (pageIndex-1)*10 , (pageIndex-1)*1+10);
		String strLike =  "%_" + String.valueOf(userId) + "_%";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, strLike);
        return DaoManager.getPengGameDao().queryList(PENG_TABLE, "playerList like ?",
                                                     params, "gameDate desc", strLimit);
    }
	
	
    public static boolean updateCharmValve(int userId, String nameList, String scoreList)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, nameList);
        params.put(Types.VARCHAR, scoreList);
        params.put(Types.INTEGER, userId);
        return DaoManager.getPengGameDao().update(PENG_TABLE, "nameList=?,scoreList=?",
                                                    "id=?", params) != 0;
    }
    
    
	public static List<PengGameDetail> getPengGameDetailList(int pengId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, pengId);
        return DaoManager.getPengGameDetailDao().queryList(PENGDETAIL_TABLE, "pengId=?",
                                                     params, "pengGameId", null);
    }
	
	
}
