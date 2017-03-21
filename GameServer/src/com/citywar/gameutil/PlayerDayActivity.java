package com.citywar.gameutil;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.bll.UserDayActivityBussiness;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.UserDayActivityInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.ItemMgr;
import com.citywar.type.PropType;

/**
 * 用户统计数据
 * @author shanfeng.cao
 *
 */
public class PlayerDayActivity {
	
    private static final Logger LOGGER = Logger.getLogger(ItemMgr.class.getName());

	private GamePlayer gamePlayer;
	
	private UserDayActivityInfo userDayActivity;
	
	public PlayerDayActivity(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}
	
	/**
	 * 加载玩家的用户统计数据
	 * @param userId
	 * @return
	 */
	public void loadUserDayActivity() {
		List<UserDayActivityInfo> userDayActivityInfos = 
				UserDayActivityBussiness.selectUserDayActivity(gamePlayer.getUserId());
		if(null != userDayActivityInfos && userDayActivityInfos.size() > 0) {
			UserDayActivityInfo userDayActivityInfo = null;
			for(int i=0;i<userDayActivityInfos.size();i++) {
				userDayActivityInfo = userDayActivityInfos.get(i);
				if(0 == i) {
					userDayActivity = userDayActivityInfo;
					userDayActivity.setCountDate(new Timestamp(System.currentTimeMillis()));
					userDayActivity.setOp(Option.UPDATE);
				} else {//删除多余的活动日志
					UserDayActivityBussiness.deleteUserDayActivity(userDayActivityInfo.getId());
				}
			}
		} else if(null != gamePlayer) {
			userDayActivity = new UserDayActivityInfo();
			userDayActivity.setUserId(gamePlayer.getUserId());
			userDayActivity.setCountDate(new Timestamp(System.currentTimeMillis()));
			userDayActivity.setOp(Option.INSERT);
		}
		int oneDayIncreaseCoins = userDayActivity.getIncreaseCoins();
		int nowCoins = gamePlayer.getPlayerInfo().getCoins();
		gamePlayer.getPlayerInfo().setYesterdayCoins(nowCoins - oneDayIncreaseCoins);
	}

	/**
	 * 将用户统计数据保存入数据库
	 * 
	 * @return
	 */
	public void saveIntoDb() {
		if(null != userDayActivity) {
			if(userDayActivity.getOp() == Option.INSERT) {
				UserDayActivityBussiness.insertUserDayActivity(userDayActivity);
			} else if(userDayActivity.getOp() == Option.UPDATE) {
				UserDayActivityBussiness.updateUserDayActivity(userDayActivity);
			}
			userDayActivity.setOp(Option.NONE);
		} else {
			LOGGER.error("PlayerDayActivity is null");
		}
	}
	
	/**
	 * 在玩家登陆的时候更新玩家的活动
	 */
	public void updateOnLogin() {
		if(null == userDayActivity) {
			return ;
		}
		Timestamp countDate = userDayActivity.getCountDate();
		if(countDate.before(GamePlayerUtil.getResetTime())) {//TODO
			userDayActivity.setPerCapitaTime(0);
			userDayActivity.setOp(Option.UPDATE);
		}
	}
	
	/**
	 * 在玩家退出的时候更新玩家的活动
	 */
	public void updateOnQiut() {
		if(null == userDayActivity || null == gamePlayer
				|| null == gamePlayer.getUserProperty()
				|| null == gamePlayer.getPlayerInfo()) {
			return ;
		}
		long userOnlineTime = computePlayerOnlineTime();
		long perCapitaTime = userDayActivity.getPerCapitaTime();
		userDayActivity.setPerCapitaTime(perCapitaTime + userOnlineTime);
		userDayActivity.setIncreaseCoins(gamePlayer.getPlayerInfo().getDayIncreaseCoins());
		gamePlayer.getPlayerInfo().setBuyItemCoins(0);
		userDayActivity.setOp(Option.UPDATE);
		saveIntoDb();
	}
	
	/**
	 * 记录玩家使用道具
	 * @param type
	 * @param count
	 */
	public void recordUsePropItemByType(int type, int count) {
		if(null == userDayActivity) {
			return ;
		}
		boolean needRecord = false;
		switch (type) {
		case PropType.WAKE_TYPE:
			userDayActivity.setUsageCounterTea(userDayActivity.getUsageCounterTea()
					+ count);
			needRecord = true;
			break;
		case PropType.TRUMPET_TYPE:
			userDayActivity.setUsageCounterHorn(userDayActivity.getUsageCounterTea()
					+ count);
			needRecord = true;
			break;
		default:
			break;
		}
		if(needRecord) {
			userDayActivity.setOp(Option.UPDATE);
		}
	}
	
	/**
	 * 获得用户的累积在线长（在数据分析统计中用到的）
	 * @return
	 */
	public long getUserOnlineTime() {
		long onlineTime = 0;
		if(null != userDayActivity) {
			onlineTime = userDayActivity.getPerCapitaTime();
		}
		return onlineTime;
	}
	
	/**
	 * 获得用户的累积在线长（防沉迷系统中用到的）
	 * @return
	 */
	public long getAccumulateOnlineTime() {
		long onlineTime = 0;
		if(null != userDayActivity) {
			onlineTime = userDayActivity.getPerCapitaTime() + computePlayerOnlineTime();
		}
		return onlineTime;
	}
	
	/**
	 * 计算玩家的这次登陆在线时间
	 */
	public long computePlayerOnlineTime() {
		if(null == gamePlayer
				|| null == gamePlayer.getPlayerInfo()
				|| null == gamePlayer.getPlayerInfo().getLastLoginDate()) {
			return 0;
		}
		Timestamp countDate = new Timestamp(System.currentTimeMillis());
		long userOnlineTime = (countDate.getTime() - 
				gamePlayer.getPlayerInfo().getLastLoginDate().getTime()) / 1000;
		return userOnlineTime;
	}
}
