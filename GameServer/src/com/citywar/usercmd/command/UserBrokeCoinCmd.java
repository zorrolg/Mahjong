package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.AwardMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_BROKE_COIN, desc = "玩家奖励")
public class UserBrokeCoinCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	
    	AwardMgr.palyerCoinsLessAwardCoins(player);
    	    	    
    	
        return 0;
    }
}

