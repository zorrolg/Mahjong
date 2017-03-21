package com.citywar.manager;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.citywar.bll.RobotBussiness;
import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.hall.BaseHall;
import com.citywar.room.RobRoom;

public class RobRoomMgr {

	public static List<GamePlayer> allRoomRob = new ArrayList<GamePlayer>();

	/** key=大厅类型 */
	public static Map<Integer, RobRoom> allRobRoom = new HashMap<Integer, RobRoom>();

	/** 已使用的机器人下标 */
	public static int roomRobPosition = 0;

	/** 已使用的机器人图片下标 */
	public static int picUsePosition = 0;

	/** 占位机器人 总数 */
	public static int robSize = 8;

	/** 占位机器人 图片总数 */
	public static int roomRob_Pic_Count = 160;

	/** 占位机器人 图片规则 */
	public static String roomRob_pic_pattern = "{0}.jpg";

	public static Random random = new Random();
	/** 占位机器人 图片顺序 */
	public static List<String> usePicList = new ArrayList<String>();

	static {
		picPathSort();
	}

	/**
	 * 把占位机器人图片的顺序打乱
	 */
	private static void picPathSort() {
		
		usePicList.clear();
		
		int randomTime = roomRob_Pic_Count / 2;

		// 先随机取出一些图片
		for (int i = 0; i < randomTime; i++) {
			String currentPic = getRandomPic();
			if (!usePicList.contains(currentPic)) {
				usePicList.add(currentPic);
			}
		}

		// 再把所有图片加载到集合中
		for (int i = 0; i < roomRob_Pic_Count; i++) {
			String currentPic = getPicPathID(i);
			if (!usePicList.contains(currentPic)) {
				usePicList.add(currentPic);
			}
		}
	}

	public static boolean init()  {
		List<PlayerInfo> robotsInfo = RobotBussiness.getRoomRobPlayerInfo();
		if (robotsInfo == null) {
			return true;
		}
		GamePlayer gamePlayer;
		for (PlayerInfo info : robotsInfo) {
			info.setPicPath(userNextPosPic());
			gamePlayer = new GamePlayer(info.getUserId());
			gamePlayer.setPlayerInfo(info);
			gamePlayer.setIsRobot(true);
			allRoomRob.add(gamePlayer);
		}
		robSize = allRoomRob.size() - 1;
		
		if(robSize > roomRob_Pic_Count/3){
			 //System.out.println("机器人的头像不足 请补充机器人头像");
		}
		return true;
	}

	/**
	 * 创建一个机器人专用房间
	 * 
	 * @return null 表示无法创建机器人房间
	 */
	public static RobRoom createRobRoom(int roomId, BaseHall hall,
			HallTypeInfo hallType) {
		// 如果剩余可用的机器人 不够一桌 则不创建房间 直接返回
		if (hall == null || roomRobPosition + 4 >= robSize - 1) {
			return null;
		}
		RobRoom currentRoom = new RobRoom(roomId, hall.getHallId(), hallType);
		for (int j = 0; j < 4; j++) {
			GamePlayer rob = allRoomRob.get(roomRobPosition);
			hall.roomRobInHall(rob);// 机器人加入大厅
			currentRoom.addNpcPlayer(rob);
			roomRobPosition++;
		}
		allRobRoom.put(hall.getHallId(), currentRoom);
		return currentRoom;
	}

	/**
	 * 替换所有占位房间内机器人的头像
	 * 每个机器人的头像有50%的可能性被替换
	 */
	public static void replaceRoomRobHead() {
		
		for (GamePlayer p : allRoomRob) {

			if (random.nextInt(10) % 2 == 0) {
				p.getPlayerInfo().setPicPath(userNextPosPic());
			}
		}
	}

	/**
	 * 重置所有占位机器人的头像
	 */
	public static void resetPicPath() {
		
		picPathSort();
		picUsePosition = 0;
		for (GamePlayer info : allRoomRob) {
			info.getPlayerInfo().setPicPath(userNextPosPic());
		}
	}

	/**
	 * 刷新房间内占位机器人的头像
	 * 
	 * 有一定几率刷新房间内一个占位机器人的头像
	 * 
	 * @param room
	 */
	public static void refreshPic(RobRoom room) {
		int randomNum = random.nextInt(250);
		if (randomNum < 4) {
			List<GamePlayer> list = room.getPlayers();
			if (list.size() > randomNum) {
				//System.out.println("更新头像");
				list.get(randomNum).getPlayerInfo()
						.setPicPath(userNextPosPic());
			}
		}
	}

	public static String getRandomPic() {
		return MessageFormat.format(roomRob_pic_pattern,
				random.nextInt(roomRob_Pic_Count));
	}

	private static String getPicPathID(int id) {
		return MessageFormat.format(roomRob_pic_pattern, id);
	}

	private static String userNextPosPic() {
		// 使用的图片索引达到了最后一张 则刷新所有占位机器人的头像 以避免头像重复
		picUsePosition++;
		if (picUsePosition >= roomRob_Pic_Count) {
			resetPicPath();
		}
		return usePicList.get(picUsePosition);
	}

}
