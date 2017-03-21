package com.citywar.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.ReferenceBussiness;
import com.citywar.bll.ReferenceLogBussiness;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserRefWorkInfo;
import com.citywar.dice.entity.UserReferenceInfo;
import com.citywar.dice.entity.UserReferenceLogInfo;
import com.citywar.game.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.util.SafeLinkedHashMap;
import com.citywar.util.ThreadSafeRandom;

/**
 * 奴隶关系管理类
 * 
 * @author Jacky.zheng
 * 
 */
public class ReferenceMgr {
	
	private static Map<Integer, UserReferenceInfo> allReferenceMap;// key--我们自己生成的真实id
	
	private static Map<Integer, UserRefWorkInfo> ALLRefWorkList;
	/*
	 * 保存所有用户在没没上线的时候产生的关系日志，并且是排好序的。
	 * 以便于定时存入数据库中去
	 */
	private static List<UserReferenceLogInfo> AllUserReferenceLogNowNotToDBSortList;

	private static ReadWriteLock rwListLock;
	
	private static ReadWriteLock rwLock;

	private static final Logger logger = Logger.getLogger(ReferenceMgr.class.getName());

	/**
	 * 奴隶关系管理初始化
	 * 
	 * @return
	 */
	public static boolean init() {
		rwLock = new ReentrantReadWriteLock();
		rwListLock = new ReentrantReadWriteLock();
		allReferenceMap = new SafeLinkedHashMap<Integer, UserReferenceInfo>();//LinkedHashMap
		AllUserReferenceLogNowNotToDBSortList = new ArrayList<UserReferenceLogInfo>();
		
		ALLRefWorkList = new SafeLinkedHashMap<Integer, UserRefWorkInfo>();//LinkedHashMap
		for(UserRefWorkInfo work : ReferenceBussiness.getRefWorkList())
			ALLRefWorkList.put(work.getId(), work);
		
		return true;
	}
	
	/**
	 * 取得所有管理器中 的关系数据
	 * key -- 我们生成关系ID value -- 关系
	 * @return
	 */
	public static Map<Integer, UserReferenceInfo> getAllReferenceMap() {
		Map<Integer, UserReferenceInfo> tempMap = new HashMap<Integer, UserReferenceInfo>();
		try {
			rwLock.readLock().lock();
			for (Entry<Integer, UserReferenceInfo> entry : allReferenceMap.entrySet()) {
				tempMap.put(entry.getKey(), entry.getValue());
			}
		} catch (Exception e) {
			logger.error("[ ReferenceMgr : getAllReferenceMap ]", e);
		} finally {
			rwLock.readLock().unlock();
		}
		return tempMap;
	}

	public static List<Integer> loadUserReferences(List<UserReferenceInfo> list) {
		List<Integer> keyList = new LinkedList<Integer>();
		if(null == list) {
			return null;
		}
		try {
			boolean isHaveInfo = false;
			Map<Integer, UserReferenceInfo> tempAllReferenceMap = getAllReferenceMap();
			rwLock.writeLock().lock();//重要
			for (UserReferenceInfo listInfo : list) {
				isHaveInfo = false;
				Entry<Integer, UserReferenceInfo> tempInfo = null;
				for (Entry<Integer, UserReferenceInfo> entry : tempAllReferenceMap.entrySet()) {
					if (entry.getValue().getId() == listInfo.getId()) {// 查看HashMap中是否有了这条关系
						isHaveInfo = true;
						tempInfo = entry;
					}
				}
				int ketReferenceId = 0;
				if (!isHaveInfo) {// HashMap中没有了这条关系
					ketReferenceId = listInfo.getId();
					// 重点   查看allReferenceMap的key是否已经用到了这个主键，用到了的话就要重新生成
					while (ketReferenceId == 0 || allReferenceMap.containsKey(ketReferenceId)) {
						ketReferenceId = new ThreadSafeRandom().next(1, 100000);
					}
					allReferenceMap.put(ketReferenceId, listInfo);
				} else {
					ketReferenceId = null != tempInfo ? tempInfo.getKey() : 0;
				}
				keyList.add(ketReferenceId);
			}
		} catch (Exception e) {
			logger.error("[ ReferenceMgr : loadUserReferences ]", e);
		} finally {
			rwLock.writeLock().unlock();
		}
		return keyList;
	}
	/**
	 * 直接设置为主键 的加载
	 * 
	 * @return
	 */
	public static void setUserReference(Integer ketReferenceId, UserReferenceInfo userReferenceInfo) {
		if(null == userReferenceInfo) {
			return ;
		}
		rwLock.writeLock().lock();
		try {
			allReferenceMap.put(ketReferenceId, userReferenceInfo);
		} catch (Exception e) {
			logger.error("[ ReferenceMgr : loadUserReference ]", e);
		} finally {
			rwLock.writeLock().unlock();
		}
	}
	
