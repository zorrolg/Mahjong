/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.room.action;

import org.apache.log4j.Logger;

import com.citywar.queue.SelfDrivenRunnableQueue;
import com.citywar.util.StackMessagePrint;

/**
 * 抽象的roomaction类，所有antion需继承此类
 * 
 * @author sky
 * @date 2011-6-3
 * @version
 * 
 */
public abstract class AbstractRoomAction implements IRoomAction
{
    protected static final Logger LOGGER = Logger.getLogger(AbstractRoomAction.class.getName());

    private SelfDrivenRunnableQueue<AbstractRoomAction> actionQueue;

    /**
     * @return the actionQueue
     */
    public SelfDrivenRunnableQueue<AbstractRoomAction> getActionQueue()
    {
        return actionQueue;
    }

    /**
     * @param actionQueue
     *            the actionQueue to set
     */
    public void setActionQueue(
            SelfDrivenRunnableQueue<AbstractRoomAction> actionQueue)
    {
        this.actionQueue = actionQueue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        if (actionQueue != null)
        {
            try
            {
                execute();
            }
            catch (Exception e)
            {
                String errorMsg = String.format("room action error:%n%s%n",
                                                StackMessagePrint.printErrorTrace(e));
                LOGGER.error(errorMsg, e);
            }
            finally
            {
                // 不论是否成功执行，均移除此action
                actionQueue.remove();
            }
        }
    }

    public abstract void execute();
}
