package com.citywar.gameutil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.bll.ReferenceBussiness;
import com.citywar.bll.ReferenceLogBussiness;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.UserRefWorkInfo;
import com.citywar.dice.entity.UserReferenceInfo;
import com.citywar.dice.entity.UserReferenceLogInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.manager.ReferenceMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.WorldMgr;

/**
 * 玩家奴隶和奴隶日志
 * @author shanfeng.cao
 *
 */
public class PlayerReference {
	
	private static final Logger logger = Logger.getLogger(PlayerReference.class.getName());
	
	/**
	 * 保存 玩家 此次登陆的 所有主人关系ID list
	 */
	private List<UserReferenceLogInfo> oldMasterReferenceIdList;

	private int masterReferenceId;//（机器人也有）
	
	/**
	 * 保存了与奴隶之间最新的50关系ID，并且是排好序的（机器人没有）
	 */
	private List<Integer> slavesSortList;

	/**
	 * 保存用户关系日志，并且是排好序的（机器人没有）
	 */
	private List<UserReferenceLogInfo> referenceLogSortList;
	
	/**
	 * 需要保存用户关系日志的条数
	 */
	private int referenceLogCount = 20;

	private GamePlayer gamePlayer;
	
	private Object lock = new Object();
	
	public PlayerReference(GamePlayer gamePlayer) {
		
		this.gamePlayer = gamePlayer;
		
		this.slavesSortList = new ArrayList<Integer>();
		
		this.referenceLogSortList = new ArrayList<UserReferenceLogInfo>();
	}

	/**
	 * 获取用户当前拥有的奴隶
	 * @return
	 */
	public List<UserReferenceInfo> getUserCurrentSlaves() {
		synchronized (slavesSortList) {
			return ReferenceMgr.getUserReferenceInfoByList(slavesSortList);
		}
	}

	/**
	 * 获取用户当前拥有的奴隶信息的类型
	 * @return
	 */
	public byte getOneSlaveTypeByReferenceId(int referenceId) {
		UserReferenceInfo info = getSlavesByReferenceId(referenceId);
		return getOneSlaveTypeByReference(info);
	}

	/**
	 * 获取奴隶信息的类型
	 * 其中 1 表示 是   0 表示 否
	 * 		是否为自己的奴隶	是否有税金	是否可以惩罚
	 * 1 	1				1			1				
	 * 2	1				1			0				
	 * 3	1				0			1				
	 * 4	1				0			0				
	 * 5	0				1			0				
	 * 6	0				0			0				//TODO
	 * @return
	 */
	public byte getOneSlaveTypeByReference(UserReferenceInfo info) {
		/** 1现在还是属于自己的奴隶并且有奖励  
		 *  2现在还是属于自己的奴隶当没奖励   
		 *  3自己的奴隶但是不能领取也不能惩罚 
		 *  4 过期的也没税金的  
		 *  5过期奴隶但是可领取税金 
		 *  OLD 老版本的 时间20120726
		 */
		boolean haveIncome = false;//是否有税金
		boolean canPenalize = true;//是否可以惩罚
		byte slaveType = 6;//状态
		if(null == info) {
			return slaveType;
		}
		Timestamp validityTime = getOldSlaveValidityTime();
		if (info.getTakeUserId() == 0 || info.getRemoveTime() == null) {
			//判断是否可以征税
			if(info.getIncomeCoins() > 0) {
				haveIncome = true;
			}
			//判断是否可以惩罚
			Timestamp lastDecideTime =  getOldLogValidityTime();//上一次惩罚的最晚时间
			for(UserReferenceLogInfo LogInfo : referenceLogSortList) {
				if(LogInfo.getLogType() == 3 && lastDecideTime.before(LogInfo.getLogCreateTime())//在更晚时间如果惩罚过的话
						&& LogInfo.getPassivesUserId() == info.getSlavesUserId()) {
					canPenalize = false;
				}
			}
			if(canPenalize) {
				if(haveIncome) {
					slaveType = 1;
				} else {
					slaveType = 3;
				}
			} else {
				if(haveIncome) {
					slaveType = 2;
				} else {
					slaveType = 4;
				}
			}
		}else if(info.getIncomeCoins() > 0 && info.getRemoveTime().after(validityTime)) {//已经被抢走了的奴隶,还有奖励的
			slaveType = 5;
		} else {
			slaveType = 4;
		}
		return slaveType;
	}
	
