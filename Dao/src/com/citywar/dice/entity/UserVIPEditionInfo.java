/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 玩家购买VIP收费版，领取奖励
 * 
 * @author shanfeng.cao
 * @date 2012-09-07
 * @versions *
 */
public class UserVIPEditionInfo extends DataObject
{
    /**
     * Id
     */
    private int id;
    /**
     * 用户 Id
     */
    private int userId;
    /**
     * 设备显卡ID
     */
    private String machineryId;
    /**
     * 礼品赠送时间
     */
    private Timestamp awardTime;
    /**
     * 
     */
    public UserVIPEditionInfo()
    {
        super();
    }

	public UserVIPEditionInfo(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.userId = rs.getInt("user_id");
		this.machineryId = rs.getString("machinery_id");
		this.awardTime = rs.getTimestamp("award_time");
	}

	@Override
	public String toString() {
		return "id: " + id
				+ "userId: " + userId + "machineryId: "
				+ machineryId + "awardTime: " + awardTime + "Op: " + getOp();
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

	public String getMachineryId() {
		return machineryId;
	}

	public void setMachineryId(String machineryId) {
		this.machineryId = machineryId;
	}

	public Timestamp getAwardTime() {
		return awardTime;
	}

	public void setAwardTime(Timestamp awardTime) {
		this.awardTime = awardTime;
	}
	
}
