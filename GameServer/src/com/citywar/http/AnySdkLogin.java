package com.citywar.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;




public class AnySdkLogin{


	private static final Logger logger = Logger.getLogger(AnySdkLogin.class.getName());
	
	private static ServerSocket serverSocket;//服务器Socket

	private static Thread mainThread;
	
	private static boolean running;
	
	
	
	
	/**
	 * anysdk统一登录地址
	 */
	private static String loginCheckUrl = "http://callback-play.cocos.com/api/User/LoginOauth/";
	
	/**
	 * connect time out
	 * 
	 * @var int
	 */
	private static int connectTimeOut = 30 * 1000;

	/**
	 * time out second
	 * 
	 * @var int
	 */
	private static int timeOut = 30 * 1000;

	/**
	 * user agent
	 * 
	 * @var string
	 */
	private static final String userAgent = "px v1.0";
	
	
	
	
	
    /**
     * 开始服务器 Socket 线程.
     */
	 public static boolean init() {
    	
		 int PORT = 5005;
		 
        try {
        	      	        	
            serverSocket=new ServerSocket(PORT);
            if(serverSocket == null)  
            	return false;
            
        } catch(Exception e) {
            System.out.println("HTTP start error:"+e.getLocalizedMessage());
        }
        
        
        boolean isStart = start();
        System.out.println("anysdk login started, port:"+PORT);
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
                        	
                        	PrintWriter out=new PrintWriter(client.getOutputStream(),true);
                 	        out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答
                 	        out.println("Content-Type:application/json;charset=utf-8");
                 	        out.println();// 根据 HTTP 协议, 空行将结束头信息
                 	        out.println("{\"status\":\"ok\",\"data\":{\"uid\":\"322288\",\"nickName\":\"daivdz\",\"token\":\"E10ADC3\",\"avatarUrl\":\"http://pic.h5kk.com/uploadimg/322288/pic_20151217201841_s.jpg\",\"gameid\":\"605\",\"gametoken\":\"451276873\"},\"common\":{\"channel\":\"160104\",\"user_sdk\":\"RT_jinwan\",\"uid\":\"322288\",\"server_id\":\"1\",\"plugin_id\":\"294\"},\"ext\":{\"accountID\":\"322288\"}}");
               	        
                 	        out.close();                 	        
                             
                 	        closeSocket(client);                          
                            
                        }
                        else if ("POST".equalsIgnoreCase(method)) 
                        {
                        	
                        	String dataLine = "";  
                        	int contentLength = 0;
                        	String postStr = "";//in.readLine();  
                            while (postStr != null){
                                System.out.println("http server post=========:" + postStr);
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
                            
                            
                            
                            
                            
                            
                            dataLine+="&private_key=E727370BD145EF05E9CF0535142FEFE7";
                            System.out.println("http server post data=========:" + dataLine);
                            strReturn = check(dataLine, tempMap);
                            
                            
                            
                            
                            
                            
                            //登录验证流程第 8 步，游戏服务器再返回通知AnySDK框架登录验证结果，时需要给 ext 中加一个 accountID，值为 common 里的 uid                          
                            JSONObject jsonMain = JSONObject.fromObject(strReturn);
                            JSONObject jsonCommon = jsonMain.getJSONObject("common");
                            String strUid = setParamWithDefaultValue(jsonCommon,"uid");
                            
                            JSONObject jsonExt = new JSONObject();
//                            JSONObject jsonExt = jsonMain.getJSONObject("ext");
                            jsonExt.put("accountID", strUid);
                            jsonMain.put("ext", jsonExt);
                            strReturn = jsonMain.toString();
                            
                            
                        }
                        
                    } catch(Exception ex) {                    	
                    	logger.error("HTTP error:" + ex.getLocalizedMessage());                    	
                    }
                    
                    
                    
