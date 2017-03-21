package com.citywar.gameutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.citywar.bll.UserGiftBussiness;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.UserGiftInfo;
import com.citywar.dice.entity.UserGiftIntegrationInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.UserGiftMgr;

/**
 * 玩家收到的礼物
 * 
 * @author shanfeng.cao
 * @date 2011-06-26
 * @versions
 */
public class PlayerGift {

//	private static final Logger logger = Logger.getLogger(PlayerGift.class.getName());
	/**
	 * 拥有的玩家
	 */
	private GamePlayer gamePlayer;
	/**
	 * 玩家收到的礼品列表
	 */
	private List<UserGiftInfo> userGiftList;
	/**
	 * 玩家收到的礼品整合列表
	 */
	private Map<Integer, UserGiftIntegrationInfo> userGiftIntegrationMap;

	private Object lock = new Object();

	public PlayerGift(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
		userGiftList = new ArrayList<UserGiftInfo>();
		userGiftIntegrationMap = new HashMap<Integer, UserGiftIntegrationInfo>();
	}

	/**
	 * 获取用户当前拥有的所有礼品信息
	 * 
	 * @return List<UserGiftInfo>
	 */
	public List<UserGiftInfo> getUserAllGift() {
		if(null == userGiftList) {
			return null;
		}
		List<UserGiftInfo> tempList = new ArrayList<UserGiftInfo>();
		synchronized (lock) {
			for (UserGiftInfo info : userGiftList) {
				tempList.add(info);
			}
		}
		return tempList;
	}

	/**
	 * 获取用户当前拥有的所有礼品的整合信息
	 * 
	 * @return List<UserGiftIntegrationInfo>
	 */
	public List<UserGiftIntegrationInfo> getUserAllGiftIntegration() {
		if(null == userGiftIntegrationMap || userGiftIntegrationMap.size() == 0) {
			return null;
		}
		List<UserGiftIntegrationInfo> tempList = new ArrayList<UserGiftIntegrationInfo>();
		synchronized (lock) {
			for (UserGiftIntegrationInfo info : userGiftIntegrationMap.values()) {
				tempList.add(info);
			}
		}
		return tempList;
	}

	/**
	 * 用户接收到礼品
	 * 
	 * @return
	 */
	public void receiveGift(UserGiftInfo info) {
		synchronized (lock) {
			userGiftList.add(0, info);
			//接受者增加魅力值
//		    int charmValve = GiftTemplateMgr.getGiftCharmValveById(info.getGiftId());
//		    int count = info.getCount();
//		    gamePlayer.addCharmValve(charmValve * count);//接受者增加魅力值
		    gamePlayer.saveIntoDatabase(true);//及时保存到数据库
			if (userGiftIntegrationMap.containsKey(info.getGiftId())) {//如果有了这种类型的，则数量加
				int sumCount = userGiftIntegrationMap.get(info.getGiftId())
						.getSumCount();
				userGiftIntegrationMap.get(info.getGiftId()).setSumCount(
						sumCount + info.getCount());
			} else {//如果没有了这种类型的，则新生成
				userGiftIntegrationMap.put(
						info.getGiftId(),
						new UserGiftIntegrationInfo(info.getGiftId(), info
								.getCount()));
			}
		}
	}

	/**
	 * 增加礼品到礼品List
	 * 
	 * @return
	 */
	public void userGiftListAddGifts(List<UserGiftInfo> info) {
		synchronized (lock) {
			userGiftList.addAll(0, info);
		}
	}

	/**
	 * 用户赠送礼品
	 * 
	 * @return
	 */
	public void sendPresents(GamePlayer player, UserGiftInfo info) {
		player.getPlayerGift().receiveGift(info);
	}

	/**
	 * 在用户登陆的时候，加载用户拥有的礼品
	 * 
	 * @return
	 */
	public void loadUserGifts() {
		PlayerGift playerGift = UserGiftMgr.getUserGiftByUserId(gamePlayer.getUserId(), true);
		if(null == playerGift) {
			return ;
		}
		playerGift.setGamePlayer(gamePlayer);
		gamePlayer.setPlayerGift(playerGift);
		this.close();//一定在最后面
	}

	/**
	 * 将用户礼品保存入数据库
	 * 
	 * @return
	 */
	public boolean saveIntoDb() {
		List<UserGiftInfo> updateGifts = new ArrayList<UserGiftInfo>();
		List<UserGiftInfo> insertGifts = new ArrayList<UserGiftInfo>();
		synchronized (lock) {
			for (UserGiftInfo info : userGiftList) {
				switch (info.getOp()) {
				case Option.INSERT:
					insertGifts.add(info);
					break;

				case Option.UPDATE:
					updateGifts.add(info);
					break;
				default:
					break;
				}
				info.setOp(Option.NONE);
			}
		}
		UserGiftBussiness.updateUserGifts(updateGifts);
		UserGiftBussiness.insertUserGifts(insertGifts);
		return true;
	}

	public GamePlayer getGamePlayer() {
		return gamePlayer;
	}

	public void setGamePlayer(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	public Map<Integer, UserGiftIntegrationInfo> getUserGiftIntegrationMap() {
		return userGiftIntegrationMap;
	}

	public void close() {
		this.gamePlayer = null;
		userGiftList = null;
		userGiftIntegrationMap = null;
	}
	/**
	 * 当玩家退出时后，移除相关礼品信息
	 * @return 
	 */
	public void clearPlayerGift() {
		saveIntoDb();
	}
}
