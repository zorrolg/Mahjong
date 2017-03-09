package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.Config;

@UserCmdAnnotation(code = UserCmdType.USER_EXCHANGE_RATE, desc = "获取汇率")
public class UserExchangeRate extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        
    	Packet response = new Packet(UserCmdOutType.USER_EXCHANGE_RATE);
    	
    	int type = packet.getInt();
    	
    	if(type == 1)
    	{
    		int ExchangeRate = Integer.parseInt(Config.getValue("money_coin_exchangerate"));
        	response.putInt(ExchangeRate);
        	
        	int MinuteMoney = Integer.parseInt(Config.getValue("minute_money_exchangerate"));
        	response.putInt(MinuteMoney);
    	}
    	
    	
    	

    	player.sendTcp(response);
    	
    	
        return 0;
    }
    
    
    
}

