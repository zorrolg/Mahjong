package com.citywar.manager;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.PlayerBussiness;
import com.citywar.bll.UserDayActivityBussiness;
import com.citywar.dice.entity.AroundUser;
import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.StageInfo;
import com.citywar.dice.entity.UserDayActivityInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameutil.GamePlayerUtil;
import com.citywar.gameutil.PlayerAroudUser;
import com.citywar.gameutil.PlayerInfoAndAround;
import com.citywar.socket.Packet;
import com.citywar.type.UserTopType;
import com.citywar.util.HeadPicUtil;


/**
 * 世界排行榜
 * 
 * @author zhiyun.peng
 * 
 */
public class UserTopMgr {
	
	
	private static Logger logger = Logger.getLogger(UserTopMgr.class.getName());
	
	
	public static Map<UserTopType, List<PlayerInfo>> Word_Map;								//各种类型的全部排名
	
	public static Map<Integer, List<PlayerInfo>> Stage_List;								//50个用户的排名
	public static Map<Integer, Map<Integer, PlayerInfo>> Stage_Map;							//50个用户的排名
	public static Map<Integer, LinkedHashMap<Integer, String>> Stage_List_Top;				//每个stage前三的头像
    
	
    private static ReadWriteLock lock = new ReentrantReadWriteLock(false);
    private static ReadWriteLock lockStage = new ReentrantReadWriteLock(false);
//    private static ReadWriteLock lockStageAll = new ReentrantReadWriteLock(false);
	
    
    
	public static int DISTANCE_100km = 100000;
	public static int DISTANCE_1000Km = 100000;
	public static int TOP_NUMBER = 300;
	public static int TOP_NUMBER_STAGE = 50;
	public static int PAGE_SIZE = 20;
	/**
	 * 
	 * 从数据库中 查询 每种分类排行榜数据
	 * @return
	 */
	public static boolean init()  
	{
		
		Word_Map = new HashMap<UserTopType, List<PlayerInfo>>();

		Stage_List = new HashMap<Integer, List<PlayerInfo>>();
		Stage_Map = new HashMap<Integer, Map<Integer, PlayerInfo>>();
		Stage_List_Top = new LinkedHashMap<Integer, LinkedHashMap<Integer, String>>();
		
		UserTopType key = UserTopType.INCREASECOINS;//从数据库中取出今日财富榜
		List<UserDayActivityInfo> result = UserDayActivityBussiness.getCharts(key.getWhere(), key.getOrderField(), key.getTopCount());
    	for(UserDayActivityInfo info : result) {
			PlayerInfo player = GamePlayerUtil.getPlayerInfo(info.getUserId(), true);
			if(null != player && player.getIsRobot() == 0) {
				if(player.getLastQiutDate() == null) {
					player.setLastQiutDate(new Timestamp(System.currentTimeMillis()));
					PlayerBussiness.updateAll(player.getUserId(), player);//保存一下，则只是在第一次启动的时候会执行
				}
				AllAroundPlayerInfoMgr.addPlayer(player.getUserId(), player);
				AllAroundPlayerInfoMgr.addAroundPlayers(player);
				player.setYesterdayCoins(player.getCoins() - info.getIncreaseCoins());
			}
    	}
		return reload();
	}
	
