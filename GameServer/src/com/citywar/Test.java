package com.citywar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Test {
	public static Map<Integer, Integer> sort(int[] headCard) {
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < headCard.length; i++) {
			if (map.get(headCard[i]) == null) {
				map.put(headCard[i], 1);
			} else {
				map.put(headCard[i], map.get(headCard[i]) + 1);
			}
		}
		List<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
			@Override
			public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
				if (o1.getKey() > o2.getKey()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		Map<Integer, Integer> sortMap = new LinkedHashMap<Integer, Integer>();
		for (Map.Entry<Integer, Integer> entry : list) {
			sortMap.put(entry.getKey(), entry.getValue());
		}
		return sortMap;
	}

	// }

	/**
	 * * 此算法是先将手牌归类并按牌顺序排列 * 遍历所有牌，找到所有一种牌数量大于2的情况， 然后把这两张牌（对子）去掉，然后就剩下3n张牌了， *
	 * 再判断剩下的3n张牌是否能组成顺子或是暗刻， 如果可以全部组成，那么这把牌就胡了，否则需要遍历一下把其他牌当对子的可能性 *
	 * 从第一张（种）牌开始往后检查， 每张牌有5种可能， 0， 1， 2，3，4。如果是0，直接检查下一张（种）；
	 * i.如果是1张或两张，要胡牌的话他（们）必须和后面两张组成顺子，如果不能组成顺子，肯定不胡。
	 * 如果可以组成顺子，把顺子牌取出，存入临时结果，接着处理剩下的牌；
	 * ii.如果是3张，由于是已经排好序从小往大遍历，此时就没必要和后面的牌进行顺子比对，直接做刻处理
	 * iii.如果是4张，胡牌的话必须要跟后面两张牌组成一个顺子，然后本张（种）就剩3种了，然后继续2.2的步骤就可以了。 * @param args
	 */
	public static boolean huPai(int[] headCard) {
		Map<Integer, Integer> cardMap = sort(headCard);
		boolean b = false; // 默认不胡牌
		for (Integer card : cardMap.keySet()) {
			if (cardMap.get(card) >= 2) {
				Map<Integer, Integer> teampMap = sort(headCard);
				teampMap.put(card, teampMap.get(card) - 2 > 0 ? teampMap.get(card) - 2 : null);
				// 去掉对子
				for (Integer key : teampMap.keySet()) {
					Integer value = teampMap.get(key);
					if (value == null) {
						continue;
					} // 字只有三张成刻才有效
					if (key / 10 == 4) {
						if (value == 3) {
							teampMap.put(key, null);
							b = true;
						} else {
							b = false;
						}
					}
					if (value == 1 && key % 10 <= 7 && key / 10 != 4) {
						if (teampMap.get(key + 1) != null && teampMap.get(key + 2) != null && teampMap.get(key + 1) >= 1
								&& teampMap.get(key + 2) >= 1) {
							teampMap.put(key, null);
							teampMap.put(key + 1, teampMap.get(key + 1) - 1 > 0 ? teampMap.get(key + 1) - 1 : null);
							teampMap.put(key + 2, teampMap.get(key + 2) - 1 > 0 ? teampMap.get(key + 2) - 1 : null);
							b = true;
						} else {
							b = false;
							break;
						}
					}
					if (value == 2 && key % 10 <= 7 && key / 10 != 4) {
						if (teampMap.get(key + 1) != null && teampMap.get(key + 2) != null && teampMap.get(key + 1) >= 2
								&& teampMap.get(key + 2) >= 2) {
							teampMap.put(key, null);
							teampMap.put(key + 1, teampMap.get(key + 1) - 2 > 0 ? teampMap.get(key + 1) - 2 : null);
							teampMap.put(key + 2, teampMap.get(key + 2) - 2 > 0 ? teampMap.get(key + 2) - 2 : null);
							b = true;
						} else {
							b = false;
							break;
						}
					}
					if (value == 3) {
						teampMap.put(key, null);
						b = true;
					}
					if (value == 4 && key / 10 != 4) {
						if (teampMap.get(key + 1) != null && teampMap.get(key + 2) != null && teampMap.get(key + 1) >= 1
								&& teampMap.get(key + 2) >= 1) {
							teampMap.put(key, null);
							teampMap.put(key + 1, teampMap.get(key + 1) - 1 > 0 ? teampMap.get(key + 1) - 1 : null);
							teampMap.put(key + 2, teampMap.get(key + 2) - 1 > 0 ? teampMap.get(key + 2) - 1 : null);
							b = true;
						} else {
							b = false;
							break;
						}
					}
				}
				for (Integer key : teampMap.keySet()) {
					if (teampMap.get(key) != null) {
						b = false;
						break;
					}
				}
				if (b) {
					break;
				}
			}
		}
		return b;
	}

	public static void main(String[] args) {
		int[] headCard = { 45, 45, 16, 17, 17, 18, 18, 19 };
		System.out.println(huPai(headCard));
	}
}
// }}}}
