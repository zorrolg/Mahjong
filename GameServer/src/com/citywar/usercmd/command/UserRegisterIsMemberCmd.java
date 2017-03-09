/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import org.apache.log4j.Logger;

import com.citywar.bll.UserRegisterBussiness;
import com.citywar.dice.entity.UserRegister;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 
 * @author zhiyun.peng
 * 
 *@since   V2.2
 *
 */
@UserCmdAnnotation(code = UserCmdType.USER_IS_REG_MEMBER, desc = "玩家能否注册会员")
public class UserRegisterIsMemberCmd extends AbstractUserCmd
{
    private static final Logger logger = Logger.getLogger(UserRegisterIsMemberCmd.class.getName());
    
    /** 每个用户最多能注册账号的次数*/
    private final int MAX_REG_COUNT = 2;
    @Override
    public int execute(GamePlayer player, Packet packet)
    { 
    	boolean result = true;
    	String MacId = player.getPlayerInfo().getMachineryId();
    	
    	if(MacId == null || MacId.isEmpty())
    	{
    		logger.warn("ID为"+player.getPlayerInfo().getUserId()+"注册了一个用户");
    	}else{
    		UserRegister userRegister = UserRegisterBussiness.doSearchByMacId(MacId);
    		
    		if(userRegister != null && userRegister.getHasResCount()>=MAX_REG_COUNT)
    		{
    			result = false;
    		}
    	}
    	
    	Packet response = new Packet(UserCmdOutType.USER_IS_REG_MEMBER);
		response.putBoolean(result);
		player.getOut().sendTCP(response);
    	
    	return 0;
    }
    

}
