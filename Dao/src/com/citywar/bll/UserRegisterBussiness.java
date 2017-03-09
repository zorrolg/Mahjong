package com.citywar.bll;

import java.sql.Types;

import com.citywar.dice.dao.UserRegisterDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserRegister;

public class UserRegisterBussiness {
	
	public static UserRegister doSearchById(int id)
	{
		DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, id);
        return DaoManager.getUserRegisterDao().query("t_u_register", "versionId=?", params);
	}
	
	public static UserRegister doSearchByMacId(String machineryId)
	{
		DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, machineryId);
        return DaoManager.getUserRegisterDao().query("t_u_register", "machinery_id=?", params);
	}
	
	
	/** 增加一次注册账号*/
	public static void addOneCount(String machineryId)
	{
		UserRegisterDao dao =DaoManager.getUserRegisterDao();
		UserRegister userRegister =  doSearchByMacId(machineryId);
		if(userRegister != null )
		{
			userRegister.setHasResCount(userRegister.getHasResCount()+1);
			dao.update(userRegister);
		}
		else{
			userRegister = new UserRegister();
			userRegister.setMachineryId(machineryId);
			userRegister.setHasResCount(1);
			dao.insert(userRegister);
		}
		
	}
}
