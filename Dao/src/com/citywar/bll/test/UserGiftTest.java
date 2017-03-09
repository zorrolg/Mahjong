/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.bll.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.citywar.bll.UserGiftBussiness;
import com.citywar.dice.entity.UserGiftInfo;
import com.citywar.dice.entity.UserGiftIntegrationInfo;

/**
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version 
 *
 */
public class UserGiftTest
{

    public static void queryList()
    {
        for (UserGiftInfo info : UserGiftBussiness.getAllUserGift(14186, 0))
        {
            System.out.println(info);
        }
    }
    
    public static void addReferenceLogs() {
		List<UserGiftInfo> list = new ArrayList<UserGiftInfo>();
		UserGiftInfo info = new UserGiftInfo(1, 14186, 2, 14186, 1,
				 new Timestamp(System.currentTimeMillis()), true, true, "da撒旦法师法发顺丰s", "da撒旦法师法发顺丰s", "da撒旦法师法发顺丰s");
		list.add(info);
		UserGiftBussiness.insertUserGifts(list);
	}

	public static void updateReferenceLogs() {
		List<UserGiftInfo> list = new ArrayList<UserGiftInfo>();
		UserGiftInfo info = new UserGiftInfo(1, 14186, 2, 14186, 1,
				 new Timestamp(System.currentTimeMillis()), true, true, "da撒旦法师aaaaaaa", "da撒旦法师aaaaaaa", "da撒旦法师aaaaaaa");
		list.add(info);
		UserGiftBussiness.updateUserGifts(list);
	}

	public static void selectAllUserGiftIdAndSumCount() {
		
		for (UserGiftIntegrationInfo info : UserGiftBussiness.getAllUserGiftIdAndSumCount(14186))
        {
            System.out.print(info.getGiftId() + "\t");
            //System.out.println(info.getSumCount());
        }
	}
}
