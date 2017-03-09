package com.citywar.dice.entity;


/**
 * 版本升級
 *
 */
public class VersionRise {
	private int id;
	private int state;
	private String lowVersionCode;
	private String highVersionCode;
	private String desc;
	private String xmlUrl;
	
	public VersionRise(){
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLowVersionCode() {
		return lowVersionCode;
	}

	public void setLowVersionCode(String lowVersionCode) {
		this.lowVersionCode = lowVersionCode;
	}

	public String getHighVersionCode() {
		return highVersionCode;
	}

	public void setHighVersionCode(String highVersionCode) {
		this.highVersionCode = highVersionCode;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getXmlUrl() {
		return xmlUrl;
	}

	public void setXmlUrl(String xmlUrl) {
		this.xmlUrl = xmlUrl;
	}
}
