/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.UserItemDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.ItemTemplateInfo;
import com.citywar.dice.entity.UserItemInfo;

/**
 * @author tracy
 * @date 2011-12-19
 * @version
 * 
 */
public class ItemBussiness
{
    public static boolean addUserItems(List<UserItemInfo> infos)
    {
        boolean result = true;
        UserItemDao dao = DaoManager.getUserItemDao();

        for (UserItemInfo info : infos)
        {
            int userItemId = dao.insert(info);
            if (userItemId <= 0)
            {
                result = false;
                break;
            } else {
            	info.setItemId(userItemId);
            }

        }
        return result;
    }

    public static List<UserItemInfo> getAllUserItemInfos()
    {
        return DaoManager.getUserItemDao().queryList("t_u_item", null, null);
    }

    public static List<UserItemInfo> getAllUserItemInfos(String table)
    {
        return DaoManager.getUserItemDao().queryList(table, null, null);
    }

    public static UserItemInfo getUserItemInfoByItemId(int itemId)
    {
            DBParamWrapper params = new DBParamWrapper();
            params.put(Types.INTEGER, itemId);
        return DaoManager.getUserItemDao().query("t_u_item", "ItemId=?", params);
    }

    public static List<UserItemInfo> getItemInfos(int userId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        return DaoManager.getUserItemDao().queryList("t_u_item", "UserId=? and IsExist=1",
                                                     params);
    }

    public static List<ItemTemplateInfo> getAllTemplateInfos()
    {
        return DaoManager.getItemTemplateDao().queryList("t_s_itemtemplate",
                                                         null, null);
    }

    /**
     * 更新玩家背包物品数量
     * 
     * @param userId
     * @param items
     * @return
     */
    public static boolean updateItemCount(int userId, List<UserItemInfo> items)
    {
        boolean result = true;

        UserItemDao dao = DaoManager.getUserItemDao();
        for (UserItemInfo info : items)
        {
            DBParamWrapper params = new DBParamWrapper();
            params.put(Types.INTEGER, info.getCount());
            params.put(Types.INTEGER, userId);
            params.put(Types.INTEGER, info.getTemplateId());
            if (dao.update("t_u_item", "Count=?", "UserId=? and TemplateId=?",
                           params) > 0)
            {
                result = false;
                break;
            }
        }

        return result;
    }
    
    /**
     * 更新玩家背包物品属性
     * 
     * @param userId
     * @param items
     * @return
     */
    public static boolean updateItem(List<UserItemInfo> items)
    {
        boolean result = true;
        for (UserItemInfo info : items)
        {
        	if( ! (DaoManager.getUserItemDao().update(info) > 0)) {
        		result = false;
				break;
        	}
        }

        return result;
    }

    public static boolean addUserItem(UserItemInfo info)
    {
        int userItemId = DaoManager.getUserItemDao().insert(info);
    	if(userItemId > 0) {
    		info.setItemId(userItemId);
    		return true;
    	}
        return false;
    }
}
