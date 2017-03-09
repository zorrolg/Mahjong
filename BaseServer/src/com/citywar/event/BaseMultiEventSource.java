package com.citywar.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * 事件源
 * 
 * @author : Cookie
 * @date : 2011-5-4
 * @version 同时监听多种事件，用HashMap存储映射关系,需要添加事件的实体继承 用HashSet避免重复,同时保证线程安全
 */
public class BaseMultiEventSource implements IEventSource
{
    // SourceObject
    private HashMap<Integer, Collection<IEventListener>> eventSourceHashMap;
    private ReadWriteLock lock;

    public BaseMultiEventSource()
    {
        super();
        eventSourceHashMap = new HashMap<Integer, Collection<IEventListener>>();
        lock = new ReentrantReadWriteLock(false);
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
        lock.writeLock().lock();

        Collection<IEventListener> listListeners = eventSourceHashMap.get(eEvent);
        if (listListeners == null)
        {
            // 创建线程安全版集合
            listListeners = Collections.synchronizedSet(new HashSet<IEventListener>());
            eventSourceHashMap.put(eEvent, listListeners);
        }

        if (!listListeners.contains(listener))
        {
            listListeners.add(listener);
        }

        lock.writeLock().unlock();
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
        lock.writeLock().lock();

        Collection<IEventListener> listListeners = eventSourceHashMap.get(eEvent);
        if (listListeners != null && listListeners.contains(listener))
        {
            listListeners.remove(listener);
        }

        lock.writeLock().unlock();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dicesns.event.IEventSource#NotifyListener()
     */
    @Override
    public void notifyListener(int eEvent)
    {
        lock.readLock().lock();

        List<IEventListener> tEventListeners = eventSourceHashMap.get(eEvent) == null ? null
                : new ArrayList<IEventListener>(eventSourceHashMap.get(eEvent));

        lock.readLock().unlock();

        if (tEventListeners != null)
        {
            for (IEventListener iEventListener : tEventListeners)
            {
                iEventListener.onEvent(new BaseEventArg(this, eEvent));
            }
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
        lock.readLock().lock();

        List<IEventListener> tEventListeners = eventSourceHashMap.get(eEvent) == null ? null
                : new ArrayList<IEventListener>(eventSourceHashMap.get(eEvent));

        lock.readLock().unlock();

        if (tEventListeners != null)
        {
            for (IEventListener iEventListener : tEventListeners)
            {
                iEventListener.onEvent(eventArg);
            }
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
        lock.readLock().lock();

        List<IEventListener> tEventListeners = eventSourceHashMap.get(eEvent) == null ? null
                : new ArrayList<IEventListener>(eventSourceHashMap.get(eEvent));

        lock.readLock().unlock();

        if (tEventListeners != null)
        {
            for (IEventListener iEventListener : tEventListeners)
            {
                iEventListener.onEvent(para1, para2);
            }
        }
    }
    
}
