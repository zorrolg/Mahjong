package com.citywar.bll;

import java.sql.Types;
import java.util.Collection;
import java.util.List;

import com.citywar.dice.dao.UserLetterDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserLetter;

public class UserLetterBussiness {
	
	public static boolean insertEntity(UserLetter insertData) {
		boolean result = true;
		UserLetterDao dao = DaoManager.getUserLetterDao();
        int id = dao.insert(insertData);
		if (id <= 0) {
			return false;
		}
		insertData.setId(id);
		return result;
	}
	
	public static boolean insertEntity(	Collection<UserLetter> insertDatas){
		boolean result = true;
		UserLetterDao dao = DaoManager.getUserLetterDao();
		for (UserLetter info : insertDatas) {
			int id = dao.insert(info);
			if (id <= 0) {
				result = false;
				break;
			}
			info.setId(id);
		}
		return result;
	}
	
	public static List<UserLetter> selectNoReadLetter(int selfId)
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, selfId);
		return DaoManager.getUserLetterDao().queryList("t_u_letter", "( type = 0 or  type = 3) and receiver_id=? ", params, "send_time asc","");
	}
	
	public static List<UserLetter> selectAddFriendLetter(int selfId)
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, selfId);
		return DaoManager.getUserLetterDao().queryList("t_u_letter", "( type = 1 or type =2 ) and receiver_id=? ", params, "send_time asc","");
	}
	
	public static void delete(List<UserLetter> deleteData){
		
		UserLetterDao dao = DaoManager.getUserLetterDao();
		DBParamWrapper params = null;
		for (UserLetter info : deleteData) {
			params = new DBParamWrapper();
			params.put( Types.INTEGER,info.getId() );
			dao.delete("t_u_letter", "id =?", params);
		}
	}
	
	
	
	
	


}
