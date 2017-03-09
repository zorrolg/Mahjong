/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.event;

/**
 * @author : Cookie
 * @date : 2011-5-4
 * @version 监听者接口
 */
public interface IEventListener
{
    public void onEvent(Object EventArgs);
    public void onEvent(int para1, int para2);
    
}
