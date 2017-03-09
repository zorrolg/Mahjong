/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.BaseDao;
import com.citywar.dice.db.DBBaseManger;
import com.citywar.dice.db.DBManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.util.CommonUtil;

/**
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public abstract class BaseDaoImpl implements BaseDao
{
    private final DBBaseManger dbManager;

    private static final Logger logger = Logger.getLogger(BaseDaoImpl.class.getName());

    public BaseDaoImpl()
    {
        dbManager = new DBManager();
    }

    @Override
    public DBBaseManger getDbManager()
    {
        return this.dbManager;
    }

    /************************** 父类通用实现 **************************/

    @Override
    public String getPage(String tableName, String fields, int pageSize,
            int pageNow, String strWhere, String strOrder)
    {
        int beginRow = 0;
        String limitString;
        beginRow = (pageNow - 1) * pageSize;
        limitString = " LIMIT " + beginRow + ", " + pageSize;
        String sqlString = "SELECT " + fields + " FROM " + tableName
                + "  Where " + strWhere + "  Order By " + strOrder
                + limitString + ";";
        return sqlString;
    }

    @Override
    public String getPageByJoinSql(String join, int pageSize, int pageNow,
            String strWhere, String orderString)
    {
        int beginRow = 0;
        String limitString;
        beginRow = (pageNow - 1) * pageSize;
        limitString = " LIMIT " + beginRow + ", " + pageSize;
        String sqlString = "SELECT " + join + "  Where " + strWhere
                + "  Order By " + orderString + limitString + ";";
        return sqlString;
    }

    @Override
    public String getOneValue(String tableName, String filedToQuery,
            String strWhere, DBParamWrapper params)
    {
        List<String> tempList = getOneValueList(tableName, filedToQuery,
                                                strWhere, params, null, null);
        if (tempList == null)
            return null;
        return tempList.get(0);
    }

    @Override
    public List<String> getOneValueList(String tableName, String filedToQuery,
            String strWhere, DBParamWrapper params, String strOrder,
            String strLimit)
    {
        List<String> result = new ArrayList<String>();
        List<List<String>> tempResult = getMultiValueList(tableName,
                                                          filedToQuery,
                                                          strWhere, params,
                                                          strOrder, strLimit);
        if (tempResult == null)
            return null;
        for (List<String> strList : tempResult)
        {
            result.add(strList.get(0));
        }
        return result;

    }

    @Override
    public List<List<String>> getMultiValueList(String tableName,
            String filedsToQuery, String strWhere, DBParamWrapper params,
            String strOrder, String strLimit)
    {

        String sqlText = joinSqlSelect(String.format("select %s from %s ",
                                                     filedsToQuery, tableName),
                                       strWhere, strOrder, strLimit);

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DBManager db = new DBManager();
        List<List<String>> resultList = new ArrayList<List<String>>();
        try
        {
            pstmt = db.executeQuery(sqlText,
                                    params == null ? null : params.getParams());

            if (pstmt != null)
            {
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    List<String> rowData = new ArrayList<String>();
                    for (String field : filedsToQuery.split(","))
                    {
                        rowData.add(rs.getString(field));
                    }
                    resultList.add(rowData);
                }

            }
        }
        catch (Exception e)
        {
            logger.error("[ BaseDaoImpl : getMultiValueList ]", e);
        }
        finally
        {
            db.closeConnection(pstmt, rs);
        }
        return resultList;
    }

    @Override
    public int update(String tableName, String strUpdate, String strWhere,
            DBParamWrapper params)
    {
        String sqlUpdate = String.format("update %s set %s where %s",
                                         tableName, strUpdate, strWhere);
        DBManager db = new DBManager();
        try
        {
            return db.executeNoneQuery(sqlUpdate, params.getParams());
        }
        catch (Exception e)
        {
            logger.error("[ BaseDaoImpl : update ]", e);
        }
        finally
        {
        }
        return -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T query(String tableName, String strWhere, DBParamWrapper params)
    {
        String sqlText = joinSqlSelect(" select * from " + tableName, strWhere,
                                       null, null);
        T t = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = getDbManager().executeQuery(sqlText,
                                                params == null ? null
                                                        : params.getParams());
            if (pstmt != null)
            {
                rs = pstmt.executeQuery();
                if (rs.last())
                {
                    t = ((T) getTemplate(rs));
                }
            }
        }
        catch (Exception e)
        {
            logger.error("[ BaseDaoImpl : query ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }
        return t;
    }

    @Override
    public <T> List<T> queryList(String tableName, String strWhere,
            DBParamWrapper params)
    {
        return queryList(tableName, strWhere, params, null, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> queryList(String tableName, String strWhere,
            DBParamWrapper params, String strOrder, String strLimit)
    {
        String sqlText = joinSqlSelect(" select * from " + tableName, strWhere,
                                       strOrder, strLimit);
        List<T> resultList = new ArrayList<T>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = getDbManager().executeQuery(sqlText,
                                                params == null ? null
                                                        : params.getParams());
            if (pstmt != null)
            {
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    resultList.add((T) getTemplate(rs));
                }
            }
        }
        catch (Exception e)
        {
            logger.error("[ BaseDaoImpl : queryList ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }
        return resultList;

    }

    /**
     * 拼凑 sql 语句的辅助函数 [ add by tracy : 2011-12-19 ]<br>
     * 
     * @param sourceSql
     *            原始SQL语句
     * @param strWhere
     *            查询条件 WHERE
     * @param strOrder
     *            排序条件 ORDER BY
     * @param strLimit
     *            限制结果数 LIMIT ?,?
     * @return
     */
    protected String joinSqlSelect(String sourceSql, String strWhere,
            String strOrder, String strLimit)
    {
        StringBuilder sbSql = new StringBuilder(1000);
        sbSql.append(sourceSql);
        if (!CommonUtil.isNullOrEmpty(strWhere))
        {
            sbSql.append(" where ");
            sbSql.append(strWhere);
        }
        if (!CommonUtil.isNullOrEmpty(strOrder))
        {
            sbSql.append(" order by ");
            sbSql.append(strOrder);
        }
        if (!CommonUtil.isNullOrEmpty(strLimit))
        {
            sbSql.append(" limit ");
            sbSql.append(strLimit);
        }
        return sbSql.toString();
    }

    @Override
    public int delete(String tableName, String strWhere, DBParamWrapper paramss)
    {
        String sqlDel = String.format("delete from %s where %s", tableName,
                                      strWhere);
        try
        {
            return getDbManager().executeNoneQuery(sqlDel, paramss.getParams());
        }
        catch (Exception e)
        {

        }
        return -1;
    }

    /************************** end 父类通用实现 end *************************/

    /****************************** 空实现, 子类可在必要的时候自行实现 **********************************/

    @Override
    public int insert(Object obj)
    {
        return 0;
    }

    @Override
    public int update(Object obj)
    {
        return 0;
    }

    @Override
    public int update(Object obj, String strWhere, DBParamWrapper params)
    {
        return 0;
    }

    @Override
    public int update(String strUpdate, String strWhere, DBParamWrapper paramss)
    {
        return 0;
    }

    /****************************** 空实现 end **********************************/

    /*
     * ~~~~~~~~~~~~~~~~~ ! 华 ! 丽 ! 丽 ! 的 ! 分 ! 割 ! 线 ! ~~~~~~~~~~~~~~~~~
     */

    /****************************** 需要子类个性化实现 **********************************/

    @Override
    public abstract Object getTemplate(ResultSet rs) throws SQLException;

    /****************************** 需要子类个性化实现 end **********************************/
}
