/**
t *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.room;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.citywar.bll.PengGameBussiness;
import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.dice.entity.PengConfigInfo;
import com.citywar.dice.entity.PengGame;
import com.citywar.dice.entity.PengGameDetail;
import com.citywar.event.IEventListener;
import com.citywar.game.BaseGame;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.gameutil.PengGameScore;
import com.citywar.hall.BaseHall;
import com.citywar.manager.GameMgr;
import com.citywar.manager.HallMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.RoomMgr;
import com.citywar.manager.UserTopMgr;
import com.citywar.room.action.ExitRoomAction;
import com.citywar.room.action.RobotEnterRoomAction;
import com.citywar.socket.Packet;
import com.citywar.type.GameEventType;
import com.citywar.type.GameHardType;
import com.citywar.type.GameState;
import com.citywar.type.HallGameType;
import com.citywar.type.MessageType;
import com.citywar.type.PlayerState;
import com.citywar.type.UserCmdOutType;
import com.citywar.util.Config;
import com.citywar.util.ThreadSafeRandom;
import com.citywar.util.TimeUtil;
import com.citywar.util.wrapper.WrapInteger;

/**
 * 游戏基本房间信息
 * 
 * @author Dream
 * @date 2011-12-19
 * @version
 * 
 */
public class BaseRoom
{
    private static final Logger logger = Logger.getLogger(BaseRoom.class.getName());

    /** 加密锁 当房间密码为此值得时候 表示房间上了锁  */
    public static final String PWD_LOCK ="5555555555";
    
    /** 解密锁 当房间设施了密码 用此值解密 */
    public static final String UN_LOCK = "";
    /**
     * 房间可以进行游戏的人数
     */
    protected int initPlayerCount = 4;

    /**
     * 房间ID
     */
    protected int roomId;
    /**
     * 大厅ID
     */
    private int hallId;

    
    private int pengId;
    
    
    /**
     * 房间内玩家列表
     */
    protected GamePlayer[] players;

    /**
     * 房间内观战玩家列表
     */
    private List<GamePlayer> watchingPlayers;

    /**
     * 房间位置使用情况 0 关闭 ； -1 无人； playerId 有人
     */
    protected int[] placesState;

    // 房间当前人数
    protected int playerCount = 0;

	// 房间当前开放的位置数
    private int placesCount = 4;

    // 房间当前是否在被使用
    private boolean isUsing = false;

    // 房主
    private GamePlayer host;

    // 是否在游戏中
    private boolean isPlaying;

    // 房间名称
    private String name;

    // 房间密码
    private String password;

    // 房间是否上锁  0不上锁  1 上锁
//    private byte isLock;

    // 房间类型
    private HallTypeInfo hallType;

    // 当前游戏难度等级
    private GameHardType hardType;

    private byte timeMode;

    // 当前使用的游戏场景（扩展）
    private int gameScene;

    private IEventListener listenGameOver = null;
    // 当前游戏对象
    private BaseGame game;
    
    public int getPengId()
    {    	
    	return pengId;
    }
    

    
    
    
    private boolean isInStageGame;
    private int stageRoundIndex;
    private int stageRoundScore;
    private int stageGameCount;
    private int stageGameIndex;
    private long stageGameTime = 0;
    public void setStageGame(int roundIndex, int roundScore, int GameCount)
    {
    	isInStageGame = true;
    	stageRoundIndex = roundIndex;
    	stageRoundScore = roundScore;
    	stageGameCount = GameCount;
    	stageGameIndex = 0;
    	stageGameTime = TimeUtil.getSysteCurTime().getTime();
    	
//		System.out.println("setStageGame==================true================="+ this.getRoomId() + "======" + isInStageGame);
    	
    	if(game != null)
    	{
			game.setGameState(GameState.GameOver);
    	}   	    	
    }
    
    public boolean getIsInStageGame()
    {
    	return isInStageGame;
    }
        
    public int getStageRoundIndex()
    {
    	return stageRoundIndex;
    }
    
    public int getStageRoundScore()
    {
    	return stageRoundScore;
    }
    
    public int getStageGameCount()
    {
    	return stageGameCount;
    }
    
    public int getStageGameIndex()
    {
    	return stageGameIndex;
    }
        
    public long getStageGameTime()
    {
    	return stageGameTime;
    }
    
    public int getWaitCountIndex()
    {
    	return countIndex;
    }
    
    
    
    
    
    
    
    private boolean isPengYouStart;
    private boolean isPengyouOver;
    private int pengyouRoundIndex;
    private int pengyouRoundCount;        
    private List<Integer> pengyouParaList;    
    
    private String pengQuitUserName;
    private long pengQuitTime;
    
    private PengGame pengGame;
    private List<PengGameDetail> pengDetailList;
    public void setPengyGame(int pengyouRoundIndex, int pengyouRoundCount)
    {
    	this.isPengYouStart = false;
    	this.isPengyouOver = false;
    	this.pengyouRoundIndex = pengyouRoundIndex;
    	this.pengyouRoundCount = pengyouRoundCount;
    	
    	this.pengGame = null;
    	this.pengDetailList = null;
    	
    	
    	if(this.pengyouParaList == null)
    		this.pengyouParaList = new ArrayList<Integer>();
    }
    
    public void setPengyQuit(String pengQuitUserName, long pengQuitTime)
    {
    	this.pengQuitUserName = pengQuitUserName;
    	this.pengQuitTime = pengQuitTime;
    }
    
    public void setPengyouOver(boolean isPengyouOver)
    {
    	this.isPengyouOver = isPengyouOver;
    }
    public void setPengyouParaList(List<Integer> paraList)
    {
    	this.pengyouParaList.clear();
    	for(int i = 0; i<paraList.size(); i++)
    		this.pengyouParaList.add(paraList.get(i));
    	
    	return ;
    }
    
    public int getPengyouRoundIndex()
    {
    	return pengyouRoundIndex;
    }
    
