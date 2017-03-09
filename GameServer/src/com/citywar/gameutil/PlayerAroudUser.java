package com.citywar.gameutil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.dice.entity.AroundUser;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.AllAroundPlayerInfoMgr;
import com.citywar.type.DistanceType;
import com.citywar.util.DistanceUtil;

/**
 * 
 * @author shanfeng.cao
 *
 */
public class PlayerAroudUser {

	private static final Logger logger = Logger.getLogger(PlayerAroudUser.class
			.getName());
	/**
	 * 拥有的玩家
	 */
	private GamePlayer gamePlayer;
	
	private boolean nearRefresh = true;//附近玩家的刷新频率 五分钟一次
	
	private boolean chartsRefresh = true;//排行榜的刷新频率 一小时一次
    
	public static final int PAGE_SIZE = 20;
    
	public double NOW_MAX_DISTANCE = 0;
	
    private List<AroundUser> aroudUser;
	/**
	 * 附近玩家用到的 所有取到的附近玩家
	 */
    private Map<Integer, PlayerInfoAndAround> allAroudPlayer;
    /**
	 * 排行榜的玩家用到的 所有取到的附近玩家
	 */
    private final int distance_1 = 30000;//30公里
    
    private Map<Integer, PlayerInfoAndAround> allAroudPlayer_1;
    
    private final int distance_2 = 100000;//100公里
    
    private Map<Integer, PlayerInfoAndAround> allAroudPlayer_2;
    
    private final int distance_3 = 300000;//300公里
    
    private Map<Integer, PlayerInfoAndAround> allAroudPlayer_3;
    
    private final int distance_4 = 1000000;//1000公里
    
    private Map<Integer, PlayerInfoAndAround> allAroudPlayer_4;
    
    private final int distance_5 = 30000000;//30000公里
    
    private Map<Integer, PlayerInfoAndAround> allAroudPlayer_5;
    
    private ReadWriteLock lock = new ReentrantReadWriteLock(false);

	public PlayerAroudUser(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
		allAroudPlayer = new LinkedHashMap<Integer, PlayerInfoAndAround>();
		allAroudPlayer_1 = new LinkedHashMap<Integer, PlayerInfoAndAround>();
		allAroudPlayer_2 = new LinkedHashMap<Integer, PlayerInfoAndAround>();
		allAroudPlayer_3 = new LinkedHashMap<Integer, PlayerInfoAndAround>();
		allAroudPlayer_4 = new LinkedHashMap<Integer, PlayerInfoAndAround>();
		allAroudPlayer_5 = new LinkedHashMap<Integer, PlayerInfoAndAround>();
	}

	public List<AroundUser> getAroudUser() {
		return aroudUser;
	}

	public void setAroudUser(List<AroundUser> aroudUser) {
		this.aroudUser = aroudUser;
	}

	public boolean isNearRefresh() {
		return nearRefresh;
	}

	public void setNearRefresh(boolean nearRefresh) {
		this.nearRefresh = nearRefresh;
	}

	public boolean isChartsRefresh() {
		return chartsRefresh;
	}

	public void setChartsRefresh(boolean chartsRefresh) {
		if(chartsRefresh) {
			NOW_MAX_DISTANCE = 0;
		}
		this.chartsRefresh = chartsRefresh;
	}

	/**
	 * 取得至少多少页的附近玩家数
	 * @param pageCount
	 */
	private void selectAllAroundPlayers(int pageCount) {
		if(null == gamePlayer || null == gamePlayer.getPlayerInfo()) {
			return ;
		}
		allAroudPlayer.clear();//清空
		int addSize = 0;
		int minDistance = 0;//0千米
		int maxDistance = distance_1;
		Map<Integer, PlayerInfoAndAround> addAroundPlayers = addAroundPlayersByDistance(allAroudPlayer_1, minDistance, maxDistance);
		addAllAroudPlayer(addAroundPlayers);
		addSize = allAroudPlayer.size();
		if(addSize < pageCount * PAGE_SIZE) {
			minDistance = maxDistance;
			maxDistance = distance_2;
			addAroundPlayers = addAroundPlayersByDistance(allAroudPlayer_2, minDistance, maxDistance);
			addAllAroudPlayer(addAroundPlayers);
			addSize = allAroudPlayer.size();
			if(addSize < pageCount * PAGE_SIZE) {
				minDistance = maxDistance;
				maxDistance = distance_3;
				addAroundPlayers = addAroundPlayersByDistance(allAroudPlayer_3, minDistance, maxDistance);
				addAllAroudPlayer(addAroundPlayers);
				addSize = allAroudPlayer.size();
				if(addSize < pageCount * PAGE_SIZE) {
					minDistance = maxDistance;
					maxDistance = distance_4;
					addAroundPlayers = addAroundPlayersByDistance(allAroudPlayer_4, minDistance, maxDistance);
					addAllAroudPlayer(addAroundPlayers);
					addSize = allAroudPlayer.size();
					if(addSize < pageCount * PAGE_SIZE) {
						minDistance = maxDistance;
						maxDistance = distance_5;
						addAroundPlayers = addAroundPlayersByDistance(allAroudPlayer_5, minDistance, maxDistance);
						addAllAroudPlayer(addAroundPlayers);
					}
				}
			}
		}
		allAroudPlayer.remove(gamePlayer.getUserId());
		nearRefresh = false;//更新状态
	}
	
