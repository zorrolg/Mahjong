/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.gameutil;

import java.util.Comparator;

import com.citywar.dice.entity.PlayerInfo;

/**
 * 附近用户对象，实现Comparator接口,用于按照用户有没有照片，离线时间
 * @author shanfeng.cao
 * @date 2012-08-16
 * @version 
 *
 */
public class AroundPlayerInfoComparator implements Comparator<Object> 
{
    @Override
    public int compare(Object o1, Object o2)
    {
    	int result = 0;
    	int o1havePic = 0;
    	int o2havePic = 0;
    	if(o1 instanceof PlayerInfoAndAround && o2 instanceof PlayerInfoAndAround) {
    		PlayerInfoAndAround p1 = (PlayerInfoAndAround)o1;
    		PlayerInfoAndAround p2 = (PlayerInfoAndAround)o2;
    		if(p1 != null && p2 != null) {
                PlayerInfo d1 = p1.getAroudPlayer();
                PlayerInfo d2 = p2.getAroudPlayer();
                if(d1.getPicPath() != null && ! d1.getPicPath().isEmpty()) {
                	o1havePic += 2;
                }
                if(d2.getPicPath() != null && ! d2.getPicPath().isEmpty()) {
                	o2havePic += 2;
                }
                if(d1.getLastLoginDate() != null && (d1.getLastQiutDate() == null || d1.getLastLoginDate().after(d1.getLastQiutDate()))) {
                	o1havePic += 1;
                }
                if(d2.getLastLoginDate() != null &&
                		(d2.getLastQiutDate() == null || d2.getLastLoginDate().after(d2.getLastQiutDate()))) {
                	o2havePic += 1;
                }
                
                
                if(d2.getLastQiutDate() != null && d1.getLastQiutDate() != null && o1havePic == o2havePic) {//正式比较
                	result = d2.getLastQiutDate().compareTo(d1.getLastQiutDate());//反过来
                } else {
                	result = o2havePic - o1havePic;//反过来
                }
    		}
    	}
		return result;
    }
}