	/**
	 * 获取用户当前拥有的奴隶最晚过期时间
	 * @return
	 */
	public Timestamp getOldSlaveValidityTime() {
		return new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 *1000);//一天时间
	}
	
	/**
	 * 获取用户当前拥有的日志最晚过期时间（是在判断是否可以再次折磨奴隶的时候用的）
	 * @return
	 */
	public Timestamp getOldLogValidityTime() {
		return new Timestamp(System.currentTimeMillis() - 15 * 60 *1000);//十五分钟
	}

	/**
	 * 获取用户当前拥有的还没有被抢走的奴隶
	 * @return
	 */
	public List<UserReferenceInfo> getUserCurrentNowSlaves() {
		synchronized (referenceLogSortList) {
			List<UserReferenceInfo> nowSlaves = null;//现在还是属于自己的奴隶
			List<UserReferenceInfo> list = getUserCurrentSlaves();
			if (null != list && list.size() > 0) {
				nowSlaves = new ArrayList<UserReferenceInfo>();//已经被抢走了的奴隶
				for (UserReferenceInfo info : list) {
					if (null == info) {
						continue;
					}
					if (info.getRemoveTime() == null) {
						nowSlaves.add(info);//现在还是属于自己的奴隶
					}
				}
			} 
			return nowSlaves;
		}
	}

	/**
	 * 获取用户当前已经被抢走了的奴隶，但是还有税金没有领取的
	 * @return
	 */
	public List<UserReferenceInfo> getUserCurrentOldAndHaveTaxesSlaves() {
		synchronized (referenceLogSortList) {
			List<UserReferenceInfo> oldSlaves = null;//现在还是属于自己的奴隶
			List<UserReferenceInfo> list = getUserCurrentSlaves();
			if (null != list && list.size() > 0) {
				oldSlaves = new ArrayList<UserReferenceInfo>();//已经被抢走了的奴隶,还有奖励的
				Timestamp validityTime = getOldSlaveValidityTime();
				for (UserReferenceInfo info : list) {
					if (null == info) {
						continue;
					}
					if(info.getIncomeCoins() > 0 && (null != info.getRemoveTime() && info.getRemoveTime().after(validityTime))) {//还有奖励的
						oldSlaves.add(info);//已经被抢走了的奴隶
					}
				}
			} 
			return oldSlaves;
		}
	}
	
	/**
	 * 获取用户当前拥有的奴隶关系最新日志有限制的取出referenceLogCount条
	 * @param 
	 * @return List<UserReferenceLogInfo>
	 */
	public List<UserReferenceLogInfo> getUserReferenceLogListLimit() {
		if(null == referenceLogSortList || referenceLogSortList.size() <= referenceLogCount) {
			return referenceLogSortList;
		}
		List<UserReferenceLogInfo> tempList = new ArrayList<UserReferenceLogInfo>();
		synchronized (referenceLogSortList) {
			int index = 1;
			for(UserReferenceLogInfo info : referenceLogSortList) {
				index++;
				tempList.add(info);
				if(index > referenceLogCount) {//如果有了这么多则不用取了
					break ;
				}
			}
		}
		return tempList;
	}
	
	/**
	 * 获取用户当前拥有的奴隶关系最新日志
	 * @return List<UserReferenceLogInfo>
	 */
	public List<UserReferenceLogInfo> getUserReferenceLogList() {
		synchronized (referenceLogSortList) {
			return referenceLogSortList;
		}
	}

	/**
	 * 查询我的主人的关系信息
	 * @return UserReferenceInfo
	 */
	public UserReferenceInfo getMasterUserReferenceInfo() {
		return ReferenceMgr.getUserReferenceInfoByUserReferenceId(masterReferenceId);
	}

