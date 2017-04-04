package com.citywar.game;

import com.citywar.dice.entity.UserLetter;

public class UserNoReadyLetter implements Comparable<UserNoReadyLetter>{
	
	private UserLetter lastLetter;
	private int noReayCount;
	
	public UserNoReadyLetter(UserLetter lastLetter,int noReayCount){
		this.lastLetter= lastLetter;
		this.noReayCount = noReayCount;
	}
	
	public void addNoReadyConut(int count){
		noReayCount += count;
	}

	public UserLetter getLastLetter() {
		return lastLetter;
	}

	public void setLastLetter(UserLetter lastLetter) {
		this.lastLetter = lastLetter;
	}

	public int getNoReayCount() {
		return noReayCount;
	}

	public void setNoReayCount(int noReayCount) {
		this.noReayCount = noReayCount;
	}

	@Override
	public int compareTo(UserNoReadyLetter o) {
		 return (int)(o.getLastLetter().getSendTime().getTime()- this.getLastLetter().getSendTime().getTime());
	}
}
