/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.event.BaseMultiEventSource;
import com.citywar.game.action.IAction;
import com.citywar.game.action.StartPrepareAction;
import com.citywar.game.action.UserShakeAction;
import com.citywar.gameobjects.Player;
import com.citywar.manager.HallMgr;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.GameEventType;
import com.citywar.type.GameState;
import com.citywar.type.HallGameType;
import com.citywar.type.PlayerState;
import com.citywar.type.UserCmdOutType;
import com.citywar.util.Config;
import com.citywar.util.FileOperate;
import com.citywar.util.ThreadSafeRandom;
import com.citywar.util.TickHelper;

/**
 * 游戏基本类
 * 
 * @author Dream
 * @date 2011-12-15
 * @version
 * 
 */
public class BaseGame extends BaseMultiEventSource implements Runnable
{
    private static Logger logger = Logger.getLogger(BaseGame.class.getName());

    /**
     * 房间ID
     */
    private int id;

    /**
     * 游戏类型（扩展）
     */
    protected int gameType;

    /**
     * 游戏所处大厅类型
     */
    protected HallTypeInfo hallType;

    protected int hallGameType = 0;
    /**
     * 游戏时间类型（扩展）
     */
    protected int timeType;
    
    
    protected Double startGameDelay;

    private int m_disposed = 0;

    protected List<String> gameRecordList;
    
    public int getId()
    {
        return this.id;
    }

    public int getGameType()
    {
        return this.gameType;
    }

    public int getHallGameType()
    {
    	return hallGameType;
    }
    
    public void setHallGameType(int hallGameType)
    {
    	this.hallGameType = hallGameType;
    }
    
    /**
     * 回合计数
     */
    protected int turnIndex;

    /**
     * 游戏状态
     */
    protected GameState gameState;

    /**
     * 游戏中的玩家
     */
    protected HashMap<Integer, Player> players;

    protected ThreadSafeRandom random = new ThreadSafeRandom();

    protected Player currentTurnPlayer;

    protected Player lastTurnPlayer;
    
    private List<IAction> actions;

    private long m_passTick;

    /**
     * 正在游戏中的玩家
     */
    protected List<Player> turnPlayerQueue;

    private BaseRoom baseRoom;
    
    private int hallId;// 大厅的id
    
    private boolean isResetGame;
    
    private boolean isAutoStart;
    
    private int maxNoOperateTime;
    
    protected boolean isChangeTurn;
    
    private boolean isContinueGame;
    /**
     * 加上大厅类型，处理多种大厅的多种游戏
     * @param id
     * @param hallId
     * @param roomType
     * @param gameType
     * @param timeType
     * @param hallType
     */
    public BaseGame(int id, int hallId, BaseRoom baseRoom, int gameType, int timeType, HallTypeInfo hallType, 
    		boolean isAutoStart, boolean isResetGame, int maxNoOperateTime, boolean isChangeTurn, boolean isContinueGame)
    {
        this.id = id;
        this.gameType = gameType;
        this.timeType = timeType;
        this.hallId = hallId;
        this.baseRoom = baseRoom;        
        this.hallType = hallType;
        
        
        this.isResetGame = isResetGame;					//玩家中途退出，是否重置游戏
        this.isAutoStart = isAutoStart;
        this.maxNoOperateTime = maxNoOperateTime;		//没有操作得次数
        this.isChangeTurn = isChangeTurn;					//自动开始游戏
        this.isContinueGame = isContinueGame;			//重新登录得时候，是否加入上次游戏
        
        
        this.players = new HashMap<Integer, Player>();
        this.turnPlayerQueue = new LinkedList<Player>();
        this.actions = new ArrayList<IAction>();
        this.gameRecordList = new ArrayList<String>(); 
        
    }

    public void setTurnIndex(int turnIndex)
    {
        this.turnIndex = turnIndex;
    }

    public void addGameRecordList(Packet jsonArray)
    {
    	this.gameRecordList.add(jsonArray.getJsonDate());
    }
    
    
    public void saveGameRecordList(String strPath)
    {    	
    	if(strPath != null && !strPath.equals(""))
    	{
    		String thirdName = Config.getValue("server.gameRecordPath");	
    		FileOperate.createFile(thirdName + strPath, this.gameRecordList.toString());
    	}
    }
    