//	/**
//	 * List增加奴隶 原来的有50条限制的
//	 * @return
//	 */
//	public void addSlaves(int slavesId) {
//		synchronized (slavesSortList) {
//			if(null != slavesSortList && slavesSortList.size() >= 50) {
//				int userReferenceId = slavesSortList.remove(slavesSortList.size() - 1);//需要移除最老的一条
//				slavesSortList.add(0, slavesId);
//				ReferenceMgr.removeReferenceByKeyId(userReferenceId);//查看奴隶关系管理器中是否需要移除
//			}
//			slavesSortList.add(slavesId);
//		}
//	}

	/**
	 * List增加奴隶
	 * @return
	 */
	public void addSlaves(int slavesId) {
		if(null == slavesSortList) {
			return ;
		}
		synchronized (slavesSortList) {
			if( ! slavesSortList.contains(slavesId)) {//避免重复的。在玩家重现登录时，机器人可能有重复加了
				slavesSortList.add(0, slavesId);//新加最新的奴隶数据
			}
		}
	}
	
	public List<UserReferenceLogInfo> getOldMasterReferenceIdList() {
		return oldMasterReferenceIdList;
	}

	/**
	 * List增加老的奴隶主信息 奴隶关系ID
	 * @return
	 */
	public void addOldMasterReferenceIdList(UserReferenceLogInfo oldMasterReferenceId) {
		if(null == oldMasterReferenceIdList) {
			oldMasterReferenceIdList = new ArrayList<UserReferenceLogInfo>();
		}
		oldMasterReferenceIdList.add(oldMasterReferenceId);
	}

	/**
	 * List增加奴隶日志数据（有50条限制的现在没限制，但都是有过期时间 ）
	 * @return
	 */
	public void addUserReferenceLog(UserReferenceLogInfo userReferenceLogInfo) {
		if(null == referenceLogSortList) {
			return ;
		}
		synchronized (referenceLogSortList) {
			referenceLogSortList.add(0, userReferenceLogInfo);//新加最新的奴隶日志数据
		}
	}

	/**
	 * 关系管理增加奴隶。返回新增关系的ID
	 * @param successId 
	 * @return userReferenceId 
	 */
	private int addPlayerSlave(GamePlayer successPlayer,int winCharmValve, int workType) {
		int userReferenceId = 0;
		try {
			if (null == gamePlayer || successPlayer == null) {
				return userReferenceId;
			}
			UserReferenceInfo info = new UserReferenceInfo();
			Timestamp time = new Timestamp(System.currentTimeMillis());
			int userId = gamePlayer.getUserId();
			info.setMasterUserId(successPlayer.getUserId());
			info.setMasterUserName(successPlayer.getPlayerInfo().getUserName());
			info.setMasterPicPath(successPlayer.getPlayerInfo().getPicPath());
			info.setSlavesUserId(userId);
			info.setSlavesUserName(gamePlayer.getPlayerInfo().getUserName());
			info.setSlavesPicPath(gamePlayer.getPlayerInfo().getPicPath());
			info.setSlavesMoney(gamePlayer.getPlayerInfo().getMoney());
			info.setSlavesCoins(gamePlayer.getPlayerInfo().getCoins());
			info.setSlavesSign(gamePlayer.getPlayerInfo().getSign());
			info.setSlavesPos(gamePlayer.getPlayerInfo().getPos());
			info.setWin(gamePlayer.getPlayerInfo().getWin());
			info.setLose(gamePlayer.getPlayerInfo().getLose());
	        info.setCity(gamePlayer.getPlayerInfo().getCity());
	        info.setLevel(gamePlayer.getPlayerInfo().getLevel());
	        info.setSex(gamePlayer.getPlayerInfo().getSex());
	        info.setIncomeCharmValve(winCharmValve);
	        info.setIncomeExp(workType);
	        info.setSlavesCharmValve(gamePlayer.getPlayerInfo().getCharmValve());
	        info.setSlavesGP(gamePlayer.getPlayerInfo().getGp());
	        info.setSlavesLastLoginDate(gamePlayer.getPlayerInfo().getLastLoginDate());
			info.setCreateTime(time);
			info.setOp(Option.INSERT);
			userReferenceId = ReferenceMgr.addReference(info);//存入HashMap
			if (userReferenceId > 0) {
				this.setMasterReferenceId(userReferenceId); //重新设置我的主人信息
				successPlayer.getPlayerReference().addSlaves(userReferenceId);//如果赢的用户增加奴隶
				gamePlayer.getOut().sendUserMasterInfo(successPlayer.getUserId());//通知客户端更新奴隶
			}
			//建立关于新的日志
			addPlayerSlaveBuildReferenceLog(info, time);
		} catch (Exception e) {
			logger.error("[ UserReferenceInfo : addPlayerSlave ]", e);
		} finally {
			
		}
		return userReferenceId;
	}
	
	
	public int checkRefWork(UserReferenceInfo MasterRef)
	{
		int needSecond = 0;
		if(MasterRef.getIncomeExp() > 0)
		{
			UserRefWorkInfo work = ReferenceMgr.getWorkById(MasterRef.getIncomeExp());
			
			Timestamp timeNow = new Timestamp(System.currentTimeMillis());
			long totalTime =  (long)work.getWorkTime() * 3600 * 1000;
			Timestamp timeEnd = new Timestamp((MasterRef.getCreateTime().getTime() + totalTime));
			
			if(timeNow.after(timeEnd))
			{								
				MasterRef.setIncomeCoins(work.getWorkCoin() * work.getWorkTime() * -1);
				MasterRef.setIncomeCharmValve(10000);
				MasterRef.setIncomeExp(MasterRef.getIncomeExp()*-1);
//				MasterRef.setRemoveTime(timeNow);	
				MasterRef.setOp(Option.UPDATE);
				
				needSecond = 0;
			}				
			else
			{
				needSecond = (int)((totalTime - (timeNow.getTime() - MasterRef.getCreateTime().getTime()))/1000);
			}
		}
		
//		System.out.println("checkRefWork==================" + MasterRef.getId() + "===========" + MasterRef.getIncomeExp() + "===========" + MasterRef.getIncomeCoins());
		
		return needSecond;		
	}

	/**
	 * 建立关于新的日志
	 * @return 
	 */
	private void addPlayerSlaveBuildReferenceLog(UserReferenceInfo info, Timestamp time) {
		UserReferenceLogInfo logInfo  = new UserReferenceLogInfo();
		logInfo.setOwnerUserId(info.getMasterUserId());
		logInfo.setPassivesUserId(info.getSlavesUserId());
		logInfo.setRefId(info.getId());
		logInfo.setLogCreateTime(time);
		logInfo.formatToContent(0, info.getSlavesUserName());
		logInfo.setOp(Option.INSERT);
		//System.out.println("addPlayerSlaveBuildReferenceLog======" + logInfo);
		GamePlayer masterPlayer = WorldMgr.getPlayerByID(info.getMasterUserId());
		if(null != masterPlayer) {//如果原来的奴隶主在线的话，给他加上日志
			masterPlayer.getPlayerReference().addUserReferenceLog(logInfo);
		} else {//如果原来的奴隶主在线的话，存入到关系管理器中去。以便于定时存入数据库中去
			if( ! RobotMgr.isRobotByID(info.getMasterUserId())) {//（机器人没有）
				ReferenceMgr.addTempUserReferenceLog(logInfo);
			}
		}
	}
	
	/**
	 * 变更奴隶主
	 * @return 是否成功 
	 */
	@SuppressWarnings("unused")
	private boolean updatePlayerMaster(GamePlayer successPlayer) {
		//System.out.println("============进入到更新主人的信息里面===========successPlayer");
		return updatePlayerMaster(successPlayer, getMasterUserReferenceInfo());
	}
	
	/**
	 * 变更奴隶主（就是抢奴隶）
	 * @return 是否成功 
	 */
	public boolean updatePlayerMaster(GamePlayer successPlayer, UserReferenceInfo info) {
		try {
			if (null == gamePlayer || null == successPlayer) {
				return false;
			}
			if (null != info) {
				Timestamp time = new Timestamp(System.currentTimeMillis());
//				info.setRemoveTime(time);
				info.setTakeUserId(successPlayer.getUserId());
				info.setTakeUserName(successPlayer.getPlayerInfo().getUserName());
				info.setTakeUserPicPath(successPlayer.getPlayerInfo().getPicPath());
				info.setOp(Option.UPDATE);
				//建立关于得到奴隶的日志
				takeBuildReferenceLog(info, time);
				ReferenceMgr.subSlaveCount(info.getMasterUserId());
			}
			ReferenceMgr.addSlaveCount(successPlayer.getUserId());
			//TODO 更新完奴隶信息还要检测原来的奴隶主是否在线,如果在线的话需要减少他的奴隶数量
		} catch (Exception e) {
			logger.error("[ UserReferenceInfo : removePlayerSlave ]", e);
			return false;
		} finally {
			
		}
		return true;
	}
	
	/**
	 * 建立关于抢走奴隶的日志
	 * @return 
	 */
	private void takeBuildReferenceLog(UserReferenceInfo info, Timestamp time) {
		UserReferenceLogInfo logInfo  = new UserReferenceLogInfo();
		logInfo.setOwnerUserId(info.getMasterUserId());
		logInfo.setPassivesUserId(info.getSlavesUserId());
		logInfo.setRefId(info.getId());
		logInfo.setLogCreateTime(time);
		logInfo.formatToContent(1, info.getTakeUserName(), info.getSlavesUserName());
		logInfo.setOp(Option.INSERT);
		//System.out.println("takeBuildReferenceLog======" + logInfo);
		GamePlayer masterPlayer = WorldMgr.getPlayerByID(info.getMasterUserId());
		if(null != masterPlayer) {//如果原来的奴隶主在线的话，给他加上日志
			masterPlayer.getPlayerReference().addUserReferenceLog(logInfo);
		} else {//如果原来的奴隶主在线的话，存入到关系管理器中去。以便于定时存入数据库中去
			if( ! RobotMgr.isRobotByID(info.getMasterUserId())) {//（机器人没有）
				ReferenceMgr.addTempUserReferenceLog(logInfo);
			}
		}
	}

	/**
	 * 在玩家被灌醉的时候更改奴隶主
	 * @return 
	 */
	public void changMaster(int successPlayerId,int winCharmValve, int workType) {
		GamePlayer successPlayer = WorldMgr.getPlayerByID(successPlayerId);
		//奴隶主为机器人是有可能的
		if (null == successPlayer) {
			Player robot = RobotMgr.getRobotByID(successPlayerId);
			if (null != robot) {
				successPlayer = robot.getPlayerDetail();
			}
		}
		//TODO 这里还有一种情况,如果用户刚胜利就掉线了,这里待处理
		if (null != successPlayer) {
//			//首先我的主人的关系id 不为0，表明我是有主人的，需要去更新信息
//			if (masterReferenceId != 0) {
//				UserReferenceInfo info = ReferenceMgr.getUserReferenceInfoByUserReferenceId(masterReferenceId);
//				if (null != info) {
////					if (info.getMasterUserId() != successPlayerId) {//赢我的人不是我的主人的时候需要更新
//						updatePlayerMaster(successPlayer);//先更新奴隶关系
////					} else {
////						return;
////					}
//				}
//			}
//			//如果我没有主人或者赢的用户不是我的主人就有可能是我的奴隶CSF
//			//如果是我的奴隶也需要更新
//			int successPlayerMasterReferenceId = successPlayer.getPlayerReference().getMasterReferenceId();
//			if (successPlayerMasterReferenceId != 0) {
//				UserReferenceInfo info = ReferenceMgr.getUserReferenceInfoByUserReferenceId(successPlayerMasterReferenceId);
//				if (null != info) {
//					if(info.getMasterUserId() == this.gamePlayer.getUserId()) {
//						updatePlayerMaster(successPlayer, info);
//						//当传递过来的关系对象中,我的主人的ID和我的ID相同,则说明更新的是我的奴隶的一条关系,需要解除奴隶的主人关系--赋值为0
////						successPlayer.getPlayerReference().setMasterReferenceId(0);
////						successPlayer.getOut().sendUserMasterInfo(0);
//					}
//				}
//			}
			//需要增加一条信息
			this.masterReferenceId = addPlayerSlave(successPlayer,winCharmValve, workType);//再增加奴隶关系
		}
	}

	/**
	 * 加载玩家的所有相关关系
	 * @param userId
	 * @return
	 */
	public void loadUserReferences() {
//		loadUserReferenceLogs();//先加载奴隶关系日志（不是过期状态的）
//		takeBackMasterNotCollectTaxes(0);//取回没收取税金
		
//		List<UserReferenceInfo> list = ReferenceBussiness.getAllUserReferencesLimit(gamePlayer.getPlayerInfo().getUserId(), 50);//查出所有玩家的奴隶取最新的50条
		Timestamp validityTime = getOldSlaveValidityTime();
		List<UserReferenceInfo> list = ReferenceBussiness.getUserAllNowValidReferencesLimit(gamePlayer.getPlayerInfo().getUserId(), validityTime, 0);//查出所有玩家有效的奴隶取
		synchronized (lock) {
	    	if(null != list && list.size() > 0) {
	    		//System.out.println("PlayerReference loadUserReferences()===查询中的我的奴隶的集合大小为:===" + list.size());
	    		//加载用户相关的关系
				List<Integer> tempList = ReferenceMgr.loadUserReferences(list);
				if(null != tempList) {
					slavesSortList.addAll(tempList);
				}
			}
    	}
		UserReferenceInfo ramInfo = ReferenceMgr.getMasterInfoByUserId(gamePlayer.getPlayerInfo().getUserId());//查出玩家最新的主人取最新
		UserReferenceInfo dBInfo = ReferenceBussiness.getMasterInfoByUserId(gamePlayer.getPlayerInfo().getUserId());//查出玩家最新的主人取最新
		UserReferenceInfo info = null;;//取比较新的
		if(null == ramInfo && null != dBInfo) {
			info = dBInfo;
		} else if(null != ramInfo && null == dBInfo) {
			info = ramInfo;
		} else if(null != ramInfo && null != dBInfo
				&& null != ramInfo.getCreateTime() && null != dBInfo.getCreateTime()) {
			info = ramInfo.getCreateTime().after(dBInfo.getCreateTime()) ? ramInfo : dBInfo;
		}
		
		if(null != info) {
			if (info.getTakeUserId() == 0 && info.getRemoveTime() == null) {
				masterReferenceId = ReferenceMgr.loadUserReference(info);
				//TODO 如果是机器人，给奴隶主的奴隶列表加上这条奴隶信息
				int masterPlayerId = info.getMasterUserId();
				GamePlayer masterPlayer = null;
				Player robot = RobotMgr.getRobotByID(masterPlayerId);
				if (null != robot) {
					masterPlayer = robot.getPlayerDetail();
				}
				if(null != masterPlayer) {
					masterPlayer.getPlayerReference().addSlaves(masterReferenceId);
				}
			} else {
				masterReferenceId = 0;
			}
		}
		if(null != gamePlayer && null != gamePlayer.getPlayerInfo()) {
			gamePlayer.getPlayerInfo().setSlaveCount(getCurrentSlavesCount());//初始化奴隶数
		}
		//System.out.println("PlayerReference loadUserReferences()===登陆查询出的奴隶主的关系id:===" + masterReferenceId);
	}

	/**
	 * 加载玩家的所有自己原来主人过期了但是还没收取税金的信息。并取回没收取税金
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unused")
	private void takeBackMasterNotCollectTaxes(int count) {
		Timestamp validityTime = getOldSlaveValidityTime();
		List<UserReferenceInfo> list = ReferenceBussiness.getUserAllNowValidMasterInfoByUserId(gamePlayer.getPlayerInfo().getUserId(), validityTime, 0);
		if(null == list || list.size()<0) {
			return ;
		}
		for(UserReferenceInfo info : list) {
			int taxes = info.getIncomeCoins();
			if(taxes > 0) {
				gamePlayer.addCoins(taxes);
				info.setIncomeCoins(0);
				info.setOp(Option.UPDATE);
			}
		}
		ReferenceMgr.saveIntoDBByList(list);//存到数据库中去
	}

	/**
	 * 加载玩家的所有相关关系日志
	 * @param count
	 * @return
	 */
	public void loadUserReferenceLogs() {
		List<UserReferenceLogInfo> onRAMRefList = ReferenceMgr.getUserReferenceLogByUserId(gamePlayer.getPlayerInfo().getUserId());
		List<UserReferenceLogInfo> onDBRefList = ReferenceLogBussiness.getUserAllReferencesLogLimit(gamePlayer.getPlayerInfo().getUserId(), 0);//查出所有玩家的奴隶取最新的50条
		synchronized (referenceLogSortList) {
	    	if(null != onRAMRefList && onRAMRefList.size() > 0) {
	    		//System.out.println("PlayerReference在RAM查询中的我的奴隶关系的集合大小为:===" + onRAMRefList.size());
	    		referenceLogSortList.addAll(onRAMRefList);
			}
	    	if(null != onDBRefList && onDBRefList.size() > 0) {
	    		//System.out.println("PlayerReference在DB查询中的我的奴隶关系的集合大小为:===" + onDBRefList.size());
	    		/*
	    		 * 如果日志数量大于了需要显示的数量。那么我们可以去把没有用的过期数据改成过期数据，并存到数据库
	    		 * 过期数据（客户端不需要显示，而且服务器端不必来判断是否可以马上继续惩罚（惩罚有冷却时间））
	    		 * 过期数据下次不再会从数据库中查询出来（onRAMRefList不会过期数据（每十分钟存一次））
	    		 */
	    		int index = 0;
		    	for(UserReferenceLogInfo logInfo : onDBRefList)  {
		    		if(index < referenceLogCount) {
			    		referenceLogSortList.add(logInfo);
		    		} else {
			    		if(logInfo.getLogCreateTime().before(getOldLogValidityTime())) {//无效数据
			    			logInfo.setLogType(-1);//过期数据
			    			logInfo.setOp(Option.UPDATE);
			    			ReferenceMgr.addTempUserReferenceLog(logInfo);
			    		} else {
			    			referenceLogSortList.add(logInfo);
			    		}
		    		}
		    		index ++;
		    	}
			}
    	}
	}
	
	/**
	 * 查看当前用户是否有某一条奴隶关系
	 * @return 是否有
	 */
	public boolean isHaveSlavesByReferenceId(int userReferenceId) {
		return slavesSortList.contains(userReferenceId);
	}
	
	/**
	 * 通过关系ID的到当前用户某一条奴隶关系
	 * @return 奴隶关系
	 */
	public UserReferenceInfo getSlavesByReferenceId(int userReferenceId) {
		UserReferenceInfo info = null;
		if(isHaveSlavesByReferenceId(userReferenceId)) {
			info = ReferenceMgr.getUserReferenceInfoByUserReferenceId(userReferenceId);
		}
		return info;
	}
	
	/**
	 * 当玩家退出时后，移除相关关系信息
	 * @return 
	 */
	public void clearUserReference() {
		clearUserReferenceLog();//先移除掉所有的奴隶关系日志
		
		List<Integer> tempList = new ArrayList<Integer>();
		
		if (masterReferenceId != 0) {
			if (null != slavesSortList) {
				tempList.add(masterReferenceId);
				//slavesSortList.add(masterReferenceId);//移除主人关系信息
				//slavesSortList.addAll(tempList);
			}
		}
		if(null != oldMasterReferenceIdList && oldMasterReferenceIdList.size() > 0) {
			tempList.addAll(slavesSortList);
		}
		if(null != slavesSortList && slavesSortList.size() > 0) {
			tempList.addAll(slavesSortList);
		}
		ReferenceMgr.removeReferenceByKeyList(tempList);//移除奴隶关系信息
	}
	
	/**
	 * 当玩家退出时后，移除相关关系日志的信息
	 * @return 
	 */
	public void clearUserReferenceLog() {
		saveUserReferenceLogToDB();
		referenceLogSortList.clear();
	}
	
	/**
	 * 相关关系日志的信息存储到数据库
	 * @return 
	 */
	private void saveUserReferenceLogToDB() {
		if(null == referenceLogSortList || referenceLogSortList.size() <= 0) {
			return ;
		}
		synchronized (referenceLogSortList) {
			ReferenceMgr.saveReferenceLogIntoDBByList(referenceLogSortList);
		}
	}

	public int getMasterReferenceId() {
		return masterReferenceId;
	}

	public void setMasterReferenceId(int masterReferenceId) {
		this.masterReferenceId = masterReferenceId;
	}
	
	/**
	 * 获得玩家的奴隶数
	 * @return
	 */
	public int getCurrentSlavesCount() {
		int result = 0;
		List<UserReferenceInfo> list = ReferenceMgr.getUserReferenceInfoByList(slavesSortList);
		if (null != list && list.size() > 0) {
			for (UserReferenceInfo info : list) {
				if (info.getTakeUserId() == 0 || info.getRemoveTime() == null) {
					result++;
				}
			}
		}
		return result;
	}
	
	/**
	 * 相关关系信息存储到数据库
	 * @return 
	 */
	public void saveLogIntoDb() {
		saveUserReferenceLogToDB();
	}
}
