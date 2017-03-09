/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll;

import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.GiftTemplateInfo;

/**
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version
 * 
 */
public class GiftTemplateBussiness
{
    public static List<GiftTemplateInfo> getAllGiftTemplateInfos()
    {
        return DaoManager.getGiftTemplateDao().queryList("t_s_gift_template",
                                                         null, null);
    }
}
