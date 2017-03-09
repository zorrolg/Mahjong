/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.ShopGoodInfo;

/**
 * 商店商品 bll
 * 
 * @author tracy
 * @date 2011-12-20
 * @version
 * 
 */
public class ShopGoodBussiness
{
    public static List<ShopGoodInfo> getAllShopGoods()
    {
        // 排序规则
        // 道具类商品排前, 兑换类排后
        // 消耗RMB类排前, 消耗金币
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.TINYINT, 1);
        return DaoManager.getShopGoodDao().queryList("t_s_shop",
                                                     "IsExsit=?",
                                                     params,
                                                     "goodtype, shoptype, price",
                                                     null);
    }

    public static List<ShopGoodInfo> getAllShopGoods(String strOrder,
            int startIndex, int pageSize)
    {
        return DaoManager.getShopGoodDao().queryList("t_s_shop",
                                                     null,
                                                     null,
                                                     strOrder,
                                                     startIndex + ","
                                                             + pageSize);
    }

    /**
     * 得到总页数
     * 
     * @param strWhere
     * @return
     */
    public static int getCount(String strWhere)
    {
        return Integer.parseInt(DaoManager.getShopGoodDao().getOneValue("t_s_shop",
                                                                        "count(*)",
                                                                        strWhere,
                                                                        null));
    }
}
