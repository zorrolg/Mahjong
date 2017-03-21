package com.citywar.hall;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.citywar.bll.StageDataBussiness;
import com.citywar.bll.StagePlayerBussiness;
import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.StageDataInfo;
import com.citywar.dice.entity.StageInfo;
import com.citywar.dice.entity.StagePlayerInfo;
import com.citywar.dice.entity.StagePrizeInfo;
import com.citywar.dice.entity.StageRoundInfo;
import com.citywar.game.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.manager.KKMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.RoomMgr;
import com.citywar.manager.StageMgr;
import com.citywar.manager.UserTopMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.room.BaseRoom;
import com.citywar.room.action.RobotEnterRoomAction;
import com.citywar.socket.Packet;
import com.citywar.type.HallGameType;
import com.citywar.type.UserCmdOutType;
import com.citywar.util.ThreadSafeRandom;
import com.citywar.util.TimeUtil;

/**
 * 游戏基本大厅信息
 * 
 * @author Jacky.zheng
 * @date 2012-04-28
 * @version 1.0
 * 
 */
public class BaseHall {
	// *@===BaseHall 对象的属性定义 begin ===@
	/**
	 * 大厅的ID
	 */
	private final int hallId;
	/**
	 * 大厅的类型
	 */
	private HallTypeInfo hallType;
	/**
	 * 大厅内的房间信息
	 */
	private final Map<Integer, BaseRoom> allRooms;
	/**
	 * 默认大厅拥有的房间数量
	 */
	private final static  int initRoomCount = 200;
    /**
     * 正在大厅中的玩家数量
     */
    private AtomicInteger hallPlayerCount;
	/**
	 * 在大厅里空闲的玩家集合
	 */
	private final List<GamePlayer> idlePlayers;
	
	/**
	 * 在大厅里的玩家集合
	 */
	private final List<GamePlayer> Players;

    private static ReadWriteLock lock = new ReentrantReadWriteLock(false);

    private static ThreadSafeRandom random = new ThreadSafeRandom();
    
	// **@===BaseHall 对象的属性定义 end===@
	
    
    
    
    
    private  List<StageInfo> myStageList;
    private  StageInfo currentStage;
    private  StageInfo lastStage;
    private  StageInfo nextStage;
    
    private int StageState;
    private  boolean haveHallStage;

    private final Object stageLock = new Object();
    private final Object stageRoundLock = new Object();
    
    
    
    
    private int finishedStageRoundCount;
    private StageRoundInfo currentStageRound;   
    private Map<Integer, GamePlayer> currentStagePlayers;
    private StageRoundInfo nextStageRound;   
    
    
    
    
    
	/**
	 * 默认构造方法，带大厅类型
	 */
	public BaseHall(int hallId, HallTypeInfo hallType) {
		this.hallId = hallId;
		this.hallType = hallType;//初始化大厅类型
		this.allRooms = new HashMap<Integer, BaseRoom>();// 申请数组的空间,还木有生成具体对象放入大厅中
		this.idlePlayers = new ArrayList<GamePlayer>();
		this.Players = new ArrayList<GamePlayer>();
		this.currentStagePlayers = new HashMap<Integer, GamePlayer>();
		hallPlayerCount = new AtomicInteger(0);
		
		
		int roomId = 0;
		int robRoomStartIndex = initRoomCount - hallType.getRobRoomNum();
		for (int i = 0; i < robRoomStartIndex; i++) 
		{
			roomId = hallId * 1000 + 201 + i;
			this.allRooms.put(roomId, new BaseRoom(roomId, hallId, hallType));
//			allRooms[i] = new BaseRoom(roomId, hallId, hallType);
		}
//		for (int i = robRoomStartIndex; i < initRoomCount; i++) 
//		{
//			RobRoom room = RobRoomMgr.createRobRoom(i + 1, this, hallType);
//			if(room == null )
//			{
//				allRooms[i] = new BaseRoom(i + 1, hallId, hallType);
//			}else{
//				allRooms[i] = room;
//			}
//		}
		
		
  
		myStageList = new ArrayList<StageInfo>();
		for(StageInfo stage : StageMgr.getAllStage().values())
		{
			if(stage.getHallTypeId() == hallType.getHallTypeId() && hallType.getHallType()  == HallGameType.CONTEST)
				myStageList.add(stage);
		}
				

		if(myStageList.size() > 0)
			haveHallStage = true;
		
	}

	
	
	
	/**
	 * 获取大厅所有的房间
	 * 
	 * @return
	 */
	public BaseRoom[] getALLRooms() {
		
		return toArray(allRooms.values(), BaseRoom.class);
	}

