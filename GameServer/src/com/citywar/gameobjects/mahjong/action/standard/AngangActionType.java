package com.citywar.gameobjects.mahjong.action.standard;

import static com.citywar.gameobjects.mahjong.TileGroupType.*;

import java.util.Set;
import java.util.function.Predicate;

import com.citywar.gameobjects.mahjong.PlayerInfo;
import com.citywar.gameobjects.mahjong.PlayerLocation;
import com.citywar.gameobjects.mahjong.Tile;
import com.citywar.gameobjects.mahjong.TileGroup;
import com.citywar.gameobjects.mahjong.action.AbstractActionType;
import com.citywar.gameobjects.mahjong.game.GameContext;
import com.citywar.gameobjects.mahjong.game.GameContext.PlayerView;

/**
 * 动作类型“暗杠”。
 * 
 * @author blovemaple <blovemaple2010(at)gmail.com>
 */
public class AngangActionType extends AbstractActionType {

	@Override
	public boolean canPass(GameContext context, PlayerLocation location) {
		return true;
	}

	@Override
	protected Predicate<Integer> getAliveTileSizePrecondition() {
		return size -> size % 3 == 2;
	}

	@Override
	protected int getActionTilesSize() {
		return ANGANG_GROUP.size();
	}

	@Override
	protected boolean isLegalActionWithPreconition(PlayerView context,
			Set<Tile> tiles) {
		return ANGANG_GROUP.isLegalTiles(tiles);
	}

	@Override
	protected void doLegalAction(GameContext context, PlayerLocation location,
			Set<Tile> tiles) {
		PlayerInfo playerInfo = context.getPlayerInfoByLocation(location);
		playerInfo.getAliveTiles().removeAll(tiles);
		playerInfo.getTileGroups().add(new TileGroup(ANGANG_GROUP, tiles));
	}

}
