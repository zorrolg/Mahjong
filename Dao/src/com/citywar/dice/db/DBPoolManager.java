/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.citywar.util.RSAUtil;
import com.citywar.util.StackMessagePrint;

/**
 * 数据库连接池管理类 管理类DBConnectionManager支持对一个或多个由属性文件定义的数据库连接池的访问.
 * 客户程序可以调用getInstance()方法访问本类的唯一实例
 * 
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public class DBPoolManager
{
    private static final Logger log = Logger.getLogger(DBPoolManager.class.getName());
    private static DBPoolManager instance; // 唯一实例
    private Hashtable<String, DBPool> pools = new Hashtable<String, DBPool>();// 多种连接池
    private static Properties dbProps;// 属性文件

    /**
     * 单例模式建构私有函数以防止其它对象创建本类实例
     */
    private DBPoolManager(String dbPath)
    {
        this.init(dbPath);
    }

    /**
     * 得到DBPool连接信息
     * 
     * @return
     */
    public static Properties getPropertiesFile()
    {
        return dbProps;
    }

    /**
     * 采用单例模式，返回唯一实例.如果是第一次调用此方法,则创建实例
     * 
     * @return DBConnectionManager 唯一实例
     */
    public static synchronized DBPoolManager getInstance(String dbPath)
    {
        if (instance == null)
        {
            instance = new DBPoolManager(dbPath);
        }
        return instance;
    }

    /**
     * 获得一个可用的(空闲的)连接.如果没有可用连接,且已有连接数小于最大连接数 限制,则创建并返回新连接
     * 
     * @param name
     *            在属性文件中定义的连接池名字
     * @return Connection 可用连接或null
     */
    public Connection getConnection(String name)
    {
        DBPool dbPool = (DBPool) pools.get(name);
        if (dbPool != null)
        {
            // 获取一个JOBC连接池"
            return dbPool.getConnection();
        }
        log.error(String.format("未找到连接池名为%s", name));
        return null;
    }

    /**
     * 关闭所有连接,撤销驱动程序的注册
     */
    public synchronized void closeFallow()
    {
        Enumeration<DBPool> allPools = pools.elements();
        while (allPools.hasMoreElements())
        {
            DBPool pool = (DBPool) allPools.nextElement();
            pool.shutdown();
        }
    }

    /**
     * 读取属性完成初始化
     */
    private void init(String dbPath)
    {
        if (dbPath == null || dbPath.isEmpty())
            return;

        InputStream fileinputstream = null;
        try
        {
            fileinputstream = new FileInputStream(dbPath);
            dbProps = new Properties();
            dbProps.load(fileinputstream);
        }
        catch (Exception e)
        {
            log.error(String.format(DBMessageType.Format2,
                                    DBMessageType.Config_Path, e.getMessage()));
            return;
        }
        finally
        {
            try
            {
                // 释放文件io
                fileinputstream.close();
            }
            catch (IOException e)
            {
                log.error(StackMessagePrint.printErrorTrace(e));
            }
        }

        // 创建连接池
        createPools(dbProps);

    }

    /**
     * 根据指定属性创建连接池实例.
     * 
     * @param props
     *            连接池属性
     */
    private void createPools(Properties props)
    {
        Enumeration<?> propNames = props.propertyNames();
        while (propNames.hasMoreElements())
        {
            String name = (String) propNames.nextElement();
            if (name.endsWith(".url"))
            {
                String poolName = name.substring(0, name.lastIndexOf("."));
                String config = decrypt(props.getProperty(poolName + ".url"));
                if (config == null || config.isEmpty())
                    continue;

                String[] result = config.split("\\|");
                String url = result[0];
                String user = result[1];
                String password = result[2];
                String maxconn = result[3];
                int max;
                try
                {
                    max = Integer.valueOf(maxconn).intValue();
                }
                catch (NumberFormatException e)
                {
                    log.error(String.format(DBMessageType.Format2,
                                            DBMessageType.Config_MaxConn,
                                            e.getMessage()));
                    max = 0;
                }

                DBPool pool = new DBPool(poolName, url, user, "root", max);
                log.info(String.format("加载配置连接池:%s,URL:%s完成！", poolName, url));
                pools.put(poolName, pool);
            }
        }
    }

    /**
     * 解密数据库连接字符串
     * 
     * @param str
     *            数据库连接解密key，一般不会变动，写在程序中
     * @return
     */
    private String decrypt(String str)
    {
        String PrivateKeyModulus = "AKqNU2Mi07CA9p6uQSJHwu9YEJjvjTnFJ913870mrupVARhkqu09PCO8A/uh2+jfCt0VN4aV3vHrxO4rJbXUxy7RqaNBKC5bLsGB+BVXSfOoOzcey45QpAAwQr2jO/dHfmaF7NrUzCV+6EI00Mz/nozACUMZ3dxdaGE6kqLhNey9";
        String privateExponent = "FZDfTXbW68ey56fDJrGKTbeGCcCoy/hJLTEAyhc9IPZ0t//quSr4EtCwdD4oT478ka4gIk2LLm4QrckY8KAiO2ye/zg5WtYQUtsPHayI31UqMPBoC9jVLZMjD6Bh3qyjHAMmLjpaV6H3c0ZIjyHXJfOoKDcljLUasvq4FYPbQSU=";

        return RSAUtil.decrypt(PrivateKeyModulus, privateExponent, str);
    }
    
    /**
     * 加密数据库连接字符串格式：url|user|password|maxconn
     * @param str 数据库连接加密key，一般不会变动，写在程序中
     * @return
     */
    private String encrypt(String str)
    {
        String publicKey ="AKqNU2Mi07CA9p6uQSJHwu9YEJjvjTnFJ913870mrupVARhkqu09PCO8A/uh2+jfCt0VN4aV3vHrxO4rJbXUxy7RqaNBKC5bLsGB+BVXSfOoOzcey45QpAAwQr2jO/dHfmaF7NrUzCV+6EI00Mz/nozACUMZ3dxdaGE6kqLhNey9";
        String publicExponent="AQAB";
        
        return RSAUtil.encrypt(publicKey, publicExponent,str);
    }
    
    public static void main(String[] args)
    {
//        String url="jdbc:mysql://203.195.181.156:3306/db_dice?characterEncoding=utf-8|root|pufunsql2013|100";
        String url="jdbc:mysql://127.0.0.1:3306/db_citywar?characterEncoding=utf-8|root||100";
        //String url_1="jdbc:mysql://10.10.5.69:3306/db_logs?rewriteBatchedStatements=true&characterEncoding=utf-8|dice_linux|love|100";
        DBPoolManager pool=new DBPoolManager("");
        
        String tank = pool.encrypt(url);
        //String log=pool.encrypt(url_1);
        
        System.out.println("TANK:"+tank);
        ////System.out.println("LOG:"+log);
        
        String str = pool.decrypt(tank);
        System.out.println(str);
        
        
        String str1 = pool.decrypt("kLtnyZ7P20bjV4oYcCAbebG248IQ8to78fO7ZnZ55YLVdyIx9/MIiuYAuXXlooSZeK3iYiaut/lWr1MVKrwfhK6WDhOgf6+3wdK3ihPTDjLRImziWS6VsXCCLmMqjriHMHOpVYkcvPjo5F8OTBH8ag8lF2uaa8HgUIul3jPU7CM=");
        System.out.println(str1);
        
    }
}
