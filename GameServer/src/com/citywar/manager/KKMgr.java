package com.citywar.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.StagePlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.hall.BaseHall;
import com.citywar.util.Config;
import com.citywar.util.ThreadSafeRandom;

/**
 * 私信管理类
 * 
 * @author zhiyun.peng
 * 
 */
public class KKMgr {


	
	private static final Logger logger = Logger.getLogger(KKMgr.class.getName());
    
	private static ThreadSafeRandom random = new ThreadSafeRandom();
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static Map<String, String> GetUserInfo(String channelId, String userId, String key)
	{
		
		Map<String, String> tempMap = null;		
		
		String thirdName = channelId.equals("") ? Config.getValue("thirdName") : channelId;		
		if(thirdName.equals("h5kk"))
		{
			tempMap = GetKKUserInfo(userId, key);
		}
		else if(thirdName.equals("yidongtv"))
		{
			tempMap = GetNoneUserInfo(userId, key);
		}
		else if(thirdName.equals("cutv"))
		{
			tempMap = GetCUTVUserInfo(userId, key);
		}		 
		else if(thirdName.equals("weixin"))
		{
			tempMap = GetWeixinUserInfo(userId, key);
		}
		else
		{
			tempMap = GetNoneUserInfo(userId, key);	
		}
		
		return tempMap;
	}
	
	public static Map<String, String> PayMoney (GamePlayer player,int BuyItem, float PayMoney) {
		
		Map<String, String> tempMap = null;
		
		
		String thirdName = Config.getValue("thirdName");		
		if(thirdName.equals("cutv"))
		{
			tempMap = CUTVPay(player, BuyItem, PayMoney);
		}
		else if(thirdName.equals("yidongtv"))
		{
			tempMap = GetYITENGPay(player, BuyItem, PayMoney);
		}
		else
		{
			tempMap = KKPay(player, BuyItem, PayMoney);
		}
		
		return tempMap;
		
	}
	
	public static boolean GetFriendList (GamePlayer player) {
		
		boolean isSuccess = false;
		
		
		String thirdName = Config.getValue("thirdName");		
		if(thirdName.equals("cutv"))
		{
			
		}
		else
		{
			isSuccess = GetKKFriendList(player);
		}
		
		return isSuccess;
		
	}
	
	public static boolean AddFriend (GamePlayer player, int addUserId) {
		
		boolean isSuccess = false;
		
		
		String thirdName = Config.getValue("thirdName");		
		if(thirdName.equals("cutv"))
		{
			
		}
		else
		{
			isSuccess = AddKKFriend(player, addUserId);
		}
		
		return isSuccess;
		
	}
	
	public static boolean OpenUserChat (GamePlayer player, int toUserId, String toUserName) {

		boolean isSuccess = false;
		
		
		String thirdName = Config.getValue("thirdName");		
		if(thirdName.equals("cutv"))
		{
			
		}
		else
		{
			isSuccess = OpenUserKKChat(player, toUserId, toUserName);
		}
		
		return isSuccess;
	}
	
	
	public static boolean AddGameActive (String strKKActiveId, String strTitle, String strDesc, List<StagePlayerInfo> userList) {
		
		
		boolean isSuccess = false;
		
		String thirdName = Config.getValue("thirdName");		
		if(thirdName.equals("cutv"))
		{
			
		}
		else
		{
			isSuccess = AddKKGameActive(strKKActiveId, strTitle, strDesc, userList);
		}
		
		return isSuccess;
		
	}

	public static void AddGameTopInfo (int type, GamePlayer masterPlayer, HallTypeInfo hallType, int para1, String para2) {
		

		
		String thirdName = Config.getValue("thirdName");		
		if(thirdName.equals("cutv"))
		{

		}
		else
		{
			AddKKGameTopInfo(type, masterPlayer, hallType, para1, para2);
		}
		
		return ;
		
	}
	
