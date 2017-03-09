package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 注册玩家一周留存率等统计类
 * @author shenfeng.cao
 *
 */
public class DayRetainedRateInfo {
	/**
	 * 统计时间
	 */
	private Timestamp countDate;
	/**
	 * 渠道号id--默认为1
	 */
	private int subId;
	/**
	 * 玩家总共注册数
	 */
	private int dayRegCount;
	/**
	 * 第1天留存玩家数
	 */
	private int Day1LoginCount;
	/**
	 * 第2天留存玩家数
	 */
	private int Day2LoginCount;
	/**
	 * 第3天留存玩家数
	 */
	private int Day3LoginCount;
	/**
	 * 第4天留存玩家数
	 */
	private int Day4LoginCount;
	/**
	 * 第5天留存玩家数
	 */
	private int Day5LoginCount;
	/**
	 * 第6天留存玩家数
	 */
	private int Day6LoginCount;
	/**
	 * 第7天留存玩家数
	 */
	private int Day7LoginCount;
	
	
	public DayRetainedRateInfo() {
		
	}
	
	public DayRetainedRateInfo(ResultSet rs) throws SQLException {
		this.countDate = rs.getTimestamp("count_date");
		this.subId = rs.getInt("sub_id");
		this.dayRegCount = rs.getInt("day_reg_count");
		this.Day1LoginCount = rs.getInt("day_1_login_count");
		this.Day2LoginCount = rs.getInt("day_2_login_count");
		this.Day3LoginCount = rs.getInt("day_3_login_count");
		this.Day4LoginCount = rs.getInt("day_4_login_count");
		this.Day5LoginCount = rs.getInt("day_5_login_count");
		this.Day6LoginCount = rs.getInt("day_6_login_count");
		this.Day7LoginCount = rs.getInt("day_7_login_count");
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

	public int getDayRegCount() {
		return dayRegCount;
	}

	public void setDayRegCount(int dayRegCount) {
		this.dayRegCount = dayRegCount;
	}

	public int getDay1LoginCount() {
		return Day1LoginCount;
	}

	public void setDay1LoginCount(int day1LoginCount) {
		Day1LoginCount = day1LoginCount;
	}

	public int getDay2LoginCount() {
		return Day2LoginCount;
	}

	public void setDay2LoginCount(int day2LoginCount) {
		Day2LoginCount = day2LoginCount;
	}

	public int getDay3LoginCount() {
		return Day3LoginCount;
	}

	public void setDay3LoginCount(int day3LoginCount) {
		Day3LoginCount = day3LoginCount;
	}

	public int getDay4LoginCount() {
		return Day4LoginCount;
	}

	public void setDay4LoginCount(int day4LoginCount) {
		Day4LoginCount = day4LoginCount;
	}

	public int getDay5LoginCount() {
		return Day5LoginCount;
	}

	public void setDay5LoginCount(int day5LoginCount) {
		Day5LoginCount = day5LoginCount;
	}

	public int getDay6LoginCount() {
		return Day6LoginCount;
	}

	public void setDay6LoginCount(int day6LoginCount) {
		Day6LoginCount = day6LoginCount;
	}

	public int getDay7LoginCount() {
		return Day7LoginCount;
	}

	public void setDay7LoginCount(int day7LoginCount) {
		Day7LoginCount = day7LoginCount;
	}
	
	/**
	 * 
	 * @param some
	 * @param dayLoginCount
	 */
	public void setSomeDayLoginCount(int some, int dayLoginCount) {
		switch (some) {
		case 1:
			setDay1LoginCount(dayLoginCount);
			break;
		case 2:
			setDay2LoginCount(dayLoginCount);
			break;
		case 3:
			setDay3LoginCount(dayLoginCount);
			break;
		case 4:
			setDay4LoginCount(dayLoginCount);
			break;
		case 5:
			setDay5LoginCount(dayLoginCount);
			break;
		case 6:
			setDay6LoginCount(dayLoginCount);
			break;
		case 7:
			setDay7LoginCount(dayLoginCount);
			break;
		default:
			break;
		}
	}
}
