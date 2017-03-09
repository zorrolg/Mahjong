package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class PushInfo {
	
	
	
	private int pushId;
	
	private int pushType;
	
	private String msg;
	
	private Timestamp time;
	
	private String voice;
	
	private int count;
	

	
	
	public PushInfo() {}
	
	public PushInfo(ResultSet rs) throws SQLException{
		
		this.setPushId(rs.getInt("id"));
		this.setPushType(rs.getInt("type"));
		
		this.setMsg(rs.getString("msg"));		
		this.setTime(rs.getTimestamp("time"));

		this.setCount(rs.getInt("count"));		
		this.setVoice(rs.getString("voice"));	

	}
	

	

	

	public int getPushId() {
		return pushId;
	}
	
	public void setPushId(int pushId) {
		this.pushId = pushId;
	}

	public int getPushType() {
		return pushType;
	}
	
	public void setPushType(int pushType) {
		this.pushType = pushType;
	}
		
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
		
	public Timestamp getTime() {
		return time;
	}
	
	public void setTime(Timestamp time) {
		this.time = time;
	}
	

	
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	
	public String getVoice() {
		return voice;
	}
	
	public void setVoice(String voice) {
		this.voice = voice;
	}
	

	
	

}