	public static int loadUserReference(UserReferenceInfo userReferenceInfo) {
		if(null == userReferenceInfo) {
			return 0;
		}
		List<UserReferenceInfo> list = new ArrayList<UserReferenceInfo>();
		list.add(userReferenceInfo);
		List<Integer> result = loadUserReferences(list);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return 0;
	}

	public static void saveIntoDB() {
		//先保存奴隶关系日志
		rwListLock.writeLock().lock();
		try {
			saveReferenceLogIntoDBByList(AllUserReferenceLogNowNotToDBSortList);
			AllUserReferenceLogNowNotToDBSortList.clear();
		} catch (Exception e) {
			logger.error("[ ReferenceMgr rwListLock : saveIntoDB ]", e);
		} finally {
			rwListLock.writeLock().unlock();
		}
		
		rwLock.readLock().lock();
		try {
			saveIntoDBByList(allReferenceMap.values());
		} catch (Exception e) {
			logger.error("[ ReferenceMgr rwLock : saveIntoDB ]", e);
		} finally {
			rwLock.readLock().unlock();
		}
	}

	public static int addReference(UserReferenceInfo info) {
		if (null == info) {
			return 0;
		}
		int userReferenceId = info.getId();
		rwLock.writeLock().lock();
		try {
			// 这里加入到用户奴隶集合的时候需判断集合中生成的随机ID是否存在,存在的话需要重新生成
			while (userReferenceId == 0 || allReferenceMap.containsKey(userReferenceId)) {//这里是我们自己生成的
				userReferenceId = new ThreadSafeRandom().next(1, 10000);
				//info.setId(userReferenceId);--区别未入库的对象和已经入库的对象,不能设置为非0的值
			}
			allReferenceMap.put(userReferenceId, info);// 加入--这里allReferenceMap的Key是随机的一个1-10000的不重复整数
		} catch (Exception e) {
			logger.error("[ ReferenceMgr : addReference ]", e);
		} finally {
			rwLock.writeLock().unlock();
		}
		return userReferenceId;
	}

	/**
	 * 通过关系key查找某个关系信息
	 * @param Key---唯一的关系信息标识与信息id无关
	 * 
	 * @return
	 */
	public static UserReferenceInfo getUserReferenceInfoByUserReferenceId(int UserReferenceId) {
		rwLock.readLock().lock();
		try {
			return allReferenceMap.get(UserReferenceId);
		} catch (Exception e) {
			logger.error("[ ReferenceMgr : getUserReferenceInfoByUserReferenceId ]", e);
		} finally {
			rwLock.readLock().unlock();
		}
		return null;
	}

