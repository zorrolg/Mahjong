package com.citywar.gameobjects.mahjong.game;

import java.io.Serializable;
import java.util.Map;

import com.citywar.gameobjects.mahjong.PlayerInfo;
import com.citywar.gameobjects.mahjong.PlayerLocation;
import com.citywar.gameobjects.mahjong.Tile;
import com.citywar.gameobjects.mahjong.fan.FanType;

/**
 * 一局游戏的结果。winnerLocation为null表示流局。
 */
public class GameResult implements Serializable {
	private static final long serialVersionUID = 5927092445320750860L;

	private final Map<PlayerLocation, PlayerInfo> playerInfos;
	private final PlayerLocation zhuangLocation;

	private PlayerLocation winnerLocation;
	private Tile winTile;
	private PlayerLocation paoerLocation;
	private Map<FanType, Integer> fans;

	public GameResult(Map<PlayerLocation, PlayerInfo> playerInfos,
			PlayerLocation zhuangLocation) {
		this.playerInfos = playerInfos;
		this.zhuangLocation = zhuangLocation;
	}

	public PlayerLocation getWinnerLocation() {
		return winnerLocation;
	}

	public void setWinnerLocation(PlayerLocation winnerLocation) {
		this.winnerLocation = winnerLocation;
	}

	public Tile getWinTile() {
		return winTile;
	}

	public void setWinTile(Tile winTile) {
		this.winTile = winTile;
	}

	public PlayerLocation getPaoerLocation() {
		return paoerLocation;
	}

	public void setPaoerLocation(PlayerLocation paoerLocation) {
		this.paoerLocation = paoerLocation;
	}

	public Map<FanType, Integer> getFans() {
		return fans;
	}

	public void setFans(Map<FanType, Integer> fans) {
		this.fans = fans;
	}

	public Map<PlayerLocation, PlayerInfo> getPlayerInfos() {
		return playerInfos;
	}

	public PlayerLocation getZhuangLocation() {
		return zhuangLocation;
	}

}
