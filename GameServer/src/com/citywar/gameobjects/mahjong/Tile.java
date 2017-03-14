package com.citywar.gameobjects.mahjong;

import java.util.List;

import com.citywar.gameobjects.TileType;
import com.citywar.gameobjects.mahjong.TileRank.NumberRank;
import com.citywar.gameobjects.mahjong.TileRank.ZiRank;
import com.citywar.gameobjects.mahjong.TileRank.HuaRank;
import com.google.common.collect.Lists;

public class Tile {

	public byte code ;

	public TileType type;

	public static List<Tile> all;

	static {
		all = Lists.newArrayListWithCapacity(110);
		for (TileType type : TileType.values()) {
			byte min = type.getMin();
			byte max = type.getMax();
			for (byte code = min; code <= max; code++) {
				int index = all.size() / 4 + 1;
				if (type.rank() == HuaRank.class) {
					index = 1;
				}
				Tile tile = new Tile(code, type, index);
				all.add(tile);
			}
		}
	}
	
	public Tile(byte code, TileType type, int number) {
		this.code = (byte)(type.getCode() ^ code ^ (byte)number);
		this.type = type;
	}

	public Tile(byte code, TileType type) {
		this.code = (byte)(type.getCode() ^ code);
		this.type = type;
	}

	public Tile(byte code) {
		this.code = code;
	}
	/**
	 * 花色下標
	 * <ul>
	 * 	<li>万0</li>
	 * 	<li>条1</li>
	 * 	<li>筒2</li>
	 * 	<li>字3</li>
	 * </ul>
	 * @return
	 */
	public byte getTileTypeIndex() {
		byte type = (byte) ((this.code >> 6) & 0x03);
		return type;
	}

	/**
	 * 花色的子类型
	 * 万 （1-9万）
	 * 筒 （1-9万）
	 * 条（1-9万）
	 * @return
	 */
	public int getTileSubType() {
		byte type = (byte) ((this.code >> 2));
		type = (byte) (type & 0x0F);
		return new Byte(type).intValue();
	}


	/**
	 *  牌的数量下标
	 * @return 0，1，2，3 
	 */
	public byte getTileIndex() {
		byte type = (byte) (this.code & 0x03);
		return type;
	}

	/**
	 * 该牌是此花色第几张牌
	 * @return 1,2,3,4
	 */
	public byte getTileNum() {
		return (byte)(getTileIndex() + 1);
	}


	public TileType type() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return Integer.toBinaryString(Byte.toUnsignedInt(this.code));
//		return Integer.toHexString(this.getTileSubType()) + this.type.toString() + this.getTileNum();
	}
	
	public static void main(String[] args) {
		System.out.println(all);
	}
}
