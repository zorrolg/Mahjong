package com.citywar.gameobjects.mahjong;

import java.util.Collection;

/**
 * 牌的单元的类型。
 * 
 */
public interface TileUnitType {

	/**
	 * 返回一个单元中有几张牌。
	 */
	int size();

	/**
	 * 判断指定牌集合是否是合法的单元。
	 */
	boolean isLegalTiles(Collection<Tile> tiles);

	/**
	 * 判断指定牌型集合是否是合法的单元。
	 */
	boolean isLegalTileTypes(Collection<TileType> types);

}