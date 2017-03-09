package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author shanfeng.cao
 * @date 2012-07-25
 * 大厅类型类
 */
public class HallTypeInfo {
	
	private int hallTypeId;
	
	private String hallTypeName;
	
	
	private int hallType;
	
	private int gameType;
	
	private int playerCountMin;
	
	private int playerCountMax; 
	/**
	 *  最低赌注
	 */
	private int wager;
	
	/**
	 *  加入大厅玩家最低具有金币数
	 */
	private int lowestCoins;
	
	/**
	 *  加入大厅玩家最低具有金币数
	 */
	private int highestCoins;
	
	/**
	 *  大厅快速游戏玩家最低金币数
	 */
	private int quickGameLowest;
	
	/**
	 *  大厅快速游戏玩家最高金币数
	 */
	private int quickGameHighest;
	
	/**
	 *  系统收税提成
	 */
	private int systemTaxes;
	
	/**
	 *  强退扣除的金币数
	 */
	private int forcedExitCoins;
	
	/** 首次开启 该大厅的最少金币数 */
	private int firstCoins;
	
	/**
	 *  大厅名字图片地址
	 */
	private String nameImage;
	
	private int robRoomNum = 0;
	
	public HallTypeInfo() {}
	
	public HallTypeInfo(ResultSet rs) throws SQLException{
		this.setHallTypeId(rs.getInt("hall_type_id"));
		this.setHallTypeName(rs.getString("hall_type_name"));
		
		this.setHallType(rs.getInt("hall_type"));
		this.setGameType(rs.getInt("game_type"));
		this.setPlayerCountMin(rs.getInt("player_count_min"));
		this.setPlayerCountMax(rs.getInt("player_count_max"));
		
		this.setWager(rs.getInt("wager"));
		this.setLowestCoins(rs.getInt("lowest_coins"));
		this.setHighestCoins(rs.getInt("highest_coins"));
		
		this.setQuickGameLowest(rs.getInt("quick_game_lowest"));
		this.setQuickGameHighest(rs.getInt("quick_game_highest"));
		this.setSystemTaxes(rs.getInt("system_taxes"));
		this.setForcedExitCoins(rs.getInt("forced_exit_coins"));
		this.setNameImage(rs.getString("name_image"));
		this.setFirstCoins(rs.getInt("first_coins"));
		
//		System.out.println("HallTypeInfo============" + this.getHallTypeName());
	}

	public int getHallTypeId() {
		return hallTypeId;
	}

	public void setHallTypeId(int hallTypeId) {
		this.hallTypeId = hallTypeId;
	}

	public String getHallTypeName() {
		return hallTypeName;
	}

	public void setHallTypeName(String hallTypeName) {
		this.hallTypeName = hallTypeName;
	}

	public int getHallType() {
		return hallType;
	}

	public void setHallType(int hallType) {
		this.hallType = hallType;
	}
	
	
	
	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}
	
	
	public int getPlayerCountMin() {
		return playerCountMin;
	}

	public void setPlayerCountMin(int playerCountMin) {
		this.playerCountMin = playerCountMin;
	}

	
	public int getPlayerCountMax() {
		return playerCountMax;
	}

	public void setPlayerCountMax(int playerCountMax) {
		this.playerCountMax = playerCountMax;
	}
	
	
		
	
	public int getWager() {
		return wager;
	}

	public void setWager(int wager) {
		this.wager = wager;
	}

	public int getLowestCoins() {
		return lowestCoins;
	}

	public void setLowestCoins(int lowestCoins) {
		this.lowestCoins = lowestCoins;
	}
	
	public int getHighestCoins() {
		return highestCoins;
	}

	public void setHighestCoins(int highestCoins) {
		this.highestCoins = highestCoins;
	}
		
	public int getQuickGameLowest() {
		return quickGameLowest;
	}

	public void setQuickGameLowest(int quickGameLowest) {
		this.quickGameLowest = quickGameLowest;
	}

	public int getQuickGameHighest() {
		return quickGameHighest;
	}

	public void setQuickGameHighest(int quickGameHighest) {
		this.quickGameHighest = quickGameHighest;
	}

	public int getSystemTaxes() {
		return systemTaxes;
	}

	public void setSystemTaxes(int systemTaxes) {
		this.systemTaxes = systemTaxes;
	}

	public int getForcedExitCoins() {
		return forcedExitCoins;
	}

	public void setForcedExitCoins(int forcedExitCoins) {
		this.forcedExitCoins = forcedExitCoins;
	}

	public String getNameImage() {
		return nameImage;
	}

	public void setNameImage(String nameImage) {
		this.nameImage = nameImage;
	}

	public int getRobRoomNum() {
//		if(hallTypeId == 4)
//		{
//			return new Random().nextInt(6);
//		}
		return robRoomNum;
	}

	public void setRobRoomNum(int robRoomNum) {
		this.robRoomNum = robRoomNum;
	}

	public int getFirstCoins() {
		return firstCoins;
	}

	public void setFirstCoins(int firstCoins) {
		this.firstCoins = firstCoins;
	}
}
