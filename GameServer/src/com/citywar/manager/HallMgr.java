package com.citywar.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.citywar.bll.HallTypeBussiness;
import com.citywar.bll.PengConfigBussiness;
import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.dice.entity.PengConfigInfo;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.hall.BaseHall;
import com.citywar.queue.SelfDrivenRunnableQueue;
import com.citywar.room.BaseRoom;
import com.citywar.room.RobRoom;
import com.citywar.room.action.AbstractRoomAction;
import com.citywar.room.action.ChangePlaceOperationAction;
import com.citywar.room.action.CheckEndGameAction;
import com.citywar.room.action.EnterRoomAction;
import com.citywar.room.action.ExitRoomAction;
import com.citywar.room.action.RobotEnterRoomAction;
import com.citywar.room.action.UpdatePlayerStateAction;
import com.citywar.type.HallGameType;
import com.citywar.util.ThreadSafeRandom;

/**
 * 大厅管理类--主要提供大厅的房间数据管理,替代房间管理类
 * 
 * @author Jacky.zheng
 * @date 2012-04-28
 *
 */
public class HallMgr {

	/**
	 * 大厅专用线程池线程数量
	 */
	private static final int THREAD_NUM = 8;

	/**
	 * 服务器所有大厅对象
	 */
	private static Map<Integer, BaseHall> allHalls = new LinkedHashMap<Integer, BaseHall>();

	/**
	 * 自驱动的action队列
	 */
	private static SelfDrivenRunnableQueue<AbstractRoomAction> actionQueue;

	/**
	 * 所有房间逻辑使用的线程池
	 */
	private static ExecutorService executorService;

	/**
	 * 正在使用中的房间数量
	 */
	private static AtomicInteger usingRoomCount;

	/**
	 * 是否正在运行
	 */
	private static boolean isRunning;

	/**
	 * 大厅类型列表（主要是因为大厅是以数组来存储的，所以用的是List而不是Map） 所以大厅类型ID必须连续，而且与大厅ID一致
	 */
	private static List<HallTypeInfo> hallTypes;

	private static List<PengConfigInfo> pengConfigs;

	private static Map<String, PengConfigInfo> mapPengConfigs;

	public static boolean init() {

		// 创建大厅逻辑专用线程池
		executorService = Executors.newFixedThreadPool(THREAD_NUM);
		actionQueue = new SelfDrivenRunnableQueue<AbstractRoomAction>(executorService);

		hallTypes = HallTypeBussiness.getAllHallTypeInfos();
		pengConfigs = PengConfigBussiness.getAllConfigsInfo();

		if (null == hallTypes || hallTypes.size() == 0) {
			return false;
		}
		// 生成大厅列表
		int maxRoomCount = hallTypes.size();// 大厅数量

		mapPengConfigs = new HashMap<String, PengConfigInfo>();
		for (int index = 0; index < pengConfigs.size(); index++) {
			mapPengConfigs.put(pengConfigs.get(index).getTypeId() + "-" + pengConfigs.get(index).getTypeIndex(),
					pengConfigs.get(index));
		}

		for (int index = 0; index < maxRoomCount; index++) {
			int hallTypeId = hallTypes.get(index).getHallTypeId();
			BaseHall hall = new BaseHall(hallTypeId, hallTypes.get(index));
			allHalls.put(hallTypeId, hall);
			// allHalls[index] = new BaseHall(index + 1, hallTypes.get(index));//初始化大厅。带类型的
		}

		usingRoomCount = new AtomicInteger(0);

		return true;
	}

