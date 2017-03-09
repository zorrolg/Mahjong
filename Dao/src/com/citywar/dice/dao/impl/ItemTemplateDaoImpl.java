/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.ItemTemplateDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.ItemTemplateInfo;

/**
 * 物品模板 DAO -- 接口
 * 
 * @author tracy
 * @date 2011-12-19
 * @version
 * 
 */
public class ItemTemplateDaoImpl extends BaseDaoImpl implements ItemTemplateDao
{
    private static final Logger logger = Logger.getLogger(ItemTemplateDaoImpl.class.getName());

    public int insert(Object obj)
    {
        int intResult = -1;
        ItemTemplateInfo info = (ItemTemplateInfo) obj;
        String sqlText = " INSERT INTO `t_s_itemtemplate` (`TemplateId`, `Name`, `ResPath`, `NeedLevel`, `Type`) VALUES (?, ?, ?, ?, ?, ?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, info.getTemplateId());
        params.put(Types.VARCHAR, info.getName());
        params.put(Types.VARCHAR, info.getResPath());
        params.put(Types.INTEGER, info.getNeedLevel());
        params.put(Types.INTEGER, info.getType());

        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ ItemTemplateDaoImpl : insert ]", e);
        }
        return intResult;
    }

    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        ItemTemplateInfo info = new ItemTemplateInfo();
        info.setTemplateId(rs.getInt("TemplateId"));
        info.setName(rs.getString("Name"));
        info.setResPath(rs.getString("ResPath"));
        info.setNeedLevel(rs.getInt("NeedLevel"));
        info.setType(rs.getInt("Type"));
        info.setPropertyDesc(rs.getString("PropertyDesc"));
        info.setProbability(rs.getInt("Probability"));
        info.setPrar1(rs.getInt("Para_1"));
        return info;
    }
}
