package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class PushLogInfo {
	
	private int pushId;
	
	private String deviceToken;
	
	private String msg;
	
	private int result;
	
	private Timestamp time;
	

	

	
	
	public PushLogInfo() {}
	
	public PushLogInfo(ResultSet rs) throws SQLException{
		
		this.setPushId(rs.getInt("id"));
		this.setDeviceToken(rs.getString("deviceToken"));
		
		this.setMsg(rs.getString("msg"));		
		this.setResult(rs.getInt("result"));
		this.setTime(rs.getTimestamp("time"));

	}
	

	

	

	public int getPushId() {
		return pushId;
	}
	
	public void setPushId(int pushId) {
		this.pushId = pushId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}
	
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public int getResult() {
		return result;
	}
	
	public void setResult(int result) {
		this.result = result;
	}
	
	public Timestamp getTime() {
		return time;
	}
	
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	

	
	
	
	
	
	
	

}