    /**
     * 
     * @return
     */
    public int getCurrentActionCount()
    {
        return this.CurrentActionCount;
    }

    public BaseRoom getBaseRoom()
    {
        return this.baseRoom;
    }
    
    public int getHallId()
    {	
    	return this.hallId;
    }

    public HashMap<Integer, Player> getPlayers()
    {
        return this.players;
    }

    public int getPlayerCount()
    {
        synchronized (this.players)
        {
            return this.players.size();
        }
    }

    /**
     * 游戏回合计数
     */
    public int getTurnIndex()
    {
        return this.turnIndex;
    }

    /**
     * 当前游戏状态
     */
    public GameState getGameState()
    {
        return this.gameState;
    }

    /**
     * 设置当前游戏状态
     */
    public void setGameState(GameState state)
    {
        this.gameState = state;
    }

    public boolean getIsResetGame()
    {
        return this.isResetGame;
    }
    
    public boolean getIsContinueGame()
    {
        return this.isContinueGame;
    }


    
    
    
    public boolean isPlaying()
    {
        return (GameState.Playing == gameState || gameState == GameState.GameOver) ? true
                : false;
    }

    public List<Player> getTurnQueue()
    {
		synchronized (this.turnPlayerQueue)
	    {
	        List<Player> turnPlayerQueueTemp = new ArrayList<Player>();
	        for (Player tempPlayer : turnPlayerQueue)
	        {
	        	turnPlayerQueueTemp.add(tempPlayer);
	        }
	        return turnPlayerQueueTemp;
	    }
    }

    /**
     * 添加游戏玩家
     * 
     * @param gp
     * @param fp
     */
    public void addGamePlayer(int userID, Player fp)
    {
        synchronized (this.players)
        {
            this.players.put(userID, fp);
            this.onAddGame(fp.getPlayerDetail());
        }
    }

    public void onAddGame(GamePlayer fp)
    {
    	
    }
    /**
     * 添加玩家到游戏对战列表
     * 
     * @param player
     */
    public void addPlayer(Player player)
    {
        synchronized (this.turnPlayerQueue)
        {
            this.turnPlayerQueue.add(player);
            
//            if(this.turnPlayerQueue.size() > 3)
            	System.out.println("addPlayer====================================================" + this.getBaseRoom().getRoomId() +
            			"=================================" + this.turnPlayerQueue.size() + "================================="+ player.getPlayerID()
            			+ "================================="+ player.getPlayerDetail().getPlayerInfo().getUserName());
        }
    }

    public Player removePlayer(GamePlayer gp)
    {
        Player player = null;
        synchronized (this.players)
        {
            player = this.players.remove(gp.getUserId());
        }

        synchronized (this.turnPlayerQueue)
        {
            if (player != null)
            {
                this.turnPlayerQueue.remove(player);
                
                System.out.println("removePlayer==========================================================" + this.getBaseRoom().getRoomId() + 
                		"===========================" + this.turnPlayerQueue.size() + "================================="+ player.getPlayerID());
            }
        }
        
        return player;
    }

    public void clearPlayer()
    {
        synchronized (this.turnPlayerQueue)
        {
            this.turnPlayerQueue.clear();            
        }
    }
    
    public boolean HasPlayer()
    {
        return this.players.size() > 0;
    }

    public ThreadSafeRandom getRandom()
    {
        return this.random;
    }

    public Player getCurrentTurnPlayer()
    {
        return this.currentTurnPlayer;
    }

    public Player getLastTurnPlayer()
    {
        return this.lastTurnPlayer;
    }

    /**
     * 直接返回players.get(id)即可，不用遍历
     */
    public Player findPlayer(int id)
    {
        synchronized (this.players)
        {
            return this.players.get(id);
        }
    }

