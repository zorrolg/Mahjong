/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll;

import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.GiftBagItem;

/**
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version
 * 
 */
public class GiftBagBussiness
{
	/** 查询所有礼包*/
    public static List<GiftBagItem> getAllGiftBag()
    {
        return DaoManager.getGiftBagDao().queryList("t_s_gift_bag_item",
                                                         null, null);
    }
}
