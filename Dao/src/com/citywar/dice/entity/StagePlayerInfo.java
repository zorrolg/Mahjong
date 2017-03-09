package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class StagePlayerInfo {
	
	
	
	private int stage_player_id;
	private int stage_data_id;
	private int stage_prize_id;
	private int player_id;
	private int score;
	private int index;
	private String prize;
	private String desc;
	private Timestamp leagueDate;
	
	private int kkprizeid;

	
	public StagePlayerInfo() {}
	
	public StagePlayerInfo(ResultSet rs) throws SQLException{
		
		this.setStagePlayerId(rs.getInt("stage_player_id"));
		this.setStageDataId(rs.getInt("stage_data_id"));
		this.setStagePrizeId(rs.getInt("stage_prize_id"));
		this.setPlayerId(rs.getInt("player_id"));		
		this.setScore(rs.getInt("score"));
		this.setIndex(rs.getInt("index"));
		
		this.setPrize(rs.getString("prize"));
		this.setDesc(rs.getString("desc"));
		this.setLeagueDate(rs.getTimestamp("leagueDate"));
	}
	
	public int getStagePlayerId() {
		return stage_player_id;
	}
	
	public void setStagePlayerId(int stage_player_id) {
		this.stage_player_id = stage_player_id;
	}

	
	public int getStageDataId() {
		return stage_data_id;
	}
	
	public void setStageDataId(int stage_data_id) {
		this.stage_data_id = stage_data_id;
	}
	
	public int getStagePrizeId() {
		return stage_prize_id;
	}
	
	public void setStagePrizeId(int stage_prize_id) {
		this.stage_prize_id = stage_prize_id;
	}
	
	
	
	
	
	
	public int getPlayerId() {
		return player_id;
	}
	
	public void setPlayerId(int player_id) {
		this.player_id = player_id;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	
	public int getKKPrizeId() {
		return kkprizeid;
	}
	
	public void setKKPrizeId(int kkprizeid) {
		this.kkprizeid = kkprizeid;
	}
	
	public String getPrize() {
		return prize;
	}
	
	public void setPrize(String prize) {
		this.prize = prize;
	}
	
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Timestamp getLeagueDate() {
		return leagueDate;
	}
	
	public void setLeagueDate(Timestamp leagueDate) {
		this.leagueDate = leagueDate;
	}
}