    public Player findNextLiving()
    {
        if (this.turnPlayerQueue.size() == 0)
            return null;

        int start = this.random.next(this.turnPlayerQueue.size());
        currentTurnPlayer = turnPlayerQueue.get(start);
        lastTurnPlayer = currentTurnPlayer;
        return currentTurnPlayer;
    }

    public void checkState(int delay)
    {
    }

    public Player getPlayerById(int index)
    {
        return this.players.get(index);
    }

    public Player FindRandomPlayer()
    {
        return FindRandomPlayer(0, null);
    }

    public Player FindRandomPlayer(int exceptTeam)
    {
        List<Player> list = new ArrayList<Player>();
        Player Player = null;

        for (Player player : this.players.values())
        {
            list.add(player);
        }

        int next = random.next(0, list.size());

        if (list.size() > 0)
        {
            Player = list.get(next);
        }

        return Player;
    }

    /**
     * 随机查找一个玩家
     * 
     * @param exceptTeam
     *            0表示从所有玩家中随机
     * @param exceptLiving
     * @return
     */
    public Player FindRandomPlayer(int exceptTeam, Player exceptLiving)
    {
        List<Player> list = new ArrayList<Player>();
        Player Player = null;

        for (Player player : this.players.values())
        {
            if (player != exceptLiving)
            {
                list.add(player);
            }
        }

        int next = random.next(0, list.size());

        if (list.size() > 0)
        {
            Player = list.get(next);
        }

        return Player;
    }

    public void start()
    {
        onGameStarted();
    }

    public void stop()
    {
        onGameStopped();
    }

    public void sendShakePrepare()
    {
    	
    }
    
    public void sendNotShakeUserPrepare() {
    	
    }
    
    public void changePlayerRobotState(int userId, boolean isRobotState)
    {
        
    }
    
    public void sendShakeGame(Player player)
    {
    	
    }
    
    public void sendGameNextTurn(Player living, BaseGame game)
    {
    	
    }
    
    public void sendGameNextTurn(Player living, BaseGame game, Player player) {
    	
    }
    
    public boolean CanAddPlayer()
    {
        return false;
    }

    public void Pause(int time)
    {
        m_passTick = Math.max(m_passTick, TickHelper.GetTickCount() + time);
    }

    public void Resume()
    {
        m_passTick = 0;
    }

    public void ClearAllAction()
    {
        synchronized (this.actions)
        {
            this.actions.clear();
        }
    }

    public void AddAction(IAction action)
    {
        synchronized (this.actions)
        {
            this.actions.add(action);
        }
    }

    public void AddAction(List<IAction> actions)
    {
        synchronized (this.actions)
        {
            this.actions.addAll(actions);
        }
    }

    private int CurrentActionCount = 0;

    public void WriteLog(String logs)
    {
        // ThreadPool.QueueUserWorkItem(new WaitCallback(Log), logs);
    }

    public void Log(Object state)
    {
        String logs = (String) state;
        logger.warn(logs);
    }

    public List<Player> getAllPlayers()
    {
        List<Player> list = new ArrayList<Player>();
        synchronized (this.players)
        {
            list.addAll(this.players.values());
        }

        return list;
    }

    public void sendToAll(Packet pkg, GamePlayer except)
    {
        List<Player> temp = getAllPlayers();
        for (Player p : temp)
        {
            if (p.getPlayerDetail() != except)
            {
                p.getPlayerDetail().sendTcp(pkg);
            }
        }
    }

    /**
     * 游戏准备
     */
    public void prepare()
    {
        gameState = GameState.Prepared;
    }

    public void SendGamePlayerCoin(GamePlayer fp)
    {
    	
    }
    
    public void SendGameStart(int wager)
    {
        
    }

    public void SendGameContinue(GamePlayer playerCon)
    {
        
    }
    
    public void SendPlayerRobotState(GamePlayer player, boolean isRobotState)
    {
    	Packet pkg = new Packet(UserCmdOutType.GAME_ROBOT_STATE);
		pkg.putInt(player.getUserId());
		pkg.putBoolean(player.getIsRobot() ? false : isRobotState);	 
		sendToAll(pkg);
    }
    