	/**
	 * 通过关系id的List查找某些关系信息
	 * 
	 * @return
	 */
	public static List<UserReferenceInfo> getUserReferenceInfoByList(List<Integer> slavesSortList) {
		List<UserReferenceInfo> userSlavesList = new ArrayList<UserReferenceInfo>();
		UserReferenceInfo referenceInfo = null;
		if (null != slavesSortList && !slavesSortList.isEmpty()) {
//			//System.out.println("通过关系id====查询关系在内存中的值");
			rwLock.readLock().lock();
			try {
				for (Integer id : slavesSortList) {
	//				//System.out.println("id=====" + id + "====是否在内存中==" + allReferenceMap.containsKey(id));
					if (allReferenceMap.containsKey(id)) { // 首先要判断是否包含在插入的集合中
						referenceInfo = allReferenceMap.get(id).clone();
						referenceInfo.setId(id);
						userSlavesList.add(referenceInfo);
					}
				}
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				rwLock.readLock().unlock();
			}
		}
		return userSlavesList;
	}
	
	/**
	 * 查找某个关系信息,看是否需要从管理器中移除
	 * 
	 * @return
	 */
	public static void removeReferenceByKeyId(int userReferenceId) {
		if(userReferenceId <= 0) {
			return ;
		}
		List<Integer> list = new ArrayList<Integer>();
		list.add(userReferenceId);
		removeReferenceByKeyList(list);
	}
	
	/**
	 * 查找某个关系List信息,看是否需要从管理器中移除
	 * 
	 * @return
	 */
	public static void removeReferenceByKeyList(List<Integer> list) {
		if(null == list || list.size() < 0) {
			return ;
		}
		List<UserReferenceInfo> allUserRefSaveList = new ArrayList<UserReferenceInfo>();
		List<Integer> tempList = new ArrayList<Integer>();
		GamePlayer masterPlayer = null;
		GamePlayer slavePlayer = null;
		Player robot = null;
		for(Integer userReferenceId : list) {
			UserReferenceInfo info = getUserReferenceInfoByUserReferenceId(userReferenceId);
			if(null != info) {
				allUserRefSaveList.add(info);
				boolean isRemoveSlave = false;
				masterPlayer = WorldMgr.getPlayerByID(info.getMasterUserId());
				if(null == masterPlayer) {//如果奴隶主不在线，移除
					isRemoveSlave = true;
				}else if(!masterPlayer.getPlayerReference().isHaveSlavesByReferenceId(userReferenceId)) {
					//如果奴隶主在线，但是不在奴隶主最新的50（以前有）条中，移除。或者奴隶主为机器人也没必要保存
					isRemoveSlave = true;
				}
				boolean isRemoveMaster = true;
				if(isRemoveMaster && info.getTakeUserId() == 0 && null == info.getRemoveTime()) {
					slavePlayer = WorldMgr.getPlayerByID(info.getSlavesUserId());
					if (null == slavePlayer) {
						//从在值班的机器人中取
						robot = RobotMgr.getRobotByID(info.getSlavesUserId());
						if (null != robot) {
							slavePlayer = robot.getPlayerDetail();
						}
					}
					if(null != slavePlayer) {
						isRemoveMaster = false;
					}
				}
				if(isRemoveSlave && isRemoveMaster) {//只有在条信息的主人和奴隶都不在线的时候才能够移除
					tempList.add(userReferenceId);
				}
			} else {
				logger.error("[ ReferenceMgr : removeReferenceByKeyList ] 没去到 ID 为：" + userReferenceId);
			}
		}
		saveIntoDBByList(allUserRefSaveList);//把所以用户的信息都保存到数据库中去
		//System.out.println("有多少条关系从内存中移除====" + tempList.size());
		if(tempList.size() > 0) {
			rwLock.readLock().lock();
			try {
				for(Integer removeUserReferenceId : tempList) {
					allReferenceMap.remove(removeUserReferenceId);
				}
			} catch (Exception e) {
				logger.error("[ ReferenceMgr : removeReferenceByKeyList ]", e);
			} finally {
				rwLock.readLock().unlock();
			}
		}
	}

