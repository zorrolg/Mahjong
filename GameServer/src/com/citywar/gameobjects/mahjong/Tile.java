package com.citywar.gameobjects.mahjong;

import java.util.List;

import com.google.common.collect.Lists;

public class Tile {

	public byte code ;

	public TileType type;

	public static List<Tile> all;


	//	public static final Tile W1_1 = new Tile(0x04);
	//	public static final Tile W1_2 = new Tile(0x05);
	//	public static final Tile W1_3 = new Tile(0x06);
	//	public static final Tile W1_4 = new Tile(0x07);
	//
	//	public static final Tile W2_1 = new Tile(0x08);
	//	public static final Tile W2_2 = new Tile(0x09);
	//	public static final Tile W2_3 = new Tile(0x0A);
	//	public static final Tile W2_4 = new Tile(0x0B);
	//
	//	public static final Tile W3_1 = new Tile(0x0C);
	//	public static final Tile W3_2 = new Tile(0x0D);
	//	public static final Tile W3_3 = new Tile(0x0E);
	//	public static final Tile W3_4 = new Tile(0x0F);
	//
	//	public static final Tile W4_1 = new Tile(0x10);
	//	public static final Tile W4_2 = new Tile(0x11);
	//	public static final Tile W4_3 = new Tile(0x12);
	//	public static final Tile W4_4 = new Tile(0x13);
	//
	//	public static final Tile W5_1 = new Tile(0x14);
	//	public static final Tile W5_2 = new Tile(0x15);
	//	public static final Tile W5_3 = new Tile(0x16);
	//	public static final Tile W5_4 = new Tile(0x17);
	//
	//	public static final Tile W6_1 = new Tile(0x18);
	//	public static final Tile W6_2 = new Tile(0x19);
	//	public static final Tile W6_3 = new Tile(0x1A);
	//	public static final Tile W6_4 = new Tile(0x1B);
	//
	//	public static final Tile W7_1 = new Tile(0x1C);
	//	public static final Tile W7_2 = new Tile(0x1D);
	//	public static final Tile W7_3 = new Tile(0x1E);
	//	public static final Tile W7_4 = new Tile(0x1F);
	//
	//	public static final Tile W8_1 = new Tile(0x20);
	//	public static final Tile W8_2 = new Tile(0x21);
	//	public static final Tile W8_3 = new Tile(0x22);
	//	public static final Tile W8_4 = new Tile(0x23);
	//
	//	public static final Tile W9_1 = new Tile(0x24);
	//	public static final Tile W9_2 = new Tile(0x25);
	//	public static final Tile W9_3 = new Tile(0x26);
	//	public static final Tile W9_4 = new Tile(0x27);

	static {
		all = Lists.newArrayListWithCapacity(110);
		for (TileType type : TileType.values()) {
			byte min = type.getMin();
			byte max = type.getMax();
			for (byte code = min; code <= max; code++) {
				all.add(new Tile(code, type));
			}
		}
	}

	public Tile(byte code, TileType type) {
		this.code = code;
		this.type = type;
	}

	public Tile(byte code) {
		this.code = code;
	}

	public byte getTileTypeIndex() {
		byte type = (byte) ((this.code >> 6) & 0x03);
		return type;
	}

	public byte getTileSubType() {
		byte type = (byte) ((this.code >> 2) & 0x0F);
		return type;
	}

	/**
	 * 取中间三位
	 * @return
	 */
	public byte getTileType() {
		byte type = (byte) ((this.code >> 2) & 0x3F);
		return type;
	}

	/**
	 * @return 0，1，2，3  牌的数量下标
	 */
	public byte getTileIndex() {
		byte type = (byte) (this.code & 0x03);
		return type;
	}

	/**
	 * 该牌是此花色第几张牌
	 */
	public byte getTileNum() {
		return (byte)(getTileIndex() + 1);
	}

	public static void main(String[] args) {
		//		System.out.println(Tile.W3_4.code);
		//		System.out.println(Tile.W3_4.getTileTypeIndex());
		//		System.out.println(Tile.W3_4.getTileSubType());
		//		System.out.println(Tile.W3_4.getTileType());
		//		System.out.println(Tile.W3_4.getTileNum());
		//		System.out.println(Tile.W3_4.getTileType());

		System.out.println(all);
	}
}
