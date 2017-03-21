package com.citywar.http.pengyou;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import org.apache.log4j.Logger;

import com.citywar.bll.PlayerBussiness;
import com.citywar.bll.ServerUserBussiness;
import com.citywar.dice.entity.ServerUser;
import com.citywar.game.GamePlayer;
import com.citywar.manager.HallMgr;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.RobotChatMgr;
import com.citywar.manager.ShopMgr;
import com.citywar.manager.StageMgr;
import com.citywar.manager.TaskMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.type.HttpProcessType;

public class pengyouProcess {

	private static final Logger logger = Logger.getLogger(pengyouProcess.class.getName());
	
	
public static HttpProcessType processBuy (Socket client,Map<String, String> tempMap, String dataLine) throws IOException {
		
	
	
	
		
	HttpProcessType processType = null;
		
		try {
			Thread.sleep(5000);
			processType = processSave(client, tempMap);
			
		}
		catch (Exception ex) {
			processType = HttpProcessType.FAIL;
			processType.setErrorInfo(ex.getMessage());
	  	}
		finally{
			
		}
	    	
		
		String returnMsg = "fail";//"{\"status\":\"error\"}";
		if(processType == HttpProcessType.SUCCESS)
			returnMsg = "ok";//"{\"status\":\"success\"}";
		else
			logger.error("===============================tongbuProcess========:" + processType.getValue() + "========="+ processType.getErrorInfo() + "==========" + dataLine);
		
		
		
		processType.setReturnInfo(returnMsg);
    	return processType;
    	
	}
		
		

	
	
	
	public static HttpProcessType processSave (Socket client, Map<String, String> map) {
		//System.out.println("orderString===" + orderString);
    	
		HttpProcessType isSuccess = HttpProcessType.BUY_FAIL;      	
		
		try {
			if (map.size() > 0) {
				
				
				String action = map.get("action");
				if(action.equals("playerBuyCard"))
				{
					int playerId = Integer.valueOf(map.get("playerId"));               
					int userId = Integer.valueOf(map.get("userId"));               
					int cardCount = Integer.valueOf(map.get("cardCount"));               
	       
					ServerUser user = ServerUserBussiness.getUserCardsById(userId);
					if(user.getCardcount() >= cardCount)
					{
						GamePlayer player = WorldMgr.getPlayerByID(playerId);					
						if(player != null)
						{
							player.getPlayerInfo().setCardCount(player.getPlayerInfo().getCardCount() + cardCount);
							player.getOut().sendUpdateBaseInfo();				            
							player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
							isSuccess = HttpProcessType.SUCCESS;	
						}
						else
						{
							PlayerBussiness.updateCardCount(cardCount, playerId);
							isSuccess = HttpProcessType.SUCCESS;	
						}
						
						if(isSuccess == HttpProcessType.SUCCESS)
							ServerUserBussiness.subUserCardsInfo(userId, cardCount);					
					}
				}
				else if(action.equals("useBuyCard"))
				{
					int fromUlayerId = Integer.valueOf(map.get("fromuserId"));               
					int toUserId = Integer.valueOf(map.get("touserId"));               
					int cardCount = Integer.valueOf(map.get("cardCount"));               
	       
					ServerUser fromUser = ServerUserBussiness.getUserCardsById(fromUlayerId);
//					ServerUser toUser = ServerUserBussiness.getUserCardsById(toUserId);
					if(fromUser.getCardcount() >= cardCount)
					{						
						ServerUserBussiness.subUserCardsInfo(cardCount, fromUlayerId);
						ServerUserBussiness.addUserCardsInfo(cardCount, toUserId);							
						isSuccess = HttpProcessType.SUCCESS;				
					}
				}
				else if(action.equals("reflash"))
				{
					
					String reflashType = map.get("reflashType");        

					switch (reflashType) {
					case "hall"://表示创建房间
						HallMgr.reload();
						break;
					case "stage"://表示创建房间
						StageMgr.reload();
						break;
					case "task"://表示创建房间
						TaskMgr.reload();
						break;
					case "level"://表示创建房间
						LevelMgr.reload();
						break;
					case "shop"://表示创建房间
						ShopMgr.reload();
						break;
					case "chat"://表示创建房间
						RobotChatMgr.reload();
						break;
					}
					isSuccess = HttpProcessType.SUCCESS;	
				}
			}
        
		} catch (Exception ex) {
			isSuccess = HttpProcessType.BUY_SAVE_ERROR;	
        	isSuccess.setErrorInfo(ex.getMessage());
        	
        	
        	logger.error("===============================yitengProcess========error==========" + ex.getMessage());
  	  	}
			
		return isSuccess;
				
	}

   
    

}
