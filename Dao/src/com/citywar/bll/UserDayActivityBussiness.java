package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.UserDayActivityDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserDayActivityInfo;

/**
 * @author shanfeng.cao
 * @date 2012-07-05
 * @version
 * 
 */
public class UserDayActivityBussiness {

	/**
	 * 每天的玩家统计信息
	 */
	private final static String USER_DAY_ACTIVITY_TABLE = "t_u_day_activity";

	public static boolean insertUserDayActivity(UserDayActivityInfo insertUserDayActivity) {
		boolean result = true;
		UserDayActivityDao dao = DaoManager.getUserDayActivityDao();
		int id = dao.insert(insertUserDayActivity);
		if (id <= 0) {
			result = false;
		}
		return result;
	}

	public static boolean updateUserDayActivity(UserDayActivityInfo insertUserDayActivity) {
		boolean result = true;
		UserDayActivityDao dao = DaoManager.getUserDayActivityDao();
		int id = dao.update(insertUserDayActivity);
		if (id <= 0) {
			result = false;
		}
		return result;
	}

	/**
	 * 根据用户id查询用户统计数据
	 * 
	 * @param userId
	 *            count
	 * @return List<UserDayActivityInfo>
	 */
	public static List<UserDayActivityInfo> selectUserDayActivity(int userId) {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
		return DaoManager.getUserDayActivityDao().queryList(USER_DAY_ACTIVITY_TABLE,
				" user_id = ? ", params, "count_date desc", "");
	}

	/**
	 * 查询所有用户统计数据
	 * 
	 *            count
	 * @return List<UserDayActivityInfo>
	 */
	public static List<UserDayActivityInfo> selectAllUserDayActivity() {
		DBParamWrapper params = new DBParamWrapper();
		return DaoManager.getUserDayActivityDao().queryList(USER_DAY_ACTIVITY_TABLE,
				"", params);
	}
	
	public static boolean clearUserDayActivity() {
		DBParamWrapper params = new DBParamWrapper();
		return DaoManager.getUserDayActivityDao().update(USER_DAY_ACTIVITY_TABLE, " `online_time`=0,`usage_counter_tea`=0 ,`usage_counter_horn`=0,`increase_coins`=0", "`online_time`!=0", params) != -1;
	}
	
	public static List<UserDayActivityInfo> getCharts(String strWhere, String orderField,int topCount)
    {
    	 DBParamWrapper params = new DBParamWrapper();
         params.put(Types.VARCHAR, orderField);
         return DaoManager.getUserDayActivityDao().queryList(USER_DAY_ACTIVITY_TABLE,strWhere,null,orderField,String.valueOf(topCount));
    }
	
	/**
     * 删除玩家多余的记录
     * 
     * @param 记录ID
     * @return 是否删除成功
     */
    public static boolean deleteUserDayActivity(int id)
    {
        return DaoManager.getUserDayActivityDao().delete(id);
    }
}
