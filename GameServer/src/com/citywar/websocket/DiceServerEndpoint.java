package com.citywar.websocket;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.citywar.GameServer;
import com.citywar.socket.Packet;

@ServerEndpoint(value = "/game")
public class DiceServerEndpoint {

//    private Logger logger = Logger.getLogger(this.getClass().getName());

    @OnOpen
    public void onOpen(Session session) {
//        logger.info("Connected ... " + session.getId());        
        GameServer.getInstance().webSessionOpened(session);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
    	
    	GameServer.getInstance().webSessionClosed(session);
//        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
        
    @OnMessage
    public String onMessage(String unscrambledWord, Session session) {

//    	System.out.println("Received: " + unscrambledWord);
	    // Send the first message to the client
//	    try {

	    	Packet packet = new Packet(unscrambledWord);   	
	    	GameServer.getInstance().webmessageReceived(session, packet);
	    	
//			session.getBasicRemote().sendText("This is the first server message");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    return "";
    }
        
    @OnError
    public void error(Session session, Throwable t)
    {
    	GameServer.getInstance().webSessionClosed(session);
    }

}
