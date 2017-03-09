package com.citywar.gameobjects;

import com.citywar.gameobjects.mahjong.TileRank;
import com.citywar.gameobjects.mahjong.TileRank.HuaRank;
import com.citywar.gameobjects.mahjong.TileRank.NumberRank;
import com.citywar.gameobjects.mahjong.TileRank.ZiRank;

public enum TileType {

	WAN(0, 0x04, 0x27) {
		@Override
		public Class<NumberRank> rank() {
			return NumberRank.class;
		}
	},

	TONG(1, 0x28, 0x4C) {
		@Override
		public Class<NumberRank> rank() {
			return NumberRank.class;
		}
	},

	TIAO(2, 0x4D, 0x71) {
		@Override
		public Class<NumberRank> rank() {
			return NumberRank.class;
		}
	},

	ZI_HUA(3, 0x72, 0x96) {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends TileRank> rank() {
			byte calc = (byte) (this.getCode() & 0x63);
			calc = (byte) (calc >> 2);
			if (calc > 0x08) {
				return ZiRank.class;
			} else {
				return HuaRank.class;
			}
		}
	};

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

	public Class<? extends TileRank> rank() {
		return null;
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


	public TileType type() {
		return this;
	}

	public static TileType type(int code) {
		for (TileType t : values()) {
			if (t.code == code) {
				return t;
			}
		}
		return null;
	}
	public static void main(String[] args) {
		for (TileType string : values()) {
			System.out.println(string.toString());
		}
	}

}
