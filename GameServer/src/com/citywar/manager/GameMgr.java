/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.citywar.dice.entity.CardTypeInfo;
import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.dice.entity.PengConfigInfo;
import com.citywar.game.BaseGame;
import com.citywar.game.DiceGame;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.room.BaseRoom;
import com.citywar.type.GameState;
import com.citywar.type.GameType;
import com.citywar.type.HallGameType;

/**
 * 
 * @author Dream
 * @date 2011-12-15
 * @version
 * 
 */
public class GameMgr
{
    private static final Logger logger = Logger.getLogger(GameMgr.class.getName());

    private static ConcurrentHashMap<Integer, BaseGame> games;

    private static int ThreadPoolCount = 8;

    private static ExecutorService executorService;

    private static boolean running;

    private static AtomicInteger gameId;
    
    private static Thread mainThread;
    /**
     * 二个人的玩游戏的次数
     */
    private static AtomicInteger gameTypeTwoPlayer;
    /**
     * 三个人的玩游戏的次数
     */
    private static AtomicInteger gameTypeThreePlayer;
    /**
     * 四个人的玩游戏的次数
     */
    private static AtomicInteger gameTypeFourPlayer;

    
    private static Map<Integer,Integer> gameContestCardList; 
    private static Map<Integer,Integer> gameSocialCardList; 

    public static Map<Integer,Integer> getGameEndCardList(int type)
    {
    	if(type == HallGameType.CONTEST)
    		return gameContestCardList;
    	else 
    		return gameSocialCardList;
    }
    
    
    public static boolean init()
    {
        // 使用单线程专用线程池，将线程管理交给线程池完成
        executorService = Executors.newFixedThreadPool(ThreadPoolCount);
        games = new ConcurrentHashMap<Integer, BaseGame>();
        gameId = new AtomicInteger(0);
        gameTypeTwoPlayer = new AtomicInteger(0);
        gameTypeThreePlayer = new AtomicInteger(0);
        gameTypeFourPlayer = new AtomicInteger(0);
        
        
        gameContestCardList = new HashMap<Integer,Integer>(); 
        gameSocialCardList = new HashMap<Integer,Integer>(); 
        for (CardTypeInfo itemCardInfo : CardMgr.getCardTypeList())
        {
        	if(itemCardInfo.getUserType() == 1)
        	{
        		gameContestCardList.put(itemCardInfo.getCardTypeId(), itemCardInfo.getPara());

        		if(itemCardInfo.getGameType() == 0 || itemCardInfo.getGameType() == 1)
        		{
        			gameSocialCardList.put(itemCardInfo.getCardTypeId(), itemCardInfo.getPara());
        		}
        	}
        }
        
        
       
//        gameContestCardList[0][0] = GamePlayerEndState.Win.getValue();   
//        gameContestCardList[0][1] = CardType.WIN_COIN;        
//        gameContestCardList[1][0] = GamePlayerEndState.Win.getValue();   
//        gameContestCardList[1][1] = CardType.WIN_DRUNK;        
//
//        gameContestCardList[2][0] = GamePlayerEndState.Lose.getValue();   
//        gameContestCardList[2][1] = CardType.LOSE_COIN;        
//        gameContestCardList[3][0] = GamePlayerEndState.Lose.getValue();   
//        gameContestCardList[3][1] = CardType.LOSE_DRUNK;        
                
//        gameEndCardList[4][0] = GamePlayerEndState.Draw.getValue();   
//        gameEndCardList[4][1] = CardType.DRAW_COIN;        

        
        return true;
    }

