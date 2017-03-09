/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.ShopGoodDao;
import com.citywar.dice.entity.ShopGoodInfo;

/**
 * 商店商品 DAO 实现
 * 
 * @author tracy
 * @date 2011-12-20
 * @version
 * 
 */
public class ShopGoodDaoImpl extends BaseDaoImpl implements ShopGoodDao
{

    /*
     * (non-Javadoc)
     * 
     * @see com.road.dice.dao.impl.BaseDaoImpl#getTemplate(java.sql.ResultSet)
     */
    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        ShopGoodInfo info = new ShopGoodInfo();
        info.setId(rs.getInt("Id"));
        info.setItemCode(rs.getString("itemCode"));
        info.setShopId(rs.getInt("ShopId"));
        info.setShopType(rs.getByte("ShopType"));
        info.setGoodType(rs.getByte("GoodType"));
        info.setTemplateId(rs.getInt("TemplateId"));
        info.setName(rs.getString("Name"));
        info.setDescription(rs.getString("Description"));
        info.setPrice(rs.getFloat("Price"));
        info.setCount(rs.getInt("Count"));
        info.setValidDay(rs.getInt("ValidDay"));
        info.setUnit(rs.getString("Unit"));
        info.setShopPicPath(rs.getString("ShopPicPath"));
        info.setNamePicPath(rs.getString("NamePicPath"));
        info.setUsePresentation(rs.getString("use_presentation"));
        return info;
    }

}
