package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.UserPropertyDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserPropertyInfo;

/**
 * @author shanfeng.cao
 * @date 2012-09-04
 * @version
 * 
 */
public class UserPropertyBussiness {

	/**
	 * 根据用户id查询玩家信息（实名制，防沉迷系统）
	 * 
	 * @param userId
	 * @return UserPropertyInfo
	 */
	 public static UserPropertyInfo getPlayerUserProperty(int userId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        return DaoManager.getUserPropertyDao().query("t_u_property", "user_id = ?",
                                                   params);
    }

	public static boolean insertUserProperty(UserPropertyInfo insertUserProperty) {
		boolean result = true;
		UserPropertyDao dao = DaoManager.getUserPropertyDao();
		int id = dao.insert(insertUserProperty);
		if (id <= 0) {
			result = false;
		}
		return result;
	}
	
	public static boolean insertUserPropertys(List<UserPropertyInfo> insertUserPropertys) {
		boolean result = true;
		UserPropertyDao dao = DaoManager.getUserPropertyDao();
		for (UserPropertyInfo info : insertUserPropertys) {
			int id = dao.insert(info);
			if (id <= 0) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	public static boolean updateUserPropertys(List<UserPropertyInfo> insertUserPropertys) {
		boolean result = true;
		UserPropertyDao dao = DaoManager.getUserPropertyDao();
		for (UserPropertyInfo info : insertUserPropertys) {
			if (dao.update(info) <= 0) {
				result = false;
				break;
			}
		}
		return result;
	}
}
