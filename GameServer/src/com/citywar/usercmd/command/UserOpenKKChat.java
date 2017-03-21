package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.manager.KKMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_OPEN_KKCHAT, desc = "用户发送私信")
public class UserOpenKKChat extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	   	    	
    	int toUserId = packet.getInt();
    	String toUserName = packet.getStr();
    	
    	
    	boolean isSuccess = KKMgr.OpenUserChat(player, toUserId, toUserName);
    	
    	
    	Packet pkg = new Packet(UserCmdOutType.USER_OPEN_KKCHAT);
		pkg.putBoolean(isSuccess);    	
        player.getOut().sendTCP(pkg);
        
    	
        return 0;
    }
    

}
