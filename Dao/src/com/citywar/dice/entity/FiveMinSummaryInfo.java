package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 五分钟在线、注册统计类
 * @author jacky.zheng
 *
 */
public class FiveMinSummaryInfo extends DataObject {
	/**
	 * 统计五分钟时间点
	 */
	private Timestamp countDate;
	/**
	 * 渠道号id--默认为1
	 */
	private int subId;
	/**
	 * 五分钟注册数量
	 */
	private int regCount;
	/**
	 * 五分钟在线数量
	 */
	private int onlineCount;
	
	public FiveMinSummaryInfo() {
		
	}
	
	public FiveMinSummaryInfo(ResultSet rs) throws SQLException {
		this.countDate = rs.getTimestamp("count_date");
		this.subId = rs.getInt("sub_id");
		this.regCount = rs.getInt("reg");
		this.onlineCount = rs.getInt("online");
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
	public int getRegCount() {
		return regCount;
	}
	public void setRegCount(int regCount) {
		this.regCount = regCount;
	}
	public int getOnlineCount() {
		return onlineCount;
	}
	public void setOnlineCount(int onlineCount) {
		this.onlineCount = onlineCount;
	}
}
