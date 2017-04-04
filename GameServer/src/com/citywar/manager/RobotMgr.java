package com.citywar.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.ReferenceBussiness;
import com.citywar.bll.RobotBussiness;
import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserReferenceInfo;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.hall.BaseHall;
import com.citywar.util.ThreadSafeRandom;


/**
 * @author charles
 * @date 2012-1-5
 */
public final class RobotMgr
{
	
	
    private static int robotDutyChangeRate = 6 * 3;//八个小时换一拨
    private static int robotDutyChangeCount = Integer.MAX_VALUE - 1;//计数
    private static ThreadSafeRandom random = new ThreadSafeRandom();
    private static ReadWriteLock lock = new ReentrantReadWriteLock(false);   

    private static final Logger logger = Logger.getLogger(RobotMgr.class.getName());
        

    private static Map<Integer,List<Player>> allRobotPlayer = new HashMap<Integer, List<Player>>();
    private static Map<Integer,List<Player>> allOnDutyRobotPlayer = new HashMap<Integer, List<Player>>();
    private static Map<Integer, Player> allRobotPlayerMap = new HashMap<Integer, Player>();
    private static Map<Integer, Player> allOnDutyRobotPlayerMap = new HashMap<Integer, Player>();
    
    
    public static int robotHallContestId = 10000;
    
    /**
     * 初始化
     * 
     * @return
     */
    public static boolean init()
    {
        return reload();
    }

    /**
     * 加载概率数据
     * 
     * @return
     */
    public static boolean reload()
    {
        try
        {
//            Map<String, String> tempMap = new HashMap<String, String>();
            List<Player> tempRobots = new ArrayList<Player>();
            // tempMap = loadRobotProbilityInfo();
            tempRobots = loadRobotInfo();

            
            Map<Integer,List<Player>> RobotPlayerTemp = new HashMap<Integer, List<Player>>();
            List<BaseHall> hallListSocial  = HallMgr.getHallTypesList(1);
            List<BaseHall> hallListContest = HallMgr.getHallTypesList(2);
            for(BaseHall hall : hallListSocial)
            {
            	RobotPlayerTemp.put(hall.getHallId(), new ArrayList<Player>());   
            	allOnDutyRobotPlayer.put(hall.getHallId(), new ArrayList<Player>());
            }
            	     
            for(BaseHall hall : hallListContest)
            {
            	RobotPlayerTemp.put(hall.getHallId(), new ArrayList<Player>());
            	allOnDutyRobotPlayer.put(hall.getHallId(), new ArrayList<Player>());
            }
            RobotPlayerTemp.put(robotHallContestId, new ArrayList<Player>());
            allOnDutyRobotPlayer.put(robotHallContestId, new ArrayList<Player>());
            
    
				
            int[] percent = {30, 30, 30, 30, 30, 100};
            
            int rnd = 0;
            int flag = 1;
        	for(Player player : tempRobots)
        	{
        		
        		flag=random.next(10);        		
        		if(flag % 5 != 0)
        		{
        			for(int i = 0; i < hallListSocial.size()  ; i++)
                    {
        				rnd = random.next(100);
            			if(rnd < percent[i])
            			{
            				RobotPlayerTemp.get(hallListSocial.get(i).getHallId()).add(player);       
            				
            				int robotLevel = Math.round(4 + i/2) ;
            				player.setRobotLevel(robotLevel);
            				
//            				System.out.println("RobotMgr=====================================" + hallListSocial.get(i).getHallId());
            				break;
            			}
                    }
        		}
        		else
        		{
        			player.setRobotLevel(6);
            		RobotPlayerTemp.get(robotHallContestId).add(player);  		            			                    
        		}
        		
        	}
            
            
            
            lock.writeLock().lock();
            allRobotPlayer = RobotPlayerTemp;
            robotDutyChange(150);
            lock.writeLock().unlock();

            return true;
        }
        catch (Exception e)
        {
            logger.error("[ RobotMgr : load ]", e);
        }
    
       
        
        return false;
    }

    /**
     * 从DB中加载概率数据
     * 
     * @param map
     * @return
     */
    public static Map<String, String> loadRobotProbilityInfo()
    {
        return RobotBussiness.getRobotProbilityInfo();
    }

