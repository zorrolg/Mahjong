package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户所拥有的奴隶entity
 * @author Jacky.zheng
 *
 */
public class UserRefWorkInfo extends DataObject {
	
	private int id;
			
	private String workName;
		
	private int workTime;
	
	private int workCoin;
	
	
	public UserRefWorkInfo(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.workName = rs.getString("name");
		this.workTime = rs.getInt("time");
		this.workCoin = rs.getInt("coin");
		//从数据库中取出的相册集合字符串,通过逗号分割
       
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getWorkName() {
		return workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public int getWorkTime() {
		return workTime;
	}

	public void setWorkTime(int workTime) {
		this.workTime = workTime;
	}
	
	public int getWorkCoin() {
		return workCoin;
	}

	public void setWorkCoin(int workCoin) {
		this.workCoin = workCoin;
	}



}
