package com.citywar.dice.entity;

public class TaskPrize {
	
	private int prizeType;
	
	private int prizeNum;
	
	public TaskPrize(String prizeType, String prizeNum) {
		this.prizeType = Integer.valueOf(prizeType);
		this.prizeNum = Integer.valueOf(prizeNum);
	}

	public int getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(int prizeType) {
		this.prizeType = prizeType;
	}

	public int getPrizeNum() {
		return prizeNum;
	}

	public void setPrizeNum(int prizeNum) {
		this.prizeNum = prizeNum;
	}
}