    /**
     * 初始化机器人信息
     * 
     * @return
     */
    public static List<Player> loadRobotInfo()
    {
        List<Player> robots = new ArrayList<Player>();
        GamePlayer gamePlayer = null;

        List<PlayerInfo> robotsInfo = RobotBussiness.getRobotPlayerInfo();

        if (null != robotsInfo && robotsInfo.size() > 0)
        {
            for (PlayerInfo info : robotsInfo)
            {
                // 初始化最大醉酒度这个逻辑字段
                info.setDrunkLevelLimit(LevelMgr.getDrunkLevel(info.getLevel()));
//                info.setLevel(LevelMgr.getLevel(info.getCharmValve()));
                
                
                // set the player as robot player.
                gamePlayer = new GamePlayer(info.getUserId());
                gamePlayer.setPlayerInfo(info);                
                gamePlayer.setIsRobot(true);
//                gamePlayer.setRobotCardId(info.getUDID());
                gamePlayer.setIsRobotSleep(false);
//                gamePlayer.initStagesInfo();
                
                
                
                // 初始化机器人奴隶主
                UserReferenceInfo ufinfo = ReferenceBussiness.getMasterInfoByUserId(info.getUserId());
                if (null != ufinfo && (ufinfo.getTakeUserId() == 0 && ufinfo.getRemoveTime() == null)) {//并且还被自己赢回来
                	gamePlayer.getPlayerReference().setMasterReferenceId(ufinfo.getId());
                	ReferenceMgr.setUserReference(ufinfo.getId(), ufinfo);//直接设置为主键
                }
                // 初始化机器人的礼品信息
                gamePlayer.getPlayerGift().loadUserGifts();
                gamePlayer.addCharmValve(0, 0);
                Player gp = new Player(gamePlayer, null);
                allRobotPlayerMap.put(info.getUserId(), gp);
                
                WorldMgr.addPlayer(info.getUserId(), gamePlayer);
                
                robots.add(gp);
            }
        }

        return robots;
    }
    
    /**
     *  获取所有的机器人中，用ID
     * @param playerID
     * @return
     */
    public static Player getRobotByID(int robotId)
    {
        Player player = null;
        lock.readLock().lock();
        player = allRobotPlayerMap.get(robotId);
        lock.readLock().unlock();
        return player;
    }

    /**
     * 获取在值班的机器人中，用ID
     * @param playerID
     * @return
     */
    public static Player getOnDutyRobotByID(int robotId)
    {
        Player player = null;
        synchronized (allOnDutyRobotPlayerMap)
        {
        	player = allOnDutyRobotPlayerMap.get(robotId);
        }
	    return player;
    }
    
    /**
     * 查看是否为机器人
     * @param playerID
     * @return
     */
    public static boolean isRobotByID(int robotId)
    {
        Player player = null;
	    lock.readLock().lock();
	    player = allRobotPlayerMap.get(robotId);
	    lock.readLock().unlock();
	    return null != player;
    }
    
    /**
     * 随机获取一个Robot
     * 
     * @param playerId
     * @return
     */
    public static Player getRobot(int hallId)
    {
        Player player = null;

        synchronized (allOnDutyRobotPlayer)
        {
        	
        	if(allOnDutyRobotPlayer.containsKey(hallId))
        	{        	
	        	int count = 50;
	            while (count > 0 && allOnDutyRobotPlayer.get(hallId).size() > 0)
	            {
	                int index=random.next(allOnDutyRobotPlayer.get(hallId).size());
	                player=allOnDutyRobotPlayer.get(hallId).get(index);
	                
	                if (player != null && !player.isPlaying() && !player.getPlayerDetail().getIsRobotSleep())
	                {
	//                    player.setIsPlaying(true);
	                    // robot的醉酒度为0时，随机分配醉酒度
	                	setRobotToNormal(player.getPlayerDetail(), hallId);
	                    //System.out.println("getRobot=========" + player.getPlayerDetail().getPlayerInfo().getUserName());
	                    break;
	                } else {
	                	player = null;
	                }
	                count--;
	            }
        	}
        }

        return player;
    }
    
    public static void addRobotToContestHall(int hallId, Map<Integer, Player> robotList)
    {
    	    
    	for(Player ply : robotList.values())
    	{
    		allOnDutyRobotPlayer.get(hallId).add(ply);
    		setRobotToNormal(ply.getPlayerDetail(), hallId);
    	}
//    	allOnDutyRobotPlayer.get(robotHallContestId).removeAll(robotList);
    	    	   	              
    }
    
