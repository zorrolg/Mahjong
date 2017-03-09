package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class CityInfo {
	
	
	
	
	private int cityId;
	
	
	private String cityName;
	
	/**
     * 纬度
     */
    private double x;
    
    /**
     * 经度
     */
    private double y;
    
    /**
     * 服务器id
     */
    private int serverId;
	
    /**
     * 服务器名称
     */
	private String serverName;
    
	
	
	public CityInfo() {}
	
	public CityInfo(ResultSet rs) throws SQLException{
		
		this.setCityId(rs.getInt("build_id"));
		this.setCityName(rs.getString("build_type_id"));

		this.setX(rs.getDouble("build_id"));
		this.setY(rs.getDouble("build_id"));
		
		this.setServerId(rs.getInt("build_id"));
		this.setServerName(rs.getString("build_type_id"));
		
		
	}
	
	public int getCityId() {
		return cityId;
	}
	
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}

	
	
	
	public int getServerId() {
		return serverId;
	}
	
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
