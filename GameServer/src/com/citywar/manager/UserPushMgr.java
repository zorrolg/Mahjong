package com.citywar.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import org.apache.log4j.Logger;

import com.citywar.bll.PushInfoBussiness;
import com.citywar.dice.entity.PushInfo;
import com.citywar.dice.entity.PushLogInfo;
import com.citywar.dice.entity.PushPlayerInfo;
import com.citywar.util.Config;
import com.citywar.util.TimeUtil;

/**
 * 世界排行榜
 * 
 * @author zhiyun.peng
 * 
 */
public class UserPushMgr {
	

	
    private static Logger logger = Logger.getLogger(UserPushMgr.class.getName());
	
    
    private static Object lockPlayer = new Object();
    private static Object lockPush = new Object();
    
	
    private static List<PushPlayerInfo> pushPlayerList = new ArrayList<PushPlayerInfo>();
//    private static List<PushInfo> pushInfoList = new ArrayList<PushInfo>();
    
    
    private static String cerPath;
    private static String cerPwd;
    private static boolean isFormal;
//    private static PushNotificationManager pushManager;

	/**
	 * 
	 * 
	 * @return
	 */
	public static boolean init()  
	{		
		
		try{
			cerPath = Config.getValue("pushserver.file");//"/Library/WebServer/Documents/ck.pem"; 
			cerPwd = Config.getValue("pushserver.pwd");//"ufun2013"; 	
			isFormal = Config.getValue("pushserver.isforaml").equals("true") ? true : false;
			

			System.out.println("Push server start======foraml:"+ isFormal);
		}
		catch (Exception e) {
            logger.error("[ UserPushMgr : init ]", e);
        } finally {
           
        }
		
		return reloadPushPlayer();
	}
	
	/**
     * 
     * 
     * @return
     */
    public static boolean reload()
    {

    	System.out.println("UserPushMgr============reload======");
        try
        {

        	synchronized (lockPush) {
        		
        		
	        	List<PushInfo> pushList = PushInfoBussiness.getAllPushInfo();
	        	List<PushInfo> pushAlready = new ArrayList<PushInfo>();
	        	
//	        	System.out.println("UserPushMgr============1");
	        	for(PushInfo push : pushList)
	        	{	       
	        		
	        		switch (push.getPushType()) {
	        		
		        		case 1://时间规则，所有人发送
		        			
		        			Timestamp time = TimeUtil.getSysteCurTime();
							if(push.getTime().compareTo(time) < 0)
							{
					            List<String> tokens=new ArrayList<String>(); 
					            
					            for(PushPlayerInfo player : pushPlayerList)		            		            	
					            	tokens.add(player.getDeviceToken()); 					                        
			           
//					            System.out.println("UserPushMgr============2");
					            sendPush(tokens, push.getMsg(), push.getCount(), push.getVoice()); 
					            pushAlready.add(push);
							}

		        			break;
		        		
		        		default://
		        			
		        			break;
	        		}        
	        	}
	        	
	        	
	        	PushInfoBussiness.delete(pushAlready);
	        	
        	}
            
        } catch (Exception e) {
            logger.error("[ UserTopMgr : reload ]", e);
        } finally {
           
        }
		return true;
    }
    

    /**
     * 
     * 
     * @return
     */
    public static boolean reloadPushPlayer()
    {

    	
        try
        {
        	List<PushPlayerInfo> tempPlayerList = PushInfoBussiness.getPushPlayerList();
        	
        	synchronized (lockPlayer) {
        		pushPlayerList = tempPlayerList;
        		
        		return true;
        	}
        	
        } catch (Exception e) {
            logger.error("[ UserTopMgr : reload ]", e);
        } finally {
           
        }
		return false;
    }
    
    
    
    
    
    /************************************************
    测试推送服务器地址：gateway.sandbox.push.apple.com /2195 
    产品推送服务器地址：gateway.push.apple.com / 2195 

   需要javaPNS_2.2.jar包

    ***************************************************/ 
   /**

    *这是一个比较简单的推送方法，

    * apple的推送方法

    * @param tokens   iphone手机获取的token

    * @param path 这里是一个.p12格式的文件路径，需要去apple官网申请一个 

    * @param password  p12的密码 此处注意导出的证书密码不能为空因为空密码会报错

    * @param message 推送消息的内容

    * @param count 应用图标上小红圈上的数值

    * @param sendCount 单发还是群发  true：单发 false：群发

    */ 

   public static void sendPush(List<String> tokens, String message,Integer count, String voice) { 

	    try { 
	    	
//	    	System.out.println("UserPushMgr============3");
	        //message是一个json的字符串{“aps”:{“alert”:”iphone推送测试”}} 
	 
	    		String messageJson="{'aps':{'alert':'" + message + "'}}"; 
	    	
	            PushNotificationPayload payLoad =  PushNotificationPayload.fromJSON(messageJson); 
	             
	            payLoad.addAlert(message); // 消息内容 	             
	            payLoad.addBadge(count); // iphone应用图标上小红圈上的数值 	             
	            payLoad.addSound(voice); // 铃音 默认 
	             
	             
	 
	            // 发送push消息 
	            List<Device> device = new ArrayList<Device>(); 		            
	            for (String token : tokens) 	      
	            	if(token.length() == 64)
	            		device.add(new BasicDevice(token)); 
	            
	            
//	            System.out.println("UserPushMgr============4");
	            PushNotificationManager pushManager = new PushNotificationManager(); 
		        //true：表示的是产品发布推送服务 false：表示的是产品测试推送服务 
		        pushManager.initializeConnection(new AppleNotificationServerBasicImpl(cerPath, cerPwd, isFormal)); 
	            List<PushedNotification> notifications = pushManager.sendNotifications(payLoad, device); 
	            
	            
	            Timestamp time = TimeUtil.getSysteCurTime();
	            List<PushLogInfo> insertUserBuilds = new ArrayList<PushLogInfo>();
	            
	            
	            
	            List<PushedNotification> failedNotifications = PushedNotification.findFailedNotifications(notifications); 	             
	            List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications); 	
	            
	            
//	            System.out.println("UserPushMgr============4");
	            for(PushedNotification pushResult:failedNotifications)
	            {
	            	PushLogInfo push = new PushLogInfo();
	            	push.setDeviceToken(pushResult.getDevice().getToken());
	            	push.setMsg(message);
	            	push.setResult(0);
	            	push.setTime(time);
	            	
	            	insertUserBuilds.add(push);
	            	
//	            	if(pushResult.getResponse() != null)
//	            		System.out.println("sendPush=====fail==" + pushResult.getResponse().getMessage());
	            }
	            
	            for(PushedNotification pushResult:successfulNotifications)
	            {
	            	PushLogInfo push = new PushLogInfo();
	            	push.setDeviceToken(pushResult.getDevice().getToken());
	            	push.setMsg(message);
	            	push.setResult(1);
	            	push.setTime(time);
	            	
	            	insertUserBuilds.add(push);
	            	
//	            	System.out.println("sendPush=====success==" + pushResult.getResponse().getMessage());
//	            	System.out.println("sendPush=====success==" + pushResult.toString());
	            }
	            
//	            System.out.println("UserPushMgr============" + insertUserBuilds.size());
	            PushInfoBussiness.insertPushLog(insertUserBuilds);
	            pushManager.stopConnection(); 
	             
	            
	     
	    } catch (Exception e) { 
	    	logger.error("[ UserPushMgr : sendPush ]", e);
	    } 

   } 
    

}