	/**
     * 重新载入排行榜信息
     * 
     * @return
     */
    public static boolean reload()
    {
    	
    	if(null == Word_Map) {
    		return false;
    	}
    	Map<UserTopType, List<PlayerInfo>> tempWord_Map = new HashMap<UserTopType, List<PlayerInfo>>();
    	
    	HashMap<Integer, List<PlayerInfo>> tempStage_List = new HashMap<Integer, List<PlayerInfo>>();
    	HashMap<Integer, Map<Integer,PlayerInfo>> tempStage_Map = new HashMap<Integer, Map<Integer,PlayerInfo>>();    	
    	LinkedHashMap<Integer, LinkedHashMap<Integer, String>> temp_Stage_List_Top = new LinkedHashMap<Integer, LinkedHashMap<Integer, String>>();
		for (UserTopType key:  UserTopType.values()) 
		{
			List<PlayerInfo> saveResult = new LinkedList<PlayerInfo>();
			if(key == UserTopType.INCREASECOINS) 
			{//当日赚得的金币数
				saveResult = AllAroundPlayerInfoMgr.getAllPlayers();
				key.sort(saveResult);
			}
			else 
			{//其它的排行榜只需要一个SQL
				List<PlayerInfo> result = PlayerBussiness.getCharts(key.getWhere(), key.getOrderField(), key.getTopCount());
				for (PlayerInfo info: result) 
				{
					if(info.getLastQiutDate() == null) {
						info.setLastQiutDate(new Timestamp(System.currentTimeMillis()));
					}
					info.setYesterdayCoins(info.getCoins());//设置昨天的金币数
					AllAroundPlayerInfoMgr.addPlayer(info.getUserId(), info);
					AllAroundPlayerInfoMgr.addAroundPlayers(info);
					info.setLevel(LevelMgr.getLevel(info));
					saveResult.add(info);
				}
			}
			tempWord_Map.put(key, saveResult);
		}
		
		lock.writeLock().lock(); 
        try
        {
        	Word_Map = tempWord_Map;
        	
        } catch (Exception e) {
            logger.error("[ UserTopMgr : reload ]", e);
        } finally {
            lock.writeLock().unlock();
        }
        
        
//        List<PlayerInfo> StageTop = PlayerBussiness.getCharmValvesTopByStage();
//		for (StageInfo stage:  StageMgr.getAllStage().values()) 
//		{
//			
//			List<PlayerInfo> nowstageList = new LinkedList<PlayerInfo>();
//			Map<Integer,PlayerInfo> nowstageMap = new HashMap<Integer,PlayerInfo>();
//			LinkedHashMap<Integer, String> nowStageTop = new LinkedHashMap<Integer, String>();
//			for(PlayerInfo info : StageTop)
//			{
//				if(info.getStageId() == stage.getStageId())
//				{
//					nowstageList.add(info);
//					nowstageMap.put(info.getUserId(), info);
//					
//					if(nowStageTop.size() < 3)
//						nowStageTop.put(info.getUserId(),info.getPicPath());
//				}
//			}
//			
//			tempStage_List.put(stage.getStageId(), nowstageList);
//			tempStage_Map.put(stage.getStageId(), nowstageMap);
//			temp_Stage_List_Top.put(stage.getStageId(), nowStageTop);
//		}

		lockStage.writeLock().lock(); 
        try
        {
        	Stage_List = tempStage_List;
        	Stage_Map = tempStage_Map;
        	Stage_List_Top = temp_Stage_List_Top;
        	
        } catch (Exception e) {
            logger.error("[ UserTopMgr : reload ]", e);
        } finally {
        	lockStage.writeLock().unlock();
        }
        
        
            	
		return true;
    }
    

