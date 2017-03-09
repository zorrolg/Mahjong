package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 每日在线、注册等统计类
 * @author shenfeng.cao
 *
 */
public class UserDayActivityInfo extends DataObject {
	/**
	 * Id
	 */
	private int id;
	/**
	 * 用户Id
	 */
	private int userId;
	/**
	 * 统计时间
	 */
	private Timestamp countDate;
	/**
	 * 用户均日在线时长
	 */
	private long perCapitaTime;
	/**
	 * 用户日使用醒酒茶数
	 */
	private int usageCounterTea;
	/**
	 * 用户日使用喇叭数
	 */
	private int usageCounterHorn;
	/**
	 * 当天赚的金币数
	 */
	private int increaseCoins;
	
	
	/**
	 * 当天赚的金币数
	 */
	private int increaseCharmValve;
	
	
	
	
	public UserDayActivityInfo() {
		
	}
	
	public UserDayActivityInfo(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.userId = rs.getInt("user_id");
		this.countDate = rs.getTimestamp("count_date");
		this.perCapitaTime = rs.getLong("online_time");
		this.usageCounterTea = rs.getInt("usage_counter_tea");
		this.usageCounterHorn = rs.getInt("usage_counter_horn");
		this.increaseCoins = rs.getInt("increase_coins");
		this.increaseCharmValve = rs.getInt("increase_charmvalve");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getCountDate() {
		return countDate;
	}

	public void setCountDate(Timestamp countDate) {
		this.countDate = countDate;
	}

	public long getPerCapitaTime() {
		return perCapitaTime;
	}

	public void setPerCapitaTime(long perCapitaTime) {
		this.perCapitaTime = perCapitaTime;
	}

	public int getUsageCounterTea() {
		return usageCounterTea;
	}

	public void setUsageCounterTea(int usageCounterTea) {
		this.usageCounterTea = usageCounterTea;
	}

	public int getUsageCounterHorn() {
		return usageCounterHorn;
	}

	public void setUsageCounterHorn(int usageCounterHorn) {
		this.usageCounterHorn = usageCounterHorn;
	}

	public int getIncreaseCoins() {
		return increaseCoins;
	}

	public void setIncreaseCoins(int increaseCoins) {
		this.increaseCoins = increaseCoins;
	}
	
	public int getIncreaseCharmValve() {
		return increaseCharmValve;
	}

	public void setIncreaseCharmValve(int increaseCharmValve) {
		this.increaseCharmValve = increaseCharmValve;
	}
	
	
}
