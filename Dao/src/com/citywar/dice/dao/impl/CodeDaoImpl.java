/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.CodeDao;

/**
 * @author tracy
 * @date 2012-2-13
 * @version
 * 
 */
public class CodeDaoImpl extends BaseDaoImpl implements CodeDao
{
    private static final Logger logger = Logger.getLogger(CodeDaoImpl.class.getName());

    @Override
    public int getUserId()
    {
        // TODO 字符串 抽象出枚举
        return (int) addId("t_u_userid");
    }

    private long addId(String tableName)
    {
        String sql = String.format(" INSERT INTO %s(id)  VALUES(null);",
                                   tableName);
        int userId = -1;
        try
        {
            userId = getDbManager().executeLastId(sql);
        }
        catch (SQLException e)
        {
            logger.error("[ BaseDaoImpl : logger ]", e);
        }
        finally
        {
        }
        return userId;
    }

    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

}
