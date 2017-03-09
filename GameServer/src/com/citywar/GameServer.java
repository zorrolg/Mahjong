package com.citywar;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.websocket.Session;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;

import com.citywar.bll.common.LanguageMgr;
import com.citywar.commands.ConsoleCommandMgr;
import com.citywar.dice.db.DBManager;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.AllAroundPlayerInfoMgr;
import com.citywar.manager.AntiAddictionMgr;
import com.citywar.manager.BuildMgr;
import com.citywar.manager.CardMgr;
import com.citywar.manager.DrawMgr;
import com.citywar.manager.FeedbackMgr;
import com.citywar.manager.GameMgr;
import com.citywar.manager.GameServerTimerMgr;
import com.citywar.manager.GiftBagMgr;
import com.citywar.manager.GiftTemplateMgr;
import com.citywar.manager.HallMgr;
import com.citywar.manager.ItemMgr;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.ReferenceMgr;
import com.citywar.manager.RobRoomMgr;
import com.citywar.manager.RobotChatMgr;
import com.citywar.manager.RobotLevelMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.RoomMgr;
import com.citywar.manager.ShopMgr;
import com.citywar.manager.StageMgr;
import com.citywar.manager.TaskMgr;
import com.citywar.manager.UserGiftMgr;
import com.citywar.manager.UserTopMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.manager.client.ClientConnetor;
import com.citywar.socket.BaseIoHandler;
import com.citywar.socket.Packet;
import com.citywar.socket.WebSocketCodecFactory;
import com.citywar.usercmd.UserPacketHandler;
import com.citywar.util.Config;
import com.citywar.util.DirtyCharUtil;
import com.citywar.websocket.WebSocketServer;


public class GameServer extends BaseServer
{
	private static String configPath;

	private static Logger logger = LogManager.getLogger(Class.class.getName());

	private static GameServer gameServer = null;

	private static ManagerServer managerServer;

	private UserPacketHandler handler = new UserPacketHandler();

	private static ExecutorService userCmdExecutor = null;

	private static ExecutorService serverCmdExecutor = null;

	public static final String Edition = "2612558";

	protected Hashtable<Session, BaseClient> allWebClients;

	// 服务器id
	private int serverId = 0;

	private String serverConn = "";

	private static boolean isRunning = false;

	private GameServer()
	{
		super();
	}