	/**
	 * 各种范围的玩家加到列表中
	 * 附近玩家用到的方法
	 * 顺便版排行榜更新一下数据
	 */
	private Map<Integer, PlayerInfoAndAround> addAroundPlayersByDistance(Map<Integer, PlayerInfoAndAround> chartsllAroudPlayer, double minDistance, double maxDistance) {
		if(null == chartsllAroudPlayer) {
			chartsllAroudPlayer = new LinkedHashMap<Integer, PlayerInfoAndAround>();
		} else {
			chartsllAroudPlayer.clear();
		}
		Map<Integer, PlayerInfoAndAround> allAroudPlayer = new LinkedHashMap<Integer, PlayerInfoAndAround>();
		PlayerInfo player =gamePlayer.getPlayerInfo();
		try{
			lock.writeLock().lock();
			if(NOW_MAX_DISTANCE < maxDistance) {
				NOW_MAX_DISTANCE = maxDistance;
			}
			List<AroundUser> tempAllAroudUser = AllAroundPlayerInfoMgr.getAroundPlayersByDistance(player, minDistance, maxDistance);
			List<PlayerInfoAndAround> tempAllAroudPlayer = AllAroundPlayerInfoMgr.getPlayerInfoAndArounds(gamePlayer, tempAllAroudUser);
			sortAndSublist(tempAllAroudPlayer);
			for(PlayerInfoAndAround aroudPlayer : tempAllAroudPlayer) {
				chartsllAroudPlayer.put(aroudPlayer.getUserId(), aroudPlayer);
//				PlayerInfo info = aroudPlayer.getAroudPlayer();
//				if(info != null) {//附近玩家的规则
//					if(info.getLevel() >= 6 ||(info.getPicPath() != null && ! info.getPicPath().isEmpty())) {
						allAroudPlayer.put(aroudPlayer.getUserId(), aroudPlayer);
//					}
//				}
			}
		} catch (Exception e) {
			logger.error("PlayerAroudUser addAroundPlayersByDistance userId = " + gamePlayer.getUserId(), e);
		} finally {
			lock.writeLock().unlock();
		}
		return allAroudPlayer;
	}

	/**
	 * 取得附近所有玩家
	 * 排行榜用到的方法
	 * @param pageCount
	 */
	private void selectAllAroundPlayersByDistance(double maxDistance2) {
		if(null == gamePlayer || null == gamePlayer.getPlayerInfo()) {
			return ;
		}
		allAroudPlayer.clear();//清空
		int minDistance = 0;//0千米
		int maxDistance = distance_1;
		allAroudPlayer_1 = addChartsAroundPlayersByDistance(minDistance, maxDistance);
		addAllAroudPlayer(allAroudPlayer_1);
		minDistance = maxDistance;
		maxDistance = distance_2;
		if(maxDistance2 <= maxDistance) {
		}
		allAroudPlayer_2 = addChartsAroundPlayersByDistance(minDistance, maxDistance);
		addAllAroudPlayer(allAroudPlayer_2);
		minDistance = maxDistance;
		maxDistance = distance_3;
		if(maxDistance2 <= maxDistance) {
			return ;
		}
		allAroudPlayer_3 = addChartsAroundPlayersByDistance(minDistance, maxDistance);
		addAllAroudPlayer(allAroudPlayer_3);
		minDistance = maxDistance;
		maxDistance = distance_4;
		if(maxDistance2 <= maxDistance) {
			return ;
		}
		allAroudPlayer_4 = addChartsAroundPlayersByDistance(minDistance, maxDistance);
		addAllAroudPlayer(allAroudPlayer_4);
		minDistance = maxDistance;
		maxDistance = distance_5;
		if(maxDistance2 <= maxDistance) {
			return ;
		}
		allAroudPlayer_5 = addChartsAroundPlayersByDistance(minDistance, maxDistance);
		addAllAroudPlayer(allAroudPlayer_5);
		nearRefresh = false;//更新状态
	}
	
