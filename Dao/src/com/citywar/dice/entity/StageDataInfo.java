package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class StageDataInfo {
	
		
	
	private int stage_data_id;
	private int stage_id;
	private String stage_title;
	private Timestamp stage_time;
	
		    
	
	
	public StageDataInfo() {}
	
	public StageDataInfo(ResultSet rs) throws SQLException{
		
		this.setStageDataId(rs.getInt("stage_data_id"));
		this.setStageId(rs.getInt("stage_id"));

		this.setStageTitle(rs.getString("stage_title"));
		this.setStageTime(rs.getTimestamp("stage_time"));
		
		
	}
	
	public int getStageDataId() {
		return stage_data_id;
	}
	
	public void setStageDataId(int stage_data_id) {
		this.stage_data_id = stage_data_id;
	}

	public int getStageId() {
		return stage_id;
	}
	
	public void setStageId(int stage_id) {
		this.stage_id = stage_id;
	}
	
		
	
	
	public String getStageTitle() {
		return stage_title;
	}
	
	public void setStageTitle(String stage_title) {
		this.stage_title = stage_title;
	}

	public Timestamp getStageTime() {
		return stage_time;
	}
	
	public void setStageTime(Timestamp stage_time) {
		this.stage_time = stage_time;
	}
	
}
