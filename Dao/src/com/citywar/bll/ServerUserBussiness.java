package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.ServerUser;

public class ServerUserBussiness {
	
	private final static String DATA_TABLE = "t_s_user";//静态的任务数据表
	
	
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<ServerUser> getInfo()
	{
		return DaoManager.getServerUserDao().queryList(DATA_TABLE, null, null);
	}
	
	
	
	
	/**
     * 根据 Id 查询玩家信息
     * 
     * @param userId
     * @return
     */
    public static ServerUser getUserCardsById(int userId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        return DaoManager.getServerUserDao().query(DATA_TABLE, "Id=?",
                                                   params);
    }
    
    
    

	public static int addUserCardsInfo(int userId, int cardCount) {
		
		DBParamWrapper params = new DBParamWrapper();
		
		params.put(Types.INTEGER, cardCount);		
		params.put(Types.INTEGER, userId);//在更新的时候，可以直接用主键来确定用户

		return DaoManager.getPlayerCardInfoDao().update(DATA_TABLE, "`cardCount` = `cardCount` + ?", "Id=?", params);
	
	}


	public static int subUserCardsInfo(int userId, int cardCount) {
		
		DBParamWrapper params = new DBParamWrapper();
		
		params.put(Types.INTEGER, cardCount);		
		params.put(Types.INTEGER, userId);//在更新的时候，可以直接用主键来确定用户

		return DaoManager.getPlayerCardInfoDao().update(DATA_TABLE, "`cardCount` = `cardCount` - ?", "Id=?", params);
	
	}

}