    public static void addRobotToWaitHall(int hallId)
    {
    	    	
    	List<Player> robotList = allOnDutyRobotPlayer.get(hallId);
    	allOnDutyRobotPlayer.get(robotHallContestId).addAll(robotList);    	
    	allOnDutyRobotPlayer.get(hallId).clear();
    	
    }
    
    public static void setRobotToNormal(GamePlayer gamePlayer, int hallId) {
    	
    	
    	if(hallId == RobotMgr.robotHallContestId)
    		return;
    	
    	
    	HallTypeInfo hallTyppe = HallMgr.getHallById(hallId).getHallType();
    	PlayerInfo info = gamePlayer.getPlayerInfo();
    	
    	
    	int DrunkLimit = gamePlayer.getPlayerInfo().getDrunkLevelLimit();
        if (gamePlayer.getDrunkLevel() < DrunkLimit/3)
        {        	
        	gamePlayer.addDrunkLevel(random.next(0,DrunkLimit/5) + DrunkLimit/3);
        }

        
        if (info.getCoins() < hallTyppe.getLowestCoins())
        {
        	gamePlayer.addCoins(random.next(1, 5) * (hallTyppe.getLowestCoins() - info.getCoins()));
//        	info.setCoins(info.getCoins() + random.next(1, 5) * (hallTyppe.getLowestCoins() - info.getCoins()));
        }
        
        
        
//        if(hallTyppe.getHallType() == HallGameType.CONTEST)
//        {
//        	int charmValveMin = hallTyppe.getLowestCoins();
//            int charmValveMax = hallTyppe.getHighestCoins();       
//            
//            if(HallMgr.getHallById(hallId + 1) != null)
//            	charmValveMax = HallMgr.getHallById(hallId + 1).getHallType().getLowestCoins();
//            
//            
//            int changeArea = (charmValveMax - charmValveMin) / 10;
//            if (info.getCharmValve() < charmValveMin + charmValveMin / 3)
//            	gamePlayer.addCharmValve(charmValveMin / 3, 0);            
//            else if (info.getCharmValve() > charmValveMax - changeArea)
//            	gamePlayer.addCharmValve( - changeArea * 3, 0);
//        }

        
       
        
              
        
        
      
        
    }
//    /**
//     * 随机获取一个适合某类大厅的Robot
//     * 
//     * @param lowestCoins
//     * @return
//     */
//    public static Player getRobot(int hallId)
//    {
//    	int lowestCoins = HallMgr.getHallLowestCoins(hallId);
//        Player player = null;
//
//        synchronized (allOnDutyRobotPlayer)
//        {
//        	int count = 50;
//            while (count > 0 && allOnDutyRobotPlayer.size() > 0)
//            {
//                int index=random.next(allOnDutyRobotPlayer.size());
//                player=allOnDutyRobotPlayer.get(index);
//                
//                if (player != null && !player.isPlaying()
//                		&& player.getPlayerDetail().getPlayerInfo().getCoins() >= lowestCoins)
//                {
////                    player.setIsPlaying(true);
//                    // robot的醉酒度为0时，随机分配醉酒度
//                	setRobotToNormal(player.getPlayerDetail().getPlayerInfo());
//                    //System.out.println("getRobot===hallId======" + player.getPlayerDetail().getPlayerInfo().getUserName());
//                    break;
//                } else {
//                	player = null;
//                }
//                count--;
//            }
//        }
//
//        return player;
//    }

