/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.DrawGoodInfo;


/**
 * 商店商品 bll
 * 
 * @author tracy
 * @date 2011-12-20
 * @version
 * 
 */
public class DrawGoodBussiness
{
    public static List<DrawGoodInfo> getAllDrawGoods()
    {
        // 排序规则
        // 道具类商品排前, 兑换类排后
        // 消耗RMB类排前, 消耗金币
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.TINYINT, 1);
        return DaoManager.getDrawGoodDao().queryList("t_s_draw",
        		null,
        		null,
        		null,
                                                     null);
    }

 
}
