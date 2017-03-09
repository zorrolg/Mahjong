/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.gameobjects;

import com.citywar.event.BaseEventArg;

/**
 * @author : Cookie
 * @date : 2011-5-5
 * @version 用于GamePlayer事件中的参数
 */
public class PlayerEventArg extends BaseEventArg
{

    /**
     * @param object
     * @param eventType
     */
    int args;

    public PlayerEventArg(Object object, int eventType, int args)
    {
        super(object, eventType);
        this.args = args;
    }

    /**
     * @return the args
     */
    public int getArgs()
    {
        return args;
    }

    /**
     * @param args
     *            the args to set
     */
    public void setArgs(int args)
    {
        this.args = args;
    }

}
