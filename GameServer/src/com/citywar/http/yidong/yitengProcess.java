package com.citywar.http.yidong;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.citywar.bll.OrderBusiness;
import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.ShopGoodInfo;
import com.citywar.dice.entity.UserOrderInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.ShopMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.type.HttpProcessType;
import com.citywar.type.TaskConditionType;

public class yitengProcess {

	private static final Logger logger = Logger.getLogger(yitengProcess.class.getName());
	
	
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
				
				
				
				String gameId = map.get("gameId");               
				String orderId = map.get("orderId");               
				String billingIndex = map.get("billingIndex");               
				String billingFee = map.get("billingFee");               
				String billingPrice = map.get("billingPrice");               
				String billingCount = map.get("billingCount");               
				String extStr = map.get("extStr");               
				String sign = map.get("sign");               
				
				
				
//				String tempsign = sign.toLowerCase();
//	            if (tempsign == sign.toLowerCase())
//	            {
	            		
	            	List<UserOrderInfo> list = OrderBusiness.getUserOrderByTransactionId(extStr);
	    			if(list == null || list.size() >= 0) {
	    					    				
	    				UserOrderInfo userOrderInfo =list.get(0);
	    				
	    				int userID = userOrderInfo.getUserId();    			
	    				GamePlayer player = WorldMgr.getPlayerByID(userID);
	    				
						if (!userOrderInfo.getStatus().isEmpty() && !userOrderInfo.getProductId().isEmpty())
						{
							int status = Integer.valueOf(userOrderInfo.getStatus());
							int shop_id = Integer.valueOf(userOrderInfo.getProductId());
							if (status == 0 && shop_id > 0) {
								
								ShopGoodInfo shopGood = ShopMgr.getShopGoodById(shop_id);
								player.getPropBag().addOneItem(shop_id);
								
								if(shopGood.getId() == 115)
								{
									player.addMoney(2000);
								}
								
								player.getOut().sendReturnMessage(true, LanguageMgr.getTranslation("CityWar.Shop.Success"));
								
								player.isFinishTask(TaskConditionType.ChargeMoney, shopGood.getCount(), 0);
								
								userOrderInfo.setStatus("1");
								OrderBusiness.update(userOrderInfo);
								isSuccess = HttpProcessType.SUCCESS;	  	
								
								
							}
						}
			            player.getOut().sendUpdateBaseInfo();
			            player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
	    						
	    			} 
//	            }	            	
			}
        
		} catch (Exception ex) {
			isSuccess = HttpProcessType.BUY_SAVE_ERROR;	
        	isSuccess.setErrorInfo(ex.getMessage());
        	
        	
        	logger.error("===============================yitengProcess========error==========" + ex.getMessage());
  	  	}
			
		return isSuccess;
				
	}

   
    

}