    public void SendMessage(GamePlayer player, String msg, String msg1, int type)
    {
        if (msg != null)
        {
            Packet pkg = new Packet(UserCmdOutType.GAME_CHAT);
            pkg.putInt(type);
            pkg.putStr(msg);
            player.sendTcp(pkg);
        }
        if (msg1 != null)
        {
            Packet pkg = new Packet(UserCmdOutType.GAME_CHAT);
            pkg.putInt(type);
            pkg.putStr(msg1);
            sendToAll(pkg, player);
        }
    }

    protected void onBeginNewTurn()
    {
        notifyListener(GameEventType.BeginNewTurn.getValue());
    }

    protected void onRest()
    {
        notifyListener(GameEventType.RoomRest.getValue());
    }

    protected void onGameOverred()
    {
        notifyListener(GameEventType.GameOverred.getValue());
    }

    protected void onGameStarted()
    {
        notifyListener(GameEventType.GameStarted.getValue());
    }

    protected void onGameStopped()
    {
        notifyListener(GameEventType.GameStopped.getValue());
    }

    @Override
    public String toString()
    {
        return String.format(
                             "Id:%d,player:%d,state:%d,turnIndex:%d,actions:%d",
                             getId(), getPlayerCount(),
                             getGameState().getValue(), this.turnIndex,
                             this.actions.size());
    }

    public void processData(Packet packet)
    {
//        if (players.containsKey(packet.getClientId()))
//        {
//            Player player = players.get(packet.getClientId());
//            AddAction(new DiceProcessAction(player, packet));
//        }
    }

    public void Dispose(boolean disposing)
    {

    }

    public synchronized void Dispose()
    {
        int disposed = m_disposed;
        m_disposed = 1;
        if (disposed == 0)
        {
            Dispose(true);
        }
    }

    /**
     * @param packet
     * @param player
     */
    public void sendToPlayer(Packet packet, Player player)
    {
        if (player.getPlayerDetail() != null)
            player.getPlayerDetail().sendTcp(packet);
    }

    /**
     * 判断游戏中的玩家是否>1
     * 
     * @return
     */
    public boolean isAllComplete()
    {
        return true;
    }

    public void sendToAll(Packet pkg)
    {
        sendToAll(pkg, null);
    }

    /**
     * millisecond that the game will be waited to execute.
     */
    private long m_waitTimer = 0;

    public void ClearWaitTimer()
    {
        m_waitTimer = 0;
    }

    public void WaitTime(int delay)
    {
        m_waitTimer = Math.max(m_waitTimer, TickHelper.GetTickCount() + delay);
    }
    
    /**
     * 游戏延迟addDelay秒钟，addDelay必须大于等于0
     * 
     * @return
     */
    public void addWaitTime(long addDelay)
    {
    	if(addDelay >= 0) {
            m_waitTimer = Math.max(m_waitTimer + addDelay, TickHelper.GetTickCount() + addDelay);
    	}
    }

    public long GetWaitTimer()
    {
        return m_waitTimer;
    }
    
    private int m_lifeTime = 0;

    public int getLifeTime()
    {
        return m_lifeTime;
    }

    @Override
    public void run()
    {
        long current = System.currentTimeMillis();

        if (m_passTick < current)
        {
            m_lifeTime++;
            if (gameState == GameState.Stopped)
                return;

            List<IAction> temp = new ArrayList<IAction>();
            synchronized (this.actions)
            {
                temp.addAll(actions);
                this.actions.clear();
            }

            if (temp != null)
            {
                CurrentActionCount = temp.size();
                if (temp.size() > 0)
                {
                	List<IAction> left = new ArrayList<IAction>();
                    for (IAction action : temp)
                    {
                        try
                        {
                            action.execute(this, current);
                            long end = TickHelper.GetTickCount();

                            if (end - current > 100)
                                WriteLog(String.format(
                                                       "Game Action time out - time: %d, action:%s ",
                                                       end - current,
                                                       action.getClass().getName()));

                            if (action.isFinished(this, current) == false)
                            {
                                left.add(action);
                            }
                        }
                        catch (Exception ex)
                        {
                            System.err.println("Game Update Error:"
                                    + ex.getMessage());
                            logger.error("Game update error:", ex);
                        }
                    }

                    AddAction(left);
                }
                else if (m_waitTimer < current)
                {
                    checkState(0);
                    m_passTick = current + 1000;
                }
            }
        }
    }

