package com.citywar.gameutil;

import java.sql.Timestamp;
import java.util.Calendar;

import com.citywar.bll.FriendsBussiness;
import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.manager.AllAroundPlayerInfoMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.WorldMgr;

public class GamePlayerUtil {

	/** 正在游戏 */
	private static byte PLAY_GAME = 1;
	/** 不在游戏  */
	private static byte NO_GAME = 2;
	/** 不在线 */
	private static byte OFF_LINE = 3;

	/**
	 * 玩家当前状态(包括机器人)
	 * 
	 * @return 正在游戏 1， 不在游戏 2 不在线3
	 */
	public static byte gamePlayerState(int gamePlayerId) {
		
		byte isPlaying = OFF_LINE;
		GamePlayer gp = WorldMgr.getPlayerByID(gamePlayerId);
		if (null == gp) {
			Player pl = RobotMgr.getOnDutyRobotByID(gamePlayerId);
			if (null != pl) {
				isPlaying = pl.isPlaying() ? PLAY_GAME : NO_GAME;
			}
		} else {
			isPlaying = gp.getIsPlaying() ? PLAY_GAME : NO_GAME;
		}
		return isPlaying;
	}

	/**
	 * 玩家是否在线 (包括机器人)
	 * 
	 * @param gamePlayerId
	 * @return true 在线                  false不在线
	 */
	public static boolean isOnLine(int gamePlayerId) {
		byte gameState = gamePlayerState(gamePlayerId);
		if (gameState == OFF_LINE)
			return false;
		return true;
	}

    /**
     * 得到在线玩家(包括机器人)
     * 	
     * @param gamePlayerId
     * @return null表示玩家不在线
     */
	public static GamePlayer getOnLineGamePlayer(int gamePlayerId) {

		GamePlayer gp = WorldMgr.getPlayerByID(gamePlayerId);

		if (null == gp) {
			// 从机器人里面取,值班的
			Player pl = RobotMgr.getRobotByID(gamePlayerId);
			if(null != pl) {
				gp = pl.getPlayerDetail();
			}
		}
		return gp;
	}
	/**
	 * 得到玩家playerInfo(包括机器人的)
	 * 先从内存中取
	 * 内存中取不到 就从数据库中取
	 * @param userId
	 * @param isSearchDB 是否查库
	 * @return PlayerInfo 最新的数据      null 未知的userId
	 */
	public static PlayerInfo getPlayerInfo(int userId,boolean isSearchDB){
        PlayerInfo user = null;
		
		GamePlayer gp = WorldMgr.getPlayerByID(userId);
    	if(null != gp)
    	{
    		user = gp.getPlayerInfo();
    	}
    	else
    	{
    		Player rp = RobotMgr.getRobotByID(userId);
    		if(null != rp)
    		{
    			user = rp.getPlayerDetail().getPlayerInfo();
    		}
    	}
    	// 在线的用户没用，从离线不超时的用户中取
    	if(null == user && isSearchDB)
    	{
    		user = AllAroundPlayerInfoMgr.getPlayerInfoById(userId);
    	}
    	// 内存中都没有，只能找数据库，应该不会存在这种可能
    	if(null == user && isSearchDB)
    	{
    		user = PlayerBussiness.getPlayerInfoById(userId);
    	}
    	return user;
		
	}
	/**
	 * 判断用户是否是好友
	 */
	public static boolean isFriend(int userId,int friendId)
	{
		GamePlayer gameplayer = getOnLineGamePlayer(userId);
		if(gameplayer != null){
			return gameplayer.getFriends().containsKey(friendId);
		}else{
			return FriendsBussiness.isFriend(userId, friendId);
		}
	}
	/**
	 * 获得系统的各种重置时间，游戏中新的一天
	 * 
	 * @param
	 * @return
	 */
	public static Timestamp getResetTime(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 4);//开始时间为凌晨四点开始算
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		//判断它的第一个任务是不是今天凌晨四点之后得到的，如果是那就不用推新的了
		return new Timestamp(c.getTimeInMillis());
	}
	
	
	/**
	 * 获得系统的各种重置时间，游戏中新的一天
	 * 
	 * @param
	 * @return
	 */
	public static Timestamp getTaskResetTime(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);//开始时间为凌晨四点开始算
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		//判断它的第一个任务是不是今天凌晨四点之后得到的，如果是那就不用推新的了
		return new Timestamp(c.getTimeInMillis());
	}
	
}
