/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.events;

import com.citywar.event.BaseEventArg;
import com.citywar.util.wrapper.WrapInteger;

/**
 * @author : Cookie
 * @date : 2011-5-12
 * @version
 * 
 */
public class PlayerContinueReduceEventArgs extends BaseEventArg
{
    WrapInteger count;
    int type;

    public PlayerContinueReduceEventArgs(Object object, int eventType,
            WrapInteger count, int type)
    {
        super(object, eventType);
        this.count = count;
        this.type = type;
    }

    /**
     * @return the count
     */
    public WrapInteger getCount()
    {
        return count;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(WrapInteger count)
    {
        this.count = count;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

}
