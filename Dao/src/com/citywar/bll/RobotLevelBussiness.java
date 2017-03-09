package com.citywar.bll;

import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.RobotLevelInfo;

/**
 * @author shanfeng.cao
 * @date 2012-07-10
 * @version
 * 
 */
public class RobotLevelBussiness {

	private final static String USER_LEVEL = "t_s_robot_level";

	/**
	 * 查询所有机器人对话语句集合
	 * 
	 * @return List<RobotChatInfo>
	 */
	public static List<RobotLevelInfo> getAllRobotLevel() {
		DBParamWrapper params = new DBParamWrapper();
		return DaoManager.getRobotLevelDao().queryList(USER_LEVEL, "", params);
	}

	
}
