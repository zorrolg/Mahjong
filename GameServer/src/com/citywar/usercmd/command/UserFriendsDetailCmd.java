/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.entity.FriendInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.gameutil.GamePlayerUtil;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.ReferenceMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.DistanceUtil;

/**
 * 查看个人信息（查看好友及非好友的个人信息均走这一协议）
 * 
 * @author charles
 * @date 2011-12-19
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.FRIENDS_DETAIL, desc = "查看好友信息")
public class UserFriendsDetailCmd extends AbstractUserCmd
{
    private static final Logger logger = Logger.getLogger(UserFriendsDetailCmd.class.getName());

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        try
        {
        	boolean isFriend = false;
            int userId = player.getUserId();
            /**
             * 从packet中获取好友用户ID
             */
            int friendUserId = packet.getInt();
            int slaveCount = ReferenceMgr.getSlaveCount(friendUserId);
            if (userId <= 0 || friendUserId <= 0)
            {
                return 1;
            }

            double[] userPosArray = DistanceUtil.getLatAndLon(player.getPlayerInfo().getPos());

            /**
             * 通过Bussiness查找好友信息(改成从内存中取)
             */
            FriendInfo friendInfo = player.getFriends().get(friendUserId);
            // FriendInfo friendInfo = FriendsBussiness.getFriend(userId,
            // friendUserId);
            // 从allPlayers里面取，如果allPlayers里面没有，从数据库中取
        	PlayerInfo user  = GamePlayerUtil.getPlayerInfo(friendUserId, true);
            // 是好友
            if (null != friendInfo)
            {
            	isFriend = true;
            	// 如果是好友，则更新玩家的好友列表
            	friendInfo.setWin(user.getWin());
                friendInfo.setLose(user.getTotal());
                friendInfo.setVipLevel(user.getVipLevel());
                friendInfo.setLevel(user.getLevel());
                friendInfo.setFriendGp(user.getGp());
                friendInfo.setFriendPos(user.getPos());
                friendInfo.getFriendRealPicPath(user.getRealPicPath());
                friendInfo.setFriendName(user.getUserName());
                friendInfo.setCity(user.getCity());
                friendInfo.setFriendSign(user.getSign());
                friendInfo.setSex(user.getSex());
                friendInfo.setFriendCoins(user.getCoins());
                friendInfo.setFriendMoney(user.getMoney());
                friendInfo.setFriendCharmValve(user.getCharmValve());
            }
            
        	if (null != user) {
        		 double[] userPos = DistanceUtil.getLatAndLon(user.getPos());
                 double userDistance = DistanceUtil.distanceByLatLon(userPosArray[0],
                                                                     userPosArray[1],
                                                                     userPos[0],
                                                                     userPos[1]);
                 /**
                  * 构造响应包
                  */
                 Packet response = new Packet(UserCmdOutType.FRIENDS_DETAIL_RESP);

                 response.putInt(user.getUserId());
                 response.putStr(user.getRealPicPath());
                 List<String> list = user.getPicPathList();
                 boolean isNotEmpty = !user.getRealPicPath().isEmpty();
                 if (null != list && list.size() > 0)
                 {
                	byte sum = (byte)list.size();
                 	if (isNotEmpty)
                 	{
                 		sum++;
                 	}
                 	response.putByte(sum);
                 	if (isNotEmpty)
                 	{
                 		response.putStr(user.getRealPicPath());
                 	}
                 	for (String s : list)
                 	{
                 		response.putStr(user.getRealPicPath(s));
                 	}
                 } 
                 else if (isNotEmpty)
                 {
                 	response.putByte((byte)1);
                 	response.putStr(user.getRealPicPath());
                 }
                 else 
                 {
                	 response.putByte((byte)0);
                 }
                 response.putStr(user.getUserName());
                 response.putInt(user.getLevel());
                 response.putStr(LevelMgr.getLevelTitle(user.getLevel()));
                 response.putInt(user.getWin());
                 response.putInt(user.getTotal());
                 response.putByte((byte)user.getVipLevel());
                 response.putInt((int) userDistance);
                 response.putStr(user.getCity());
                 response.putBoolean(isFriend);//加上是否为好友
                 response.putInt(user.getSex());
                 response.putInt(LevelMgr.getUserUpgradeGp(user));
                 response.putInt(LevelMgr.getUpgradeGp(user.getLevel()));
                 response.putInt(slaveCount);
                 response.putInt(user.getCoins());
                 response.putInt(user.getCharmValve());
                 response.putInt(user.getDrunkLevelSocial());
                 response.putInt(LevelMgr.getDrunkLevel(user.getLevel()));
                 /**
                  * 发送响应包给客户端
                  */
                 player.getOut().sendTCP(response);
        	} 
        	else 
        	{
        		  logger.error("UserFriendsDetailCmd exception userId is not exist==" + friendUserId);
        	}

        }
        catch (Exception e)
        {
            logger.error("[ UserFriendsDetailCmd : execute ]", e);
        }

        return 0;
    }
    
}