    public int getPengyouRoundCount()
    {
    	return pengyouRoundCount;
    }
    
    
    public String getPengQuitUserName()
    {
    	return pengQuitUserName;
    }
    
    public long getPengQuitTime()
    {
    	return pengQuitTime;
    }
    
    public List<Integer> getPengyouParaList()
    {
    	return pengyouParaList;
    }
    
    
    
//    /**
//     * 初始化 房间
//     * @param roomId
//     * @param hallId
//     */
//    public BaseRoom(int roomId , int hallId)
//    {
//        this.roomType = RoomType.Match;
//        this.roomId = roomId;
//        this.hallId = hallId;
//        players = new GamePlayer[initPlayerCount];
//        placesState = new int[initPlayerCount];
//        watchingPlayers = new ArrayList<GamePlayer>();
//        countIndex = 0;
//        random = new ThreadSafeRandom();
//        RobotAddSecond = random.next(4, 7);//加机器人时间随机
//
//        reset();
//    }
    
    /**
     * 初始化 房间
     * @param roomId
     * @param hallId
     */
    public BaseRoom(int roomId , int hallId, HallTypeInfo hallType)
    {
        this.hallType = hallType;
        this.roomId = roomId;
        this.hallId = hallId;
        initPlayerCount = hallType.getPlayerCountMax();
        players = new GamePlayer[initPlayerCount];
        placesState = new int[initPlayerCount];
        watchingPlayers = new ArrayList<GamePlayer>();
        countIndex = 0;
        random = new ThreadSafeRandom();
        RobotAddSecond = random.next(1, 2);//加机器人时间随机

        isInStageGame = false;
        stageRoundIndex = 0;
    	stageRoundScore = 0;
        stageGameIndex = 0;
        stageGameCount = 0;
        reset();
               
    }

    public void setGame(BaseGame game)
    {
        this.game = game;
    }

    /**
     * 重置房间
     */
    private void reset()
    {
        for (int i = 0; i < initPlayerCount; i++)
        {
            players[i] = null;
            placesState[i] = -1;
        }

        unLock();
        host = null;
        isPlaying = false;
        placesCount = initPlayerCount;
        playerCount = 0;
        hardType = GameHardType.Simple;
    }

    private Timer timer;
    
    private int countIndex;

    private int RobotAddSecond;
    
    private ThreadSafeRandom random;

    /**
     * 开始使用房间
     */
    public void start()
    {
        if (isUsing == false)
        {
            countIndex = 0;
            isUsing = true;
            RobotAddSecond = random.next(1, 4);//加机器人时间随机
            RoomMgr.addUsingRoomCount();
            
                     
            if(this.listenGameOver == null)
            {
            	this.listenGameOver = new GameOverredListener();
            	game.addListener(GameEventType.GameOverred.getValue(), this.listenGameOver);
            }
            
            
          if(getHallType().getHallType() == HallGameType.CONTEST)
          {          		
        	  sendHallStageIndex();
          }
          
        }
    }

    /**
     * 停用房间，房间对象不会释放，将被循环使用
     */
    public void stop()
    {
    	
    	
    	if(this.listenGameOver != null)
        {
        	game.removeListener(GameEventType.GameOverred.getValue(), this.listenGameOver);
        	this.listenGameOver = null;
        }
    	
    	
    	reset();//TODO1
        isUsing = false;

        if (game != null)
        {
            game.stop();
            GameMgr.removeGame(game);
            game = null;
            isPlaying = false;
            password = "";
        }
        RoomMgr.subUsingRoomCount();
        
    }
    
    public void onStartAddRobot()
    { 
    	onStartAddRobot(1000);
    }
    
    public void onStartAddRobot(int delay)
    {    	
    	
    	if(getHallType().getHallType() == HallGameType.SOCIAL)
        {   
    		if(this.getPlayerCount() < hallType.getPlayerCountMax())
        	{
        		if (Config.getValue("add_robot_set") != null
                        && Config.getValue("add_robot_set").equals("1"))
                {
                	if (null == timer) {
                		timer = new Timer();
                        timer.schedule(new AddNPCTask(), delay, 1000);
                	}
                }
        	}
        }    	
    }

    class AddNPCTask extends TimerTask
    {
        @Override
        public void run()
        {
            // robot等待的时间计数，1秒加1
            countIndex++;
            System.out.println("add Robot:=====11==" + countIndex + "==" + RobotAddSecond + "==" + playerCount);
            // 取消添加机器人的timer 1.已经添加了机器人；2.超时；3.玩家进入，没添加机器人就退出，房间无人

            int minCount = playerCount > hallType.getPlayerCountMin() ? playerCount : hallType.getPlayerCountMin();
            int robotMaxCount = random.next(minCount, hallType.getPlayerCountMax());
            if (countIndex > RobotAddSecond || playerCount == 0
                    || playerCount >= robotMaxCount )
            {
        		timer.cancel();
        		timer = null;
                countIndex = 0;
                return;
            }

//            System.out.println("add Robot:=====22==" + countIndex + "==" + RobotAddSecond + "==" + playerCount);
            if (countIndex == RobotAddSecond)
            {
            	if(playerCount < hallType.getPlayerCountMax())
            	{
            		RobotAddSecond = random.next(1, 2);
            		countIndex = 0;
            	}
            		
            	
            	RoomMgr.addAction(new RobotEnterRoomAction(BaseRoom.this));
            }
        }
    }

    

    /**
     * 能否开始游戏 所有玩家都准备后，开始
     * 
     * @return true 可以开始游戏 or false 游戏人数为1
     */
    public boolean canStart()
    {
        int ready = 0;
        for (int i = 0; i < initPlayerCount; i++)
        {
            if (placesState[i] > 0)
                ready++;
        }
        return ready > 1;
    }

    public boolean isLock(){
    	if(password!=null && password.equals(BaseRoom.PWD_LOCK))
    	{
    		return true;
    	}
      return false;
    }
    /**
     * 房间是否设置密码
     * 
     * @return true 有密码，false 无密码
     */
    public boolean isNeedPassword()
    {
        return (password != null) && (password.length() != 0);
    }
    
//    public boolean isLock() {
//		return isLock == 1;
//	}


