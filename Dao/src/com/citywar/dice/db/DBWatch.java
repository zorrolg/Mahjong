/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.db;

import org.apache.log4j.Logger;

import com.citywar.util.TimeUtil;

/**
 * DB 监测类, 可考虑在生产环境下关闭, 避免影响效率
 * 
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public class DBWatch
{
    public static final Logger LOGGER = Logger.getLogger(DBWatch.class.getName());

    private long first = 0;

    public DBWatch()
    {
        first = TimeUtil.getSysCurSeconds();
    }

    public void commit(String procName)
    {
        if (DBPoolManager.getPropertiesFile().getProperty("DBWatch").equals("1"))
        {
            long second = TimeUtil.getSysCurSeconds();
            long spendTime = second - first;
            if (spendTime > 1)
                LOGGER.error(String.format("执行存储过程%s花耗时间 超过:%s秒", procName,
                                           spendTime));
        }
    }
}
