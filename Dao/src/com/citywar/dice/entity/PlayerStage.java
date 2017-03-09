package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerStage extends DataObject{

	/**
     * 用户 id
     */
    private int userId;
    
    /**
     * 玩家的奴隶数
     */
    private int stageid;
    
    
    private int charmValve;
    
    
    public PlayerStage(int userId, int stageId, int CharvValve) {
		
		this.setUserId(userId);
		this.setStageId(stageId);
		this.setCharmValve(CharvValve);
	}


	public PlayerStage(ResultSet rs) {
		
		try {
			
			this.setUserId(rs.getInt("user_id"));
			this.setStageId(rs.getInt("stage_id"));
			this.setCharmValve(rs.getInt("charmvalve"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
		}
	}

	
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getStageId() {
		return stageid;
	}
	
	public void setStageId(int stageid) {
		this.stageid = stageid;
	}
	
	public int getCharmValve() {
		return charmValve;
	}
	
	public void setCharmValve(int charmValve) {
		this.charmValve = charmValve;
	}
	
}
