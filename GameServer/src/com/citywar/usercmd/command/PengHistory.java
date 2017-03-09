package com.citywar.usercmd.command;

import java.util.List;

import net.sf.json.JSONArray;

import com.citywar.bll.PengGameBussiness;
import com.citywar.dice.entity.PengGame;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameutil.PengGameScore;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.PENGYOU_HISTORY, desc = "排行榜")
public class PengHistory extends AbstractUserCmd
{

    @SuppressWarnings("unchecked")
	@Override
    public int execute(GamePlayer player, Packet packet)
    {

    	int type = packet.getInt();
		int id = packet.getInt();
		int page = packet.getInt();
		
		
		List<PengGame> listPengGame;
		
		if(type == 1)
			listPengGame = PengGameBussiness.getPengGameList(player.getUserId(), page);
		else
			listPengGame = PengGameBussiness.getPengGame(id);
		

		
		Packet pkg = new Packet(UserCmdOutType.PENGYOU_HISTORY);
		pkg.putInt(listPengGame.size());
		for(int i= 0; i<listPengGame.size(); i++ )
		{
						
			pkg.putInt(listPengGame.get(i).getId());
			pkg.putInt(listPengGame.get(i).getRoomid());
			pkg.putStr(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(listPengGame.get(i).getGamedate()));
			
			JSONArray json = JSONArray.fromObject(listPengGame.get(i).getScorelist());
			List<PengGameScore> GameList = (List<PengGameScore>)JSONArray.toCollection(json, PengGameScore.class);
			  
			if(json.toString().equals("[null]"))
				GameList.clear();
			
			pkg.putInt(GameList.size());
			for(int j= 0; j<GameList.size(); j++ )
			{
				pkg.putStr(GameList.get(j).getName());
				pkg.putInt(GameList.get(j).getScore());
			}
		}
		player.getOut().sendTCP(pkg);
    			
		
    	
    	return 0;
    }
    

}

