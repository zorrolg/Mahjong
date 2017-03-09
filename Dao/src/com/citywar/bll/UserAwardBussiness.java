package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.UserAwardDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserAwardInfo;

/**
 * @author shanfeng.cao
 * @date 2012-07-05
 * @version
 * 
 */
public class UserAwardBussiness {

	private final static String USER_AWARD_TABLE = "t_u_award";//

	public static boolean insertUserAward(UserAwardInfo insertUserAward) {
		boolean result = true;
		UserAwardDao dao = DaoManager.getUserAwardDao();
		int id = dao.insert(insertUserAward);
		if (id <= 0) {
			result = false;
		}
		return result;
	}

	public static boolean updateUserAward(UserAwardInfo insertUserAward) {
		boolean result = true;
		UserAwardDao dao = DaoManager.getUserAwardDao();
		int id = dao.update(insertUserAward);
		if (id <= 0) {
			result = false;
		}
		return result;
	}

	/**
	 * 根据用户id查询所有礼品集合
	 * 
	 * @param userId
	 *            count
	 * @return List<UserGiftInfo>
	 */
	public static List<UserAwardInfo> selectUserDayCoinsLessAward(int userId, int count) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		String limit = "";
		if (0 != count) {
			limit = String.valueOf(count);
		}
		return DaoManager.getUserAwardDao().queryList(USER_AWARD_TABLE,
				" user_id = ? ", params, "award_last_time desc", limit);
	}
}
