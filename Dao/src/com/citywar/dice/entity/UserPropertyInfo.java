/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 玩家信息表（实名制，防沉迷系统）
 * 
 * @author shanfeng.cao
 * @date 2012-09-04
 * @versions *
 */
public class UserPropertyInfo extends DataObject
{
    /**
     * 用户 Id
     */
    private int userId;
    /**
     * 玩家的身份证ID
     */
    private String identityCard;
    /**
     * 玩家的正式名字
     */
    private String realName;
    
    /**
     * 
     */
    public UserPropertyInfo()
    {
        super();
    }

	public UserPropertyInfo(int userId, String identityCard, String realName) {
		super();
		this.userId = userId;
		this.identityCard = identityCard;
		this.realName = realName;
	}

	public UserPropertyInfo(ResultSet rs) throws SQLException {
		this.userId = rs.getInt("user_id");
		this.identityCard = rs.getString("identity_card");
		this.realName = rs.getString("real_name");
	}

	@Override
	public String toString() {
		return "userId: " + userId + "identityCard: " + identityCard + "realName: " + realName + "Op: " + getOp();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
}
