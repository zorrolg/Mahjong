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

import com.citywar.bll.BuildBussiness;
import com.citywar.dice.entity.BuildInfo;

/**
 * 发奖管理类
 * 
 * @author tracy
 * @date 2011-12-27
 * @version
 * 
 */
public class BuildMgr
{
	private static final Logger LOGGER = Logger.getLogger(ItemMgr.class.getName());

    private static Map<Integer,BuildInfo> buildInfo = new HashMap<Integer, BuildInfo>();
    
    
    private static ReadWriteLock rwLock;

    public static boolean reload()
    {
        try
        {
            Map<Integer, BuildInfo> tempMap = new HashMap<Integer, BuildInfo>();
            if (loadFromDb(tempMap))
            {
                rwLock.writeLock().lock();

                try
                {
                	buildInfo = tempMap;
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
        rwLock = new ReentrantReadWriteLock();
        return reload();
    }

    private static boolean loadFromDb(Map<Integer, BuildInfo> tempMap)
    {
    	
        List<BuildInfo> tempList = BuildBussiness.getAllBuildsInfo();
        for (BuildInfo itemBuildInfo : tempList)
        {
            Integer iBuildId = itemBuildInfo.getBuildId();
            if (!tempMap.containsKey(iBuildId))
            {
                tempMap.put(iBuildId, itemBuildInfo);
            }
        }
        return true;
    }
    
    
    /**
     * 根据建筑 Id 取得建筑信息
     * 
     * @param buildId
     * @return
     */
    public static BuildInfo findBuild(int buildId)
    {
        if (buildInfo == null)
            init();
        rwLock.writeLock().lock();
        try
        {
            if (buildInfo.containsKey(buildId))
                return buildInfo.get(buildId);
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
    
    
    
    /**
     * 根据建筑 Id 取得建筑信息
     * 
     * @param buildId
     * @return
     */
    public static BuildInfo findBuildByType(int buildTypeId,int level)
    {
        if (buildInfo == null)
            init();
        rwLock.writeLock().lock();
        try
        {
        	for (BuildInfo build : buildInfo.values())
            {
        		if(build.getBuildTypeId() == buildTypeId && build.getBuildLevel() == level)
        			return build;
            }
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
