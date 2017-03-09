/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.queue;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;

/**
 * 自驱动的可执行消息队列，能够自动驱动本队列中的指令有序执行
 * 
 * @author Administrator
 * @date
 * @version
 * 
 */
public class SelfDrivenRunnableQueue<MessageThread extends Runnable> implements
        IRunnableQueue<MessageThread>
{
    private final static Logger LOGGER = Logger.getLogger(SelfDrivenRunnableQueue.class.getName());

    // 用于执行消息的线程池，需在队列初始化时指定
    private ExecutorService executorService = null;

    // 消息队列
    private Queue<MessageThread> msgQueue = null;

    // 运行状态
    private boolean isRunning = false;

    /**
     * 构造函数，使用非优先队列
     * 
     * @param executorService
     *            消息执行线程池
     */
    public SelfDrivenRunnableQueue(ExecutorService executorService)
    {
        this.executorService = executorService;
        this.msgQueue = new LinkedList<MessageThread>();
    }

    public SelfDrivenRunnableQueue(ExecutorService executorService,
            Comparator<MessageThread> comparator)
    {
        this.executorService = executorService;
        this.msgQueue = new PriorityQueue<MessageThread>(10, comparator);
    }

    /**
     * 添加可执行到队列，若当前队列为空，则自动提交到指定的线程池执行执行
     * 
     * @see com.citywar.queue.IRunnableQueue#add(java.lang.Runnable)
     */
    @Override
    public void add(MessageThread msg)
    {
        synchronized (msgQueue)
        {
            msgQueue.add(msg);

            /* 原指令队列为空，提交到线程池推动指令执行 */
            if (msgQueue.size() != 0 && !isRunning)
            {
                // if (!isRunning)
                // {

                executorService.execute(msg);
                isRunning = true;
            }
        }
    }

    /**
     * 移除消息队列顶部的指令，若当前队列非空，则自动提交指令执行
     * 
     * @see com.citywar.queue.IRunnableQueue#remove()
     */
    @Override
    public void remove()
    {
        synchronized (msgQueue)
        {
            try
            {
                isRunning = false;
                msgQueue.remove();

                if (msgQueue.size() != 0)
                {
                    executorService.execute(msgQueue.peek());
                    isRunning = true;
                }
            }
            catch (Exception e)
            {
                LOGGER.error("UserCmdQueue::remove:", e);
            }
        }
    }

    /**
     * 清空所有消息
     * 
     * @see com.citywar.queue.IRunnableQueue#clear()
     */
    @Override
    public void clear()
    {
        synchronized (msgQueue)
        {
            msgQueue.clear();
        }
    }
}
