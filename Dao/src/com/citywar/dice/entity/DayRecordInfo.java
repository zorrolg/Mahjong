package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 每日在线、注册等统计类
 * @author shenfeng.cao
 *
 */
public class DayRecordInfo extends DataObject {
	/**
	 * 统计时间
	 */
	private Timestamp countDate;
	/**
	 * 渠道号id--默认为1
	 */
	private int subId;
	/**
	 * 注册会员数
	 */
	private int regMemberCount;
	/**
	 * 日注册用户数
	 */
	private int regUserCount;
	/**
	 * 日登录用户数
	 */
	private int loginUserCount;
	/**
	 * 日在线峰值
	 */
	private int maxOnlineCount;
	/**
	 * 用户均日在线时长
	 */
	private long perCapitaTime;
	/**
	 * 日使用醒酒茶数
	 */
	private int usageCounterTea;
	/**
	 * 日使用喇叭数
	 */
	private int usageCounterHorn;
	
	public DayRecordInfo() {
		
	}
	
	public DayRecordInfo(ResultSet rs) throws SQLException {
		this.countDate = rs.getTimestamp("count_date");
		this.subId = rs.getInt("sub_id");
		this.regMemberCount = rs.getInt("reg_member_count");
		this.regUserCount = rs.getInt("reg_user_count");
		this.loginUserCount = rs.getInt("login_user_count");
		this.maxOnlineCount = rs.getInt("max_online_count");
		this.perCapitaTime = rs.getInt("per_capita_time");
		this.usageCounterTea = rs.getInt("usage_counter_tea");
		this.usageCounterHorn = rs.getInt("usage_counter_horn");
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

	public int getRegMemberCount() {
		return regMemberCount;
	}

	public void setRegMemberCount(int regMemberCount) {
		this.regMemberCount = regMemberCount;
	}

	public int getRegUserCount() {
		return regUserCount;
	}

	public void setRegUserCount(int regUserCount) {
		this.regUserCount = regUserCount;
	}

	public int getLoginUserCount() {
		return loginUserCount;
	}

	public void setLoginUserCount(int loginUserCount) {
		this.loginUserCount = loginUserCount;
	}

	public int getMaxOnlineCount() {
		return maxOnlineCount;
	}

	public void setMaxOnlineCount(int maxOnlineCount) {
		this.maxOnlineCount = maxOnlineCount;
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
}
