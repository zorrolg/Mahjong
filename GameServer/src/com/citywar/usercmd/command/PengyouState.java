package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.HallGameType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.PENGYOU_GAMESTATE, desc = "排行榜")
public class PengyouState extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {

    	BaseRoom room = player.getCurrentRoom();        
    	if(room != null && room.getHallType().getHallType() == HallGameType.PENGYOU)
    	{
    		
    		room.sendPengYouGameState();
    		
    		
//    		List<GamePlayer> playerList = room.getPlayers();
//    		
//    		Packet pkg = new Packet(UserCmdOutType.PENGYOU_GAMESTATE);
//    		
//    		pkg.putInt(room.getRoomId());
//    		pkg.putStr(room.getPassword());
//    		
//    		pkg.putInt(room.getPengyouRoundCount());
//    		pkg.putInt(room.getPengyouRoundIndex());
//    		
//    		
//        	pkg.putInt(playerList.size());
//            for (int i = 0;i < playerList.size(); i++) 
//            {            	      
//            	GamePlayer gp = playerList.get(i);
//            	
//            	pkg.putInt(gp.getUserId());
//            	pkg.putStr(gp.getPlayerInfo().getUserName());            	
//            	pkg.putInt(gp.getPlayerInfo().getPengYouCoins());       
//    		}
//            
//             player.getOut().sendTCP(pkg);
             
             
    	}
    	
        
        
        
    	
         

    	return 0;
    }
    

}