	/**
	 * 各种范围的玩家加到列表中
	 * 排行榜用到的方法
	 */
	private Map<Integer, PlayerInfoAndAround> addChartsAroundPlayersByDistance(double minDistance, double maxDistance) {
		Map<Integer, PlayerInfoAndAround> allAroudPlayer_type = new LinkedHashMap<Integer, PlayerInfoAndAround>();
		PlayerInfo player =gamePlayer.getPlayerInfo();
		try{
			lock.writeLock().lock();
			if(NOW_MAX_DISTANCE < maxDistance) {
				NOW_MAX_DISTANCE = maxDistance;
			}
			List<AroundUser> tempAllAroudUser = AllAroundPlayerInfoMgr.getAroundPlayersByDistance(player, minDistance, maxDistance);
			List<PlayerInfoAndAround> tempAllAroudPlayer = AllAroundPlayerInfoMgr.getPlayerInfoAndArounds(gamePlayer, tempAllAroudUser);
			sortAndSublist(tempAllAroudPlayer);
			for(PlayerInfoAndAround aroudPlayer : tempAllAroudPlayer) {
				allAroudPlayer_type.put(aroudPlayer.getUserId(), aroudPlayer);
			}
		} catch (Exception e) {
			logger.error("PlayerAroudUser addAroundPlayersByDistance userId = " + gamePlayer.getUserId(), e);
		} finally {
			lock.writeLock().unlock();
		}
		return allAroudPlayer_type;
	}
	
	/**
	 * 取得至少多少页的附近玩家数
	 * @param pageCount
	 */
	public List<PlayerInfoAndAround> getAllAroundPlayers(int pageCount) {
		if(pageCount * PAGE_SIZE < 0) {
			return null;
		}
		List<PlayerInfoAndAround> tempAllAroudPlayer = new LinkedList<PlayerInfoAndAround>();
		if(nearRefresh) {
			selectAllAroundPlayers(pageCount);
		} else if(pageCount * PAGE_SIZE > allAroudPlayer.size()) {
			selectAllAroundPlayers(pageCount);
		}
		
		System.out.println("getAllAroundPlayers========================================" + allAroudPlayer.size());
		
		try{
			lock.writeLock().lock();
			int size = allAroudPlayer.size();
			int beginIndex = ((pageCount - 1) * PAGE_SIZE <= size)? (pageCount - 1) * PAGE_SIZE : size;
			int endIndex = (pageCount * PAGE_SIZE >= size)? size : pageCount * PAGE_SIZE;
//			int i=beginIndex;i<endIndex;i++
			int index = 0;
			for(PlayerInfoAndAround playerInfoAndAround : allAroudPlayer.values()) {
				if(beginIndex <= index && index < endIndex) {
					tempAllAroudPlayer.add(playerInfoAndAround);
				} else if(index > endIndex) {
					break ;
				}
				index++;
			}
		} catch (Exception e) {
			logger.error("PlayerAroudUser getAllAroundPlayers userId = " + gamePlayer.getUserId(), e);
		} finally {
			lock.writeLock().unlock();
		}
		return tempAllAroudPlayer;
	}

    /**
     * 根据相应规则对附近用户排序
     * 
     * @param users
     * @return
     */
    private List<PlayerInfoAndAround> sortAndSublist(List<PlayerInfoAndAround> users)
    {
        if (null != users && users.size() > 1)
        {
        	System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(users, new AroundPlayerInfoComparator());
        }
        return users;
    }

