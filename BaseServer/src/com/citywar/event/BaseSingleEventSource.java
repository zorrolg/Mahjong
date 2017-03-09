/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.event;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author : Cookie
 * @date : 2011-5-4
 * @version 如果一个对象，只用一种被监听继承这个
 */
public class BaseSingleEventSource implements IEventSource
{
    Collection<IEventListener> eventListeners;

    public BaseSingleEventSource()
    {
        super();
        this.eventListeners = Collections.synchronizedSet(new HashSet<IEventListener>());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dicesns.event.IEventSource#addListener(int,
     * com.citywar.dicesns.event.IEventListener)
     */
    @Override
    public void addListener(int eEvent, IEventListener listener)
    {
        if (!eventListeners.contains(listener))
        {
            eventListeners.add(listener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dicesns.event.IEventSource#removeListener(int,
     * com.citywar.dicesns.event.IEventListener)
     */
    @Override
    public void removeListener(int eEvent, IEventListener listener)
    {
        if (eventListeners.contains(listener))
        {
            eventListeners.remove(listener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dicesns.event.IEventSource#NotifyListener(int)
     */
    @Override
    public void notifyListener(int eEvent)
    {
        for (IEventListener iEventListener : eventListeners)
        {
            iEventListener.onEvent(new BaseEventArg(this, eEvent));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dicesns.event.IEventSource#NotifyListener(int,
     * com.citywar.dicesns.event.BaseEventArg)
     */
    @Override
    public void notifyListener(int eEvent, BaseEventArg eventArg)
    {
        for (IEventListener iEventListener : eventListeners)
        {
            iEventListener.onEvent(eventArg);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dicesns.event.IEventSource#NotifyListener(int,
     * com.citywar.dicesns.event.BaseEventArg)
     */
    @Override
    public void notifyListener(int eEvent, int para1, int para2)
    {
        for (IEventListener iEventListener : eventListeners)
        {
            iEventListener.onEvent(para1, para2);
        }
    }
}