	/**
     * 能否添加玩家
     * 
     * @return true 能， false 不能
     */
    public boolean canAddPlayer()
    {
        return playerCount < placesCount;
    }

    /**
     * 获取房间内所有游戏的玩家
     * 
     * @return 玩家列表
     */
    public List<GamePlayer> getPlayers()
    {
        LinkedList<GamePlayer> temp = new LinkedList<GamePlayer>();

        synchronized (players)
        {
            for (int i = 0; i < initPlayerCount; i++)
            {
                if (players[i] != null)
                {
                    temp.add(players[i]);
                }
            }
        }

        return temp;
    }

    /**
     * 获取房间内所有
     * 
     * @return
     */
    public List<GamePlayer> getWatchPlayers()
    {
        LinkedList<GamePlayer> temp = new LinkedList<GamePlayer>();

        synchronized (watchingPlayers)
        {
            for (int i = 0; i < watchingPlayers.size(); i++)
            {
                if (players[i] != null)//TODO csf
                {
                    temp.add(players[i]);
                }
            }
        }

        return temp;
    }

    /**
     * 设置房主
     * 
     * @param player
     *            新房主
     */
    public void setHost(GamePlayer player)
    {
        if (host == player)
            return;

        if (host != null)
        {

        }

        host = player;
        //TODO 如果房主发生变化 发包告诉客户端

    }

    // 更新房间信息
//    public void updateRoom(String name, String pwd, int roomType,
//            byte timeMode, int sceneId)
//    {
//        this.name = name;
//        this.password = pwd;
//        this.timeMode = timeMode;
//        this.gameScene = sceneId;
//        placesCount = 4;
//    }

    /**
     * 广播协议
     * 
     * @param pkg
     *            协议包
     */
    public void sendToAll(Packet pkg)
    {
        sendToAll(pkg, null);
    }

    /**
     * 广播协议，排除指定玩家
     * 
     * @param pkg
     *            协议
     * @param except
     *            排除玩家
     */
    public void sendToAll(Packet pkg, GamePlayer except)
    {
        List<GamePlayer> temp = new ArrayList<GamePlayer>();

        synchronized (watchingPlayers)
        {
            temp.addAll(watchingPlayers);
        }

        synchronized (players)
        {
            for (GamePlayer gamePlayer : players.clone())
            {
                temp.add(gamePlayer);
            }
        }

        for (GamePlayer player : temp)
        {
            if (player != null && player != except && player.getOut() != null)
            {
                player.getOut().sendTCP(pkg);
            }
        }
    }

    /**
     * 广播协议
     * 
     * @param pkg
     *            协议包
     */
    public void sendToUsers(Packet pkg)
    {
        sendToUsers(pkg, null);
    }

    /**
     * 广播协议，排除指定玩家,GS只转发不处理
     * 
     * @param pkg
     *            协议
     * @param except
     *            排除玩家
     */
    public void sendToUsers(Packet pkg, GamePlayer except)
    {
        GamePlayer[] temp = null;

        synchronized (players)
        {
            temp = players.clone();
        }

        if (temp != null)
        {
            for (int i = 0; i < temp.length; i++)
            {
                if (temp[i] != null && temp[i] != except)
                {
                    temp[i].getOut().sendTCP(pkg);
                }
            }
        }
    }

    /**
     * 发送消息给房主
     * 
     * @param pkg
     *            数据包
     */
    public void sendToHost(Packet pkg)
    {
        if (host != null)
            host.getOut().sendTCP(pkg);
    }

    /**
     * 发送房间所有位置的状态
     */
    public void sendPlaceState()
    {
        if (host != null)
        {
            Packet pkg = host.getOut().sendRoomUpdatePlacesStates(placesState, true);
            sendToAll(pkg, host);
        }
    }

    /**
     * 发送提示消息
     * 
     * @param type
     *            消息类型
     * @param msg
     *            消息内容
     */
    public void sendMessage(MessageType type, String msg)
    {
        if (host != null)
        {
            Packet pkg = host.getOut().SendMessage(type, msg);
            sendToAll(pkg, host);
        }
    }
    
    /** 发送房间当前的基本信息给所用用户  */
    public void sendRoomBaseInfo(GamePlayer gameplay)
    {
    
    	int hostID = -1; //当前房主
    	int state =  -1; //0 表示无锁  1 表示上锁  2 密码房
    	
    	if(host == null ){
    		//System.out.println("sendRoomBaseInfo:[ host == null][roomId="+roomId+"]");
    		return ;
    	}
    	hostID = host.getUserId();
    	if(password == null || password.isEmpty() || password.equals(BaseRoom.UN_LOCK))
    	{
    		state = 0;
    	}else if(password!=null && password.equals(BaseRoom.PWD_LOCK)){
    		state = 1;
    	}else if(password!=null && password.length()>0){
    		state = 2;
    	}
    	Packet respose = new Packet(UserCmdOutType.ROOM_UPDATE_INFO);
    	
    	respose.putInt(hostID);
    	respose.putByte((byte)state);
    	
    	if(gameplay == null)
    	{
    		sendToAll(respose);
    	}else{
    		gameplay.sendTcp(respose);
    	}
    	
    }
    
    /** 发送房间当前的基本信息给所用用户  */
    public void sendRoomPlayerCoin(GamePlayer gameplay,int addCoin)
    {
    	Packet pkg = gameplay.getOut().sendRoomPlayerCoin(addCoin);
        sendToAll(pkg, gameplay);
    }
    
