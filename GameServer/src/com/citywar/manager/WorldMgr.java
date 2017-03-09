package com.citywar.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.dice.entity.AroundUser;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.util.DistanceUtil;
import com.citywar.util.GeohashUtil;

/**
 * @author Dream
 * @date 2011-4-25
 */
public final class WorldMgr
{
    private static Logger logger = Logger.getLogger(WorldMgr.class.getName());

    private static HashMap<Integer, GamePlayer> allPlayers = new HashMap<Integer, GamePlayer>();
    /**
     * 正在World中的玩家数量
     */
    private static AtomicInteger worldPlayerCount;

    private static ReadWriteLock lock = new ReentrantReadWriteLock(false);

    /**
     * 存储用户信息 key：用户经纬度代表的hash值 value：用户信息
     */
    private static TreeMap<String, AroundUser> allAroundPlayers = new TreeMap<String, AroundUser>();
    /**
     * 前后各取多少人
     */
//    private  static int SIZE = 50;

    public static boolean addPlayer(int playerID, GamePlayer player)
    {
    	boolean isAdd = false;
        lock.writeLock().lock();
        try
        {
	        if (!allPlayers.containsKey(playerID))
	        {
	            allPlayers.put(playerID, player);
	            addWorldPlayerCount();
	            isAdd= true;
	        }
        } catch (Exception e) {
            logger.error("[ WorldMgr : addPlayer ]", e);
        } finally {
            lock.writeLock().unlock();
        }
        if(isAdd) {
        	AllAroundPlayerInfoMgr.addPlayer(playerID, player.getPlayerInfo());
			AllAroundPlayerInfoMgr.addAroundPlayers(player.getPlayerInfo());
        }

        return true;
    }
    
    /**
     * 玩家下线，退出Word方法
     * 玩家还要退出当前大厅，让大厅好统计在线人数
     * @param playerID
     * @return
     */
    public static boolean removePlayer(int playerID)
    {
        boolean result = false;
        GamePlayer player = null;
        lock.writeLock().lock();
        try
        {
            if (allPlayers.containsKey(playerID))
            {
                player = allPlayers.remove(playerID);
                //玩家还要退出当前大厅，让大厅好统计在线人数
                if(null != player) {
                	subWorldPlayerCount();
                	if(null != player.getCurrentHall()) {
            			player.setCurrenetHall(null);
                    }
                }
                	
                result = true;
            }
        }
        catch (Exception e)
        {
            logger.error("[ WorldMgr : removePlayer ]", e);
        }
        finally
        {
            lock.writeLock().unlock();
        }

        if (player != null)
        {
            // GameServer.getInstance().getCenterServerProxy().sendUserOffline(playerID,
            // player.getPlayerInfo().getConsortiaId(),
            // player.getPlayerInfo().getExtendedInfo().getIsVip(),
            // player.getPlayerInfo().getExtendedInfo().getViplevel());
            return true;
        }

        // player.save();
        return result;
    }