    /**
     * 排行榜取得附近玩家
     * @param maxDistance
     * @return
     */
	public List<PlayerInfoAndAround> getNearPlayerInfoAndAround(double maxDistance) {
		
		if(gamePlayer.getPlayerInfo().getPos().equals("0,0"))
			maxDistance = DistanceType.WORLD.getDistance();
		List<PlayerInfoAndAround> aroundNearPlayers = new LinkedList<PlayerInfoAndAround>();
		if(gamePlayer == null || gamePlayer.getPlayerInfo() == null) {	//	|| 
			return aroundNearPlayers;
		}
		Map<Integer, PlayerInfoAndAround> tempAllAroudPlayer = new LinkedHashMap<Integer, PlayerInfoAndAround>();
		if(chartsRefresh || NOW_MAX_DISTANCE < maxDistance) {
			selectAllAroundPlayersByDistance(maxDistance);
			chartsRefresh = false;
			NOW_MAX_DISTANCE = maxDistance;
		}
		tempAllAroudPlayer.putAll(allAroudPlayer_1);
		tempAllAroudPlayer.putAll(allAroudPlayer_2);
		tempAllAroudPlayer.putAll(allAroudPlayer_3);
		tempAllAroudPlayer.putAll(allAroudPlayer_4);
		tempAllAroudPlayer.putAll(allAroudPlayer_5);
		boolean isHaveSelf = false;
		int count = 0;
    	for(PlayerInfoAndAround aroundPlayer : tempAllAroudPlayer.values()) {
    		if(null == aroundPlayer.getAroudUser() || null == aroundPlayer.getAroudPlayer()) {
    			continue ;
    		}
        	if(aroundPlayer.getAroudUser().getUserDistance() <= maxDistance || count < 50) {
            	if(! isHaveSelf && aroundPlayer.getAroudPlayer().equals(gamePlayer.getPlayerInfo())) {
            		isHaveSelf = true;
        		}
        		if(! aroundNearPlayers.contains(aroundPlayer)) {
        			aroundNearPlayers.add(aroundPlayer);
        		}
    		} else {
    			break ;
    		}
        	count++;
    	}
    	if( ! isHaveSelf && aroundNearPlayers.size() > 0) {//加上玩家自己
    		PlayerInfoAndAround selfPlayerInfoAndAround = new PlayerInfoAndAround();
    		selfPlayerInfoAndAround.setAroudPlayer(gamePlayer.getPlayerInfo());
    		AroundUser selfAroudUser = new AroundUser();
    		double[] posArray = DistanceUtil.getLatAndLon(gamePlayer.getPlayerInfo().getPos());
    		selfAroudUser.setX(posArray[0]);
    		selfAroudUser.setY(posArray[1]);
    		selfAroudUser.setUserDistance(0);
    		selfAroudUser.setUserId(gamePlayer.getPlayerInfo().getUserId());
    		selfPlayerInfoAndAround.setAroudUser(selfAroudUser);
    		aroundNearPlayers.add(selfPlayerInfoAndAround);
    	}
		return aroundNearPlayers;
	}
	
	/**
	 * 加入附件玩家中
	 * @param playerInfoAndAround
	 * @return
	 */
	public PlayerInfoAndAround addAroadUserPlayer(PlayerInfoAndAround playerInfoAndAround) {
		if(null == playerInfoAndAround.getAroudUser()) {
			return null;
		}
		int distance = (int)playerInfoAndAround.getAroudUser().getUserDistance();
		if(distance < distance_1) {
			allAroudPlayer_1.put(playerInfoAndAround.getUserId(), playerInfoAndAround);
		} else if(distance < distance_2) {
			allAroudPlayer_2.put(playerInfoAndAround.getUserId(), playerInfoAndAround);
		} else if(distance < distance_3) {
			allAroudPlayer_3.put(playerInfoAndAround.getUserId(), playerInfoAndAround);
		} else if(distance < distance_4) {
			allAroudPlayer_4.put(playerInfoAndAround.getUserId(), playerInfoAndAround);
		} else if(distance < distance_5) {
			allAroudPlayer_5.put(playerInfoAndAround.getUserId(), playerInfoAndAround);
		}
		return playerInfoAndAround;
	}
		
