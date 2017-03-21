package com.citywar.usercmd.command;

import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_PERSENT_MONEY, desc = "赠送")
public class UserPersentMoney extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	
    	
        
    	String strUserName		= packet.getStr();
    	int persentMoney       	= packet.getInt(); 
    	int persentCoin       	= packet.getInt(); 
    	
    	
    	
    	
    	
    	
    	int result = 0;
    	String strMsg = "";
//    	boolean isPush = false;
    	int coins = player.getPlayerInfo().getCoins();
    	if(coins > 1800000)
    	{
    		
    		PlayerInfo receiverPlayer = PlayerBussiness.getPlayerInfoByName(strUserName);    	
    		
    		if(receiverPlayer != null)
    		{
    			GamePlayer receiver = WorldMgr.getPlayerByID(receiverPlayer.getUserId());
        		if(receiver != null) //接收者在线 
        		{
        			player.addCoins(-1800000);
        			receiver.addCoins(1800000);
        			
        			player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)1);
        			receiver.getOut().sendUpdatePrivateInfo(receiver.getPlayerInfo(), (byte)1);
        			
        			sendOnLine(receiver, result, persentMoney, persentCoin);    
//        			player.getPlayerInfo().setCoins(player.getPlayerInfo().getCoins() - 1800000);
//        			receiver.getPlayerInfo().setCoins(receiver.getPlayerInfo().getCoins() + 1800000);
        		}
        		else 
        		{
//        			isPush = true;     		
//        			player.getPlayerInfo().setCoins(player.getPlayerInfo().getCoins() - 1800000);
        			player.addCoins(-1800000);
        			PlayerBussiness.updateCoins(receiverPlayer.getUserId(), receiverPlayer.getCoins() + 1800000);
        		}
        		
        		result = 1;
    			
    		}
    		else
    		{
    			strMsg = "没有找到该用户!";
    		}
    		
    	}
    	else
    	{
    		strMsg = "金币不足!";
    	}
    	
    	System.out.println("UserPersentMoney==================" + strMsg + "======" + result);
    	
    	Packet pkg = new Packet(UserCmdOutType.USER_PERSENT_MONEY);
    	pkg.putInt(result);
    	pkg.putInt(persentMoney);
    	pkg.putInt(persentCoin);
    	pkg.putStr(strMsg);
    	player.getOut().sendTCP(pkg);
    	
    	
        return 0;
    }
    
    
    

    private void sendOnLine(GamePlayer receiver, int result, int money, int coin){
    	
    	
    	Packet pkg = new Packet(UserCmdOutType.USER_PERSENT_MONEY);
    	pkg.putInt(result);
    	pkg.putInt(money);
    	pkg.putInt(coin);
    	pkg.putStr("");
    	receiver.getOut().sendTCP(pkg);
    	        	
    }


    
}

