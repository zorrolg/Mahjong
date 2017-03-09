/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.db;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * jdbc 对 sql 执行参数封装类
 * 
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public class DBParameter
{
    private final int dbtype;
    private Object info;

    private static final Logger logger = Logger.getLogger(DBParameter.class.getName());

    /**
     * 设置存储过程访问参数
     * 
     * @param ParameterDirection
     *            设置参数传入、传出,指定范围:ParameterDirection.Input;ParameterDirection.
     *            Output ; ParameterDirection.InputOutput
     * @param Types
     *            设置参数数据类型，指定范围：Types.INTEGER;Types.VARCHAR;Types.DATE;
     * @param info
     *            设置参数值，类型为输入时，不能为空；
     */
    private int getType(Object o)
    {
        String name = o.getClass().getSimpleName();
        if (name.equals(Integer.class.getSimpleName()))
        {
            return Types.INTEGER;
        }

        if (name.equals(Float.class.getSimpleName()))
        {
            return Types.FLOAT;
        }

        if (name.equals(Long.class.getSimpleName()))
        {
            return Types.BIGINT;
        }
        else if (name.equals(String.class.getSimpleName()))
        {
            return Types.VARCHAR;
        }
        else if (name.equals(Byte.class.getSimpleName()))
        {
            return Types.INTEGER;
        }
        else if (name.equals(Short.class.getSimpleName()))
        {
            return Types.INTEGER;
        }
        else if (name.equals(Boolean.class.getSimpleName()))
        {
            return Types.BOOLEAN;
        }

        else if (name.equals(Date.class.getSimpleName()))
        {
            return Types.DATE;
        }
        else if (name.equals(Timestamp.class.getSimpleName()))
        {
            return Types.DATE;
        }
        else if (name.equals(byte[].class.getSimpleName()))
        {
            return Types.VARBINARY;
        }
        return 0;
    }

    private void checkMatch(int type, Object info)
    {
        // 检查类型一致性
        if (info == null)
        {
            return;
        }
        int tempType = type;
        if (type == Types.INTEGER || type == Types.TINYINT
                || type == Types.SMALLINT)
        {
            tempType = Types.INTEGER;
        }
        if (type == Types.TIMESTAMP)
        {
            tempType = Types.DATE;
        }

        if (type == Types.FLOAT)
            tempType = Types.FLOAT;

        if (type == Types.BLOB)
        {
            tempType = Types.VARBINARY;
        }
        try
        {
            if (tempType != getType(info))
            {
                System.err.println("type match error:"
                        + info.getClass().getSimpleName());
                throw new Exception("type match error:"
                        + info.getClass().getSimpleName());
            }
        }
        catch (Exception e)
        {
            logger.error("[ DBParameter : checkMatch ]", e);
        }
    }

    public DBParameter(int type, Object info)
    {
        this.dbtype = type;
        this.info = info;
        if (DBPoolManager.getPropertiesFile().getProperty("DataTypeCheck").equalsIgnoreCase("1"))
            checkMatch(type, info);
    }

    /**
     * 设置存储过程访问参数（限为输出类型）
     * 
     * @param ParameterDirection
     *            设置参数传入、传出,指定范围:ParameterDirection.Input;ParameterDirection.
     *            Output ;ParameterDirection.InputOutput
     * @param Types
     *            设置参数数据类型，指定范围：Types.INTEGER;Types.VARCHAR;Types.DATE;
     */
    public DBParameter(int Types)
    {
        this.dbtype = Types;
    }

    public int getDbtype()
    {
        return dbtype;
    }

    public Object getResult()
    {
        return info;
    }

    public void setResult(Object result)
    {
        this.info = result;
    }
}