    public static boolean init()
    {
        boolean result = false;
        try
        {
            allPlayers.clear();
            worldPlayerCount = new AtomicInteger(0);
            result = true;
        }
        catch (Exception e)
        {
            logger.error("WordMgr Init", e);
        }
        return result;
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
     * 得到所有玩家信息
     * 
     * @return
     */
    public static List<GamePlayer> getAllPlayers()
    {
        List<GamePlayer> list = new ArrayList<GamePlayer>();

        lock.readLock().lock();
        try
        {
            for (GamePlayer p : allPlayers.values())
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
    
    /**
     * 增加World在线人数
     */
    private static void addWorldPlayerCount()
    {
    	worldPlayerCount.incrementAndGet();
    }

    /**
     * 减少World在线人数
     */
    private static void subWorldPlayerCount()
    {
    	worldPlayerCount.decrementAndGet();
    }
    
    /**
     * 获得World在线人数
     * 
     * @return 使用中的房间数量
     */
    public static int getWorldPlayerCount()
    {
        return worldPlayerCount.get();
    }

    public static List<GamePlayer> getAllPlayersNoGame()
    {
        List<GamePlayer> list = new LinkedList<GamePlayer>();
        List<GamePlayer> allPlayers = getAllPlayers();

        for (GamePlayer p : allPlayers)
        {
            if (p.getCurrentRoom() == null)
                list.add(p);
        }

        return list;
    }

    /**
     * @param pkg
     */
    public static void sendToAll(Packet pkg)
    {
        List<GamePlayer> players = getAllPlayers();
        for (GamePlayer p : players)
        {
            p.getOut().sendTCP(pkg);
        }
    }

    
    /**
     * @param pkg
     */
    public static void sendSystemTrumpet(String msg)
    {
    	
    	Packet response = new Packet(UserCmdOutType.USE_TRUMPET);  
    	
        response.putByte((byte)0);//验证结果
        response.putInt(4);   //优先级
        response.putInt(200000);   //发送者的ID
        response.putStr(""); //发送者的名称
        response.putStr(msg);//喇叭内容       
                
        sendToAll(response);
                
    }
    
    
    /**
     * @param msg
     * @param m_player
     * @param normalsysnoticewithrevolving
     * @return
     */
//    public static Packet sendSysNotice(String msg, GamePlayer player)
//    {
//        return null;
//    }
    

    /**
     * 用户下线,遍历所有用户,有用户是其好友，则通知其状态改变
     * 
     * @param playerid
     * @param consortiaID
     * @param isVIP
     * @param vIPLevel
     */
    public static void onPlayerOffline(int playerid, int consortiaID,
            boolean isVIP, int vIPLevel)
    {
        ChangePlayerState(playerid, 0, consortiaID, isVIP, vIPLevel);
    }

    /**
     * @param playerid
     * @param i
     * @param consortiaID
     * @param isVIP
     * @param vIPLevel
     */
    public static void ChangePlayerState(int playerID, int state,
            int consortiaID, boolean isVIP, int VIPLevel)
    {
        Packet pkg = null;
        List<GamePlayer> list = getAllPlayers();
        for (GamePlayer client : list)
        {
            if ((client.getFriends() != null
                    && client.getFriends().containsKey(playerID) && client.getFriends().containsKey(playerID)))
            {
                if (pkg == null)
                {
                    pkg = client.getOut().sendFriendState(playerID, state,
                                                          isVIP, VIPLevel);
                }
                else
                {
                    client.getOut().sendTCP(pkg);
                }
            }
        }

    }

    /**
     * @param playerid
     * @param consortiaId
     * @param areaID
     * @param b
     * @param isVip
     * @param viplevel
     */
//    public static void onPlayerOnline(int playerid, int consortiaId,
//            int areaID, boolean isSend, boolean isVip, int viplevel)
//    {
//        ChangePlayerState(playerid, 1, consortiaId, isVip, viplevel);
//        if (isSend)
//        {
//
//        }
//    }

    /**
     * 用户登录时，通过GeoHash算法得到用户坐标对应的字符串,并以此为key存入全局Map中
     * 
     * @param playerId
     */
    public static void addAroundPlayers(int playerId)
    {
        try
        {
        	lock.writeLock().lock();
            if (null == allPlayers.get(playerId))
            {
                return;
            }
            PlayerInfo playerInfo = allPlayers.get(playerId).getPlayerInfo();
            String currentUserPos = playerInfo.getPos();
            //TODO 测试添加坐标
            //currentUserPos = "50,50";
            double[] posArray = DistanceUtil.getLatAndLon(currentUserPos);
            
            // 如果该用户的坐标为[0,0],表示他为pc用户或者设置了位置隐私的用户，可以不往allAroundPlayers里面存值
//            if((int)posArray[0] == 0 && (int)posArray[1] == 0)
//            {
//            	logger.error("[AroundUser] PC or Position Private user, no need add to around user map: " 
//            			+ allPlayers.get(playerId).getPlayerInfo().getUserName() + " "
//            			+ posArray[0] + " - "
//            			+ posArray[1]);
//            	return;
//            }

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
            user.setUserId(playerId);
            user.setX(posArray[0]);
            user.setY(posArray[1]);

            // 以hash值+用户ID作为key以避免用户坐标完全相同导致hash值一致.
            allAroundPlayers.put(hashStr + user.getUserId(), user);
        }
        catch (Exception e)
        {
            logger.error("[ WorldMgr : addAroundPlayers ]", e);
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }
    
//    public static List<AroundUser> getAroundPlayers(int playerId,int size)
//    {
//    	SIZE = 50;
//    	if(size*2 < SIZE)
//    		SIZE = Math.max(SIZE, size*2);
//    	
//    	return getAroundPlayers(playerId);
//    }

//	public static List<AroundUser> getUserAroudAll(int playerId) {
//
//		if (allPlayers.get(playerId) == null) {
//			return null;
//		}
//
//		String currentUserPos = allPlayers.get(playerId).getPlayerInfo().getPos();
//		double[] currentUserPosArray = DistanceUtil.getLatAndLon(currentUserPos);
//
//		List<AroundUser> result = new ArrayList<AroundUser>();
//		Collection<AroundUser> values = allAroundPlayers.values();
//		for (AroundUser current : values) {
//			if ((int) (current.getX()) == 0 && (int) (current.getY()) == 0) 
//			{
//				continue;
//			}
//			if(current.getUserId() == playerId){
//				continue;
//			}
//			double distance = DistanceUtil.distanceByLatLon(currentUserPosArray[0], currentUserPosArray[1],current.getX(), current.getY());
//			current.setUserDistance(distance);
//			result.add(current);
//		}
//		return sortAndSublist(result);
//	}
    /**
     * 获取当前用户附近用户信息
     * 
     * @param playerId
     */
//    public static List<AroundUser> getAroundPlayers(int playerId)
//    {
//        List<AroundUser> aroundUserInfo = new ArrayList<AroundUser>();
//        if (null == allPlayers.get(playerId))
//        {
//            return null;
//        }
//
//        try
//        {
//            String currentUserPos = allPlayers.get(playerId).getPlayerInfo().getPos();
//            //TODO 测试添加坐标
//            //currentUserPos = "50,50";
//            double[] currentUserPosArray = DistanceUtil.getLatAndLon(currentUserPos);
//            
//            // 如果自己的坐标为（0,0），表示该用户不愿意公开自己的位置信息，这样该用户也看不到自己附近的用户，直接返回空列表。
////            if((int)(currentUserPosArray[0]) == 0 && (int)(currentUserPosArray[1]) == 0)
////            {
////            	logger.error("[AroundUser] PC or Position Private user, not show around users : " 
////            			+ allPlayers.get(playerId).getPlayerInfo().getUserName());
////            	return null;
////            }
//            
//            // 取得hash算法所得的字符串
//            GeohashUtil geohash = new GeohashUtil();
//            String hashStr = geohash.encode(currentUserPosArray[0],
//                                            currentUserPosArray[1]);
//
//            // 坐标hash值+用户ID的形式作为key
//            hashStr += playerId;
//
//            if (null == hashStr || "".equals(hashStr))
//            {
//                return null;
//            }
//            aroundUserInfo = getRequiredAroundPlayers(hashStr,
//                                                      currentUserPosArray[0],
//                                                      currentUserPosArray[1]);
//        }
//        catch (Exception e)
//        {
//            logger.error("[ WorldMgr : getAroundPlayers ]", e);
//        }
//
//        return sortAndSublist(aroundUserInfo);
//    }
    
    /**
     * (测试方法) 用户登录时，通过GeoHash算法得到用户坐标对应的字符串,并以此为key存入全局Map中
     * @param playerId
     */
//    public static void addAroundPlayersTest(int playerId)
//    {
//        try
//        {
//            //System.out.println("allAroundPlayers size -> " + allAroundPlayers.size());
//            allAroundPlayers.clear();
//            
//            playerId = 111;
//            double[] posArray = new double[]{50.0000, 50.0000};
//            
//            /**
//             * 取得hash算法所得的字符串
//             */
//            GeohashUtil geohash = new GeohashUtil();
//            for(int i = playerId; i < playerId + 5000; i++)
//            {
//            	AroundUser user = new AroundUser();
//            	user.setUserId(i);
//            	
//            	double pos1 = posArray[0] + 0.0001*(i-playerId);
//            	double pos2 = posArray[1] + 0.0001*(i-playerId);
//            	user.setX(pos1);
//            	user.setY(pos2);
//            	
//            	String hashStr = geohash.encode(pos1, pos2);
//                if(null == hashStr || "".equals(hashStr))
//                {
//                    break;
//                }
//            	allAroundPlayers.put(hashStr + user.getUserId(), user);
//            }
//            
//            for(int i = playerId+5000; i < playerId + 10000; i++)
//            {
//                int count = 1;
//            	AroundUser user = new AroundUser();
//            	user.setUserId(i);
//            	
//            	double pos1 = 49.5 + 0.0001*count;
//            	double pos2 = 49.5 + 0.0001*count;
//            	user.setX(pos1);
//            	user.setY(pos2);
//            	
//            	String hashStr = geohash.encode(pos1, pos2);
//                if(null == hashStr || "".equals(hashStr))
//                {
//                    break;
//                }
//            	allAroundPlayers.put(hashStr + user.getUserId(), user);
//            	count++;
//            }
//            
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//        }
//    }

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
//    private static List<AroundUser> getRequiredAroundPlayers(String hash,
//            double x, double y)
//    {
//        List<AroundUser> headerAroundUserInfo = new ArrayList<AroundUser>();
//        List<AroundUser> tailAroundUserInfo = new ArrayList<AroundUser>();
//
//        // 标记取前后用户时数目是否足够
//        boolean notEnough = false;
//        int notEnoughCount = 0;
//        int recordCount = 0;
//
//        // 取当前用户前SIZE个用户，如果不够，需要标记
//        headerAroundUserInfo = getHeadAroundPlayers(hash, x, y,
//                                                    headerAroundUserInfo,
//                                                    notEnough, notEnoughCount);
//
//        recordCount = headerAroundUserInfo.size();
//        if (recordCount < SIZE)
//        {
//            notEnough = true;
//            notEnoughCount = SIZE - recordCount;
//        }
//
//        // 取当前用户后SIZE个用户
//        tailAroundUserInfo = getTailAroundPlayers(hash, x, y,
//                                                  tailAroundUserInfo,
//                                                  notEnough, notEnoughCount);
//
//        // 如果总数不够，需要再取一次
//        recordCount += tailAroundUserInfo.size();
//        if (recordCount < 2 * SIZE)
//        {
//            notEnough = true;
//            notEnoughCount = 2 * SIZE - recordCount;
//            headerAroundUserInfo.clear();
//            headerAroundUserInfo = getHeadAroundPlayers(hash, x, y,
//                                                        headerAroundUserInfo,
//                                                        notEnough,
//                                                        notEnoughCount);
//        }
//
//        headerAroundUserInfo.addAll(tailAroundUserInfo);
//
//        return headerAroundUserInfo;
//    }

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
//    private static List<AroundUser> getHeadAroundPlayers(String hash, double x,
//            double y, List<AroundUser> users, boolean notEnough,
//            int notEnoughCount)
//    {
//        Map<String, AroundUser> headMap = allAroundPlayers.headMap(hash);
//
//        // 取前SIZE个用户
//        List<Map.Entry<String, AroundUser>> headList = new ArrayList<Map.Entry<String, AroundUser>>(
//                headMap.entrySet());
//        int size = headList.size();
//        if (null != headList && size > 0)
//        {
//            int needSize = notEnough ? SIZE + notEnoughCount : SIZE;
//            int endIndex = size > needSize ? size - (1 + needSize) : -1;
//
//            AroundUser u = null;
//            AroundUser au = null;
//            for (int i = size - 1; i > endIndex; i--)
//            {
//                Map.Entry<String, AroundUser> e = headList.get(i);
//                u = e.getValue();
//                if((int)(u.getX()) == 0 && (int)(u.getY()) == 0)
//                {
//                	continue;
//                }
//                double distance = DistanceUtil.distanceByLatLon(x, y, u.getX(),
//                                                                u.getY());
//                au = new AroundUser(u);
//                au.setUserDistance(distance);
//                users.add(au);
//            }
//        }
//
//        return users;
//    }
//
//    /**
//     * 取当前用户后25个附近用户
//     * 
//     * @param hash
//     *            当前用户hash值
//     * @param x
//     *            当前用户纬度
//     * @param y
//     *            当前用户经度
//     * @param list
//     *            存储结果的list
//     * @param notEnough
//     *            上次取的是否数目不够
//     * @param notEnoughCount
//     *            不够的数目
//     * @return
//     */
//    private static List<AroundUser> getTailAroundPlayers(String hash, double x,
//            double y, List<AroundUser> users, boolean notEnough,
//            int notEnoughCount)
//    {
//        Map<String, AroundUser> tailMap = allAroundPlayers.tailMap(hash, false);
//
//        if (null != tailMap && tailMap.size() > 0)
//        {
//            // 取后SIZE个用户
//            int count = 0;
//            AroundUser u = null;
//            AroundUser au = null;
//            for (Map.Entry<String, AroundUser> e : tailMap.entrySet())
//            {
//                u = e.getValue();
//                
//                if((int)(u.getX()) == 0 && (int)(u.getY()) == 0)
//                {
//                	continue;
//                }
//                
//                double distance = DistanceUtil.distanceByLatLon(x, y, u.getX(),
//                                                                u.getY());
//                au = new AroundUser(u);
//                au.setUserDistance(distance);
//                users.add(au);
//                count++;
//
//                if (count == (notEnough ? (SIZE + notEnoughCount) : SIZE))
//                {
//                    break;
//                }
//            }
//        }
//
//        return users;
//    }

    /**
     * 根据距离对50个附近用户排序
     * 
     * @param users
     * @return
     */
//    @SuppressWarnings("unchecked")
//    private static List<AroundUser> sortAndSublist(List<AroundUser> users)
//    {
//        if (null != users && users.size() > 1)
//        {
//            Collections.sort(users, new AroundUserComparator());
//        }
//        return users;
//    }

    /**
     * 退出时移除附近用户列表中的当前用户信息
     * 
     * @param playerId
     */
    public static void removeAroundUserInfo(int playerId)
    {
        if (!allPlayers.containsKey(playerId))
        {
            return;
        }
        GeohashUtil util = new GeohashUtil();
        if(null == allPlayers.get(playerId).getPlayerInfo()) {
        	logger.error("[ WorldMgr : removeAroundUserInfo ]  allPlayers.get(playerId).getPlayerInfo() is null");
        	return;
        }
        double[] posArray = DistanceUtil.getLatAndLon(allPlayers.get(playerId).getPlayerInfo().getPos());
        String hash = util.encode(posArray[0], posArray[1]) + playerId;
        try
        {
            lock.writeLock().lock();
            if (allAroundPlayers.containsKey(hash))
            {
                allAroundPlayers.remove(hash);
            }
        }
        catch (Exception e)
        {
            logger.error("[ WorldMgr : removeAroundUserInfo ]", e);
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

    /**
     * 
     * @param playerID
     * @return
     */
    public static GamePlayer getPlayerByID(int playerId)
    {
        GamePlayer player = null;
        lock.readLock().lock();
        player = allPlayers.get(playerId);
        lock.readLock().unlock();
        return player;
    }

    /**
     * 更新用户的位置信息
     * 
     * @param playerId
     */
    public static String updatePlayerLocationInfo(int playerId, double x,
            double y)
    {
    	boolean isUpdate = false;
    	PlayerInfo playerInfo = null;
    	String hashKey = "";
        try
        {
        	lock.writeLock().lock();
            if (allPlayers.containsKey(playerId))
            {
            	playerInfo = allPlayers.get(playerId).getPlayerInfo();
            	isUpdate = true;
                String currentUserPos = playerInfo.getPos();
                double[] posArray = DistanceUtil.getLatAndLon(currentUserPos);
                GeohashUtil geohash = new GeohashUtil();
                hashKey = geohash.encode(posArray[0], posArray[1]) + playerId;
                String pos = new String(x + "," + y);
                // 更新allPlayers中的位置信息
                
                allPlayers.get(playerId).getPlayerInfo().setPos(pos);
            }
        }
        finally
        {
            lock.writeLock().unlock();
        }
        if(isUpdate && null != playerInfo) {
        	AllAroundPlayerInfoMgr.updatePlayerLocationInfo(playerInfo, x, y);
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
    	AllAroundPlayerInfoMgr.updateAroundPlayerLocationInfo(hashKey, playerId, x, y);
        try
        {
        	lock.writeLock().lock();
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
            	}
            }
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }
    
}