	public static void addRobotInfo () {
	
		
		String thirdName = Config.getValue("thirdName");		
		if(thirdName.equals("cutv"))
		{
			
		}
		else
		{
			addKKRobotInfo();
		}
		
		return ;
	
	}




	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	//登录
	public static Map<String, String> GetKKUserInfo (String userId, String key) {
		
		Map<String, String> tempMap = new HashMap<String, String>();      
		
		try {
//	          microAjax("/KKUser/UserInfo", function (res) {
//	              alert(res);
//	          }, "UserId=29&UserKey=F5BB0C8DE1");
			
			
			Map<String, String> map = new HashMap<String,String>();
			map.put("GameId", Config.getValue("H5kkGameId"));
			map.put("GameToken", Config.getValue("H5kkGameKey"));
			map.put("UserId", userId);
			map.put("UserToken", key);
						
			org.json.JSONObject jsonObject = new org.json.JSONObject(map);
			
			

        		String uidCheckUrl = "http://api.h5kk.com/KKInterFace/UserInfo";	        		
        		URL url = new URL(uidCheckUrl);
    			

        		URLConnection connection = (URLConnection) url.openConnection();
        		connection.setRequestProperty("accept", "*/*");
//    			connection.setRequestProperty("content-type", "text/json");
    			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
    			connection.setDoOutput(true);
    			connection.setDoInput(true); 
    			connection.setAllowUserInteraction(false);

    			
    			PrintStream ps = new PrintStream(connection.getOutputStream(), true, "UTF-8");
    			ps.print(getUTF8String(jsonObject.toString()));
    			ps.close();
    			BufferedReader br = new BufferedReader(new InputStreamReader(
    					connection.getInputStream(), "UTF-8"));
    			String str;
    			StringBuffer sb = new StringBuffer();
    			while ((str = br.readLine()) != null) {
    				sb.append(str);
    			}
    			br.close();
    			String response = sb.toString();
    			System.out.println(response);
    			
    			

    			org.json.JSONObject jsonReturn = new org.json.JSONObject(response);
    			String userKey = jsonReturn.getString("IsSuccess");    			
    			if(userKey == "true")
    			{
    				String strData = jsonReturn.getString("ObjData");
    				strData = strData.substring(1, strData.length() - 1);
    				org.json.JSONObject jsonPlayer = new org.json.JSONObject(strData);
//    				UserId":29,
//    	            UserKey":"F5BB0C8DE1",
//    	            NickName":"15019225062",
//    	            Email":"",
//    	            Sex":0,
//    	            Province":"",
//    	            City":"",
//    	            Sign":"这家伙很懒 什么都没留下",
//    	            PicPath":"/Content/playerhead.png",
//    	            Age":1,
//    	            Mobile":"",
//    	            VipLevel":0,
//    	            Money":0,
//    	            Address":""
    					
    	            tempMap.put("UserId", jsonPlayer.getString("UserId"));
    		        tempMap.put("UserKey", jsonPlayer.getString("UserKey"));
    		        tempMap.put("NickName", jsonPlayer.getString("NickName"));
    		        tempMap.put("Sex", jsonPlayer.getString("Sex"));
    		        tempMap.put("Province", jsonPlayer.getString("Province"));
    		        tempMap.put("City", jsonPlayer.getString("City"));
    		        tempMap.put("Sign", jsonPlayer.getString("Sign"));
    		        tempMap.put("PicPath", jsonPlayer.getString("PicPath"));
    		        tempMap.put("Age", jsonPlayer.getString("Age"));
    		        tempMap.put("Loc", jsonPlayer.getString("Loc"));

    				
    			}
    			
    			

	        
		 } catch(Exception ex) {
         	logger.error("HTTP服务器错误:" + ex.getLocalizedMessage());
         }
		finally
        {
        }

		return tempMap;
	}

	//购买	
	public static Map<String, String> KKPay (GamePlayer player,int BuyItem, float PayMoney) {
			
			Map<String, String> tempMap = new HashMap<String, String>();
			
			try {

	            String GameId = Config.getValue("H5kkGameId");
	            String GameKey = Config.getValue("H5kkGameKey");
				
	            
		        	String receipt = "UserId="+ player.getUserId() +"&UserKey="+ player.getUserKey() + "&GameId="+ GameId +"&GameKey="+ GameKey + "&BuyItem=" + BuyItem + "&PayMoney=" + PayMoney;
	        		String uidCheckUrl = "http://www.h5kk.com/KKPay/UserPay";
	        		URL url = new URL(uidCheckUrl);
	    			

	        		URLConnection connection = (URLConnection) url.openConnection();
	        		connection.setRequestProperty("accept", "*/*");
	    			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
	    			connection.setDoOutput(true);
	    			connection.setDoInput(true); 
	    			connection.setAllowUserInteraction(false);

	    			
	    			PrintStream ps = new PrintStream(connection.getOutputStream(), true, "UTF-8");
	    			ps.print(getUTF8String(receipt));
	    			ps.close();
	    			BufferedReader br = new BufferedReader(new InputStreamReader(
	    					connection.getInputStream(), "UTF-8"));
	    			String str;
	    			StringBuffer sb = new StringBuffer();
	    			while ((str = br.readLine()) != null) {
	    				sb.append(str);
	    			}
	    			br.close();
	    			String response = sb.toString();

	    			
	    			

	    			org.json.JSONObject jsonReturn = new org.json.JSONObject(response);
	    			String userKey = jsonReturn.getString("IsSuccess");    			
	    			if(userKey == "true")
	    			{
	    				String strData = jsonReturn.getString("ObjData");
	    				strData = strData.substring(1, strData.length() - 1);
	    				org.json.JSONObject jsonPlayer = new org.json.JSONObject(strData);
	    				
	    				tempMap.put("OrderId", jsonPlayer.getString("OrderId"));
	    				tempMap.put("State", jsonPlayer.getString("State"));
	    	            tempMap.put("BuyItem", jsonPlayer.getString("BuyItem"));
	    		        tempMap.put("PayMoney", jsonPlayer.getString("PayMoney"));
	    				
	    			}
	    			   			

			 } catch(Exception ex) {
	         	logger.error("H5BuyOrder错误:" + ex.getLocalizedMessage());
	         }
			finally
	        {
	        }

			return tempMap;
		}
		