	public static boolean reload() {
		// 创建大厅逻辑专用线程池

		hallTypes = HallTypeBussiness.getAllHallTypeInfos();
		if (null == hallTypes || hallTypes.size() == 0) {
			return false;
		}

		pengConfigs = PengConfigBussiness.getAllConfigsInfo();
		if (null == pengConfigs || pengConfigs.size() == 0) {
			return false;
		}

		mapPengConfigs.clear();
		for (int index = 0; index < pengConfigs.size(); index++) {
			mapPengConfigs.put(pengConfigs.get(index).getTypeId() + "-" + pengConfigs.get(index).getTypeIndex(),
					pengConfigs.get(index));
		}

		// 生成大厅列表
		int maxRoomCount = hallTypes.size();// 大厅数量
		allHalls.clear();
		for (int index = 0; index < maxRoomCount; index++) {
			// allHalls.get(index).setHallType(hallTypes.get(index));
			BaseHall hall = new BaseHall(index + 1, hallTypes.get(index));
			allHalls.put(hallTypes.get(index).getHallTypeId(), hall);
		}

		return true;
	}

	public static void checkHallStage() {

		// Timestamp now = new Timestamp(System.currentTimeMillis());
		// if(now.getMinutes() != 0 && now.getMinutes() != 30)
		// {
		// return;
		// }

		// System.out.println("=======checkHallStage=================================================");
		synchronized (allHalls) {
			for (BaseHall hall : allHalls.values()) {
				hall.checkStage();
			}
		}
	}

	/**
	 * @return the executorService
	 */
	public static ExecutorService getExecutorService() {
		return executorService;
	}

	public static boolean start() {
		if (!isRunning) {
			isRunning = true;

			// 重新初始化管理类
			init();
		}
		return true;
	}

	/**
	 * 服务器关闭时的善后处理
	 */
	public static boolean stop() {
		if (isRunning) {
			isRunning = false;
			executorService.shutdown();
			actionQueue.clear();
		}
		return true;
	}

	/**
	 * 增加已使用房间数量，非线程安全
	 */
	public static void addUsingRoomCount() {
		usingRoomCount.incrementAndGet();
	}

	/**
	 * 减少已使用房间数量，非线程安全
	 */
	public static void subUsingRoomCount() {
		usingRoomCount.decrementAndGet();
	}

	/**
	 * 获取当前正在使用的房间数量
	 * 
	 * @return 使用中的房间数量
	 */
	public static int getUsingRoomCount() {
		return usingRoomCount.get();
	}

	public static void addAction(AbstractRoomAction action) {
		action.setActionQueue(actionQueue);
		actionQueue.add(action);
	}

	/**
	 * player enter the room
	 * 
	 * @param player
	 * @param roomId
	 */
	public static void enterRoom(GamePlayer player, int roomId, byte cmdType, String password, int followPlayerId,
			List<Integer> paraList) {
		addAction(new EnterRoomAction(player, roomId, cmdType, password, followPlayerId, paraList));
	}

	/**
	 * 加入指定房间
	 * 
	 * @param player
	 * @param roomId
	 */
	public static void enterRoom(GamePlayer player, int roomId) {
		enterRoom(player, roomId, (byte) 1, "", -1, null);
	}

	/**
	 * player enter the room
	 * 
	 * @param player
	 * @param roomId
	 */
	public static void enterRoom(AbstractRoomAction action) {
		if (null != action) {
			addAction(action);
		}
	}

	public static void exitRoom(BaseRoom room, GamePlayer player, boolean isStageGame, int pengQuitType) {
		addAction(new ExitRoomAction(room, player, isStageGame, pengQuitType));
	}

