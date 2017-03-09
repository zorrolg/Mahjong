package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.UserGiftDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserGiftInfo;
import com.citywar.dice.entity.UserGiftIntegrationInfo;

/**
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version
 * 
 */
public class UserGiftBussiness {

	private final static String USER_GIFT_VIEW = "v_user_gift";//

	/**
	 * 根据用户id查询所有礼品集合
	 * 
	 * @param userId
	 *            count
	 * @return List<UserGiftInfo>
	 */
	public static List<UserGiftInfo> getAllUserGift(int userId, int count) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		String limit = "";
		if (0 != count) {
			limit = String.valueOf(count);
		}
		return DaoManager.getUserGiftDao().queryList(USER_GIFT_VIEW,
				"owner_user_id=?", params, "give_time desc", limit);
	}

	/**
	 * 根据用户id查询各种礼品类型的总数量
	 * 
	 * @param userId
	 * @return List<UserGiftInfo>
	 */
	public static List<UserGiftIntegrationInfo> getAllUserGiftIdAndSumCount(
			int userId) {
		return DaoManager.getUserGiftDao().selectUserGiftIdAndSumCount(userId);

	}

	public static boolean insertUserGifts(List<UserGiftInfo> insertUserGifts) {
		boolean result = true;
		UserGiftDao dao = DaoManager.getUserGiftDao();
		for (UserGiftInfo info : insertUserGifts) {
			int id = dao.insert(info);
			if (id <= 0) {
				result = false;
				break;
			}
			info.setId(id);
		}
		return result;
	}

	public static boolean updateUserGifts(List<UserGiftInfo> insertUserGifts) {
		boolean result = true;
		UserGiftDao dao = DaoManager.getUserGiftDao();
		for (UserGiftInfo info : insertUserGifts) {
			if (dao.update(info) <= 0) {
				result = false;
				break;
			}
		}
		return result;
	}
}
