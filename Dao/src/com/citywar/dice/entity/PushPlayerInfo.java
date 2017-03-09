package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class PushPlayerInfo {
	
	
	
	private int playerId;
	
	private String deviceToken;
	

	

	

	
	
	public PushPlayerInfo() {}
	
	public PushPlayerInfo(ResultSet rs) throws SQLException{
		
		this.setPlayerId(rs.getInt("UserId"));		
		this.setDeviceToken(rs.getString("machinery_id"));		

	}
	

	

	

	public int getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	
	public String getDeviceToken() {
		return deviceToken;
	}
	
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	
	
	

	
	
	
	
	
	
	

}