	public static List<BaseRoom> getAllUsingRoom() {
		List<BaseRoom> list = new ArrayList<BaseRoom>();
		synchronized (allHalls) {
			for (BaseHall hall : allHalls.values()) {
				for (BaseRoom room : hall.getALLRooms()) {
					if (room.isUsing()) {
						list.add(room);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 随机获得一个没有使用的房间
	 * 
	 * @return
	 */
	public static BaseRoom getRondomNotUsingRoom(int hallId) {
		if (hallId <= 0) {
			return null;
		}
		ThreadSafeRandom random = new ThreadSafeRandom();
		BaseHall hall = allHalls.get(hallId);
		BaseRoom[] allRooms = hall.getALLRooms();
		BaseRoom room = allRooms[random.next(0, allRooms.length)];
		while (room.isUsing()) {
			room = allRooms[random.next(0, allRooms.length)];
		}
		return room;
	}

	/**
	 * 按顺序获得一个没有使用的房间
	 * 
	 * @return
	 */
	public static BaseRoom getOrderNotUsingRoom(int hallId) {
		if (hallId <= 0) {
			return null;
		}

		ThreadSafeRandom random = new ThreadSafeRandom();
		BaseHall hall = allHalls.get(hallId);
		BaseRoom[] allRooms = hall.getALLRooms();
		int i = random.next(0, allRooms.length);
		BaseRoom room = allRooms[i];
		while (room.isUsing()) {
			room = allRooms[random.next(0, allRooms.length)];
		}
		return room;
	}

	/**
	 * 更新房间位置的开关
	 * 
	 * @param room
	 * @param index
	 * @param isOpen
	 */
	public static void updateRoomPlace(BaseRoom room, int index, boolean isOpen) {
		addAction(new ChangePlaceOperationAction(room, index, isOpen));
	}

	/**
	 * when the player is offline or player click button [Trustee],change player state to Trustee.
	 * 
	 * @param currentRoom
	 * @param gamePlayer
	 */
	public static void handlePlayerTrustee(BaseRoom currentRoom, GamePlayer gamePlayer, boolean isRobotState) {
		addAction(new UpdatePlayerStateAction(currentRoom, gamePlayer, isRobotState));
	}

	public static void checkEndGame(int hallId, int delay) {
		addAction(new CheckEndGameAction(hallId, delay));
	}

	/**
	 * 获取指定大厅号号的大厅
	 * 
	 * @param roomId
	 *            房间ID
	 * @return id合法时返回对应房间的房间对象，否则返回null
	 */
	public static BaseHall getHallById(int hallId) {
		if (hallId <= 0)
			return null;
		else
			return allHalls.get(hallId);
	}

	/**
	 * 获取一个大厅内最新的房间列表
	 * 
	 * @return
	 */
	public static List<Integer> getTheNewestRoomsList(int hallId, int count) {
		List<Integer> result = new ArrayList<Integer>();
		Map<Integer, List<Integer>> roomMap = new HashMap<Integer, List<Integer>>();
		if (hallId <= 0) {
			return null;
		} else {
			BaseHall hall = allHalls.get(hallId);
			BaseRoom[] rooms = hall.getALLRooms();
			for (int i = 0; i < rooms.length; i++) {
				if (rooms[i].isNeedPassword() && !rooms[i].isLock()) {// 需要密码的跳过
					addRoomIdToMap(roomMap, 5, rooms[i], count);
				} else if (rooms[i] instanceof RobRoom) {
					addRoomIdToMap(roomMap, 6, rooms[i], count);
				} else {
					addRoomIdToMap(roomMap, rooms[i], count);
				}
			}
			FromMapAddListToResult(roomMap, 6, result, 0, 2);
			FromMapAddListToResult(roomMap, 2, result, 0, 100);
			FromMapAddListToResult(roomMap, 4, result, 0, 100);
			FromMapAddListToResult(roomMap, 6, result, 2, 100);
			FromMapAddListToResult(roomMap, 1, result, 0, 100);
			List<Integer> roomIdList = roomMap.get(0);
			if (result.size() == 0 && null != roomIdList && roomIdList.size() > 1) {// 至少要有两个空房间
				ThreadSafeRandom random = new ThreadSafeRandom();
				int randomId = random.next(1, 8 < roomIdList.size() ? 8 : roomIdList.size()) - 1;
				addRobotToRoom(20, randomId, hall);
			}
			if (roomIdList != null) {
				result.addAll(roomIdList);
			}
			FromMapAddListToResult(roomMap, 5, result, 0, 100);
			// 如果数量多了,需要移除多余的房间
			while (result.size() > count) {
				result.remove(count);
			}
		}
		return result;
	}

	/**
	 * 随机添加机器人到随机空房间--留给大厅人少的时候添加机器人用
	 * 
	 * @param count
	 *            -- 数量
	 * @param roomid
	 *            -- 加入机器人的起始房间id
	 * @param hall
	 *            -- 加入所在大厅
	 */
	public static void addRobotToRoom(int count, int roomId, BaseHall hall) {
		Map<Integer, Player> robotMap = new HashMap<Integer, Player>();
		for (int i = roomId; i < hall.getALLRooms().length && count > 0; i++) {
			BaseRoom currentRoom = hall.getRoomById(i);
			if (null == currentRoom || currentRoom.isUsing()) {
				continue;
			}
			Player robot = RobotMgr.getRobot(hall.getHallId());// TODO 多大厅
			if (null != robot) {
				if (!robotMap.containsKey(robot.getPlayerID())) {
					robotMap.put(robot.getPlayerID(), robot);
					RoomMgr.addAction(new RobotEnterRoomAction(currentRoom, robot));
					count--;
				}
			}
			// 有50%的概率 不再下个房间加机器人 为了一屏幕显示2个机器人做的贡献
			int random = new Random().nextInt(3);
			i += random;
		}
	}

	public static void addRoomIdToMap(Map<Integer, List<Integer>> roomMap, Integer key, BaseRoom room, int count) {
		List<Integer> roomIdList = roomMap.get(key);
		if (key == 4 && null != roomIdList && roomIdList.size() > count) {
			// 至少给用户一个可以加入的房间.不然这里有一个ＢＵＧ就是将来有２８个房间满了，那永远会取到这２８个房间满的
			return;
		}
		if (null == roomIdList) {
			roomIdList = new ArrayList<Integer>();
			roomMap.put(key, roomIdList);
		}
		roomIdList.add(room.getRoomId());
	}

	public static void addRoomIdToMap(Map<Integer, List<Integer>> roomMap, BaseRoom room, int count) {
		int roomPlayerCount = room.getPlayerCount();
		addRoomIdToMap(roomMap, roomPlayerCount, room, count);
	}

	public static void FromMapAddListToResult(Map<Integer, List<Integer>> roomMap, Integer key, List<Integer> result,
			int startIndex, int count) {
		List<Integer> roomIdList = roomMap.get(key);
		if (null != roomIdList && roomIdList.size() > 0) {
			// if(key == 4 )
			// {
			// int addkey0Count = 12-result.size();
			// //如果result里面的数据不足12桌 用空桌来填充
			// if(addkey0Count>0){
			// List<Integer> addkey0List = roomMap.get(0);
			// while (addkey0Count>0 && addkey0List.size()>0) {
			// result.add(addkey0List.remove(0));
			// addkey0Count--;
			// }
			// }
			//
			// int time = result.size()/2;
			// for (int i = 0; i < time; i++)
			// {
			// int startIndex = i*4 + new Random().nextInt(3);
			// for (int j = 0; j < 2 && roomIdList.size()>0; j++) {
			// if(result.size()>startIndex)
			// {
			// result.add(startIndex,roomIdList.remove(0));
			// }
			// }
			// }
			// result.addAll(0,roomIdList);
			// }else

			if (count > roomIdList.size())
				count = roomIdList.size();

			if (startIndex > roomIdList.size())
				startIndex = roomIdList.size();

			result.addAll(roomIdList.subList(startIndex, count));
		}
	}

	/**
	 * 获取大厅列表
	 * 
	 * @return List<BaseHall>
	 */
	public static List<BaseHall> getHallTypesList() {
		if (null == allHalls) {
			return null;
		}
		List<BaseHall> list = new ArrayList<BaseHall>();
		for (BaseHall hall : allHalls.values()) {
			list.add(hall);
		}
		return list;
	}

	/**
	 * 获取大厅列表
	 * 
	 * @return List<BaseHall>
	 */
	public static List<BaseHall> getHallTypesList(int type) {
		if (null == allHalls) {
			return null;
		}
		List<BaseHall> list = new ArrayList<BaseHall>();
		for (BaseHall hall : allHalls.values()) {

			if (hall.getHallType().getHallType() == type)
				list.add(hall);
		}
		return list;
	}

	/**
	 * 获得快速游戏大厅
	 * 
	 * @param player
	 * @return
	 */
	public static BaseHall getQiuckGameHall(int hallTypeId, GamePlayer player) {

		if (null == hallTypes || null == player || null == player.getPlayerInfo()) {
			return null;
		}

		int hallId = 0;
		if (hallTypeId == HallGameType.SOCIAL) {
			int playerCoin = player.getPlayerInfo().getCoins();
			for (HallTypeInfo hallType : hallTypes) {
				if (hallType.getHallType() == HallGameType.SOCIAL && hallType.getLowestCoins() <= playerCoin
						&& hallType.getHighestCoins() >= playerCoin) {// 一个一个尝试
					hallId = hallType.getHallTypeId();
					break;// 成功者返回
				}
			}

			if (hallId == 0)
				hallId = 101;
		} else if (hallTypeId == HallGameType.CONTEST) {
			int playerCoin = player.getPlayerInfo().getCoins();
			for (int i = hallTypes.size() - 1; i >= 0; i--) {
				HallTypeInfo hallType = hallTypes.get(i);
				if (i == 0) {
					hallId = hallType.getHallTypeId();
					break;// 成功者返回
				} else if (hallType.getHallType() == HallGameType.CONTEST
						// && hallType.getHallTypeId() < 100
						&& hallType.getLowestCoins() <= playerCoin && hallType.getHighestCoins() >= playerCoin) {// 一个一个尝试
					hallId = hallType.getHallTypeId();
					break;// 成功者返回
				}
			}
		}

		return getHallById(hallId);
	}

	/**
	 * 获得快速游戏大厅
	 * 
	 * @param player
	 * @return
	 */
	public static BaseHall getQiuckGameHall(GamePlayer player) {
		if (null == hallTypes || null == player || null == player.getPlayerInfo()) {
			return null;
		}
		// int playerCharmValve = player.getPlayerInfo().getCharmValve();
		int playerCharmValve = player.getPlayerInfo().getCoins();
		int hallId = 0;

		for (int i = hallTypes.size(); i > 0; i--) {
			HallTypeInfo hallType = hallTypes.get(i - 1);
			if (hallType.getLowestCoins() <= playerCharmValve && hallType.getHighestCoins() >= playerCharmValve
					&& hallType.getHallType() == HallGameType.SOCIAL) {// 一个一个尝试
				hallId = hallType.getHallTypeId();
				break;// 成功者返回
			}
		}
		// for(HallTypeInfo hallType : hallTypes) {
		// if(hallType.getQuickGameLowest() <= playerCharmValve
		// && hallType.getQuickGameHighest() >= playerCharmValve) {//一个一个尝试
		// hallId = hallType.getHallTypeId();
		// break ;//成功者返回
		// }
		// }
		return getHallById(hallId);
	}

	/**
	 * 获得快速撮合游戏房间
	 * 
	 * @param player
	 * @return
	 */
	public static BaseRoom getQiuckGameRoom(GamePlayer player) {
		if (null == hallTypes || null == player.getCurrentHall()) {
			return null;
		}
		BaseRoom room = null;
		int hallId = player.getCurrentHall().getHallId();
		for (int i = (hallId - 1); i < allHalls.keySet().size() && i >= 0; i--) {
			BaseHall hall = allHalls.get(hallId);
			if (null != hall && null != hall.getALLRooms()) {
				room = getHavePlayerRoom(hall.getALLRooms(), player);
				if (null != room) {
					break;
				}
			}
		}
		return room;
	}

	/**
	 * 取得有人的房间
	 * 
	 * @param rooms
	 * @return
	 */
	private static BaseRoom getHavePlayerRoom(BaseRoom[] rooms, GamePlayer player) {
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i].isNeedPassword() || rooms[i].ishavePlayer(player)) {// 需要密码的跳过
				continue;
			}
			if (rooms[i].getPlayerCount() >= 4 && rooms[i].canAddPlayer())
				continue;
			if (rooms[i].getPlacesCount() <= 3 && rooms[i].canAddPlayer())
				return rooms[i];
			if (rooms[i].getPlayerCount() >= 1 && rooms[i].canAddPlayer())
				return rooms[i];
		}
		return null;
	}

	/**
	 * 查找用户适合的大厅ID列表
	 * 
	 * @param player
	 * @return suitHalls
	 */
	public static List<Integer> findPlayerSuitHall(GamePlayer player) {
		if (null == player) {
			return null;
		}
		int coins = player.getPlayerInfo().getCharmValve();
		List<Integer> suitHalls = new ArrayList<Integer>();
		for (HallTypeInfo typeInfo : hallTypes) {
			if (coins >= typeInfo.getLowestCoins()) {
				suitHalls.add(typeInfo.getHallTypeId());
			}
		}
		return suitHalls;
	}

	/**
	 * 查找两个用户适合的大厅ID 取最小的玩家金币数通过取 (机器人只能进新手场)
	 * 
	 * @param player
	 * @param otherPlayer
	 * @return
	 */
	public static BaseHall canStayOneHall(GamePlayer player, GamePlayer otherPlayer) {
		if (null == player || null == otherPlayer) {
			return null;
		}
		if (player.getIsRobot() || otherPlayer.getIsRobot()) {
			return getHallById(1);// 机器人只能进新手场
		}
		int coins = player.getPlayerInfo().getCharmValve();
		int otherCoins = otherPlayer.getPlayerInfo().getCharmValve();
		GamePlayer lessPlayer = player;// 取最小的玩家金
		if (coins > otherCoins) {
			lessPlayer = otherPlayer;
		}
		return getQiuckGameHall(lessPlayer);
	}

	/**
	 * 获得进入某大厅的最小金币数
	 * 
	 * @param hallId
	 * @return
	 */
	public static int getHallLowestCoins(int hallId) {
		HallTypeInfo typeInfo = hallTypes.get(hallId - 1);
		return typeInfo.getLowestCoins();
	}

	/**
	 * 获得当前金币能首次进入的大厅
	 * 
	 * @param hallId
	 * @return 大厅的ID
	 */
	public static HallTypeInfo getFirstEnterHall(int coins) {

		for (int i = 0; i < hallTypes.size(); i++) {
			if (hallTypes.get(i).getFirstCoins() > coins) {
				if (i > 0) {
					return hallTypes.get(i - 1);
				} else {
					hallTypes.get(0);
				}
			}
		}
		return hallTypes.get(hallTypes.size() - 1);
	}

	/**
	 * 获得大厅的数量
	 * 
	 * @param hallId
	 * @return
	 */
	public static int getHallCount() {
		return hallTypes.size();
	}

	public static List<PengConfigInfo> getPengConfigList() {
		return pengConfigs;
	}

	public static PengConfigInfo getPengConfig(int id, int index) {
		return mapPengConfigs.get(id + "-" + index);
	}

	/**
	 * 移除房间中的所有玩家
	 * 
	 * @param result
	 * @param packet
	 */
	public static void removeRoomPlayers(int hallId, int roomId) {
		BaseHall hall = getHallById(hallId);
		if (null != hall) {
			BaseRoom room = hall.getRoomById(roomId);
			if (null != room) {
				room.removeAllPlayer();
			}
		}
	}

}
