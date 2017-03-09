/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll.test;

import java.util.List;

import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.PlayerInfo;

/**
 * @author tracy
 * @date 2011-12-19
 * @version
 * 
 */
public class PlayerInfoTest
{
    public static void query()
    {
        System.err.println(PlayerBussiness.getPlayerInfoByName("tracy").getLevel());
    }
    
    //同步用到
    public static List<PlayerInfo> getAllPlayer(String table )
    {
        return PlayerBussiness.getAllPlayer(table);
    }
    
    //同步用到
    public static void updatePlayer(PlayerInfo player)  
    {
    	PlayerBussiness.updateAll(player.getUserId(), player);
    }
    
    //同步用到
    public static PlayerInfo getPlayerByAccountAndPwd(String account, String pwd)  
    {
    	return PlayerBussiness.checkAccoutAndPwd(account, pwd);
    }
    
    //同步用到
    public static int addPlayer(PlayerInfo info)  
    {
    	return PlayerBussiness.addPlayerInfo(info);
    }

    public static void getUserNamesById()
    {
        for (String userName : PlayerBussiness.getUserNamesById(new int[] { 1,
                2, 3 }))
        {
            System.err.println("userName --> " + userName);
        }
    }

    public static void getUserNameById()
    {
        System.err.println("single username --> "
                + PlayerBussiness.getUserNameById(2));
    }
    
    public static void getPlayerInfosByName()
    {
        for (PlayerInfo info : PlayerBussiness.getPlayerInfosByName("tracy"))
        {
            System.err.println(info.getUserPwd());
        }
    }
    
    public static void addPlayer(String userName)
    {
        
    }
    
    public static void getPicPath(int userId)
    {
        System.err.println(PlayerBussiness.getImagePath(userId));
    }
}
