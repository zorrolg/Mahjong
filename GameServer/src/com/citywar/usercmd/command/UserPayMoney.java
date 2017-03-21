package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_PAY_MONEY, desc = "钻石购买")
public class UserPayMoney extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        
    	Packet response = new Packet(UserCmdOutType.USER_PAY_MONEY);
    	

    	int buyCoin       	= packet.getInt(); //赠品Id 或者 模板ID
    	int money = player.addCoinByMoney(buyCoin);
    	
    	
    	response.putInt(money);
    	player.sendTcp(response);
    	
    	player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
    	
    	
    	if(player.getCurrentRoom() != null)
			player.getCurrentRoom().sendRoomPlayerCoin(player, buyCoin);
    	
        return 0;
    }
    
    
    
}

