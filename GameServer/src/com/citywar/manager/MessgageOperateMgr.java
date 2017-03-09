/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.type.OperateCmdType;



/**
 * 发奖管理类
 * 
 * @author shanfeng.cao
 * @date 2012-08-07
 * @version
 * 
 */
public class MessgageOperateMgr
{
	
	public static void sendMessgageAndOperate(GamePlayer gamePlayer, short operateMgrCode) {
		if(null == gamePlayer) {
			return ;
		}
		String message = "";
		switch(operateMgrCode) {
    	
    	default:
    		break ;
    	}
		gamePlayer.getOut().sendMessageAndOperate(operateMgrCode, message);
	}
	
	public static void executeOperate(GamePlayer gamePlayer, short operateMgrCode) {
		if(null == gamePlayer) {
			return ;
		}
    	switch(operateMgrCode) {
    	case OperateCmdType.CANCEL :
    		break ;
    	default:
    		break ;
    	}
	}
}
