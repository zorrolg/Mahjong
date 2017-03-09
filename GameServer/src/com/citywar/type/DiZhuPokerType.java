/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.type;

/**
 * 游戏类型定义
 * @author Dream
 * @date 2011-12-19
 * @version 
 *
 */
public interface DiZhuPokerType
{

	public static final int none		=0;
    
    public static final int Single		=1;
    public static final int Couple		=2;
    public static final int Three		=3;    
    public static final int Three_one	=4;
    public static final int Three_two	=5;
    public static final int Junko		=6;    
    public static final int Junko_two	=7;
    public static final int Fly			=8;
    public static final int Fly_one		=9;    
    public static final int Fly_two		=10;
    public static final int Bomb_one	=11;
    public static final int Bomb_two	=12;    
    
    public static final int Bomb 		=13;
    public static final int Rocket		=14;  

}



//public enum DiZhuPokerType
//{
//	
//	
//	none		(0, 0),
//
//	Single		(1, 1),
//	Couple		(2, 1),
//	Three		(3, -1),
//	Three_one	(4, 1),
//	Three_two	(5, 1),
//	Junko		(6, 1),
//	Junko_two	(7, 1),
//	Fly			(8, 1),
//	Fly_one		(9, 1),
//	Fly_two		(10, 1),
//	Bomb_one	(11, 1),
//	Bomb_two	(12, 1),
//
//	Bomb 		(13, 1),
//	Rocket		(14, 1);
//	
//
//    private final int value;
//	
//	private final int rate;
//
//    private DiZhuPokerType(int value, int rate)
//    {
//        this.value = value;
//        this.rate = rate;
//    }
//
//    public int getValue()
//    {
//        return value;
//    }
//    
//    public int getRate()
//    {
//        return rate;
//    }
//}
