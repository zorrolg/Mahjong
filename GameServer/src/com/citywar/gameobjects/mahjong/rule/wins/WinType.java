package com.citywar.gameobjects.mahjong.rule.wins;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.citywar.gameobjects.mahjong.PlayerInfo;
import com.citywar.gameobjects.mahjong.Tile;
import com.citywar.gameobjects.mahjong.TileType;
import com.citywar.gameobjects.mahjong.TileUnit;

/**
 * 和牌类型。
 */
public interface WinType {
	
	/**
	 * 全部解析成可以和牌的完整的TileUnit集合的列表并填入和牌信息并返回，失败填入并返回空列表。
	 * 
	 * @param winInfo
	 *            和牌信息
	 */
	public List<List<TileUnit>> parseWinTileUnits(WinInfo winInfo);

	/**
	 * 判断根据此和牌类型是否可以和牌。如果可以和牌，则向winInfo填入和牌信息。<br>
	 * 默认实现为调用parseWinTileUnits解析。
	 * 
	 * @param winInfo
	 *            和牌信息
	 * @return 是否可以和牌
	 */
	public default boolean match(WinInfo winInfo) {
		List<List<TileUnit>> units = parseWinTileUnits(winInfo);
		return units != null && !units.isEmpty();
	}

	/**
	 * 用于机器人，返回建议打出的牌，即从手牌中排除掉明显不应该打出的牌并返回。返回的列表按建议的优先级从高到低排列。
	 */
	public List<Tile> getDiscardCandidates(Set<Tile> aliveTiles, Collection<Tile> candidates);

	/**
	 * 用于机器人，获取ChangingForWin的流，移除changeCount个牌，增加(changeCount+1)个牌。
	 */
	public Stream<ChangingForWin> changingsForWin(PlayerInfo playerInfo, int changeCount, Collection<Tile> candidates);

	/**
	 * 一种结果是和牌的换牌方法，移除removedTiles并增加addedTiles。
	 */
	public static class ChangingForWin {
		public Set<Tile> removedTiles, addedTiles;
		private int hashCode;

		public ChangingForWin(Set<Tile> removedTiles, Set<Tile> addedTiles) {
			this.removedTiles = removedTiles;
			this.addedTiles = addedTiles;
		}

		@Override
		public int hashCode() {
			if (hashCode == 0) {
				final int prime = 31;
				int result = 1;
				result = prime * result + addedTiles.stream().map(Tile::type).mapToInt(TileType::hashCode).sum();
				result = prime * result + removedTiles.stream().map(Tile::type).mapToInt(TileType::hashCode).sum();
				hashCode = result;
			}
			return hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof ChangingForWin))
				return false;
			ChangingForWin other = (ChangingForWin) obj;
			return hashCode() == other.hashCode();
		}

		@Override
		public String toString() {
			return "ChangingForWin [removedTiles=" + removedTiles + ", addedTiles=" + addedTiles + "]";
		}

	}
}
