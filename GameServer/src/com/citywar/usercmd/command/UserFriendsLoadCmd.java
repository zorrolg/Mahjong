/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.entity.FriendInfo;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.DistanceUtil;
import com.citywar.util.TimeUtil;

/**
 * 好友列表加载
 * 
 * @author charles
 * @date 2011-12-19
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.FRIENDS_LOAD, desc = "好友列表加载")
public class UserFriendsLoadCmd extends AbstractUserCmd
{
    private static final Logger logger = Logger.getLogger(UserFriendsLoadCmd.class.getName());

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        try
        {
            int userId = player.getUserId();
            /**
             * 从packet中获取分页页码
             */
            int pageNo = packet.getInt();

            if (userId <= 0 || pageNo <= 0)
            {
                return 1;
            }

            /**
             * 通过Bussiness查找好友信息(改成从内存中取)
             */
            List<FriendInfo> friendInfos = player.getPagedList(pageNo,
                                                               player.getFriendPageSize());
            int pageSize = friendInfos.size();
            int totalPage = player.getTotalPage(player.getFriendPageSize());

            // List<FriendInfo> friendInfos =
            // FriendsBussiness.getFriendsList(userId, pageNo);
            // int pageSize = friendInfos.size();
            // int totalPage = FriendsBussiness.getFriendsTotalPage(userId);

            double[] userPosArray = DistanceUtil.getLatAndLon(player.getPlayerInfo().getPos());

            /**
             * 构造响应包
             */
            Packet response = new Packet(UserCmdOutType.FRIENDS_LOAD_RESP);
            response.putInt(totalPage);
            response.putInt(pageSize);
            response.putInt(pageNo);
            int isPlaying;
            for (int index=friendInfos.size()-1;index>=0;index--)
            {
            	FriendInfo info = friendInfos.get(index);
            	
            	
            	
            	GamePlayer tempPlayer = WorldMgr.getPlayerByID(info.getFriendId());
    			if (null == tempPlayer) {
    				//从在值班的机器人中取
    				Player robot = RobotMgr.getOnDutyRobotByID(info.getFriendId());
    				if (null != robot) {
    					tempPlayer = robot.getPlayerDetail();
    				}
    			}		
    			 if(tempPlayer != null)
    			 {				 
    				 info.setFriendName(tempPlayer.getPlayerInfo().getUserName());
    				 info.setFriendPicPath(tempPlayer.getPlayerInfo().getRealPicPath());
    				 info.setLevel(tempPlayer.getPlayerInfo().getLevel());
    				 info.setWin(tempPlayer.getPlayerInfo().getWin());
    				 info.setLose(tempPlayer.getPlayerInfo().getLose());
    				 info.setVipLevel(tempPlayer.getPlayerInfo().getVipLevel());
    				 
    				 info.setSex(tempPlayer.getPlayerInfo().getSex());
    				 info.setFriendCharmValve(tempPlayer.getPlayerInfo().getCharmValve());
    				 info.setCity(tempPlayer.getPlayerInfo().getCity());
    				     				 
    			 }
    			 
    			 

                response.putInt(info.getFriendId());
        		String picPath = "";
				if (!info.getFriendPicPath().isEmpty()) {
					picPath = info.getFriendPicPath();
				}
                response.putStr(picPath);
                response.putStr(info.getFriendName());
                response.putInt(info.getLevel());
                response.putStr(LevelMgr.getLevelTitle(info.getLevel()));
                response.putInt(info.getFriendCharmValve());
                response.putInt(info.getWin());
                response.putInt(info.getLose());
                response.putByte((byte)info.getVipLevel());
                

                double[] friendPosArray = DistanceUtil.getLatAndLon(info.getFriendPos());
                double distance = DistanceUtil.distanceByLatLon(userPosArray[0],
                                                                userPosArray[1],
                                                                friendPosArray[0],
                                                                friendPosArray[1]);
                response.putInt((int) distance);
                response.putBoolean(true);
                response.putInt(info.getSex());
                
                // 默认为不在线
                isPlaying = 3;
                
                // 加上是否在游戏中的属性
                GamePlayer gp = WorldMgr.getPlayerByID(info.getFriendId());
                
                if(null == gp)
                {
                	// 从机器人里面取,值班的
                	Player pl = RobotMgr.getOnDutyRobotByID(info.getFriendId());
                	if(null != pl)
                	{
                		isPlaying = pl.isPlaying() ? 1 : 2;
                	}
                }
                else
                {
                	// 在线，正在游戏 1， 不在游戏 2
                	isPlaying = gp.getIsPlaying() ? 1 : 2;
                }
                response.putInt(isPlaying);
                
                
                
                
                
                
                
             // 加上是否在游戏中的属性
//                GamePlayer aroundPlayer = WorldMgr.getPlayerByID(info.getUserId());
//				if (null == aroundPlayer) {
//					//从在值班的机器人中取
//					Player robot = RobotMgr.getOnDutyRobotByID(info.getUserId());
//					if (null != robot) {
//						aroundPlayer = robot.getPlayerDetail();
//					}
//				}
				String timeInterval = "分钟前";
                if(null != tempPlayer)
                {
                	timeInterval = "在线";
                	// 在线，正在游戏 1， 不在游戏 2
                	isPlaying = tempPlayer.getIsPlaying() ? 1 : 2;
                } else {
                	// 不在线(这种情况不可能，因为附近用户必须是在线的)
                	isPlaying = 3;
                    Timestamp today = new Timestamp(System.currentTimeMillis());
                    Timestamp lastQiutDate = info.getFriendLastLoginDate();
                    if(null == lastQiutDate) {
                    	timeInterval = "7天前";
                    } else {
                        int interval = TimeUtil.timeSpan(lastQiutDate, today, "min");
                        if(interval > 60) {
                        	interval = TimeUtil.timeSpan(lastQiutDate, today, "hour");
                        	if(interval > 24) {
                            	interval = TimeUtil.timeSpan(lastQiutDate, today, "day");
                            	timeInterval = "天前";
                            } else {
                            	timeInterval = "小时前";
                            }
                        }
                        timeInterval = interval + timeInterval;
                    }
                }
                
                response.putStr(timeInterval);//
            }

            /**
             * 发送响应包给客户端
             */
            player.getOut().sendTCP(response);
        }
        catch (Exception e)
        {
            logger.error("[ UserFriendsLoadCmd : execute ]", e);
        }

        return 0;
    }
}
