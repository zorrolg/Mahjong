package com.citywar.gameobjects;

import com.citywar.gameobjects.mahjong.TileRank;
import com.citywar.gameobjects.mahjong.TileRank.HuaRank;
import com.citywar.gameobjects.mahjong.TileRank.NumberRank;
import com.citywar.gameobjects.mahjong.TileRank.ZiRank;

public enum TileType {

	WAN(0x00, 0x04, 0x27) {
		@Override
		public Class<NumberRank> rank() {
			return NumberRank.class;
		}
		
		@Override
		public String toString() {
			return W;
		}
	},

//	TONG(1, 0x28, 0x4C) {
	TONG(0x40, 0x04, 0x27) {
		@Override
		public Class<NumberRank> rank() {
			return NumberRank.class;
		}
		
		@Override
		public String toString() {
			return T;
		}
	},

//	TIAO(2, 0x4D, 0x71) {
	TIAO(0x80, 0x04, 0x27) {
		@Override
		public Class<NumberRank> rank() {
			return NumberRank.class;
		}
		@Override
		public String toString() {
			return TO;
		}
	},

//	ZI_HUA(0xC0, 0x72, 0x96) {
	ZI_HUA(0xC0, 0x04, 0x3C) {
		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends TileRank> rank() {
			byte type = (byte) ((this.getCode() >> 2));
			type = (byte) (type & 0x0F);
			if (type >= 0x08) {
				return HuaRank.class;
			} else {
				return ZiRank.class;
			}
		}
		
		@Override
		public String toString() {
			if(this.rank() == ZiRank.class) {
				return Z;
			} else {
				return H;
			}
		}
	};

	private byte code;

	private byte min;

	private byte max;

	
	private static final String W = "萬";
	private static final String T = "筒";
	private static final String TO = "條";
	private static final String Z = "字";
	private static final String H = "花";
	
	private TileType(int code, int min, int max) {
		this.code = (byte) code;
		this.min = (byte) min;
		this.max = (byte) max;
	}

	public boolean isInRange(byte type) {
		if (type >= min && type < max) {
			return true;
		} else
			return false;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends TileRank> rank() {
		throw new RuntimeException("method must be override!");
	}
	
	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
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
