package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class StagePrizeInfo {
	
	
	private int stage_prize_id;
	private int stage_id;
	private int index;
	private int kkactive_prizeid;
	private String prize_name;
	private int prize_type;
	private int prize_number;

	
	
	  
	
	
	public StagePrizeInfo() {}
	
	public StagePrizeInfo(ResultSet rs) throws SQLException{
		
		this.setStagePrizeId(rs.getInt("stage_prize_id"));		
		this.setStageId(rs.getInt("stage_id"));
		this.setIndex(rs.getInt("index"));
		this.setKKActivePrizeId(rs.getInt("kkactive_prizeid"));		
		this.setPrizeName(rs.getString("prize_name"));		
		this.setPrizeType(rs.getInt("prize_type"));		
		this.setPrizeNumber(rs.getInt("prize_number"));		
		
//		System.out.println("StagePrizeInfo============" + this.getPrizeName());
	}
	
	public int getStagePrizeId() {
		return stage_prize_id;
	}
	
	public void setStagePrizeId(int stage_prize_id) {
		this.stage_prize_id = stage_prize_id;
	}
	
	public int getStageId() {
		return stage_id;
	}
	
	public void setStageId(int stage_id) {
		this.stage_id = stage_id;
	}
		

	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getKKActivePrizeId() {
		return kkactive_prizeid;
	}
	
	public void setKKActivePrizeId(int kkactive_prizeid) {
		this.kkactive_prizeid = kkactive_prizeid;
	}
	
	public String getPrizeName() {
		return prize_name;
	}
	
	public void setPrizeName(String prize_name) {
		this.prize_name = prize_name;
	}

	public int getPrizeType() {
		return prize_type;
	}
	
	public void setPrizeType(int prize_type) {
		this.prize_type = prize_type;
	}
	
	public int getPrizeNumber() {
		return prize_number;
	}
	
	public void setPrizeNumber(int prize_number) {
		this.prize_number = prize_number;
	}
	

		
}