	/**
	 * 把奴隶关系的保存到数据库中区
	 * 
	 * @return
	 */
	public static void saveIntoDBByList(Collection<UserReferenceInfo> saveList) {
		List<UserReferenceInfo> updateReferences = new ArrayList<UserReferenceInfo>();
		List<UserReferenceInfo> insertReferences = new ArrayList<UserReferenceInfo>();
		try {
			for (UserReferenceInfo info : saveList) {
				if (info == null)
					continue;
				switch (info.getOp()) {
				case Option.INSERT:
					insertReferences.add(info);
					break;
				case Option.UPDATE:
					updateReferences.add(info);
					break;
				default:
					break;
				}
				info.setOp(Option.NONE);
			}
			ReferenceBussiness.insertUserReferences(insertReferences);//插入新的
			ReferenceBussiness.updateUserReferences(updateReferences);//更新需要更新的
		} catch (Exception e) {
			logger.error("[ ReferenceMgr : saveIntoDBByList ]", e);
		}
	}
	
	/**
	 * 把奴隶关系的日志的保存到数据库中区
	 * 
	 * @return
	 */
	public static void saveReferenceLogIntoDBByList(Collection<UserReferenceLogInfo> saveList) {
		List<UserReferenceLogInfo> updateReferences = new ArrayList<UserReferenceLogInfo>();
		List<UserReferenceLogInfo> insertReferences = new ArrayList<UserReferenceLogInfo>();
		try {
			for (UserReferenceLogInfo info : saveList) {
				if (info == null)
					continue;
				switch (info.getOp()) {
				case Option.INSERT:
					insertReferences.add(info);
					break;
				case Option.UPDATE:
					updateReferences.add(info);
					break;
				default:
					break;
				}
				info.setOp(Option.NONE);
			}
			ReferenceLogBussiness.insertUserReferenceLogs(insertReferences);//插入新的
			ReferenceLogBussiness.updateUserReferenceLogs(updateReferences);//更新需要更新的
		} catch (Exception e) {
			logger.error("[ ReferenceMgr : saveReferenceLogIntoDBByList ]", e);
		}
	}
	
	public static List<UserReferenceInfo> getAllReference() {
		List<UserReferenceInfo> list = new ArrayList<UserReferenceInfo>();
    	rwLock.readLock().lock();
        try
        {
            for (UserReferenceInfo t : allReferenceMap.values())
            {
                if (t == null)
                    continue;

                list.add(t);
            }
        }
        finally
        {
        	rwLock.readLock().unlock();
        }
        return list;
	}
	
	/**
     * 在生成新的数据时，该用户不在线的时候，先存储在管理器中，以便于同时入库
     * @param UserReferenceLogInfo 生成新的数据
     * @return 
     */
	public static void addTempUserReferenceLog(UserReferenceLogInfo info) {
		rwListLock.readLock().lock();
		try {
			AllUserReferenceLogNowNotToDBSortList.add(info);
		} catch (Exception e) {
			logger.error("[ ReferenceMgr : addTempUserReferenceLog ]", e);
		} finally {
			rwListLock.readLock().unlock();
		}
	}
	
	/**
     * 在玩家登陆的时候，来关系管理器中取还没有入库的最先数据
     * @param userId
     * @return List<UserReferenceLogInfo> 玩家 还没有入库的最先数据
     */
	public static List<UserReferenceLogInfo> getUserReferenceLogByUserId(int userId) {
		if(userId < 0 || AllUserReferenceLogNowNotToDBSortList.size() <= 0) {
			return null;
		}
		List<UserReferenceLogInfo> referenceLogList = new ArrayList<UserReferenceLogInfo>(); 
		rwListLock.readLock().lock();
		try {
			for(UserReferenceLogInfo info :AllUserReferenceLogNowNotToDBSortList) {
				if(userId == info.getOwnerUserId()) {
					referenceLogList.add(info);
				}
			}
			AllUserReferenceLogNowNotToDBSortList.removeAll(referenceLogList);
		} catch (Exception e) {
			logger.error("[ ReferenceMgr : getUserReferenceLogByUserId ]", e);
		} finally {
			rwListLock.readLock().unlock();
		}
		return referenceLogList;
	}
	
