package com.citywar.usercmd.command;

import java.util.LinkedList;
import java.util.List;

import com.citywar.dice.entity.AroundUser;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameutil.PlayerInfoAndAround;
import com.citywar.manager.AllAroundPlayerInfoMgr;
import com.citywar.manager.UserTopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.DistanceType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.type.UserTopType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_NEW_CHARTS, desc = "排行榜")
public class ShowNewUserCharts extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        long startTick = System.currentTimeMillis();
    	int totalRecords = 0;
        int totalPages = 0;
        int pageSize = 20;
        boolean isPageOne = false;
        int pageNo = packet.getInt();
        if(1 == pageNo) {
        	isPageOne = true;
        }
        
    	DistanceType maxDistance =DistanceType.valueOf(packet.getInt());
    	UserTopType type = UserTopType.valueOf(packet.getInt());
    	int topLevel = packet.getInt();
    	
    	List<PlayerInfoAndAround> result = null;

    	if(maxDistance == DistanceType.WORLD)
    	{
    		result = UserTopMgr.getWordChartsByType(player, type, topLevel);
    	}
    	else {
    		result = UserTopMgr.getNearChartsByType(player, type, maxDistance.getDistance());
    	}
    	
    	if(result == null ){
    		result = new LinkedList<PlayerInfoAndAround>();
    	}
    	
    	int selfRanked = 0;
    	if(maxDistance == DistanceType.WORLD && type != UserTopType.INCREASECOINS)
    	{
    		PlayerInfoAndAround currentData;
    		for(int i = 0;i < result.size(); i++) {
            	currentData = result.get(i);
            	if(currentData.getAroudUser() == null) {
                	currentData = player.getPlayerAroudUser().getAroadUserPlayer(currentData.getAroudPlayer());
                	//System.out.println("currentData.getAroudUser() == null.userId = " + currentData.getUserId());
            	}
            	if(currentData.getAroudPlayer().equals(player.getPlayerInfo())) {//不同的引用
            		selfRanked = i+1;
            		//System.out.println(UserTopType.CHARM+"玩家"+player.getPlayerInfo().getUserName()+"的排名为"+selfRanked);
            	}
            }
            if(0 == selfRanked) {
        		List<PlayerInfoAndAround> topResult = null;
            	if(player.getPlayerInfo().getPos().equals("0,0")) {
            		topResult = AllAroundPlayerInfoMgr.getAllPlayerInfoAndAround();
            	} else {
            		topResult = player.getPlayerAroudUser().getNearPlayerInfoAndAround(30000000);//3万千米
            	}
            	for(PlayerInfoAndAround info : result) {
            		if( ! topResult.contains(info)) {
            			topResult.add(info);
            		}
            	}
            	type.sortPA(topResult);
            	result = topResult;
        		selfRanked = UserTopMgr.getUserChartsOnList(topResult, player);
            }
    	} else {
    		selfRanked = UserTopMgr.getUserChartsOnList(result, player);
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
        if(isPageOne && 0 != pageSize) {
        	pageSize++;
        }
    	Packet pkg = new Packet(UserCmdOutType.USER_NEW_CHARTS);
    	pkg.putInt(totalPages);
    	pkg.putInt(pageSize);
    	pkg.putInt(pageNo);
    	int chartsNumber = 0;
    	//System.out.println("当前页="+pageNo +" 起始索引="+startIndex +"  最后索引="+endIndex);
        for (int i = startIndex,j = endIndex; i < j; i++) 
        {
        	PlayerInfoAndAround currentData = result.get(i);
        	if(0 == i) {
        		PlayerInfoAndAround myData = new PlayerInfoAndAround();
        		myData.setAroudPlayer(player.getPlayerInfo());
        		AroundUser aroudUser = new AroundUser();
        		aroudUser.setUserDistance(0);
        		myData.setAroudUser(aroudUser);
            	chartsNumber = getChartsNumber(type, myData.getAroudPlayer());
        		topChartsbuildResponse(pkg, myData, chartsNumber, selfRanked);
        	}
        	chartsNumber = getChartsNumber(type, currentData.getAroudPlayer());
        	topChartsbuildResponse(pkg, currentData, chartsNumber, i+1);
		}
         player.getOut().sendTCP(pkg);
         long executeTime = System.currentTimeMillis() - startTick;
         System.out.println("ShowUserCharts use " + executeTime + " ms");
    	return 0;
    }
    
    private int getChartsNumber(UserTopType type, PlayerInfo info) {
    	int chartsNumber = 0;
//    	if(type == UserTopType.CHARM){
//    		chartsNumber = info.getCharmValve();
//		}else if(type == UserTopType.GOLD){
//			chartsNumber = info.getCoins();
//		}else if(type == UserTopType.SLAVECOUNT) {
//			chartsNumber = info.getSlaveCount();
//		}else 
    	if(type == UserTopType.WIN){
			chartsNumber = info.getWin() + info.getLose();
		}else if(type == UserTopType.INCREASECOINS) {
			chartsNumber = info.getDayIncreaseCoins();
		}
		return chartsNumber;
    }
    
    private void topChartsbuildResponse(Packet response, PlayerInfoAndAround currentData, int chartsNumber, int ranked) {
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
		response.putInt(info.getWin());
		response.putInt(info.getLose());
		response.putInt(info.getSex());
		
		response.putInt(chartsNumber);//对应的排序字段
		double distance = 0;
		if(currentData.getAroudUser() != null)
		{
			distance = currentData.getAroudUser().getUserDistance();
		}
		response.putDouble(distance);//距离
    }

}

