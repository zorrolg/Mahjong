package com.citywar.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.PlayerBussiness;
import com.citywar.bll.ReferenceBussiness;
import com.citywar.bll.UserDayActivityBussiness;
import com.citywar.dice.entity.AroundUser;
import com.citywar.dice.entity.AroundUserComparator;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.PlayerSlaveCount;
import com.citywar.dice.entity.UserDayActivityInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameutil.PlayerInfoAndAround;
import com.citywar.util.DistanceUtil;
import com.citywar.util.GeohashUtil;
import com.citywar.util.TimeUtil;

/**
 * @author shanfeng.cao
 * @date 2012-09-11
 */
public final class AllAroundPlayerInfoMgr
{
    private static Logger logger = Logger.getLogger(AllAroundPlayerInfoMgr.class.getName());

    /**
     * 存储用户信息 key：用户ID value：用户信息
     */
    private static HashMap<Integer, PlayerInfo> allPlayers = new HashMap<Integer, PlayerInfo>();

    private static ReadWriteLock lock = new ReentrantReadWriteLock(false);

    private static ReadWriteLock aroundLock = new ReentrantReadWriteLock(false);

    /**
     * 存储用户信息 key：用户经纬度代表的hash值 value：用户信息
     */
    private static TreeMap<String, AroundUser> allAroundPlayers = new TreeMap<String, AroundUser>();
    /**
     * 最长离线时间
     */
    private static final int MAX_DAY_COUNT = 7;
    /**
     * 前后各取多少人
     */
    private static int SIZE = 50;
    /**
     * 每页的翻倍数
     */
    private static int PAGE_MULTIPLE = 2;
    /**
     * 每页的数量
     */
    private static int PAGE_SIZE = 50;
    /**
     * 最大循环数
     * （最大数量 = 最大循环数 * 每页的数量）
     */
    private static int LOOP_COUNT = 100;

    /**
     * 替换掉 很重要
     * @param playerID
     * @param player
     * @return
     */
    public static boolean addPlayer(int playerID, PlayerInfo player)
    {
    	if(null == player ) {//|| player.getIsRobot() != 0
    		return false;
    	}
    	PlayerInfo oldInfo = getPlayerInfoById(playerID);
        lock.writeLock().lock(); 
        try
        {
    		if(null != oldInfo && null != player.getLastLoginDate()
    				&& null !=oldInfo.getLastLoginDate()
    				&& ! oldInfo.getLastLoginDate().before(player.getLastLoginDate())) {
	        	return false;//我们需要保存最新的
	        }
//    		player.setLevel(LevelMgr.getLevel(player.getCharmValve()));
	        PlayerInfo info = allPlayers.put(playerID, player);//替换掉
	        if(info != null && info.getSlaveCount() != 0 && player.getSlaveCount() == 0) {
	        	player.setSlaveCount(info.getSlaveCount());
	        }
        } catch (Exception e) {
            logger.error("[ AllAroundPlayerInfoMgr : addPlayer ]", e);
        } finally {
            lock.writeLock().unlock();
        }
        return true;
    }

    public static boolean init()
    {
        boolean result = false;
        try
        {
            allPlayers.clear();
            allAroundPlayers.clear();
            List<PlayerInfo> players = PlayerBussiness.slecetOffLineOverTimePlayer();
            for(PlayerInfo info : players) {
            	int userId = info.getUserId();
            	if(info.getLastLoginDate() != null && (info.getLastQiutDate() == null 
            			|| info.getLastLoginDate().after(info.getLastQiutDate()))) {
            		info.setLastQiutDate(new Timestamp(System.currentTimeMillis()));
                }
            	info.setYesterdayCoins(info.getCoins());
            	info.setLevel(LevelMgr.getLevel(info));
            	allPlayers.put(userId, info);
            	addAroundPlayers(info);
            }
            initSlaveCountOnDB();
            initDayIncreaseCoinsOnDB();
            result = true;
        }
        catch (Exception e)
        {
            logger.error("AllAroundPlayerInfoMgr Init", e);
        }
        return result;
    }

    /**
     * 初始化离线玩家奴隶数
     */
    private static void initSlaveCountOnDB() {
    	List<PlayerSlaveCount> slaveList = ReferenceBussiness.getUseReferencesCount();
    	for(PlayerSlaveCount info : slaveList) {
			PlayerInfo player = allPlayers.get(info.getUserId());
			if(null != player) {
				player.setSlaveCount(info.getSlaveCount());
			}
    	}
    }

