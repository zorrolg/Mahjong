/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.bll;

import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.FriendInfo;

/**
 * 好友相关的business入口
 * @author charles
 * @date 2011-12-16
 * @version 
 *
 */
public class FriendsBussiness
{
    /**
     * 添加好友
     * 
     * @param friendUserId 好友用户ID
     * @param userId 当前用户ID
     * @return 是否添加成功
     */
    public static boolean addFriendsInfos(int userID, int friendUserId)
    {
        return DaoManager.getFriendsDao().addFriend(userID, friendUserId);
    }
    
    /**
     * 取得好友分页信息
     * 
     * @param userId 当前用户ID 
     * @param pageNo 需要加载的页码
     * @return 好友信息列表
     */
    public static List<FriendInfo> getFriendsList(int userID, int pageNo)
    {
        return DaoManager.getFriendsDao().getFriendsList(userID, pageNo);
    }
    
    /**
     * 获取某个好友的信息
     * 
     * @param userId 当前用户ID
     * @param friendUserId 好友的ID 
     * @return 好友的信息
     */
    public static FriendInfo getFriend(int userId, int friendUserId)
    {
        return DaoManager.getFriendsDao().getFriend(userId, friendUserId);
    }
    
    /**
     * 删除好友
     * 
     * @param userId 当前用户ID
     * @param friendUserId 好友的ID 
     * @return 是否删除成功
     */
    public static boolean deleteFriend(int userId, int friendUserId)
    {
        return DaoManager.getFriendsDao().deleteFriend(userId, friendUserId);
    }
    
    /**
     * 查找好友
     * 
     * @param userId 当前用户ID
     * @param friendNickname 好友的昵称信息
     * @param pageNo 需要加载的页码 
     * @return 好友的信息列表
     */
    public static List<FriendInfo> findFriends(int userId, String friendNickname, int pageNo)
    {
        return DaoManager.getFriendsDao().findFriends(userId, friendNickname, pageNo);
    }
    
    /**
     * 获取好友总数，以获取分页信息
     * @param userId 用户id
     * @return
     */
    public static int getFriendsTotalPage(int userId)
    {
        return DaoManager.getFriendsDao().getFriendsTotalPage(userId);
    }
    
    /**
     * 判断是否已经为好友
     * @param userId 当前用户ID
     * @param friendUserId 需要加为好友的用户ID
     * @return
     */
    public static boolean isFriend(int userId, int friendUserId)
    {
        return DaoManager.getFriendsDao().isFriend(userId, friendUserId);
    }
    
    /**
     * 获取某个用户所有好友列表信息
     * @param userId
     * @return
     */
    public static List<FriendInfo> getAllFriendsList(int userId)
    {
        return DaoManager.getFriendsDao().getAllFriendsList(userId);
    }
}
