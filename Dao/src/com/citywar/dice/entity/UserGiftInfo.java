/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 玩家礼品信息表
 * 
 * @author shanfeng.cao
 * @date 2011-06-26
 * @versions *
 */
public class UserGiftInfo extends DataObject
{
    /**
     * 礼品Id
     */
    private int id;
    /**
     * 接受用户 Id
     */
    private int ownerUserId;
    /**
     * 礼品模板 Id
     */
    private int giftId;
    /**
     * 赠送用户 Id
     */
    private int presentUserId;
    /**
     * 礼品数量
     */
    private int count;
    /**
     * 礼品赠送时间
     */
    private Timestamp giveTime;
    /**
     * 是否存在的标识, 用于礼品的逻辑删除; 0 -- 不存在, 已逻辑删除; 1 -- 存在
     */
    private boolean isExist;
    /**
     * 是否使用的标识, 用于礼品使用状态的逻辑表示; 0 -- 未使用; 1 -- 使用状态; 3 -- 使用完
     */
    private boolean isUsed;
    /**
     * 备用（留言等）
     */
    private String remark;
    /**
     * 赠送用户 的昵称
     */
    private String presentUserName;
    /**
     * 赠送用户 的头像地址
     */
    private String presentUserPicPath;
    
    /**
     * 
     */
    public UserGiftInfo()
    {
        super();
    }
	
	public UserGiftInfo(int id, int ownerUserId, int giftId, int presentUserId,
			int count, Timestamp giveTime, boolean isExist, boolean isUsed,
			String remark, String presentUserName, String presentUserPicPath) {
		super();
		this.id = id;
		this.ownerUserId = ownerUserId;
		this.giftId = giftId;
		this.presentUserId = presentUserId;
		this.count = count;
		this.giveTime = giveTime;
		this.isExist = isExist;
		this.isUsed = isUsed;
		this.remark = remark;
		this.presentUserName = presentUserName;
		this.presentUserPicPath = presentUserPicPath;
	}

	public UserGiftInfo(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.ownerUserId = rs.getInt("owner_user_id");
		this.giftId = rs.getInt("gift_id");
		this.presentUserId = rs.getInt("present_user_id");
		this.count = rs.getInt("count");
		this.giveTime = rs.getTimestamp("give_time");
		this.isExist = rs.getInt("isExist") == 0 ? false : true;
		this.isUsed = rs.getInt("isUsed") == 0 ? false : true;
		this.remark = rs.getString("remark");
		this.presentUserName = rs.getString("present_user_name");
		this.presentUserPicPath = rs.getString("present_user_pic_path");
	}

	@Override
	public String toString() {
		String GiftTemplateInfoString = String
				.format("id: %d,ownerUserId: %d,giftId: %d,presentUserId: %d,count: %d",
						getId(), getOwnerUserId(), getGiftId(),
						getPresentUserId(), getCount());
		return GiftTemplateInfoString + "giveTime: " + getGiveTime().toString()
				+ "isExist: " + isExist() + "isUsed: "
				+ isUsed() + "remark: " + getRemark() + "presentUserName: " + getPresentUserName() + "presentUserPicPath: "
				+ getPresentUserPicPath() + "Op: " + getOp();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(int ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public int getGiftId() {
		return giftId;
	}

	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}

	public int getPresentUserId() {
		return presentUserId;
	}

	public void setPresentUserId(int presentUserId) {
		this.presentUserId = presentUserId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Timestamp getGiveTime() {
		return giveTime;
	}

	public void setGiveTime(Timestamp giveTime) {
		this.giveTime = giveTime;
	}

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPresentUserName() {
		return presentUserName;
	}

	public void setPresentUserName(String presentUserName) {
		this.presentUserName = presentUserName;
	}

	public String getPresentUserPicPath() {
		return presentUserPicPath;
	}

	public void setPresentUserPicPath(String presentUserPicPath) {
		this.presentUserPicPath = presentUserPicPath;
	}
}
