/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar;

import java.io.IOException;

import javax.websocket.Session;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;


/**
 * BaseClient的实现类，用于描述连接当前服务器的远程服务器对象
 * 
 * @author sky
 * @date 2011-04-26
 * @version
 * 
 */
public class ServerClient extends BaseClient
{
    protected GamePlayer player;
    protected Session webSession = null;
    private final Object sendLock = new Object();
    
    public GamePlayer getPlayer()
    {
        return player;
    }

    public void setPlayer(GamePlayer mPlayer)
    {
        player = mPlayer;
    }

    /**
     * 连接断开是的处理回调
     * 
     * @param connection
     *            网络连接对象
     */
    @Override
    public void onDisconnect()
    {
        if (player != null)
            player.disconnect();
    }

    /**
     * 建立服务器连接时的处理回调
     */
    @Override
    public void onConnect()
    {
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * @return
     * @see com.citywar.dice.socket.Connection#getIp()
     */
    @Override
    public String getIp()
    {
        return webSession.getRequestURI().getUserInfo();
    }

    /**
     * 判断client是否连接
     * 
     * @return
     */
    @Override
    public boolean isConnection()
    {
        return webSession != null && webSession.isOpen();
    }
    
    /**
     * 发送数据包 *
     * @param packet 数据包
     * @see com.citywar.dice.socket.ConnetionOwner#sendTCP(com.citywar.socket.Packet)
     */
    @Override
    public void sendTCP(Packet packet)
    {
        if (webSession != null && webSession.isOpen())
        {
            synchronized (sendLock)
            {
            	try {
					webSession.getBasicRemote().sendText(packet.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }
    
    /**
     * 获取连接session
     * 
     * @return session对象，未连接则为null
     */

    public Session getsWebSession()
    {
        return webSession;
    }

    /**
     * 设置连接session
     * 
     * @param session
     */
    public void setWebSession(Session session)
    {
        this.webSession = session;
    }

    /**
     * 断开socket连接
     */
    @Override
    public void disconnect()
    {
        // 关闭连接
    	try {
			webSession.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
//    /**
//     * 连接回调，记录session信息，交由连接回调处理
//     */
//    @Override
//    public final void sessionOpened(IoSession session)
//    {
//        this.session = session;
//        session.setAttribute(this.getClass(), this);
//        onConnect();
//    }
//
//    /**
//     * 断开连接，交由回调函数处理
//     */
//    @Override
//    public final void sessionClosed(IoSession session)
//    {
//        if (this.session != null && this.session == session)
//        {
//            onDisconnect();
//        }
//    }
//
//    /**
//     * 消息回调，直接交由handler处理
//     */
//    @Override
//    public final void messageReceived(IoSession session, Packet packet)
//    {
//        if (this.session != null && this.session == session)
//        {
//            handler.handlePacket(session, packet);
//        }
//    }
    
}