                    PrintWriter out=new PrintWriter(client.getOutputStream(),true);
         	        out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答
         	        out.println("Content-Type:application/json;charset=utf-8");
         	        out.println();// 根据 HTTP 协议, 空行将结束头信息
         	        
//                    PrintWriter out=new PrintWriter(client.getOutputStream(),true);
//        	        out.println("HTTP/1.1 200 OK");//返回应答消息,并结束应答
//        	        out.println("Transfer-Encoding:chunked");
//        	        out.println("Connection: keep-alive");
//        	        out.println("Content-Type: application/json");
//        	        out.println();// 根据 HTTP 协议, 空行将结束头信息
        	        out.println(strReturn);
        	        out.close();
        	        
        	        System.out.println("strReturn=========:" + strReturn);
        	        
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
    
    
    
    public static String setParamWithDefaultValue(JSONObject json, String keyName)
	{
		String result = "";
		if (null != json && json.containsKey(keyName))
		{
			result = json.getString(keyName);
		}
		return null != result ? result : "";
	}
    
    
    
    
    
    
    
    
    
    
    static public String check(String queryString, HashMap<String,String> params) {
		
    	
    	String strReturn = "";
    	
    	try{
    		
            //检测必要参数
            if(parametersIsset( params )) {
            	strReturn = "parameter not complete";
//            	sendToClient( response, "parameter not complete" );
                return strReturn;
            }
            
            
            URL url = new URL( loginCheckUrl );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty( "User-Agent", userAgent );
            conn.setReadTimeout(timeOut);
            conn.setConnectTimeout(connectTimeOut);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8") );
            writer.write( queryString );
            writer.flush();
            tryClose( writer );
            tryClose( os );
            conn.connect();
            
            Map<String, List<String>> map = conn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                System.out.println("Key : " + entry.getKey() + 
                                   " ,Value : " + entry.getValue());
            }
            
            InputStream is = conn.getInputStream();
            String result = stream2String( is ); 
//            sendToClient( response, result );
            strReturn= result;
            return strReturn;
    	} catch( Exception e ) {
    		e.printStackTrace();
    	}
    	strReturn= "Unknown error!";
//        sendToClient( response,  );
    	return strReturn;
    }


    

	/**
	 * check needed parameters isset 检查必须的参数 channel
	 * uapi_key：渠道提供给应用的app_id或app_key（标识应用的id）
	 * uapi_secret：渠道提供给应用的app_key或app_secret（支付签名使用的密钥）
	 * 
	 * @param params
	 * @return boolean
	 */
	static private boolean parametersIsset(HashMap<String, String> params) {
		return !(params.containsKey("channel") && params.containsKey("uapi_key")
				&& params.containsKey("uapi_secret"));
	}



	/**
	 * 获取流中的字符串
	 * @param is
	 * @return
	 */
	static private String stream2String( InputStream is ) {
		BufferedReader br = null;
		try{
			br = new BufferedReader( new java.io.InputStreamReader( is ));	
			String line = "";
			StringBuilder sb = new StringBuilder();
			while( ( line = br.readLine() ) != null ) {
				
				sb.append( line );
			}
			return sb.toString();
		} catch( Exception e ) {
			e.printStackTrace();
		} finally {
			tryClose( br );
		}
		return "";
	}
	
//	/**
//	 * 向客户端应答结果
//	 * @param response
//	 * @param content
//	 */
//	static private void sendToClient( HttpServletResponse response, String content ) {
//		response.setContentType( "text/plain;charset=utf-8");
//		try{
//			PrintWriter writer = response.getWriter();
//			writer.write( content );
//			writer.flush();
//		} catch( Exception e ) {
//			e.printStackTrace();
//		}
//	}
	/**
	 * 关闭输出流
	 * @param os
	 */
	static private void tryClose( OutputStream os ) {
		try{
			if( null != os ) {
				os.close();
				os = null;
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭writer
	 * @param writer
	 */
	static private void tryClose( java.io.Writer writer ) {
		try{
			if( null != writer ) {
				writer.close();
				writer = null;
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭Reader
	 * @param reader
	 */
	static private void tryClose( java.io.Reader reader ) {
		try{
			if( null != reader ) {
				reader.close();
				reader = null;
			}
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	
    
}
