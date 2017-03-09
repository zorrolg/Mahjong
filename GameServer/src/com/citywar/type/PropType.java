/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * tracy
 * 
 * @author 道具相关
 * @date 2012-1-4
 * @version
 * 
 */
public interface PropType
{

    /**
     * 解酒药 模板id
     */
    public static final int WAKE_DRUG = 14001;

    /**
     * 醒酒茶  模板id
     * 每次改数据库中“醒酒茶  模板id”的时候服务器记得相应改这里的数据
     */
    public static final int WAKE_TEA = 14002;

    /**
     * 大喇叭  模板id
     * 每次改数据库中“醒酒茶  模板id”的时候服务器记得相应改这里的数据
     */
    public static final int TRUMPET = 15001;

    /**
     * 醒酒茶  效果减去10点醉酒度
     */
    public static final int WAKE_TEA_EFFECT = 10;

    /**
     * 皮肤
     */
    public static final int SKIN_TYPE = 2;

    /**
     * 聊天泡泡
     */
    public static final int CHAT_BUBBLE_TYPE = 3;

    /**
     * 醒酒茶（type 4：为醒酒茶）
     */
    public static final int WAKE_TYPE = 4;

    /**
     * 大喇叭（type 5：为大喇叭）
     */
    public static final int TRUMPET_TYPE = 5;



    /**
     * 扣除银子
     */
    public static final int REMOVE_MONEY = 2;

    /**
     * 扣除游戏币
     */
    public static final int REMOVE_COINS = 3;
    



	
    /**
     * 各种RMB换游戏币
     */
    public static final int COINS_5K = 2001;
    public static final int COINS_1W = 2002;
    public static final int COINS_3W = 2003;
    public static final int COINS_4W = 2004;
    public static final int COINS_10W = 2005;
    public static final int COINS_50W = 2006;
    
    
    public static final int CARD_GRAB = 1;
    public static final int CARD_COIN = 11;
    public static final int CARD_DRUNK = 12;
    public static final int CARD_DEFEND = 13;
    

}
