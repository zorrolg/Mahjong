/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import java.sql.Timestamp;
import java.util.Calendar;

import com.citywar.bll.ItemBussiness;
import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserAwardInfo;
import com.citywar.dice.entity.UserItemInfo;
import com.citywar.dice.entity.UserReward;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameutil.GamePlayerUtil;
import com.citywar.util.Config;
import com.citywar.util.TimeUtil;

/**
 * 发奖管理类
 * 
 * @author tracy
 * @date 2011-12-27
 * @version
 * 
 */
public class AwardMgr
{
    static final int LOGIN_FIST_COINS = 10000; // 首次登陆奖励 20000
    static final int LOGIN_FIST_MONEY = 200; // 首次登陆奖励 20000
    static final int LOGIN_DAILY_COINS = 500; // 日常登陆奖励 500
    static final int COINS_LESS_AEARD_COINS = 1000; // 当玩家金币小于100，时候送玩家金币的数量
    static final int COINS_LESS_COINS = 1000; // 当玩家金币不足多少时，满足送每日4次的条件
    static final int AWARD_COINS_COUNT = 2; //当玩家金币小于100，时候送玩家金币的次数

    
//    static final String[] awardList = Config.getValue("award.item").split("[|]");
//    static final String[] rewardList = Config.getValue("reward.item").split("[|]");
    
    
    private static int[][] AwardList = null;
    private static int[][] RewardList = null;
    
    
    public static int[][] getAwardList()
    {
    	if(AwardList == null)
    	{
    		int index = 0;
    		String[] awardList = Config.getValue("award.item").split("[|]");       		
    		AwardList =  new int[awardList.length][2] ; 
    		
    		for (String award : awardList) {
        		
        		String[] awardkey = award.split("_");        		
        		if(awardkey.length == 2)
        		{
        			int type = Integer.parseInt(awardkey[0]);
        			int count = Integer.parseInt(awardkey[1]);
        			
        			AwardList[index][0] = type;
        			AwardList[index][1] = count;
        			
        			index +=1;
        		}
    		}
    	}
    	return AwardList;
    }
    
    public static int[][] getRewardList()
    {
    	if(RewardList == null)
    	{
    		int index = 0;
    		String[] awardList = Config.getValue("reward.item").split("[|]");    	
    		RewardList =  new int[awardList.length][3] ; 
    		
    		
    		for (String award : awardList) {
        		
        		String[] awardkey = award.split("_");        		
        		if(awardkey.length == 3)
        		{
        			int type = Integer.parseInt(awardkey[0]);
        			int count = Integer.parseInt(awardkey[1]);
        			int minute = Integer.parseInt(awardkey[2]);
        			
        			RewardList[index][0] = type;
        			RewardList[index][1] = count;
        			RewardList[index][2] = minute;
        			
        			index +=1;
        		}
    		}
    	}
    	return RewardList;
    }
    
    /**
     * 领取登陆游戏币奖励
     * 
     * @param player
     * @return
     */
    public static int getLoginAwardCoins(PlayerInfo info)
    {
        int result = 0;
//        int money = 0;
        if (info.getLastLoginDate() == null)
        {
            // TODO 首次登陆, 送游戏币, 送10个醒酒茶和1个解酒药(直接保存到数据库???)
        	
        	
        	for (int[] award : getAwardList()) {
        		
    			int type = award[0];
    			int count = award[1];
    			
				if (type == 0) {//游戏币奖励    					
					info.setCoins(count);
				} else if (type == -1) { //经验奖励
					info.setMoney(count);
				}else if (type == -2) { //经验奖励
					info.setCardCount(count);
				} else if (type > 0 && type < 10000) { //经验奖励
					ItemBussiness.addUserItem(new UserItemInfo(2,award[0], info.getUserId(), count));
				}else if (type > 10000) { //醒酒茶
					ItemBussiness.addUserItem(new UserItemInfo(1,award[0], info.getUserId(), count));
				} 
        		

			}
        	
        	PlayerBussiness.updateAll(info.getUserId(), info);
        	
//            result = LOGIN_FIST_COINS;
//            money = LOGIN_FIST_MONEY;
//
//            
//            ItemBussiness.addUserItem(new UserItemInfo(1,PropType.WAKE_DRUG, info.getUserId(), 3));
//            ItemBussiness.addUserItem(new UserItemInfo(1,PropType.TRUMPET, info.getUserId(), 5));
//            
//            ItemBussiness.addUserItem(new UserItemInfo(2,PropType.CARD_GRAB, info.getUserId(), 5));
//            ItemBussiness.addUserItem(new UserItemInfo(2,PropType.CARD_COIN, info.getUserId(), 5));
//            ItemBussiness.addUserItem(new UserItemInfo(2,PropType.CARD_DRUNK, info.getUserId(), 5));
//            ItemBussiness.addUserItem(new UserItemInfo(2,PropType.CARD_DEFEND, info.getUserId(), 5));
            

//            ItemBussiness.addUserItem(new UserItemInfo(PropType.WAKE_DRUG, info.getUserId(), 10));
        } 
//        else if (!TimeUtil.dateCompare(info.getLastLoginDate()))
//            result = LOGIN_DAILY_COINS;
        // 更新数据库
//        info.setCoins(info.getCoins() + result);
//        info.setMoney(money);
        info.setOnline(true);
        info.setLastLoginDate(TimeUtil.getSysteCurTime()); // 这里时间的误差可否接收
        
        return result;
    }
    
