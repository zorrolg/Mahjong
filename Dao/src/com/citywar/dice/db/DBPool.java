/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * 数据库连接池类
 * 
 * @author tracy
 * @date 2011-12-15
 * @version beta1.0
 * 
 */
public class DBPool
{
    private static final Logger logger = Logger.getLogger(DBPool.class.getName());

    BoneCP connectionPool = null;
    BoneCPConfig config = null;

    /**
     * 创建新的连接池构造函数
     * 
     * @param poolName
     *            连接池名字
     * @param dbConnUrl
     *            数据库的JDBC URL
     * @param dbUserName
     *            数据库帐号或 null
     * @param dbPassWord
     *            密码或 null
     * @param maxConn
     *            此连接池允许建立的最大连接数
     */
    public DBPool(String poolName, String dbConnUrl, String dbUserName,
            String dbPassWord, int maxConn)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (Exception e)
        {
            logger.error("[ DBPool : DBPool ]", e);
            return;
        }
        config = new BoneCPConfig();
        config.setJdbcUrl(dbConnUrl);
        config.setUsername(dbUserName);
        config.setPassword(dbPassWord);

        int idle = DBPoolManager.getPropertiesFile().getProperty("idleConnectionTestPeriod") == null ? 300
                : Integer.valueOf(DBPoolManager.getPropertiesFile().getProperty("idleConnectionTestPeriod"));
        config.setIdleConnectionTestPeriodInMinutes(idle);

        // 设置一个未使用的连接的存活时间
        int idleMaxAge = DBPoolManager.getPropertiesFile().getProperty("IdleMaxAgeInMinutes") == null ? 300
                : Integer.valueOf(DBPoolManager.getPropertiesFile().getProperty("IdleMaxAgeInMinutes"));
        config.setIdleMaxAgeInMinutes(idleMaxAge);

        // 设置分区个数
        int partitionCount = DBPoolManager.getPropertiesFile().getProperty("partitionCount") == null ? 4
                : Integer.valueOf(DBPoolManager.getPropertiesFile().getProperty("partitionCount"));
        config.setPartitionCount(partitionCount);

        // 设置每个分区最大的连接
        int maxConnPerPart = DBPoolManager.getPropertiesFile().getProperty("maxConnectionsPerPartition") == null ? 10
                : Integer.valueOf(DBPoolManager.getPropertiesFile().getProperty("maxConnectionsPerPartition"));
        config.setMaxConnectionsPerPartition(maxConnPerPart);

        // 设置每个分区最小的连接
        int minConnPerPart = DBPoolManager.getPropertiesFile().getProperty("minConnectionsPerPartition") == null ? 1
                : Integer.valueOf(DBPoolManager.getPropertiesFile().getProperty("minConnectionsPerPartition"));
        config.setMinConnectionsPerPartition(minConnPerPart);

        // 设置连接的增长数量
        int acquireIncrement = DBPoolManager.getPropertiesFile().getProperty("AcquireIncrement") == null ? 5
                : Integer.valueOf(DBPoolManager.getPropertiesFile().getProperty("AcquireIncrement"));
        config.setAcquireIncrement(acquireIncrement);

        int ReleaseHelperThreads = DBPoolManager.getPropertiesFile().getProperty("releaseHelperThreads") == null ? 4
                : Integer.valueOf(DBPoolManager.getPropertiesFile().getProperty("releaseHelperThreads"));
        config.setReleaseHelperThreads(ReleaseHelperThreads);

        int connectionTimeoutInMs = DBPoolManager.getPropertiesFile().getProperty("CloseConnectionWatchTimeout") == null ? 60000
                : Integer.valueOf(DBPoolManager.getPropertiesFile().getProperty("CloseConnectionWatchTimeout"));
        config.setConnectionTimeoutInMs(connectionTimeoutInMs);

        /* 设置是否开启监视 仅调试时使用 */
        boolean closeConnectionWatch = DBPoolManager.getPropertiesFile().getProperty("CloseConnectionWatch") == null ? true
                : Boolean.valueOf(DBPoolManager.getPropertiesFile().getProperty("CloseConnectionWatch"));
        config.setCloseConnectionWatch(closeConnectionWatch);

        // 超时未关闭监控，60秒
        int closeConnectionWatchTimeoutInMs = DBPoolManager.getPropertiesFile().getProperty("ConnectionTimeout") == null ? 60 * 1000
                : Integer.valueOf(DBPoolManager.getPropertiesFile().getProperty("ConnectionTimeout"));
        config.setCloseConnectionWatchTimeoutInMs(closeConnectionWatchTimeoutInMs);
        try
        {
            connectionPool = new BoneCP(config);
        }
        catch (SQLException e)
        {
            logger.error("[ DBPool : DBPool ]", e);
        }
        catch (Exception e)
        {
            logger.error("[ DBPool : DBPool ]", e);
        }

    }

    public void shutdown()
    {
        if (connectionPool != null)
        {
            connectionPool.shutdown();
            connectionPool.close();
        }
    }

    /**
     * 从连接池获得一个可用连接.如果没有空闲的连接且当前连接数小于最大连接 数限制,则创建新连接.
     * 如原来登记为可用的连接不再有效,则从向量删除之,然后递归调用自己以尝试新的可用连接.
     */
    public Connection getConnection()
    {
        try
        {
            Connection conn;
            conn = connectionPool.getConnection();
            return conn;
        }
        catch (SQLException e)
        {
            logger.error("[ DBPool : getConnection ]", e);
        }
        return null;
    }

}
