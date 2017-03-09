/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.ItemTemplateInfo;

/**
 * @author tracy
 * @date 2011-12-20
 * @version
 * 
 */
public class ItemTemplateBussiness
{
    public ItemTemplateInfo getItemTemplateInfoById(int templateId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, templateId);
        return DaoManager.getItemTemplateDao().query("t_s_itemtemplate",
                                                     "templateid=?", params);
    }

    public List<ItemTemplateInfo> getAllItemTemplateInfos()
    {
        return DaoManager.getItemTemplateDao().queryList("t_s_itemteplate",
                                                         null, null);
    }
}
