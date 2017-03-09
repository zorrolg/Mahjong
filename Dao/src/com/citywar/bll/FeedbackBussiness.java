package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.FeedbackDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.FeedbackInfo;


public class FeedbackBussiness {
	
	private final static String T_U_FEEDBACK_TABLE = "t_u_feedback";//
	
	public static boolean insertUserFeedbacks(List<FeedbackInfo> insertFeedbacks) {
		boolean result = true;
		FeedbackDao dao = DaoManager.getFeedbackDao();
		for (FeedbackInfo info : insertFeedbacks) {
			int id = dao.insert(info);
			if (id <= 0) {
				result = false;
				break;
			}
			info.setId(id);
		}
		return result;
	}

	public static boolean updateUserFeedbacks(List<FeedbackInfo> updateFeedbacks) {
		boolean result = true;
		FeedbackDao dao = DaoManager.getFeedbackDao();
		for(FeedbackInfo info : updateFeedbacks) {
			if (dao.update(info) <= 0) {
				result = false;
				break;
			}
		}
		return result;
	}
	/**
	 * 根据用户id查询所有反馈集合
	 * @param userId count
	 * @return List<FeedbackInfo>
	 */
	public static List<FeedbackInfo> getUserAllFeedbackLimit(int userId, int count) 
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		String limit = "";
		if(0 != count) {
			limit = String.valueOf(count);
		}
		return DaoManager.getFeedbackDao().queryList(T_U_FEEDBACK_TABLE, "user_id=?", params, "feedback_create_time desc", limit);
	}
}
