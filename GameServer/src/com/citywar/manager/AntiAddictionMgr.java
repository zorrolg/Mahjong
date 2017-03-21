package com.citywar.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.game.GamePlayer;
import com.citywar.gameutil.UserProperty;
import com.citywar.util.Config;
import com.citywar.util.IDCard;

/**
 * 防沉迷系统管理器。实名制
 * @author shanfeng.cao
 * 
 */
public class AntiAddictionMgr {
	
    private static Logger logger = Logger.getLogger(AntiAddictionMgr.class.getName());

    private static ReadWriteLock lock = new ReentrantReadWriteLock(false);
    
    static boolean STRAT_SYSTEM = false; //是否启动系统
    
    static boolean STRAT_REAL_SYSTEM = false; //是否启动实名制系统
    
    static final int FIRST_GRADE = 3; // 0-3小时
    
    static final int SECOND_GRADE = 5; //3-5小时

    private static List<GamePlayer> allAntiAddictionPlayers;

    
	public static boolean init() {
		allAntiAddictionPlayers =  new ArrayList<GamePlayer>();
		String isStart = Config.getValue("isStartAntiAddiction");
		if(isStart.equals("true")) {
			STRAT_SYSTEM = true;
			STRAT_REAL_SYSTEM = true;
			//System.out.println("防沉迷系统管理器===成功启动");
		} else {
			//System.out.println("防沉迷系统管理器===没有启动");
		}
		return true;
	}

	/**
	 * 定时查看玩家是否满足需要提醒
	 */
	public static void checkPlayeAntiAddiction() {
		if( ! STRAT_SYSTEM) {
			return;
		}
		lock.writeLock().lock(); 
        try
        {
            for(GamePlayer player : allAntiAddictionPlayers) {
            	UserProperty userProperty = player.getUserProperty();
             	if(null != userProperty) {//计算玩家的收入比例，和防沉迷提醒
             		userProperty.computePlayeIncomeRatio();
             		userProperty.sendPlayeAntiAddiction();
             	}
             }
        } catch (Exception e) {
            logger.error("[ AntiAddictionMgr : checkPlayeAntiAddiction ]", e);
        } finally {
            lock.writeLock().unlock();
        }
	}
	
	/**
	 * 增加需要提醒的玩家
	 */
	public static void addAntiAddictionPlayer(GamePlayer player) {
		lock.writeLock().lock(); 
        try
        {
    		if( ! allAntiAddictionPlayers.contains(player)) {
    			allAntiAddictionPlayers.add(player);
    		}
        } catch (Exception e) {
            logger.error("[ AntiAddictionMgr : addAntiAddictionPlayer ]", e);
        } finally {
            lock.writeLock().unlock();
        }
	}
	
	/**
	 * 移除需要提醒的玩家
	 */
	public static void removeAntiAddictionPlayer(GamePlayer player) {
		lock.writeLock().lock(); 
        try
        {
    		if(allAntiAddictionPlayers.contains(player)) {
    			allAntiAddictionPlayers.remove(player);
    		}
        } catch (Exception e) {
            logger.error("[ AntiAddictionMgr : removeAntiAddictionPlayer ]", e);
        } finally {
            lock.writeLock().unlock();
        }
	}
	
	/**
	 * 查看是否需要防沉迷
	 * @return
	 */
    public static boolean isStartAntiAddictionSystem() {
		return STRAT_SYSTEM;
	}

	/**
     * 身份证ID是有效的
     */
    public static boolean IDCardValidate(String IDStr) {
    	return IDCard.IDCardValidate(IDStr).equals("");
    }
    
    /**
     * 
     * 得到防沉迷的提示语句
     * @param perCapitaTime
     * @return
     */
    public static String getAntiAddictionSendStr(long perCapitaTime) {
    	String resultStr = "";
    	if( ! STRAT_SYSTEM) {
    		return resultStr;
    	}
    	long minuteTime = perCapitaTime / 60;
    	long timeRemainder = minuteTime % 15;//15分钟
    	if(timeRemainder == 0) {//15分钟
    		if(minuteTime >= 5 * 60) {
    			resultStr = "累计在线已满5小时，游戏收益已降为零，建议您立即下线。";
    			return resultStr;
    		}
    		timeRemainder = minuteTime % 30;
    		if(timeRemainder == 0) {//30分钟
    			if(minuteTime >= 3 * 60) {
    				resultStr = "累计在线已满3小时，游戏收益下降50％，请您合理安排学习生活。";
        			return resultStr;
    			}
    			timeRemainder = minuteTime % 60;
    			if(timeRemainder == 0) {//60分钟
    				if(minuteTime >= 1 * 60) {
    					timeRemainder = minuteTime/60;
        				resultStr = "您累计在线时间已满" + timeRemainder + "小时";
            			return resultStr;
        			}
    	    	}
        	}
    	}
		return resultStr;
    }

	public static int getAntiAddictionIncomeRatio(long userOnlineTime) {
		long minuteTime = userOnlineTime / 60 / 60;
		int incomeRatio = 100;//正常收益
		if(STRAT_SYSTEM) {
			if(minuteTime >= 3 && minuteTime < 5) {
				incomeRatio = 50;//降为正常收益的50％
			} else if(minuteTime >= 5) {
				incomeRatio = 0;//降为0
			}
		}
		return incomeRatio;
	}

	public static void stratAntiAddiction() {
		STRAT_SYSTEM = true;
	}

	public static void stopAntiAddiction() {
		STRAT_SYSTEM = false;
	}

	public static void stratRealInformation() {
		STRAT_REAL_SYSTEM = true;
	}

	public static void stopRealInformation() {
		STRAT_REAL_SYSTEM = false;
	}

	public static boolean isStrat() {
		return STRAT_SYSTEM;
	}

	public static boolean isStratReal() {
		return STRAT_REAL_SYSTEM;
	}
	
}
