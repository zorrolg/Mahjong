/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.PushInfoDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.PushInfo;
import com.citywar.dice.entity.PushLogInfo;
import com.citywar.dice.entity.PushPlayerInfo;

/**
 * 机器人Business类
 * @author charles
 * @date 2012-1-5
 * @version 
 *
 */
public class PushInfoBussiness
{
	private final static String PUSH_TABLE = "t_s_push";//静态的任务数据表
	
//	private final static String PUSH_LOG_TABLE = "t_s_push_log";//随时更新的用户已经完成任务表
	
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<PushInfo> getAllPushInfo()
	{
		return DaoManager.getPushInfoDao().queryList(PUSH_TABLE, null, null);
	}
	
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<PushPlayerInfo> getPushPlayerList()
	{
		return DaoManager.getPushInfoDao().getPushPlayerList();
	}
	
	public static boolean insertPushLog(List<PushLogInfo> insertUserBuilds) {
		
		boolean result = true;
		PushInfoDao dao = DaoManager.getPushInfoDao();
		for (PushLogInfo info : insertUserBuilds) {
			int id = dao.insert(info);
			if (id <= 0) {
				result = false;
				break;
			}
//			info.setId(id);
		}
		return result;
	}
	
	
	public static void delete(List<PushInfo> deleteData){
		
		PushInfoDao dao = DaoManager.getPushInfoDao();
		DBParamWrapper params = null;
		for (PushInfo info : deleteData) {
			params = new DBParamWrapper();
			params.put( Types.INTEGER,info.getPushId() );
			dao.delete(PUSH_TABLE, "id =?", params);
		}
	}

}