	/**
     * 重新载入今日财富排行榜信息
     * 为了及时，而且效率要高（排行榜不能时时刻刻都计算，但是频率又要高一些）
     * @return
     */
    public static void reloadIncreasecoins() {
    	lock.writeLock().lock(); 
        try
        {
//        	UserTopType key = UserTopType.INCREASECOINS;
//        	List<PlayerInfo> saveResult = AllAroundPlayerInfoMgr.getAllPlayers();
//        	key.sort(saveResult);
//        	Word_Map.put(key, saveResult);
        } catch (Exception e) {
            logger.error("[ UserTopMgr : reloadIncreasecoins ]", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
	
	/**
	 * 
	 * 返回指定类型 的世界排行榜的数据
	 * @param player 
	 */
	public static List<PlayerInfoAndAround> getWordChartsByType(GamePlayer player, UserTopType type,int Level) {
		List<PlayerInfoAndAround> resultPA = new LinkedList<PlayerInfoAndAround>();
		List<PlayerInfo> result = null;
		result = Word_Map.get(type);
		PlayerInfoAndAround infoPA = null;
		for(PlayerInfo info : result) {
			infoPA = player.getPlayerAroudUser().getAroadUserPlayer(info);
			
			if(Level == 0)
				resultPA.add(infoPA);
			else if(infoPA.getAroudPlayer().getLevel() == Level)
				resultPA.add(infoPA);
				
		}
		return resultPA;
	}
	
	/**
	 * 从玩家的附近玩家中查出来
	 * @param player
	 * @param type
	 * @param maxDistance
	 * @return
	 */
	public static List<PlayerInfoAndAround> getNearChartsByType(GamePlayer player,UserTopType type,double maxDistance)
	{
		if(null == player || null == player.getPlayerInfo()) {
			return null;
		}
		List<PlayerInfoAndAround> result = null;
		PlayerAroudUser playerAroudUser = player.getPlayerAroudUser();
		if(null == playerAroudUser) {
			List<AroundUser> list = AllAroundPlayerInfoMgr.getAroundPlayersByDistance(player.getPlayerInfo(), 0, maxDistance);
			result = AllAroundPlayerInfoMgr.getPlayerInfoAndArounds(player, list);
		} else {
			result = playerAroudUser.getNearPlayerInfoAndAround(maxDistance);
		}
		type.sortPA(result);
		return result;
	}
	
	/**
	 * 取得玩家在集合中的
	 * @param topChartsPlayer
	 * @param player
	 * @return
	 */
	public static int getUserChartsOnList(List<PlayerInfoAndAround> topChartsPlayer, GamePlayer player) {
		if(null == topChartsPlayer || null == player) {
			return 0;
		}
    	int selfRanked = 0;
        for(int i = 0;i < topChartsPlayer.size(); i++) {
        	PlayerInfoAndAround currentData = topChartsPlayer.get(i);
        	if(currentData.getAroudPlayer().equals(player.getPlayerInfo())) {
        		selfRanked = i+1;
        		//System.out.println(UserTopType.CHARM+"玩家"+player.getPlayerInfo().getUserName()+"的排名为"+selfRanked);
        		break ;
        	}
        }
		return selfRanked;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 取得玩家在集合中的
	 * @param topChartsPlayer
	 * @param player
	 * @return
	 */
	public static void checkLevelTop(boolean isWin, GamePlayer gamePlayer, HallTypeInfo hallType, int stage,int stageCharmValve) {

		
		PlayerInfo player = gamePlayer.getPlayerInfo();
		if(stage == 0)
			return;
		
		

		
		
        
				
        lockStage.writeLock().lock(); 
        List<PlayerInfo> nowStageList = Stage_List.get(stage);
        Map<Integer,PlayerInfo> nowStageMap = Stage_Map.get(stage);
                
               
        
        if(nowStageList.size() == 0
//        		|| stageCharmValve >= nowStageList.get(nowStageList.size() - 1).getCharmValve()
        		|| nowStageMap.containsKey(player.getUserId()))
        {


            try
            {
            	int index = TOP_NUMBER_STAGE;
            	if(nowStageMap.containsKey(player.getUserId()))
            	{
            		
            		
            		PlayerInfo playerNow = nowStageMap.get(player.getUserId());
            		index = nowStageList.indexOf(playerNow);
            		playerNow.setCharmValve(playerNow.getCharmValve() + stageCharmValve);
            		
//            		System.out.println("checkLevelTop=====================" + stageCharmValve + "=======" + playerNow.getCharmValve());
            		
            		if(index > nowStageList.size() - 1)
            			index = nowStageList.size() - 1;
            			
            		
            		int newIndex = index;            		
            		if(isWin)
            		{
    	        		while(newIndex >= 1 && playerNow.getCharmValve() > nowStageList.get(newIndex - 1).getCharmValve())        		
    	        			newIndex = newIndex - 1;
            		}
            		else
            		{
            			while(newIndex <= TOP_NUMBER_STAGE && newIndex + 1 < nowStageList.size()
            					&& playerNow.getCharmValve() < nowStageList.get(newIndex + 1).getCharmValve())        		
    	        			newIndex = newIndex + 1;
            		}
            		
            		if(newIndex != index)
            		{
            			nowStageList.remove(index);
            			nowStageList.add(newIndex,playerNow);
            		}
            		
            	}
            	else
            	{            
            		System.out.println("stageCharmValve===============================================================================" + gamePlayer.getPlayerInfo().getUserId() + "======" + gamePlayer.getPlayerInfo().getCharmValve());
            	}
            	
            	
            } catch (Exception e) {
                logger.error("[ UserTopMgr : reload ]", e);
            } finally {
            	lockStage.writeLock().unlock();
            }
        }
        
        
        
        
    	return ;//isLevelChangeBefore || isLevelChangeNow || 
	
	}
	
	
	
	
	
	
	
	
	
	public static List<PlayerInfoAndAround> getStageTopList(GamePlayer player, int stage) {

		
		List<PlayerInfoAndAround> resultPA = new LinkedList<PlayerInfoAndAround>();
		List<PlayerInfo> result = null;
		
		lockStage.readLock().lock(); 	
		
		int count = Stage_List.get(stage).size() > TOP_NUMBER_STAGE ? TOP_NUMBER_STAGE : Stage_List.get(stage).size();
		result = Stage_List.get(stage).subList(0, count);
		PlayerInfoAndAround infoPA = null;
		for(PlayerInfo info : result) {
			infoPA = player.getPlayerAroudUser().getAroadUserPlayer(info);
			resultPA.add(infoPA);
		}
		lockStage.readLock().unlock();
			
		
		return resultPA;
	}

	
	
	
	
	
	public static int getAllStageIndex(int stage,PlayerInfo player)
	{
		
		int index = 0;		
		List<PlayerInfo> nowStageTop = null;
        
		
        lockStage.readLock().lock(); 
        
        nowStageTop = Stage_List.get(stage);        
        if(nowStageTop != null)
        	index = nowStageTop.indexOf(player);
        
        lockStage.readLock().unlock();

        
		return index;
		
	}
	
	public static int getAllStageIndex(int stage,int playerId)
	{
		
		int index = 0;		
		List<PlayerInfo> nowStageTop = null;
        
		
        lockStage.readLock().lock(); 
        
        if(Stage_Map.get(stage).containsKey(playerId))
        {
	        PlayerInfo player = Stage_Map.get(stage).get(playerId);
	        nowStageTop = Stage_List.get(stage);        
	        if(nowStageTop != null)
	        	index = nowStageTop.indexOf(player);
        }
        lockStage.readLock().unlock();
        
		return index;
		
	}
	
	public static int getStageScore(int stage,int playerId)
	{
		
		int score = 0;		
      		
        lockStage.readLock().lock(); 
        
        if(Stage_Map.get(stage).containsKey(playerId))
        {
        	PlayerInfo player = Stage_Map.get(stage).get(playerId);
        	score = player.getCharmValve();
        }
        lockStage.readLock().unlock();
        
		return score;
		
	}
	
	public static boolean isStageEnroll(int stage,int playerId)
	{
		
		boolean isEnroll = false;		
      		
        lockStage.readLock().lock(); 
        
        if(Stage_Map.get(stage).containsKey(playerId))
        {
        	isEnroll = true;
        }
        lockStage.readLock().unlock();
        
		return isEnroll;
		
	}
	
	
	
	public static void getAllStageHead(Packet packet)
	{
		lockStage.readLock().lock(); 
		
		
		packet.putInt(StageMgr.getAllStage().values().size());
		for(StageInfo stage : StageMgr.getAllStage().values())
		{

			packet.putInt(stage.getStageId());
			packet.putStr(stage.getCity());
			packet.putStr(stage.getPic());
			
			LinkedHashMap<Integer, String> list =  Stage_List_Top.get(stage.getStageId());				
			if(list == null)
			{
				packet.putInt(0);
			}
			else
			{
				packet.putInt(list.size());
				for(Integer key : list.keySet())
					packet.putStr(HeadPicUtil.getRealSmallPicPath(key,list.get(key)));
			}
			
		}
		lockStage.readLock().unlock();
        
		return;
		
	}
	
	public static List<PlayerInfo> getStageTopList(int stageId)
	{
		
		List<PlayerInfo> nowStageTop = null;
        
        lockStage.readLock().lock(); 
        nowStageTop = Stage_List.get(stageId);
        
        lockStage.readLock().unlock();
              
		
		return nowStageTop;
	}
	
	
	
	
	
	public static void clearStageTopList(int stageId)
	{
		       
        lockStage.readLock().lock();           	
    	
    	Stage_List.get(stageId).clear();
    	Stage_Map.get(stageId).clear();
    	        
        lockStage.readLock().unlock();
              		
		return;
	}
	
	
//	public static void addStageTopList(int stageId, Map<Integer, Player> listPlayer, int score)
//	{
//		       
//        lockStage.readLock().lock();           	
//    	
//        for(Player player : listPlayer.values())
//        {
//        	player.getPlayerDetail().getPlayerInfo().setCharmValve(score);
//	    	Stage_List.get(stageId).add(player.getPlayerDetail().getPlayerInfo());
//	    	Stage_Map.get(stageId).put(player.getPlayerID(), player.getPlayerDetail().getPlayerInfo());
//	    	
//        }
//        
//        lockStage.readLock().unlock();
//              		
//		return;
//	}
	
	
	public static void addStageTop(int stageId, PlayerInfo playerInfo, int score)
	{
		       
        lockStage.readLock().lock();           	
    	
        playerInfo.setCharmValve(score);
	    Stage_List.get(stageId).add(playerInfo);
	    Stage_Map.get(stageId).put(playerInfo.getUserId(), playerInfo);
	    	                
        lockStage.readLock().unlock();
              		
		return;
	}
	
	
}
