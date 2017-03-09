/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.dice.entity;

/**
 * 附近用户对象
 * @author charles
 * @date 2011-12-21
 * @version 
 *
 */
public class AroundUser 
{
    /**
     * 用户ID
     */
    private int userId;
    
    /**
     * 纬度
     */
    private double x;
    
    /**
     * 经度
     */
    private double y;
    
    /**
     * 用户距离
     */
    private double userDistance;
    
    public AroundUser() {
	}
    
    public AroundUser(AroundUser u) {
    	if(null != u) {
    		userId = u.getUserId();
    		x = u.getX();
    		y = u.getY();
    	}
	}
    
    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public double getUserDistance()
    {
        return userDistance;
    }

    public void setUserDistance(double userDistance)
    {
        this.userDistance = userDistance;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }
}
