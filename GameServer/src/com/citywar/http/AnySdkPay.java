package com.citywar.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.citywar.bll.OrderBusiness;
import com.citywar.dice.entity.ShopGoodInfo;
import com.citywar.dice.entity.UserOrderInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.ShopMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.type.ChannelType;
import com.citywar.type.TaskConditionType;


public class AnySdkPay{


	private static final Logger logger = Logger.getLogger(AnySdkPay.class.getName());

	
	private static ServerSocket serverSocket;//服务器Socket

	private static Thread mainThread;
	
	private static boolean running;
	
	
	
	
	private static String paramValues = "";

	/**
	 * 从通知参数里面获取到的签名值
	 */
	private static String originSign = "";
	
	
    /**
     * 开始服务器 Socket 线程.
     */
	 public static boolean init() {
    	
		 
    	int PORT = 5006;
    	
        try {
        	      	        	
            serverSocket=new ServerSocket(PORT);
            if(serverSocket == null)  
            	return false;
            
        } catch(Exception e) {
            System.out.println("HTTP start error:"+e.getLocalizedMessage());
        }
        
        
        boolean isStart = start();
        System.out.println("anysdk pay started, port::"+PORT);
        return isStart;
        
    }
    

	 
	 /**
	     * 启动游戏执行线程
	     * 
	     * @return ture
	     */
	    public static boolean start()
	    {
	        if (running == false)
	            running = true;

	        // 启动游戏执行线程
	        mainThread=new Thread(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                gameThread();
	            }
	        });
	        mainThread.start();

	        return true;
	    }

	    /**
	     * 终止执行
	     */
	    public static void stop()
	    {
	        if (running)
	        {
	            running = false;
	        }
	    }
	    
	    
	    
	    
    /**
     * 运行服务器主线程, 监听客户端请求并返回响应.
     */
    public static void gameThread() {
    	
    	while (running)
        {
        	
            try {
            	
                Socket client=null;//客户Socket
                client = serverSocket.accept();//客户机(这里是 IE 等浏览器)已经连接到当前服务器
                
                
                if(client != null) {
                	
                	
                	String strReturn = "";
//                    System.out.println("http server client=========:" + client);
                    
                	
//                	HttpProcessType processType = null;                	
                    try {
                    	
                        // 第一阶段: 打开输入流
                        BufferedReader in=new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));                        
                        
                        
                        // 读取第一行, 请求地址                        
                        String line=in.readLine();
                        String method = new StringTokenizer(line).nextElement().toString();// 获取请求方法, GET 或者 POST     
                        if(line.length() <= 2) 
                         	continue;

                        
                        
                        String resource = "";
                        HashMap<String, String> tempGetMap = new LinkedHashMap<String, String>();
                        
                        int indexQueryStart = line.indexOf("/?") > 0 ? line.indexOf('/') + 2: line.indexOf('/') + 1 ;
                        if(indexQueryStart < line.lastIndexOf('/')-5)
                        {
                        	//获得请求的资源的地址
                            resource=line.substring(indexQueryStart,line.lastIndexOf('/')-5);                        
                            resource=URLDecoder.decode(resource, "UTF-8");//反编码 URL 地址
                                                          
                        	//变量                                                     
                            String[] listGet = resource.split("&");                        
                            for(String str:listGet)
                            {
                            	String[] kv = str.split("=");
                            	if(kv.length == 2)
                            		tempGetMap.put(kv[0], kv[1]);
                            }
                        }
                        System.out.println("");
                        System.out.println("http server resource=========:" + resource);
                        
                        
                        
                        
                        // 处理
                        if("GET".equalsIgnoreCase(method)) 
                        {                        	
                            //process                        	
                            if(tempGetMap.containsKey("source") && tempGetMap.get("source").indexOf(ChannelType.TONGBU.getValueTag()) != -1)			//同步推
                            {
//                            	processType = tongbuProcess.processBuy(client,tempGetMap, line);
                            }

                            
                            

                            
                	        
                        }
                        else if ("POST".equalsIgnoreCase(method)) 
                        {
                        	
                        	String dataLine = "";  
                        	int contentLength = 0;
                        	String postStr = in.readLine();  
                            while (postStr != null){
//                                System.out.println("http server post=========:" + postStr);
                                postStr = in.readLine();  
                                if ("".equals(postStr)) {
                                    break;
                                } else if (postStr.indexOf("Content-Length") != -1) {  
                                    contentLength = Integer.parseInt(postStr.substring(postStr.indexOf("Content-Length") + 16));  
                                }
                            }
//                            contentLength -= 4;
//                            //继续读取普通post（没有附件）提交的数据  
//                            System.out.println("http server post len=========:" + contentLength);     

                            

                            
                          ////////////////////////////1111///////////////////////////////////////////////////////////////////////华为中文无法通过验证
                            //用户发送的post数据正文  
                            byte[] buf = {};  
                            int size = 0;  
                            if (contentLength != 0) {  
                                buf = new byte[contentLength];  
                                while(size<contentLength){  
                                    int c = in.read();  
                                    buf[size++] = (byte)c;  
                                }  
                                //dataLine = new String(buf,"UTF-8");						
                                //dataLine = new String(buf, 0, size);
                                dataLine = new String(buf,"ISO-8859-1");
                            }  
                            System.out.println("http server post data=========:" + dataLine);
                          ////////////////////////////1111///////////////////////////////////////////////////////////////////////                     
                            
                                                        
                            
                          
                          
                            HashMap<String, String> tempMap = new LinkedHashMap<String, String>();
                            String[] list = dataLine.split("&");
                            for(String str:list)
                            {
                            	if(str.indexOf("=") != -1)
                            	{
                            		String key = str.substring(0,str.indexOf("="));
                            		String valve = URLDecoder.decode(str.substring(str.indexOf("=") + 1,str.length()), "UTF-8");
                            		tempMap.put(key, valve);
//                            		System.out.println("gameThread================" + key + "======" + valve);
                            	}
                            }
                            
                            
                            
                            
                            
                            
                            // serverlet下需使用 getValues 生成待签字符串, 先要完善 getValues 方法
                            originSign = "ea11ec63eabf3c96565cb779df56580b";                   		
                    		paramValues = "19999991PB06971408131039426501499912014-08-13 10:39:430jinbigold13{\"amount\":\"1\",\"app_id\":\"1738\",\"cp_order_id\":\"PB069714081310394265014\",\"ext1\":\"\",\"ext2\":\"\",\"trans_id\":\"20282\",\"trans_status\":\"1\",\"user_id\":\"1799\",\"sign\":\"08dfe21e1f4f26e334ec3b9b7f419b731dcd8255\"}1799";
                    		
                    		
                    		
                    		/**
                    		 * AnySDK分配的 PrivateKey
                    		 * 
                    		 * 正式使用时记得用AnySDK分配的正式的PrivateKey
                    		 */
                            AnySdkPayNotify paynotify = new AnySdkPayNotify();
                    		paynotify.setPrivateKey("anysdkPrivateKeyxxxxxx");
                    	
                    		
                    		// 这是验签测试
//                    		System.out.println("参考签名值: " + originSign + "\n");
//                    		System.out.println("待签字符串: " + paramValues + "\n");
//                    		System.out.println("计算得到的签名值: " + paynotify.getSign(paramValues) + "\n");                    		                    		
                    		if (paynotify.checkSign(paramValues, originSign)){
                    			
                    			strReturn = "ok";
                    			System.out.println("验证签名成功\n");    
                    			
                    			                    			       
                    			
                    			//订单处理  alex
                    			UserOrderInfo userOrderInfo = OrderBusiness.getUserOrderById("");
								if (!userOrderInfo.getStatus().isEmpty() && !userOrderInfo.getProductId().isEmpty())
        						{
        							int status = Integer.valueOf(userOrderInfo.getStatus());
        							int shop_id = Integer.valueOf(userOrderInfo.getProductId());
        							if (status == 0 && shop_id > 0) {
        								
        								GamePlayer player = WorldMgr.getPlayerByID(1);
        								player.getPropBag().addOneItem(shop_id);
        								
        								ShopGoodInfo shopGood = ShopMgr.getShopGoodById(shop_id);
        								player.isFinishTask(TaskConditionType.ChargeMoney, shopGood.getCount(), 0);
        								
        								player.getOut().sendUpdateBaseInfo();
                			            player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
        							}
        						}

        			            
                    		} 
                    		else 
                    		{                    			
                    			strReturn = "false";
                    			System.out.println("验证签名失败");                    			
                    		}
                    		
                        }
                        
                    } catch(Exception ex) {                    	
                    	logger.error("HTTP error:" + ex.getLocalizedMessage());                    	
                    }
                    
                    
                    PrintWriter out=new PrintWriter(client.getOutputStream(),true);
        	        out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答
        	        out.println("Content-Type:text/html;charset=utf-8");
        	        out.println();// 根据 HTTP 协议, 空行将结束头信息
        	        out.println(strReturn);
        	        out.close();
        	        
                    closeSocket(client);
                }
                //System.out.println(client+"连接到HTTP服务器");//如果加入这一句,服务器响应速度会很慢
            } catch(Exception ex) {
            	logger.error("HTTP error:" + ex.getLocalizedMessage());
            }
        }
    	
    	
    }
    

    static void closeSocket(Socket socket) {
  	  
    	try {
  		  socket.close();
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
	  	logger.debug(socket + " close Socket");   
    }
    
    
    
    
    
    
    
    
    
//    /**
//	 * 将参数名从小到大排序，结果如：adfd,bcdr,bff,zx
//	 * 
//	 * @param List<String> paramNames 
//	 */
//	public void sortParamNames(List<String> paramNames) {
//			Collections.sort(paramNames, new Comparator<String>() {
//				public int compare(String str1,String str2) {
//					return str1.compareTo(str2);
//				}
//			});
//	}
//	
//	/**
//	 * 从 HTTP请求参数 生成待签字符串, 此方法需要在 serverlet 下测试, 测试的时候取消注释, 引入该引入的类
//	 */
//	public static String getValues(HttpServletRequest request){
//		Enumeration<String> requestParams=request.getParameterNames();//获得所有的参数名
//		List<String> params=new ArrayList<String>();
//		while (requestParams.hasMoreElements()) {
//			params.add((String) requestParams.nextElement());
//		}
//		sortParamNames(params);// 将参数名从小到大排序，结果如：adfd,bcdr,bff,zx
//
//		String paramValues="";
//		for (String param : params) {//拼接参数值
//			if (param.equals("sign")) {
//				originSign = request.getParameter(param);
//				continue;
//			}
//			String paramValue=request.getParameter(param);
//			if (paramValue!=null) {
//				paramValues+=paramValue;
//			}
//		}
//		
//		return paramValues;
//	}
	
	
    
}
