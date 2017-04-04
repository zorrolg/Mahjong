package com.citywar.gameobjects.mahjong.action.standard;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import com.citywar.gameobjects.mahjong.MahjongTable;
import com.citywar.gameobjects.mahjong.PlayerInfo;
import com.citywar.gameobjects.mahjong.PlayerLocation;
import com.citywar.gameobjects.mahjong.PlayerLocation.Relation;
import com.citywar.gameobjects.mahjong.Tile;
import com.citywar.gameobjects.mahjong.action.Action;
import com.citywar.gameobjects.mahjong.action.ActionType;
import com.citywar.gameobjects.mahjong.action.IllegalActionException;
import com.citywar.gameobjects.mahjong.game.GameContext;

/**
 * 动作类型“发牌”。<br>
 * 发牌动作不由玩家执行，只实现doAction方法。
 */
public class DealActionType implements ActionType {

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
		MahjongTable table = context.getTable();
		PlayerLocation zhuang = context.getZhuangLocation();
		for (int i = 0; i < 4; i++) {
			int drawCount = i < 3 ? 4 : 1;
			Stream.of(Relation.values()).map(zhuang::getLocationOf)
					.map(context::getPlayerInfoByLocation)
					.map(PlayerInfo::getAliveTiles)
					.forEach(aliveTiles -> aliveTiles
							.addAll(table.draw(drawCount)));
		}
		context.getPlayerInfoByLocation(zhuang).getAliveTiles()
				.addAll(table.draw(1));
	}

}