    /**
     * 更新房间位置状态 请不要直接调用,使用RoomMgr.UpdatePos
     * 
     * @param pos
     *            位置坐标 0-3
     * @param isOpened
     *            true 开启， false 关闭
     * @return true 更新成功， false 失败
     */
    public boolean updatePosUnsafe(int pos, Boolean isOpened)
    {
        if (pos < 0 || pos > 3)
            return false;
        int temp = isOpened ? -1 : 0;
        if (placesState[pos] != temp)
        {
            if (players[pos] != null)
            {
                StringBuffer sb = new StringBuffer();
                logger.error(sb.append(
                                       "[BaseRoom - updatePosUnsafe()] : remove player - ").append(
                                                                                                   players[pos].getPlayerInfo().getUserName()));
                removePlayer(players[pos]);
                // logger.debug("[BaseRoom - updatePosUnsafe()] : remove player [("
                // + players[pos].getPlayerInfo().getUserId() + ") " +
                // players[pos].getPlayerInfo().getUserName() + "] in Room [" +
                // this.getRoomId() + "]");
            }
            placesState[pos] = temp;
            sendPlaceState();
            if (isOpened)
            {
                placesCount++;
            }
            else
            {
                placesCount--;
            }
            return true;
        }
        sendPlaceState();
        return false;
    }

    /**
     * 判断房间是否已经人满(方便观战的扩展）
     * 
     * @return
     */
    public boolean hasPlace()
    {
        for (int i = 0; i < placesState.length; i++)
        {
            if (placesState[i] == -1)
                return true;
        }

        return false;
    }

    /**
     * 添加player到观战列表
     * 
     * @param player
     */
    public boolean addWatchPlayer(GamePlayer player)
    {
        synchronized (watchingPlayers)
        {
            watchingPlayers.add(player);
        }

        // 设置房间信息
        player.setCurrentRoom(this);

        // ------------待定是否广播-----------------------------
        // // 将新加入玩家信息广播给房间内所有人
        // Packet pkg = player.getOut().sendRoomPlayerAdd(player);
        // sendToAll(pkg, player);
        //
        // // 将房间内其它玩家的信息发送给新加入的玩家
        // List<GamePlayer> list = getPlayers();
        // for (GamePlayer p : list)
        // {
        // if (p != player)
        // {
        // player.getOut().sendRoomPlayerAdd(p);
        // }
        // }

        sendPlaceState();

        return true;
    }

    /**
     * 添加player到观战列表
     * 
     * @param player
     */
    public void removeWatchPlayer(GamePlayer player)
    {
        synchronized (watchingPlayers)
        {
            watchingPlayers.remove(player);
        }
    }

    /**
     * 添加玩家到游戏座位
     * 
     * @param player
     *            玩家对象
     */
    public boolean addPlayer(GamePlayer player)
    {
        int index = -1;
        synchronized (players)
        {
            // 寻找空位加入
            for (int i = 0; i < initPlayerCount; i++)
            {
                if ((players[i] == null && placesState[i] == -1))
                {
                    players[i] = player;
                    placesState[i] = player.getUserId();
                    playerCount++;
                    index = i;
                    break;
                }
            }
        }

        
//        System.out.println("setStageGame================addPlayer==================="+ this.getRoomId() + "======" + playerCount + "======" + isInStageGame );
        if(playerCount == 1 && isInStageGame == false)
        {        		
        	onStartAddRobot(1000);
        }
            

        // 如果玩家进入座位
        if (index != -1)
        {
            // 设置房间信息
        	BaseRoom curRoom = player.getCurrentRoom();
			if (curRoom != null) {//不管怎么样 有房间就先从房间移除
				curRoom.removePlayer(player);
			}
            player.setCurrentRoom(this);
            player.setCurrentRoomPos(index);

            // 无房主则当房主，否则为普通玩家
            if (host == null)
                host = player;
            
            if(playerCount == 2) {//如果只有两个在玩游戏。坐对面的需求。第一个位置没人
            	for (int i = 0; i < initPlayerCount; i++) {//第一个位置肯定有人
            		if (players[i]!= null && players[i] != player) {//如果另外一个人不是在对面的情况
            			int j = (i + 2) % initPlayerCount;
            			if (j != index)
            			{
            				players[index] = null;
                			players[j] = player;
                			placesState[index] = -1;
                			placesState[j] = player.getUserId();
                			player.setCurrentRoomPos(j);
            			}
                    }
            	}
            }
            
            player.setIsRobotState(false);
            sendRoomInformation(player);
            sendRoomBaseInfo(player);
            
            
        }
//        else//为了避免玩家通过找房间进入，而出现大于人数多于房间最大人数，就是会出现观战，但现在没用到
//            addWatchPlayer(player);

        return index != -1;
    }

    /**
     * send all player information to new player.
     * 
     * @param player
     */
    public void sendRoomInformation(GamePlayer player)
    {
        // broadcast new player to all other player in the room.
        Packet pkg = player.getOut().sendRoomPlayerAdd(player, true);
        sendToAll(pkg, player);

        // broadcast all other player to the new player.
        List<GamePlayer> list = getPlayers();
        for (GamePlayer p : list)
        {
            if (p != player) {
                player.getOut().sendRoomPlayerAdd(p, true);
            }            
        }

        // broadcast player state to new player.
        sendPlaceState(); 
    }

