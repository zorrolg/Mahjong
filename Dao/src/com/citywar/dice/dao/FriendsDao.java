/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.FriendInfo;

/**
 * 好友相关DAO层定义
 * @author charles
 * @date 2011-12-16
 * @version 
 *
 */
public interface FriendsDao extends BaseDao
{
    /**
     * 获取用户的好友信息列表
     * @param userId 当前用户ID
     * @param pageNo 请求的页码
     * @return 好友信息列表 
     */
    List<FriendInfo> getFriendsList(int userId, int pageNo);
    
    /**
     * 获取某个好友的信息
     * @param 当前用户的ID
     * @param 好友的ID
     * @return 好友信息
     */
    FriendInfo getFriend(int userId, int friendUserId);

    /**
     * 增加好友
     * @param userId 当前用户ID
     * @param friendUserId 需要添加的好友ID
     * @return 是否添加成功
     */
    boolean addFriend(int userId, int friendUserId);
    
    /**
     * 查找好友
     * @param userId 当前用户ID
     * @param friendNickname 查找的好友昵称
     * @param pageNo 需要加载的页码
     * @return 查找到的好友信息列表
     */
    List<FriendInfo> findFriends(int userId, String friendNickname, int pageNo);
    
    /**
     * 删除好友
     * @param userId 当前用户ID
     * @param friendUserId 待删除的用户ID
     * @return 是否删除成功
     */
    boolean deleteFriend(int userId, int friendUserId);
    
    /**
     * 获取好友总数
     * @param userId 用户ID
     * @return
     */
    int getFriendsTotalPage(int userId);
    
    /**
     * 判断是否已经是好友
     * @param userId
     * @param friendUserId
     * @return
     */
    boolean isFriend(int userId, int friendUserId);
    
    /**
     * 获取某用户所有好友信息
     * @param userId
     * @return
     */
    List<FriendInfo> getAllFriendsList(int userId);
}
