package com.citywar.usercmd.command;

import java.util.LinkedList;
import java.util.List;

import com.citywar.dice.entity.PlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.gameutil.PlayerInfoAndAround;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.UserTopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.DistanceType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.type.UserTopType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_CHARTS, desc = "排行榜")
public class ShowUserCharts extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        long startTick = System.currentTimeMillis();
    	int totalRecords = 0;
        int totalPages = 0;
        int pageSize = 10;
//        boolean isPageOne = false;
        int pageNo = packet.getInt();
//        if(1 == pageNo) {
//        	isPageOne = true;
//        }
        
    	DistanceType MaxDistance =DistanceType.valueOf(packet.getInt());
    	UserTopType type = UserTopType.valueOf(packet.getInt());
//    	int topLevel = packet.getInt();
    	
    	List<PlayerInfoAndAround> result = null;

    	if(MaxDistance == DistanceType.WORLD)
    	{
    		result = UserTopMgr.getWordChartsByType(player, type, 0);
//    		if(topLevel == 0)
//    			result = UserTopMgr.getWordChartsByType(player, type, topLevel);
//    		else
//    			result = UserTopMgr.getLevelTopList(player, topLevel);
    	}
    	else {
    		result = UserTopMgr.getNearChartsByType(player, type, MaxDistance.getDistance());
    	}
    	
    	if(result == null ){
    		result = new LinkedList<PlayerInfoAndAround>();
    	}
    	
    	int selfRanked = 0;
    	if(MaxDistance == DistanceType.WORLD)
    	{
    		List<PlayerInfoAndAround> tempResult = new LinkedList<PlayerInfoAndAround>();
    		for(int i = 0;i < result.size(); i++) {
            	PlayerInfoAndAround currentData = result.get(i);
            	if(currentData.getAroudUser() == null) {
                	currentData = player.getPlayerAroudUser().getAroadUserPlayer(currentData.getAroudPlayer());
                	result.remove(i);
                	result.add(i, currentData);
            	}
            	if(currentData.getUserId() == player.getUserId()) {
//            	if(currentData.getAroudPlayer() == player.getPlayerInfo()) {不同的引用
            		selfRanked = i+1;
            		//System.out.println(UserTopType.CHARM+"玩家"+player.getPlayerInfo().getUserName()+"的排名为"+selfRanked);
            	}
            	if( ! tempResult.contains(currentData)) {
            		tempResult.add(currentData);
            	}
            }
//            if(0 == selfRanked) {
//        		List<PlayerInfoAndAround> topResult = UserTopMgr.getNearChartsByType(player, type, 30000000);//3万千米
//        		topResult.addAll(0, tempResult);
//        		selfRanked = UserTopMgr.getUserChartsOnList(topResult, player);
//            }
    	}
    	else {
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
//        if(isPageOne && 0 != pageSize) {
//        	pageSize++;
//        }
    	Packet pkg = new Packet(UserCmdOutType.USER_CHARTS);
    	pkg.putInt(totalPages);
    	pkg.putInt(pageSize);
    	pkg.putInt(pageNo);
    	//System.out.println("当前页="+pageNo +" 起始索引="+startIndex +"  最后索引="+endIndex);
        for (int i = startIndex,j = endIndex; i < j; i++) 
        {
        	PlayerInfoAndAround currentData = result.get(i);
//        	if(0 == i) {
//        		PlayerInfoAndAround myData = new PlayerInfoAndAround();
//        		myData.setAroudPlayer(player.getPlayerInfo());
//        		AroundUser aroudUser = new AroundUser();
//        		aroudUser.setUserDistance(0);
//        		myData.setAroudUser(aroudUser);
//        		topChartsbuildResponse(pkg, myData, selfRanked);
//        	}
        	topChartsbuildResponse(pkg, currentData, i+1);
//			if(type == UserTopType.CHARM){
//				//System.out.println("索引为"+i+"的用户 魅力值为="+currentInfo.getCharmValve());
//			}else if(type == UserTopType.GOLD){
//				//System.out.println("索引为"+i+"的用户 黄金为="+currentInfo.getCoins());
//			}else if(type == UserTopType.SLAVECOUNT) {
//				//System.out.println("索引为"+i+"的用户 奴隶数为="+currentInfo.getSlaveCount());
//			}
		}
         player.getOut().sendTCP(pkg);
         long executeTime = System.currentTimeMillis() - startTick;
         System.out.println("USER_CHARTS===========" + executeTime);

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
		
		
		//System.out.println("topChartsbuildResponse========================= " + info.getUserName() + " ");
		
    }

}

