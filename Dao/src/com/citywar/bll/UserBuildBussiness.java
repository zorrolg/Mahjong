package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.UserBuildDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserBuildInfo;

public class UserBuildBussiness {
	
	private final static String BUILD_TABLE = "t_u_build";//静态的任务数据表
	


	
	
	public static boolean insertUserBuild(UserBuildInfo userBuildInfo) {
		return DaoManager.getPlayerBuildInfoDao().insert(userBuildInfo) > 0;
	}
	
	
	public static List<UserBuildInfo> getUserBuildsInfo(int userId) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		return DaoManager.getPlayerBuildInfoDao().queryList(BUILD_TABLE, "UserId=?", params);
	}
	
	
	
	public static int updateUserBuildsInfo(UserBuildInfo info) {
		
//		DBParamWrapper params = new DBParamWrapper();
//		
//		params.put(Types.INTEGER, info.getBuildTypeId());
//		params.put(Types.INTEGER, info.getBuildLevel());
//		params.put(Types.INTEGER, info.getBuildIsUpgrade());
//		params.put(Types.DATE, info.getBuildUpgradeTime());
//		
//		params.put(Types.INTEGER, info.getPlayerId());//在更新的时候，可以直接用主键来确定用户

		return DaoManager.getPlayerBuildInfoDao().update(info);
	
	}
	
	
	
	
	public static boolean insertUserBuild(List<UserBuildInfo> insertUserBuilds) {
		boolean result = true;
		UserBuildDao dao = DaoManager.getPlayerBuildInfoDao();
		for (UserBuildInfo info : insertUserBuilds) {
			int id = dao.insert(info);
			if (id <= 0) {
				result = false;
				break;
			}
//			info.setId(id);
		}
		return result;
	}

	public static boolean updateUserBuild(List<UserBuildInfo> insertUserBuilds) {
		boolean result = true;
		UserBuildDao dao = DaoManager.getPlayerBuildInfoDao();
		for (UserBuildInfo info : insertUserBuilds) {
			if (dao.update(info) <= 0) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	
	
}



