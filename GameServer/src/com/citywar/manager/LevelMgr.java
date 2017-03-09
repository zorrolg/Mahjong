/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.LevelInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.util.Config;


/**
 * 管理等级相关信息
 * 
 * @author tracy
 * @date 2011-12-27
 * @version
 * 
 */
public class LevelMgr
{
    private static Map<Integer, LevelInfo> levelMap;

    private static ReadWriteLock rwLock;

    private static final Logger logger = Logger.getLogger(LevelMgr.class.getName());

    private static int levelType = 0;
    /**
     * 等级管理初始化
     * 
     * @return
     */
    public static boolean init()
    {
    	levelType =  Integer.parseInt(Config.getValue("level.type"));
        rwLock = new ReentrantReadWriteLock();
        levelMap = new LinkedHashMap<Integer, LevelInfo>();
        return reload();
    }

    /**
     * 重新载入等级信息
     * 
     * @return
     */
    public static boolean reload()
    {
        try
        {
            Map<Integer, LevelInfo> tempMap = new LinkedHashMap<Integer, LevelInfo>();
            if (loadLevelFromDb(tempMap))
            {
                rwLock.writeLock().lock();
                try
                {
                    levelMap = tempMap;
                    return true;
                }
                finally
                {
                    rwLock.writeLock().unlock();
                }

            }
            return true;
        }
        catch (Exception e)
        {
            logger.error("[ LevelMgr : reload ]", e);
        }
        return false;
    }

    /**
     * 从数据库加载所有的等级信息
     * 
     * @param tempMap
     *            存储等级信息的 map, level为键, levelInfo 为值
     * @return
     */
    private static boolean loadLevelFromDb(Map<Integer, LevelInfo> tempMap)
    {
        try
        {
            List<LevelInfo> tempInfos = PlayerBussiness.getAllLevelInfos();
            for (LevelInfo level : tempInfos)
            {
                if (!tempMap.containsKey(level.getLevel()))
                    tempMap.put(level.getLevel(), level);
            }
            return true;
        }
        catch (Exception e)
        {
            logger.error("[ LevelMgr : loadLevelFromDb ]", e);
        }

        return false;
    }

    /**
     * 获取指定经验值对应的等级
     * 
     * @param gp
     *            指定的经验值
     * @return
     */
    public static int getLevel(PlayerInfo plyInfo)
    {
    	
    	int number = getLevelPara(plyInfo);
    	
    	
        int level = 1;
        for (LevelInfo info : levelMap.values())
        {
            if (number >= info.getGp())
            {
                level = info.getLevel();//level > info.getLevel() ? level : info.getLevel();
            } else {
            	break;
            }
        }
        if(level > getMaxLevel()) {//避免玩家等级过高
        	level = getMaxLevel();
        }
        
//        //System.out.println("getLevel=========================" + gp + "==============" + level);
        
        return level;
    }

    /**
     * 获取指定等级对应的最低经验值
     * 
     * @param level
     *            指定的等级
     * @return
     */
    public static int getGp(int level)
    {
        return levelMap.containsKey(level) ? levelMap.get(level).getGp() : 0;
    }
    
    /**
     * 获得某等级用户所需升级经验值
     * 
     * @param
     */
	public static int getUpgradeGp(int level) {
		int nowLevelGp = getGp(level - 1);//用户所处的等级的最低经验值
		int upLevelGp = getGp(level);//用户升级所需要的经验值
		return upLevelGp - nowLevelGp > 0 ? upLevelGp - nowLevelGp : 0;
	}

    /**
     * 获得当前用户升级经验值
     * 
     * @param
     */
	public static int getUserUpgradeGp(PlayerInfo plyInfo) {
		
		int number = getLevelPara(plyInfo);
		
//		int nowGp = gp;//用户当前的经验值
		//用户升级所需要的经验值
		int nowLevel = LevelMgr.getLevel(plyInfo);
		int nowLevelGp = LevelMgr.getGp(nowLevel + 1);
		return number - nowLevelGp > 0 ? number - nowLevelGp : 0;
	}
	
	/**
     * 获得当前用户头衔
     * 
     * @param
     */
	public static String getLevelTitle(int level) {
		
		String title = levelMap.containsKey(level) ? levelMap.get(level).getTitle() : "";
		
//		//System.out.println("getLevelTitle=========================" + level + "==============" + title);
		return title;
		
	}
	
	
    /**
     * 获取指定等级对应的醉酒度
     * 
     * @param level
     *            指定的等级
     * @return
     */
    public static int getDrunkLevel(int level)
    {
        return levelMap.containsKey(level) ? levelMap.get(level).getDrunkFullLevel()
                : 0;
    }
    
    /**
     * 获取指定等级对应的赎金
     * 
     * @param level
     *            指定的赎金
     * @return
     */
    public static int getRansomMoney(int level)
    {
        return levelMap.containsKey(level) ? levelMap.get(level).getRansomMoney()
                : 0;
    }
    
    /**
     * 获取当前版本的最高等级
     * 
     * @return
     */
    public static int getMaxLevel()
    {
        return levelMap.size();
    }

    
    public static List<LevelInfo> getAllLevel()
    {
    	List<LevelInfo> list = new ArrayList<LevelInfo>();
		for(LevelInfo level : levelMap.values()) {
			list.add(level);
		}
		return list;
    }
    
    public static int getLevelPara(PlayerInfo plyInfo)
    {
    	int number = 0;
    	if(levelType == 0)
    	{
    		number = plyInfo.getCoins();
    	}
    	else if(levelType == 1)
    	{
    		number = plyInfo.getGp();
    	}
    	else 
    	{
    		number= plyInfo.getCharmValve();
    	}
    	return number;
    }
}