    /**
     * 初始化离线玩家奴当日赚得的金币数
     */
    private static void initDayIncreaseCoinsOnDB() {
    	List<UserDayActivityInfo> list = UserDayActivityBussiness.selectAllUserDayActivity();
    	for(UserDayActivityInfo info : list) {
			PlayerInfo player = allPlayers.get(info.getUserId());
			if(null != player) {
				player.setYesterdayCoins(player.getCoins() - info.getIncreaseCoins());
			}
    	}
    }

    /**
     * 
     */
    public static void removeAll()
    {
        if (allPlayers != null)
        {
            allPlayers.clear();
        }
    }


    /**
     * 用户登录时，通过GeoHash算法得到用户坐标对应的字符串,并以此为key存入全局Map中
     * 
     * @param playerId
     */
    public static void addAroundPlayers(PlayerInfo player)
    {
    	if(null == player ) {    //|| player.getIsRobot() != 0
    		return ;
    	}
        try
        {
        	aroundLock.writeLock().lock();
            String currentUserPos = player.getPos();
            double[] posArray = DistanceUtil.getLatAndLon(currentUserPos);

            /**
             * 取得hash算法所得的字符串
             */
            GeohashUtil geohash = new GeohashUtil();
            String hashStr = geohash.encode(posArray[0], posArray[1]);
            if (null == hashStr || "".equals(hashStr))
            {
                return;
            }

            AroundUser user = new AroundUser();
            user.setUserId(player.getUserId());
            user.setX(posArray[0]);
            user.setY(posArray[1]);

            // 以hash值+用户ID作为key以避免用户坐标完全相同导致hash值一致.
            allAroundPlayers.put(hashStr + user.getUserId(), user);
//            if(!allAroundPlayers.containsKey(hashStr + user.getUserId()))
//            {
//            	allAroundPlayers.put(hashStr + user.getUserId(), user);            
//            	//System.out.println("addAroundPlayers===1====" + user.getUserId());
//            }
        }
        catch (Exception e)
        {
            logger.error("[ AllAroundPlayerInfoMgr : addAroundPlayers ]", e);
        }
        finally
        {
        	aroundLock.writeLock().unlock();
        }
    }
    
    public static List<AroundUser> getAroundPlayers(PlayerInfo player,int size)
    {
    	SIZE = 50;
    	SIZE = Math.max(SIZE, size*2);
    	return getAroundPlayers(player);
    }
    
    /**
     * 获取当前用户附近用户信息
     * 
     * @param playerId
     */
    public static List<AroundUser> getAroundPlayers(PlayerInfo player)
    {
        List<AroundUser> aroundUserInfo = new LinkedList<AroundUser>();
        try
        {
            String currentUserPos = player.getPos();
            double[] currentUserPosArray = DistanceUtil.getLatAndLon(currentUserPos);
            
            // 取得hash算法所得的字符串
            GeohashUtil geohash = new GeohashUtil();
            String hashStr = geohash.encode(currentUserPosArray[0],
                                            currentUserPosArray[1]);

            // 坐标hash值+用户ID的形式作为key
            hashStr += player.getUserId();

            if (null == hashStr || "".equals(hashStr))
            {
                return null;
            }
            aroundUserInfo = getRequiredAroundPlayers(hashStr,
                                                      currentUserPosArray[0],
                                                      currentUserPosArray[1]);
        }
        catch (Exception e)
        {
            logger.error("[ AllAroundPlayerInfoMgr : getAroundPlayers ]", e);
        }

        return sortAndSublist(aroundUserInfo);
    }

