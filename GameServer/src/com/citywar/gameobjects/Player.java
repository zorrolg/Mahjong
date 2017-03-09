/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.gameobjects;

import java.util.ArrayList;
import java.util.List;

import com.citywar.dice.entity.RobotLevelInfo;
import com.citywar.game.BaseGame;
import com.citywar.game.DiceGame;
import com.citywar.manager.RobotLevelMgr;
import com.citywar.type.PlayerState;
import com.citywar.util.ThreadSafeRandom;

/**
 * the game player wrapping GamePlayer instance
 * 
 * @author Dream
 * @date 2011-12-15
 * @version
 * 
 */
public class Player
{
//    private static Logger logger = Logger.getLogger(Player.class.getName());

    /**
     * GamePlayer component
     */
    private GamePlayer player;

    /**
     * player id
     */
    private int playerID;
    /**
     * game component
     */
    protected BaseGame game;
    /**
     * player whether is playing.
     */
    protected boolean isPlaying;

    /**
     * player current play state.
     */
    private byte playerState;
    
    /**
     * player id
     */
    private int drawCoin;
    
    private int noOperateTime;
    
    private boolean isPassBet;
    
    private RobotLevelInfo robotLevelInfo;
    
    
    
    public boolean isPlaying()
    {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying)
    {
        this.isPlaying = isPlaying;
    }

    public boolean getIsPassBet()
    {
        return isPassBet;
    }

    public void setIsPassBet(boolean isPassBet)
    {
        this.isPassBet = isPassBet;
    }
    
    public void setPlayerState(byte state)
    {
        this.playerState = state;
    }

    public byte getPlayerState()
    {
        return this.playerState;
    }
    
    public int getDrawCoin()
    {
        return drawCoin;
    }

    public void setDrawCoin(int drawCoin)
    {
    	this.drawCoin = drawCoin;
    }
    
    public int getNoOperateTime()
    {
        return noOperateTime;
    }

    public void setNoOperateTime(int noOperateTime)
    {
    	this.noOperateTime = noOperateTime;
    }
    
    public void addNoOperateTime()
    {
    	this.noOperateTime++;
    }
    
    public void setRobotLevel(int robotLevel)
    {
    	robotLevelInfo = RobotLevelMgr.findRobotLevel(robotLevel);
    }
    
   
    
    
    public void setGame(BaseGame game)
    {
        this.game = game;
    }

    public int getPlayerID()
    {
        return this.playerID;
    }

    /**
     * @return
     */
    public GamePlayer getPlayerDetail()
    {
        return this.player;
    }
    
    private ThreadSafeRandom random = new ThreadSafeRandom();

    
    
    
    
    
    
    
    
    /////////////////////////////////dice/////////////////////////////////////////////////////////////////////////////
    private byte[] DiceNumbers;
    private byte sameCount;
    private int DiceNumber;
    private int DicePoint;
    private boolean IsCallOne;
    private boolean IsGetDice;
    public boolean getIsGetDice()
    {
        return this.IsGetDice;
    }
    
    public byte[] getDiceNumbers()
    {
        return DiceNumbers;
    }

    public void setDiceNumbers(byte[] diceNumbers)
    {
        DiceNumbers = diceNumbers;
        IsGetDice = true;
    }

    public int getDiceNumber()
    {
        return DiceNumber;
    }

    public void setDiceNumber(int diceNumber)
    {
        DiceNumber = diceNumber;
    }
    public int getDicePoint()
    {
        return DicePoint;
    }

    public void setDicePoint(int dicePoint)
    {
        DicePoint = dicePoint;
    }

    public boolean isIsCallOne()
    {
        return IsCallOne;
    }