    /**
     * 启动游戏执行线程
     * 
     * @return ture
     */
    public static boolean start()
    {
        if (running == false)
            running = true;

        // 启动游戏执行线程
        mainThread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                gameThread();
            }
        });
        mainThread.start();

        return true;
    }

    /**
     * 终止执行
     */
    public static void stop()
    {
        if (running)
        {
            running = false;
            executorService.shutdown();
        }
    }

    /**
     * 获取自增id，使用原子数据类型，无需同步
     * 
     * @return id
     */
    public static int getGameId()
    {
        return gameId.getAndIncrement();
    }

    /**
     * 获取所有游戏对象
     * 
     * @return
     */
    public static List<BaseGame> getAllGame()
    {
        List<BaseGame> list = new ArrayList<BaseGame>(games.values());
        return list;
    }

    /**
     * 添加游戏
     * 
     * @param game
     */
    public static void addGame(BaseGame game)
    {
        if (game == null || !running)
            return;

        games.put(game.getId(), game);
    }

    /**
     * add game to thread pool.
     * 
     * @param game
     */
    public static void readdGame(BaseGame game)
    {
        if (game == null || !running)
            return;
    }

    /**
     * 移除游戏
     * 
     * @param game
     *            游戏对象
     */
    public static void removeGame(BaseGame game)
    {
        if (game == null)
            return;
        games.remove(game.getId());
    }
    /**
     * 
     * @param roomId
     * @param hallId
     * @param list
     * @param roomType
     * @param gameType
     * @param timeType
     * @param hallType
     * @return
     */
    public static BaseGame createGame(BaseRoom baseRoom, int hallId,
			List<GamePlayer> list, int timeType,
			HallTypeInfo hallType) {
    	
    	
    	BaseGame game = null;
    	switch(hallType.getGameType()) {
	    	case 1:
	    		game = new DiceGame(getGameId(), hallId , baseRoom, list, 
	    				hallType.getGameType(), timeType, hallType);
	    		break ;
	    	
    	}
    	    	
        addGame(game);
		return game;
	}
    
    
