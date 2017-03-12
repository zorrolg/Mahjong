package com.citywar.gameobjects.mahjong;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.citywar.gameobjects.TileType;

/**
 * 牌的单元，即判断和牌时对牌的分组，如顺子、刻子、杠子、将牌等。
 * 
 */
public class TileUnit {
	private final TileUnitType type;
	private final Set<Tile> tiles;
	private List<TileType> tileTypes; // 排序过的
	private final TileUnitSource source;
	private final Tile gotTile;

	public enum TileUnitSource {
		SELF, GOT
	}

	public static TileUnit self(TileUnitType type, Collection<Tile> tiles) {
		return new TileUnit(type, tiles, TileUnitSource.SELF, null);
	}

	public static TileUnit got(TileUnitType type, Collection<Tile> tiles, Tile gotTile) {
		return new TileUnit(type, tiles, TileUnitSource.GOT, gotTile);
	}

	public TileUnit(TileUnitType type, Collection<Tile> tiles, TileUnitSource source, Tile gotTile) {
		this.type = type;
		this.tiles = tiles instanceof Set ? (Set<Tile>) tiles : new HashSet<>(tiles);
		this.source = source;
		this.gotTile = gotTile;
	}

	public TileUnitType getType() {
		return type;
	}

	public Set<Tile> getTiles() {
		return tiles;
	}

	public List<TileType> getTileTypes() {
		if (tileTypes == null)
			tileTypes = tiles.stream().map(Tile::type).sorted().collect(Collectors.toList());
		return tileTypes;
	}

	public TileUnitSource getSource() {
		return source;
	}

	public Tile getGotTile() {
		return gotTile;
	}

	@Override
	public String toString() {
		return "TileUnit [type=" + type + ", tiles=" + tiles + "]";
	}

}
