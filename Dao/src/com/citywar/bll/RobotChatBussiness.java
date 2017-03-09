package com.citywar.bll;

import java.util.List;

import com.citywar.dice.dao.RobotChatDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.RobotChatInfo;

/**
 * @author shanfeng.cao
 * @date 2012-07-10
 * @version
 * 
 */
public class RobotChatBussiness {

	private final static String USER_CHAT = "t_u_chat";

	/**
	 * 查询所有机器人对话语句集合
	 * 
	 * @return List<RobotChatInfo>
	 */
	public static List<RobotChatInfo> getAllRobotChat() {
		DBParamWrapper params = new DBParamWrapper();
		return DaoManager.getRobotChtDao().queryList(USER_CHAT, " regex IS NOT NULL ", params);
	}

	public static boolean insertRobotChatInfos(List<RobotChatInfo> inserts) {
		boolean result = true;
		RobotChatDao dao = DaoManager.getRobotChtDao();
		for (RobotChatInfo info : inserts) {
			int id = dao.insert(info);
			if (id <= 0) {
				result = false;
				break;
			}
			info.setId(id);
		}
		return result;
	}

	public static boolean insertRobotChatInfo(RobotChatInfo insert) {
		boolean result = true;
		RobotChatDao dao = DaoManager.getRobotChtDao();
		int id = dao.insert(insert);
		if (id <= 0) {
			result = false;
		}
		return result;
	}

	public static int getRobotChatInfoCount() {
		RobotChatDao dao = DaoManager.getRobotChtDao();
		return dao.getRobotChatCount();
	}
}