	//查找用户好友
	public static boolean GetKKFriendList (GamePlayer player) {
		

		boolean isSuccess = false;
		
		try {

			
			Map<String, String> map = new HashMap<String,String>();
			map.put("GameId", Config.getValue("H5kkGameId"));
			map.put("GameKey", Config.getValue("H5kkGameKey"));
			map.put("UserId", String.valueOf(player.getUserId()));
			map.put("UserKey", player.getUserKey());

						
			org.json.JSONObject jsonObject = new org.json.JSONObject(map);
			
			
//            String GameId = Config.getValue("H5kkGameId");
//            String GameKey = Config.getValue("H5kkGameKey");	
//	        	String receipt = "GameId="+ GameId +"&GameKey="+ GameKey + "&UserId="+ player.getUserId() +"&UserKey="+ player.getUserKey() + "&GameId="+ GameId +"&GameKey="+ GameKey;
	        	
        		String uidCheckUrl = "http://api.h5kk.com/KKInterFace/PushUserGameInfo";
        		URL url = new URL(uidCheckUrl);
    			

        		URLConnection connection = (URLConnection) url.openConnection();
        		connection.setRequestProperty("accept", "*/*");
    			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
    			connection.setDoOutput(true);
    			connection.setDoInput(true); 
    			connection.setAllowUserInteraction(false);

    			
    			PrintStream ps = new PrintStream(connection.getOutputStream(), true, "UTF-8");
    			ps.print(getUTF8String(jsonObject.toString()));
    			ps.close();
    			BufferedReader br = new BufferedReader(new InputStreamReader(
    					connection.getInputStream(), "UTF-8"));
    			String str;
    			StringBuffer sb = new StringBuffer();
    			while ((str = br.readLine()) != null) {
    				sb.append(str);
    			}
    			br.close();
    			String response = sb.toString();

    			
    			

    			org.json.JSONObject jsonReturn = new org.json.JSONObject(response);
    			String userKey = jsonReturn.getString("IsSuccess");
    			if(userKey == "true")
    			{
    				isSuccess = true;
    			}


		 } catch(Exception ex) {
         	logger.error("H5BuyOrder错误:" + ex.getLocalizedMessage());
         }
		finally
        {
			
        }
		

		return isSuccess;
	}
		
	//添加好友
	public static boolean AddKKFriend(GamePlayer player, int addUserId) {
		

		boolean isSuccess = false;
		
		try {

			
			
			
//            String GameId = Config.getValue("H5kkGameId");
//            String GameKey = Config.getValue("H5kkGameKey");
//			            
//            
//	        	String receipt = "GameId="+ GameId +"&GameKey="+ GameKey + "&UserId="+ player.getUserId() +"&UserKey="+ player.getUserKey() + "&addUserId="+ addUserId;
			
			
			Map<String, String> map = new HashMap<String,String>();
			map.put("GameId", Config.getValue("H5kkGameId"));
			map.put("GameKey", Config.getValue("H5kkGameKey"));
			map.put("UserId", String.valueOf(player.getUserId()));
			map.put("UserKey", player.getUserKey());
			map.put("addUserId",String.valueOf(addUserId));
						
			org.json.JSONObject jsonObject = new org.json.JSONObject(map);
			
			
			
			
        		String uidCheckUrl = "http://api.h5kk.com/KKInterFace/FriendAdd";
        		URL url = new URL(uidCheckUrl);
    			

        		URLConnection connection = (URLConnection) url.openConnection();
        		connection.setRequestProperty("accept", "*/*");
    			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
    			connection.setDoOutput(true);
    			connection.setDoInput(true); 
    			connection.setAllowUserInteraction(false);

    			
    			PrintStream ps = new PrintStream(connection.getOutputStream(), true, "UTF-8");
    			ps.print(getUTF8String(jsonObject.toString()));
    			ps.close();
    			BufferedReader br = new BufferedReader(new InputStreamReader(
    					connection.getInputStream(), "UTF-8"));
    			String str;
    			StringBuffer sb = new StringBuffer();
    			while ((str = br.readLine()) != null) {
    				sb.append(str);
    			}
    			br.close();
    			String response = sb.toString();

    			
    			

    			org.json.JSONObject jsonReturn = new org.json.JSONObject(response);
    			String userKey = jsonReturn.getString("IsSuccess");    			
    			if(userKey == "true")
    			{
    				isSuccess = true;    				
    			}


		 } catch(Exception ex) {
         	logger.error("H5BuyOrder错误:" + ex.getLocalizedMessage());
         }
		finally
        {
			
        }

		return isSuccess;
	}