    /**
     * 判断玩家是否没钱了玩家没钱了
     * 
     * @param player
     * @return
     */
    public static boolean palyerCoinsLess(GamePlayer player) {
    	if(player == null || player.getPlayerInfo() == null ||
    			player.getPlayerInfo().getCoins() >= COINS_LESS_COINS) {//大于则不送
    		return false;
    	}
    	return true;
    }
    
    /**
     * 玩家没钱了，每天可以领取4次500金币
     * 
     * @param player
     * @return
     */
    public static boolean palyerCoinsLessAwardCoins(GamePlayer player)
    {
    	boolean canAward = true;
    	if(player.getIsRobot()) {//机器人直接送
//    		player.addCoins(COINS_LESS_AEARD_COINS);
    		return false;
    	} 
    	if(player.getPlayerInfo().getCoins() >= COINS_LESS_COINS) {
    		return false;
    	}
    	int userId = player.getUserId();
    	UserAwardInfo info = player.getUserAward();
    	int haveCount = 0;
    	if(null == info || info.getAwardLastTtime().before(GamePlayerUtil.getResetTime())) {//没有或者今天没有领取
    		haveCount = 1;
    		info = new UserAwardInfo(userId, 1, haveCount, new Timestamp(System.currentTimeMillis()));
    		player.setUserAward(info);
    		info.setOp(Option.INSERT);    		
    	} else if(info.getAwardCount() < AWARD_COINS_COUNT) {//今天没有领取了，但是还没达到4次
    		haveCount = info.getAwardCount() + 1;
    		info.setAwardCount(haveCount);
    		info.setOp(Option.UPDATE);
    	} else {
    		haveCount = AWARD_COINS_COUNT;//系统已经赠送完成
    		canAward = false;
    	}
    	if(canAward) {
    		player.addCoins(COINS_LESS_AEARD_COINS);
    		player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
    		player.getOut().sendCoinsNotEnough(canAward, AWARD_COINS_COUNT, AWARD_COINS_COUNT - haveCount, COINS_LESS_AEARD_COINS);//发送给玩家剩余次数
    		    		
//    		if(player.getCurrentRoom() != null)
//    			player.getCurrentRoom().sendRoomPlayerCoin(player, COINS_LESS_AEARD_COINS);
    	}
    	
    	
		return canAward;
    }
    
    
    
    public static boolean checkCoinsLessAwardCoins(GamePlayer player)
    {

    	boolean canAward = true;
    	int userId = player.getUserId();
    	UserAwardInfo info = player.getUserAward();
    	int haveCount = 0;
    	
    	if(player.getPlayerInfo().getCoins() >= COINS_LESS_COINS) {
    		canAward = false;
    	}
    	else
    	{

        	if(null == info || info.getAwardLastTtime().before(GamePlayerUtil.getResetTime())) {//没有或者今天没有领取
        		haveCount = 0;
        		info = new UserAwardInfo(userId, 1, haveCount, new Timestamp(System.currentTimeMillis()));
        		player.setUserAward(info);
        		info.setOp(Option.INSERT);    		
        	} else if(info.getAwardCount() < AWARD_COINS_COUNT) {//今天没有领取了，但是还没达到4次
        		haveCount = info.getAwardCount();
        	} else {
        		haveCount = AWARD_COINS_COUNT;//系统已经赠送完成
        		canAward = false;
        	}
    	}
    	    	
    		
    	player.getOut().sendCoinsNotEnough(canAward, AWARD_COINS_COUNT, AWARD_COINS_COUNT - haveCount, 0);//发送给玩家剩余次数
		return canAward;
    }
    
    
    /**
     * 检查和重置连续登陆的状态
     * @param player
     */
	public static void awardLoginCoins(GamePlayer player) {
		PlayerInfo info = player.getPlayerInfo();
		UserReward userReward = player.getUserReward();
		if(null != info && null != userReward) {
			Timestamp lastLoginDate = userReward.getLoginAwardTime();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			if(null == lastLoginDate) {
				lastLoginDate = today;
				userReward.setLoginAwardTime(lastLoginDate);
			}
			
			
			Calendar lastLoginDateCalendar = Calendar.getInstance(); 
			lastLoginDateCalendar.setTime(lastLoginDate);
			Calendar todayCalendar = Calendar.getInstance(); 
			todayCalendar.setTime(today);
			int compareDay = TimeUtil.dateCompareDay(lastLoginDateCalendar, todayCalendar);			
			if(compareDay > 1)
			{
				userReward.setLoginAward(0);
				userReward.setOp(Option.UPDATE);
			}
			else if(compareDay == 1)
			{
				userReward.setLoginAward(Math.abs(userReward.getLoginAward()));
				userReward.setOp(Option.UPDATE);
			}
			
			
			
			
			
			
			
			
			
			Timestamp lastDrawDate = userReward.getDayDrawTime();
			
			if(null == lastDrawDate) {
				lastDrawDate = today;
				userReward.setDayDraw(0);
				userReward.setDayDrawTime(lastDrawDate);
			}
			
			
			Calendar lastDrawDateCalendar = Calendar.getInstance(); 
			lastDrawDateCalendar.setTime(lastLoginDate);
			compareDay = TimeUtil.dateCompareDay(lastDrawDateCalendar, todayCalendar);			
			if(compareDay >= 1)
			{
				userReward.setDayDraw(0);
				userReward.setOp(Option.UPDATE);
			}
			else
			{
				userReward.setLoginAward(1);
				userReward.setOp(Option.UPDATE);
			}
		}
		
		player.getOut().sendNewestRewardState();
		
		
	}
}
