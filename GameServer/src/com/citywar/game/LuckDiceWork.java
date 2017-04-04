package com.citywar.game;

import com.citywar.gameobjects.ai.DicePointFactory;
import com.citywar.util.ThreadSafeRandom;

public class LuckDiceWork {
	
	/**
	 * 客户端的位置
	 */
	private final byte[] place = new byte[]{0,1,3,4,2};

	/** 3颗相同的概率 */
	private int same_Point_3 = 20;
	/** 4 颗相同的概率 */
	private int same_Point_4 = 15;
	/** 5颗相同的概率 */
	private int same_Point_5 = 5;

	public LuckDiceWork(int same_Point_3, int same_Point_4, int same_Point_5) {
		this.same_Point_3 = same_Point_3;
		this.same_Point_4 = same_Point_4;
		this.same_Point_5 = same_Point_5;
	}
	ThreadSafeRandom random = new ThreadSafeRandom();

	/**
	 * 
	 * @return object[] 数组 大小为2 第一个值为参数的点数 类型为byte[] 第二个值为 骰子数最多的骰点个数
	 *         用这个值区分是否是幸运骰
	 */
	public Object[] run() {

		int randomNum = random.next(100);
		byte[] result = null;
		Byte maxSameCount = 0;

		if (randomNum <= same_Point_3)// 至少摇出3颗相同的
		{
			int randomNum2 = random.next(100);

			if (randomNum2 <= same_Point_4)// 至少摇出4颗相同的
			{
				int randomNum3 = random.next(100);

				if (randomNum3 <= same_Point_5)// 至少摇出5颗相同的
				{
					maxSameCount = 5;
				} else {
					maxSameCount = 4;
				}
			} else {
				maxSameCount = 3;
			}
		} else {

			maxSameCount = 2;
		}
		
		switch (maxSameCount) {
		case 3://摇出3颗相同的骰子
			result = DicePointFactory.createDiceSame3();
			break;
		case 4://摇出4颗相同的骰子
			result = DicePointFactory.createDiceSame4();
			break;
		case 5://摇出5颗相同的骰子
			result = DicePointFactory.createDiceSame5();
			break;
		default://摇出少于3颗相同的骰子
			result = DicePointFactory.createDiceSameLess3();
			break;
		}
		int index = random.next(0, 5);
		byte[] temp = new byte[5];
		for(int j=0;j<result.length;j++) {
			temp[place[(j+index) % 5]] = result[j];
		}
		return new Object[] { temp, maxSameCount };
	}
}
