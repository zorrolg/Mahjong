package com.citywar.usercmd.command;

import java.util.LinkedList;
import java.util.List;

import com.citywar.dice.entity.PlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.gameutil.PlayerInfoAndAround;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.UserTopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.STAGE_CHARTS, desc = "排行榜")
public class UserStageCharts extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        long startTick = System.currentTimeMillis();
    	int totalRecords = 0;
        int totalPages = 0;
//        int pageSize = 10;
        int stage = packet.getInt();
        int pageNo = packet.getInt();
        int pageSize = packet.getInt();
        
//        System.out.println("topChartsbuildResponse==============================" + stage);
    	
    	List<PlayerInfoAndAround> result = null;
    	result = UserTopMgr.getStageTopList(player, stage);
    	
    	if(result == null ){
    		result = new LinkedList<PlayerInfoAndAround>();
    	}
    	    	
    	
    	
		if(null != result && result.size() > UserTopMgr.TOP_NUMBER) {
			result = result.subList(0, UserTopMgr.TOP_NUMBER);
		}
		
		
    	totalRecords = result.size();
        if (totalRecords > 0)
        {
            if (totalRecords % pageSize == 0)
            {
                totalPages = totalRecords / pageSize;
            }
            else
            {
                totalPages = (totalRecords / pageSize) + 1;
            }
        } 
        
        
        
    	//计算起始索引
        int startIndex = totalRecords < (pageNo - 1) * pageSize ? totalRecords
        		: (pageNo - 1) * pageSize;
        int endIndex = totalRecords < (pageNo * pageSize) ? totalRecords
                : pageNo * pageSize;
        pageSize = endIndex - startIndex;

        
        
        
    	Packet pkg = new Packet(UserCmdOutType.STAGE_CHARTS);
    	pkg.putInt(totalPages);
    	pkg.putInt(pageSize);
    	pkg.putInt(pageNo);

        for (int i = startIndex,j = endIndex; i < j; i++) 
        {
        	PlayerInfoAndAround currentData = result.get(i);
        	topChartsbuildResponse(pkg, currentData, i+1);
		}
        
         player.getOut().sendTCP(pkg);
         
         long executeTime = System.currentTimeMillis() - startTick;
         System.out.println("STAGE_CHARTS===========execute Time===============" + executeTime);
         
    	return 0;
    }
    
    private void topChartsbuildResponse(Packet response, PlayerInfoAndAround currentData, int ranked) {
    	PlayerInfo info = currentData.getAroudPlayer();
    	response.putInt(ranked);
    	response.putInt(info.getUserId());
		String picPath = "";
		if (!info.getPicPath().isEmpty()) {
			picPath = info.getRealPicPath();
		}
		response.putStr(picPath);//原图头像
		response.putStr(info.getUserName());
		response.putInt(info.getLevel());
		response.putStr(LevelMgr.getLevelTitle(info.getLevel()));
		response.putInt(info.getWin());
		response.putInt(info.getLose());
		response.putByte((byte)info.getVipLevel());
		response.putInt(info.getSex());
		
		
		response.putInt(info.getSlaveCount());//奴隶数
		response.putInt(info.getCoins());//金币
		response.putInt(info.getCharmValve());//魅力
		double distance = 0;
		if(currentData.getAroudUser() != null)
		{
			distance = currentData.getAroudUser().getUserDistance();
		}
		response.putDouble(distance);//距离
		
		
//		System.out.println("topChartsbuildResponse========================= " + info.getUserName() + "===========" + info.getCharmValve());
		
    }

}

