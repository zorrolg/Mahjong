package com.citywar.usercmd.command;

import java.util.List;

import net.sf.json.JSONArray;

import com.citywar.bll.PengGameBussiness;
import com.citywar.dice.entity.PengGameDetail;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameutil.PengGameScore;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.PENGYOU_HISTORY_DETAIL, desc = "排行榜")
public class PengHistoryDetail extends AbstractUserCmd
{

    @SuppressWarnings("unchecked")
	@Override
    public int execute(GamePlayer player, Packet packet)
    {

		int id = packet.getInt();
				   		
        		
		List<PengGameDetail> listPengGameDetail = PengGameBussiness.getPengGameDetailList(id);
		
		Packet pkg = new Packet(UserCmdOutType.PENGYOU_HISTORY_DETAIL);
		pkg.putInt(listPengGameDetail.size());
		for(int i= 0; i<listPengGameDetail.size(); i++ )
		{
			pkg.putInt(i + 1);
			pkg.putStr(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(listPengGameDetail.get(i).getGamedate()));
			
			
			
			JSONArray json = JSONArray.fromObject(listPengGameDetail.get(i).getGamelist());
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