    public void setIsCallOne(boolean isCallOne)
    {
        IsCallOne = isCallOne;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    
    /////////////////////////////////Poker/////////////////////////////////////////////////////////////////////////////   

    private int gameOverCoin;
   
	
	public int getGameOverCoin()
	{
		return gameOverCoin;
	}
	
	public void setGameOverCoin(int gameOverCoin)
	{
		this.gameOverCoin = gameOverCoin;
	}
	

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
	
	
	
	
	
	
	    
    
	
	
	
	
	
	
	

    /**
     * @param id
     * @param game
     * @param name
     */
    public Player(GamePlayer player, BaseGame game)
    {
        isPlaying = false;
        this.player = player;
        this.game = game;
        this.playerID = player.getUserId();
        playerState = PlayerState.Idle;
        this.noOperateTime = 0;
        isPassBet = false;
        

        resetPlayer(1);
                       
    }


    /**
     * reset the value of information.
     */
    public void resetPlayer(int gameType)
    {
    	    	
    	if(gameType == 1)
    	{
    		this.DiceNumber = 0;
            this.DicePoint = 0;
            this.DiceNumbers = new byte[5];
            this.IsCallOne = false;
            this.IsGetDice = false;
    	}
    	
                         	
    	getPlayerDetail().setIsRobotState(false);
    	getPlayerDetail().setPengConfirmState(0);
        setNoOperateTime(0);        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /////////////////////////////////dice/////////////////////////////////////////////////////////////////////////////
    // 机器人开局叫实现
    public void startCall()
    {
    	DiceGame diceGame = (DiceGame) game;
    	int playerSize = diceGame.getTurnQueue().size();
    	int callNum = 0;
	    if (random.next(1, 11) > 1)
	    {
	    	callNum = random.next(playerSize, playerSize + 2);	     
	    }	     
	    else	          
	    {  	
	    	callNum = (random.next(playerSize + 2, 2 * playerSize + 1));
	    }
	    diceGame.setDiceNumber((byte) callNum);    	
    	diceGame.setDicePoint((byte) (random.next(1, 7)));
    	nextCallWhenHave();
    	return;
    }

    // 机器人非开局叫之前的判断：判断手上是否有场上叫的数字
    public void beforeNextCall()
    {
        DiceGame diceGame = (DiceGame) game;

        if (isHaveCurrentPoint(diceGame))
        {
        	// 如果上一个玩家叫的数字自己有,走nextCallWhenHave
        	nextCallWhenHave();
        }
        else		
        {
        	// 上一个玩家叫的数字自己没有
        	nextCallWhenNotHave();
        }
    }

    /**
     * when the turn time is out,system call the number instead for player.
     */
    public boolean PlayerTimeOutCall()
    {
        DiceGame diceGame = (DiceGame) game;
        byte playerSize = (byte) diceGame.getTurnQueue().size();
        
       
        //用户超时之前保存的预设值
        if(this.DicePoint != 0 && this.DiceNumber != 0 && 
        		getLowestCanCallNum(diceGame, this.DicePoint, this.IsCallOne) <= this.DiceNumber) {//是否大于最小可叫的值
            diceGame.setDiceNumber((byte) this.DiceNumber);//可以则使用 用户超时之前保存的预设值
            diceGame.setDicePoint((byte) this.DicePoint);
            diceGame.setHasCallOne(diceGame.getHasCallOne() ? true : this.IsCallOne);//如果是斋局则设成斋
        }
        // if the call is the first,call current player size +1,else current numbers add 1.
        // if the call is the first,randomly create a point,else the same to last.
        if (diceGame.getTurnIndex() == 1)
        {
            diceGame.setDiceNumber((byte)(playerSize + 1));
            diceGame.setHasCallOne(false);
            diceGame.setDicePoint((byte) 2);
        } else if(diceGame.getDiceNumber() + 1 >= ((diceGame.getTurnQueue().size() * 5) + 1)) {
        	diceGame.changeTurn(playerID);
            diceGame.gameOver(false);
        	return true;
        } else {
    		diceGame.setDiceNumber((byte) (diceGame.getDiceNumber() + 1));
            diceGame.setDicePoint(diceGame.getDicePoint());
        }
//        System.out.println("gamer PlayerTimeOutCall=========================================================" + diceGame.getDiceNumber() + "====" + diceGame.getDicePoint() + "====" +diceGame.getHasCallOne());
        return false;
    }

    // 机器人非开局叫实现(机器人有当前玩家叫的筛子)
    public void nextCallWhenHave()
    {
        DiceGame diceGame = (DiceGame) game;
        int playerSize = diceGame.getTurnQueue().size();
        int totalDiceSize = playerSize * 5;

        

        int rndtype = random.next(1, 101);
        // 随机叫任何数字的最低可叫值: 概率30%
        if(rndtype < robotLevelInfo.getRealcallmostpercent())
        {
//        	System.out.println("================================real type 1================"+rndtype + "==================================" + robotLevelInfo.getRealcallmostpercent()
//       			 + "======" + robotLevelInfo.getRealcallnumberpercent());
        	// 获取可叫的点数
        	int randomDicePoint = diceGame.getMaxDicePoints(diceGame.getHasCallOne());
        	
        	// 获取最低可叫个数
        	int lowestDiceNumber = getRobotLowestCanCallNum(diceGame, randomDicePoint);
        	int finallyNumber = 0;
        	if (random.next(1, 101) < robotLevelInfo.getRealcallnumberpercent())
        		finallyNumber = lowestDiceNumber > totalDiceSize ? totalDiceSize : lowestDiceNumber;
        	else
        		finallyNumber = lowestDiceNumber + 1 > totalDiceSize ? totalDiceSize : lowestDiceNumber + 1;
        		
        	//这里有一种特殊情况当对方叫3个5,机器人随机进入这里的逻辑,最低可叫值是3,随机出来5之后就和对方叫的一样
        	if ((finallyNumber == diceGame.getDiceNumber() && randomDicePoint == diceGame.getDicePoint())
            			|| finallyNumber == playerSize)
        	{
        		//这里就要叫斋
        		diceGame.setHasCallOne(true);
        	}
        	diceGame.setDiceNumber((byte)finallyNumber);
            diceGame.setDicePoint((byte)randomDicePoint);
        }
        else if (rndtype < robotLevelInfo.getRealcallmostpercent() + robotLevelInfo.getRealcallrndpercent())
        {
//        	System.out.println("================================real type 3================"+rndtype + "==================================" + robotLevelInfo.getRealcallrndpercent()
//          			 + "======" + robotLevelInfo.getRealcallnumberpercent());
        	// 获取可叫的点数
        	int randomDicePoint = 1;
        	List<Integer> list = getLowestCanCallPoint(diceGame.getDicePoint());
        	if (null != list && list.size() > 0) {
        		int index = random.next(0, list.size());
            	randomDicePoint = list.get(index);
        	}
        	// 获取最低可叫个数
        	int lowestDiceNumber = getRobotLowestCanCallNum(diceGame, randomDicePoint);
        	int finallyNumber = 0;
        	if (random.next(1, 101) < robotLevelInfo.getRealcallnumberpercent())
        		finallyNumber = lowestDiceNumber > totalDiceSize ? totalDiceSize : lowestDiceNumber;
        	else
        		finallyNumber = lowestDiceNumber + 1 > totalDiceSize ? totalDiceSize : lowestDiceNumber + 1;
        	//这里有一种特殊情况当对方叫3个5,机器人随机进入这里的逻辑,最低可叫值是3,随机出来5之后就和对方叫的一样
        	if ((finallyNumber == diceGame.getDiceNumber() && randomDicePoint == diceGame.getDicePoint())
            			|| finallyNumber == playerSize)
        	{
        		//这里就要叫斋
        		diceGame.setHasCallOne(true);
        	}
        	diceGame.setDiceNumber((byte)finallyNumber);
            diceGame.setDicePoint((byte)randomDicePoint);
        }
        // 叫上一个人的数字,个数+1OR+2: 概率70%
        else
        {
//        	System.out.println("================================real type 2================"+rndtype + "==================================" + robotLevelInfo.getRealcalladdpercent()
//          			 + "======" + robotLevelInfo.getRealcallnumberpercent());
        	
        	int finallyNumber = 0;
        	if (random.next(1, 101) < robotLevelInfo.getUnRealcallnumberpercent())
        		finallyNumber = diceGame.getDiceNumber() + 1 > totalDiceSize ? totalDiceSize : diceGame.getDiceNumber() + 1;
        	else
        		finallyNumber = diceGame.getDiceNumber() + 2 > totalDiceSize ? totalDiceSize : diceGame.getDiceNumber() + 2;        		
        	diceGame.setDiceNumber((byte)finallyNumber);
        	
//            diceGame.setDiceNumber((byte) (diceGame.getDiceNumber() + 1 > totalDiceSize ? totalDiceSize
//                    : diceGame.getDiceNumber() + 1));
        }
        
        
        // 如果叫1，则为斋
        if ((int) (diceGame.getDicePoint()) == 1)
        {
            diceGame.setHasCallOne(true);
        }
        
//        System.out.println("gamer nextCallWhenHave=========================================================" + diceGame.getDiceNumber() + "====" + diceGame.getDicePoint() + "====" +diceGame.getHasCallOne());
//        System.out.println("" + );
    }
    
    // 机器人非开局叫实现(机器人无当前玩家叫的筛子)
    public void nextCallWhenNotHave()
    {
        DiceGame diceGame = (DiceGame) game;
        int playerSize = diceGame.getTurnQueue().size();
        int totalDiceSize = playerSize * 5;

//        int rndtype = random.next(1, 101);    	
        // 随机叫任何数字的最低可叫值，或者最低可叫值+1: 概率80%
        if(random.next(1, 101) < robotLevelInfo.getUnRealcallmostpercent())
        {
//        	System.out.println("================================un real type 1================"+rndtype + "==================================" + robotLevelInfo.getUnRealcallmostpercent()
//       			 + "======" + robotLevelInfo.getUnRealcallnumberpercent());
        	// 获取可叫的点数
        	int randomDicePoint = diceGame.getMaxDicePoints(diceGame.getHasCallOne());
        	// 获取最低可叫个数
        	int lowestDiceNumber = getRobotLowestCanCallNum(diceGame, randomDicePoint);
        	int finallyNumber = 0;
        	if (random.next(1, 101) < robotLevelInfo.getUnRealcallnumberpercent())
        		finallyNumber = lowestDiceNumber > totalDiceSize ? totalDiceSize : lowestDiceNumber;
        	else
        		finallyNumber = lowestDiceNumber + 1 > totalDiceSize ? totalDiceSize : lowestDiceNumber + 1;
        	
        	//这里有一种特殊情况当对方叫3个5,机器人随机进入这里的逻辑,最低可叫值是3,随机出来5之后就和对方叫的一样
        	//要修改为随机叫的大于等于当前值
        	if ((finallyNumber == diceGame.getDiceNumber() && randomDicePoint == diceGame.getDicePoint())
        			|| finallyNumber == playerSize)
        	{
        		//这里就要叫斋
        		diceGame.setHasCallOne(true);
        	}
        	diceGame.setDiceNumber((byte)finallyNumber);
            diceGame.setDicePoint((byte)randomDicePoint);
        }
        else if (random.next(1, 101) < robotLevelInfo.getUnRealcallmostpercent() + robotLevelInfo.getUnRealcallrndpercent())
        {
//        	System.out.println("================================un real type 3================"+rndtype + "==================================" + robotLevelInfo.getUnRealcallrndpercent()
//          			 + "======" + robotLevelInfo.getUnRealcallnumberpercent());
        	// 获取可叫的点数
        	int randomDicePoint = 1;
        	List<Integer> list = getLowestCanCallPoint(diceGame.getDicePoint());
        	if (null != list && list.size() > 0) {
        		int index = random.next(0, list.size());
            	randomDicePoint = list.get(index);
        	}
        	// 获取最低可叫个数
        	int lowestDiceNumber = getRobotLowestCanCallNum(diceGame, randomDicePoint);
        	int finallyNumber = 0;
        	if (random.next(1, 101) < robotLevelInfo.getUnRealcallnumberpercent())
        		finallyNumber = lowestDiceNumber > totalDiceSize ? totalDiceSize : lowestDiceNumber;
        	else
        		finallyNumber = lowestDiceNumber + 1 > totalDiceSize ? totalDiceSize : lowestDiceNumber + 1;
        	
        	//这里有一种特殊情况当对方叫3个5,机器人随机进入这里的逻辑,最低可叫值是3,随机出来5之后就和对方叫的一样
        	//要修改为随机叫的大于等于当前值
        	if ((finallyNumber == diceGame.getDiceNumber() && randomDicePoint == diceGame.getDicePoint())
            			|| finallyNumber == playerSize)
        	{
        		//这里就要叫斋
        		diceGame.setHasCallOne(true);
        	}
        	diceGame.setDiceNumber((byte)finallyNumber);
            diceGame.setDicePoint((byte)randomDicePoint);
        }
        // 叫上一个人的数字,个数+1OR+2: 概率20%
        else
        {
//        	System.out.println("================================un real type 2================"+rndtype + "==================================" + robotLevelInfo.getUnRealcalladdpercent()
//          			 + "======" + robotLevelInfo.getUnRealcallnumberpercent());
        	
        	int finallyNumber = 0;
        	if (random.next(1, 101) < robotLevelInfo.getUnRealcallnumberpercent())
        		finallyNumber = diceGame.getDiceNumber() + 1 > totalDiceSize ? totalDiceSize : diceGame.getDiceNumber() + 1;
        	else
        		finallyNumber = diceGame.getDiceNumber() + 2 > totalDiceSize ? totalDiceSize : diceGame.getDiceNumber() + 2;        		
        	diceGame.setDiceNumber((byte)finallyNumber);
        	
//            diceGame.setDiceNumber((byte) (diceGame.getDiceNumber() + 1 > totalDiceSize ? totalDiceSize
//                    : diceGame.getDiceNumber() + 1));
        }
        // 如果叫1，则为斋
        if ((int) (diceGame.getDicePoint()) == 1)
        {
            diceGame.setHasCallOne(true);
        }
        
//        System.out.println("gamer nextCallWhenNotHave=========================================================" + diceGame.getDiceNumber() + "====" + diceGame.getDicePoint() + "====" +diceGame.getHasCallOne());
    }

    // 轮到机器人,叫或开
    public void robotTurn()
    {
        DiceGame diceGame = (DiceGame) game;
        int currentNumber = diceGame.getDiceNumber();
        int playerCount = diceGame.getPlayerCount() - 1;
        int total = calculatePoint(diceGame);
        int robotHaveCount = diceGame.haveCurrentPointCount(this.getDiceNumbers(),diceGame.getDicePoint());
        //System.out.println("机器人叫时候的游戏id===" + diceGame.getId());
        //System.out.println("机器人叫的时候diceGame.getTurnQueue()的大小为==" + diceGame.getTurnQueue().size());
        
             
        
        boolean isOutArea = false;
        if(currentNumber >= robotHaveCount + 4 * playerCount)
        	isOutArea = true;
        
        boolean isLowArea = false;
        if(currentNumber <= robotHaveCount + 1 * playerCount)
        	isLowArea = true;
        
//        System.out.println("");
//        System.out.println("robotTurn================" + currentNumber + "====" + total);
        
        
//        robotLevelInfo
        if(isLowArea)        
        {
        	beforeNextCall();        
        }
        else if(isOutArea)
        {
        	robotOpen(diceGame);        
        }
        else if (currentNumber > total)
        {
        	int rndOpen = random.next(1, 101);
//        	System.out.println("not real open================"+rndOpen + "======" + robotLevelInfo.getUnRealOpenPercent());
            // 当场上数字大于实际值，轮到自己，开: 概率80% 或者玩家叫的数值超过了当前有的筛子数
            if (rndOpen < robotLevelInfo.getUnRealOpenPercent())
            	robotOpen(diceGame);
            else
                beforeNextCall();		// 当场上数字大于实际值，轮到自己，不开: 概率20%                  
        }
        else
        {
        	int rndOpen = random.next(1, 101);
//        	System.out.println("real open================"+rndOpen + "======" + robotLevelInfo.getRealOpenPercent());
            // 当场上数字小于实际值，轮到自己，开: 概率10%
            if (rndOpen < robotLevelInfo.getRealOpenPercent())
            	robotOpen(diceGame);
            else
            	beforeNextCall();	// 当场上数字小于实际值，轮到自己，不开: 概率90%
        }
    	    	
    }

    // 机器人抢开实现
    public void directlyOpen()
    {
        DiceGame diceGame = (DiceGame) game;
        int total = calculatePoint(diceGame);
        if (diceGame.getDiceNumber() > total)
        {
            // 当场上数字大于实际值，直接抢开: 概率80%
            if (random.next(1, 11) > 2)
            {
            	robotOpen(diceGame);
            }
        }
        else
        {
            // 当场上数字小于实际值，直接抢开: 概率20%
            if (random.next(1, 11) > 8)
            {
            	robotOpen(diceGame);
            }
        }
    }

    // 计算点数
    private int calculatePoint(DiceGame diceGame)
    {
        int total = 0;
        for (Player l : diceGame.getTurnQueue())
        {
            Player player = (Player) l;
            byte result = diceGame.calculatePoint(player.getDiceNumbers());
            total += result;
        }
        return total;
    }

    // 机器人开实现
    public void robotOpen(DiceGame diceGame)
    {
    	int diceNumber = diceGame.getDiceNumber();
    	if (diceNumber <= calculateMyPoint(diceGame))
        {
    		nextCallWhenHave();
        } else {
            int openID = getPlayerDetail().getUserId();
            diceGame.changeTurn(openID);
            diceGame.gameOver(false);
        }
    }

    // 取得当前玩家手上的个数
    public int calculateMyPoint(DiceGame diceGame)
    {
        Player p = (Player) diceGame.getCurrentTurnPlayer();
        if(null == p) {
        	p = this;
        }
        return diceGame.calculatePoint(p.getDiceNumbers());
    }

    // 判断当前玩家手上是否有场上正在叫的数字
    public boolean isHaveCurrentPoint(DiceGame diceGame)
    {
        return diceGame.haveCurrentPointCount(this.getDiceNumbers(),
                                           diceGame.getDicePoint()) > 0;
    }
    
    /** 
     * 取得最低可叫数
     * @param diceGame
     * @param randomDicePoint -- 当前所叫的点数
     * @return
     */
    public int getLowestCanCallNum(DiceGame diceGame, int randomDicePoint, boolean isCallOne)
    {
    	int currentPoint = diceGame.getDicePoint();//当前游戏点数
    	int currentNumber = diceGame.getDiceNumber();//当前游戏个数
    	boolean hasCallOne = diceGame.getHasCallOne();//是否为斋局
    	/**
    	 *  筛子大小顺序 1 > 6 >　5 > 4 > 3 > 2
    	 *  最小可叫值的规则: 
    	 *  如果随机获得的筛子大于等于场上的数字，最小值为场上个数
    	 *  如果随机获得的筛子小于等于场上的数字，最小值为场上个数 +1
    	 */
    	
    	if(randomDicePoint > currentPoint) //如果玩家叫的点数是大于等于游戏上一轮叫的点数
    	{
    		if(currentPoint == 1) // 如果游戏上一轮叫的点数为1
    		{
    			return currentNumber + 1; // 返回最低可叫个数为上一轮个数加1
    		}
    		return currentNumber; // 如果游戏上一轮叫的不是1,则直接返回上一轮叫的个数为最低可叫个数
    	}
    	else // 如果玩家叫的点数是小于等于游戏上一轮叫的点数
    	{
    		if(randomDicePoint == 1) // 如果玩家叫的点数是1
    		{
    			if(currentPoint == 1) // 并且游戏上一轮叫的点数也是1
    			{
    				return currentNumber + 1; // 返回最低可叫个数为上一轮个数加1
    			}
    			return currentNumber;
    		} 
    		else if (isCallOne && ! hasCallOne)
    		{
    			return currentNumber;
    		}
    		return currentNumber + 1;
    	}
    }
    
    /**
     * 机器人专用判断
     * @param diceGame
     * @param randomDicePoint
     * @return
     */
    public int getRobotLowestCanCallNum(DiceGame diceGame, int randomDicePoint)
    {
    	int currentPoint = diceGame.getDicePoint();//当前游戏点数
    	int currentNumber = diceGame.getDiceNumber();//当前游戏个数
    	
    	if(randomDicePoint > currentPoint) //如果玩家叫的点数是大于等于游戏上一轮叫的点数
    	{
    		if(currentPoint == 1) // 如果游戏上一轮叫的点数为1
    		{
    			return currentNumber + 1; // 返回最低可叫个数为上一轮个数加1
    		}
    		return currentNumber; // 如果游戏上一轮叫的不是1,则直接返回上一轮叫的个数为最低可叫个数
    	}
    	else // 如果玩家叫的点数是小于等于游戏上一轮叫的点数
    	{
    		if(randomDicePoint == 1) // 如果玩家叫的点数是1
    		{
    			if(currentPoint == 1) // 并且游戏上一轮叫的点数也是1
    			{
    				return currentNumber + 1; // 返回最低可叫个数为上一轮个数加1
    			}
    			return currentNumber;
    		}
    		return currentNumber + 1;
    	}
    }
    public byte getSameCount() {
		return sameCount;
	}

	public void setSameCount(byte sameCount) {
		this.sameCount = sameCount;
	}
    /**
     * 取得当前可叫的点数数组
     * @param randomDicePoint -- 上轮叫的点数
     * @param hasCallOne -- 上轮叫的是否是斋
     * @return
     */
    public List<Integer> getLowestCanCallPoint(int randomDicePoint)
    {
    	List<Integer> result = new ArrayList<Integer>();
    	if (randomDicePoint > 1) 
    	{
    		for (int i = 6; i > randomDicePoint; i--)
    		{
    			result.add(i);
    		}
    		result.add(1);//1为最大点数,没有叫1的时候可以加入
    	}
    	else // 如果叫的点数就是1,那么任何数都可以叫，
    	{
    		for (int i = 1; i <= 6; i++)
    		{
    			result.add(i);
    		}
    	}
    	return result;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
        
    
    
    
    
    
    
    
    
    
    @Override
	public String toString() {
    	String playerString = String.format(
                "PlayerID: %d,PlayerState: %d,RoomId: %d,Coins: %d,",
                getPlayerID(), getPlayerState(),
                getPlayerDetail().getCurrentRoom() != null ? getPlayerDetail().getCurrentRoom().getRoomId() : -1,
                		getPlayerDetail().getPlayerInfo().getCoins());
        return playerString + "IsPlaying: " + getPlayerDetail().getIsPlaying() + 
        		"IsRobot: " + getPlayerDetail().getIsRobot() + 
        		"UserName: " + getPlayerDetail().getPlayerInfo().getUserName();
	}
    
       
    
    
    
}



