package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserRegister extends DataObject implements Cloneable {
	
	private int id;
	
	 /** 设备的唯一标示(目前是网卡ID)*/
    private String machineryId;

    /** 该设备已经注册账号的数量*/
    private int hasResCount;

    public UserRegister() {}
    public UserRegister(ResultSet rs) throws SQLException{
    	this.setId(rs.getInt("id"));
        this.setMachineryId(rs.getString("machinery_id"));
        this.setHasResCount(rs.getInt("has_count"));
    }
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMachineryId() {
		return machineryId;
	}

	public void setMachineryId(String machineryId) {
		this.machineryId = machineryId;
	}

	public int getHasResCount() {
		return hasResCount;
	}

	public void setHasResCount(int hasResCount) {
		this.hasResCount = hasResCount;
	}
    
}
