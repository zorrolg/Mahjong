package com.citywar.bll;

import java.sql.Types;

import com.citywar.dice.dao.UserRewardDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserReward;

public class UserRewardBussiness {
	
	public static void insertEntity(UserReward insertEntity) 
	{
		UserRewardDao dao = DaoManager.getUserRewardDao();
      	int id = dao.insert(insertEntity);
		if (id > 0) {
			insertEntity.setId(id);
		}
	}
	
	public static UserReward selectByUserId(int userId)
	{
		DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        return DaoManager.getUserRewardDao().query("t_u_reward", "user_id=?", params);
	}
	
	public static boolean UpdateEntity(UserReward updateEntity)
	{
		return DaoManager.getUserRewardDao().update(updateEntity)>0;
	}
	
	
	
	


}
