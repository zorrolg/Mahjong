/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 
 * DB 层基本操作接口
 * 
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public interface DBBaseManger
{
    /**
     * 返回引响行数<Insert,Update>
     * 
     * @param sqlText
     * @param params
     * @return
     * @throws SQLException
     */
    int executeNoneQuery(String sqlText, Map<Integer, DBParameter> params)
            throws SQLException;

    /**
     * 统计返回最大值、或总数
     * 
     * @param sqlText
     * @param params
     * @return
     * @throws SQLException
     */
    int executeCountQuery(String sqlText, Map<Integer, DBParameter> params)
            throws SQLException;

    /**
     * 返回引响行数
     * 
     * @param sqlText
     * @return
     * @throws SQLException
     */
    int executeNoneQuery(String sqlText) throws SQLException;

    /**
     * 返回自增id
     * 
     * @param sqlText
     * @return
     * @throws SQLException
     */
    int executeLastId(String sqlText) throws SQLException;

    /**
     * 返回自增id
     * 
     * @param sqlText
     * @param params
     * @return
     * @throws SQLException
     */
    int executeLastId(String sqlText, Map<Integer, DBParameter> params)
            throws SQLException;

    /**
     * 返回数据集<Select>
     * 
     * @param sqlText
     * @param params
     * @return
     * @throws SQLException
     */
    PreparedStatement executeQuery(String sqlText,
            Map<Integer, DBParameter> params) throws SQLException;

    /**
     * 返回数据集<Select>
     * 
     * @param sqlText
     * @return
     * @throws SQLException
     */
    PreparedStatement executeQuery(String sqlText) throws SQLException;

    /**
     * 获取最大ID
     * 
     * @param type
     * @return
     * @throws SQLException
     */
    long getMaxId(short type) throws SQLException;

    /**
     * 关闭连接对象, 释放连接
     * 
     * @param pstmt
     * @param rs
     */
    void closeConnection(PreparedStatement pstmt, ResultSet rs);

}
