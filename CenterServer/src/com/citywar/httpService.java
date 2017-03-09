package com.citywar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
//
//import net.sf.json.JSONObject;

public class httpService {

	// private static final Logger logger = Logger.getLogger("");

	private static ServerSocket serverSocket;// 服务器Socket

	private static Thread mainThread;

	private static boolean running;

	/**
	 * 开始服务器 Socket 线程.
	 */
	public static boolean init() {

		int PORT = 0;// Integer.parseInt(Config.getValue("httpserver.port"));

		try {

			serverSocket = new ServerSocket(PORT);
			if (serverSocket == null)
				return false;

		} catch (Exception e) {
			System.out.println("无法启动HTTP服务器:" + e.getLocalizedMessage());
		}

		boolean isStart = start();
		System.out.println("HTTP服务器正在运行,端口:" + PORT);
		return isStart;

	}

	/**
	 * 启动游戏执行线程
	 * 
	 * @return ture
	 */
	public static boolean start() {
		if (running == false)
			running = true;

		// 启动游戏执行线程
		mainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				gameThread();
			}
		});
		mainThread.start();

		return true;
	}

	/**
	 * 终止执行
	 */
	public static void stop() {
		if (running) {
			running = false;
		}
	}

	/**
	 * 运行服务器主线程, 监听客户端请求并返回响应.
	 */
	public static void gameThread() {

		while (running) {

			try {

				Socket client = null;// 客户Socket
				client = serverSocket.accept();// 客户机(这里是 IE 等浏览器)已经连接到当前服务器

				if (client != null) {

					System.out.println("连接到服务器的用户:" + client);

					try {
						// 第一阶段: 打开输入流
						BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						System.out.println("客户端发送的请求信息: ***************");

						// 读取第一行, 请求地址
						String line = in.readLine();
						System.out.println(line);
						if (line.length() <= 2)
							continue;

						// 获得请求的资源的地址
						String resource = line.substring(line.indexOf('/') + 2, line.lastIndexOf('/') - 5);
						resource = URLDecoder.decode(resource, "UTF-8");// 反编码
																		// URL
																		// 地址
						String method = new StringTokenizer(line).nextElement().toString();// 获取请求方法,
																							// GET
																							// POST
						if ("POST".equalsIgnoreCase(method))
							continue;

						// 变量
						Map<String, String> tempMap = new HashMap<String, String>();
						String[] list = resource.split("&");
						for (String str : list) {
							String[] kv = str.split("=");
							if (kv.length == 2)
								tempMap.put(kv[0], kv[1]);
						}

					} catch (Exception e) {
						System.out.println("HTTP服务器错误:" + e.getLocalizedMessage());
					}
				}
				// System.out.println(client+"连接到HTTP服务器");//如果加入这一句,服务器响应速度会很慢
			} catch (Exception e) {
				System.out.println("HTTP服务器错误:" + e.getLocalizedMessage());
			}
		}
	}

	// /**
	// * 关闭客户端 socket 并打印一条调试信息.
	// * @param socket 客户端 socket.
	// */
	// static void closeSocket(Socket socket) {
	// try {
	// socket.close();
	// } catch (IOException ex) {
	// ex.printStackTrace();
	// }
	// System.out.println(socket + "离开了HTTP服务器");
	// }

	// /**
	// * 读取一个文件的内容并返回给浏览器端.
	// * @param fileName 文件名
	// * @param socket 客户端 socket.
	// */
	// static void fileService(String fileName, Socket socket)
	// {
	//
	// try
	// {
	// PrintStream out = new PrintStream(socket.getOutputStream(), true);
	// File fileToSend = new File(fileName);
	// if(fileToSend.exists() && !fileToSend.isDirectory())
	// {
	// out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答
	// out.println("Content-Type:application/binary");
	// out.println("Content-Length:" + fileToSend.length());// 返回内容字节数
	// out.println();// 根据 HTTP 协议, 空行将结束头信息
	//
	// FileInputStream fis = new FileInputStream(fileToSend);
	// byte data[] = new byte[fis.available()];
	// fis.read(data);
	// out.write(data);
	// out.close();
	// fis.close();
	// }
	// }
	// catch(Exception e)
	// {
	// System.out.println("传送文件时出错:" + e.getLocalizedMessage());
	// }
	// }

}