	/**
     * 在玩家对奴隶进行处理的时候（奖励和折磨），奴隶主生成日志 带来的经验或者金币数
     * @param player主人 info对应的奴隶关系 decideType处理类型 addManyOrGp给主人带来的收益
     * @return UserReferenceLogInfo
     */
	public static UserReferenceLogInfo buildMasterToSlaveDecideLogForMaster(GamePlayer player, UserReferenceInfo info, byte decideType, int addManyOrGp) {
		if(null == player || null == info) {
			return null;
		}
		UserReferenceLogInfo logInfo  = new UserReferenceLogInfo();
		Timestamp time =  new Timestamp(System.currentTimeMillis());//惩罚或者奖励的时间
		logInfo.setOwnerUserId(info.getMasterUserId());
		logInfo.setPassivesUserId(info.getSlavesUserId());
		logInfo.setRefId(info.getId());
		logInfo.setLogCreateTime(time);
		logInfo.setOp(Option.INSERT);
		if (decideType == 1) {
			//建立关于新的日志   收取税收
			logInfo.formatToContent(2, info.getSlavesUserName(), addManyOrGp);
		} else if (decideType == 2) {// && isAddGpDirect
			//建立关于新的日志  给奴隶惩罚
			logInfo.formatToContent(3, info.getSlavesUserName(), addManyOrGp);
		}
		player.getPlayerReference().addUserReferenceLog(logInfo);
		//System.out.println("buildMasterToSlaveDecideLogForMaster======" + logInfo);
		return logInfo;
	}
	
	/**
     * 在玩家对奴隶进行处理的时候（奖励和折磨），给奴隶生成日志 带来的经验或者金币数
     * @param info对应的奴隶关系 decideType处理类型 addManyOrGp给主人带来的收益
     * @return UserReferenceLogInfo
     */
	public static UserReferenceLogInfo buildMasterToSlaveDecideLogForSlave(UserReferenceInfo info, byte decideType, int addManyOrGp) {
		UserReferenceLogInfo logInfo  = new UserReferenceLogInfo();
		Timestamp time =  new Timestamp(System.currentTimeMillis());//惩罚或者奖励的时间
		logInfo.setOwnerUserId(info.getSlavesUserId());
		logInfo.setPassivesUserId(info.getMasterUserId());
		logInfo.setRefId(info.getId());//没什么用现在还都是0
		logInfo.setLogCreateTime(time);
		logInfo.setOp(Option.INSERT);
		if (decideType == 1) {
			//建立关于新的日志   收取税收
			logInfo.formatToContent(4, info.getMasterUserName(), addManyOrGp);
		} else if (decideType == 2) {// && isAddGpDirect
			//建立关于新的日志  给奴隶惩罚
			logInfo.formatToContent(5, info.getMasterUserName(), addManyOrGp);
		}
		GamePlayer masterPlayer = WorldMgr.getPlayerByID(info.getSlavesUserId());//奴隶（机器人没有）
		if(null != masterPlayer) {//如果奴隶主在线的话，给他加上日志
			masterPlayer.getPlayerReference().addUserReferenceLog(logInfo);
		} else {//如果奴隶主在线的话，存入到关系管理器中去。以便于定时存入数据库中去//TODO 机器人有没有
			if( ! RobotMgr.isRobotByID(info.getMasterUserId())) {//（机器人没有）
				ReferenceMgr.addTempUserReferenceLog(logInfo);
			}
		}
		//System.out.println("buildMasterToSlaveDecideLogForSlave======" + logInfo);
		return logInfo;
	}
	
	/**
     * 取得玩家最新的奴隶主关系信息
     * @param userId 用户ID
     * @return UserReferenceInfo
     */
	public static UserReferenceInfo getMasterInfoByUserId(int userId) {
		UserReferenceInfo info = null;
		rwLock.readLock().lock();
		try {
			for (Entry<Integer, UserReferenceInfo> entry : allReferenceMap.entrySet()) {
				if (entry.getValue().getSlavesUserId() == userId) {// 查看HashMap中是否有了这条关系
					info = entry.getValue();//一定不能有break
				}
			}
		} catch (Exception e) {
			logger.error("[ ReferenceMgr : getMasterInfoByUserId ]", e);
		} finally {
			rwLock.readLock().unlock();
		}
		return info;
	}