		/**
		 * 加入附件玩家中
		 * @param aroudPlayer
		 * @return
		 */
	public PlayerInfoAndAround addAroadUserPlayer(PlayerInfo aroudPlayer) {
		if(null == aroudPlayer) {
			return null;
		}
		double[] userPosArray = DistanceUtil.getLatAndLon(gamePlayer.getPlayerInfo().getPos());
		double[] userPos = DistanceUtil.getLatAndLon(aroudPlayer.getPos());
		double distance = DistanceUtil.distanceByLatLon(userPosArray[0],
                        userPosArray[1],
                        userPos[0],userPos[1]);
		PlayerInfoAndAround playerInfoAndAround = new PlayerInfoAndAround();
		playerInfoAndAround.setAroudPlayer(aroudPlayer);
		playerInfoAndAround.setUserId(aroudPlayer.getUserId());
		AroundUser aroudUser = new AroundUser();
		aroudUser.setUserDistance(distance);
		aroudUser.setX(userPos[0]);
		aroudUser.setY(userPos[1]);
		playerInfoAndAround.setAroudUser(aroudUser);
		return addAroadUserPlayer(playerInfoAndAround);
	}
	
	/**
	 * 从附件玩家中取出
	 * 没有则加入
	 * @param aroudPlayer
	 * @return
	 */
	public PlayerInfoAndAround getAroadUserPlayer(PlayerInfo aroudPlayer) {
		if(null == aroudPlayer) {
			return null;
		}
		PlayerInfoAndAround playerInfoAndAround = allAroudPlayer_1.get(aroudPlayer.getUserId());
		if(null == playerInfoAndAround) {
			playerInfoAndAround = allAroudPlayer_2.get(aroudPlayer.getUserId());
			if(null == playerInfoAndAround) {
				playerInfoAndAround = allAroudPlayer_3.get(aroudPlayer.getUserId());
				if(null == playerInfoAndAround) {
					playerInfoAndAround = allAroudPlayer_4.get(aroudPlayer.getUserId());
					if(null == playerInfoAndAround) {
						playerInfoAndAround = allAroudPlayer_5.get(aroudPlayer.getUserId());
						if(null == playerInfoAndAround) {
							playerInfoAndAround = addAroadUserPlayer(aroudPlayer);
						}
					}
				}
			}
		}
		if(null != playerInfoAndAround) {
			playerInfoAndAround.setAroudPlayer(aroudPlayer);
		}
		return playerInfoAndAround;
	}
	
	/**
	 * 加入附件玩家
	 * @param aroudPlayer
	 * @return
	 */
	public void addAllAroudPlayer(Map<Integer, PlayerInfoAndAround> allAroudPlayer_nunber) {
		if(null == allAroudPlayer_nunber) {
			return ;
		}
		PlayerInfo info = null;
		try{
			lock.writeLock().lock();
			for(Entry<Integer, PlayerInfoAndAround> entry : allAroudPlayer_nunber.entrySet()) {
				info = entry.getValue().getAroudPlayer();//附近玩家的规则
				if(info != null 
						&& (info.getPicPath() != null && !info.getPicPath().isEmpty())) {
					allAroudPlayer.put(entry.getKey(), entry.getValue());
				}
			}
		} catch (Exception e) {
			logger.error("PlayerAroudUser addAllAroudPlayer ", e);
		} finally {
			lock.writeLock().unlock();
		}
	}
    
    public void printlnAllAroudPlayer2()
    {
        //System.out.println("=============================begin==============================");
    	for(PlayerInfoAndAround playerInfoAndAround : allAroudPlayer.values()) {
            System.out.print(playerInfoAndAround.getAroudPlayer().getUserName());
            System.out.print("============" + playerInfoAndAround.getAroudPlayer().getLastQiutDate());
            System.out.print("============" + playerInfoAndAround.getAroudPlayer().getPicPath());
            //System.out.println("============" + (playerInfoAndAround.getAroudUser().getUserDistance() / 1000));
    	}
        System.out.print("=============================end==============================");
    }
    
    public void printlnAllAroudPlayer2(List<PlayerInfoAndAround> tempAllAroudPlayer)
    {
        //System.out.println("=============================tempAllAroudPlayer begin==============================");
    	for(PlayerInfoAndAround playerInfoAndAround : tempAllAroudPlayer) {
            System.out.print(playerInfoAndAround.getAroudPlayer().getUserName());
            System.out.print("============" + playerInfoAndAround.getAroudPlayer().getLastQiutDate());
            System.out.print("============" + playerInfoAndAround.getAroudPlayer().getPicPath());
            //System.out.println("============" + (playerInfoAndAround.getAroudUser().getUserDistance() / 1000));
    	}
        System.out.print("=============================tempAllAroudPlayer end==============================");
    }
}
