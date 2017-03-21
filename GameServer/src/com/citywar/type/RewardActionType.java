package com.citywar.type;

import java.sql.Timestamp;

import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.UserReward;
import com.citywar.game.GamePlayer;
import com.citywar.manager.AwardMgr;
import com.citywar.util.TimeUtil;

public enum RewardActionType {
	/** 用户点击5星评论*/
	FIVE_ASSESS_CLICK(1,0) {
		@Override
		public boolean excute(GamePlayer player) {
			
			boolean isSuccess = false;
			UserReward userAssess = player.getUserReward();
			
			synchronized (userAssess) {
				if(userAssess.getAssessFlag() == 0){
					userAssess.setAssessFlag(currentFlag + 1);
					userAssess.setOp(Option.UPDATE);
					isSuccess = true;
				}
			}
			return isSuccess;
		}
	}, 
	
	/** 用户领取5行评论奖励*/
	FIVEASSESS_PRIZE_CLICK(2,1) {
		@Override
		public boolean excute(GamePlayer player) {
			
			boolean isSuccess = false;
			UserReward userAssess = player.getUserReward();
			
			//防止重复领取奖励
			synchronized (userAssess) {
				if(userAssess.getAssessFlag() == currentFlag){
					userAssess.setAssessFlag(currentFlag + 1);
					userAssess.setOp(Option.UPDATE);
					isSuccess = true;
//					player.addCoins(reward);
					
					int[][] rewardList = AwardMgr.getRewardList();
					if(rewardList.length >= 1)
					{
						reward = rewardList[0][1];
						player.addMoney(reward);						
					}
					
		    		player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
				}
			}
			return isSuccess;
		}
	},
	
	/** 微信分享成功成功*/
	WEIXIN_SUCCESS(3,0) {
		@Override
		public boolean excute(GamePlayer player) {
			
			boolean isSuccess = false;
			UserReward userAssess = player.getUserReward();

			
			int waitTime = 24 * 60;
			int[][] rewardList = AwardMgr.getRewardList();
			if(rewardList.length >= 2)
			{
				waitTime = rewardList[1][2];					
			} 
			
			if((userAssess.getFriendFlag() > 1 && (TimeUtil.getSysteCurTime().getTime() - userAssess.getLastFriendTime().getTime())/60 > waitTime)
					|| (userAssess.getFriendFlag() == 0 && userAssess.getLastFriendTime() == null))//用户曾经授权成功过  此次授权不进入授权奖励状态
			{
				userAssess.setFriendFlag(currentFlag + 1);
				userAssess.setOp(Option.UPDATE);
				isSuccess = true;
				
//				System.out.println("WEIXIN_SUCCESS======" + userAssess.getFriendFlag());
			}
			return isSuccess;
		}
	},
	
	/** 微信分享成功奖励*/
	WEIXIN_SUCCESS_PRIZE_CLICK(4,1) {
		@Override
		public boolean excute(GamePlayer player) {
			
			boolean isSuccess = false;
			UserReward userAssess = player.getUserReward();
			Timestamp nowTime = new Timestamp( System.currentTimeMillis());
			
			//防止重复领取奖励
			synchronized (userAssess) {
				if(userAssess.getFriendFlag() == currentFlag){
					userAssess.setFriendFlag(currentFlag + 1);
					userAssess.setLastFriendTime(nowTime);
					
					int[][] rewardList = AwardMgr.getRewardList();
					if(rewardList.length >= 2)
					{
						reward = rewardList[1][1];
						player.addCoins(reward);			
					} 
					
					userAssess.setOp(Option.UPDATE);
		    		player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
		    		isSuccess = true;
//		    		System.out.println("WEIXIN_SUCCESS_PRIZE_CLICK======" + userAssess.getFriendFlag());
				}
			}
			return isSuccess;
		}
	},
	
	/** 用户分享成功*/
	SHARE_SUCCESS(5,0) {
		@Override
		public boolean excute(GamePlayer player) {
			
			boolean isSuccess = false;
			UserReward userAssess = player.getUserReward();

			
			int waitTime = 24 * 60;
			int[][] rewardList = AwardMgr.getRewardList();
			if(rewardList.length >= 2)
			{
				waitTime = rewardList[2][2];					
			} 
			
			
			if((userAssess.getShareFlag() > 1 && (TimeUtil.getSysteCurTime().getTime() - userAssess.getLastShareTime().getTime())/60 > waitTime)
					|| (userAssess.getShareFlag() == 0 && userAssess.getLastShareTime() == null))//用户曾经授权成功过  此次授权不进入授权奖励状态
			{
				userAssess.setShareFlag(currentFlag + 1);
				userAssess.setOp(Option.UPDATE);
				isSuccess = true;
			}
			return isSuccess;
		}
	},
	
	/** 用户分享领奖*/
	SHARE_SUCCESS_PRIZE(6,1) {
		@Override
		public boolean excute(GamePlayer player) {
			
			boolean isSuccess = false;
			UserReward userAssess = player.getUserReward();
			Timestamp nowTime = new Timestamp( System.currentTimeMillis());
			
			//防止重复领取奖励
			synchronized (userAssess) {
				if(userAssess.getShareFlag() == currentFlag){
					userAssess.setShareFlag(currentFlag + 1);
					userAssess.setLastShareTime(nowTime);
					
					int[][] rewardList = AwardMgr.getRewardList();
					if(rewardList.length >= 2)
					{
						reward = rewardList[2][1];
						player.addCoins(reward);			
					} 
					
					userAssess.setOp(Option.UPDATE);
		    		player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
		    		isSuccess = true;
				}
			}
			return isSuccess;
		}
	},
	
	/** 连续登陆奖励*/
	LOGIN_ONE_SUCCESS_PRIZE(7) {
		@Override
		public boolean excute(GamePlayer player) {
			
			boolean isSuccess = false;
			UserReward userAssess = player.getUserReward();
			if(null != userAssess && 0 <= userAssess.getLoginAward()) {
				int loginTimes = userAssess.getLoginAward();
				
				
				
				int[][] rewardList = AwardMgr.getRewardList();
				if(loginTimes > rewardList.length)
					loginTimes = rewardList.length;
				
				if (rewardList[loginTimes][0] == 0) {//游戏币奖励					
					player.addCoins(rewardList[loginTimes][1]);
				} else if (rewardList[loginTimes][0] == -1) {//游戏币奖励					
					player.addMoney(rewardList[loginTimes][1]);
				}
				
				
				userAssess.setLoginAwardTime(new Timestamp(System.currentTimeMillis()));
				userAssess.setLoginAward( - (loginTimes + 1));//加一天，并且领奖过了
				userAssess.setOp(Option.UPDATE);
				player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
				isSuccess = true;
			}
			return isSuccess;
		}
		
	};
	
	protected int id;
	protected int currentFlag;
    protected int reward;
    protected int minute;
    

    private RewardActionType(int id){
    	this.id       = id;
    }
    private RewardActionType(int id,int currentFlag){
    	this.id       = id;
    	this.currentFlag = currentFlag;
    }
    private RewardActionType(int id,int currentFlag,int minute){
    	this.id       = id;
    	this.currentFlag = currentFlag;
    	this.minute = minute;
    }
    
    public static RewardActionType valueOf(int id)
	{
		for (RewardActionType function : values())
		{
			if (function.id == id) { return function; }
		}

		throw new IllegalArgumentException(" illegal RewardActionType id:" + id);
	}

   public abstract boolean excute(GamePlayer player);

   public int getReward() {
	return reward;
   }
   
}
