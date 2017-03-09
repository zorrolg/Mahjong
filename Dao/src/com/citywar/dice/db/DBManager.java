/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.citywar.util.StackMessagePrint;
import com.mysql.jdbc.Statement;

/**
 * DB 管理类, 提供数据库底层基本操作的接口统一实现
 * 
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public class DBManager implements DBBaseManger
{

    private Connection myConn;
    private static DBPoolManager pools;
    private static final Logger logger = Logger.getLogger(DBManager.class.getName());
    private String connName;

    // 事务监控相关处理
    private String callStack = "";

    public DBManager()
    {
        this.connName = "db_tank";
    }

    public void setConnName(String connName)
    {
        this.connName = connName;
    }

    public static boolean initConfig(String dbPath)
    {
        pools = DBPoolManager.getInstance(dbPath);
        // NDBManager.setPools(pools);
        return pools != null ? true : false;
    }

    private boolean openConnection()
    {
        try
        {
            if ((connName == null) || (connName.equals("")))
                return false;

            if (DBPoolManager.getPropertiesFile().getProperty("TransactionCheck").equals("1")
                    && myConn != null
                    && myConn.isClosed() != true
                    && myConn.getAutoCommit() != true)
            {
                logger.error("事务未被正常关闭：\n\r" + callStack);
                myConn.commit();
                myConn.setAutoCommit(true);
                myConn.close();
            }

            myConn = pools.getConnection(connName);
            if (myConn != null)
            {
                callStack = StackMessagePrint.captureStackTrace(Thread.currentThread(),
                                                                "事务状态未正常结束！");
                return true;
            }
        }
        catch (NullPointerException e)
        {
            logger.error(String.format(DBMessageType.Format2,
                                       DBMessageType.Connect_None,
                                       e.getMessage()));
        }
        catch (Exception e)
        {
            logger.error("获取链接错误", e);
        }
        return false;

    }

    private void prepareCommand(PreparedStatement pstmt,
            Map<Integer, DBParameter> parms) throws SQLException
    {
        if (parms == null)
            return;
        for (Map.Entry<Integer, DBParameter> entry : parms.entrySet())
        {
            pstmt.setObject(entry.getKey(), entry.getValue().getResult());
        }
    }

    private void closeConnection(PreparedStatement pstmt) throws SQLException
    {
        if (pstmt != null && pstmt.isClosed() == false)
        {
            pstmt.clearParameters();
            pstmt.close();
        }
        if (myConn != null && myConn.isClosed() == false)
            try
            {
                if (myConn.getAutoCommit() != true)
                {
                    logger.error("事务未被正常关闭：\n\r" + callStack);
                    myConn.commit();
                    myConn.setAutoCommit(true);
                }

                myConn.close();
            }
            catch (SQLException e)
            {
                logger.error("[ DBManager : closeConnection ]", e);
            }
    }

    public void closeConnection(PreparedStatement pstmt, ResultSet rs)
    {
        try
        {
            if (rs != null && rs.isClosed() == false)
                rs.close();
            closeConnection(pstmt);
        }
        catch (SQLException e)
        {
            logger.error("[ DBManager : closeConnection ]", e);
            logger.error(String.format(DBMessageType.Format3,
                                       DBMessageType.Connect_Close,
                                       e.getMessage(), myConn.toString()));
        }
    }

    /**
     * 提供Connection接口，用于特殊处理（如事务处理）； 调用该接口后请使用closeConnection关闭连接；
     */

    public Connection getConnection()
    {
        if (!openConnection())
            return null;
        return myConn;
    }

    /**
     * 批处理操作
     * 
     * @param sqlText
     * @param params
     * @return
     */
    public int executeBatchQuery(String sqlText,
            List<Map<Integer, DBParameter>> params)
    {
        DBWatch watch = new DBWatch();
        if (!openConnection() || params == null)
            return -1;
        int count = 0;

        PreparedStatement pstmt = null;

        try
        {
            myConn.setAutoCommit(false);
            pstmt = myConn.prepareStatement(sqlText,
                                            ResultSet.TYPE_SCROLL_SENSITIVE,
                                            ResultSet.CONCUR_READ_ONLY);
            for (Map<Integer, DBParameter> map : params)
            {
                for (Map.Entry<Integer, DBParameter> entry : map.entrySet())
                    pstmt.setObject(entry.getKey(),
                                    entry.getValue().getResult());

                pstmt.addBatch();
                count++;
                if (count % 500 == 0)
                {
                    pstmt.executeBatch();
                    myConn.commit();
                }
            }

            pstmt.executeBatch();
            myConn.commit();
            return 1;
        }
        catch (Exception ex)
        {
            logger.error("[ DBManager : executeBatchQuery ]", ex);
            //System.out.println(pstmt.toString());
            logger.error(String.format(DBMessageType.Format3,
                                       DBMessageType.Sql_Error,
                                       ex.getMessage(), "调用Sql语句" + sqlText
                                               + "出错"));
        }
        finally
        {
            try
            {
                myConn.setAutoCommit(true);
                closeConnection(pstmt);
            }
            catch (SQLException e)
            {
                logger.error("[ DBManager : executeBatchQuery ]", e);
                logger.error(String.format(DBMessageType.Format3,
                                           DBMessageType.Connect_Close,
                                           e.getMessage(), myConn.toString()));
            }
            watch.commit(sqlText);
        }
        return -1;
    }

    /**
     * 以事务方式执行所有sql指令，采用JDBC事务处理方式，不支持跨数据库的事务处理。 仅支持INSERT UPDATE DELETE指令
     * 
     * @param allSqlCmds
     *            事务的所有指令集
     * @return 成功返回true，否则false
     */
    public boolean executeTransaction(List<DBCmd> allSqlCmds)
    {
        StringBuilder sqlStringBuilder = new StringBuilder("");
        PreparedStatement pstmt = null;

        if (allSqlCmds.size() == 0)
            return true;

        if (!openConnection())
            return false;

        try
        {
            myConn.setAutoCommit(false);

            for (DBCmd sqlCmd : allSqlCmds)
            {
                sqlStringBuilder.append(sqlCmd.getSqlCmd());
                pstmt = myConn.prepareStatement(sqlCmd.getSqlCmd());

                if (sqlCmd.getParams() != null)
                    prepareCommand(pstmt, sqlCmd.getParams());

                pstmt.executeUpdate();
            }

            // 成功执行，提交所有修改
            myConn.commit();
            return true;
        }
        catch (Exception ex)
        {
            try
            {
                // 执行出错，回滚数据
                myConn.rollback();
            }
            catch (SQLException e)
            {
                logger.error("[ DBManager : executeTransaction ]", e);
                //System.out.println(pstmt.toString());
                logger.error(String.format(DBMessageType.Format3,
                                           DBMessageType.Sql_Error,
                                           ex.getMessage(),
                                           "事务" + sqlStringBuilder.toString()
                                                   + "回滚出错"));
            }
            logger.error("[ DBManager : executeTransaction ]", ex);
            //System.out.println(pstmt.toString());
            logger.error(String.format(DBMessageType.Format3,
                                       DBMessageType.Sql_Error,
                                       ex.getMessage(), "调用Sql语句"
                                               + sqlStringBuilder.toString()
                                               + "出错"));
        }
        finally
        {
            try
            {
                // 关闭连接
                myConn.setAutoCommit(true);
                closeConnection(pstmt);
            }
            catch (SQLException e)
            {
                logger.error("[ DBManager : executeTransaction ]", e);
                logger.error(String.format(DBMessageType.Format3,
                                           DBMessageType.Connect_Close,
                                           e.getMessage(), myConn.toString()));
            }
            new DBWatch().commit(sqlStringBuilder.toString());
        }

        return true;
    }

    /**
     * 取得所有符合条件的数目, 分页时可用
     * 
     * @param tableName
     * @param where
     * @param params
     * @return
     */
    public int getTotalCount(String tableName, String where,
            Map<Integer, DBParameter> params)
    {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("select count(*) from ");
        sb.append(tableName);
        if (where != null && !where.isEmpty())
        {
            sb.append(" where ");
            sb.append(where);
        }
        sb.append("; ");

        PreparedStatement pstmt = executeQuery(sb.toString(), params);
        if (pstmt != null)
        {
            try
            {
                ResultSet rs = pstmt.executeQuery();
                if (rs.last())
                    return rs.getInt(1);
                return -1;
            }
            catch (Exception e)
            {
                logger.error("[ DBManager : getTotalCount ]", e);
                logger.error(String.format(DBMessageType.Format3,
                                           DBMessageType.Sql_Error,
                                           e.getMessage(), myConn.toString()));
            }
            finally
            {
                try
                {
                    closeConnection(pstmt);
                }
                catch (SQLException e)
                {
                    logger.error("[ DBManager : getTotalCount ]", e);
                }
            }
        }
        return -1;
    }

    /*
     * ======================== Override method ========================
     */

    @Override
    public int executeNoneQuery(String sqlText)
    {
        return executeNoneQuery(sqlText, null);
    }

    @Override
    public int executeNoneQuery(String sqlText, Map<Integer, DBParameter> params)
    {
        int result = -1;
        if (!openConnection())
            return result;
        PreparedStatement pstmt = null;
        try
        {
            pstmt = myConn.prepareStatement(sqlText);
            prepareCommand(pstmt, params);

            return pstmt.executeUpdate();
        }
        catch (Exception ex)
        {
            logger.error("[ DBManager : executeNoneQuery ]", ex);
            //System.out.println(pstmt.toString());
            logger.error(String.format(DBMessageType.Format3,
                                       DBMessageType.Sql_Error,
                                       ex.getMessage(), "调用Sql语句" + sqlText
                                               + "出错"));
        }
        finally
        {
            try
            {
                closeConnection(pstmt);
            }
            catch (SQLException e)
            {
                logger.error("[ DBManager : executeNoneQuery ]", e);
                logger.error(String.format(DBMessageType.Format3,
                                           DBMessageType.Connect_Close,
                                           e.getMessage(), myConn.toString()));
            }
            new DBWatch().commit(sqlText);
        }
        return -1;

    }

    @Override
    public PreparedStatement executeQuery(String sqlText)
    {
        return executeQuery(sqlText, null);
    }

    @Override
    public PreparedStatement executeQuery(String sqlText,
            Map<Integer, DBParameter> params)
    {
        if (!openConnection())
            return null;

        PreparedStatement pstmt = null;
        try
        {
            pstmt = myConn.prepareStatement(sqlText);
            prepareCommand(pstmt, params);
            return pstmt;
        }
        catch (Exception ex)
        {
            logger.error(String.format(DBMessageType.Format3,
                                       DBMessageType.Sql_Error,
                                       ex.getMessage(), "调用Sql语句" + sqlText
                                               + "出错"));
        }
        finally
        {
            new DBWatch().commit(sqlText);
        }
        return null;
    }

    @Override
    public long getMaxId(short type)
    {
        if (!openConnection())
            return 0;
        CallableStatement call = null;
        try
        {
            call = myConn.prepareCall("{call proc_code(?,?)}");
            call.setShort(1, type);
            call.registerOutParameter(2, Types.BIGINT);
            call.execute();
            return call.getLong(2);

        }
        catch (Exception e)
        {
            logger.error("[ DBManager : getMaxId ]", e);
        }
        finally
        {
            try
            {
                closeConnection(call);
            }
            catch (SQLException e)
            {
                logger.error("[ DBManager : getMaxId ]", e);
                logger.error(String.format(DBMessageType.Format3,
                                           DBMessageType.Connect_Close,
                                           e.getMessage(), myConn.toString()));
            }
        }
        return 0;

    }

    @Override
    public int executeLastId(String sqlText, Map<Integer, DBParameter> params)
    {
        int result = -1;
        if (!openConnection())
            return result;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = myConn.prepareStatement(sqlText,
                                            Statement.RETURN_GENERATED_KEYS);
            prepareCommand(pstmt, params);
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next())
            {
                return rs.getInt(1);
            }
        }
        catch (Exception ex)
        {
            logger.error("[ DBManager : executeLastId ]", ex);
            logger.error(String.format(DBMessageType.Format3,
                                       DBMessageType.Sql_Error,
                                       ex.getMessage(), "调用Sql语句" + sqlText
                                               + "出错"));
        }
        finally
        {
            closeConnection(pstmt, rs);
            new DBWatch().commit(sqlText);
        }
        return -1;
    }

    @Override
    public int executeLastId(String sqlText) throws SQLException
    {
        return executeLastId(sqlText, null);
    }

    @Override
    public int executeCountQuery(String sqlText,
            Map<Integer, DBParameter> params) throws SQLException
    {
        PreparedStatement pstmt = executeQuery(sqlText, params);
        if (pstmt != null)
        {
            try
            {
                ResultSet rs = pstmt.executeQuery();
                if (rs.last())
                    return rs.getInt(1);
                return -1;
            }
            catch (Exception e)
            {
                logger.error("[ DBManager : executeCountQuery ]", e);
                logger.error(String.format(DBMessageType.Format3,
                                           DBMessageType.Sql_Error,
                                           e.getMessage(), myConn.toString()));
            }
            finally
            {
                closeConnection(pstmt);
            }
        }
        return -1;
    }
    /*
     * ======================== end of Override method ========================
     */

}
