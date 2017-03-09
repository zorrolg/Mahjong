/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import java.sql.Timestamp;

import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserGiftInfo;
import com.citywar.dice.entity.UserLetter;
import com.citywar.gameobjects.GamePlayer;



/**
 * 发奖管理类
 * 
 * @author shanfeng.cao
 * @date 2012-07-10
 * @version
 * 
 */
public class SystemAwardMgr
{

	/** 系统用户的ID*/
	private static int SYSTEM_PLAYER_ID = 200000;
	/** 系统用户的ID*/
	private static String SYSTEM_PLAYER_PICPATH = "pic__b.jpg";
	
	
	public static void systemAwardUserGift(GamePlayer gamePlayer, int giftId) {
		if(null == gamePlayer) {
			return ;
		}
		UserGiftInfo userGiftInfo = new UserGiftInfo();
        userGiftInfo.setOwnerUserId(gamePlayer.getUserId());
        userGiftInfo.setGiftId(giftId);
        userGiftInfo.setPresentUserId(SYSTEM_PLAYER_ID);
        userGiftInfo.setCount(1);//数量
        userGiftInfo.setGiveTime(new Timestamp(System.currentTimeMillis()));
        userGiftInfo.setExist(true);
        userGiftInfo.setUsed(true);
        userGiftInfo.setRemark(LanguageMgr.getTranslation("CityWar.Game.GiftSenderRemark"));
        userGiftInfo.setPresentUserName(LanguageMgr.getTranslation("CityWar.Game.GiftSenderName"));
        userGiftInfo.setPresentUserPicPath(SYSTEM_PLAYER_PICPATH);
        userGiftInfo.setOp(Option.INSERT);
        gamePlayer.getPlayerGift().receiveGift(userGiftInfo);
        gamePlayer.getOut().sendUpdatePrivateInfo(gamePlayer.getPlayerInfo(), (byte)2);
	}
	
	public static void awardUserGift(GamePlayer gamePlayer) {
		if(null == gamePlayer || null == gamePlayer.getPlayerGift()
				 || null == gamePlayer.getPlayerGift().getUserAllGift()) {
			return ;
		}
		if(gamePlayer.getPlayerGift().getUserAllGift().size() <= 0) {
			systemAwardUserGift(gamePlayer, 6);
		}
	}

	/**
	 * 用户首次进入游戏  发一封欢迎私信
	 */
	public static UserLetter createWelComeLetter(PlayerInfo player){
		if(player == null )
		{
			return null;
		}
		UserLetter welComeLetter = new UserLetter();
		welComeLetter.setSenderId(SYSTEM_PLAYER_ID);
		welComeLetter.setSenderName(LanguageMgr.getTranslation("CityWar.GameName"));
		welComeLetter.setSenderPic(SYSTEM_PLAYER_PICPATH);
		welComeLetter.setOp(Option.INSERT);
		welComeLetter.setContent(LanguageMgr.getTranslation("CityWar.Game.WelcomeLetter"));
		welComeLetter.setSendTime(new Timestamp(System.currentTimeMillis()));
		
		welComeLetter.setReceiverId(player.getUserId());
		welComeLetter.setReceiverName(player.getUserName());
		return welComeLetter;
	}
	
	
	/**
	 * 用户
	 */
	public static UserLetter createChargeLetter(int userId,String userName,String proName){

		UserLetter welComeLetter = new UserLetter();
		welComeLetter.setSenderId(SYSTEM_PLAYER_ID);
		welComeLetter.setSenderName(LanguageMgr.getTranslation("CityWar.GameName"));
		welComeLetter.setSenderPic(SYSTEM_PLAYER_PICPATH);
		welComeLetter.setOp(Option.INSERT);
		welComeLetter.setContent(LanguageMgr.getTranslation("CityWar.Game.ChargeLetter",proName));
		welComeLetter.setSendTime(new Timestamp(System.currentTimeMillis()));
		
		welComeLetter.setReceiverId(userId);
		welComeLetter.setReceiverName(userName);
		return welComeLetter;
	}
	
	
	/**
	 * 用户首次进入游戏  发一封欢迎私信
	 */
	public static UserLetter createSystemLetter(GamePlayer receiver, String strContent){
		if(receiver == null )
		{
			return null;
		}
		UserLetter welComeLetter = new UserLetter();
		welComeLetter.setSenderId(SYSTEM_PLAYER_ID);
		welComeLetter.setSenderName(LanguageMgr.getTranslation("CityWar.GameName"));
		welComeLetter.setSenderPic(SYSTEM_PLAYER_PICPATH);
		welComeLetter.setOp(Option.INSERT);
		welComeLetter.setContent(strContent);
		welComeLetter.setSendTime(new Timestamp(System.currentTimeMillis()));
		
		welComeLetter.setReceiverId(receiver.getUserId());
		welComeLetter.setReceiverName(receiver.getPlayerInfo().getUserName());
		return welComeLetter;
	}
	
}