    /**
     * 添加玩家到游戏座位（加机器人的方法）
     * 
     * @param player
     *            玩家对象
     */
    public boolean addNpcPlayer(GamePlayer player)
    {
        int index = -1;
        if (playerCount < hallType.getPlayerCountMax())
        {
            synchronized (players)
            {
                // 寻找空位加入
                for (int i = 0; i < initPlayerCount; i++)
                {
                    if ((players[i] == null && placesState[i] == -1))
                    {
                        players[i] = player;
                        placesState[i] = player.getUserId();
                        playerCount++;
                        index = i;
                        break;
                    }
                }
            }
            
            // 如果玩家进入座位
            if (index != -1)
            {
	            // 设置房间信息
            	BaseRoom curRoom = player.getCurrentRoom();
    			if (curRoom != null) {//不管怎么样 有房间就先从房间移除
    				curRoom.removeRobotPlayer(player);
    			}
	            player.setCurrentRoom(this);
	            player.setCurrentRoomPos(index);
	
	            if (playerCount == 1)
	            {//如果房间只有机器人, 机器人可以随便坐
	            	int position = new Random().nextInt(hallType.getPlayerCountMax());
	            	if (position != index)
	            	{
	            		synchronized (players)
	                    {
		            		placesState[index] = -1;
			            	players[index] = null;
			            	
		            		placesState[position] = player.getUserId();
			            	players[position] = player;
			            	player.setCurrentRoomPos(position);
	                    }
	            	}
	            }
	            else if(playerCount == 2) {//如果只有两个在玩游戏。坐对面的需求。第一个位置没人
	            	
	            	
	            	synchronized (players)
                    {
		            	for (int i = 0; i < initPlayerCount; i++) {//第一个位置肯定有人
		            		if (players[i]!= null && players[i] != player) {//如果另外一个人不是在对面的情况
		            			
		            			int temp = initPlayerCount / 2;		            			
		            			int	j = (i + temp) % initPlayerCount;
		            			
		            			if (j != index)
		            			{
		            				players[index] = null;
		                			players[j] = player;
		                			placesState[index] = -1;
		                			placesState[j] = player.getUserId();
		                			player.setCurrentRoomPos(j);
		            			}
		                    }
		            	}
                    }
	            }
	            
	            // 将新加入玩家信息广播给房间内所有人 
	            Packet pkg = player.getOut().sendRoomPlayerAdd(player, true);
	            sendToAll(pkg, player);
	            sendPlaceState();
            }
        }
        
        
//        String strList = "";
//        for(GamePlayer play : players)
//        	if(play != null)
//        		strList += "=======" + play.getUserId();
        	
//        System.out.println("addNpcPlayer====================================" + roomId + "========" + strList);
        
        return index != -1;
    }

    /**
     * 添加玩家到指定位置
     * 
     * @param player
     * @param place
     * @return
     */
    public boolean addPlayerToPlace(GamePlayer player, int place)
    {
        if (place >= initPlayerCount)
            return false;

        synchronized (players)
        {
            if (players[place] == null && placesState[place] == -1)
            {
                players[place] = player;
                placesState[place] = player.getUserId();
                playerCount++;
                return true;
            }
        }

        return false;
    }

    /**
     * 移除房间内的玩家
     * 
     * @param player
     *            需移除的玩家
     * @return
     */
    public boolean removePlayer(GamePlayer player)
    {
        return removePlayer(player, true, false, false, 0);
    }

    public boolean removeRobotPlayer(GamePlayer player)
    {
        return removePlayer(player, true, false, false, 0);
    }
    
    /**
     * 移除玩家
     * 
     * @param player
     *            待移除玩家
     * @param isKick
     *            true 被踢，false 未被踢
     * @return
     */
    public boolean removePlayer(GamePlayer player, Boolean isTrustee, Boolean isKick, boolean isStageGame, int pengQuitType)
    {

//    	if(isStageGame)
    		System.out.println("removePlayer==================" + isStageGame);
    	
//    	Exception e = new Exception("this is a log");
//    	e.printStackTrace();
    	
    	
        int index = -1;
        int robotCount = 0;
        
        synchronized (players)
        {
            for (int i = 0; i < initPlayerCount; i++)
            {
                // 移除玩家
                if (players[i] == player)
                {
                	                	
                	if(hallType.getHallType() == HallGameType.PENGYOU && game.getGameState().isInPlay() && pengQuitType == 0)
                	{             
                		return false;
                	}
                	                	
                	if(isTrustee && game.getGameState().isInPlay())
                	{
                		RoomMgr.handlePlayerTrustee(this, player, true);
                		return false;
                	}
                	
                	
                	
                	
                    players[i] = null;
                    placesState[i] = -1;
                    playerCount--;
                    index = i;
                    // 在这里更改玩家的游戏状态
                    player.setPlaying(false);
                    if (player.getIsRobot()) {
                    	//这里移除机器人的时候要改变他的游戏状态
                    	Player robotPlayer = RobotMgr.getRobotByID(player.getUserId());
                    	if (null != robotPlayer) {
                    		robotPlayer.setIsPlaying(false);//重点
                    		robotPlayer.setPlayerState(PlayerState.Idle);//与下一句位置关系
                    	}
                        game.removePlayer(player);
                    }
                    //break;
                }
                
                if(players[i] != null && players[i].getIsRobot())
                {
                	robotCount++;
                }
            }
        }

        if (index != -1)
        {
            updatePosUnsafe(index, true);
            player.setCurrentRoom(null);
//            player.setCurrenetHall(null);

            Packet pkg = player.getOut().sendRoomPlayerRemove(player, isKick, isStageGame, pengQuitType);
            sendToAll(pkg);
           
            // 发送被剔出房间的提示
            if (isKick)
            {
                player.getOut().SendMessage(
                                            MessageType.CHATERROR,
                                            LanguageMgr.getTranslation("Game.Server.SceneGames.KickRoom"));
            }

            // 游戏中移除玩家
            if (game != null)
            {
                game.removePlayer(player);
                game.sendUpdatePlayerState();
                // 判断房间里是否只有robot玩家，如果是，移除
                
//                System.out.println("removePlayer============================================" + playerCount + "====" + robotCount);
                if (robotCount >= playerCount)
                {
                    synchronized (players)
                    {
                        GamePlayer p = null;
                        for (int i = 0; i < initPlayerCount; i++)
                        {
                            // 移除玩家
                            if (players[i] != null)
                            {
                                if (players[i].getIsRobot())
                                {
                                	//这里移除机器人的时候要改变他的游戏状态
                                	Player robotPlayer = RobotMgr.getRobotByID(players[i].getUserId());
                                	if (null != robotPlayer) {
                                		robotPlayer.setIsPlaying(false);
                                		robotPlayer.setPlayerState(PlayerState.Idle);//与下一句位置关系
                                		robotPlayer.getPlayerDetail().setPlaying(false);
                                		robotPlayer.getPlayerDetail().setCurrentRoom(null);
                                	}
                                    game.removePlayer(players[i]);
                                    players[i] = null;
                                    placesState[i] = -1;
                                    playerCount--;
                                }
                                else
                                {
                                    p = players[i];
                                }
                                //break;
                            }
                        }

                        // if the player is the last one,update his state to Idle.
                        if (p != null)
                        {
                            Player player2 = game.getPlayerById(p.getUserId());
                            if (player2 != null && game!=null)
                            {
                                player2.setPlayerState(PlayerState.Idle);
                                game.sendShakePrepare();
                                game.sendUpdatePlayerState();
                            }
                        }
                    }
                }
            }

            //房间是否解锁
            boolean isUnLock = false;
            // change house host.
            if (host == player)
            {
            	
            	isUnLock = true;
                if (playerCount > 0)
                {
                    for (int i = 0; i < initPlayerCount; i++)
                    {
                        if (players[i] != null)
                        {
                            setHost(players[i]);
                            break;
                        }
                    }
                }
                else
                {
                    host = null;
                }
            }
            // 房间已经无人，更新为空闲状态
            if (playerCount == 0)
                stop();
            else  if (playerCount == 1 && isStageGame == false)
            {
            	isUnLock = true;            	
                onStartAddRobot();
            }
            
            //通知客户端 修改房间基本信息(房主和锁)
            if(isUnLock){
            	unLock();
            	sendRoomBaseInfo(null);
            }
        }
        
        return index != -1;
    }