    /**
     * 取满足条件的附近用户，前后各取25人
     * 
     * @param hash
     *            当前用户hash
     * @param x
     *            当前用户纬度
     * @param y
     *            当前用户经度
     * @return
     */
    private static List<AroundUser> getRequiredAroundPlayers(String hash,
            double x, double y)
    {
        List<AroundUser> headerAroundUserInfo = new LinkedList<AroundUser>();
        List<AroundUser> tailAroundUserInfo = new LinkedList<AroundUser>();

        // 标记取前后用户时数目是否足够
        boolean notEnough = false;
        int notEnoughCount = 0;
        int recordCount = 0;

        // 取当前用户前SIZE个用户，如果不够，需要标记
        headerAroundUserInfo = getHeadAroundPlayers(hash, x, y,
                                                    headerAroundUserInfo,
                                                    notEnough, notEnoughCount);

        recordCount = headerAroundUserInfo.size();
        if (recordCount < SIZE)
        {
            notEnough = true;
            notEnoughCount = SIZE - recordCount;
        }

        // 取当前用户后SIZE个用户
        tailAroundUserInfo = getTailAroundPlayers(hash, x, y,
                                                  tailAroundUserInfo,
                                                  notEnough, notEnoughCount);

        // 如果总数不够，需要再取一次
        recordCount += tailAroundUserInfo.size();
        if (recordCount < 2 * SIZE)
        {
            notEnough = true;
            notEnoughCount = 2 * SIZE - recordCount;
            headerAroundUserInfo.clear();
            headerAroundUserInfo = getHeadAroundPlayers(hash, x, y,
                                                        headerAroundUserInfo,
                                                        notEnough,
                                                        notEnoughCount);
        }

        headerAroundUserInfo.addAll(tailAroundUserInfo);

        return headerAroundUserInfo;
    }

    /**
     * 取当前用户前25个附近用户
     * 
     * @param hash
     *            当前用户的hash值
     * @param x
     *            当前用户纬度
     * @param y
     *            当前用户经度
     * @param list
     *            存储结果的list
     * @param notEnough
     *            上次取的是否数目不够
     * @param notEnoughCount
     *            不够的数目
     * @return
     */
    private static List<AroundUser> getHeadAroundPlayers(String hash, double x,
            double y, List<AroundUser> users, boolean notEnough,
            int notEnoughCount)
    {
        Map<String, AroundUser> headMap = allAroundPlayers.headMap(hash);

        // 取前SIZE个用户
        List<Map.Entry<String, AroundUser>> headList = new LinkedList<Map.Entry<String, AroundUser>>(
                headMap.entrySet());
        int size = headList.size();
        if (null != headList && size > 0)
        {
            int needSize = notEnough ? SIZE + notEnoughCount : SIZE;
            int endIndex = size > needSize ? size - (1 + needSize) : -1;

            AroundUser u = null;
            AroundUser au = null;
            for (int i = size - 1; i > endIndex; i--)
            {
                Map.Entry<String, AroundUser> e = headList.get(i);
                u = e.getValue();
                if((int)(u.getX()) == 0 && (int)(u.getY()) == 0)
                {
                	continue;
                }
                double distance = DistanceUtil.distanceByLatLon(x, y, u.getX(),
                                                                u.getY());
                au = new AroundUser(u);
                au.setUserDistance(distance);
                users.add(au);
            }
        }

        return users;
    }

    /**
     * 取当前用户后25个附近用户
     * 
     * @param hash
     *            当前用户hash值
     * @param x
     *            当前用户纬度
     * @param y
     *            当前用户经度
     * @param list
     *            存储结果的list
     * @param notEnough
     *            上次取的是否数目不够
     * @param notEnoughCount
     *            不够的数目
     * @return
     */
    private static List<AroundUser> getTailAroundPlayers(String hash, double x,
            double y, List<AroundUser> users, boolean notEnough,
            int notEnoughCount)
    {
        Map<String, AroundUser> tailMap = allAroundPlayers.tailMap(hash, false);

        if (null != tailMap && tailMap.size() > 0)
        {
            // 取后SIZE个用户
            int count = 0;
            AroundUser u = null;
            AroundUser au = null;
            for (Map.Entry<String, AroundUser> e : tailMap.entrySet())
            {
                u = e.getValue();
                
                if((int)(u.getX()) == 0 && (int)(u.getY()) == 0)
                {
                	continue;
                }
                
                double distance = DistanceUtil.distanceByLatLon(x, y, u.getX(),
                                                                u.getY());
                au = new AroundUser(u);
                au.setUserDistance(distance);
                users.add(au);
                count++;

                if (count == (notEnough ? (SIZE + notEnoughCount) : SIZE))
                {
                    break;
                }
            }
        }

        return users;
    }

