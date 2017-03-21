package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.dice.entity.PengConfigInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.HallMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.PENGYOU_GAMECONFIG, desc = "排行榜")
public class PengyouConfig extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	
		Packet pkg = new Packet(UserCmdOutType.PENGYOU_GAMECONFIG);
		
		List<PengConfigInfo> configList = HallMgr.getPengConfigList();
		
		pkg.putInt(configList.size());
        for (int i = 0;i < configList.size(); i++) 
        {
        	PengConfigInfo config = configList.get(i);
        	
        	pkg.putInt(config.getTypeId());
        	pkg.putInt(config.getTypeIndex());
        	pkg.putStr(config.getName());
        	pkg.putStr(config.getDescript());
		}

        player.getOut().sendTCP(pkg);
    	return 0;
    }
    

}

