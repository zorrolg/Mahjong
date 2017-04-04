/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.entity.PlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.gameutil.PlayerAroudUser;
import com.citywar.gameutil.PlayerInfoAndAround;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.HeadPicUtil;
import com.citywar.util.TimeUtil;

/**
 * 查看附近用户
 * 
 * @author charles
 * @date 2011-12-20
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.ALL_AROUND_LOAD, desc = "查看所有的附近用户")
public class UserAllAroundLoadCmd extends AbstractUserCmd
{
    private static final Logger logger = Logger.getLogger(UserAllAroundLoadCmd.class.getName());

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        try
        {
            if (null == player || null == player.getPlayerInfo().getPos()
                    || "".equals(player.getPlayerInfo().getPos()))
            {//TODO 0,0
                return 1;
            }
            int size = 0;
            int pageSize = packet.getInt();
            int pageNo = packet.getInt();
            int totalPages = pageNo;
            
            List<PlayerInfoAndAround> aroundUserInfos = player.getPlayerAroudUser().getAllAroundPlayers(pageNo);
            size = aroundUserInfos != null ? aroundUserInfos.size() : 0;
            if(size < PlayerAroudUser.PAGE_SIZE) {
            	totalPages = pageNo;
            } else {
            	totalPages++;
            }
            /**
             * 构造响应包
             */
            Packet response = new Packet(UserCmdOutType.ALL_AROUND_LOAD_RESP);
            response.putInt(totalPages);
            response.putInt(size);
            response.putInt(pageNo);

            if (null != aroundUserInfos && aroundUserInfos.size() > 0)
            {
                int isPlaying;
                for (PlayerInfoAndAround playerInfoAndAround : aroundUserInfos)
                {
                	PlayerInfo playerInfo = playerInfoAndAround.getAroudPlayer();
            		response.putInt(playerInfo.getUserId());
            		String picPath = "";
    				if (!playerInfo.getPicPath().isEmpty()) {
    					picPath = HeadPicUtil.getSmallPicPath(playerInfo.getRealPicPath());
    				}
	                response.putStr(picPath);
                    response.putStr(playerInfo.getUserName());
                    response.putInt(playerInfo.getLevel());
                    response.putStr(LevelMgr.getLevelTitle(playerInfo.getLevel()));
                    response.putInt(playerInfo.getCharmValve());
                    response.putInt(playerInfo.getWin());
                    response.putInt(playerInfo.getLose());
                    response.putByte((byte)playerInfo.getVipLevel());
                    
                    response.putInt(null != playerInfoAndAround.getAroudUser()
                    		? (int)playerInfoAndAround.getAroudUser().getUserDistance() : 0);
                    response.putBoolean(player.getFriends().containsKey(playerInfo.getUserId()) ? true
                            : false);
                    response.putInt(playerInfo.getSex());
                    
                    // 加上是否在游戏中的属性
                    GamePlayer aroundPlayer = WorldMgr.getPlayerByID(playerInfo.getUserId());
					if (null == aroundPlayer) {
						//从在值班的机器人中取
						Player robot = RobotMgr.getOnDutyRobotByID(playerInfo.getUserId());
						if (null != robot) {
							aroundPlayer = robot.getPlayerDetail();
						}
					}
					String timeInterval = "分钟前";
                    if(null != aroundPlayer)
                    {
                    	timeInterval = "在线";
                    	// 在线，正在游戏 1， 不在游戏 2
                    	isPlaying = aroundPlayer.getIsPlaying() ? 1 : 2;
                    } else {
                    	// 不在线(这种情况不可能，因为附近用户必须是在线的)
                    	isPlaying = 3;
                        Timestamp today = new Timestamp(System.currentTimeMillis());
                        Timestamp lastQiutDate = playerInfo.getLastQiutDate();
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
                    response.putInt(isPlaying);
                    response.putStr(timeInterval);//间隔时间
                }
            }
            /**
             * 发送给客户端
             */
            player.getOut().sendTCP(response);

        }
        catch (Exception e)
        {
            logger.error("[ UserAllAroundLoadCmd : execute ]", e);
        }

        return 0;
    }
}
