package com.citywar.bll;

import java.sql.Types;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserMessageTokenInfo;

public class UserMessageTokenBussiness {
	
	private final static String USER_MESSAGE_TOKEN = "t_u_token";//

	public static int addUserMessageToken(UserMessageTokenInfo info)
	{
		return DaoManager.getUserMessageTokenDao().insert(info);
	}
	
	public static boolean isExistUserMessageToken(String tokenId)
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR, tokenId);
		return DaoManager.getUserMessageTokenDao().query(USER_MESSAGE_TOKEN, "token_id=?", params) != null;
	}
}
