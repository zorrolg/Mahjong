/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.dice.entity;

import java.util.Comparator;

/**
 * 附近用户对象，实现Comparator接口,用于按照距离对用户进行排序
 * @author charles
 * @date 2011-12-21
 * @version 
 *
 */
public class AroundUserComparator implements Comparator<Object> 
{
    @Override
    public int compare(Object o1, Object o2)
    {
        double d1 = ((AroundUser)o1).getUserDistance();
        double d2 = ((AroundUser)o2).getUserDistance();
        return (int)((d1 - d2) * 10000);
    }
}
