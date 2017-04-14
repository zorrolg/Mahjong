package com.citywar.gameobjects.mahjong;

import java.util.List;

import com.citywar.gameobjects.mahjong.TileRank.HuaRank;
import com.citywar.gameobjects.mahjong.TileRank.NumberRank;
import com.citywar.gameobjects.mahjong.TileRank.ZiRank;
import com.google.common.collect.Lists;

public class Tile {

	/**
	 * tile 的code
	 */
	private byte code;

	private TileType type;

	private TileRank<? extends TileRank<?>> rank;
	
	private TileSuit suit;

	public static List<Tile> all;

	static {
		all = Lists.newArrayListWithCapacity(144);
		for (TileType type : TileType.values()) {
			byte min = type.getMin();
			byte max = type.getMax();
			for (byte code = min; code <= max;) {
				Tile tile = new Tile(code, type);
				int subType = code / 4;
				// 如果是字花段，0x1F代表已经到花牌
				if (code <= 0x1F) {
					code++;
					if (type.getClass() != TileType.ZI_HUA.getClass()) {
						NumberRank rank = NumberRank.valueOf(subType);
						tile.rank = rank;
					} else {
						ZiRank rank = ZiRank.valueOf(subType);
						tile.rank = rank;
					}
				} else {
					code = (byte) (code + 0x4);
					HuaRank rank = HuaRank.valueOf(subType);
					tile.rank = rank;
				}
				all.add(tile);
			}
		}
	}

	public Tile(byte code, TileType type) {
		this.code = (byte) (type.getCode() ^ code);
		this.type = type;
		suit = new TileSuit(type, rank);
	}

	public Tile(byte code, TileType type, int number) {
		this.code = (byte) (type.getCode() ^ code);
		this.code = (byte) (code ^ (byte) number);
		this.type = type;
		suit = new TileSuit(type, rank);
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
	 * 根据传入的offset获取新的tile e.p: 8條3 10100010 offset = -1 7條3 00011110
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

	/**
	 * 根据类型和子类型获取code
	 * 
	 * @return
	 */
	public byte subTypeCode() {
		int code = (this.getCode() & 0xFC);
		return (byte) (code >> 2);
	}

	/**
	 * 该牌是此花色第几张牌
	 * 
	 * @return 1,2,3,4
	 */
	public byte tileNum() {
		return (byte) (getTileIndex() + 1);
	}

	public TileType type() {
		return this.type;
	}
	
	@SuppressWarnings("rawtypes")
	public TileRank<? extends TileRank> rank() {
		return this.rank;
	}

	public TileSuit suit() {
		return this.suit;
	}
	public String toBinaryString() {
		return Integer.toBinaryString(this.getCode());
	}

	@Override
	public String toString() {
		 return this.rank + this.type.toString() + tileNum() + " : " + toBinaryString();
		// return String.format("0x%s",
		// Integer.toBinaryString(Tile.toUnsignedByte(this.code)));
//		return Integer.toString(this.getTileSubType(), 10) + this.type.toString() + this.tileNum() + " "
//				+ toBinaryString();
	}

	public class TileSuit {
		TileType type;
		TileRank<? extends TileRank<?>> rank;
		TileSuit(TileType type, TileRank<? extends TileRank<?>> rank) {
			this.type = type;
			this.rank = rank;
		}
	}
	public static void main(String[] args) {
        System.out.println(all);
    }
}
