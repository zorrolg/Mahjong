package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.HallGameType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.PENGYOU_GAMECONTINUE, desc = "排行榜")
public class PengyouContinue extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {

    	int index = packet.getInt();
    	
    	BaseRoom room = player.getCurrentRoom();        
    	if(room != null && room.getHallType().getHallType() == HallGameType.PENGYOU)
    	{
    		room.continuePengYouGame(player, index);
    	}
    	
    	return 0;
    }
    

}

