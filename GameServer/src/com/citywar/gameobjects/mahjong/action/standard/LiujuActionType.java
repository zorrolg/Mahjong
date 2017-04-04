package com.citywar.gameobjects.mahjong.action.standard;

import java.util.Collection;
import java.util.Set;

import com.citywar.gameobjects.mahjong.PlayerLocation;
import com.citywar.gameobjects.mahjong.Tile;
import com.citywar.gameobjects.mahjong.action.Action;
import com.citywar.gameobjects.mahjong.action.ActionType;
import com.citywar.gameobjects.mahjong.action.IllegalActionException;
import com.citywar.gameobjects.mahjong.game.GameContext;
import com.citywar.gameobjects.mahjong.game.GameResult;

/**
 * 动作类型“流局”。
 */
public class LiujuActionType implements ActionType {

	@Override
	public boolean canDo(GameContext context, PlayerLocation location) {
		// 不作为常规动作
		return false;
	}

	@Override
	public boolean canPass(GameContext context, PlayerLocation location) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Set<Tile>> getLegalActionTiles(
			GameContext.PlayerView context) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isLegalAction(GameContext context, PlayerLocation location, Action action) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void doAction(GameContext context, PlayerLocation location, Action action) throws IllegalActionException {
		GameResult result = new GameResult(context.getTable().getPlayerInfos(),
				context.getZhuangLocation());
		context.setGameResult(result);
	}

}
