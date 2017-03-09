package com.citywar.dice.entity;

import java.sql.Timestamp;



public class UserReward extends DataObject{
	
	private int id;
	
	/**
	 * 用户ID
	 */
    private int userId;
    
    /**
     * 5星评论的状态  0 未评论  1  已评论(可领取奖励)  2 已领取奖励
     */
    private int assessFlag;
    
    /**
     * 授权状态  0 未授权  1  已授权(可领取奖励)  2 可分享  3 分享领奖 
     */
    private int shareFlag;
    
    /**
     * 上次分享时间
     */
    private Timestamp lastShareTime;
    
    
    /**
     * 授权状态  0 未授权  1  已授权(可领取奖励)  2 可分享  3 分享领奖 
     */
    private int friendFlag;
    
    /**
     * 上次分享时间
     */
    private Timestamp lastFriendTime;
    
    
    /**
     * 连续登陆（签到）天数 和 状态
     * 连续登陆的天数：	1	2	3+	
     * 可以领取		：	1	2   3
     * 不可领取		：    -1  -2  -3
     */
    private int loginAward;
    
    /**
     * 上次领连续登陆奖励时间
     */
    private Timestamp loginAwardTime;
    
    
    
    /**
     * 授权状态  0 未授权  1  已授权(可领取奖励)  2 可分享  3 分享领奖 
     */
    private int drawGameCount;
    
    /**
     * 授权状态  0 未授权  1  已授权(可领取奖励)  2 可分享  3 分享领奖 
     */
    private int dayDraw;
    
    /**
     * 上次分享时间
     */
    private Timestamp dayDrawTime;
    
    
    
    
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAssessFlag() {
		return assessFlag;
	}

	public void setAssessFlag(int assessFlag) {
		this.assessFlag = assessFlag;
	}

	public int getShareFlag() {
		return shareFlag;
	}

	public void setShareFlag(int shareFlag) {
		this.shareFlag = shareFlag;
	}

	public Timestamp getLastShareTime() {
		return lastShareTime;
	}

	public void setLastShareTime(Timestamp lastShareTime) {
		this.lastShareTime = lastShareTime;
	}

	
	
	

	public int getFriendFlag() {
		return friendFlag;
	}

	public void setFriendFlag(int friendFlag) {
		this.friendFlag = friendFlag;
	}

	public Timestamp getLastFriendTime() {
		return lastFriendTime;
	}

	public void setLastFriendTime(Timestamp lastFriendTime) {
		this.lastFriendTime = lastFriendTime;
	}
	
	
	
	public int getDrawGameCount() {
		return drawGameCount;
	}

	public void setDrawGameCount(int drawGameCount) {
		this.drawGameCount = drawGameCount;
	}
	

        
    
    public Timestamp getDayDrawTime() {
		return dayDrawTime;
	}

	public void setDayDrawTime(Timestamp dayDrawTime) {
		this.dayDrawTime = dayDrawTime;
	}
    
	
	public int getDayDraw() {
		return dayDraw;
	}

	public void setDayDraw(int dayDraw) {
		this.dayDraw = dayDraw;
	}
	
	
	public int getLoginAward() {
		return loginAward;
	}

	public void setLoginAward(int loginAward) {
		
		System.out.println("setLoginAward==========================" + loginAward);
		this.loginAward = loginAward;
	}

	public Timestamp getLoginAwardTime() {
		return loginAwardTime;
	}

	public void setLoginAwardTime(Timestamp loginAwardTime) {
		this.loginAwardTime = loginAwardTime;
	}

	/**
     * 连续登陆（签到）天数 和 状态
     * 连续登陆的天数：	1	2	3+	
     * 可以领取		：	0	1	2+
     * 不可领取		：	-1	-2	-3+
     * 
     * 这里需要构造成 客服端的数据格式：
     * 
     * 连续登陆（签到）天数 和 状态
     * 连续登陆的天数：	1	2	3+	
     * 可以领取		：	1	2	3
     * 不可领取		：	-1	-2	-3
     */
	public int buildSendLoginAward() {
		int tempLoginAward = loginAward;
		if(tempLoginAward >= 0) {
			tempLoginAward ++;
			if(tempLoginAward > 3) {
				tempLoginAward = 3;
			}
		} else {
			if(tempLoginAward < -3) {
				tempLoginAward = -3;
			}
		}
		return tempLoginAward;
	}
}
