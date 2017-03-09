/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.UserVIPEditionDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserVIPEditionInfo;

/**
 * @author shanfeng.cao
 * @date 2012-09-07
 * @version
 * 
 */
public class UserVIPEditionBussiness
{
    public static List<UserVIPEditionInfo> getUserVIPEdition(int userId)
    {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, userId);
        return DaoManager.getUserVIPEditionDao().queryList("t_u_vip_edition",
                                                         " user_id = ? ", params);
    }
    
    public static List<UserVIPEditionInfo> getUserVIPEdition(String machineryId)
    {
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR, machineryId);
        return DaoManager.getUserVIPEditionDao().queryList("t_u_vip_edition",
                                                         " machinery_id like ? ", params);
    }
    
    public static boolean insertUserVIPEditionInfo(UserVIPEditionInfo insertUserVIPEdition) {
		boolean result = true;
		UserVIPEditionDao dao = DaoManager.getUserVIPEditionDao();
		int id = dao.insert(insertUserVIPEdition);
		if (id <= 0) {
			result = false;
		}
		return result;
	}
}