	//添加好友
	public static boolean OpenUserKKChat(GamePlayer player, int toUserId, String toUserName) {
			

		boolean isSuccess = false;
		
		try {

			
			
			
//	            String GameId = Config.getValue("H5kkGameId");
//	            String GameKey = Config.getValue("H5kkGameKey");
//				            
//	            
//		        	String receipt = "GameId="+ GameId +"&GameKey="+ GameKey + "&UserId="+ player.getUserId() +"&UserKey="+ player.getUserKey() + "&addUserId="+ addUserId;
			
			
			Map<String, String> map = new HashMap<String,String>();
			map.put("GameId", Config.getValue("H5kkGameId"));
			map.put("GameKey", Config.getValue("H5kkGameKey"));
			map.put("UserId", String.valueOf(player.getUserId()));
			map.put("ToUserId", String.valueOf(toUserId));
			map.put("ToUserName",toUserName);
						
			org.json.JSONObject jsonObject = new org.json.JSONObject(map);
			
			
			
			
        		String uidCheckUrl = "http://api.h5kk.com/KKInterFace/UserOpenChat";
        		URL url = new URL(uidCheckUrl);
    			

        		URLConnection connection = (URLConnection) url.openConnection();
        		connection.setRequestProperty("accept", "*/*");
    			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
    			connection.setDoOutput(true);
    			connection.setDoInput(true); 
    			connection.setAllowUserInteraction(false);

    			
    			PrintStream ps = new PrintStream(connection.getOutputStream(), true, "UTF-8");
    			ps.print(getUTF8String(jsonObject.toString()));
    			ps.close();
    			BufferedReader br = new BufferedReader(new InputStreamReader(
    					connection.getInputStream(), "UTF-8"));
    			String str;
    			StringBuffer sb = new StringBuffer();
    			while ((str = br.readLine()) != null) {
    				sb.append(str);
    			}
    			br.close();
    			String response = sb.toString();

    			
    			
    			

    			org.json.JSONObject jsonReturn = new org.json.JSONObject(response);
    			String userKey = jsonReturn.getString("IsSuccess");    			
    			if(userKey == "true")
    			{
    				isSuccess = true;    				
    			}


		 } catch(Exception ex) {
         	logger.error("H5BuyOrder错误:" + ex.getLocalizedMessage());
         }
		finally
        {
			
        }

		return isSuccess;
	}

		
	//活动信息推送
	public static boolean AddKKGameActive(String strKKActiveId, String strTitle, String strDesc, List<StagePlayerInfo> userList) {
		

		boolean isSuccess = false;
		
		try {


		    
			    JSONObject json=new JSONObject();  
			    JSONArray jsonMembers = new JSONArray();  
				for(StagePlayerInfo stagePlayer : userList)
				{
					
					 JSONObject member1 = new JSONObject();  
					 member1.put("UserId", String.valueOf(stagePlayer.getPlayerId()));
					 member1.put("Index", String.valueOf(stagePlayer.getIndex()));					 
					 member1.put("Score", String.valueOf(stagePlayer.getScore()));
					 member1.put("PrizeId", String.valueOf(stagePlayer.getKKPrizeId()));
					 
					 jsonMembers.put(member1); 				    
				}
							
			
				
				json.put("GameId", Config.getValue("H5kkGameId"));
				json.put("GameKey", Config.getValue("H5kkGameKey"));
				                      
				json.put("ContentActiveToken", strKKActiveId);
				json.put("Title", strTitle);
				json.put("Descript", strDesc);
				json.put("UserList", jsonMembers);

				

			
        		String uidCheckUrl = "http://api.h5kk.com/KKInterFace/AddGameActiveData";
        		URL url = new URL(uidCheckUrl);
    			

        		URLConnection connection = (URLConnection) url.openConnection();
        		connection.setRequestProperty("accept", "*/*");
    			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
    			connection.setDoOutput(true);
    			connection.setDoInput(true); 
    			connection.setAllowUserInteraction(false);

    			
    			PrintStream ps = new PrintStream(connection.getOutputStream(), true, "UTF-8");
    			ps.print(getUTF8String(json.toString()));
    			ps.close();
    			BufferedReader br = new BufferedReader(new InputStreamReader(
    					connection.getInputStream(), "UTF-8"));
    			String str;
    			StringBuffer sb = new StringBuffer();
    			while ((str = br.readLine()) != null) {
    				sb.append(str);
    			}
    			br.close();
    			String response = sb.toString();

    			
    			

    			org.json.JSONObject jsonReturn = new org.json.JSONObject(response);
    			String userKey = jsonReturn.getString("IsSuccess");    			
    			if(userKey == "true")
    			{
    				isSuccess = true;    				
    			}


		 } catch(Exception ex) {
         	logger.error("H5BuyOrder错误:" + ex.getLocalizedMessage());
         }
		finally
        {
			
        }

		return isSuccess;
	}
			
