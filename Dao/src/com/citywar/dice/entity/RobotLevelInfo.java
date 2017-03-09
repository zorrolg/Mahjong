package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RobotLevelInfo {
	/**
	 * ID
	 */
	private int level;
	/**
	 * 玩家游戏的状态
	 */
	private int realOpenPercent;
	/**
	 * 等级（权值）
	 */
	private int unrealOpenPercent;
	
	
	
	private int realcallnumberpercent;	
	private int realcallmostpercent;	
//	private int realcalladdpercent;	
	private int realcallrndpercent;	
	
	private int unrealcallnumberpercent;	
	private int unrealcallmostpercent;
	private int unrealcalladdpercent;	
	private int unrealcallrndpercent;
	

	
	public RobotLevelInfo(ResultSet rs) throws SQLException {
		super();
		this.level = rs.getInt("level");
		this.realOpenPercent = rs.getInt("real_Open_Percent");
		this.unrealOpenPercent = rs.getInt("unreal_Open_Percent");
		
		this.realcallnumberpercent = rs.getInt("real_call_number_percent");
		this.realcallmostpercent = rs.getInt("real_call_most_percent");
//		this.realcalladdpercent = rs.getInt("real_call_add_percent");
		this.realcallrndpercent = rs.getInt("real_call_rnd_percent");
		
		this.unrealcallnumberpercent = rs.getInt("unreal_call_number_percent");
		this.unrealcallmostpercent = rs.getInt("unreal_call_most_percent");
		this.unrealcallmostpercent = rs.getInt("unreal_call_add_percent");
		this.unrealcallrndpercent = rs.getInt("unreal_call_rnd_percent");
		
			
			
			
			
			
			
			
		
		
		
	}
	
	public RobotLevelInfo() {
	}

	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	
	
	
	public int getRealOpenPercent() {
		return realOpenPercent;
	}
	
	public void setRealOpenPercent(int realOpenPercent) {
		this.realOpenPercent = realOpenPercent;
	}
	
	public int getUnRealOpenPercent() {
		return unrealOpenPercent;
	}
	
	public void setUnRealOpenPercent(int unrealOpenPercent) {
		this.unrealOpenPercent = unrealOpenPercent;
	}
	
	
	
	
	
	
	public int getRealcallnumberpercent() {
		return realcallnumberpercent;
	}
	
	public void setRealcallnumberpercent(int realcallnumberpercent) {
		this.realcallnumberpercent = realcallnumberpercent;
	}
	
	public int getRealcallmostpercent() {
		return realcallmostpercent;
	}
	
	public void setRealcallmostpercent(int realcallmostpercent) {
		this.realcallmostpercent = realcallmostpercent;
	}
	
//	public int getRealcalladdpercent() {
//		return realcallnumberpercent;
//	}
//	
//	public void setRealcalladdpercent(int realcalladdpercent) {
//		this.realcalladdpercent = realcalladdpercent;
//	}
	
	public int getRealcallrndpercent() {
		return realcallrndpercent;
	}
	
	public void setRealcallrndpercent(int realcallrndpercent) {
		this.realcallrndpercent = realcallrndpercent;
	}
	
	


	
	public int getUnRealcallnumberpercent() {
		return unrealcallnumberpercent;
	}
	
	public void setunrealcallnumberpercent(int unrealcallnumberpercent) {
		this.unrealcallnumberpercent = unrealcallnumberpercent;
	}
	
	public int getUnRealcallmostpercent() {
		return unrealcallmostpercent;
	}
	
	public void setUnRealcallmostpercent(int unrealcallmostpercent) {
		this.unrealcallmostpercent = unrealcallmostpercent;
	}
	
	public int getUnRealcalladdpercent() {
		return unrealcalladdpercent;
	}
	
	public void setUnRealcalladdpercent(int unrealcalladdpercent) {
		this.unrealcalladdpercent = unrealcalladdpercent;
	}
	
	public int getUnRealcallrndpercent() {
		return unrealcallrndpercent;
	}
	
	public void setUnRealcallrndpercent(int unrealcallrndpercent) {
		this.unrealcallrndpercent = unrealcallrndpercent;
	}
		
	
}
