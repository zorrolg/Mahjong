package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.game.GamePlayer;
import com.citywar.manager.RoomMgr;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.HallGameType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.PENGYOU_OUTCONFIRM, desc = "排行榜")
public class PengConfirm extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {

    	BaseRoom room = player.getCurrentRoom();        
    	if(room != null && room.getHallType().getHallType() == HallGameType.PENGYOU)
    	{
    		    		
    		int quitType = packet.getInt();    		
    		Packet pkg = new Packet(UserCmdOutType.PENGYOU_OUTCONFIRM);
    		if(quitType == 1)
    		{
    			int countConfirm = 0;
        		List<GamePlayer> players = room.getPlayers();//玩家列表
    			for (GamePlayer temp : players)
    			{
    				if(temp.getPengConfirmState() == 1)
    					countConfirm += 1;
    			}
        		    			
    			player.setPengConfirmState(1);
    			if(countConfirm == 0)
    			{
    				room.setPengyQuit(player.getPlayerInfo().getUserName(), System.currentTimeMillis());
    			}
    			
    			
    			
    			if(countConfirm + 1 == players.size())
    			{
    				room.sendPengYouGameState();
    				RoomMgr.exitRoom(room, player, false, 1);
    			}
    			
    			
    			
    			pkg.putBoolean(true);
    			pkg.putStr(room.getPengQuitUserName());
    			pkg.putLong((room.getPengQuitTime() - System.currentTimeMillis())/1000);
    			
    			pkg.putInt(players.size());
    			for (GamePlayer temp : players)
    			{
    				pkg.putInt(temp.getUserId());
    				pkg.putStr(temp.getPlayerInfo().getUserName());
    				pkg.putInt(temp.getPengConfirmState());
    			}
    			
    		}
    		else
    		{    							
    			pkg.putBoolean(false);
    			pkg.putStr(player.getPlayerInfo().getUserName());
    			
    			
    			room.setPengyouOver(true);
    			List<GamePlayer> players = room.getPlayers();//玩家列表
    			for (GamePlayer temp : players)
    			{
    				temp.setPengConfirmState(0);
    			}    			
    		}
    		room.sendToAll(pkg);
    	}
    	
    	return 0;
    }
    

}

