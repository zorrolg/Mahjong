package com.citywar.gameobjects.mahjong;

import java.util.function.Supplier;

import com.citywar.gameobjects.mahjong.rule.GameStrategy;
import com.citywar.gameobjects.mahjong.rule.TimeLimitStrategy;
import com.citywar.gameobjects.mahjong.rule.simple.SimpleGameStrategy;
import com.citywar.gameobjects.mahjong.game.MahjongGame;

/**
 * 本地游戏。
 * 
 * @author blovemaple <blovemaple2010(at)gmail.com>
 */
public class LocalGame {
	private GameStrategy gameStrategy = new SimpleGameStrategy();
	private TimeLimitStrategy timeStrategy = TimeLimitStrategy.NO_LIMIT;
	private Class<? extends Player> botPlayerClass = BarBot.class;

	private Player localPlayer;
	private Supplier<Boolean> newGameChecker;

	/**
	 * 新建一个实例。
	 * 
	 * @param localPlayer
	 *            本地玩家
	 * @param newGameChecker
	 *            一局结束后决定是否开始新的一局的函数
	 */
	public LocalGame(Player localPlayer, Supplier<Boolean> newGameChecker) {
		this.localPlayer = localPlayer;
		this.newGameChecker = newGameChecker;
	}

	public void play() throws InterruptedException {
		try {
			MahjongTable table = new MahjongTable();
			table.init();
			table.setPlayer(PlayerLocation.EAST, localPlayer);
			table.setPlayer(PlayerLocation.SOUTH, botPlayerClass.newInstance());
			table.setPlayer(PlayerLocation.WEST, botPlayerClass.newInstance());
			table.setPlayer(PlayerLocation.NORTH, botPlayerClass.newInstance());

			MahjongGame game = new MahjongGame(gameStrategy, timeStrategy);
			while (newGameChecker.get()) {
				game.play(table);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
