package com.citywar.gameutil;

import com.citywar.bll.UserPropertyBussiness;
import com.citywar.dice.entity.UserPropertyInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.AntiAddictionMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.util.IDCard;

public class UserProperty {
	

	private GamePlayer gamePlayer;

    /**
     * 玩家实名制等信息
     */
    private UserPropertyInfo userPropertyInfo;

    /**
     * 是否完成实名制
     */
    private boolean isNeedReal = true;
    
    /**
     * 是否需要防沉迷限制
     */
    private boolean isNeedAntiAddiction = true;
    
    /**
     * 得到的效益成数
     */
    private int incomeRatio = 100;
    
    public UserProperty(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	/**
     * 加载玩家的实名制和防沉迷信息
     */
    public void loadUserProperty() {
    	if(null == gamePlayer) {
    		return ;
    	}
    	userPropertyInfo = UserPropertyBussiness.getPlayerUserProperty(gamePlayer.getUserId());
    	checkPlayeAntiAddiction();
    }
    
    /**
     * 检查玩家是否实名制和是否需要防沉迷
     */
    public void checkPlayeAntiAddiction() {
    	if( ! isNeedAntiAddiction || ! isNeedReal) {//用户已经实名制了。执行不需要实名制和防沉迷
    		return ;
    	}
    	if(null != userPropertyInfo) {
    		String IDStr = userPropertyInfo.getIdentityCard();
    		if(AntiAddictionMgr.IDCardValidate(IDStr)) {
    			isNeedReal = false;
    			if(IDCard.checkIDCardisAdult(IDStr)) {
    				isNeedAntiAddiction = false;
    			}
    		}
    	}
    	if(isNeedAntiAddiction) {
    		AntiAddictionMgr.addAntiAddictionPlayer(gamePlayer);
    	} else {
        	incomeRatio = 100;
    	}
    }
    
    
    /**
     * 更新玩家实名制信息
     * @param identityCard
     * @param realName
     * @return
     */
    public boolean insertUserProperty(String identityCard, String realName) {
    	if(null == gamePlayer || null != userPropertyInfo) {
    		return false;
    	}
		userPropertyInfo = new UserPropertyInfo();
		userPropertyInfo.setUserId(gamePlayer.getUserId());
		userPropertyInfo.setIdentityCard(identityCard);
		userPropertyInfo.setRealName(realName);
		UserPropertyBussiness.insertUserProperty(userPropertyInfo);
		checkPlayeAntiAddiction();
		return true;
    }

    /**
     * 查看玩家是否需要实名制
     * @return
     */
	public boolean isNeedReal() {
		return isNeedReal && AntiAddictionMgr.isStratReal();
	}

	/**
	 * 查看玩家是否需要防沉迷
	 * @return
	 */
	public boolean isNeedAntiAddiction() {
		return isNeedAntiAddiction && AntiAddictionMgr.isStrat();
	}
	
	/**
     * 提醒用户注意防沉迷
     * @param identityCard
     * @param realName
     * @return
     */
	public void computePlayeIncomeRatio() {
		if(null == gamePlayer || ( ! isNeedAntiAddiction)
				|| null == gamePlayer.getDayActivity()) {
    		return;
    	}
		long userOnlineTime = gamePlayer.getDayActivity().getAccumulateOnlineTime();
		incomeRatio = AntiAddictionMgr.getAntiAddictionIncomeRatio(userOnlineTime);
	}
		
		/**
	     * 提醒用户注意防沉迷
	     * @param identityCard
	     * @param realName
	     * @return
	     */
	public void sendPlayeAntiAddiction() {
		if(null == gamePlayer || ( ! isNeedAntiAddiction)
				|| null == gamePlayer.getDayActivity()) {
    		return;
    	}
		long userOnlineTime = gamePlayer.getDayActivity().getAccumulateOnlineTime();
		String sendStr = AntiAddictionMgr.getAntiAddictionSendStr(userOnlineTime);
		if(null != sendStr && ! sendStr.isEmpty()) {
			Packet response = new Packet(UserCmdOutType.PROMPT_MESSAGE);
			response.putByte((byte)1);//提示框的类型
			response.putStr(sendStr);//提示语句
			gamePlayer.getOut().sendTCP(response);
		}
	}

	/**
	 * 退出时移除
	 */
	public void clearAntiAddiction() {
		AntiAddictionMgr.removeAntiAddictionPlayer(gamePlayer);
	}

	public int getIncomeRatio() {
		return incomeRatio;
	}

	public void setIncomeRatio(int incomeRatio) {
		this.incomeRatio = incomeRatio;
	}
}