	//推送用户游戏动态协议
	private static boolean addKKInfo (GamePlayer player,String strPushInfo, String strPara) {


				boolean isSuccess = false;
				
				try {

					
					Map<String, String> map = new HashMap<String,String>();
					map.put("GameId", Config.getValue("H5kkGameId"));
					map.put("GameKey", Config.getValue("H5kkGameKey"));
					map.put("UserId", String.valueOf(player.getUserId()));
					map.put("UserKey", player.getUserKey());
					map.put("PushInfo", strPushInfo);
					map.put("Para", strPara);
								
					org.json.JSONObject jsonObject = new org.json.JSONObject(map);
					
							
		            
		            
			        	//String receipt = "UserId="+ player.getUserId() +"&UserKey="+ player.getUserKey() + "&GameId="+ GameId +"&GameKey="+ GameKey + "&PushInfo=" + strPushInfo+ "&Para=" + strPara;
		        		String uidCheckUrl = "http://api.h5kk.com/KKInterFace/PushUserGameInfo";
		        		URL url = new URL(uidCheckUrl);
		    			

		        		URLConnection connection = (URLConnection) url.openConnection();
		        		connection.setRequestProperty("accept", "*/*");
		    			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
		    			connection.setDoOutput(true);
		    			connection.setDoInput(true); 
		    			connection.setAllowUserInteraction(false);

		    			
		    			PrintStream ps = new PrintStream(connection.getOutputStream(), true, "UTF-8");
		    			ps.print(getUTF8String(jsonObject.toString()));
		    			ps.close();
		    			BufferedReader br = new BufferedReader(new InputStreamReader(
		    					connection.getInputStream(), "UTF-8"));
		    			String str;
		    			StringBuffer sb = new StringBuffer();
		    			while ((str = br.readLine()) != null) {
		    				sb.append(str);
		    			}
		    			br.close();
		    			String response = sb.toString();

		    			
		    			

		    			org.json.JSONObject jsonReturn = new org.json.JSONObject(response);
		    			String userKey = jsonReturn.getString("IsSuccess");    			
		    			if(userKey == "true")
		    			{
		    				isSuccess = true;    				
		    			}


				 } catch(Exception ex) {
		         	logger.error("H5BuyOrder错误:" + ex.getLocalizedMessage());
		         }
				finally
		        {
					
		        }

				return isSuccess;
			}
	
	
	
	
	
	
	
