package com.citywar.usercmd.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.citywar.bll.OrderBusiness;
import com.citywar.dice.entity.ShopGoodInfo;
import com.citywar.dice.entity.UserOrderInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.ShopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.Config;

@UserCmdAnnotation(code = UserCmdType.USER_BUY_ORDER, desc = "用户购买的苹果商品订单信息")
public class UserBuyOrderCmd extends AbstractUserCmd{

    private static final Logger logger = Logger.getLogger(UserBuyOrderCmd.class.getName());
    
	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		int type	   	= packet.getInt();
		int length 		= packet.getInt();
		
		
		String receipt = "";
		for(int i = 0;i<length;i++)
			receipt = receipt + packet.getStr();
		
		
		//System.out.println("receipt===" + receipt);
		String response = "";
		response = verifyReceipt(type, receipt);
		response = response.replaceAll("/n", "");
		//System.out.println("After === replace === " + response);
		int status = -1;
		org.json.JSONObject result;
		try {
			result = new org.json.JSONObject(response);
			status = result.getInt("status");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		//System.out.println(status);
		if (status == 0) {
			saveToDb(player, response);
		} else {

		}
		
		Packet pkg = new Packet(UserCmdOutType.USER_BUY_ORDER);
        pkg.putInt(status);
        player.getOut().sendTCP(pkg);
        
		return 0;
	}

	public static String verifyReceipt(int type, String receipt) {
		
		try {
			
			
			URL url = new URL(Config.getValue("charge.url"));
			
			//System.out.println(Config.getValue("charge.url"));
//			URL url = new URL("https://buy.itunes.apple.com/verifyReceipt");//正式版本的地址
//			URL url = new URL("https://sandbox.itunes.apple.com/verifyReceipt");//测试版本的地址
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestProperty("content-type", "text/json");
			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true); 
			connection.setAllowUserInteraction(false);
			Map<String, String> map = new HashMap<String,String>();
			map.put("receipt-data", receipt);
			
//			if(type == 1)
//			map.put("password", "52638426edff4b54adefafb97fca849c");
			
			org.json.JSONObject jsonObject = new org.json.JSONObject(map);
			//System.out.println(jsonObject.getString("receipt-data"));
			PrintStream ps = new PrintStream(connection.getOutputStream());
			ps.print(jsonObject.toString());
			ps.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String str;
			StringBuffer sb = new StringBuffer();
			while ((str = br.readLine()) != null) {
				sb.append(str);
				sb.append("/n");
			}
			br.close();
			String response = sb.toString();
			//System.out.println(response);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String setParamWithDefaultValue(JSONObject json, String keyName)
	{
		String result = "";
		if (null != json && json.containsKey(keyName))
		{
			result = json.getString(keyName);
		}
		return null != result ? result : "";
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
		//System.out.println(status);
	}
	
	public boolean saveToDb (GamePlayer player, String orderString) {
		//System.out.println("orderString===" + orderString);
		if (!orderString.isEmpty()) {
			JSONObject jsonMain = JSONObject.fromObject(orderString);
			JSONObject json1 = jsonMain.getJSONObject("receipt");
			UserOrderInfo userOrderInfo = new UserOrderInfo();
			userOrderInfo.setChannelName("apple");
			userOrderInfo.setUserId(player.getUserId());
			userOrderInfo.setOriginalPurchaseDatePst(setParamWithDefaultValue(json1,"original_purchase_date_pst"));
			userOrderInfo.setPurchaseDateMs(setParamWithDefaultValue(json1,"purchase_date_ms"));
			userOrderInfo.setOriginalTransactionId(setParamWithDefaultValue(json1,"original_transaction_id"));
			userOrderInfo.setOriginalPurchaseDateMs(setParamWithDefaultValue(json1,"original_purchase_date_ms"));
			userOrderInfo.setTransactionId(setParamWithDefaultValue(json1,"transaction_id"));
			userOrderInfo.setQuantity(setParamWithDefaultValue(json1,"quantity"));
			userOrderInfo.setBvrs(setParamWithDefaultValue(json1,"bvrs"));
			userOrderInfo.setHostedIapVersion(setParamWithDefaultValue(json1,"hosted_iap_version"));
			userOrderInfo.setProductId(setParamWithDefaultValue(json1,"product_id"));
			userOrderInfo.setPurchaseDate(setParamWithDefaultValue(json1,"purchase_date"));
			userOrderInfo.setOriginalPurchaseDate(setParamWithDefaultValue(json1,"original_purchase_date"));
			userOrderInfo.setPurchaseDatePst(setParamWithDefaultValue(json1,"purchase_date_pst"));
			userOrderInfo.setBid(setParamWithDefaultValue(json1,"bid"));
			userOrderInfo.setItemId(setParamWithDefaultValue(json1,"item_id"));
			userOrderInfo.setStatus(setParamWithDefaultValue(jsonMain,"status"));
			List<UserOrderInfo> list = OrderBusiness.getUserOrderByTransactionId(userOrderInfo.getTransactionId());
			if(list == null || list.size() <= 0) {
				boolean result = OrderBusiness.insertUserOrder(userOrderInfo);
				if(userOrderInfo.getBid().equals("com.citywar.yediandahuawang")) {
					if (result) 
					{
						if (!userOrderInfo.getStatus().isEmpty() && !userOrderInfo.getProductId().isEmpty())
						{
							int status = Integer.valueOf(userOrderInfo.getStatus());
							int shop_id = Integer.valueOf(userOrderInfo.getProductId());
							if (status == 0 && shop_id > 0) {
								player.getPropBag().addOneItem(shop_id);
								
								ShopGoodInfo shopGood = ShopMgr.getShopGoodById(shop_id);
								player.isFinishTask(TaskConditionType.ChargeMoney, shopGood.getCount(), 0);
							}
						}
			            player.getOut().sendUpdateBaseInfo();
			            player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
						return true;
					} else {
						logger.error(player.getUserId() + "===========" + orderString + "===UserBuyOrderCmd saveToDb insertUserOrder 有问题!");
					}
				} else {
					logger.error(player.getUserId() + "============" + orderString + "===UserBuyOrderCmd saveToDb 有问题!BID不是com.citywar.yediandahuawang!");
				}
			} else {
				logger.error(player.getUserId() + "==========" + orderString + "===UserBuyOrderCmd saveToDb 有问题!已经存在transaction_id相同的数据");
			}
		}
		logger.debug("orderString123===" + orderString);
		return false;
		
	}
}
