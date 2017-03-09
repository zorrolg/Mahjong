/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.db;

import java.util.Map;

/**
 * 数据库指令类，对单条sql语句进行封装
 * 
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public class DBCmd
{
    // sql 语句
    String sqlCmd;

    // 语句执行参数
    Map<Integer, DBParameter> params;

    public DBCmd(String sqlCmd, Map<Integer, DBParameter> params)
    {
        this.sqlCmd = sqlCmd;
        this.params = params;
    }

    public DBCmd(String sqlCmd)
    {
        this(sqlCmd, null);
    }

    /**
     * 获取sql指令
     * 
     * @return sql指令
     */
    public String getSqlCmd()
    {
        return sqlCmd;
    }

    /**
     * 获取指令执行参数
     * 
     * @return 指令执行参数
     */
    public Map<Integer, DBParameter> getParams()
    {
        return params;
    }
}