	public int getServerId()
	{
		return serverId;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		System.out.println("Game server begin start!");

		long time = System.currentTimeMillis();
		if (args.length <= 0)
		{
			System.err.println("please input configration file path...");
			return;
		}
		configPath = args[0];

		if (!Config.initConfig(configPath))
		{
			System.err.println("loading server configration file failed!");
			System.exit(1);
		}

		/* 初始化log4j日志配置 */
		PropertyConfigurator.configure(Config.getValue("log4j.path"));

		// 管理客户端登陆
		if (args.length > 1 && args[1].equals("manager"))
		{
			managerClient();
			return;
		}

		// 当系统发生无法捕获的异常时处理
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler()
		{
			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				e.printStackTrace();
				System.out.println("uncaughtexception:" + t.getId()+ e.getMessage());
				logger.error("uncaughtexception:" + t.getId() + e.getMessage());
			}
		});

		gameServer = new GameServer();

		// TODO 调整实际使用的消息处理线程数量
		userCmdExecutor = Executors.newFixedThreadPool(20);
		serverCmdExecutor = Executors.newFixedThreadPool(8);

		// 注册退出回调
		Runtime.getRuntime().addShutdownHook(new BaseShutdownHooker(gameServer));

		if (!gameServer.start())
		{
			System.out.println("Game Server started failed!");
			System.exit(1);
			return;
		}

		LOGGER.debug("游戏服务器启动成功!");
		System.out.println("Game server started successfully!");
		isRunning = true;
		System.out.println("starting waste time: "
				+ (System.currentTimeMillis() - time));

	}

	public static GameServer getInstance()
	{
		return gameServer;
	}

	@Override
	public UserPacketHandler getHandler()
	{
		return handler;
	}

	@Override
	public boolean start()
	{
		serverId = Integer.parseInt(Config.getValue("server.id"));
		serverConn = Config.getValue("db.path");

		// 启动数据库管理类, 初始化数据库连接, 使用连接池
		if (!initComponent(DBManager.initConfig(serverConn),
				"initialize db configration file"))
			return false;

		// 启动语言包管理类, 初始化游戏中使用的语言提示信息, load入内存
		if (!initComponent(LanguageMgr.setup(Config.getValue("server.languagePath")),
				"initialize language file"))
			return false;

		// 初始化脏字符处理
		if (!initComponent(DirtyCharUtil.illegalcharacterInit(Config.getValue("server.dirtycharPath")),
				"initialize dirtychar file"))
			return false;

		// 启动建筑信息管理类, 初始化建筑相关信息, load入内存
		if (!initComponent(BuildMgr.init(), "initialize BuildMgr"))
			return false;

		// 启动卡片信息管理类, 初始化卡片相关信息, load入内存
		if (!initComponent(CardMgr.init(), "initialize CardMgr"))
			return false;

		// 启动等级信息管理类, 初始化等级相关信息, load入内存
		if (!initComponent(LevelMgr.init(), "initialize LevelMgr"))
			return false;

		// 礼品模板管理
		if (!initComponent(GiftTemplateMgr.init(), "initialize GiftTemplateMgr"))
			return false;

		// 未登录用户暂时保存礼品模板管理
		if (!initComponent(UserGiftMgr.init(), "initialize UserGiftMgr"))
			return false;

		// 防沉迷管理
		if (!initComponent(AntiAddictionMgr.init(), "initialize AntiAddictionMgr"))
			return false;

		refreshGameProperties();

		// 启动全局管理类, 对所有上线玩家的管理
		if (!initComponent(WorldMgr.init(), "initialize WorldMgr"))
			return false;

		// 启动全局管理类, 对所有离线没超时的玩家的管理
		if (!initComponent(AllAroundPlayerInfoMgr.init(), "initialize AllAroundPlayerInfoMgr"))
			return false;

		// 启动游戏管理类, 开启线程池, 对游戏逻辑实例的生命周期的管理
		if (!initComponent(GameMgr.init(), "initialize GameMgr"))
			return false;

		// 启动物品管理类, 初始化全局物品模板信息
		if (!initComponent(ItemMgr.init(), "initialize ItemMgr"))
			return false;

		// 启动礼包, 初始化系统所有礼包
		if (!initComponent(GiftBagMgr.init(), "initialize GiftBagMgr"))
			return false;

		// 启动商场管理类, 初始化全局商城商品信息
		if (!initComponent(ShopMgr.init(), "initialize ShopMgr"))
			return false;

		// 抽奖管理类
		if (!initComponent(DrawMgr.init(), "initialize ShopMgr"))
			return false;

		// 启动管理客户端
		if (!initComponent(initManagerServer(), "initialize manager client"))
			return false;

		// 启动脚本调试
		if (!initComponent(InitScriptComponents(), "initialize script command"))
			return false;

		// 启动任务管理
		if (!initComponent(TaskMgr.init(), "initialize TaskMgr"))
			return false;

		// 启动奴隶关系管理
		if (!initComponent(ReferenceMgr.init(), "initialize ReferenceMgr"))
			return false;

		// 用户反馈管理器
		if (!initComponent(FeedbackMgr.init(), "initialize FeedbackMgr"))
			return false;

		//填充大厅的机器人房间
		if (!initComponent(RobRoomMgr.init(), "initialize RobRoomMgr"))
			return false;

		if (!initComponent(StageMgr.init(), "initialize StageMgr"))
			return false;

		// 机器人初始化
		if (!initComponent(UserTopMgr.init(), "initialize UserTopMgr"))
			return false;

		// 启动房间管理类, 开启线程池, 管理房间的逻辑创建、房间状态的更改, 进出入房间、房间开始游戏等的控制
		if (!initComponent(HallMgr.init(), "initialize RoomMgr"))
			return false;

		// 机器人初始化
		if (!initComponent(RobotLevelMgr.init(), "initialize RobotLevelMgr"))
			return false;

		// 机器人初始化
		if (!initComponent(RobotMgr.init(), "initialize RobotMgr"))
			return false;

		// 推送管理类
		//		if (!initComponent(UserPushMgr.init(), "initialize UserPushMgr"))
		//			return false;

		// HTTP管理类
		//        if (!initComponent(HttpServer_one.init(), "initialize HttpMgr1"))
		//            return false;
		//
		// HTTP管理类
		//        if (!initComponent(AnySdkLogin.init(), "initialize AnySdkLogin"))
		//            return false;

		//智能机器人语言库加载
		RobotChatMgr.init();

		// 全局timer启动
		GameServerTimerMgr.setup();

		// 启动游戏线程
		GameMgr.start();

		try
		{
			String url = Config.getValue("server.url");
			int port = Integer.parseInt(Config.getValue("server.port"));


			allWebClients = new Hashtable<Session, BaseClient>();
			WebSocketServer.startServer(url, port);

			//          startListen(port);

		}
		catch (Exception e)
		{
			logger.error("[ GameServer : start ]", e);
		}

		System.out.println("MINA Time server started.");

		return true;
	}

	/**
	 * initialize 服务器监听器
	 */
	@Override
	protected final void initServerAcceptor(IoAcceptor serverAcceptor)
	{
		serverAcceptor.getFilterChain().addLast("logger", new LoggingFilter());
		serverAcceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(
						new WebSocketCodecFactory()));

		serverAcceptor.setHandler(new BaseIoHandler(this));
		serverAcceptor.getSessionConfig().setReadBufferSize(4096);
	}

	@Override
	public boolean stop()
	{
		if (!isRunning)
		{
			return false;
		}
		isRunning = false;

		// 房间
		RoomMgr.stop();
		// 战斗游戏
		GameMgr.stop();

		// 各种TIMER
		GameServerTimerMgr.stop();
		// 保存玩家数据
		SaveTimerProc(null);

		super.stop();

		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		logger.warn("Server Stopped!");
		return true;
	}

	/**
	 *
	 */
	private void SaveTimerProc(Object object)
	{
		try
		{
			long startTick = System.nanoTime();
			if (logger.isInfoEnabled())
			{

			}
			int saveCount = 0;

			// 保存人物
			List<GamePlayer> list = WorldMgr.getAllPlayers();
			for (GamePlayer p : list)
			{
				p.getPlayerInfo().setLastQiutDate(new Timestamp(System.currentTimeMillis()));
				p.saveIntoDatabase(true);

				//保存玩家关系信息
				p.getPlayerReference().saveLogIntoDb();

				saveCount++;
			}
			//保存所有玩家关系信息
			ReferenceMgr.saveIntoDB();

			startTick = System.nanoTime() - startTick;
			if (logger.isInfoEnabled())
			{
				logger.info("Saving database complete!");
				logger.info("Saved all databases and " + saveCount
						+ " players in " + startTick + "ms");
			}
			if (startTick > 2 * 60 * 1000)
			{
				logger.warn(String.format(
						"Saved all databases and %d players in %d ms",
						saveCount, startTick));
			}
		}
		catch (Exception e1)
		{
			if (logger.isInfoEnabled())
				logger.error("SaveTimerProc", e1);
		}
	}

	/*
	 * 生成客户端对象
	 * 
	 * @see com.citywar.dicesns.BaseServer#getClient()
	 */
	@Override
	public BaseClient getClient()
	{
		return new ServerClient();
	}


	public ServerClient getServerClient()
	{
		return new ServerClient();
	}

	public BaseClient getDefaultClient()
	{
		if (allClients.size() > 0)
			return allClients.get(0);
		else
			return null;
	}

	/**
	 * 获取玩家指令专用执行线程池
	 * 
	 * @return 单列的执行线程池
	 */
	public static ExecutorService getUserCmdExecutor()
	{
		return userCmdExecutor;
	}

	/**
	 * 获取服务器间指令专用执行线程池
	 * 
	 * @return 单列的执行线程池
	 */
	public static ExecutorService getServerCmdExecutor()
	{
		return serverCmdExecutor;
	}

	public boolean refreshGameProperties()
	{

		return true;
	}

	/**
	 * 管理客户端处理
	 */
	private static void managerClient()
	{
		String ip = Config.getValue("server.ip");
		int port = Integer.parseInt(Config.getValue("manager.port"));

		try
		{
			new ClientConnetor(ip, port);
		}
		catch (UnknownHostException e)
		{
			logger.error("[ GameServer : managerClient ]", e);
		}
		catch (IOException e)
		{
			logger.error("[ GameServer : managerClient ]", e);
		}
	}

	// initialize 管理客户端
	private boolean initManagerServer()
	{
		managerServer = new ManagerServer();

		return managerServer.start();
	}

	/**
	 * 脚本命令initialize
	 * 
	 * @return
	 */
	private boolean InitScriptComponents()
	{
		// 调试命令脚本
		ConsoleCommandMgr.loadCommand();
		return true;
	}






	///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 连接回调，记录session信息，交由连接回调处理
	 */
	public final void webSessionOpened(Session session)
	{
		ServerClient client = getServerClient();
		client.setWebSession(session);
		String strName1 = client.getClass().toString();
		//        String strName2 = String.valueOf(client.hashCode());

		session.getUserProperties().put( strName1, client);
		//      session.setAttribute(client.getClass(), client);


		// 执行连接回调
		client.onConnect();

		// 记录客户端信息
		allWebClients.put(session, client);
	}

	/**
	 * 断开连接，交由回调函数处理
	 */
	public final void webSessionClosed(Session session)
	{
		BaseClient client = allWebClients.get(session);

		if (client != null)
		{
			// 移除客户端
			allClients.remove(session);

			// 执行断线回调
			client.onDisconnect();
		}
	}

	/**
	 * 消息回调，直接交由handler处理
	 */
	public final void webmessageReceived(Session session, Packet packet)
	{
		handler.handleWebPacket(session, packet);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////


}
