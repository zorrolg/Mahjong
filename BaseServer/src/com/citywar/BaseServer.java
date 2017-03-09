package com.citywar;

import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citywar.socket.BaseIoHandler;
import com.citywar.socket.Handler;
import com.citywar.socket.Packet;
import com.citywar.socket.SessionOwner;
import com.citywar.socket.StrictCodecFactory;
import com.citywar.util.Config;

public abstract class BaseServer implements SessionOwner {
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseServer.class);

	protected int id;

	protected int type;

	protected Hashtable<IoSession, BaseClient> allClients;

	// 普通连接监听器
	protected IoAcceptor acceptor;

	protected boolean isRunning;

	protected BaseServer() {
		allClients = new Hashtable<IoSession, BaseClient>();
		id = Integer.parseInt(Config.getValue("server.id"));
		type = Integer.parseInt(Config.getValue("server.type"));
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public List<BaseClient> getAllClients() {
		LinkedList<BaseClient> temp = null;

		synchronized (allClients) {
			temp = new LinkedList<BaseClient>(allClients.values());
		}
		return temp;
	}

	/**
	 * 初始化相关模块
	 * 
	 * @param initResult
	 * @param componentName
	 * @return
	 */
	public boolean initComponent(boolean initResult, String componentName) {
		if (!initResult) {
			LOGGER.error(componentName + " failed");
			// System.out.println(componentName + " failed");
		} else {
			LOGGER.info(componentName + " successfully");
		}
		return initResult;
	}

	protected final void startListen(int port) throws Exception {
		// 开启主监听器
		acceptor = new NioSocketAcceptor();
		initServerAcceptor(acceptor);
		acceptor.bind(new InetSocketAddress(port));

		isRunning = true;
	}

	/**
	 * 服务器主监听器初始化，此处实现通用初始化过程，实例对象若需要特殊初始化过程，可重载本接口
	 */
	protected void initServerAcceptor(IoAcceptor serverAcceptor) {
		((NioSocketAcceptor) serverAcceptor).setReuseAddress(true);
		serverAcceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new StrictCodecFactory()));
		serverAcceptor.getFilterChain().addLast("logger", new LoggingFilter());
		serverAcceptor.getSessionConfig().setReadBufferSize(4096);
		serverAcceptor.setHandler(new BaseIoHandler(this));
	}

	/**
	 * 移除指定客户端
	 * 
	 * @param client
	 *            客户端对象
	 */
	public void removeClient(BaseClient client) {
		IoSession session = client.getsSession();

		if (session != null)
			allClients.remove(session);
	}

	public abstract Handler getHandler();

	public abstract BaseClient getClient();

	public abstract boolean start();

	public boolean stop() {
		List<BaseClient> cList = getAllClients();

		// 断开所有连接
		for (BaseClient client : cList) {
			client.disconnect();
		}

		// 关闭监听
		isRunning = false;

		if (acceptor != null)
			acceptor.unbind();

		synchronized (allClients) {
			allClients.clear();
		}

		return true;
	}

	/**
	 * 连接回调，记录session信息，交由连接回调处理
	 */
	public final void sessionOpened(IoSession session) {
		BaseClient client = getClient();
		client.setSession(session);
		session.setAttribute(client.getClass(), client);

		// 执行连接回调
		client.onConnect();

		// 记录客户端信息
		allClients.put(session, client);
	}

	/**
	 * 断开连接，交由回调函数处理
	 */
	public final void sessionClosed(IoSession session) {
		BaseClient client = allClients.get(session);

		if (client != null) {
			// 移除客户端
			allClients.remove(session);

			// 执行断线回调
			client.onDisconnect();
		}
	}

	/**
	 * 消息回调，直接交由handler处理
	 */
	public final void messageReceived(IoSession session, Packet packet) {
		getHandler().handlePacket(session, packet);
	}
}
