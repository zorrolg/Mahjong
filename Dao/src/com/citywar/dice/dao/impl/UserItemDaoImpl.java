/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserItemDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserItemInfo;

/**
 * 玩家物品 DAO -- 实现
 * 
 * @author tracy
 * @date 2011-12-19
 * @version
 * 
 */
public class UserItemDaoImpl extends BaseDaoImpl implements UserItemDao
{

    private static final Logger logger = Logger.getLogger(UserItemDaoImpl.class.getName());

    @Override
    public int insert(Object obj)
    {
        int intResult = -1;
        UserItemInfo info = (UserItemInfo) obj;
        String sqlText = " INSERT INTO `t_u_item` (`UserId`,`itemType`, `TemplateId`, `Count`, `IsExist`, `IsUsed`, `last_validity_time`) VALUES (?,?,?,?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, info.getUserId());
        params.put(Types.INTEGER, info.getItemTypeId());
        params.put(Types.INTEGER, info.getTemplateId());
        params.put(Types.INTEGER, info.getCount());
        params.put(Types.TINYINT, info.isExist() ? 1 : 0);
        params.put(Types.TINYINT, info.isUsed() ? 1 : 0);
        params.put(Types.TIMESTAMP, info.getLastValidityTime());
        try
        {
            //intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (SQLException e)
        {
            logger.error("[ UserItemDaoImpl : insert ]", e);
        }
        return intResult;
    }
    
    @Override
    public int update(Object obj)
    { 
		int intResult = -1;
        UserItemInfo info = (UserItemInfo) obj;
        String sqlText = " UPDATE " + "t_u_item" + " SET `UserId`=?,`itemType`=?, `TemplateId`=?, `Count`=?, `IsExist`=?, `IsUsed`=?, `last_validity_time`=? where ItemId = ?; ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, info.getUserId());
        params.put(Types.INTEGER, info.getItemTypeId());
        params.put(Types.INTEGER, info.getTemplateId());
        params.put(Types.INTEGER, info.getCount());
        params.put(Types.TINYINT, info.isExist() ? 1 : 0);
        params.put(Types.TINYINT, info.isUsed() ? 1 : 0);
        params.put(Types.TIMESTAMP, info.getLastValidityTime());
        params.put(Types.INTEGER, info.getItemId());
        
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ UserReferenceDaoImpl : update ]", e);
        }
        return intResult;
    }

    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        UserItemInfo info = new UserItemInfo();
        info.setItemId(rs.getInt("ItemId"));
        info.setUserId(rs.getInt("UserId"));
        info.setItemTypeId(rs.getInt("itemType"));
        info.setTemplateId(rs.getInt("TemplateId"));
        info.setCount(rs.getShort("Count"));
        info.setExist(rs.getByte("IsExist") == 1 ? true : false);
        info.setUsed(rs.getByte("IsUsed") == 1 ? true : false);
        info.setLastValidityTime(rs.getTimestamp("last_validity_time"));
        return info;
    }
}