    /**
     * update game player state.
     * notify to all player the changing state.
     */
    public void sendUpdatePlayerState()
    {
        Packet pkg = new Packet(UserCmdOutType.UPDATE_PLAYER_STATE);
        List<Player> list = getAllPlayers();
        pkg.putInt(list.size());
        for (Player player : list)
        {
            pkg.putInt(player.getPlayerID());
            pkg.putByte(player.getPlayerState());
            
//            System.out.println("sendUpdatePlayerState=======" + player.getPlayerID() + "====" + player.getPlayerState());
        }

        sendToAll(pkg);
    }
    
    public void StartPrepareGame()
    {
    	 AddAction(new StartPrepareAction(startGameDelay.intValue()));
    }
   
    
    
    
    
    
    
    
    private long kickPlayerTime;

    public void setKickPlayerTime(long delay)
    {
        kickPlayerTime = TickHelper.GetTickCount() + delay;
    }

    /**
     * checks whether kick the player out the room.
     * @param currentTime
     */
    public void checkComplete(long currentTime)
    {
    	
    	List<Player> list = getAllPlayers();
    	if(list.size() < 1)
    	{
    		return;
    	}
    	
    	
    	List<Player> tempList = new ArrayList<Player>();    	
    	if(hallType.getHallType() == HallGameType.SOCIAL)
    	{
    		if(gameState == GameState.Prepared)
            {
            	
            	int robotCount = 0;
                for (Player player : list)
                {
                    if (player.getPlayerDetail().getPlayerInfo().isOnline() == false)
                    {
                    	tempList.add(player);
                    }
                    
                    if(player.getPlayerDetail().getIsRobot())
                    	robotCount+=1;                
                }
                for (Player player : tempList)
                {
                	this.getBaseRoom().removePlayer(player.getPlayerDetail());        	
                }
                if(list.size() == robotCount)
            	{
            		for (Player player : list)
                    {
            			this.getBaseRoom().removePlayer(player.getPlayerDetail());                                       
                    }
            	}

               
                
                
                if (currentTime > kickPlayerTime)
                {
                	            	
            		for (Player player : list)
                    {
                        if (player.getPlayerState() < PlayerState.EndShaked || player.getPlayerDetail().getPlayerInfo().isOnline() == false)
                        {
                        	tempList.add(player);
                        }
                    }
                	
                	
                    //找到所有需要移除的玩家
                    for (Player player : tempList)
                    {
                    	
                    	if(HallMgr.getHallById(getHallId()).getHallType().getHallType() == HallGameType.CONTEST || 
                    			(this.isAutoStart && player.getNoOperateTime() <= this.maxNoOperateTime && player.getPlayerDetail().getPlayerInfo().isOnline() == true))
                    	{
                    		UserShakeAction action = new UserShakeAction(player.getPlayerDetail(), 0);
                    		this.getBaseRoom().getGame().AddAction(action);//csf    
                    	}                		                		
                    	else
                    	{
                    		this.getBaseRoom().removePlayer(player.getPlayerDetail());
                    	}
                    }
                    

                    if(this.getBaseRoom().getWaitCountIndex() > 200)
                    {
                    	for (Player player : tempList)
                        {
                    		this.getBaseRoom().removePlayer(player.getPlayerDetail());
                        }
                    }
                    else if(list.size() < HallMgr.getHallById(getHallId()).getHallType().getPlayerCountMin())
                    {                        	
                		System.out.println("onStartAddRobot====================22============");
                		this.getBaseRoom().onStartAddRobot();
                    }
                    
                    setKickPlayerTime(Integer.MAX_VALUE);//Integer.MAX_VALUE   如果已经踢出了一个用户这个踢出标志位要重置

                }
            }
    	}

        
        
        
        return;
    }
    
    
    
}
