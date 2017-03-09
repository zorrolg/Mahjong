/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * @author : Cookie
 * @date : 2011-5-13
 * @version 游戏内事件
 */
public interface TaskConditionType
{
    /**
     * 完成游戏 0
     */
	public static final int FinishGame = 0;

	/**
     * 登录游戏 2
     */
	public static final int LoginGame = 2;
	
    /**
     * 更新信息 3
     */
	public static final int UpdateInfo = 3;

    /**
     * 添加好友 4
     */
	public static final int AddFriend = 4;

    /**
     * 邀请游戏 5
     */
	public static final int InviteGame = 5;

    /**
     * 灌醉玩家 6
     */
	public static final int DrunkUser = 6;

    /**
     * 完成建筑 7
     */
	public static final int FinishBuild = 7;

    /**
     * 金杯数量 8
     */
	public static final int CharmValue = 8;
    
    /**
     * 充值 9
     */
	public static final int ChargeMoney = 9;
	
	/**
     * 消耗钻石 10
     */
	public static final int UseMoney = 10;
    
    /**
     * 研发 11
     */
	public static final int CardDeveloper = 11;
    
    /**
     * 工厂 12
     */
	public static final int CardFactory = 12;
    
	/**
     * 其他任务 13
     */
	public static final int FinishStage = 13;
	
	/**
     * 赢取金币 14
     */
	public static final int WinCoin = 14;
    
	/**
     * 累计场次 15
     */
	public static final int GameCount = 15;
    
}
