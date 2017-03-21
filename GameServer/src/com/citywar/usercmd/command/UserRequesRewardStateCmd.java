package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_REWARD_STATE, desc = "玩家目前奖励状况")
public class UserRequesRewardStateCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	player.getOut().sendNewestRewardState();
    	
        return 0;
    }
    
    
    
}

