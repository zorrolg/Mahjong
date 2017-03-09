package com.citywar.usercmd.command;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.citywar.bll.OrderBusiness;
import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.ShopGoodInfo;
import com.citywar.dice.entity.UserOrderInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.KKMgr;
import com.citywar.manager.ShopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.Config;

@UserCmdAnnotation(code = UserCmdType.USER_BUY_ORDER_ANDROID, desc = "用户购买的苹果商品订单信息")
public class UserH5BuyOrderCmd extends AbstractUserCmd{

    private static final Logger logger = Logger.getLogger(UserH5BuyOrderCmd.class.getName());
    
	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		int BuyItem	   			= packet.getInt();
		
		ShopGoodInfo shopGood = ShopMgr.getShopGoodById(BuyItem);
		
		

		int status = -1;
		Map<String, String> response = KKMgr.PayMoney(player, BuyItem, shopGood.getPrice());		 		
		if (response.size() > 0) {
			saveToDb(player, response);
			status = Integer.valueOf(response.get("State"));			
		}
		
		Packet pkg = new Packet(UserCmdOutType.USER_BUY_ORDER_ANDROID);
		pkg.putInt(BuyItem);
		pkg.putStr(response.get("OrderId"));
        pkg.putInt(status);
        pkg.putStr(response.get("Msg"));
        player.getOut().sendTCP(pkg);
        
		return 0;
		
	}
	
	
	
	
	



	
	
	public static void main(String[] args) {
		String str = "{/n\"receipt\":{\"original_purchase_date_pst\":\"2012-06-06 05:31:34 America/Los_Angeles\", \"original_transaction_id\":\"1000000050766806\", \"original_purchase_date_ms\":\"1338985894755\", \"transaction_id\":\"1000000050766806\", \"quantity\":\"1\", \"product_id\":\"20001\", \"bvrs\":\"1.0.0\", \"purchase_date_ms\":\"1338985894755\", \"purchase_date\":\"2012-06-06 12:31:34 Etc/GMT\", \"original_purchase_date\":\"2012-06-06 12:31:34 Etc/GMT\", \"purchase_date_pst\":\"2012-06-06 05:31:34 America/Los_Angeles\", \"bid\":\"com.DiceGame201206\", \"item_id\":\"533710547\"}, \"status\":0}/n";
		str = str.replaceAll("/n", "");
		//System.out.println(str);
		int status = -1;
		org.json.JSONObject result;
		try {
			result = new org.json.JSONObject(str);
			status = result.getInt("status");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(status);
	}
	
	public boolean saveToDb (GamePlayer player, Map<String, String> orderMap) {
		//System.out.println("orderString===" + orderString);
		
		
		try {
			
		
			if (orderMap.size() > 0) {
	
				String thirdName = Config.getValue("thirdName");		
				
				
				
		        
				UserOrderInfo userOrderInfo = new UserOrderInfo();
				userOrderInfo.setChannelName(thirdName);
				userOrderInfo.setUserId(player.getUserId());
				userOrderInfo.setTransactionId(orderMap.get("OrderId"));
				userOrderInfo.setStatus(orderMap.get("State"));
				userOrderInfo.setItemId(orderMap.get("BuyItem"));
				userOrderInfo.setProductId(orderMap.get("BuyItem"));
				userOrderInfo.setBid("com.citywar.game");
				userOrderInfo.setMoney(Float.valueOf(orderMap.get("PayMoney")));
	
				
				
				userOrderInfo.setOriginalPurchaseDatePst(" ");
				userOrderInfo.setPurchaseDateMs(" ");
				userOrderInfo.setOriginalTransactionId(" ");
				userOrderInfo.setOriginalPurchaseDateMs(" ");			
				userOrderInfo.setQuantity(" ");
				userOrderInfo.setBvrs(" ");
				userOrderInfo.setHostedIapVersion(" ");			
				userOrderInfo.setPurchaseDate(" ");
				userOrderInfo.setOriginalPurchaseDate(" ");
				userOrderInfo.setPurchaseDatePst(" ");
				
				
				
				List<UserOrderInfo> list = OrderBusiness.getUserOrderByTransactionId(userOrderInfo.getTransactionId());
				if(list == null || list.size() <= 0) {
					boolean result = OrderBusiness.insertUserOrder(userOrderInfo);
					if(userOrderInfo.getBid().equals("com.citywar.game")) {
						if (result) 
						{
							if (!userOrderInfo.getStatus().isEmpty() && !userOrderInfo.getItemId().isEmpty())
							{
								int status = Integer.valueOf(userOrderInfo.getStatus());
								int shop_id = Integer.valueOf(userOrderInfo.getItemId());
								if (status == 1 && shop_id > 0) {
									player.getPropBag().addOneItem(shop_id);
									
									ShopGoodInfo shopGood = ShopMgr.getShopGoodById(shop_id);
									player.isFinishTask(TaskConditionType.ChargeMoney, shopGood.getCount(), 0);
									
									
									if(userOrderInfo.getMoney() > 0)
									{
										if(thirdName.equals("cutv"))
										{											
											player.getPlayerInfo().setFortune(Integer.parseInt(orderMap.get("Fortune")));
											PlayerBussiness.updateAll(player.getUserId(), player.getPlayerInfo());
										}
										else if(thirdName.equals("h5kk"))
										{
//											player.getPlayerInfo().setFortune(userOrderInfo.getMoney());
//											PlayerBussiness.updateAll(player.getUserId(), player.getPlayerInfo());
										}
										else if(thirdName.equals("yidongtv"))
										{
											player.addCoins(22000000);
//											player.getPlayerInfo().setCoins(player.getPlayerInfo().getCoins() + 22000000); 
											PlayerBussiness.updateAll(player.getUserId(), player.getPlayerInfo());
										}
									}
								}
							}
				            player.getOut().sendUpdateBaseInfo();
				            player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
							return true;
						} else {
							logger.error(player.getUserId() + "==============UserBuyOrderCmd saveToDb insertUserOrder 有问题!");
						}
					} else {
						logger.error(player.getUserId() + "===============UserBuyOrderCmd saveToDb 有问题!BID不是com.citywar.yediandahuawang!");
					}
				} else {
					logger.error(player.getUserId() + "=============UserBuyOrderCmd saveToDb 有问题!已经存在transaction_id相同的数据");
				}
			}
		
		 } catch(Exception ex) {
	         	logger.error("H5BuyOrder错误:" + ex.getLocalizedMessage());
		 }   
		
		logger.debug("orderString123===");
		return false;
		
	}
}
