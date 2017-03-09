package com.citywar.gameobjects.mahjong;

public enum TileType {

	WAN(0, 0x04, 0x27),

	TONG(1, 0x28, 0x4C),

	TIAO(2, 0x4D, 0x71),

	ZI(3, 0x72, 0x96);

	private byte code;

	private byte min;

	private byte max;

	private TileType(int min, int max) {
		this.min = (byte) min;
		this.max = (byte) max;
	}

	private TileType(int code, int min, int max) {
		this.code = (byte) code;
		this.min = (byte) min;
		this.max = (byte) max;
	}

	public boolean isInRange(byte code) {
		if (code >= min && code < max) {
			return true;
		} else
			return false;
	}

	public byte getCode() {
		return code;
	}

	public byte getMin() {
		return min;
	}

	public byte getMax() {
		return max;
	}

	public static void main(String[] args) {
		for (TileType string : values()) {
			System.out.println(string.toString());
		}
	}

}
