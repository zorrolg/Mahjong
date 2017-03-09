package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_DEL_LETTER, desc = "用户删除私信")
public class UserDelLetter extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	int senderId = packet.getInt();
    	if(senderId >0){
    		player.getMsgBox().cleanLetter(senderId);
    	}
    	
    	int type  = 0 ;
    	if(packet.isHasData())
    	{
    		type = packet.getByte();
    		//删除系统私信
    		if(type > 0 ) 
    		{
        		player.getMsgBox().readAddFriend();
        	}
    	}
        return 0;
    }
    

}
