package com.citywar.gameobjects.mahjong.rule;

import java.util.Map;
import java.util.Set;

import com.citywar.gameobjects.mahjong.PlayerLocation;
import com.citywar.gameobjects.mahjong.action.ActionType;
import com.citywar.gameobjects.mahjong.game.GameContext;

/**
 * 限时策略。
 */
@FunctionalInterface
public interface TimeLimitStrategy {
	/**
	 * 不限时。
	 */
	public static final TimeLimitStrategy NO_LIMIT = (context, choises) -> null;

	/**
	 * 根据上下文返回限时。
	 * 
	 * @return 限时（单位：秒），若不限制则返回null。
	 */
	Integer getLimit(GameContext context,
			Map<PlayerLocation, Set<ActionType>> choises);
}
