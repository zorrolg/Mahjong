/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citywar.event.BaseMultiEventSource;
import com.citywar.socket.Handler;
import com.citywar.socket.Packet;
import com.citywar.socket.SessionOwner;
import com.citywar.util.Config;

/**
 * 连接服务器的抽象客户端对象
 * 
 * @author sky
 * @date 2011-04-26
 * @version
 * 
 */
public abstract class BaseClient extends BaseMultiEventSource implements
        SessionOwner
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseClient.class);

    protected IoSession session = null;

    protected Handler handler = null;

    protected int id;

    protected int type;

    protected int serverId;

    protected String name;

    private String key = "ajdafjfjaljdflafj14317407130*#&**#@&#Q!*#()*";

    public String getKey()
    {
        return key;
    }

    public BaseClient()
    {
        serverId = Integer.parseInt(Config.getValue("server.id"));
    }

    /**
     * @return
     * @see com.citywar.dice.socket.Connection#getIp()
     */
    public String getIp()
    {
        return session.getRemoteAddress().toString();
    }

    /**
     * 判断client是否连接
     * 
     * @return
     */
    public boolean isConnection()
    {
        return session != null && session.isConnected();
    }

    /**
     * @param id
     *            设置客户端id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return 客户端id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param name
     *            客户端名称
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return 客户端名称
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    private final Object sendLock = new Object();

    /**
     * 发送数据包 *
     * 
     * @param packet
     *            数据包
     * 
     * @see com.citywar.dice.socket.ConnetionOwner#sendTCP(com.citywar.socket
     *      .Packet)
     */
    public void sendTCP(Packet packet)
    {
        if (session != null && !session.isClosing())
        {
            synchronized (sendLock)
            {
                session.write(packet);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.citywar.dicesns.socket.ConnetionOwner#setConnectionInfo(int, int,
     * java.lang.String)
     */
    public void setConnectionInfo(int id, int type, String name)
    {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    /**
     * @param serverId
     *            当前服务器的ID号，而非远程服务器的ID号
     */
    protected void setServerId(int serverId)
    {
        this.serverId = serverId;
    }

    /**
     * @return 当前服务器的ID号，而非远程服务器的ID号
     */
    public int getServerId()
    {
        return serverId;
    }

    /**
     * 获取连接session
     * 
     * @return session对象，未连接则为null
     */
    public IoSession getsSession()
    {
        return session;
    }

    /**
     * 设置连接session
     * 
     * @param session
     */
    public void setSession(IoSession session)
    {
        this.session = session;
    }

    /**
     * 断开socket连接
     */
    public void disconnect()
    {
        // 关闭连接
        session.close(false);
    }

    /**
     * @param handler
     *            the handler to set
     */
    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }

    /**
     * @return the handler
     */
    public Handler getHandler()
    {
        return handler;
    }

    /**
     * 连接回调，记录session信息，交由连接回调处理
     */
    public final void sessionOpened(IoSession session)
    {
        this.session = session;
        session.setAttribute(this.getClass(), this);
        onConnect();
    }

    /**
     * 断开连接，交由回调函数处理
     */
    public final void sessionClosed(IoSession session)
    {
        if (this.session != null && this.session == session)
        {
            onDisconnect();
        }
    }

    /**
     * 消息回调，直接交由handler处理
     */
    public final void messageReceived(IoSession session, Packet packet)
    {
        if (this.session != null && this.session == session)
        {
            handler.handlePacket(session, packet);
        }
    }

    /**
     * 客户端连接回调
     */
    public abstract void onConnect();

    /**
     * 客户端断开连接回调
     */
    public abstract void onDisconnect();
}
