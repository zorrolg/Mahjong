package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.AwardMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_BROKE_CHECK, desc = "玩家奖励")
public class UserBrokeCheckCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	
    	AwardMgr.checkCoinsLessAwardCoins(player);
    	        	
        return 0;
    }
}

