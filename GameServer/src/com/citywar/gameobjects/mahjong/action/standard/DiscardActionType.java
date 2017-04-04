package com.citywar.gameobjects.mahjong.action.standard;

import java.util.Set;
import java.util.function.Predicate;

import com.citywar.gameobjects.mahjong.PlayerInfo;
import com.citywar.gameobjects.mahjong.PlayerLocation;
import com.citywar.gameobjects.mahjong.Tile;
import com.citywar.gameobjects.mahjong.action.AbstractActionType;
import com.citywar.gameobjects.mahjong.game.GameContext;
import com.citywar.gameobjects.mahjong.game.GameContext.PlayerView;

/**
 * 动作类型“打牌”。
 */
public class DiscardActionType extends AbstractActionType {

	@Override
	public boolean canPass(GameContext context, PlayerLocation location) {
		return false;
	}

	@Override
	protected Predicate<Integer> getAliveTileSizePrecondition() {
		return size -> size % 3 == 2;
	}

	@Override
	protected int getActionTilesSize() {
		return 1;
	}

	@Override
	protected boolean isLegalActionWithPreconition(PlayerView context,
			Set<Tile> tiles) {
		if (!context.getMyInfo().isTing()) {
			// 没听牌时，所有aliveTiles都可以打出
			return true;
		} else {
			// 听牌后只允许打出最后摸的牌
			Tile justDrawed = context.getJustDrawedTile();
			return justDrawed != null
					&& justDrawed.equals(tiles.iterator().next());
		}
	}

	@Override
	protected void doLegalAction(GameContext context, PlayerLocation location, Set<Tile> tiles) {
		PlayerInfo playerInfo = context.getPlayerInfoByLocation(location);
		playerInfo.getAliveTiles().removeAll(tiles);
		playerInfo.getDiscardedTiles().addAll(tiles);
	}

}
