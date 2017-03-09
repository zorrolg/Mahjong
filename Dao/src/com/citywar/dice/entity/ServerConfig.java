package com.citywar.dice.entity;

public class ServerConfig {
	
	private int id;
	private String name;
	private String ip;
	private int port;
	private int state;
	private String desc;
	private int versionId;
	
	public ServerConfig(){}
	
	public ServerConfig(int id,int port,int versionId,int state,String name,String ip,String desc){
		this.id = id;
		this.port = port;
		this.versionId = versionId;
		this.state = state;
		this.name = name;
		this.ip = ip;
		this.desc = desc;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getVersionId() {
		return versionId;
	}
	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

}
