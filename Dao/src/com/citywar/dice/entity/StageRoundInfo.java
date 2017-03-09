package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class StageRoundInfo {
	
	
	private int stage_round_id;
	private int stage_id;
	private int round_index;
	private int round_score;
	private int player_count;
	private int game_count;

	
		  
	
	
	public StageRoundInfo() {}
	
	public StageRoundInfo(ResultSet rs) throws SQLException{
		
		this.setStageRoundId(rs.getInt("stage_round_id"));		
		this.setStageId(rs.getInt("stage_id"));
		this.setRoundIndex(rs.getInt("round_index"));
		this.setRoundScore(rs.getInt("round_score"));		
		this.setPlayerCount(rs.getInt("player_count"));		
		this.setGameCount(rs.getInt("game_count"));		
		
//		System.out.println("StagePrizeInfo============" + this.getPrizeName());
	}
	
	public int getStageRoundId() {
		return stage_round_id;
	}
	
	public void setStageRoundId(int stage_round_id) {
		this.stage_round_id = stage_round_id;
	}
	
	public int getStageId() {
		return stage_id;
	}
	
	public void setStageId(int stage_id) {
		this.stage_id = stage_id;
	}
		

	
	public int getRoundIndex() {
		return round_index;
	}
	
	public void setRoundIndex(int round_index) {
		this.round_index = round_index;
	}
	
	public int getRoundScore() {
		return round_score;
	}
	
	public void setRoundScore(int round_score) {
		this.round_score = round_score;
	}
	
	public int getPlayerCount() {
		return player_count;
	}
	
	public void setPlayerCount(int player_count) {
		this.player_count = player_count;
	}
	
	public int getGameCount() {
		return game_count;
	}
	
	public void setGameCount(int game_count) {
		this.game_count = game_count;
	}
	

		
}
