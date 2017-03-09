package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class BuildInfo {
	
	private int buildId;
	
	private int buildTypeId;
	
	private int needBuildType;
	
	private int needBuildLevel;
	
	private String buildName;
	
	private int buildLevel;
	
	private String descript;
	
	private int upgradeTime;
	
	private int upgradeCoin;
	
	private int para1;
	
	private int para2;
	
	
	
	public BuildInfo() {}
	
	public BuildInfo(ResultSet rs) throws SQLException{
		
		this.setBuildId(rs.getInt("build_id"));
		this.setBuildTypeId(rs.getInt("build_type_id"));
		this.setNeedBuildType(rs.getInt("need_build_type"));
		this.setNeedBuildLevel(rs.getInt("need_build_level"));
		this.setBuildName(rs.getString("name"));
		this.setBuildLevel(rs.getInt("level"));
		this.setDescript(rs.getString("descript"));
		this.setUpgradeTime(rs.getInt("upgrade_time"));
		this.setUpgradeCoin(rs.getInt("upgrade_coin"));
		this.setPara1(rs.getInt("para_1"));
		this.setPara2(rs.getInt("para_2"));
		
	}
	
	public int getBuildId() {
		return buildId;
	}
	
	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}

	public int getBuildTypeId() {
		return buildTypeId;
	}
	
	public void setBuildTypeId(int buildTypeId) {
		this.buildTypeId = buildTypeId;
	}
	
	public int getNeedBuildType() {
		return needBuildType;
	}
	
	public void setNeedBuildType(int needBuildType) {
		this.needBuildType = needBuildType;
	}

	public int getNeedBuildLevel() {
		return needBuildLevel;
	}
	
	public void setNeedBuildLevel(int needBuildLevel) {
		this.needBuildLevel = needBuildLevel;
	}
	
	public String getBuildName() {
		return buildName;
	}
	
	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}
	
	public int getBuildLevel() {
		return buildLevel;
	}
	
	public void setBuildLevel(int buildLevel) {
		this.buildLevel = buildLevel;
	}
	
	public String getDescript() {
		return descript;
	}
	
	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	public int getUpgradeTime() {
		return upgradeTime;
	}
	
	public void setUpgradeTime(int upgradeTime) {
		this.upgradeTime = upgradeTime;
	}
	
	public int getUpgradeCoin() {
		return upgradeCoin;
	}
	
	public void setUpgradeCoin(int upgradeCoin) {
		this.upgradeCoin = upgradeCoin;
	}
	
	public int getPara1() {
		return para1;
	}
	
	public void setPara1(int para1) {
		this.para1 = para1;
	}
	
	public int getPara2() {
		return para2;
	}
	
	public void setPara2(int para2) {
		this.para2 = para2;
	}
	
}
