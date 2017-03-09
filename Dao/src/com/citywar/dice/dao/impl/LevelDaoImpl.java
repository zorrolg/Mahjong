/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.LevelDao;
import com.citywar.dice.entity.LevelInfo;

/**
 * 等级 DAO -- 实现
 * 
 * @author tracy
 * @date 2011-12-29
 * @version
 * 
 */
public class LevelDaoImpl extends BaseDaoImpl implements LevelDao
{

    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        LevelInfo info = new LevelInfo();
        info.setLevel(rs.getInt("Level"));
        info.setTitle(rs.getString("Title"));
        info.setGp(rs.getInt("GP"));
        info.setDrunkFullLevel(rs.getInt("DrunkFullLevel"));
        info.setRansomMoney(rs.getInt("RansomMoney"));
        return info;
    }

}
