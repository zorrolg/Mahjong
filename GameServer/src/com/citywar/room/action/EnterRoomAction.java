/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.room.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.bll.common.LanguageMgr;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.game.action.UserAddGameAction;
import com.citywar.hall.BaseHall;
import com.citywar.manager.GameMgr;
import com.citywar.manager.HallMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.room.BaseRoom;
import com.citywar.type.GameType;
import com.citywar.type.UserCmdOutType;
import com.citywar.util.ThreadSafeRandom;

/**
 * user enter the room.
 * 
 * @author Dream
 * @date 2011-12-16
 * @version
 * 
 */
public class EnterRoomAction extends AbstractRoomAction {

	private ThreadSafeRandom random;
	
	private GamePlayer player;

	private int roomId;
	
	private byte cmdType;
	
	private String password;
	
	private int followPlayerId;

	private List<Integer> paraList;
	
	private static Logger logger = Logger.getLogger(EnterRoomAction.class
			.getName());

	public EnterRoomAction(GamePlayer player, int roomId, byte cmdType, String password, int followPlayerId, List<Integer> paraList) {
		random = new ThreadSafeRandom();
		this.player = player;
		this.roomId = roomId;
		this.cmdType = cmdType;
		this.password = password;
		this.followPlayerId = followPlayerId;
		this.paraList = paraList;
	}
	/*
	 * byte createState = 0;
	 * errorId -- 表示进入房间失败的原因
	 * @值的意义:
	 * 0--成功进入房间,没有异常
	 * 1--表示当前用户当前的状态为不在大厅里,需要加入大厅
	 * 2--表示用户创建房间失败,大厅里已经没有可用的房间了--//TODO 这里可以扩展如果没有房间了进入下一个大厅创建
	 * 3--表示用户加入房间的时候,房间是有密码的,用户输入的密码跟房间的密码不匹配--导致加入失败
	 * 4--表示用户加入房间的时候,从客户端传递过来的房间号在大厅里是不存在的
	 * 5--表示用户跟随用户进入房间的时候,从客户端传递过来的用户id是不在线的或者非法的.
	 * 6--表示用户跟随用户进入房间的时候,跟随用户不在大厅里
	 * 7--表示用户跟随用户进入房间的时候,跟随用户不在房间里
	 * 8--表示用户加入房间的时候,传过来的密码是空,需要提示客户单输入密码~
	 * 9--表示用户加入房间的时候,房间已经满了
	 *10--房间加锁了 不能进入
	 */
	@Override
	public void execute() {
		try {
			
						
			
			// if the player had been inside a room,remove the player from the room.
			BaseRoom curRoom = player.getCurrentRoom();
			if (curRoom != null && curRoom.getGame().getIsContinueGame()) {
												
				if(player.getCurrentHall().getHallType().getGameType() == GameType.ErqiwangGame)
				{
					player.getOut().sendRoomCreate((byte)-1, curRoom, UserCmdOutType.GAME_ROOM_CREATE);
					return;
				}
					
				
				
				
				curRoom.getGame().changePlayerRobotState(player.getUserId(), false);	
				
				player.sendEnterTrusteedRoom();//				
				curRoom.getGame().SendGameContinue(player);
//				System.out.println("EnterRoomAction=======================" + player.getUserId());
				return;
			}
			
			
			
			
			// check the player whether has the priority entering a room.
			if(cmdType == 3){
				player.setCurrenetHall(null);		//玩家在主页点击的快速游戏
			}
//			if(player.getCurrentRoom() != null) {
//				player.getCurrentRoom().removePlayer(player);
//			}
			
			
			
			
			
			
			
			
			// find a room to enter.
			byte createState = 0;
			BaseHall hall = null;
			BaseRoom room = null;
			hall = player.getCurrentHall();
			player.clearAlreadyRoom();
			
			
			
			
			
			switch (cmdType) {
			case 0://表示创建房间
				if (null != hall) {
					room = HallMgr.getOrderNotUsingRoom(hall.getHallId());//方法里已经判断是否房间已经使用
					if (null != room) {
						roomId = room.getRoomId();
						if (!password.isEmpty()) {//创建房间的时候密码不为空,则需要为房间加上密码
							room.setPassword(password);
						}
					} else {
						createState = 2;
					}
				} else {
					createState = 1;
				}
				break;
			case 1://表示加入指定roomId的房间
				if (null != hall ) {
					room = hall.getRoomById(roomId);
					if (null != room ) {
						if (room.isNeedPassword()) {
							if(room.isLock()){
								createState = 10;
							}
							else if (password.isEmpty()) {
								createState = 8;
							} else {
								if (!password.equals(room.getPassword())) {
									createState = 3;
								}
							}
						}
					} else {
						createState = 4;
					}
				} else {
					createState = 1;					
				}
				break;
			case 2://表示跟随用户(好友)加入其所在房间
				GamePlayer followPlayer = WorldMgr.getPlayerByID(followPlayerId);
				if (null == followPlayer) {
					//从在值班的机器人中取
					Player robot = RobotMgr.getOnDutyRobotByID(followPlayerId);
					if (null != robot) {
						followPlayer = robot.getPlayerDetail();
					} else {
						createState = 5;
					}
				}
				if (null != followPlayer) {
					hall = followPlayer.getCurrentHall();
					if (null != hall) {
						room = followPlayer.getCurrentRoom();
						if (null == room) {
							createState = 7;
						}else if (room.isNeedPassword() && room.isLock()) //上锁的房间 不能跟随进入
						{
							createState = 10;
						}
					} else {
						createState = 6;
					}
				} else {
					createState = 5;
				}
				break;
			case 3://快速加入房间
				hall = HallMgr.getQiuckGameHall(player);
				if (null != hall) {
					room = findRandomRoom(hall.getALLRooms(), hall.getHallType().getPlayerCountMax());
				} else {
					createState = 2;
				}
				break;
			case 4://快速加入快醉的玩家房间
				GamePlayer haveDrunkPlayer = null;
				List<Integer> suitHallId = HallMgr.findPlayerSuitHall(player);
				for (int hallId : suitHallId) {
					hall = HallMgr.getHallById(hallId);
					List<GamePlayer> allPlayers = hall.getPlayers();
					int differentSex = getDifferentSex(player.getPlayerInfo().getSex());
					for (GamePlayer tempPlayer : allPlayers) {
						if (differentSex == tempPlayer.getPlayerInfo().getSex()
								&& tempPlayer.getIsPlaying()
								&& null != tempPlayer.getCurrentRoom()
								&& tempPlayer.getCurrentRoom().canAddPlayer()
								&& tempPlayer.getDrunkLevel() <= 3) {//快醉的
							haveDrunkPlayer = tempPlayer;
							break;
						}
					}
					if(null != haveDrunkPlayer) {
						room = haveDrunkPlayer.getCurrentRoom();
						break;
					}
				}
				if(null == haveDrunkPlayer && null != hall) {
					room = qiuckFindEmptyRoom(hall.getALLRooms());
				}
				break;
			case 5://快速加入当前大厅房间
				hall = player.getCurrentHall();
								
				if (null != hall) {
//					hall = HallMgr.getQiuckGameHall(hallTypeId, player);
					room = findRandomRoom(hall.getALLRooms(), hall.getHallType().getPlayerCountMax());
				} 
				else 
				{
					
					BaseHall newhall = HallMgr.getQiuckGameHall(player);//hallType.getHallType()					
					if(newhall != null)
					{
						newhall.playerInHall(player);//加入大厅
						room = findRandomRoom(newhall.getALLRooms(), newhall.getHallType().getPlayerCountMax());
						createState = 0;
					}					
				}
				break;
				
			case 10://朋友局
				hall = player.getCurrentHall();
								
				if (null != hall) {
					room = qiuckFindEmptyRoom(hall.getALLRooms());//findRandomRoom(hall.getALLRooms(), hall.getHallType().getPlayerCountMax());
										
					if(GameMgr.isPengYouCreateRoom(hall.getHallType().getGameType(), room, player, this.paraList))
					{
						createState = 0;						
						player.setPengLoseCount(0);
						player.setPengWinCount(0);
//						room.setPassword(String.valueOf(random.next(1000, 9999)));				
					}
					else
					{
						createState = 11;
//						player.getOut().sendRoomReturnMessage(false,LanguageMgr.getTranslation("CityWar.PengYou.NoCard"));
					}
				}
				
				break;
			case 11://快速加入当前大厅房间
				if (null != hall ) 
				{
					room = hall.getRoomById(roomId);
					if (null != room ) 
					{
						if(room.isUsing())
						{
							createState = 0;							
						}
						else
						{
							createState = 4;
						}
					}
					else
					{
						createState = 4;
					}
				}
				else
				{
					createState = 1;
				}
				break;

			}
			
			
			
			
			
			
//			if(hall != null && hall.getHallType().getHallType() == HallGameType.CONTEST && cmdType != 1)
//				return;
			
			
			
			
			

			//hall check
			if(hall != null && !hall.canStayHere(player)) {
				createState = 7;
			}
//			else if(hall != null && hall != player.getCurrentHall()) {
//				hall.playerInHall(player);
//			}
									
			//room check
			if(createState == 0)
			{
				if(room == null){
					logger.error("EnterRoomAction null == player.getSession()。玩家在房间状态出现异常====" + cmdType);
					createState = 4;
				}			
				else if(!room.canStayHere(player)){
					createState = 7;
				}
				else if(!room.hasPlace()){
					createState = 9;
				}
			}
			
					
			
			
			
			
			
			//in room
			if (createState == 0){		
				if(room.addPlayer(player))
					player.getOut().sendRoomCreate(createState, room, UserCmdOutType.GAME_ROOM_CREATE);
				else
					createState = 9;
			}
						
			//fail
			if (createState > 0) {
				player.getOut().sendRoomReturnMessage(false,LanguageMgr.getTranslation("CityWar.EnterRoom.Fail" + createState));
				return;
			}
			
			
			
			

			
			
			
			
//			// check room whether has place,if not adding player to watching list.
//			if (room.hasPlace()) {
//				// adding player to the place.
//				if (!room.addPlayer(player))
//					return;
//			} else {
//				room.addWatchPlayer(player);
//			}

			// check game instance of the room whether exists,if not,create one.
			//System.out.println("用户进入的房间号为====" + room.getRoomId());
			if (room.getGame() == null)
			{
				room.createGame();
				player.getPlayerInfo().setPengYouCoins(0);
			} else {
				//System.out.println("用户加入游戏的时候加入游戏对象BaseGame===" + room.getGame().getId());
			}
			// 在房间中存在 Session 为空的用户,这个时候需要在其他玩家加入房间的时候踢出异常玩家
//			if(null != room && hall.getHallType().getHallType() == HallGameType.SOCIAL)
//			{
//				for (GamePlayer player : room.getPlayers())
//				{
//					if(!player.getIsRobot())
//					{
//						if (null == player.getSession() || !player.getSession().isOpen())
//						{
////								logger.error("EnterRoomAction null == player.getSession()。玩家在房间状态出现异常");
//							room.removePlayer(player);
//						}
//					}
//				}
//			}
			
			// the logic handle may deal with by a game action.
			// 踢出异常玩家后,需要判断当前游戏对象是否还存在
			if (room.getGame() != null)
			{
				room.getGame().AddAction(new UserAddGameAction(player));
				if((3 == cmdType || 5 == cmdType) && room.getPlayerCount() == 1) {//如果为快速加入，且房间为空，则加机器人
					Player robot = RobotMgr.getRobot(hall.getHallId());//TODO 多大厅
					if(null != robot) {
						//RoomMgr.addAction(new RobotEnterRoomAction(room, robot));
					}
				}else if(4 == cmdType && room.getPlayerCount() == 1) {//快速加入快醉的玩家房间，且房间为空，则加机器人
					Player robot = RobotMgr.getRobot(hall.getHallId());//TODO 多大厅
					
					player.addCoins(-1000 + random.next(20) * 10);
//            		robot.getPlayerDetail().getPlayerInfo().setCoins(
//            				player.getPlayerInfo().getCoins() - 1000 + random.next(20) * 10);//随机设置金币数
					if(null != robot) {
//						RoomMgr.addAction(new RobotEnterRoomAction(room, robot));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * check player whether has the priority of entering the room
	 * 
	 * @return true:cannot enter room false:can enter room
	 */
//	private boolean checkEnterRoom() {
//		//有房间这先移除
//		if(player.getCurrentRoom() != null) {
//			player.getCurrentRoom().removePlayer(player);
//		}
//
//		// 玩家身上金币不足100, 不允许进入房间--这个时候需要赠送500金币
//		boolean isPass = true;
//		if(player.getCurrentRoom().getHallType().getHallType() != HallGameType.PENGYOU)
//			isPass = player.isCoinsEnough();
//		return !isPass;
//	}
	
	/**
	 * get the room randomly.
	 * 最新版本的快速游戏
	 * @param rooms
	 * @return
	 */
	private BaseRoom findRandomRoom(BaseRoom[] rooms, int roomMaxCount) {
		BaseRoom room1 = null;
		BaseRoom room2 = null;
		BaseRoom room3 = null;
		BaseRoom room0 = null;
		int index = 0;

	    
	    
		int startRoom = random.next(rooms.length < 30 ? rooms.length : 30, rooms.length / 2);
		for (int i = 0; i < rooms.length; i++) {
			index++;
			if(rooms[i].isNeedPassword()) {//需要密码的跳过
				continue ;
			}
			
			
			// the player count is beyond 3,return.
			if (rooms[i].getPlayerCount() >= roomMaxCount && rooms[i].canAddPlayer())
				continue;

			if (rooms[i].getPlayerCount() == (roomMaxCount - 1) 
					&& rooms[i].canAddPlayer()
					&& !player.isAlreadyRoom(rooms[i].getRoomId()) 
					&& !rooms[i].isPlaying())
				return rooms[i];
			
			if (rooms[i].getPlayerCount() == (roomMaxCount - 2) 
					&& rooms[i].canAddPlayer() 
					&& !player.isAlreadyRoom(rooms[i].getRoomId())
					&& !rooms[i].isPlaying())
				return rooms[i];
			
			// if the room that any place is closed,return.
			if (rooms[i].getPlayerCount() == (roomMaxCount - 3) 
					&& rooms[i].canAddPlayer() 
					&& !player.isAlreadyRoom(rooms[i].getRoomId())
					&& !rooms[i].isPlaying())
				return rooms[i];


			if (rooms[i].getPlayerCount() == 1 && rooms[i].canAddPlayer() && !player.isAlreadyRoom(rooms[i].getRoomId())) {
				room1 = rooms[i];
			} else if (rooms[i].getPlayerCount() == 2 && rooms[i].canAddPlayer() && !player.isAlreadyRoom(rooms[i].getRoomId())) {
				room2 = rooms[i];
			} else if (rooms[i].getPlayerCount() == 3 && rooms[i].canAddPlayer() && !player.isAlreadyRoom(rooms[i].getRoomId())) {
				room3 = rooms[i];
			} else if (rooms[i].getPlayerCount() == 0 && rooms[i].canAddPlayer()
					&& null == room0 && i > startRoom && !player.isAlreadyRoom(rooms[i].getRoomId())) {//这样可以去前面的
				room0 = rooms[i];
			}

			if (index >= 180) {
				if (room2 != null)
					return room2;
				else if (room3 != null)
					return room3;
				else if (room1 != null)
					return room1;
				else
					index = 0;
			}
		}

		if (room2 != null)
			return room2;
		else if (room3 != null)
			return room3;
		else if (room1 != null)
			return room1;
		else if (room0 != null)
			return room0;
		
		return rooms[random.next(0, rooms.length)];
	}
	
	/**
	 * 快速游戏逻辑：
	 * 找没有设置密码的房间
	 * 1：去找只有一个玩家在的房间玩游戏
	 * 2：如果没有一个人在的房间，去找一个空房间加个机器人玩游戏
	 * @param rooms
	 * @return
	 */
//	private BaseRoom qiuckFindRandomRoom(BaseRoom[] rooms) {
//		BaseRoom room1 = null;//1：去找只有一个玩家在的房间玩游戏
//		BaseRoom room2 = null;//2：去找一个空房间加个机器人玩游戏
//		boolean haveOnePlayerRoom = false;
//
//		for (int i = 0; i < rooms.length; i++) {
//			if(rooms[i].isNeedPassword()) {//需要密码的跳过
//				continue ;
//			}
//			
//			if (rooms[i].getPlayerCount() == 1 && rooms[i].canAddPlayer()) {//是只有一个人的房间
//				List<GamePlayer> playerList = rooms[i].getPlayers();
//				if(null != playerList) {
//					GamePlayer gamePlayer = playerList.get(0);
//					if(null != gamePlayer) {
//						room1 = rooms[i];
//						break ;
//					}
//				}
//			}
//			if(rooms[i].getPlayerCount() == 0 && rooms[i].canAddPlayer() && !haveOnePlayerRoom) {//是空房间，并且是第一个空的
//				room2 = rooms[i];
//				haveOnePlayerRoom = true;
//			}
//		}
//
//		if (room1 != null)
//			return room1;
//		else if (room2 != null)
//			return room2;
//
//		return rooms[random.next(0, rooms.length)];
//	}
	
	/**
	 * 快速游戏逻辑：
	 * 找没有设置密码的房间
	 * 1：去找只有一个玩家在的房间玩游戏
	 * 2：如果没有一个人在的房间，去找一个空房间加个机器人玩游戏
	 * @param rooms
	 * @return
	 */
	private BaseRoom qiuckFindEmptyRoom(BaseRoom[] rooms) {
		BaseRoom room = null;//2：去找一个空房间

		for (int i = 0; i < rooms.length; i++) {
			if(rooms[i].isNeedPassword()) {//需要密码和锁的跳过
				continue ;
			}
			if(rooms[i].getPlayerCount() == 0 && rooms[i].canAddPlayer()) {//是空房间
				room = rooms[i];
				break ;
			}
		}
		if (room != null)
			return room;

		return rooms[random.next(0, rooms.length)];
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
