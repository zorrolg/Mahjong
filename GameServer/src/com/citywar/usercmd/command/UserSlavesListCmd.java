package com.citywar.usercmd.command;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.citywar.dice.entity.UserRefWorkInfo;
import com.citywar.dice.entity.UserReferenceInfo;
import com.citywar.dice.entity.UserReferenceLogInfo;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.ReferenceMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.Config;
import com.citywar.util.DistanceUtil;
import com.citywar.util.HeadPicUtil;
import com.citywar.util.TimeUtil;

@UserCmdAnnotation(code = UserCmdType.USER_SLAVES_LIST, desc = "加载用户奴隶的列表")
public class UserSlavesListCmd extends AbstractUserCmd{

	@Override
	public int execute(GamePlayer player, Packet packet) {
		try {
			
			List<UserReferenceInfo> list = new ArrayList<UserReferenceInfo>();
			List<UserReferenceInfo> nowSlaves = player.getPlayerReference().getUserCurrentNowSlaves();//现在还是属于自己的奴隶
			//首选组装奴隶数据
			if(null != nowSlaves) {
				list.addAll(nowSlaves);
			}
			
			Packet response = new Packet(UserCmdOutType.USER_SLAVES_LIST);
			addUserInfoToPacket(list, response, player);//发送现在还是属于自己的奴隶,发送已经被抢走了的奴隶
			response.putInt(0);
            player.getOut().sendTCP(response);
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private void addUserInfoToPacket(List<UserReferenceInfo> Slaves, Packet response ,GamePlayer player) {
		double[] userPosArray = DistanceUtil.getLatAndLon(player.getPlayerInfo().getPos());
		int isPlaying;
//		int SlavesCount = 0;//只有当奴隶在线的时候，奴隶的奴隶数才是对的，其它都为0
//		System.out.println("进入查询用户奴隶模块======当前用户的奴隶数" + Slaves.size());
		response.putInt(Slaves.size());
		for (UserReferenceInfo info : Slaves) {
			
			
			GamePlayer tempPlayer = WorldMgr.getPlayerByID(info.getSlavesUserId());
			if (null == tempPlayer) {
				//从在值班的机器人中取
				Player robot = RobotMgr.getOnDutyRobotByID(info.getSlavesUserId());
				if (null != robot) {
					tempPlayer = robot.getPlayerDetail();
				}
			}		
			 if(tempPlayer != null)
			 {				 
				 info.setSlavesUserName(tempPlayer.getPlayerInfo().getUserName());
				 info.setLevel(tempPlayer.getPlayerInfo().getLevel());
				 info.setWin(tempPlayer.getPlayerInfo().getLevel());
				 info.setLose(tempPlayer.getPlayerInfo().getLevel());				 
				 info.setSex(tempPlayer.getPlayerInfo().getSex());
				 info.setSlavesCharmValve(tempPlayer.getPlayerInfo().getCharmValve());
				 info.setSlavesPos(tempPlayer.getPlayerInfo().getPos());
				 info.setCity(tempPlayer.getPlayerInfo().getCity());
				 info.setSlavesCoins(tempPlayer.getPlayerInfo().getCoins());
				 info.setSlavesMoney(tempPlayer.getPlayerInfo().getMoney());				 
			 }
			 
			 
			response.putInt(info.getId());
			String str = "";
			if (!info.getSlavesPicPath().isEmpty()) {
				str = HeadPicUtil.getSmallPicPath(info.getSlavesPicPath());
			}
			response.putStr(str);//玩家图片
            response.putStr(info.getSlavesUserName());
            response.putInt(info.getSlavesUserId());
            response.putInt(info.getLevel());
            response.putStr(LevelMgr.getLevelTitle(info.getLevel()));
            response.putInt(info.getWin());
            response.putInt(info.getLose());
            response.putByte((byte)info.getVipLevel());
            

            double[] slavesPosArray = DistanceUtil.getLatAndLon(info.getSlavesPos());
            double distance = DistanceUtil.distanceByLatLon(userPosArray[0],
                                                            userPosArray[1],
                                                            slavesPosArray[0],
                                                            slavesPosArray[1]);
            response.putInt((int) distance);
            response.putBoolean(true);
            response.putInt(info.getSex());
           
            
            
            response.putInt(info.getSlavesCharmValve());
            response.putInt(info.getIncomeCharmValve());
            
            
            
            
            int getType = 0;
            String workName = "";
            if(info.getIncomeExp() != 0)
            {
            	UserRefWorkInfo work = ReferenceMgr.getWorkById(Math.abs(info.getIncomeExp()));     
            	workName = work.getWorkName();
            	
            	if(info.getIncomeCoins() < 0)
            	{
            		if(player.getUserReward().getLoginAward() < Integer.parseInt(Config.getValue("ref_getcoins_max")))
                    {            	
                		if(Math.abs(info.getIncomeCoins()) == work.getWorkTime()*work.getWorkCoin())            		           			
                			getType = 1;            		
                		else
                			getType = 2;            	
                    }
                    else
                    {
                    	getType = 3;
                    }            	
            	}            	
            }
            response.putStr(workName);
            
            
            

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
                Timestamp lastQiutDate = info.getSlavesLastLoginDate();
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
            response.putStr(timeInterval);//间隔时间
            
            
            
            
           
            
            // 默认为不在线
            isPlaying = 3;            
            if(null == tempPlayer) {
            	// 从机器人里面取
            	Player pl = RobotMgr.getOnDutyRobotByID(info.getSlavesUserId());//从在值班的机器人中取
            	if(null != pl) {
            		isPlaying = pl.isPlaying() ? 1 : 2;
//            		SlavesCount = pl.getPlayerDetail().getPlayerReference().getCurrentSlavesCount();
            	}
            } else {
            	// 在线，正在游戏 1， 不在游戏 2
            	isPlaying = tempPlayer.getIsPlaying() ? 1 : 2;
//            	SlavesCount = tempPlayer.getPlayerReference().getCurrentSlavesCount();
            }
            response.putInt(isPlaying);
            
            int timeLeft = player.getPlayerReference().checkRefWork(info);
            response.putInt(timeLeft);
            
            
            
            
            response.putByte((byte)getType);//关系状态
            response.putInt(info.getIncomeCoins());//税金
            
//            System.out.println("slaveslist==================" + info.getId() + "===========" + info.getIncomeExp() + "===========" + info.getIncomeCoins());
		}
	}
	
	public void userReferenceLogbuildResponse(UserReferenceLogInfo info, Packet response) {
		response.putByte((byte)info.getLogType());
		String initiativeUserName = info.getInitiativeUserName();
		String passivesUserName = info.getPassivesUserName();
		if(null == initiativeUserName || initiativeUserName.isEmpty()) {
			initiativeUserName = "";
		}
		if(null == passivesUserName || passivesUserName.isEmpty()) {
			passivesUserName = "";
		}
		response.putStr(initiativeUserName);
		response.putStr(passivesUserName);
		response.putInt(info.getIncomeCoins());
		response.putInt(info.getIncomeExp());
	}
}
