package com.citywar.usercmd.command;

import java.util.Map;

import com.citywar.dice.entity.StagePrizeInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.StageMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.STAGE_PRIZE, desc = "排行榜")
public class UserStagePrize extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {

        int stage = packet.getInt();
        
        Map<Integer, StagePrizeInfo> prizeList = StageMgr.findStagePrize(stage);
        
        
    	Packet pkg = new Packet(UserCmdOutType.STAGE_PRIZE);
    	pkg.putInt(prizeList.size());
        for (int i = 0;i < prizeList.size(); i++) 
        {
        	if(prizeList.containsKey(i+1))
        	{        		
        		pkg.putInt(prizeList.get(i+1).getIndex());
        		pkg.putStr(prizeList.get(i+1).getPrizeName());
        		
        	}
		}
        
         player.getOut().sendTCP(pkg);
         

    	return 0;
    }
    

}

