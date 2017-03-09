package com.citywar.event;

/**
 * 
 * 基础事件类，自定义事件类继承本事件类,用来作为回调的参数
 * 
 * @author : Cookie
 * @date : 2011-5-4
 * @version
 * 
 */
public class BaseEventArg
{
    Object object;
    int eventType;

    public BaseEventArg(Object object, int eventType)
    {
        super();
        this.object = object;
        this.eventType = eventType;
    }

    public Object getObject()
    {
        return object;
    }

    public int getEventType()
    {
        return eventType;
    }
}
