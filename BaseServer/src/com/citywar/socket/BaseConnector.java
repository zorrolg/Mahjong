/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.socket;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.citywar.manager.ManagerClient;

/**
 * 连接器类
 * 
 * @author sky
 * @date 2011-9-23
 * @version
 * 
 */
public abstract class BaseConnector extends ManagerClient
{
    /**
     * 默认连接超时时间：15s
     */
    public static final long DEFAULT_TIMEOUT_MILLISECOND = 15000;

    protected InetSocketAddress currentAddress = null;

    protected IoConnector connector = null;

    /**
     * 连接远程服务器（异步连接）
     * 
     * @param host
     *            主机地址
     * @param port
     *            连接端口号
     */
    public void connectTo(String host, int port)
    {
        connector = new NioSocketConnector();

        // 初始化
        initConnector(connector);

        // 开始连接
        currentAddress = new InetSocketAddress(host, port);
        connector.connect(currentAddress);
    }

    /**
     * 同步连接远程服务器，若未指定超时时间，则默认超时时间为15s
     * 
     * @param host
     *            主机地址
     * @param port
     *            连接端口号
     * @param timeoutMillis
     *            连接超时时间
     * @return
     */
    public boolean connectToSync(String host, int port, long timeoutMillis)
    {
        if (timeoutMillis == 0)
            timeoutMillis = DEFAULT_TIMEOUT_MILLISECOND;

        connector = new NioSocketConnector();

        // 初始化
        initConnector(connector);

        // 开始连接
        currentAddress = new InetSocketAddress(host, port);

        try
        {
            ConnectFuture connectFuture = connector.connect(currentAddress);
            return connectFuture.await(timeoutMillis);
        }
        catch (InterruptedException e)
        {
            LOGGER.error(String.format("fail to reconnect to %s:%d",
                                       currentAddress.getAddress(),
                                       currentAddress.getPort()), e);
            return false;
        }
    }

    /**
     * 尝试重新连接，必须指定超时时间
     * 
     * @param timeoutMillis
     *            超时时间，以毫秒为单位
     * @return 成功为true，否则false
     */
    public boolean retryConnection(long timeoutMillis)
    {
        try
        {
            ConnectFuture connectFuture = connector.connect(currentAddress);
            return connectFuture.await(timeoutMillis);
        }
        catch (InterruptedException e)
        {
            LOGGER.error(String.format("fail to reconnect to %s:%d",
                                       currentAddress.getAddress(),
                                       currentAddress.getPort()), e);
            return false;
        }
    }

    /**
     * 通用connector初始化，如有个性化需求，可重载本接口
     * 
     * @param connector
     */
    public void initConnector(IoConnector connector)
    {
        connector.getFilterChain().addLast("codec",
                                           new ProtocolCodecFilter(
                                                   new StrictCodecFactory()));
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getSessionConfig().setReadBufferSize(4096);
        connector.setHandler(new ConnectorIoHandler(this));
    }
}