    /**
     * 重置房间信息
     */
    public void restRoomInfo()
    {
        gameScene = 10000;
        hardType = GameHardType.Simple;
        if (host != null)
        {
            Packet pkg = host.getOut().sendRoomChange(this);
            sendToAll(pkg, host);
        }
    }

    /**
     * 处理游戏指令
     * 
     * @param packet
     *            游戏指令
     */
    public void processData(Packet packet)
    {
        if (game != null)
        {
            game.processData(packet);
        }
    }

    public String toString()
    {
        return String.format("Id:{%d},player:{%d},game:{%s},isPlaying:{%b}",
                             roomId, playerCount, game != null ? game.toString() : "", isPlaying);
    }

    /**
     * 移除所有玩家对象
     */
    public void removeAllPlayer()
    {
        for (int i = 0; i < initPlayerCount; i++)
        {
            if (players[i] != null)
            {
                // addAction(new ExitRoomAction(this, players[i]));
                RoomMgr.addAction(new ExitRoomAction(this, players[i], false, 0));
            }
        }
    }

    /**
     * @return the roomId
     */
    public int getRoomId()
    {
        return roomId;
    }
    
    public int getHallId()
    {
    	return hallId;
    }

    /**
     * @return the placesState
     */
    public int[] getPlacesState()
    {
        return placesState;
    }

    /**
     * @return the playerCount
     */
    public int getPlayerCount()
    {
        return playerCount;
    }

    /**
     * @return the placesCount
     */
    public int getPlacesCount()
    {
        return placesCount;
    }

    /**
     * @return the isUsing
     */
    public boolean isUsing()
    {
        return isUsing;
    }

    public void setIsUsing(boolean isUsing)
    {
        this.isUsing = isUsing;
    }

    /**
     * @return the host
     */
    public GamePlayer getHost()
    {
        return host;
    }

    /**
     * @return the isPlaying
     */
    public boolean isPlaying()
    {
        return isPlaying;
    }

    /**
     * @param isPlaying
     *            the isPlaying to set
     */
    public void setPlaying(boolean isPlaying)
    {
        this.isPlaying = isPlaying;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String pws) 
    {
    	if(password !=null && password.equals(pws)  ){
    		return ;
    	}
    	password = pws;
     
	}
    //房间解锁
    public void unLock(){
    	password = BaseRoom.UN_LOCK;
    }


    /**
     * @return the hardType
     */
    public GameHardType getHardType()
    {
        return hardType;
    }

    /**
     * @return the timeMode
     */
    public byte getTimeMode()
    {
        return timeMode;
    }

    /**
     * @param timeMode
     *            the timeMode to set
     */
    public void setTimeMode(byte timeMode)
    {
        this.timeMode = timeMode;
    }

    /**
     * @return the mapId
     */
    public int getGameScene()
    {
        return this.gameScene;
    }

    /**
     * @param mapId
     *            the mapId to set
     */
    public void setGameScene(int gameScene)
    {
        this.gameScene = gameScene;
    }

    /**
     * @return the game
     */
    public BaseGame getGame()
    {
        return game;
    }

    /**
     * @param hardType
     *            the hardType to set
     */
    public void setHardType(GameHardType hardType)
    {
        this.hardType = hardType;
    }

