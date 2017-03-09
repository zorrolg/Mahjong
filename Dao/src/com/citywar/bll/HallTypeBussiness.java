/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.HallTypeInfo;

/**
 * @author shanfeng.cao
 * @date 2012-07-25
 * @version
 * 
 */
public class HallTypeBussiness
{
	private final static String HALL_TYPE_TABLE = "t_s_hall_type";
	
    public static HallTypeInfo getHallTypeInfoById(int hallTypeId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, hallTypeId);
        return DaoManager.getHallTypeDao().query(HALL_TYPE_TABLE,
                                                     "hall_type_id=?", params);
    }

    public static List<HallTypeInfo> getAllHallTypeInfos()
    {
        return DaoManager.getHallTypeDao().queryList(HALL_TYPE_TABLE, null, null);
    }
}
