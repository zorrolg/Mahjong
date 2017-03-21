package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_SEND_GIFT, desc = "玩家送礼")
public class UserSendGift extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        
    	Packet response = new Packet(UserCmdOutType.USER_SEND_GIFT);
    	
    	int giftId     = packet.getInt(); //赠品Id 或者 模板ID
    	int senderId   = packet.getInt(); //赠送者ID
    	int receiverId = packet.getInt(); //接受者的ID
    	int sendCount  = packet.getInt(); //赠送数量
    	
    	GamePlayer sender   = WorldMgr.getPlayerByID(senderId);
    	
    	if(giftId <=0 || senderId<=0 || receiverId<=0 || sendCount <=0 || sender == null)
    	{
    		return 0;
    	}
    	
    	if(sender.sendGift(receiverId, sendCount,giftId))
    		response.putBoolean(true);
    	else
    		response.putBoolean(false);
    	
    	player.sendTcp(response);
    	
        return 0;
    }
    
    
    
}

