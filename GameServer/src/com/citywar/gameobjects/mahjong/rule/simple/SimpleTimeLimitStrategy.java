package com.citywar.gameobjects.mahjong.rule.simple;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.citywar.gameobjects.mahjong.PlayerLocation;
import com.citywar.gameobjects.mahjong.action.ActionType;
import com.citywar.gameobjects.mahjong.game.GameContext;
import com.citywar.gameobjects.mahjong.rule.TimeLimitStrategy;

/**
 * 简单的限时策略，采用固定限时。
 */
public class SimpleTimeLimitStrategy implements TimeLimitStrategy {
	private final int limit;

	/**
	 * 新建一个实例。
	 * 
	 * @param discardLimit
	 *            打牌限时
	 * @param cpkLimit
	 *            其他操作限时
	 * @param timeUnit
	 *            时间单位
	 */
	public SimpleTimeLimitStrategy(int limit, TimeUnit timeUnit) {
		this.limit = (int) TimeUnit.SECONDS.convert(limit, timeUnit);
	}

	@Override
	public Integer getLimit(GameContext context,
			Map<PlayerLocation, Set<ActionType>> choises) {
		return limit;
	}

}
