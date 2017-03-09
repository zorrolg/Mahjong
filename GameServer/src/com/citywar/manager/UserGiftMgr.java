package com.citywar.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.citywar.bll.UserGiftBussiness;
import com.citywar.dice.entity.UserGiftInfo;
import com.citywar.dice.entity.UserGiftIntegrationInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameutil.PlayerGift;

/**
 * 用户礼品管理器。现在改成了所有礼品 都需要及时入库
 * 为那些不在线的用户保存礼品
 * @author shanfeng.cao
 * 
 */
public class UserGiftMgr {
	
	/**
	 * 为那些不在线的用户保存礼品。每过一段时间再同时入库
	 * key：用户ID value：用户礼品
	 */
    private static Map<Integer, PlayerGift> alluserPlayerGift;

//	private static final Logger logger = Logger.getLogger(UserGiftMgr.class.getName());

	/**
	 * 用户礼品定时入库管理初始化
	 * 
	 * @return
	 */
	public static boolean init() {
		alluserPlayerGift = new HashMap<Integer, PlayerGift>();
		return true;
	}
	

	/**
	 * 把用户礼品保存到数据库中去
	 * 
	 * @return
	 */
	public static void clearAlluserPlayerGift() {
		synchronized (alluserPlayerGift) {
			alluserPlayerGift.clear();
		}
	}
	/*
	 * 增加不在线的用户礼品，为那些不在线的用户保存礼品。
	 */
	public static void addUserGift(UserGiftInfo info) {
	    PlayerGift playerGift = getUserGiftByUserId(info.getOwnerUserId(), false);
	    playerGift.receiveGift(info);
	}
	/*
	 * 取得用户的礼品信息
	 */
	public static PlayerGift getUserGiftByUserId(int userId, boolean isLogin) {
		synchronized (alluserPlayerGift) {
			if(alluserPlayerGift.containsKey(userId)) {//如果管理其中有
				PlayerGift userGift =  alluserPlayerGift.get(userId);
				if(isLogin) {
					alluserPlayerGift.remove(userId);
				}
				return userGift;
			}
		}
		GamePlayer gamePlayer = new GamePlayer(userId);
		PlayerGift playerGift = new PlayerGift(gamePlayer);
		gamePlayer.setPlayerGift(playerGift);
		loadUserGifts(playerGift);
		if( ! isLogin) {
			synchronized (alluserPlayerGift) {//加入到管理器中为用户保留一段时间
				alluserPlayerGift.put(userId, playerGift);
			}
		}
		return playerGift;
	}

	/**
	 * 加载用户拥有的礼品
	 * 
	 * @return
	 */
	private static void loadUserGifts(PlayerGift playerGift) {
		int userId = playerGift.getGamePlayer().getUserId();
		List<UserGiftInfo> tempList = UserGiftBussiness.getAllUserGift(
				userId, 50);// 暂时设定为显示50条
		playerGift.userGiftListAddGifts(tempList);
		List<UserGiftIntegrationInfo> userGiftIntegrationList = UserGiftBussiness
				.getAllUserGiftIdAndSumCount(userId);
		for (UserGiftIntegrationInfo info : userGiftIntegrationList) {
			playerGift.getUserGiftIntegrationMap().put(info.getGiftId(), info);
		}
	}
}