	/**
	 * 得到用户的奴隶数
	 * 
	 * 先从内存中取
	 * 内存中取不到  就从数据库中取
	 * 
	 * @param userId
	 * @return
	 */
	public static int getSlaveCount(int userId){
//		if(GamePlayerUtil.isOnLine(userId)) {//用户在线的话  从内存中取
//			GamePlayer masterPlayer   = GamePlayerUtil.getOnLineGamePlayer(userId);
//			if(null != masterPlayer.getPlayerReference()) {
//				return masterPlayer.getPlayerReference().getCurrentSlavesCount();
//			}
//			return 0;
//		}//TODO
		PlayerInfo playerInfo = AllAroundPlayerInfoMgr.getPlayerInfoById(userId);
		if(null != playerInfo) {
			return playerInfo.getSlaveCount();
		} else {//表明此人不在线,需要从数据库中统计
			return getSlaveCountOnDB(userId);
		}
	}
	/**
	 * 得到用户的奴隶数
	 * 
	 * 从数据库中取（是否需要先从内存中取）
	 * 
	 * @param userId
	 * @return
	 */
	private static int getSlaveCountOnDB(int userId){
		List<UserReferenceInfo> result = new ArrayList<UserReferenceInfo>();
		Timestamp validityTime = new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 *1000);
		List<UserReferenceInfo> list = ReferenceBussiness.getUserAllNowValidReferencesLimit(userId, validityTime, 0);//查出所有玩家有效的奴隶
		if (null != list && list.size() > 0) {
			List<UserReferenceInfo> existReferences = ReferenceMgr.getAllReference();
			for (UserReferenceInfo info : list) {
				boolean isAddList = false;
				// 判断当前的奴隶信息在内存中有无已存在的,如果有需要取内存中最新的,数据库中的数据存在延迟
				//TODO 过滤条件的方式可以优化--待处理
				for (UserReferenceInfo info2 : existReferences) {
					if (info2.getId() == info.getId()) {
						isAddList = true; //内存中存在 
						//判断奴隶是否被抢走
						if (info2.getTakeUserId() == 0 || info2.getRemoveTime() == null) {
							result.add(info2);
						}
					}
				}
				if (!isAddList) { //如果内存中是不存在的
					if (info.getTakeUserId() == 0 || info.getRemoveTime() == null) {
						result.add(info);
					}
				}
			}
			return result.size();
		}
		return 0;
	}
	/**
	 * 减少用户的奴隶数
	 * 
	 * @param userId
	 * @return
	 */
	public static void subSlaveCount(int userId){
		PlayerInfo playerInfo = AllAroundPlayerInfoMgr.getPlayerInfoById(userId);
		if(null != playerInfo) {
			int oldCount = playerInfo.getSlaveCount();
			playerInfo.setSlaveCount(--oldCount);
		}
	}
	/**
	 * 增加用户的奴隶数
	 *
	 * @param userId
	 * @return
	 */
	public static void addSlaveCount(int userId){
		PlayerInfo playerInfo = AllAroundPlayerInfoMgr.getPlayerInfoById(userId);
		if(null != playerInfo) {
			int oldCount = playerInfo.getSlaveCount();
			playerInfo.setSlaveCount(++oldCount);
		}
	}
	
	
	public static UserRefWorkInfo getRndWork()
	{
		ThreadSafeRandom random = new ThreadSafeRandom();
		int index = random.next(1, ALLRefWorkList.size());
		return ALLRefWorkList.get(index);
		 
	}
	
	
	public static UserRefWorkInfo getWorkById(int id)
	{
		return ALLRefWorkList.get(id);		 
	}
}
