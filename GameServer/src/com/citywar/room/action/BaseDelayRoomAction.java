/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.room.action;

import com.citywar.manager.HallMgr;

/**
 * 支持延时执行的room action基类
 * 
 * @author sky
 * @date 2011-6-28
 * @version
 * 
 */
public abstract class BaseDelayRoomAction extends AbstractRoomAction
{
		
    private long tick;
//    private long finishTick;


    public BaseDelayRoomAction(int delay)
    {
        tick = System.currentTimeMillis() + delay;
//        finishTick = Long.MAX_VALUE;
    }

    public final void execute()
    {
        if (tick <= System.currentTimeMillis())
        {        	
            executeImp();
        }
        else
        {
        	HallMgr.addAction(this);
        }
    }

    protected abstract void executeImp();
}
