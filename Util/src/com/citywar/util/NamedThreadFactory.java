/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;

/**
 * 命名线程生成工厂，仅为了给游戏所用线程池所创建的线程提供命名支持
 * 
 * @author sky
 * @date 2011-12-30
 * @version
 * 
 */
public class NamedThreadFactory implements ThreadFactory,
        UncaughtExceptionHandler
{
    private static Logger logger = Logger.getLogger(NamedThreadFactory.class);

    /**
     * 是否为后台线程
     */
    private boolean daemon;

    /**
     * 线程名
     */
    private String threadName;

    /**
     * 默认构造函数
     * 
     * @param threadName
     *            线程名前缀
     * @param daemon
     *            是否为后台线程
     */
    public NamedThreadFactory(String threadName, boolean daemon)
    {
        this.threadName = threadName;
        this.daemon = daemon;
    }

    public NamedThreadFactory(String threadName)
    {
        this(threadName, false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     */
    // @Override
    public Thread newThread(Runnable r)
    {
        Thread t = new Thread(r, this.threadName);
        t.setDaemon(this.daemon);
        t.setUncaughtExceptionHandler(this);
        return t;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread,
     *      java.lang.Throwable)
     */
    public void uncaughtException(Thread thread, Throwable throwable)
    {
        logger.error("Uncaught Exception in thread " + thread.getName(),
                     throwable);
    }

}
