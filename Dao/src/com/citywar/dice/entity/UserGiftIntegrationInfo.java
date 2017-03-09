package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserGiftIntegrationInfo {
    /**
     * 礼品Id
     */
    private int giftId;
    /**
     * 数量
     */
    private int sumCount;
    
    public UserGiftIntegrationInfo(ResultSet rs) throws SQLException {
		this.giftId = rs.getInt("gift_id");
		this.sumCount = rs.getInt("sum_count");
	}

	public UserGiftIntegrationInfo(int giftId, int sumCount) {
		this.giftId = giftId;
		this.sumCount = sumCount;
	}
	
	@Override
	public String toString() {
		String UserGiftIntegrationInfoString = String
				.format("giftId: %d,sumCount: %d",
						getGiftId(), getSumCount());
		return UserGiftIntegrationInfoString;
	}
	
	public int getGiftId() {
		return giftId;
	}

	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}

	public int getSumCount() {
		return sumCount;
	}

	public void setSumCount(int sumCount) {
		this.sumCount = sumCount;
	}
}
