/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.bll.test;

import com.citywar.bll.FriendsBussiness;

/**
 * @author Administrator
 * @date 2011-12-29
 * @version 
 *
 */
public class FriendsTest
{

    public static void addFriends()
    {
        for(int i=200; i<250; i++)
        {
            FriendsBussiness.addFriendsInfos(5, i);
            FriendsBussiness.addFriendsInfos(i, 5);
        }
    }
}
