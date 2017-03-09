package com.citywar.websocket;

import org.glassfish.tyrus.server.Server;

public class WebSocketServer {

	private static Server webSocketLister;

	public static void startServer(String url, int port) {

		webSocketLister = new Server(url, port, "/citywar", DiceServerEndpoint.class);

		try {

			webSocketLister.start();
			System.out.println("server WebSocketLister started, port:" + port);

		} catch (Exception e) {

			System.out.println("server WebSocketLister started:" + e.getLocalizedMessage());

		} finally {

		}
	}

	public static void stopServer() {

		try {

			webSocketLister.stop();
			System.out.println("server WebSocketLister stoped");

		} catch (Exception e) {
			System.out.println("server WebSocketLister stoped:" + e.getLocalizedMessage());
		} finally {
			webSocketLister.getServerContainer().stop();
		}
	}

}
