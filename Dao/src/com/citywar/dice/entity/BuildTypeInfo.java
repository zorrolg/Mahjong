package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class BuildTypeInfo {
	

	
	private int buildTypeId;
	
	private String buildTypeName;
	

	
	
	
	public BuildTypeInfo() {}
	
	public BuildTypeInfo(ResultSet rs) throws SQLException{
		
		this.setBuildTypeId(rs.getInt("build_type_id"));
		this.setBuildTypeName(rs.getString("name"));

	}


	public int getBuildTypeId() {
		return buildTypeId;
	}
	
	public void setBuildTypeId(int buildTypeId) {
		this.buildTypeId = buildTypeId;
	}
	
	public String getBuildTypeName() {
		return buildTypeName;
	}
	
	public void setBuildTypeName(String buildTypeName) {
		this.buildTypeName = buildTypeName;
	}
	

	
}