//	public static BaseGame createDiceGame(int roomId, int hallId,
//			List<GamePlayer> list, int roomType, int gameType, int timeType,
//			HallTypeInfo hallType) {
//		DiceGame game = new DiceGame(getGameId(), hallId , roomId, list, roomType,
//                gameType, timeType, hallType);
//        addGame(game);
//		return game;
//	}
//	
//	
//	public static BaseGame createDiZhuGame(int roomId, int hallId,
//			List<GamePlayer> list, int roomType, int gameType, int timeType,
//			HallTypeInfo hallType) {
//		BaseGame game = new DiZhuGame(getGameId(), hallId , roomId, list, roomType,
//                gameType, timeType, hallType);
//        addGame(game);
//		return game;
//	}
	
    /**
     * 查找指定id的游戏对象
     * 
     * @param gameId
     *            游戏id
     * @return 游戏对象 or null
     */
    public static BaseGame findGame(int gameId)
    {
        BaseGame game = null;
        game = games.get(gameId);
        return game;
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private static final int CLEAR_GAME_INTERVAL = 5 * 1000;

    private static long m_clearGamesTimer;

    private static final long THREAD_INTERVAL = 40;

    /**
     * 游戏主线程，执行游戏状态变更及已结束游戏的清理
     */
    private static void gameThread()
    {
        long balance = 0;
        m_clearGamesTimer = System.currentTimeMillis();
        int gameCount = 0;
        while (running)
        {
            long start = System.currentTimeMillis();
            try
            {
                // 更新所有游戏状态
                gameCount = updateGames(start);

                // 定期清理已经结束的游戏对象，m_clearGamesTimer记录下次清理的时间点，当当前时间大于该时间点时执行清理
                if (m_clearGamesTimer <= start)
                {
                    m_clearGamesTimer += CLEAR_GAME_INTERVAL;

                    final ArrayList<BaseGame> temp = new ArrayList<BaseGame>();
                    Collection<BaseGame> allGames = getAllGame();
                    for (BaseGame g : allGames)
                    {
                        if (g.getGameState() == GameState.Stopped)
                        {
                            temp.add(g);
                        }
                    }
                    for (BaseGame g : temp)
                    {
                        removeGame(g);
                    }

                    Thread thread = new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            clearStoppedGames(temp);
                        }
                    });
                    thread.start();
                }
            }
            catch (Exception ex)
            {
                logger.error("Game Mgr Thread Error:", ex);
            }

            // 时间补偿
            long end = System.currentTimeMillis();

            balance += THREAD_INTERVAL - (end - start);

            // 执行时间过长
            if (end - start > THREAD_INTERVAL * 2)
            {
                logger.warn(String.format("Game Mgr spent too much times: %d ms, count:%d",
                                          end - start, gameCount));
            }

            if (balance > 0)
            {
                // 提前完成，避免执行过于频繁，按THREAD_INTERVAL指定的时间间隔调度执行
                try
                {
                    Thread.sleep((int) balance);
                }
                catch (InterruptedException e)
                {
                    logger.error("[ GameMgr : gameThread ]", e);
                }
                balance = 0;
            }
            else
            {
                // 执行过慢，进行一定的时间补偿，未补足部分将从后续执行序列中补回
                if (balance < -1000)
                {
                    balance += 1000;
                }
            }
        }
    }

    /**
     * 清理所有已经结束的游戏对象
     * 
     * @param games
     *            需清理列表
     */
    private static void clearStoppedGames(List<BaseGame> games)
    {
        for (BaseGame g : games)
        {
            try
            {
                g.Dispose();
            }
            catch (Exception ex)
            {
                logger.error("game dispose error:", ex);
            }
        }
    }

    /**
     * 驱动所有游戏对象
     * 
     * @param tick
     *            当前时间点，用于游戏action执行时作时间戳对比
     * @return 当前游戏对象数量
     */
    private static int updateGames(long tick)
    {
        Collection<BaseGame> games = getAllGame();
        if (games != null)
        {
            for (BaseGame g : games)
            {
                try
                {
                    executorService.submit(g);
                }
                catch (Exception ex)
                {
                    logger.error("Game  updated error:", ex);
                }
            }

            return games.size();
        }
        return 0;
    }
    
    /**
     * 增加各类型游戏次数
     */
    public static void addGameTypeCount(int gameType)
    {
    	switch(gameType) {
    	case 2:
    		gameTypeTwoPlayer.incrementAndGet();
    		break ;
    	case 3:
    		gameTypeThreePlayer.incrementAndGet();
    		break ;
    	case 4:
    		gameTypeFourPlayer.incrementAndGet();
    		break ;
    	}
    }

    /**
     * 清空各类型游戏次数
     */
    public static void clearGameTypeCount(int gameType)
    {
    	switch(gameType) {
    	case 2:
    		gameTypeTwoPlayer.set(0);
    		break ;
    	case 3:
    		gameTypeThreePlayer.set(0);
    		break ;
    	case 4:
    		gameTypeFourPlayer.set(0);
    		break ;
    	}
    }
    
    /**
     * 获得各类型游戏次数
     * 
     * @return 
     */
    public static int getGameTypeCount(int gameType)
    {
    	int gameTypeCount = 0;
    	switch(gameType) {
    	case 2:
    		gameTypeCount = gameTypeTwoPlayer.get();
    		break ;
    	case 3:
    		gameTypeCount = gameTypeThreePlayer.get();
    		break ;
    	case 4:
    		gameTypeCount = gameTypeFourPlayer.get();
    		break ;
    	}
    	return gameTypeCount;
    }
    
    
    
    
    
    
    
	public static boolean isPengYouCreateRoom(int gameType, BaseRoom room, GamePlayer player, List<Integer> paraList)
	{
		
		boolean canStartGame = false;
		switch(gameType)
    	{
    	    
    		case GameType.DiceGame:	        		
    		

    			break;        		
        	case GameType.DiZhuGame:	        		
        		
        		if(paraList.size() >= 3)
        		{
        			PengConfigInfo config = HallMgr.getPengConfig(1, paraList.get(0));
        			
        			if(player.getPlayerInfo().getCardCount() >= config.getPara1())
        			{                				
        				//paraList	1.最大倍数
        				room.setPengyGame(1, config.getPara2());
        				room.setPengyouParaList(paraList);        				  				        				
        				canStartGame = true;
        			
        			}
        		}
        		
        		break;
        	case GameType.NiuGame:
        		
        		if(paraList.size() >= 3)
        		{
        			PengConfigInfo config = HallMgr.getPengConfig(1, paraList.get(0));
        			
        			if(player.getPlayerInfo().getCardCount() >= config.getPara1())
        			{                				
        				//paraList	1.最大倍数
        				room.setPengyGame(1, config.getPara2());
        				room.setPengyouParaList(paraList);        				  				        				
        				canStartGame = true;
        			
        			}
        		}
        		break;
        	case GameType.TexasGame:
        		
        		if(paraList.size() >= 3)
        		{
        			PengConfigInfo config = HallMgr.getPengConfig(1, paraList.get(0));
        			
        			if(player.getPlayerInfo().getCardCount() >= config.getPara1())
        			{                				
        				//paraList	1.最大倍数
        				room.setPengyGame(1, config.getPara2());
        				room.setPengyouParaList(paraList);		        				
        				canStartGame = true;
        			
        			}
        		}
        		break;        		
        	case GameType.ErqiwangGame:
        		
        		if(paraList.size() >= 3)
        		{
        			PengConfigInfo config = HallMgr.getPengConfig(1, paraList.get(0));
        			
        			if(player.getPlayerInfo().getCardCount() >= config.getPara1())
        			{                				
        				//paraList	1.最大倍数
        				room.setPengyGame(1, config.getPara2());
        				room.setPengyouParaList(paraList);        				  				        				
        				canStartGame = true;
        			
        			}
        		}
        		break;        		
    		default:
    			break;
    			
    	}
		
		return canStartGame;
		
	}
	
	
	
}
