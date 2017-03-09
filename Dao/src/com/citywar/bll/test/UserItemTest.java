/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.bll.test;

import java.util.ArrayList;
import java.util.List;

import com.citywar.bll.ItemBussiness;
import com.citywar.dice.entity.UserItemInfo;

/**
 * @author tracy
 * @date 2011-12-19
 * @version 
 *
 */
public class UserItemTest
{

    public static void queryList()
    {
        for (UserItemInfo info :ItemBussiness.getAllUserItemInfos())
        {
            System.err.println(info.getTemplateId());
        }
    }

    public List<UserItemInfo> queryAllList()
    {
        return ItemBussiness.getAllUserItemInfos();
    }

    public List<UserItemInfo> queryAllList(String table)
    {
        return ItemBussiness.getAllUserItemInfos(table);
    }
    
    public static void getSingleList(int userId)
    {
        for(UserItemInfo info : ItemBussiness.getItemInfos(userId))
        {
            System.err.println(info.getCount());
        }
    }
    
    public static void addItem(int userId, int[] templateIds)
    {
        List<UserItemInfo> infos = new ArrayList<UserItemInfo>();
        for (int templateId : templateIds)
        {
            UserItemInfo info = new UserItemInfo();
            info.setUserId(userId);
            info.setCount((short)10);
            info.setTemplateId(templateId);
            info.setExist(true);
            infos.add(info);
        }
        ItemBussiness.addUserItems(infos);
    }
    
}