    /**
     * 根据距离对50个附近用户排序
     * 
     * @param users
     * @return
     */
//    @SuppressWarnings("unchecked")
    private static List<AroundUser> sortAndSublist(List<AroundUser> users)
    {
        if (null != users && users.size() > 1)
        {
            Collections.sort(users, new AroundUserComparator());
        }
        return users;
    }

    /**
     * 退出时移除附近用户列表中的当前用户信息
     * 
     * @param playerId
     */
    public static void removeAroundUserInfo(PlayerInfo player)
    {
        GeohashUtil util = new GeohashUtil();
        double[] posArray = DistanceUtil.getLatAndLon(player.getPos());
        String hash = util.encode(posArray[0], posArray[1]) + player.getUserId();
        try
        {
        	aroundLock.writeLock().lock();
            if (allAroundPlayers.containsKey(hash))
            {
                allAroundPlayers.remove(hash);
            }
        }
        catch (Exception e)
        {
            logger.error("[ AllAroundPlayerInfoMgr : removeAroundUserInfo ]", e);
        }
        finally
        {
        	aroundLock.writeLock().unlock();
        }
    }

    /**
     * 更新用户的位置信息
     * 
     * @param playerId
     */
    public static String updatePlayerLocationInfo(PlayerInfo player, double x,
            double y)
    {
        String hashKey = "";
        try
        {
            lock.writeLock().lock();
            if (allPlayers.containsValue(player))
            {
                String currentUserPos = player.getPos();
                double[] posArray = DistanceUtil.getLatAndLon(currentUserPos);
                GeohashUtil geohash = new GeohashUtil();
                hashKey = geohash.encode(posArray[0], posArray[1]) + player.getUserId();
                String pos = new String(x + "," + y);

                // 更新allPlayers中的位置信息
                player.setPos(pos);
            }
        }
        finally
        {
            lock.writeLock().unlock();
        }
        return hashKey;
    }

    /**
     * 更新附近玩家列表（allAroundPlayers）中的值
     * 
     * @param hashKey
     */
    public static void updateAroundPlayerLocationInfo(String hashKey,
            int playerId, double x, double y)
    {
        try
        {
        	aroundLock.writeLock().lock();
            if (allAroundPlayers.containsKey(hashKey))
            {
            	if((int)x == 0 && (int)y == 0)
            	{
	                allAroundPlayers.remove(hashKey);
            	}
            	else
            	{
	                // 计算新位置产生的新hash值
	                GeohashUtil geohash = new GeohashUtil();
	                String newHashKey = geohash.encode(x, y) + playerId;
	                
	                AroundUser u = new AroundUser();
	                u.setUserId(playerId);
	                u.setX(x);
	                u.setY(y);
	
	                allAroundPlayers.remove(hashKey);
	                allAroundPlayers.put(newHashKey, u);
	                
	                //System.out.println("addAroundPlayers===2====" + playerId);
            	}
            }
        }
        finally
        {
        	aroundLock.writeLock().unlock();
        }
    }

    /**
     * 获得用户在某一个距离
     * 
     * @param hashKey
     */
    public static List<AroundUser> getAroundPlayersByDistance(PlayerInfo player, double minDistance, double maxDistance,int size)
    {
    	PAGE_SIZE = 50;
    	PAGE_SIZE = Math.max(PAGE_SIZE, size*2);
    	return getAroundPlayersByDistance(player, minDistance, maxDistance);
    }

    /**
     * 获得用户在某一个距离范围内的玩家
     * 
     * @param hashKey
     */
    public static List<AroundUser> getAroundPlayersByDistance(PlayerInfo player, double minDistance, double maxDistance) {
        List<AroundUser> aroundPlayers = new LinkedList<AroundUser>();
    	try
        {
            long startTick = System.currentTimeMillis();
            int oldprio = Thread.currentThread().getPriority();
            Thread.currentThread().setPriority(1);
        	int countPlayerSize = PAGE_SIZE;
        	int loopCount = 0;
        	int allAroundPlayerSize = allAroundPlayers.size();
        	boolean isFindAll = false;
        	while (loopCount < LOOP_COUNT && countPlayerSize < allAroundPlayerSize) {
        		countPlayerSize *= PAGE_MULTIPLE;
        		List<AroundUser> tempAroundPlayers = getAroundPlayers(player, countPlayerSize);
        		aroundPlayers.clear();
            	for(AroundUser aroundPlayer : tempAroundPlayers) {
            		if(aroundPlayer.getUserDistance() < minDistance) {
            			continue ;
            		}
                	if(aroundPlayer.getUserDistance() <= maxDistance) {
                		if(! aroundPlayers.contains(aroundPlayer)) {
                			aroundPlayers.add(aroundPlayer);
                		}
            		} else {
            			isFindAll = true;
            			break ;
            		}
            	}
            	if(isFindAll) {
            		break ;
            	}
            	loopCount++;
    		}
            Thread.currentThread().setPriority(oldprio);
            long executeTime = System.currentTimeMillis() - startTick;
            //System.out.println("getAroundPlayersByDistance use " + executeTime + " ms");
            logger.debug(String.format("getAroundPlayersByDistance use %d ms", executeTime));
        }
        catch (Exception e1)
        {
        	logger.error("getAroundPlayersByDistance", e1);
        }
		return aroundPlayers;
    }
    
