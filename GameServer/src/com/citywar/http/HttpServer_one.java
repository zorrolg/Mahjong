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

import com.citywar.http.pengyou.pengyouProcess;
import com.citywar.http.yidong.yitengProcess;
import com.citywar.type.ChannelType;
import com.citywar.type.HttpProcessType;
import com.citywar.util.Config;


public class HttpServer_one{


	private static final Logger logger = Logger.getLogger(HttpServer_one.class.getName());

	
	private static ServerSocket serverSocket;//服务器Socket

	private static Thread mainThread;
	
	private static boolean running;
	
	
    /**
     * 开始服务器 Socket 线程.
     */
	 public static boolean init() {
    	
		 
    	int PORT = Integer.parseInt(Config.getValue("httpserver1.port"));
    	
        try {
        	      	        	
            serverSocket=new ServerSocket(PORT);
            if(serverSocket == null)  
            	return false;
            
        } catch(Exception e) {
            System.out.println("HTTP start error:"+e.getLocalizedMessage());
        }
        
        
        boolean isStart = start();
        System.out.println("HTTP server started, port:"+PORT);
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
                	
//                    System.out.println("http server client=========:" + client);
                    
                	
                	HttpProcessType processType = null;                	
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
                            if(tempGetMap.containsKey("gameId"))			//同步推
                            {
                            	processType = yitengProcess.processBuy(client,tempGetMap, line);
                            }
                            else if(tempGetMap.containsKey("secret") && tempGetMap.get("secret").equals("15019225062"))			//同步推
                            {
                            	processType = pengyouProcess.processBuy(client,tempGetMap, line);
                            }
                            logger.error("===============================yitengProcess========:==========" + resource);
                            
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
                            
                            
                            

                            if((tempMap.containsKey("source") && tempMap.get("source").indexOf(ChannelType.ZHIFUBAO.getValue()) != -1)
                            		||(tempMap.containsKey("out_trade_no") && tempMap.get("out_trade_no").indexOf(ChannelType.ZHIFUBAO.getValue()) != -1))//支付宝-－客户端配置
                            {                       		
//                            	processType = alipayProcess.processBuy(client, tempMap, dataLine);
                            }
                            else if(tempMap.containsKey("secret") && tempMap.get("secret").equals("15019225062"))			//同步推
                            {
                            	processType = pengyouProcess.processBuy(client,tempMap, line);
                            }
                        }
                        
                    } catch(Exception ex) {                    	
                    	logger.error("HTTP error:" + ex.getLocalizedMessage());                    	
                    }
                    
                    
                    PrintWriter out=new PrintWriter(client.getOutputStream(),true);
        	        out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答
        	        out.println("Content-Type:text/html;charset=utf-8");
        	        out.println();// 根据 HTTP 协议, 空行将结束头信息
        	        out.println(processType == null ? "" : processType.getReturnInfo());
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
    
}