	/**
	 * 获取指定房间号的房间
	 * 
	 * @param roomId
	 *            房间ID
	 * @return id合法时返回对应房间的房间对象，否则返回null
	 */
	public BaseRoom getRoomById(int roomId) {
		
		return this.allRooms.get(roomId);
		
//		if (roomId <= 0 || roomId > allRooms.length) {
//			return null;
//		} else {
//			return allRooms[roomId - 1];
//		}
	}

	/**
	 * 玩家进入大厅
	 * 
	 * @param player 玩家
	 * 
	 * @since V2.2版本  对外公开
	 */
	public void addIdlePlayer(GamePlayer player) {
		synchronized (Players) {
			if( ! Players.contains(player)) {
				addHallPlayerCount();
				Players.add(player);
			}
		}
	}

	/**
	 * 玩家退出大厅
	 * 此方法对外开放，只在GamePlayer的setCurrenetHall内调用了
	 * @param player
	 *            玩家
	 */
	public void removeIdlePlayer(GamePlayer player) {
		synchronized (Players) {
			if(Players.contains(player)) {
				subHallPlayerCount();
				Players.remove(player);
			}
		}
	}

	
	
	
	public int getHallId() {
		return hallId;
	}

	public HallTypeInfo getHallType() {
		return hallType;
	}

	public void setHallType(HallTypeInfo hallType) {
		this.hallType = hallType;
	}
//	
//	/**
//	 * 获得大厅在线人数
//	 * @return
//	 */
//	public int getHallPlayerCount() {
//		if(null == Players) {
//			return 0;
//		}
//		return Players.size();
//	}
    
    /**
     * 增加大厅在线人数
     */
    private void addHallPlayerCount()
    {
    	hallPlayerCount.incrementAndGet();
    }

    /**
     * 减少大厅在线人数
     */
    private void subHallPlayerCount()
    {
    	hallPlayerCount.decrementAndGet();
    }
    
    /**
     * 获得大厅在线人数
     * 
     * @return 使用中的房间数量
     */
    public int getHallPlayerCount()
    {
        return hallPlayerCount.get();
    }
	
	/**
	 * 玩家是否可以呆在这个大厅，并且发送协议
	 * (查看玩家是否满足条件)
	 * @param gamePlayer
	 * @return 返回是否满足条件
	 */
	public boolean canStayHere(GamePlayer gamePlayer) {
		return canStayHere(gamePlayer, true);
	}
	
	/**
	 * 玩家是否可以呆在这个大厅
	 * 加上是否发送协议成功协议
	 * (查看玩家是否满足条件)
	 * @param gamePlayer
	 * @param sendResult 是否发送协议
	 * @return 返回是否满足条件
	 */
	public boolean canStayHere(GamePlayer gamePlayer, boolean sendResult) {
		if(null == hallType) {
			return false;
		}
		boolean canStay = true;
    	String msg = "";
    	
    	if(haveHallStage)
    	{
    		if(currentStage == null)
    			return false;
    	}
    	
    	
    	if(hallType.getHallType() == HallGameType.PENGYOU)
    		return true;
    	
    	

		
		int playerCharmValve = 0;
		if(hallType.getHallType() == HallGameType.SOCIAL)
		{
			playerCharmValve = gamePlayer.getPlayerInfo().getCoins();
			if(playerCharmValve < hallType.getLowestCoins())//如果已经开启了这个大厅 则金币大于底注就能玩游戏
	    	{
	    		canStay = false;
	    		msg = LanguageMgr.getTranslation("CityWar.EnterRoom.NoEnoughCoin");
	    	}
						
		}
		else
		{
			playerCharmValve = gamePlayer.getPlayerInfo().getCoins();
			if(playerCharmValve < hallType.getLowestCoins())//如果已经开启了这个大厅 则金币大于底注就能玩游戏
	    	{
	    		canStay = false;
	    		msg = LanguageMgr.getTranslation("CityWar.EnterHall.NoEnoughCoin");
	    	}
			
		}
		
			
//    	if(!canStay)
//    	{
//    		if(sendResult) {
//    			gamePlayer.getOut().sendRoomReturnMessage(false,msg);
//    			    			
//    			gamePlayer.setCurrenetHall(null);    			
//    			if(gamePlayer.getCurrentRoom() != null)
//    				gamePlayer.getCurrentRoom().removePlayer(gamePlayer);
//    			    			
//    		}
//    	}
    	
		return canStay;
	}
	
