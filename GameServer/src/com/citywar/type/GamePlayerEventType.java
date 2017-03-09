/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;


/**
 * @author Dream
 * @date 2011-5-10
 * @version
 * 
 */
public interface GamePlayerEventType
{
    /**
     * 游戏结束事件
     */
    public static int GameOverEvent = 1;

    /**
     * 完成成就事件
     */
    public static int AchievementFinishEvent = 2;

    /**
     * 玩家充值事件
     */
    public static int PlayerChargeMoneyEvent = 3;

    /**
     * 完成副本
     */
    public static int MissionOverEvent = 4;

    /**
     * 完成关卡
     */
    public static int MissionTurnOverEvent = 5;

    /**
     * 玩家击杀其他玩家或NPC事件
     */
    public static int AfterKillingLiving = 6;

    /**
     * 玩家击杀掉落事件
     */
    public static int GameKillDropEvent = 7;

    /**
     * 合成成功/合成类型/次数事件
     */
    public static int ItemComposeEvent = 8;

    /**
     * 11、熔炼成功/熔炼类型/次数事件
     */
    public static int ItemFusionEvent = 9;

    /**
     * 25、镶嵌任务
     */
    public static int ItemInsertEvent = 10;

    /**
     * 12、炼化/装备类型/炼化等级
     */
    public static int ItemMeltEvent = 11;

    /**
     * 12、用户使用道具
     */
    public static int AfterUsingItem = 12;

    /**
     * 工会任务
     */
    public static int PlayerOwnConsortiaEvent = 13;

    /**
     * 温泉任务
     */
    public static int PlayerOwnSpaEvent = 14;

    /**
     * 通用拜师收徒事件
     */
    public static int BuildApprenticeship = 15;

    /**
     * 拜师事件（成就系统）
     */
    public static int BeApprentice = 16;

    /**
     * 收徒事件
     */
    public static int TakeApprentice = 17;

    /**
     * 社区事件
     */
    public static int PlayerShareSNS = 18;

    /**
     * 发送邮件事件
     */
    public static int playerSendMailQuest = 19;

    /**
     * 玩家购买事件
     */
    public static int PlayerShopEvent = 20;

    /**
     * 玩家完成任务事件
     */
    public static int PlayerQuestFinishEvent = 21;

    /**
     * 温泉事件
     */
    public static int PlayerAddSpaTime = 22;

    /**
     * 在仅剩最后一滴血的成就
     */
    public static int PlayerFightOneBlood = 23;

    /**
     * 用户buffer事件
     */
    public static int PlayerEvent = 24;

    /**
     * 用户装备套卡
     */
    public static int PlayerEquipCardEvent = 25;

    /**
     * 用户开卡牌事件
     */
    public static int PlayerOpenCardEvent = 26;

    /**
     * 用户日常奖励
     */
    public static int PlayerDailyAwardEvent = 27;

    /**
     * 发表胜利宣言
     */
    public static int PlayerDispatchesEvent = 28;

    /**
     * 用户强化
     */
    public static int PlayerItemStrengthenEvent = 29;

    /**
     * 用户结婚
     */
    public static int PlayerMarryEvent = 30;

    /**
     * 玩家装备武器
     */
    public static int PlayerEquipItemEvent = 31;

    /**
     * 玩家赠送物品
     */
    public static int PlayerGoodsPresentEvent = 32;

    /**
     * 玩家升级
     */
    public static int PlayerValueEvent = 33;

    /**
     * 魅力等级提升
     */
    public static int PlayerGiftLevelUp = 34;
    
    /**
     * 游戏部分
     */
    public static int GameStart = 35;
    public static int GameOver = 36;
    public static int GameLogin = 37;
    public static int GameCreate = 38;
    public static int GameCancel = 39;
    public static int GameExit = 40;
    
    /**
     * 礼物等级升级
     */
    public static int PlayerGiftGradeUp = 41;
}
