package com.citywar.bll;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.citywar.dice.dao.UserReferenceDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.PlayerSlaveCount;
import com.citywar.dice.entity.UserRefWorkInfo;
import com.citywar.dice.entity.UserReferenceInfo;


public class ReferenceBussiness {
	
	private final static String USER_REF_VIEW = "v_ref";//奴隶视图
	
	/**
	 * 根据用户id查询所有奴隶集合
	 * @param userId
	 * @return List<UserReferenceInfo>
	 */
	public static List<UserReferenceInfo> getAllUserReferencesLimit(int userId, int count) 
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		String limit = "";
		if(0 != count) {
			limit = String.valueOf(count);
		}
		return DaoManager.getUserReferenceDao().queryList(USER_REF_VIEW, "master_user_id=?", params, "create_time desc", limit);
	}
	
	/**
	 * 根据查询所有玩家没被抢的奴隶数
	 * 
	 * @return List<UserReferenceInfo>
	 */
	public static List<PlayerSlaveCount> getUseReferencesCount() {
		return DaoManager.getUserReferenceDao().getUseReferencesCount();
	}
	
	/**
	 * 根据查询所有玩家奴隶数最多的一些玩家数量
	 * 
	 * @return List<UserReferenceInfo>
	 */
	public static List<PlayerSlaveCount> getTopUseReferencesCount(int topCount) {
		return DaoManager.getUserReferenceDao().getTopUseReferencesCount(topCount);
	}
	
	/**
	 * 根据用户id查询所有有效的奴隶集合
	 * @param userId
	 * @return List<UserReferenceInfo>
	 */
	public static List<UserReferenceInfo> getUserAllNowValidReferencesLimit(int userId, Timestamp validityTime, int count) 
	{
		if(null == validityTime) {
			return null;
		}
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
        params.put(Types.TIMESTAMP, validityTime);
		String limit = "";
		if(0 != count) {
			limit = String.valueOf(count);
		}
		return DaoManager.getUserReferenceDao().queryList(USER_REF_VIEW, "master_user_id=? and " +
				" (remove_time>? OR take_user_id=0) ", params, "create_time desc", limit);
	}
	
	public static boolean insertUserReferences(List<UserReferenceInfo> insertReferences) {
		boolean result = true;
		UserReferenceDao dao = DaoManager.getUserReferenceDao();
		for (UserReferenceInfo info : insertReferences) {
			int id = dao.insert(info);
			if (id <= 0) {
				result = false;
				break;
			}
			info.setId(id);
		}
		return result;
	}

	public static boolean updateUserReferences(List<UserReferenceInfo> updateReferences) {
		boolean result = true;
		UserReferenceDao dao = DaoManager.getUserReferenceDao();
		for(UserReferenceInfo info : updateReferences) {
			if (dao.update(info) <= 0) {
				result = false;
				break;
			}
		}
		return result;
	}

	public static UserReferenceInfo getMasterInfoByUserId(int userId) {
		UserReferenceInfo userReferenceInfo = null;
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		List<UserReferenceInfo> list = DaoManager.getUserReferenceDao().queryList(USER_REF_VIEW, "slaves_user_id = ? AND take_user_id =0", params, "create_time desc", null);
		if(null != list && list.size()>0) {
			userReferenceInfo = list.get(0);
			list.remove(0);
			List<UserReferenceInfo> updateList =  new ArrayList<UserReferenceInfo>();
			for(UserReferenceInfo info : list) {
				if(null != info) {
					info.setTakeUserId(info.getSlavesUserId());
//					info.setRemoveTime(new Timestamp(System.currentTimeMillis()));
					info.setTakeUserName(info.getSlavesUserName());
					info.setTakeUserPicPath(info.getSlavesPicPath());
					updateList.add(info);
				}
			}
			updateUserReferences(updateList);
		}
		return userReferenceInfo;
	}

	/**
	 * 根据用户id查询所有有效的奴隶主集合（有的奴隶主过期了但是还没收税金）
	 * @param userId
	 * @return List<UserReferenceInfo>
	 */
	public static List<UserReferenceInfo> getUserAllNowValidMasterInfoByUserId(int userId, Timestamp validityTime, int count) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
        params.put(Types.TIMESTAMP, validityTime);
		String limit = "";
		if(0 != count) {
			limit = String.valueOf(count);
		}
		List<UserReferenceInfo> list = DaoManager.getUserReferenceDao().queryList(USER_REF_VIEW, "slaves_user_id = ?" +
				" and remove_time <? and income_coins > 0", params, "create_time desc", limit);
		return list;
	}
		
	public static List<UserRefWorkInfo> getRefWorkList()
	{
		return DaoManager.getUserReferenceDao().getRefWorkList();
	}
}