    /**
     * 获取所有机器人
     * 
     * @return 所有机器人列表
     */
    public static List<Player> getAllRobotPlayer()
    {
        List<Player> list = new LinkedList<Player>();
        try
        {
            lock.readLock().lock();
            for (List<Player> players : allRobotPlayer.values())
            {
            	for(Player player : players)
            		list.add(player);
            }
            return list;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 更新机器人的胜负场次信息到数据库中.在游戏的计时器RobotTimerTask中调用
     */
    public static int updateAllRobotPlayer()
    {
    	robotDutyChange(150);
    	
    	int saveCount = 0;
    	List<Player> robots = getAllRobotPlayer();
    	for(Player p : robots)
    	{
    		if(null != p)
    		{
    			int effectRow = RobotBussiness.updateRobotPlayer(p.getPlayerDetail().getPlayerInfo());
    			if(effectRow > 0)
    			{
    				saveCount += effectRow;
    			}
//    	        setOnDutyRobotPlayerToSuitableHall(p);//位置。是机器人分类
    		}
    	}
    	return saveCount;
    }
    /**
     * 查看机器人是否需要换班了，需要时候换班
     */
    private static void robotDutyChange(int robotDutycount)
    {
    	robotDutyChangeCount++;
    	if(robotDutyChangeCount >= robotDutyChangeRate) {
    		robotDutyChangeCount = 0;
    		
    		
    		
    		 Map<Integer,List<Player>> allRobotPlayerTemp = new HashMap<Integer, List<Player>>();
			if(null != allRobotPlayer && allRobotPlayer.size() > 0) {
	    		synchronized (allRobotPlayer)
	            {
	    			for(Integer hallId : allRobotPlayer.keySet()) {	    			
		    			for(Player robot : allRobotPlayer.get(hallId)) {
		    				
		    				if(!allRobotPlayerTemp.containsKey(hallId))
		    					allRobotPlayerTemp.put(hallId, new ArrayList<Player>());
		    				
		    				if(!allOnDutyRobotPlayer.containsKey(hallId) ||
		    						(allOnDutyRobotPlayer.containsKey(hallId) && !allOnDutyRobotPlayer.get(hallId).contains(robot)))
		    					allRobotPlayerTemp.get(hallId).add(robot);      	
		    				
		    				
//		    				System.out.println("robotDutyChange===========" + hallId + "=====" + robot.getPlayerDetail().getUserId() + "======" + robot.getPlayerDetail().getPlayerInfo().getCharmValve());
		    			}
	    			}
    			}
            }
			
			
			
			allOnDutyRobotPlayerMap.clear();			
			for(Integer hallId : allOnDutyRobotPlayer.keySet())			
				allOnDutyRobotPlayer.get(hallId).clear();
			
				
			for(Integer hallId : allRobotPlayerTemp.keySet()){

				
				int robotCount = robotDutycount;				
				if(robotCount > allRobotPlayerTemp.get(hallId).size() / 2)
					robotCount = allRobotPlayerTemp.get(hallId).size() / 2;

				
		    		if(allRobotPlayerTemp.get(hallId).size() >= robotCount) {
			    		
						while (allOnDutyRobotPlayer.get(hallId).size() < robotCount)
			            {
			                int index = random.next(allRobotPlayerTemp.get(hallId).size() - 1);
			                Player robot = allRobotPlayerTemp.get(hallId).remove(index);
			                robot.getPlayerDetail().setIsRobotSleep(false);
			                allOnDutyRobotPlayer.get(hallId).add(robot);
			                allOnDutyRobotPlayerMap.put(robot.getPlayerID(), robot);
			            }
					} else {
						allOnDutyRobotPlayer.put(hallId, allRobotPlayer.get(hallId));
						
						for(Player robot : allRobotPlayerTemp.get(hallId))
							allOnDutyRobotPlayerMap.put(hallId, robot);
					}
				
//		    		for(Player p : allOnDutyRobotPlayer.get(hallId))
//		    			System.out.println("robotDutyChange===========" + hallId + "=====" + p.getPlayerDetail().getUserId() + "======" + p.getPlayerDetail().getPlayerInfo().getCharmValve());
		    		
		    		System.out.println("robotDutyChange=========" + hallId + "==" + allOnDutyRobotPlayer.get(hallId).size());
			}
			
						
    	}
    }

	public static List<Player> getAllOnDutyRobotPlayer() {
		
		List<Player> list = new LinkedList<Player>();
        try
        {
            lock.readLock().lock();
            for (List<Player> players : allOnDutyRobotPlayer.values())
            {
            	for(Player player : players)
            		list.add(player);
            }
        }
        finally
        {
            lock.readLock().unlock();
        }
                
		return list;
	}

//	/**
//	 * 是机器人适合某类大厅，根据机器人的ID来分类的
//	 * 机器人金币、分类 还可以改善
//	 * @param robot
//	 */
//	public static void setOnDutyRobotPlayerToSuitableHall(Player robot) {
//		if(robot == null || robot.getPlayerDetail().getPlayerInfo() == null) {
//			return ;
//		}
//		int hallCoount = HallMgr.getHallCount();
//		int remainder = robot.getPlayerID() % hallCoount;
//		int lowestCoins = HallMgr.getHallLowestCoins(remainder + 1);
//		int Dvalue = lowestCoins - robot.getPlayerDetail().getPlayerInfo().getCoins();
//		if(Dvalue > 0) {
//			//这里可以搞一些算法，使之更加看不出来
//			robot.getPlayerDetail().addCoins(((Dvalue + lowestCoins/10) / 100) * 100);
//		}
//	}
}
