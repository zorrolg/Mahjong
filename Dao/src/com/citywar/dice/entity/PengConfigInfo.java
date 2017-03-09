package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class PengConfigInfo {
	
	private int configId;
	
	private int gameId;
	
	private int typeId;
	
	private int typeIndex;
			
	private int para1;
	
	private int para2;
	
	private String name;
			
	private String descript;
	
	
		
	
	public PengConfigInfo() {}
	
	public PengConfigInfo(ResultSet rs) throws SQLException{
		
		this.setConfigId(rs.getInt("configId"));
		this.setGameId(rs.getInt("gameId"));
		this.setTypeId(rs.getInt("typeId"));
		this.setTypeIndex(rs.getInt("typeIndex"));
		this.setPara1(rs.getInt("para1"));
		this.setPara2(rs.getInt("para2"));
		
		this.setName(rs.getString("name"));		
		this.setDescript(rs.getString("descript"));
				
	}
	
	public int getConfigId() {
		return configId;
	}
	
	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public int getGameId() {
		return gameId;
	}
	
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public int getTypeId() {
		return typeId;
	}
	
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	public int getTypeIndex() {
		return typeIndex;
	}
	
	public void setTypeIndex(int typeIndex) {
		this.typeIndex = typeIndex;
	}
	
	public int getPara1() {
		return para1;
	}
	
	public void setPara1(int para) {
		this.para1 = para;
	}
	
	public int getPara2() {
		return para2;
	}
	
	public void setPara2(int para) {
		this.para2 = para;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescript() {
		return descript;
	}
	
	public void setDescript(String descript) {
		this.descript = descript;
	}
}
