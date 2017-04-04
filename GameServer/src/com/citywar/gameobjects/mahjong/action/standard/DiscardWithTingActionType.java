package com.citywar.gameobjects.mahjong.action.standard;

import static com.citywar.gameobjects.mahjong.utils.MyUtils.*;

import java.util.HashSet;
import java.util.Set;

import com.citywar.gameobjects.mahjong.PlayerInfo;
import com.citywar.gameobjects.mahjong.PlayerLocation;
import com.citywar.gameobjects.mahjong.Tile;
import com.citywar.gameobjects.mahjong.game.GameContext;
import com.citywar.gameobjects.mahjong.game.GameContext.PlayerView;
import com.citywar.gameobjects.mahjong.rule.GameStrategy;
import com.citywar.gameobjects.mahjong.rule.wins.WinInfo;

/**
 * 动作类型“打牌的同时听牌”。与打牌动作的区别是：
 * <li>合法性要判断打出后是否可以听；
 * <li>执行动作时要设置听牌状态。
 */
public class DiscardWithTingActionType extends DiscardActionType {

	@Override
	protected boolean isLegalActionWithPreconition(PlayerView context, Set<Tile> tiles) {
		GameStrategy strategy = context.getGameStrategy();
		PlayerInfo playerInfo = context.getMyInfo();
		Set<Tile> remainAliveTiles = new HashSet<>(playerInfo.getAliveTiles());
		remainAliveTiles.removeAll(tiles);

		return
		// 获取所有牌的流
		strategy.getAllTiles().stream()
				// 只留下id==0的牌
				.filter(tileToGet -> tileToGet.getTileIndex() == 0)
				// 与打出动作牌后的aliveTiles合并，看任何一种合并后的aliveTiles能否和牌
				.anyMatch(tileToGet -> {
					WinInfo winInfo = WinInfo.fromPlayerTiles(playerInfo, tileToGet, false);
					winInfo.setAliveTiles(mergedSet(remainAliveTiles, tileToGet));
					winInfo.setContextView(context);
					return strategy.canWin(winInfo);
				});
	}

	@Override
	protected void doLegalAction(GameContext context, PlayerLocation location, Set<Tile> tiles) {
		super.doLegalAction(context, location, tiles);
		context.getPlayerInfoByLocation(location).setTing(true);
	}

}
