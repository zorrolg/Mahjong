/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.entity.PlayerInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.HeadPicUtil;
//import com.citywar.util.ThreadSafeRandom;

/**
 * 查看快喝醉的用户
 * 
 * @author shanfeng.cao
 * @date 2011-12-20
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.SHOW_DRUNK_PLAYER, desc = "查看快喝醉的用户")
public class ShowDrunkPlayerCmd extends AbstractUserCmd {
	private static final Logger logger = Logger
			.getLogger(ShowDrunkPlayerCmd.class.getName());

	@SuppressWarnings("unchecked")
	@Override
	public int execute(GamePlayer player, Packet packet) {
		Packet pkg = new Packet(UserCmdOutType.SHOW_DRUNK_PLAYER);
		List<Integer> suitHallId = HallMgr.findPlayerSuitHall(player);
		List<GamePlayer> suitPlayers = new ArrayList<GamePlayer>();
		BaseHall hall = null;
		for (int hallId : suitHallId) {
			hall = HallMgr.getHallById(hallId);
			List<GamePlayer> allPlayers = hall.getPlayers();
			int differentSex = getDifferentSex(player.getPlayerInfo().getSex());
			for (GamePlayer tempPlayer : allPlayers) {
				if (differentSex == tempPlayer.getPlayerInfo().getSex()
						&& tempPlayer.getIsPlaying()
						&& null != tempPlayer.getCurrentRoom()
						&& tempPlayer.getCurrentRoom().canAddPlayer()) {
					suitPlayers.add(tempPlayer);
				}
			}
		}
		//添加几个机器人到这个集合里
        short count = 50;//防止死循环
//        ThreadSafeRandom random = new ThreadSafeRandom();
        while (suitPlayers.size() < 7 && count > 0) {
        	Player robot = RobotMgr.getRobot(player.getCurrentHall().getHallId());
        	if(null != robot && null != robot.getPlayerDetail()
        			&& null != robot.getPlayerDetail().getPlayerInfo()
        			&& ! suitPlayers.contains(robot.getPlayerDetail())) {
//        		robot.getPlayerDetail().getPlayerInfo().setDrunkLevel(random.next(1, 3));//设置醉酒度
        		suitPlayers.add(robot.getPlayerDetail());
        	}
        	count--;
    	}
		ComparatorPlayerDrunk comparator = new ComparatorPlayerDrunk();
		Collections.sort(suitPlayers, comparator);
		if (suitPlayers.size() == 0) {
			pkg.putInt(0);
		} else {
			pkg.putInt(suitPlayers.size() < 7 ? suitPlayers.size() : 7);//最长为7
			for (int i=0;i<suitPlayers.size() && i<7;i++) {
				GamePlayer suitPlayer = suitPlayers.get(i);
				pkg.putInt(suitPlayer.getUserId());//用户ID
				pkg.putInt(suitPlayer.getDrunkLevel());//用户醉酒度
				PlayerInfo info = suitPlayer.getPlayerInfo();
				if (null != info) {
					pkg.putStr(info.getUserName());//玩家姓名
					String str = "";
					if (!info.getPicPath().isEmpty()) {
						str = HeadPicUtil.getRealSmallPicPath(info.getUserId(), info.getPicPath());//玩家头像地址
					}
					pkg.putStr(str);// 玩家图片
				}
				pkg.putInt(suitPlayer.getPlayerInfo().getSex());//用户性别
			}
		}
		player.getOut().sendTCP(pkg);
		return 0;
	}

	/**
	 * 获取异性的代表类型 0为女1为男2为未知
	 * 
	 * @param sex
	 * @return
	 */
	public int getDifferentSex(int sex) {
		int resultSex = 1;
		switch (sex) {
		case 1:
			resultSex = 0;
			break;
		case 0:
			resultSex = 1;
			break;
		default:
			resultSex = 0;
			break;
		}
		return resultSex;
	}
}

/**
 * 比较醉酒度
 * @author shanfeng.cao
 *
 */
@SuppressWarnings("rawtypes")
class ComparatorPlayerDrunk implements Comparator {

	@Override
	public int compare(Object player, Object otherPlayer) {
		if (null == player || null == otherPlayer) {
			return 0;
		}
		GamePlayer user = (GamePlayer) player;
		GamePlayer otherUser = (GamePlayer) otherPlayer;
		int drunkLevel = user.getDrunkLevel();
		int otherDrunkLevel = otherUser.getDrunkLevel();
		return drunkLevel - otherDrunkLevel;
	}
}
