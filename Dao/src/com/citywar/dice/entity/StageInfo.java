package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class StageInfo {
	
	private int stage_id;
		
	private String kkactivetoken;
	
	private int hallTypeId;
	
	private int stageType;
	
	private int defaultScore;
	
	private String province;
	
	private String city;

	private String name;
	
	private String pic;
	
	private String descript;
	
	
	
	
//	0-无限制
//	1-每天定时活动
//	2-每星期定时活动
//	3-每月定时活动
//	4-指定时间活动
	private int time_type;
	
	private int time_para;
	
	private Timestamp ready_time;
	
	private Timestamp start_time;
	
	private Timestamp end_time;
	
	
	
	
	public StageInfo() {}
	
	public StageInfo(ResultSet rs) throws SQLException{
		
		this.setStageId(rs.getInt("stage_id"));		
		this.setKKActiveToken(rs.getString("kkactive_token"));	
		this.setHallTypeId(rs.getInt("hall_type_Id"));
		this.setStageType(rs.getInt("stage_type"));		
		this.setDefaultScore(rs.getInt("default_score"));		
		
		this.setProvince(rs.getString("province"));
		this.setCity(rs.getString("city"));
		this.setName(rs.getString("name"));		
		this.setPic(rs.getString("pic"));
		this.setDescript(rs.getString("descript"));
		
		this.setTimeType(rs.getInt("time_type"));
		this.setTimePara(rs.getInt("time_para"));
		this.setStartTime(rs.getTimestamp("start_time"));
		this.setEndTime(rs.getTimestamp("end_time"));
					
	}
	
	public int getStageId() {
		return stage_id;
	}
	
	public void setStageId(int stage_id) {
		this.stage_id = stage_id;
	}
	
	
	public int getHallTypeId() {
		return hallTypeId;
	}
	
	public void setHallTypeId(int hallTypeId) {
		this.hallTypeId = hallTypeId;
	}
	
	public int getStageType() {
		return stageType;
	}
	
	public void setStageType(int stageType) {
		this.stageType = stageType;
	}
	
	public int getDefaultScore() {
		return defaultScore;
	}
	
	public void setDefaultScore(int defaultScore) {
		this.defaultScore = defaultScore;
	}
		
	public String getKKActiveToken() {
		return kkactivetoken;
	}
	
	public void setKKActiveToken(String kkactivetoken) {
		this.kkactivetoken = kkactivetoken;
	}
	
	public String getProvince() {
		return province;
	}
	
	public void setProvince(String province) {
		this.province = province;
	}
	
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getPic() {
		return pic;
	}
	
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescript() {
		return descript;
	}
	
	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	
	
	public int getTimeType() {
		return time_type;
	}
	
	public void setTimeType(int time_type) {
		this.time_type = time_type;
	}
	
	public int getTimePara() {
		return time_para;
	}
	
	public void setTimePara(int time_para) {
		this.time_para = time_para;
	}
		
	public Timestamp getStartTime() {
		return start_time;
	}
	
	public void setStartTime(Timestamp start_time) {
		this.start_time = start_time;
		
		this.ready_time = new Timestamp(this.start_time.getTime() - 1000*60*10);
	}
	
	public Timestamp getEndTime() {
		return end_time;
	}
	
	public void setEndTime(Timestamp end_time) {
		this.end_time = end_time;
	}

	public Timestamp getReadyTime() {
		return ready_time;
	}
	
	

}