    /**
     * 除去相同的得到新的
     * @param isHavePlayer
     * @param allAroundPlayer
     * @return
     */
    public static List<AroundUser> getAroundPlayers(List<AroundUser> isHavePlayer, List<AroundUser> allAroundPlayer) {
    	List<AroundUser> nextPagePlayer = new LinkedList<AroundUser>();
    	for(AroundUser aroundPlayer : allAroundPlayer) {
    		if( ! isHavePlayer.contains(aroundPlayer)) {
    			nextPagePlayer.add(aroundPlayer);
    			nextPagePlayer.add(aroundPlayer);
    		}
    	}
		return nextPagePlayer;
    }
    
    /**
     * 根据位置获得玩家信息列表
     * @param aroundUsers
     * @param player 
     * @return
     */
    public static List<PlayerInfoAndAround> getPlayerInfoAndArounds(GamePlayer player, List<AroundUser> aroundUsers) {
    	List<PlayerInfoAndAround> nextPagePlayer = new LinkedList<PlayerInfoAndAround>();
    	try {
    		lock.readLock().lock();
    		PlayerInfoAndAround playerInfoAndAround = null;
	    	for(AroundUser aroundUser : aroundUsers) {
	    		if(allPlayers.containsKey(aroundUser.getUserId())) {
	    			playerInfoAndAround = new PlayerInfoAndAround();
	    			playerInfoAndAround.setUserId(aroundUser.getUserId());
	    			playerInfoAndAround.setAroudPlayer(allPlayers.get(aroundUser.getUserId()));
	    			playerInfoAndAround.setAroudUser(aroundUser);
	            	nextPagePlayer.add(playerInfoAndAround);
	            	player.getPlayerAroudUser().addAroadUserPlayer(playerInfoAndAround);
	            } else {
	            	//System.out.println("根据getPlayerInfos获得玩家信息列表 竟然没有招到玩家信息");
	            }
	    	}
    	} finally {
            lock.readLock().unlock();
        }
		return nextPagePlayer;
    }
    
    /**
     * 根据aroundUserId获得玩家信息列表
     * @param aroundUsers
     * @return
     */
    public static List<PlayerInfo> getPlayerInfosByUserId(List<Integer> aroundUserIds) {
    	List<PlayerInfo> nextPagePlayer = new LinkedList<PlayerInfo>();
    	try {
    		lock.readLock().lock();
	    	for(Integer aroundUserId : aroundUserIds) {
	    		if(allPlayers.containsKey(aroundUserId)) {
	            	nextPagePlayer.add(allPlayers.get(aroundUserId));
	            } else {
	            	//System.out.println("根据aroundUserId获得玩家信息列表 竟然没有招到玩家信息");
	            }
	    	}
    	} finally {
            lock.readLock().unlock();
        }
		return nextPagePlayer;
    }
    
    /**
     * 根据playerInfoAndArounds获得玩家信息列表
     * @param playerInfoAndArounds
     * @return
     */
    public static List<PlayerInfo> getPlayerInfosByaroundUsers(List<PlayerInfoAndAround> playerInfoAndArounds) {
    	List<PlayerInfo> nextPagePlayer = new LinkedList<PlayerInfo>();
    	for(PlayerInfoAndAround playerInfoAndAround : playerInfoAndArounds) {
    		if(null != playerInfoAndAround.getAroudPlayer()) {
            	nextPagePlayer.add(playerInfoAndAround.getAroudPlayer());
            }
    	}
		return nextPagePlayer;
    }
    
