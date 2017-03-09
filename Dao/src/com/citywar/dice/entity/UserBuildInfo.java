package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;



/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class UserBuildInfo {
	
	private int PlayerId;
	
	private int buildTypeId;
	
	private int buildLevel;
	
	private int buildIsUpgrade;
	
	private Timestamp buildUpgradeTime;
	

	
	
	
	public UserBuildInfo(int userId,int buildTypeId) {
		
		this.setPlayerId(userId);
		this.setBuildTypeId(buildTypeId);
		this.setBuildLevel(1);
		this.setBuildIsUpgrade(0);
		this.setBuildUpgradeTime(new Timestamp(System.currentTimeMillis()));
	}
	
	public UserBuildInfo(ResultSet rs) throws SQLException{
		
		this.setPlayerId(rs.getInt("UserId"));
		this.setBuildTypeId(rs.getInt("build_type_id"));
		this.setBuildLevel(rs.getInt("build_level"));
		this.setBuildIsUpgrade(rs.getInt("build_isupgrade"));
		
		this.setBuildUpgradeTime(rs.getTimestamp("build_upgrade_time"));
		
		
	}
	
	
	public int getPlayerId() {
		return PlayerId;
	}
	
	public void setPlayerId(int PlayerId) {
		this.PlayerId = PlayerId;
	}
	
	public int getBuildTypeId() {
		return buildTypeId;
	}
	
	public void setBuildTypeId(int buildTypeId) {
		this.buildTypeId = buildTypeId;
	}

	public int getBuildLevel() {
		return buildLevel;
	}
	
	public void setBuildLevel(int buildLevel) {
		this.buildLevel = buildLevel;
	}
	


	public int getBuildIsUpgrade() {
		return buildIsUpgrade;
	}
	
	public void setBuildIsUpgrade(int buildIsUpgrade) {
		this.buildIsUpgrade = buildIsUpgrade;
	}

	public Timestamp getBuildUpgradeTime() {
		return buildUpgradeTime;
	}
	
	public void setBuildUpgradeTime(Timestamp buildUpgradeTime) {
		this.buildUpgradeTime = buildUpgradeTime;
	}
	

	
	



	
	
	
}
