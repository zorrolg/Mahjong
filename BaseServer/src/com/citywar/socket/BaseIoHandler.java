/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.socket;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用IoHandler，直接将数据包投弹给协议解析器
 * 
 * @author sky
 * @date 2011-9-22
 * @version
 * 
 */
public class BaseIoHandler extends IoHandlerAdapter
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseIoHandler.class);

    private final SessionOwner owner;

    public BaseIoHandler(SessionOwner owner)
    {
        this.owner = owner;
    }

    /**
     * 连接创建回调
     */
    public void sessionOpened(IoSession session) throws Exception
    {
        owner.sessionOpened(session);
    }

    /**
     * 连接关闭回调
     */
    public void sessionClosed(IoSession session) throws Exception
    {
        owner.sessionClosed(session);
    }

    /**
     * 异常回调，断开连接
     */
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception
    {
        session.close(true);
    }

    /**
     * 消息回调，交由指令解析器处理
     */
    public void messageReceived(IoSession session, Object message)
            throws Exception
    {
        if (!(message instanceof Packet))
            return;

        
        Packet packet = (Packet) message;
        owner.messageReceived(session, packet);
    }
    
    /**
     * @author Jacky.zheng
     * @Date 2012-03-16
     * 新增解决TCP连接TIME_WAIT过多设置的问题
     */
    public void sessionCreated(IoSession session) throws Exception {
    	SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
    	cfg.setReceiveBufferSize(2 * 1024 * 1024);
    	cfg.setReadBufferSize(2 * 1024 * 1024);
    	cfg.setKeepAlive(true);
    	cfg.setSoLinger(0);//解决TCP连接TIME_WAIT的问题
    }
}