    /**
     * 开始游戏（没有用到）
     * 
     * @param game
     *            游戏
     */
    public void StartGame()
    {
        if (game.getGameState() == GameState.Prepared)
        {
            // 发送空协议，通知玩家游戏开始
//            for (GamePlayer player : getPlayers())
//            {
//                player.getOut().sendStartGame();
//            }
        	//System.out.println("====================StartGame==================");
//            for(GamePlayer p :getPlayers()) {
            	//System.out.println("StartGamePlayers=========" + p.getPlayerInfo().getUserName());
//            }

//            for(Player p :game.getTurnQueue()) {
            	//System.out.println("StartGameTurnQueue=========" + p.getPlayerDetail().getPlayerInfo().getUserName());
//            }
        	//System.out.println("=====================StartGame=================");
            
//            game.start();

            isPlaying = true;
            
//            game.addListener(GameEventType.GameOverred.getValue(),
//                    new GameOverredListener());
            
         
//            game.addListener(GameEventType.GameStopped.getValue(),
//                             new GameStopedListener());
        }
    }

    
    
    
 // 游戏停止事件监听类
    private class GameOverredListener implements IEventListener
    {
        @Override
        public void onEvent(Object EventArgs)
        {

        	switch(hallType.getHallType())
        	{
        	    
        		case HallGameType.SOCIAL:	        		
        		
        			game.StartPrepareGame();
        		
        			break;        		
	        	case HallGameType.PENGYOU:	        		
	        		
	        		
	        		if(isPengyouOver && pengyouRoundIndex == 0)
	        			return;
	        			        		
	        		pengyouRoundIndex++;
	        		if(isPengYouStart == false)
	        		{
	        			isPengYouStart = true;
	        			savePengGame();
	        			PengConfigInfo config = HallMgr.getPengConfig(1, pengyouParaList.get(0));
	        			getHost().getPlayerInfo().setCardCount(getHost().getPlayerInfo().getCardCount() - config.getPara1());   	        			
	        			System.out.println("continuePengYouGame================================================" + getHost().getPlayerInfo().getCardCount());
	        		}
	        			
	        			        		
	        		if(pengyouRoundIndex > pengyouRoundCount || isPengyouOver)
            		{
            			savePengGameDetail();
            			updatePengGame();
            			finishPengYouGame();
            			sendPengYouGameState();
            			System.out.println("finishPengYouGame============================================" + pengyouRoundIndex + "===========" + pengyouRoundCount);     
            		}
            		else
            		{
            			savePengGameDetail();
            			game.StartPrepareGame();     
            		}
	        		
	        		
	        		game.saveGameRecordList("record/" + getPengId() + "_" + pengDetailList.size() +".txt");
            		System.out.println("pengyouRoundIndex================================================" + pengyouRoundIndex + "===============" + pengyouRoundCount);
            		
            		
	        		break;
	        	case HallGameType.CONTEST:
	        			        		
	        		if(isInStageGame)
	            	{
	            		sendHallStageIndex();
	        	    	    	    	
	            		stageGameIndex++;
	            		if(stageGameIndex >= stageGameCount)
	            		{
	            			finishStageGame();		
	            			HallMgr.checkEndGame(getHallId(), 3000);
	            		}
	            		else
	            		{
	            			game.StartPrepareGame();
	            		}
	            	}
	        		BaseHall hall = HallMgr.getHallById(hallId);      
	        		game.saveGameRecordList("contest/" + hall.getCurrentStage().getStageId() + "_" + stageGameIndex +".txt");
	        		break;
        		default:
        			break;

        	}
        }

		@Override
		public void onEvent(int para1, int para2) {
			
			System.out.println("onEvent================" + para1);
		}
    }
    
    public void finishStageGame()
    {
    	BaseHall hall = HallMgr.getHallById(hallId);        
		hall.addStageGameFinishRoom();
		if(game != null)
			game.setGameState(GameState.Wait);
			        
		
		for(int i=0;i<players.length;i++) {	        
			if(players[i] != null)
				RoomMgr.exitRoom(players[i].getCurrentRoom(), players[i], true, 0);	        			
		}
    }
    
    public void finishPengYouGame()
    {
    	isInStageGame = false;
		if(game != null)
			game.setGameState(GameState.GameOver);	
		
		for(int i=0;i<players.length;i++) {
			if(players[i] != null)
				RoomMgr.exitRoom(players[i].getCurrentRoom(), players[i], true, 2);	        			
		}
    }
    
    public void savePengGame()
    {

    	String strPlayerList = "_";
    	Collection<Player> list = game.getPlayers().values();
    	for (Player player : list)
        {
    		strPlayerList += player.getPlayerID() + "_";
        }
    	

        this.pengGame = new PengGame();
        this.pengGame.setRoomid(this.getRoomId());
        this.pengGame.setGamedate(new Timestamp(System.currentTimeMillis()));
        this.pengGame.setPlayerlist(strPlayerList);
    	PengGameBussiness.addPengGame(this.pengGame);
    	
    	pengId = this.pengGame.getId();    	
    	this.pengDetailList = new ArrayList<PengGameDetail>();
    }
    
    public void savePengGameDetail()
    {
    	    	    	
    	List<PengGameScore> GameList = new ArrayList<PengGameScore>();
    	Collection<Player> list = game.getPlayers().values();
    	for (Player player : list)
        {
    		PengGameScore score = new PengGameScore();
    		score.setName(player.getPlayerDetail().getPlayerInfo().getUserName());
    		score.setScore(player.getGameOverCoin());
    		GameList.add(score);
        }
    	
    	JSONArray jsonArray = JSONArray.fromObject(GameList);  
    	
    	PengGameDetail peng = new PengGameDetail();
    	peng.setPengid(pengId);
    	peng.setGamedate(new Timestamp(System.currentTimeMillis()));
    	peng.setGamelist(jsonArray.toString());
    	PengGameBussiness.addPengGameDetail(peng);
    	this.pengDetailList.add(peng);
    }
    
    public void updatePengGame()
    {
    	
    	List<PengGameScore> GameList = new ArrayList<PengGameScore>();
    	Collection<Player> list = game.getPlayers().values();
    	for (Player player : list)
        {
    		PengGameScore score = new PengGameScore();
    		score.setName(player.getPlayerDetail().getPlayerInfo().getUserName());
    		score.setScore(player.getPlayerDetail().getPlayerInfo().getPengYouCoins());
    		GameList.add(score);
        }
    	
    	JSONArray jsonArray = JSONArray.fromObject(GameList);  
//    	this.pengGame.setScorelist(jsonArray.toString());
    	PengGameBussiness.updateCharmValve(pengGame.getId(), "", jsonArray.toString());
    }
    
    public void continuePengYouGame(GamePlayer player, int index)
    {

    	this.pengyouParaList.set(0, index);    	
    	if(GameMgr.isPengYouCreateRoom(hallType.getGameType(), this, player, this.pengyouParaList))
    	{
    		
    		System.out.println("continuePengYouGame================================================" + getHost().getPlayerInfo().getCardCount() + "===============" + pengyouParaList.get(0));
    		
    		PengConfigInfo config = HallMgr.getPengConfig(1, pengyouParaList.get(0));
			getHost().getPlayerInfo().setCardCount(getHost().getPlayerInfo().getCardCount() - config.getPara1());   
    		getGame().StartPrepareGame();
    		    		
    		System.out.println("continuePengYouGame================================================" + getHost().getPlayerInfo().getCardCount());
    	}
    	
    	Packet pkg = new Packet(UserCmdOutType.PENGYOU_GAMECONTINUE);
    	pkg.putInt(getPengyouRoundCount());
		pkg.putInt(getPengyouRoundIndex());
    	this.sendToAll(pkg);    	
    }
    
