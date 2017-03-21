package com.citywar.gameobjects.mahjong;

import java.util.List;

import com.citywar.gameobjects.TileType;
import com.google.common.collect.Lists;

public class Tile {

	private byte code;

	public TileType type;

	public static List<Tile> all;

	static {
		all = Lists.newArrayListWithCapacity(144);
		for (TileType type : TileType.values()) {
			byte min = type.getMin();
			byte max = type.getMax();
			for (byte code = min; code <= max;) {
				Tile tile = new Tile(code, type);
				// 如果是字花段，0x1F代表已经到花牌
				if (type == TileType.ZI_HUA && code > 0x1F) {
					code = (byte) (code + 0x4);
				} else {
					code++;
				}
				all.add(tile);
			}
		}
	}

	public Tile(byte code) {
		this.code = code;
	}

	public Tile(byte code, TileType type) {
		this.code = (byte) (type.getCode() ^ code);
		this.type = type;
	}

	public Tile(byte code, TileType type, int number) {
		this.code = (byte) (type.getCode() ^ code);
		this.code = (byte) (code ^ (byte) number);
		this.type = type;
	}

	public int getCode() {
		return Byte.toUnsignedInt(code);
	}

	/**
	 * 花色下標
	 * <ul>
	 * <li>万0</li>
	 * <li>条1</li>
	 * <li>筒2</li>
	 * <li>字3</li>
	 * </ul>
	 * 
	 * @return
	 */
	public byte getTileTypeIndex() {
		byte type = (byte) ((this.getCode() >> 6) & 0x03);
		return type;
	}

	/**
	 * 花色的子类型 万 （1-9万） 筒 （1-9万） 条（1-9万）
	 * 
	 * @return
	 */
	public int getTileSubType() {
		int offint = this.getCode() >> 2;
		return offint & 0x0F;
	}

	/**
	 * 
	 * 8條3 10100010 offset = -1 7條1 00011100
	 * 
	 * @param offset
	 * @return
	 */
	public Tile getTileOffset(int offset) {
		int tileSubTypeOffset = getTileSubTypeOffset(offset);
		tileSubTypeOffset = tileSubTypeOffset << 2;
		byte code = (byte) (type.getCode() ^ tileSubTypeOffset);
		// return new Tile(code, type);
		return new Tile(code, type, 3);
	}

	public int getTileSubTypeOffset(int offset) {
		int tileSubType = getTileSubType();
		int result = tileSubType + offset;
		boolean inRange = this.type.isInRange(result);
		if (!inRange) {
			throw new RuntimeException();
		}
		return result;
	}

	/**
	 * 牌的数量下标
	 * 
	 * @return 0，1，2，3
	 */
	public byte getTileIndex() {
		byte type = (byte) (this.code & 0x03);
		return type;
	}

	public static byte toUnsignedByte(byte x) {
		return (byte) (x & 0xFF);
	}

	/**
	 * 该牌是此花色第几张牌
	 * 
	 * @return 1,2,3,4
	 */
	public byte getTileNum() {
		return (byte) (getTileIndex() + 1);
	}

	public TileType type() {
		return this.type;
	}

	public String toBinaryString() {
		return Integer.toBinaryString(this.getCode());
	}

	@Override
	public String toString() {
		// return Integer.toUnsignedString(Tile.toUnsignedByte(this.code), 2);
		// return String.format("0x%s",
		// Integer.toBinaryString(Tile.toUnsignedByte(this.code)));
		return Integer.toString(this.getTileSubType(), 10) + this.type.toString() + this.getTileNum() + " "
				+ toBinaryString();
	}

	public static void main(String[] args) {
		System.out.println(all);

		Tile tile = new Tile((byte) 0x22, TileType.TIAO);

		System.out.println(tile);
		System.out.println(tile.getTileOffset(-1));
		System.exit(0);
	}
}
