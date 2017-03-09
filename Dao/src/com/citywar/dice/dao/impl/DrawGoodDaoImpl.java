/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.DrawGoodDao;
import com.citywar.dice.entity.DrawGoodInfo;


/**
 * 商店商品 DAO 实现
 * 
 * @author tracy
 * @date 2011-12-20
 * @version
 * 
 */
public class DrawGoodDaoImpl extends BaseDaoImpl implements DrawGoodDao
{

    /*
     * (non-Javadoc)
     * 
     * @see com.road.dice.dao.impl.BaseDaoImpl#getTemplate(java.sql.ResultSet)
     */
    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        DrawGoodInfo info = new DrawGoodInfo();
        info.setId(rs.getInt("id"));
        info.setDrawType(rs.getInt("draw_type"));
        info.setDrawPara(rs.getByte("draw_para"));
        info.setTemplateId(rs.getInt("templateId"));
        info.setName(rs.getString("name"));
        info.setDescription(rs.getString("description"));
        info.setCount(rs.getInt("count"));
        info.setPicPath(rs.getString("pic_path"));
        return info;
    }

}
