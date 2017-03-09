package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 各类游戏的次数统计类
 * @author shenfeng.cao
 *
 */
public class GameTypeCountInfo {
	/**
	 * 统计时间
	 */
	private Timestamp countDate;
	/**
	 * 渠道号id--默认为1
	 */
	private int subId;
	/**
	 * 二个人的玩游戏的次数
	 */
	private int gameTypeTwoPlayer;
	/**
	 * 三个人的玩游戏的次数
	 */
	private int gameTypeThreePlayer;
	/**
	 * 四个人的玩游戏的次数
	 */
	private int gameTypeFourPlayer;
	
	public GameTypeCountInfo() {
		
	}
	
	public GameTypeCountInfo(ResultSet rs) throws SQLException {
		this.countDate = rs.getTimestamp("count_date");
		this.subId = rs.getInt("sub_id");
		this.gameTypeTwoPlayer = rs.getInt("game_2_player_count");
		this.gameTypeThreePlayer = rs.getInt("game_3_player_count");
		this.gameTypeFourPlayer = rs.getInt("game_4_player_count");
	}

	public Timestamp getCountDate() {
		return countDate;
	}

	public void setCountDate(Timestamp countDate) {
		this.countDate = countDate;
	}

	public int getSubId() {
		return subId;
	}

	public void setSubId(int subId) {
		this.subId = subId;
	}

	public int getGameTypeTwoPlayer() {
		return gameTypeTwoPlayer;
	}

	public void setGameTypeTwoPlayer(int gameTypeTwoPlayer) {
		this.gameTypeTwoPlayer = gameTypeTwoPlayer;
	}

	public int getGameTypeThreePlayer() {
		return gameTypeThreePlayer;
	}

	public void setGameTypeThreePlayer(int gameTypeThreePlayer) {
		this.gameTypeThreePlayer = gameTypeThreePlayer;
	}

	public int getGameTypeFourPlayer() {
		return gameTypeFourPlayer;
	}

	public void setGameTypeFourPlayer(int gameTypeFourPlayer) {
		this.gameTypeFourPlayer = gameTypeFourPlayer;
	}
}
