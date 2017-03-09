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
public class BeforePlayerShootEventArgs extends BaseEventArg
{
    int ball;

    public BeforePlayerShootEventArgs(Object object, int eventType, int ball)
    {
        super(object, eventType);
        this.ball = ball;
    }

    /**
     * @param object
     * @param eventType
     */
    public BeforePlayerShootEventArgs(Object object, int eventType)
    {
        super(object, eventType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the ball
     */
    public int getBall()
    {
        return ball;
    }

    /**
     * @param ball
     *            the ball to set
     */
    public void setBall(int ball)
    {
        this.ball = ball;
    }

}
