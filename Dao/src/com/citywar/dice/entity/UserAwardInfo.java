package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 用户领取奖励
 * 
 * @author shanfeng.cao
 * @date 2012-07-05
 * @version
 * 
 */
public class UserAwardInfo extends DataObject {
	/**
     * ID 
     */
	private int id;
	/**
     * 用户ID
     */
	private int userId;
	/**
     * 奖励类型
     */
	private int awardType;
	/**
     * 此类型领奖次数
     */
	private int awardCount;
	/**
     * 最后一次的时间
     */
	private Timestamp awardLastTtime;
	
	public UserAwardInfo(ResultSet rs) throws SQLException {
		this.setId(rs.getInt("id"));
		this.setUserId(rs.getInt("user_id"));
		this.setAwardType(rs.getInt("award_type"));
		this.setAwardCount(rs.getInt("award_count"));
		this.setAwardLastTtime(rs.getTimestamp("award_last_time"));
	}

	public UserAwardInfo(int userId, int awardType, int awardCount,
			Timestamp awardLastTtime) {
		super();
		this.userId = userId;
		this.awardType = awardType;
		this.awardCount = awardCount;
		this.awardLastTtime = awardLastTtime;
	}

	@Override
	public String toString() {
		String userAwardString = String
				.format("id: %d,userId: %d,awardType: %d,awardCount: %d",
						getId(), getUserId(), getAwardType(),
						getAwardCount());
		return userAwardString + "awardLastTtime: " + getAwardLastTtime();
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

	public int getAwardType() {
		return awardType;
	}

	public void setAwardType(int awardType) {
		this.awardType = awardType;
	}

	public int getAwardCount() {
		return awardCount;
	}

	public void setAwardCount(int awardCount) {
		this.awardCount = awardCount;
	}

	public Timestamp getAwardLastTtime() {
		return awardLastTtime;
	}

	public void setAwardLastTtime(Timestamp awardLastTtime) {
		this.awardLastTtime = awardLastTtime;
	}
	
}