    /**
     * 移除离线超时的玩家
     * @param aroundUsers
     * @return
     */
    public static void clearOffLineOverTimePlayer() {
		Timestamp today = new Timestamp(System.currentTimeMillis());
		Timestamp lastLoginDate = null;
		Calendar lastLoginDateCalendar = Calendar.getInstance();
		Calendar todayCalendar = Calendar.getInstance();
		todayCalendar.setTime(today);
		List<PlayerInfo> allPlayerList = getAllPlayers();
		List<PlayerInfo> removePlayerList = new ArrayList<PlayerInfo>();
    	for(PlayerInfo playerInfo : allPlayerList) {
    		lastLoginDate = playerInfo.getLastLoginDate();
    		if(null != lastLoginDate && playerInfo.isOnline() == false) {
    			lastLoginDateCalendar.setTime(lastLoginDate);
    			int compareDay = TimeUtil.dateCompare(lastLoginDateCalendar, todayCalendar);
        		if(compareDay > MAX_DAY_COUNT) {
        			removePlayerList.add(playerInfo);
        		}
    		}
    	}
		try {
    		lock.writeLock().lock();
        	for(PlayerInfo playerInfo : removePlayerList) {
        		allPlayers.remove(playerInfo.getUserId());
        		removeAroundUserInfo(playerInfo);
        	}
    	} finally {
            lock.writeLock().unlock();
        }
		
		
//		//机器人发卡
//		try {
//    		lock.writeLock().lock();
//        	for(PlayerInfo playerInfo : allPlayers.values()) {
//
//        		if(playerInfo.getIsRobot() == 1)
//        		{
//        			ItemBussiness.addUserItem(new UserItemInfo(2,PropType.CARD_GRAB, playerInfo.getUserId(), 80));
//                    ItemBussiness.addUserItem(new UserItemInfo(2,PropType.CARD_COIN, playerInfo.getUserId(), 80));
//                    ItemBussiness.addUserItem(new UserItemInfo(2,PropType.CARD_DRUNK, playerInfo.getUserId(), 80));
//                    ItemBussiness.addUserItem(new UserItemInfo(2,PropType.CARD_DEFEND, playerInfo.getUserId(), 80));
//        		}
//        		
//        		//System.out.println("clearOffLineOverTimePlayer==============" + playerInfo.getUserName());
//        		
//        	}
//    	} finally {
//            lock.writeLock().unlock();
//        }
		
		
		
    }

	public static PlayerInfo getPlayerInfoById(int userId) {
		PlayerInfo playerInfo = null;
		try {
    		lock.readLock().lock();
    		if(allPlayers.containsKey(userId)) {
    			playerInfo = allPlayers.get(userId);
    		}
    	} finally {
            lock.readLock().unlock();
        }
		return playerInfo;
	}

    /**
     * 得到所有玩家属性信息
     * 
     * @return
     */
    public static List<PlayerInfo> getAllPlayers()
    {
        List<PlayerInfo> list = new ArrayList<PlayerInfo>();
        lock.readLock().lock();
        try
        {
            for (PlayerInfo p : allPlayers.values())
            {
                if (p == null)
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

    /**
     * 得到所有玩家属性和位置信息
     * 
     * @return
     */
	public static List<PlayerInfoAndAround> getAllPlayerInfoAndAround() {
        List<PlayerInfoAndAround> list = new ArrayList<PlayerInfoAndAround>();
        lock.readLock().lock();
        try
        {
        	PlayerInfoAndAround infoPA = null;
            for (PlayerInfo p : allPlayers.values())
            {
                if (p == null)
                    continue;
                infoPA = new PlayerInfoAndAround();
                infoPA.setAroudPlayer(p);
                infoPA.setUserId(p.getUserId());
                list.add(infoPA);
            }
        }
        finally
        {
            lock.readLock().unlock();
        }
        return list;
	}

    /**
     * 清除所有玩家今日在线属性
     * 
     * @return
     */
    public static void clearAllPlayerIncreaseCoins()
    {
        lock.readLock().lock();
        try {
            for (PlayerInfo p : allPlayers.values()) {
                if (p != null) {
                	p.setYesterdayCoins(p.getCoins());
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}
