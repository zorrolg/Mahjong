/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * @author tracy
 * @date 2012-1-9
 * @version
 * 
 */
public interface GameDataType
{

    /**
     * 抢开
     */
    static final int OPEN_GRAB_TIMES = 2;

    /**
     * 普通开
     */
    static final int OPEN_TIMES = 1;

    /**
     * 抽水比例, /100
     */
    static final int WIN_RATE = 100;

    /**
     * 玩家加入游戏最低金币值比率 /100
     */
    static final int PLAYER_LOWEST_COINS_RATE = 100;
    
    /**
     * 奴隶主抽水比例, /100
     */
    static final int MASTER_WIN_RATE = 50;
    
    /**
     * 平局获得的比率
     */
	static final int DOGFALL_RATE = 10;

//    /**
//     * 能进入房间或者是能开始下局游戏的最少游戏币
//     */
//    static final int LEAST_TIMES = 1;
//
//    /**
//     * 强退扣除游戏币值
//     */
//    static final int EXIT_TIMES = -2;

    /**
     * 输方扣除的醉酒度(加法运算)
     */
    static final int DRUNK_CUT = -1;
    
    /**
     * 胜利的玩家获得的经验
     */
    static final int WIN_ADD_GP = 100;
    
    /**
     * 失败的玩家获得的经验
     */
    static final int FAIL_ADD_GP = 20;
    
    /**
     * 参与的玩家获得的经验
     */
    static final int DOGFALL_ADD_GP = 45;
}
