package com.citywar.gameobjects.mahjong;

import java.util.List;

import com.citywar.gameobjects.TileType;
import com.google.common.collect.Lists;

public class Tile {

	private byte code ;

	public TileType type;

	public static List<Tile> all;

	static {
		all = Lists.newArrayListWithCapacity(144);
		for (TileType type : TileType.values()) {
			byte min = type.getMin();
			byte max = type.getMax();
			for (byte code = min; code <= max; ) {
				Tile tile = new Tile(code, type);
				//如果是字花段，0x1F代表已经到花牌
				if (type == TileType.ZI_HUA && code > 0x1F) {
					code = (byte)(code + 0x4);
				} else {
					code++;
				}
				all.add(tile);
			}
		}
	}
	

	public Tile(byte code, TileType type) {
		this.code = (byte)(type.getCode() ^ code);
		this.type = type;
	}

	public Tile(byte code) {
		this.code = code;
	}
	
	
	public int getCode() {
		return Byte.toUnsignedInt(code);
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

	
	public int getTileSubTypeOffset(int offset) {
		return getTileSubType() + offset;
	}

	/**
	 *  牌的数量下标
	 * @return 0，1，2，3 
	 */
	public byte getTileIndex() {
		byte type = (byte) (this.code & 0x03);
		return type;
	}

	
    public static byte toUnsignedByte(byte x) {
        return (byte)(x & 0xFF);
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
	
	public String toBinaryString() {
		return Integer.toBinaryString(this.getCode());
	}
	
	@Override
	public String toString() {
//		return Integer.toUnsignedString(Tile.toUnsignedByte(this.code), 2);
//		return String.format("0x%s", Integer.toBinaryString(Tile.toUnsignedByte(this.code)));
		return Integer.toString(this.getTileSubType(), 10) + this.type.toString() + this.getTileNum() + " " + toBinaryString();
	}
	
	@Deprecated
	public Tile(byte code, TileType type, int number) {
		this.code = (byte)(type.getCode() ^ code ^ (byte)number);
		this.type = type;
		this.type.setCode(this.code);
	}
	
	public static void main(String[] args) {
		System.out.println(all);
		
		Tile tile = new Tile((byte)0x27, TileType.TIAO);
		
		System.out.println(tile);
		System.exit(0);
	}
}
