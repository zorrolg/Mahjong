package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.UserReferenceLogDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserReferenceLogInfo;


public class ReferenceLogBussiness {
	
	private final static String USER_REF_LOG_TABLE = "t_u_ref_log";//奴隶日志表
	
	/**
	 * 根据用户id查询所有奴隶日志集合
	 * @param userId count
	 * @return List<UserReferenceLogInfo>
	 */
	public static List<UserReferenceLogInfo> getUserAllReferencesLogLimit(int userId, int count) 
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		String limit = "";
		if(0 != count) {
			limit = String.valueOf(count);
		}
		return DaoManager.getUserReferenceLogDao().queryList(USER_REF_LOG_TABLE, "owner_user_id=? and log_type!=-1", params, "log_create_time desc", limit);
	}
	
	public static boolean insertUserReferenceLogs(List<UserReferenceLogInfo> insertReferences) {
		boolean result = true;
		UserReferenceLogDao dao = DaoManager.getUserReferenceLogDao();
		for (UserReferenceLogInfo info : insertReferences) {
			int id = dao.insert(info);
			if (id <= 0) {
				result = false;
				break;
			}
			info.setLogId(id);
		}
		return result;
	}

	public static boolean updateUserReferenceLogs(List<UserReferenceLogInfo> insertReferences) {
		boolean result = true;
		UserReferenceLogDao dao = DaoManager.getUserReferenceLogDao();
		for(UserReferenceLogInfo info : insertReferences) {
			if (dao.update(info) <= 0) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	public static List<UserReferenceLogInfo> getUserAllReferencesLog() 
	{
		DBParamWrapper params = new DBParamWrapper();
		return DaoManager.getUserReferenceLogDao().queryList(USER_REF_LOG_TABLE, " `log_type` = '0' AND `log_create_time` > '2012-06-26 21:16:07' ORDER BY owner_user_id DESC,passives_user_id DESC ", params);
	}
}
