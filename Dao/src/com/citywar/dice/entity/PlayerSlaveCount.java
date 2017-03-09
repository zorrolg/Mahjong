package com.citywar.dice.entity;

public class PlayerSlaveCount {

	/**
     * 用户 id
     */
    private int userId;
    
    /**
     * 玩家的奴隶数
     */
    private int slaveCount;
    
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getSlaveCount() {
		return slaveCount;
	}
	
	public void setSlaveCount(int slaveCount) {
		this.slaveCount = slaveCount;
	}
}