    public void sendPengYouGameState()
    {
    	
		
		Packet pkg = new Packet(UserCmdOutType.PENGYOU_GAMESTATE);
		
		if(this.pengGame == null)
		{
			pkg.putInt(0);
		}
		else
		{
			pkg.putInt(getRoomId());
			pkg.putInt(getPengId());
			pkg.putStr(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.pengGame.getGamedate()));
			
			pkg.putInt(getPengyouRoundCount());
			pkg.putInt(getPengyouRoundIndex());
			
			
			
			List<GamePlayer> playerList = getPlayers();
	    	pkg.putInt(playerList.size());
	        for (int i = 0;i < playerList.size(); i++) 
	        {            	      
	        	GamePlayer gp = playerList.get(i);
	        	
	        	pkg.putInt(gp.getUserId());
	        	pkg.putStr(gp.getPlayerInfo().getUserName());            	
	        	pkg.putInt(gp.getPlayerInfo().getPengYouCoins());     
	        	pkg.putInt(gp.getPengWinCount());
	        	pkg.putInt(gp.getPengLoseCount());
			}
		}
		                        	
		this.sendToAll(pkg);
        
        
    }
    
    
    public void sendHallStageIndex()
    {
    	BaseHall hall = HallMgr.getHallById(hallId);        
    	synchronized (players){			
    		
    		for (GamePlayer gamePlayer : players.clone())
            {
    			if(gamePlayer != null && hall.getCurrentStage() != null)
    			{
    				int iIndex = UserTopMgr.getAllStageIndex(hall.getCurrentStage().getStageId(), gamePlayer.getUserId()) + 1;
        			gamePlayer.getOut().sendStageIndex(gamePlayer.getPlayerInfo().getCharmValve(), iIndex, hall.getNextStageRound().getPlayerCount());   
    			}    			
            }    			 
    	}
    }
    
    // 游戏停止事件监听类
//    private class GameStopedListener implements IEventListener
//    {
//        @Override
//        public void onEvent(Object EventArgs)
//        {
//            if (game != null)
//            {
//                game.removeListener(GameEventType.GameStopped.getValue(), this);
//                game = null;
//            }
//
//            isPlaying = false;
//            restRoomInfo();
//        }
//
//		@Override
//		public void onEvent(int para1, int para2) {
//			
//		}
//
//    }

    // 房间重置时间监听类
//    private class GameRoomRestListener implements IEventListener
//    {
//        @Override
//        public void onEvent(Object EventArgs)
//        {
//            if (game != null)
//            {
//                game.removeListener(GameEventType.RoomRest.getValue(), this);
//            }
//
//            gameScene = 0;
//        }
//
//		@Override
//		public void onEvent(int para1, int para2) {
//			// TODO Auto-generated method stub
//			
//		}
//    }

    /**
     * 是否满足房间等级限制
     * 
     * @param copyId
     *            副本id
     * @param levelLimit
     *            等级限制，输出参数
     * @return true 满足、false 不满足
     */
    public boolean isGradeAchieved(int copyId, WrapInteger levelLimit)
    {
        if (copyId != 0 && copyId != 10000)
        {
            if (host != null)
                return host.getPlayerInfo().getLevel() >= levelLimit.getParam();
        }
        return true;
    }
    
    /**
	 * 进入房间，创建游戏
	 * 
	 * @param room
	 */
	public void createGame() {
		// 获取当前房间的玩家列表
		List<GamePlayer> list = new ArrayList<GamePlayer>(this.getPlayers());
		BaseGame game = GameMgr.createGame(this,
				hallId, list, 
				this.getTimeMode(), hallType);
		setGame(game);
		//System.out.println("test====roomId===" + roomId + "====HallId=== " + hallId + "====");
		start();
	}
	
	/**
	 * 玩家是否可以呆在这个房间
	 * 如果玩家不能呆在大厅，或者玩家金币数小于最低值,则返回 false
	 * @param gamePlayer
	 * @return boolean 
	 */
	public boolean canStayHere(GamePlayer gamePlayer) {
		
		boolean isStay = true;
		if(null == HallMgr.getHallById(hallId) || null == gamePlayer 
    			|| null == gamePlayer.getPlayerInfo()) {
			isStay = false;
		}
		
		
		if(hallType.getHallType() == HallGameType.PENGYOU)
    		return true;
		
		
    	int playerCoins = gamePlayer.getPlayerInfo().getTempCoins() + gamePlayer.getPlayerInfo().getCoins();
    	//玩家金币数小于最低值
    	if(null == HallMgr.getHallById(hallId) || 
    			playerCoins < hallType.getForcedExitCoins()) {
    		
			if(playerCoins > 0)
	    	{
				isStay = true;
//	    		int tableCoin = getHallType().getLowestCoins();
//	    		isStay = gamePlayer.updateTempCoin(tableCoin, hallType.getForcedExitCoins()) >= tableCoin;
//	    		if(isStay && game != null)
//	    			game.SendGamePlayerCoin(gamePlayer);
	    	}
			else
			{
				isStay = false;
			}	
    	}
    	
    	if(isStay && this.game != null)
    		this.game.onAddGame(gamePlayer);
    	
		return isStay;
	}

	public HallTypeInfo getHallType() {
		return hallType;
	}

	public void setHallType(HallTypeInfo hallType) {
		this.hallType = hallType;
	}
	
	public boolean ishavePlayer(GamePlayer player) {
		boolean isHave = false;
		synchronized (players)
        {
			for(int i=0;i<players.length;i++) {
				if(players[i] == player) {
					isHave = true;
				}
			}
        }
		return isHave;
	}
}
