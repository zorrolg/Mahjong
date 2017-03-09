package com.citywar.gameobjects.mahjong;

/**
 * 牌的花色下面的小种类。如万牌中的“一”、“二”，风牌中的“东”、“南”等。<br>
 * 实现类必须有values()方法返回相应的种类数组（枚举类就可以）。因为TileSuit会用values()方法获取对应的种类。
 * 
 * @author blovemaple <blovemaple2010(at)gmail.com>
 */
public interface TileRank<T extends TileRank<T>> extends Comparable<T> {
	/**
	 * 返回名称。用于唯一标识。
	 */
	public String name();

	/**
	 * 万、条、饼等花色使用的数字种类。
	 */
	public enum NumberRank implements TileRank<NumberRank> {
		// 顺序勿动！ofNumber依赖顺序
		ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9);

		private final int number;

		private NumberRank(int number) {
			this.number = number;
		}

		public int number() {
			return number;
		}

		public static NumberRank ofNumber(int number) {
			return values()[number - 1];
		}
	}

	/**
	 * 字牌的种类。
	 * <ul>
	 * 	<li>东
	 * 	<li>南
	 * 	<li>西
	 * 	<li>北
	 * </ul>
	 */
	public enum ZiRank implements TileRank<ZiRank> {
		EAST, SOUTH, WEST, NORTH, CENTRE, FORTUNE, BLANK
	}

	/**
	 * 花牌的种类。
	 * <ul>
	 * 	<li>春
	 * 	<li>夏
	 * 	<li>秋
	 * 	<li>冬
	 * 	<li>梅
	 * 	<li>兰
	 * 	<li>竹
	 * 	<li>菊
	 * </ul>
	 */
	public enum HuaRank implements TileRank<HuaRank> {
		SPRING, SUMMER, AUTUMN, WINTER, PLUM, ORCHID, BAMBOO, CHRYSANTHEMUM
	}
}
