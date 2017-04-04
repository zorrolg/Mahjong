package com.citywar.gameobjects.mahjong.action.standard;

import static com.citywar.gameobjects.mahjong.action.standard.StandardActionType.*;
import static com.citywar.gameobjects.mahjong.PlayerLocation.Relation.*;

import java.util.Set;
import java.util.function.BiPredicate;

import com.citywar.gameobjects.mahjong.PlayerLocation;
import com.citywar.gameobjects.mahjong.Tile;
import com.citywar.gameobjects.mahjong.action.AbstractActionType;
import com.citywar.gameobjects.mahjong.action.ActionAndLocation;
import com.citywar.gameobjects.mahjong.game.GameContext;
import com.citywar.gameobjects.mahjong.game.GameContext.PlayerView;

/**
 * 动作类型“摸牌”。
 */
public class DrawActionType extends AbstractActionType {

	@Override
	public boolean canPass(GameContext context, PlayerLocation location) {
		return false;
	}

	@Override
	protected BiPredicate<ActionAndLocation, PlayerLocation> getLastActionPrecondition() {
		// 必须是上家打牌后
		return (al, location) -> DISCARD.matchBy(al.getActionType())
				&& location.getRelationOf(al.getLocation()) == PREVIOUS;
	}

	@Override
	protected int getActionTilesSize() {
		return 0;
	}

	@Override
	protected boolean isLegalActionWithPreconition(PlayerView context,
			Set<Tile> tiles) {
		// 牌墙中必须有牌才能摸
		return context.getTableView().getTileWallSize() > 0;
	}

	@Override
	protected void doLegalAction(GameContext context, PlayerLocation location, Set<Tile> tiles) {
		Tile tile = context.getTable().draw(1).get(0);
		context.getPlayerInfoByLocation(location).getAliveTiles().add(tile);
		context.getPlayerInfoByLocation(location).setLastDrawedTile(tile);
	}

}
