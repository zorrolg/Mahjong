package com.citywar.gameobjects.mahjong.rule.simple;

import java.util.Collections;
import java.util.List;

import com.citywar.gameobjects.mahjong.PlayerLocation;
import com.citywar.gameobjects.mahjong.fan.FanType;
import com.citywar.gameobjects.mahjong.rule.wins.WinType;
import com.citywar.gameobjects.mahjong.game.GameContext;
import com.citywar.gameobjects.mahjong.rule.AbstractGameStrategy;

/**
 * 简单游戏规则。固定坐庄、没有和牌限制、和牌固定为1番。
 */
public class SimpleGameStrategy extends AbstractGameStrategy {

	@Override
	protected PlayerLocation nextZhuangLocation(GameContext context) {
		return PlayerLocation.EAST;
	}

	private static final List<WinType> WIN_TYPES = Collections.singletonList(NormalWinType.get());

	@Override
	public List<WinType> getAllWinTypes() {
		return WIN_TYPES;
	}

	private static final List<FanType> FAN_TYPES = Collections.singletonList(new SimpleFanType());

	@Override
	public List<FanType> getAllFanTypes() {
		return FAN_TYPES;
	}

}
