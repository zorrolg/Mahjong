/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.RobotLevelBussiness;
import com.citywar.dice.entity.RobotLevelInfo;


/**
 * 发奖管理类
 * 
 * @author tracy
 * @date 2011-12-27
 * @version
 * 
 */
public class RobotLevelMgr
{
	private static final Logger LOGGER = Logger.getLogger(ItemMgr.class.getName());

    private static Map<Integer,RobotLevelInfo> robotLevelInfo = new HashMap<Integer, RobotLevelInfo>();
    
    
    private static ReadWriteLock rwLock = new ReentrantReadWriteLock();
        

    public static boolean reload()
    {
        try
        {
            Map<Integer, RobotLevelInfo> tempMap = new HashMap<Integer, RobotLevelInfo>();
            if (loadFromDb(tempMap))
            {
                rwLock.writeLock().lock();

                try
                {
                	robotLevelInfo = tempMap;                    
                    return true;
                }
                catch (Exception e)
                {
                    LOGGER.error("ItemMgr reload exception:" + e.toString());
                }
                finally
                {
                    rwLock.writeLock().unlock();
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("ItemMgr reload exception:" + e);
        }
        return false;
    }

    public static boolean init()
    {
        return reload();
    }

    private static boolean loadFromDb(Map<Integer, RobotLevelInfo> tempMap)
    {
    	
        List<RobotLevelInfo> tempList = RobotLevelBussiness.getAllRobotLevel();
        for (RobotLevelInfo robotLevelInfo : tempList)
        {
            Integer ilevel = robotLevelInfo.getLevel();
            if (!tempMap.containsKey(ilevel))
            {
            	tempMap.put(ilevel, robotLevelInfo);
            }
        }
        return true;
    }
    
    
    /**
     * 根据建筑 Id 取得
     * 
     * @param cardId
     * @return
     */
    public static RobotLevelInfo findRobotLevel(int robotLevelId)
    {
        rwLock.writeLock().lock();
        try
        {
            if (robotLevelInfo.containsKey(robotLevelId))
                return robotLevelInfo.get(robotLevelId);
        }
        catch (Exception e)
        {
        }
        finally
        {
            rwLock.writeLock().unlock();
        }
        return null;

    }
    
    
    
    
    
    
    
}