	/**
	 * 玩家进入大厅调用的方法
	 * 不发送协议（根据现在的客户端，可以不用发送）
	 * @param gamePlayer
	 */
	public boolean playerInHall(GamePlayer gamePlayer) {
		return playerInHall(gamePlayer, false);
	}
	/**
	 * 玩家进入大厅调用的方法
	 * 加上是否发送协议成功协议
	 * @param gamePlayer
	 * @param sendResult 是否发送协议
	 */
	public boolean playerInHall(GamePlayer gamePlayer, boolean sendResult) {
		boolean result = false; 
    	if(canStayHere(gamePlayer)) {//查看玩家是否满足条件  
        	addIdlePlayer(gamePlayer);
        	gamePlayer.setCurrenetHall(this);//设置玩家的当前大厅号
        	if(sendResult) {
            	gamePlayer.getOut().sendCannotStayHereOk();//进入大厅成功
        	}
        	result = true;
    	}
		return result;
	}
	/**
	 * 是否开启了这个大厅
	 * @param gameplay
	 * @return
	 * @version 2.2
	 */
	public int getOpenHallId(){
		
		int openHallId = 0;
		if(hallType.getHallType() == HallGameType.SOCIAL)
		{
			openHallId = -1;
		}
		else if(hallType.getHallType() == HallGameType.CONTEST && currentStage != null)
		{
			openHallId = currentStage.getStageId();
		}
		return openHallId;
//		if(gameplay.getPlayerInfo().getMaxHallId() >=  hallId )
//		{
//			return true;
//		}
//		return false;
	}
	/**
	 * 是否是否可以进入 或者 留着这个大厅游戏
	 * @param gameplay
	 * @return
	 * @version V2.2
	 */
	public boolean isStayHall(GamePlayer gameplay){
		
		PlayerInfo info = gameplay.getPlayerInfo();
		if(info == null){
			return false;
		}
		
		
//		if(hallType.getHallType() == HallGameType.CONTEST)
//		{
//			if(gameplay.getPlayerInfo().getCharmValve() >= hallType.getLowestCoins())//如果已经开启了这个大厅 则金币大于底注就能玩游戏
//			{
//				return true;
//			}
//		}
//		else
//		{
			if(gameplay.getPlayerInfo().getCoins() >= hallType.getLowestCoins())//如果已经开启了这个大厅 则金币大于底注就能玩游戏
			{
				return true;
			}
//		}
		

		return false;
	}
	
	/**
	 * 房间里的虚假机器人进入大厅
	 */
	public void roomRobInHall(GamePlayer gamePlayer){
		addIdlePlayer(gamePlayer);
	}

	public List<GamePlayer> getPlayers() {

        List<GamePlayer> list = new ArrayList<GamePlayer>();

        lock.readLock().lock();
        try
        {
            for (GamePlayer p : Players)
            {
                if (p == null || p.getPlayerInfo() == null)
                    continue;
                list.add(p);
            }
        }
        finally
        {
            lock.readLock().unlock();
        }
        return list;
	}
	
	
	
