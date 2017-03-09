package com.citywar.dice.entity;


public class Version {

	private int versionId ;
	/** 版本号 */
	private String versionCode;
	/** 版本类型: (0:免费 1:收费)  */
	private int versionType;
	/** 版本描述：  */
	private String versionDesc;
	/** 升级状态： 1:强制升级 2:可选升级 */
	private int riseState;
	/** 服务器IP*/
	private String serverIp;
	/** 服务器PORT*/
	private int serverPort;
	/** 服务器状态： (1:可用 2:维护) */
	private int serverState;
	/** 服务器描述 */
	private String serverDesc;
	/** 更新时间 */
	private String updateTime;
	
	
	public Version(){}
	public Version(int versionId,String versionCode,int versionType,String versionDesc,int riseState,String serverIp,int serverPort,int serverState,String serverDesc,String updateTime){
		this.versionId = versionId;
		this.versionCode = versionCode;
		this.versionType = versionType;
		this.versionDesc = versionDesc;
		this.riseState = riseState;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.serverState = serverState;
		this.serverDesc = serverDesc;
		this.updateTime = updateTime;
	}
	public Version(String versionCode,int versionType,String versionDesc,int riseState,String serverIp,int serverPort,int serverState,String serverDesc,String updateTime)
	{
		this.versionCode = versionCode;
		this.versionType = versionType;
		this.versionDesc = versionDesc;
		this.riseState = riseState;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.serverState = serverState;
		this.serverDesc = serverDesc;
		this.updateTime = updateTime;
	}


	public int getVersionId() {
		return versionId;
	}



	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}



	public String getVersionCode() {
		return versionCode;
	}



	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}



	public int getVersionType() {
		return versionType;
	}



	public void setVersionType(int versionType) {
		this.versionType = versionType;
	}



	public String getVersionDesc() {
		return versionDesc;
	}



	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}



	public int getRiseState() {
		return riseState;
	}



	public void setRiseState(int riseState) {
		this.riseState = riseState;
	}



	public String getServerIp() {
		return serverIp;
	}



	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}



	public int getServerPort() {
		return serverPort;
	}



	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}



	public int getServerState() {
		return serverState;
	}



	public void setServerState(int serverState) {
		this.serverState = serverState;
	}



	public String getServerDesc() {
		return serverDesc;
	}



	public void setServerDesc(String serverDesc) {
		this.serverDesc = serverDesc;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
