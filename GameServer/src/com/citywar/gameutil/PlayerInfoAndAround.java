package com.citywar.gameutil;

import com.citywar.dice.entity.AroundUser;
import com.citywar.dice.entity.PlayerInfo;

public class PlayerInfoAndAround {
	
	private int userId;

    private PlayerInfo aroudPlayer;
    
    private AroundUser aroudUser;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public PlayerInfo getAroudPlayer() {
		return aroudPlayer;
	}

	public void setAroudPlayer(PlayerInfo aroudPlayer) {
		this.aroudPlayer = aroudPlayer;
	}

	public AroundUser getAroudUser() {
		return aroudUser;
	}

	public void setAroudUser(AroundUser aroudUser) {
		this.aroudUser = aroudUser;
	}
}