	public List<GamePlayer> getIdlePlayers() {

        List<GamePlayer> list = new ArrayList<GamePlayer>();

        lock.readLock().lock();
        try
        {
            for (GamePlayer p : idlePlayers)
            {
                if (p == null || p.getPlayerInfo() == null)
                    continue;
                list.add(p);
            }
        }
        finally
        {
            lock.readLock().unlock();
        }
        return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public int getStageCount()
	{
		return myStageList.size();
	}
	
	public void checkStage()
	{
		
		//test
//		if(myStageList.size() == 2)
//		{
//			currentStage = myStageList.get(0);
//			readyStage();
//			endStage();
//		}
		
		if(!haveHallStage)
			return;
		
		
		
 		synchronized(stageLock){
			
			Timestamp now = new Timestamp(System.currentTimeMillis());
			if(currentStage == null)
			{
				for(StageInfo stage : myStageList)
				{

					if(stage.getTimeType() == 1)
					{
						if(TimeUtil.isTimeBigInDay(now, stage.getReadyTime())
								&& TimeUtil.isTimeBigInDay(stage.getEndTime(), now))
						{
							StageState = 1;
							currentStage = stage;
							readyStage();
							break;
						}
					}
					else if(stage.getTimeType() == 2)
					{
						
					}
					
				}
			}
			else
			{
				if(currentStage.getTimeType() == 1)
				{
					if(TimeUtil.isTimeBigInDay(now, currentStage.getStartTime())
							&& TimeUtil.isTimeBigInDay(currentStage.getEndTime(), now) && StageState == 1)
					{
						StageState =2;
						startStage();
					}
//					else if(TimeUtil.isTimeBigInDay(now, currentStage.getEndTime()))
//					{			
//						StageState = 0;
//						endStage();
//						currentStage = null;
//						currentStageRound = null;
//					}		
				}
				else if(currentStage.getTimeType() == 2)
				{
					
				}
			}
			
//			if(currentStage != null)
//				System.out.println("checkStage=================================================" + currentStage.getStageId() + "===============" + StageState);
		}
	}
	

	private void readyStage()
	{
		
		UserTopMgr.clearStageTopList(currentStage.getStageId());
		
		
		List<StageRoundInfo> stageRoundList = StageMgr.findStageRound(currentStage.getStageId());
		if(stageRoundList == null)
			return;
		
		for(StageRoundInfo stageRound : stageRoundList)
		{
			if(currentStageRound == null || stageRound.getRoundIndex() > currentStageRound.getRoundIndex())
				currentStageRound = stageRound;			
		}	
		nextStageRound = StageMgr.getStageRound(currentStage.getStageId(), currentStageRound.getRoundIndex()-1);
				
		//机器人
		int robotCount = 5 + random.next(10);		
		addStageRobot(robotCount);
		
		System.out.println("readyStage=================================================" + currentStage.getStageId());
		
	}
	
	
	
	private void startStage()
	{	
		if(currentStageRound == null)
			return;
		
		System.out.println("startStage=================================================");
		
		finishedStageRoundCount = 0;
		checkStageRound(true);
	}
	
	private void endStage()
	{
		
		if(currentStageRound == null)
			return;
		
		System.out.println("endStage=================================================");
		
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		StageDataInfo stageData = new StageDataInfo();
		stageData.setStageId(currentStage.getStageId());
		stageData.setStageTitle("");
		stageData.setStageTime(now);
		
		stageData.setStageDataId(StageDataBussiness.addStageDataInfo(stageData));
		
		
		
		Map<Integer, StagePrizeInfo> prizeList = StageMgr.findStagePrize(currentStage.getStageId());
		List<PlayerInfo> topList = UserTopMgr.getStageTopList(currentStage.getStageId());
		
		List<StagePlayerInfo> userList = new ArrayList<StagePlayerInfo>();
		for(int i=1;i<=prizeList.size();i++)
		{
			StagePrizeInfo prize = prizeList.get(i);
			if(topList.size() >= prize.getIndex())
			{
				
				PlayerInfo tempPLayer = topList.get(prize.getIndex()-1);
				if(tempPLayer != null)
				{
					StagePlayerInfo stagePlayer = new StagePlayerInfo();
					stagePlayer.setStageDataId(stageData.getStageDataId());
					stagePlayer.setStagePrizeId(prize.getStagePrizeId());
					stagePlayer.setPlayerId(tempPLayer.getUserId());
					stagePlayer.setScore(tempPLayer.getCharmValve());
					stagePlayer.setPrize(prize.getPrizeName());
					stagePlayer.setIndex(prize.getIndex());
					stagePlayer.setLeagueDate(now);
					stagePlayer.setKKPrizeId(prize.getKKActivePrizeId());

					userList.add(stagePlayer);
					
					
					System.out.println("endStage============" + tempPLayer.getUserId() + "=========="+ tempPLayer.getCharmValve() + "=========="+ stagePlayer.getPrize());
				}
				
			}
		}
		
		
		for(StagePlayerInfo stagePlayer : userList)
		{
			StagePlayerBussiness.addStagePlayerInfo(stagePlayer);			
		}
		
		for(GamePlayer gp : currentStagePlayers.values())
		 {

			 GamePlayer gamePlayer = WorldMgr.getPlayerByID(gp.getUserId());					
			 if(null != gamePlayer) 
			 {												
				 int iIndex = UserTopMgr.getAllStageIndex(currentStage.getStageId(), gamePlayer.getUserId()) + 1;						
				 StagePrizeInfo prize = StageMgr.findStagePrizeByIndex(currentStage.getStageId(), iIndex);	
				 gamePlayer.getOut().sendStageOver(currentStage.getName(), iIndex, (prize == null ? "" : prize.getPrizeName()));
//				 if(prize != null)							
//					 gamePlayer.getOut().sendStageOver(currentStage.getName(), iIndex, prize.getPrizeName());					
			 }	 
		 }
		
		
//		RobotMgr.addRobotToWaitHall(hallId);		
		KKMgr.AddGameActive(currentStage.getKKActiveToken(), currentStage.getName(), currentStage.getDescript(), userList);
		
//		KKMgr.AddGameTopInfo(1, gamePlayer, hallType , newIndex, "");
				
		
	}
	


	public int checkStageRound(boolean isFirstRound)
	{

		if(currentStageRound == null)
			return 0;
		
		
//		System.out.println("");
//		System.out.println("");
		
		
		boolean isToNextRound = false;
		int roomCount = currentStageRound.getPlayerCount() / hallType.getPlayerCountMax();
		synchronized(stageRoundLock){
					
//			System.out.println("checkStageRound=============================check====================" + finishedStageRoundCount + "==============" + roomCount);
			if(finishedStageRoundCount >= roomCount)
			{
				isToNextRound = true;			
			}	
			else
			{
				
				Packet pkg = new Packet(UserCmdOutType.STAGE_WAIT);
				pkg.putInt(roomCount);
				pkg.putInt(finishedStageRoundCount);
				
				for(GamePlayer gp : currentStagePlayers.values())
				{
					if(gp.getCurrentRoom() == null)
						gp.getOut().sendTCP(pkg);
				}
				
				if(!isFirstRound)
				{
					for(BaseRoom room : allRooms.values())
					{
						
						 if(room.getStageGameIndex() < currentStageRound.getGameCount())
						 {
							 if(room.getPlayerCount() < hallType.getPlayerCountMin())
							 {
								 room.finishStageGame();				 
							 }
						 }					 
					}	
				}				
				
			}
			
			if(isToNextRound || finishedStageRoundCount >= roomCount)
			{
				currentStageRound = StageMgr.getStageRound(currentStage.getStageId(), currentStageRound.getRoundIndex()-1);
				nextStageRound = StageMgr.getStageRound(currentStage.getStageId(), currentStageRound.getRoundIndex()-1);
				roomCount = currentStageRound.getPlayerCount() / hallType.getPlayerCountMax();
				finishedStageRoundCount = 0;
				
//				System.out.println("checkStageRound=============================roundindex====================" + currentStageRound.getRoundIndex());
			}
		}
		
		
		
		if(currentStageRound.getRoundIndex() == 1)
		{
			
			endStage();
			
			StageState = 0;			
			currentStage = null;
			currentStageRound = null;
						
		}
		else if(isToNextRound || isFirstRound)
		{
			 
			if(isFirstRound)
			{
				
				while(currentStagePlayers.size() < currentStageRound.getPlayerCount())
				{
					int needCount = currentStageRound.getPlayerCount() - currentStagePlayers.size();				
					addStageRobot(needCount);
				}
				
				for(GamePlayer gp : currentStagePlayers.values())
				{
					playerInHall(gp);
//					boolean isIn = playerInHall(gp);
//					if(isIn == false)
//						System.out.println("checkStageRound=================================================playerInHall======zz=");
				}
			}
			 
			
			
			 
			//去掉不能进入下一轮的玩家
			 List<PlayerInfo> topList = UserTopMgr.getStageTopList(currentStage.getStageId());
			 Map<Integer, GamePlayer> tempStagePlayers = new HashMap<Integer, GamePlayer>();
			 for(int i = 0; i < currentStageRound.getPlayerCount();i++)
			 {
				 if(i<topList.size())
				 {
					 PlayerInfo ply = topList.get(i);
					 GamePlayer gp = currentStagePlayers.get(ply.getUserId());				 				 
					 tempStagePlayers.put(gp.getUserId(), gp);			 
				 }
			 }
			 
			 
			 //发送名次
			 for(GamePlayer gp : currentStagePlayers.values())
			 {
				 if(!tempStagePlayers.containsKey(gp.getUserId()))
				 {
					 GamePlayer gamePlayer = WorldMgr.getPlayerByID(gp.getUserId());
						if(null != gamePlayer && gamePlayer.getPlayerInfo().getIsRobot() == 0) {
							
							int iIndex = UserTopMgr.getAllStageIndex(currentStage.getStageId(), gamePlayer.getUserId()) + 1;
							StagePrizeInfo prize = StageMgr.findStagePrizeByIndex(currentStage.getStageId(), iIndex);															
							gamePlayer.getOut().sendStageOver(currentStage.getName(), iIndex, (prize == null ? "" : prize.getPrizeName()));
						}
				 }
				 else
				 {
					 int iIndex = currentStageRound.getPlayerCount();
					 if(!isFirstRound)
						 iIndex = UserTopMgr.getAllStageIndex(currentStage.getStageId(), gp.getUserId()) + 1;
					 gp.getOut().sendStageIndex(gp.getPlayerInfo().getCharmValve(), iIndex, nextStageRound.getPlayerCount());
				 }
			 }		 
				
			 currentStagePlayers = tempStagePlayers;			 		 
			 System.out.println("checkStageRound===============================remove============================"+ currentStagePlayers.size() + "==========" + roomCount );
			 
			 
			 
			 		 
			
			 
			 			 
			//重新安排房间开始下一轮游戏
			 int index = 0;
			 int count = 0;			
			 List<GamePlayer> tempPlayers = new ArrayList<GamePlayer>();
			 for(GamePlayer gp : currentStagePlayers.values())
				 tempPlayers.add(gp);
			 
			 
			 for(BaseRoom room : allRooms.values())
			 {
				 
				 room.setStageGame(currentStageRound.getRoundIndex(), currentStageRound.getRoundScore(), currentStageRound.getGameCount());
				 for(int j =0;j<hallType.getPlayerCountMax();j++)
				 {
					 count = tempPlayers.size();

					 index = count > 0 ? random.next(count) : 0;
					 GamePlayer gp = tempPlayers.get(index);
					 tempPlayers.remove(gp);
					 
					 
					 if(gp.getIsRobot())
					 {
						 Player ply = RobotMgr.getRobotByID(gp.getUserId());
						 RoomMgr.addAction(new RobotEnterRoomAction(room, ply));
					 }
					 else
					 {
						 RoomMgr.enterRoom(gp, room.getRoomId(), (byte)1, "", -1, null);
					 }
					 
					 System.out.println("checkStageRound===============================add room============================"+ gp.getUserId() + "==========" + room.getRoomId() );
				 }
			 }
		 }
		        
		return 0;
	}
	
	
	public void addStageRobot(int robotCount)
	{
//		System.out.println("addStageRobot===============================" + robotCount);
		HashMap<Integer, Player> robotList = new HashMap<Integer,Player>();

		int addCount = 0;
		while(addCount < robotCount)
		{
			Player player = RobotMgr.getRobot(RobotMgr.robotHallContestId);
			if(addStagePlayer(player.getPlayerDetail()))
			{
				robotList.put(player.getPlayerID(), player);
				addCount+=1;
			}
		}
		
		RobotMgr.addRobotToContestHall(hallId, robotList);		
//		UserTopMgr.addStageTopList(currentStage.getStageId(), robotList, currentStage.getDefaultScore());
	}
	
	public boolean addStagePlayer(GamePlayer gamePlayer)
	{
		boolean isAdd = false;
		if(currentStagePlayers.size() < currentStageRound.getPlayerCount() && currentStagePlayers.containsKey(gamePlayer.getUserId()) == false)
		{
			currentStagePlayers.put(gamePlayer.getUserId() , gamePlayer);
			gamePlayer.setCurrentStage(currentStage.getStageId());
			UserTopMgr.addStageTop(currentStage.getStageId(), gamePlayer.getPlayerInfo(), currentStage.getDefaultScore());
			isAdd = true;
		}
		
		return isAdd;
	}
	
	public boolean isInStagePlayer(GamePlayer gamePlayer)
	{
		boolean isIn = false;
		if(currentStagePlayers.containsKey(gamePlayer.getUserId()))
		{
			isIn = true;
		}
		
		return isIn;
	}
	
	public void addStageGameFinishRoom()
	{
		finishedStageRoundCount++;
//		checkStageRound(false);
	}
	
	public int getStageState()
	{
		//0   未开始
		//1   报名
		//2   开始游戏
		int stageState = 0;
		synchronized(stageLock){
			stageState = StageState;
		}
		return stageState;
	}
	
	public StageRoundInfo getCurrentStageRound()
	{
		return currentStageRound;
	}
	
	public StageRoundInfo getNextStageRound()
	{
		return nextStageRound;
	}
	
	public StageInfo getLastStage()
	{
				
		synchronized(stageLock){
			
			Timestamp now = new Timestamp(System.currentTimeMillis());
			if(currentStage != null)
			{
				return currentStage;
			}
			else
			{
				lastStage = null;
				for(StageInfo stage : myStageList)
				{
					if(lastStage == null)
					{
						lastStage = stage;
					}
					else if(TimeUtil.isTimeBigInDay(now, stage.getEndTime()) && TimeUtil.isTimeBigInDay(lastStage.getStartTime(), stage.getStartTime()))
					{						
						lastStage = stage;
					}
				}
			}
		}
        return lastStage;     
	}
	
	@SuppressWarnings("deprecation")
	public StageInfo getNextStage()
	{
				
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if(nextStage != null && TimeUtil.isTimeBigInDay(now,nextStage.getStartTime()))
		{
			return nextStage;
		}
		else
		{
			for(StageInfo stage : myStageList)
			{	
				if(nextStage == null)
				{
					nextStage = stage;
				}
				else if(TimeUtil.isTimeBigInDay(stage.getStartTime(), nextStage.getStartTime()))
				{
					int totalMinute = stage.getStartTime().getHours()*60+stage.getStartTime().getMinutes();
					int totalMinuteOld =  nextStage.getStartTime().getHours()*60+nextStage.getStartTime().getMinutes();
					
					if(totalMinute < totalMinuteOld)
						nextStage = stage;
				}
			}
		}
					
		
        return nextStage;     
	}
	
	public StageInfo getCurrentStage()
	{
		return currentStage;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public static final <T> T[] toArray(final Collection<T> collection, final Class<T> clazz) 
	{
		if (collection == null) {
			return null;
		}

		final T[] arr = (T[]) Array.newInstance(clazz, collection.size());
		return collection.toArray(arr);
	}
	
	
	
	
}