	//自动推送用户游戏状态
	//推送用户名次状态
	public static void AddKKGameTopInfo(int type, GamePlayer masterPlayer, HallTypeInfo hallType, int para1, String para2) {
			
			
		try {
			
			switch(type)
	    	{
	    		case 1:

	    			
	    			String str = null;
	    	    	if(hallType.getGameType() == 1)
	    	    		str = String.format("%s 在KK同城大话骰中的 ％s 场打出了第 %d 名的好成绩", masterPlayer.getPlayerInfo().getUserName(), hallType.getHallTypeName(), para1);                  	
	    	    	else if(hallType.getGameType() == 2)
	    	    		str = String.format("%s 在KK同城斗地主中被 ％s 场打出了第 %d 名的好成绩", masterPlayer.getPlayerInfo().getUserName(), hallType.getHallTypeName(), para1);             
	    	    	else if(hallType.getGameType() == 3)
	    	    		str = String.format("%s 在KK同城大斗牛中 ％s 场打出了第  名的好成绩", masterPlayer.getPlayerInfo().getUserName(), hallType.getHallTypeName());         
	    	    	
	    	    	String strSend = URLEncoder.encode(str, "UTF-8");
	    	        KKMgr.addKKInfo(masterPlayer, strSend, "1");
	    			break;

	    		default:
	    			break;
	    	}
			
		}
		 catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
			
	public static void addKKRobotInfo()
	{
				
		int rnd = random.next(0,100);
		if(rnd >= 0)//if(rnd > 80)
			return;
		
		
		
		List<BaseHall> HallList = HallMgr.getHallTypesList();		
		if(HallList.size() == 0)
			return;
		
		
		int gameType = HallList.get(0).getHallType().getGameType();				
		switch(gameType)
    	{
    		case 1:

    			Player robot11 = RobotMgr.getRobot(1);//TODO 多大厅
    			Player robot12 = RobotMgr.getRobot(2);//TODO 多大厅
    			AddDiceKKInfo(1, robot11.getPlayerDetail(), robot12.getPlayerDetail(), 0, "");
    			break;
    		case 2:

    			Player robot2 = RobotMgr.getRobot(1);//TODO 多大厅
    			AddDizhuKKInfo(1, robot2.getPlayerDetail(), 1, "");
    			break;
    		case 3:

    			Player robot3 = RobotMgr.getRobot(1);//TODO 多大厅
    			AddNiuKKInfo(1, robot3.getPlayerDetail(), 1, "");
    			break;
	
    		case 4:

    			Player robot4 = RobotMgr.getRobot(1);//TODO 多大厅
    			AddTexasKKInfo(1, robot4.getPlayerDetail(), 1, "");
    			break;
    		default:
    			break;
    	}
							
	}
		
	private static void AddDiceKKInfo(int type, GamePlayer successPlayer, GamePlayer failPlayer, int para1, String para2) {
		
		
		try {
			
			switch(type)
	    	{
	    		case 1:

	    			String str = String.format("%s在KK同城大话骰中被灌醉，成为了%s的私人奴隶，痛不欲生", failPlayer.getPlayerInfo().getUserName(), successPlayer.getPlayerInfo().getUserName());  
	    			String strSend = URLEncoder.encode(str, "UTF-8");
	    			addKKInfo(failPlayer, strSend, "1");
	    			break;

	    		default:
	    			break;
	    	}
			
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	private static void AddDizhuKKInfo(int type, GamePlayer masterPlayer, int para1, String para2) {
		
		
		try {
			
			switch(type)
	    	{
	    		case 1:

	    			String str = null;
	    	    	if(para1 > 0)
	    	    		str = String.format("%s在KK同城斗地主中打出了满贯，成为人生赢家", masterPlayer.getPlayerInfo().getUserName());                  	
	    	    	else                	
	    	    		str = String.format("因为炸弹太多，%s在KK同城斗地主中被炸哭了", masterPlayer.getPlayerInfo().getUserName());                  	

	    	    	String strSend = URLEncoder.encode(str, "UTF-8");
	    	        addKKInfo(masterPlayer, strSend, "1");
	    			break;

	    		default:
	    			break;
	    	}
			
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
	}
		
	private static void AddNiuKKInfo(int type, GamePlayer masterPlayer, int para1, String para2) {
		
		
		try {
			
			
			switch(type)
	    	{
	    		case 1:
	
	    			String str = null;
	    	    	if(para1 > 0)                	
	    	    		str = String.format("%s在KK同城大斗牛中打出了超级牛牛，大杀四方", masterPlayer.getPlayerInfo().getUserName());                  	
	    	    	else                	
	    	    		str = String.format("%s在KK同城大斗牛中被打成了猪头，惨不忍睹", masterPlayer.getPlayerInfo().getUserName());                  	
		    	    	
				
					String strSend = URLEncoder.encode(str, "UTF-8");
				
	    	        KKMgr.addKKInfo(masterPlayer, strSend, "1");
	    			break;
	
	    		default:
	    			break;
	    	}
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void AddTexasKKInfo(int type, GamePlayer masterPlayer, int para1, String para2) {
		
		
		try {
			
			
			switch(type)
	    	{
	    		case 1:
	
	    			String str = null;
	    	    	if(para1 > 0)                	
	    	    		str = String.format("%s在KK同城德州扑克中打出了ALL IN成功，威振全场", masterPlayer.getPlayerInfo().getUserName());                  	
	    	    	else                	
	    	    		str = String.format("%s在KK同城德州扑克中被打到破产，同情一下", masterPlayer.getPlayerInfo().getUserName());                  	
		    	    	
				
					String strSend = URLEncoder.encode(str, "UTF-8");
				
	    	        KKMgr.addKKInfo(masterPlayer, strSend, "1");
	    			break;
	
	    		default:
	    			break;
	    	}
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
		
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static Map<String, String> GetCUTVUserInfo (String userId, String key) {
		
		Map<String, String> tempMap = new HashMap<String, String>();      
		
		try {

			String jsonObject = "{\"method\":\"getMemberInfo\",\"params\":[\"" + userId + "\",\"6wRFYtJvVitsyCRvWdu5EXO1Fs6kVKNraQuPGH\\/ixbc=\",\""+ key +"\"],\"id\":1}";
						
			
			String uidCheckUrl = "http://yao.cutv.com/peopleservice/game/server.php";	        		
        		URL url = new URL(uidCheckUrl);
    			

        		URLConnection connection = (URLConnection) url.openConnection();
        		connection.setRequestProperty("accept", "*/*");
    			connection.setRequestProperty("Content-type", "application/json");
    			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
    			connection.setDoOutput(true);
    			connection.setDoInput(true); 
    			connection.setAllowUserInteraction(false);

    			
    			PrintStream ps = new PrintStream(connection.getOutputStream());
    			ps.print(jsonObject);
    			ps.close();
    			BufferedReader br = new BufferedReader(new InputStreamReader(
    					connection.getInputStream(), "UTF-8"));
    			String str;
    			StringBuffer sb = new StringBuffer();
    			while ((str = br.readLine()) != null) {
    				sb.append(str);
    			}
    			br.close();
    			String response = sb.toString();
    			System.out.println(response);
    			
    		
    			org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
    			String strResult = jsonResponse.getString("result");    
    			org.json.JSONObject jsonResult = new org.json.JSONObject(strResult);
    			String strStatus = jsonResult.getString("status");    
    			if(strStatus.equals("1"))
    			{
//    				{"id":1,"result":{"status":1,"message":"\u83b7\u53d6\u7528\u6237\u4fe1\u606f\u6210\u529f","userInfo":{"score":3279,"uid":42,"mobile":13456789876,"avatar":"http:\/\/yao.cutv.com\/uc_server\/avatar.php?uid=42&size=middle"}},"error":null}
    				String strUserInfo = jsonResult.getString("userInfo");
    				org.json.JSONObject jsonPlayer = new org.json.JSONObject(strUserInfo);

    					
    	            tempMap.put("UserId", jsonPlayer.getString("uid"));
    	            tempMap.put("Account", jsonPlayer.getString("mobile"));
    	            tempMap.put("NickName", jsonPlayer.getString("nickname"));
    	            tempMap.put("PicPath", jsonPlayer.getString("avatar"));
    	            tempMap.put("Fortune", jsonPlayer.getString("score"));   
    	                				
    			}
    			
		 } catch(Exception ex) {
         	logger.error("HTTP服务器错误:" + ex.getLocalizedMessage());
         }
		finally
        {
        }

		return tempMap;
	}
				
	private static Map<String, String> CUTVPay (GamePlayer player,int BuyItem, float PayMoney) {
		
		Map<String, String> tempMap = new HashMap<String, String>();      
		
		try {
			
				int exchangerate = Integer.valueOf(Config.getValue("thirdexchangerate"));
			 	int pay = (int) (PayMoney * -1) * exchangerate;
			
			
			 	String jsonObject = "{\"method\":\"updateScore\",\"params\":[\"" + player.getPlayerInfo().getAccount() + "\","+ String.valueOf(pay) +",\"6wRFYtJvVitsyCRvWdu5EXO1Fs6kVKNraQuPGH\\/ixbc=\",\""+ player.getUserKey() +"\"],\"id\":1}";	
				String uidCheckUrl = "http://yao.cutv.com/peopleservice/game/server.php";	        		
        		URL url = new URL(uidCheckUrl);
    			

        		URLConnection connection = (URLConnection) url.openConnection();
        		connection.setRequestProperty("accept", "*/*");
    			connection.setRequestProperty("Content-type", "application/json");
    			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
    			connection.setDoOutput(true);
    			connection.setDoInput(true); 
    			connection.setAllowUserInteraction(false);

    			
    			PrintStream ps = new PrintStream(connection.getOutputStream());
    			ps.print(jsonObject);
    			ps.close();
    			BufferedReader br = new BufferedReader(new InputStreamReader(
    					connection.getInputStream(), "UTF-8"));
    			String str;
    			StringBuffer sb = new StringBuffer();
    			while ((str = br.readLine()) != null) {
    				sb.append(str);
    			}
    			br.close();
    			String response = sb.toString();
    			System.out.println(response);
    			
    			

    			org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
    			String strResult = jsonResponse.getString("result");    
    			org.json.JSONObject jsonResult = new org.json.JSONObject(strResult);
    			String strStatus = jsonResult.getString("status");    			
    			if(strStatus.equals("1"))
    			{

//    				{"id":1,"result":{"status":1,"message":"\u79ef\u5206\u4fee\u6539\u6210\u529f001","score":3269},"error":null}    				    				 
    				UUID uuid = UUID.randomUUID();
    				 
    	            tempMap.put("State", jsonResult.getString("status"));
    		        tempMap.put("Msg", jsonResult.getString("message"));
    		        tempMap.put("Fortune", jsonResult.getString("score"));
    		            		            		        
    		        tempMap.put("OrderId", uuid.toString());
    	            tempMap.put("BuyItem", String.valueOf(BuyItem));
    		        tempMap.put("PayMoney", String.valueOf(PayMoney));
    		            				
    			}
    				        
		 } catch(Exception ex) {
         	logger.error("HTTP服务器错误:" + ex.getLocalizedMessage());
         }
		finally
        {
        }

		return tempMap;
	}
	
	
	
	public static String getUTF8String(String str) {  
	    // A StringBuffer Object  
	    StringBuffer sb = new StringBuffer();  
	    sb.append(str);  
	    String xmString = "";  
	    String xmlUTF8="";  
	    try {  
	    xmString = new String(sb.toString().getBytes("UTF-8"));  
	    xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");  
	    System.out.println("utf-8-----------------------" + xmlUTF8) ;  
	    } catch (UnsupportedEncodingException e) {  
	    // TODO Auto-generated catch block  
	    e.printStackTrace();  
	    }  
	    // return to String Formed  
	    return xmlUTF8;  
	    }  
	

		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static Map<String, String> GetNoneUserInfo (String userId, String key) {
		
		Map<String, String> tempMap = new HashMap<String, String>();      
		
		try {
		
			PlayerInfo info = PlayerBussiness.getPlayerInfoByAccount(userId);// getPlayerInfoByAccount(account);
			
			if(info == null)
			{
				Byte sex = (byte)(random.next(0,100) % 2 == 1 ? 0 : 1);
				
				tempMap.put("UserId", String.valueOf(PlayerBussiness.getNewUserId()));
				tempMap.put("Account", userId);
				tempMap.put("Sex",  String.valueOf(sex));
				tempMap.put("PicPath", sex == 0 ? "res/animation/head00.png" : "res/animation/head04.png");
			}
			else
			{
				tempMap.put("UserId", String.valueOf(info.getUserId()));				
			}
			
            
		 } catch(Exception ex) {
         	logger.error("HTTP服务器错误:" + ex.getLocalizedMessage());
         }
		finally
        {
        }

		return tempMap;
	}

	

	private static Map<String, String> GetYITENGPay (GamePlayer player,int BuyItem, float PayMoney) {
		
		Map<String, String> tempMap = new HashMap<String, String>();      
		
		try {

//    		{"id":1,"result":{"status":1,"message":"\u79ef\u5206\u4fee\u6539\u6210\u529f001","score":3269},"error":null}
			UUID uuid = UUID.randomUUID();
    				     	            
			tempMap.put("State", "0");    		        
			tempMap.put("Msg", "");    		        
			tempMap.put("Fortune", "");   		            		        
    		              
			tempMap.put("OrderId", uuid.toString());    	            
			tempMap.put("BuyItem", String.valueOf(BuyItem));    		        
			tempMap.put("PayMoney", String.valueOf(PayMoney));

	        
		 } catch(Exception ex) {
         	logger.error("HTTP服务器错误:" + ex.getLocalizedMessage());
         }
		finally
        {
        }

		return tempMap;
	}
	
	

	
	public static String GetIPCity (String strIP) {
		
		String result = "";   
		
		try {
			 
				String urlNameString = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + strIP;	        		
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
    			
    				            
	            org.json.JSONObject jsonResponse = new org.json.JSONObject(result);
	            result = jsonResponse.getString("province") + " - " + jsonResponse.getString("city");    

	        
		 } catch(Exception ex) {
         	logger.error("HTTP服务器错误:" + ex.getLocalizedMessage());
         }
		finally
        {
        }

		return result;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	

	private static Map<String, String> GetWeixinUserInfo (String userId, String key) {
		
		Map<String, String> tempMap = new HashMap<String, String>();      
		
		try {
		
			String APP_ID = Config.getValue("weixin.key");		
			String APP_SECRET = Config.getValue("weixin.secret");		
			
			String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                    + APP_ID //这里替换成你的appid
                    + "&secret="
                    + APP_SECRET //这里替换成你的appsecret
                    + "&code="
                    + userId
                    + "&grant_type=authorization_code";
			
                JSONObject jsonObject = new org.json.JSONObject(getHttpsRequest(path));// 请求https连接并得到json结果
                if (null != jsonObject) {
                    String openid = jsonObject.getString("openid").toString().trim();
                    String access_token = jsonObject.getString("access_token").toString().trim();

                    
                    String path2 = "https://api.weixin.qq.com/sns/userinfo?access_token="
                            + access_token + "&openid=" + openid;

                    JSONObject jsonObject2 = new org.json.JSONObject(getHttpsRequest(path2));  
                    //{"sex":1,"nickname":"乐城互动","unionid":"onr24tyQl3pUsrHw2KCc8SCCJe_U","privilege":[],"province":"Guangdong","openid":"owSgTxHdWZzT9-E_Zumf7D5yT24w","language":"zh_CN","headimgurl":"http://wx.qlogo.cn/mmopen/wkxv2O1d5w7MCE3UkM7iat7iciaLY9dKgF52w0RF9RNRAcZoEiaRu6Q8bKk3ZRQN1V1lB8dgFZAgic59RaWjXhQaSiaultE1bR4Wn0/0","country":"CN","city":"Shenzhen"}
                    tempMap.put("UserId", String.valueOf(PlayerBussiness.getNewUserId()));
                    tempMap.put("Account", jsonObject2.getString("unionid"));
                    tempMap.put("Password", jsonObject2.getString("unionid"));
                    tempMap.put("NickName", jsonObject2.getString("nickname"));
                    tempMap.put("PicPath", jsonObject2.getString("headimgurl"));
                    tempMap.put("Sex", jsonObject2.getString("sex"));
                                            
                }
            
			
            
		 } catch(Exception ex) {
         	logger.error("HTTP服务器错误:" + ex.getLocalizedMessage());
         }
		finally
        {
        }

		return tempMap;
	}
	
	
	
	
	
	
	

	public static String getHttpsRequest(String urlPath) {
		
		try {
			
			
			URL url = new URL(urlPath);
			
			//System.out.println(Config.getValue("charge.url"));
//			URL url = new URL("https://buy.itunes.apple.com/verifyReceipt");//正式版本的地址
//			URL url = new URL("https://sandbox.itunes.apple.com/verifyReceipt");//测试版本的地址
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//			connection.setRequestProperty("content-type", "text/json");
			connection.setRequestProperty("Proxy-Connection", "Keep-Alive");
			connection.setRequestMethod("GET");
//			connection.setDoOutput(true);
//			connection.setDoInput(true); 
//			connection.setAllowUserInteraction(false);
//			Map<String, String> map = new HashMap<String,String>();
//			map.put("receipt-data", receipt);
			
//			if(type == 1)
//			map.put("password", "52638426edff4b54adefafb97fca849c");
			
//			org.json.JSONObject jsonObject = new org.json.JSONObject(map);
			//System.out.println(jsonObject.getString("receipt-data"));
//			PrintStream ps = new PrintStream(connection.getOutputStream());
//			ps.print(jsonObject.toString());
//			ps.close();
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

}
