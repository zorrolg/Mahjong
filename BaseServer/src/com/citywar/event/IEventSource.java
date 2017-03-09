/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.event;

/**
 * @author : Cookie
 * @date : 2011-5-4
 * @version 被观察对象接口
 */
public interface IEventSource
{
    // 给事件类别为eEvent的事件，添加监听者
    public void addListener(int eEvent, IEventListener listener);

    // 移除事件类别为eEvent的监听者listener
    public void removeListener(int eEvent, IEventListener listener);

    // 响应默认事件
    public void notifyListener(int eEvent);

    // 响应自定义事件
    public void notifyListener(int eEvent, BaseEventArg eventArg);
    
 // 响应自定义事件
    public void notifyListener(int eEvent, int para1, int para2);
}
