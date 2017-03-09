/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.events;

import com.citywar.event.BaseEventArg;

/**
 * @author : Cookie
 * @date : 2011-5-12
 * @version
 * 
 */
public class PlayerEventArgs extends BaseEventArg
{

    int param;

    /**
     * @param object
     * @param eventType
     */
    public PlayerEventArgs(Object object, int eventType)
    {
        super(object, eventType);
        // TODO Auto-generated constructor stub
    }

    public PlayerEventArgs(Object object, int eventType, int param)
    {
        super(object, eventType);
        this.param = param;
    }

    /**
     * @return the param
     */
    public int getParam()
    {
        return param;
    }

    /**
     * @param param
     *            the param to set
     */
    public void setParam(int param)
    {
        this.param = param;
    }

}
