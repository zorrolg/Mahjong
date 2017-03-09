/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.StageBussiness;
import com.citywar.bll.StagePrizeBussiness;
import com.citywar.dice.entity.StageInfo;
import com.citywar.dice.entity.StagePrizeInfo;
import com.citywar.dice.entity.StageRoundInfo;



/**
 * 发奖管理类
 * 
 * @author tracy
 * @date 2011-12-27
 * @version
 * 
 */
public class StageMgr
{
	private static final Logger LOGGER = Logger.getLogger(ItemMgr.class.getName());

    private static Map<Integer,StageInfo> stageInfo = null;
    private static Map<Integer,Map<Integer, StagePrizeInfo>> stagePrizeInfo = null;
    private static Map<Integer,List<StageRoundInfo>> stageRoundInfo = null;
    
    private static ReadWriteLock rwLock;

    public static boolean reload()
    {
        try
        {
            Map<Integer, StageInfo> tempMap = new HashMap<Integer, StageInfo>();
            Map<Integer, Map<Integer, StagePrizeInfo>> tempPrizeMap = new HashMap<Integer, Map<Integer, StagePrizeInfo>>();
            Map<Integer, List<StageRoundInfo>> tempStageRound = new HashMap<Integer, List<StageRoundInfo>>();
            
            if (loadFromDb(tempMap, tempPrizeMap, tempStageRound))
            {
                rwLock.writeLock().lock();

                try
                {
                	stageInfo = tempMap;
                	stagePrizeInfo = tempPrizeMap;
                	stageRoundInfo = tempStageRound;
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

    private static boolean loadFromDb(Map<Integer, StageInfo> tempMap, Map<Integer, Map<Integer, StagePrizeInfo>> tempPrizeMap, Map<Integer, List<StageRoundInfo>> tempStageRound)
    {
    	
        List<StageInfo> tempList = StageBussiness.getAllStegesInfo(); //BuildBussiness.getAllBuildsInfo();
        for (StageInfo stageInfo : tempList)
        {
            Integer iStage = stageInfo.getStageId();
            if (!tempMap.containsKey(iStage))
            {
                tempMap.put(iStage, stageInfo);
            }
        }
        
        List<StagePrizeInfo> tempPrizeList = StagePrizeBussiness.getStagePrizeList();
        for (StagePrizeInfo stageInfo : tempPrizeList)
        {        	        	
            Integer iStage = stageInfo.getStageId();           
            if (!tempPrizeMap.containsKey(iStage))
            {
            	Map<Integer, StagePrizeInfo> listPrize = new HashMap<Integer, StagePrizeInfo>();
            	tempPrizeMap.put(iStage, listPrize);
            }
            
            tempPrizeMap.get(iStage).put(stageInfo.getIndex(), stageInfo);
        }
        
        
        List<StageRoundInfo> tempRoundList = StageBussiness.getAllStegesRoundInfo();
        for (StageRoundInfo stageInfo : tempRoundList)
        {        	        	
            Integer iStage = stageInfo.getStageId();           
            if (!tempStageRound.containsKey(iStage))
            {
            	List<StageRoundInfo> listRound = new ArrayList<StageRoundInfo>();
            	tempStageRound.put(iStage, listRound);
            }
            
            tempStageRound.get(iStage).add(stageInfo);
        }
        
        return true;
    }
    
    
    /**
     * 根据建筑 Id 取得建筑信息
     * 
     * @param buildId
     * @return
     */
    public static StageInfo findStage(int stageId)
    {
        if (stageInfo == null)
            init();
        rwLock.writeLock().lock();
        try
        {
            if (stageInfo.containsKey(stageId))
                return stageInfo.get(stageId);
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
    
    public static Map<Integer, StagePrizeInfo> findStagePrize(int stageId)
    {
        if (stagePrizeInfo == null)
            init();
        rwLock.writeLock().lock();
        try
        {
            if (stagePrizeInfo.containsKey(stageId))
                return stagePrizeInfo.get(stageId);
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
    
    public static StagePrizeInfo findStagePrizeByIndex(int stageId, int index)
    {
        if (stagePrizeInfo == null)
            init();
        rwLock.writeLock().lock();
        try
        {
            if (stagePrizeInfo.containsKey(stageId) && stagePrizeInfo.get(stageId).containsKey(index))
                return stagePrizeInfo.get(stageId).get(index);
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
    
    public static List<StageRoundInfo> findStageRound(int stageId)
    {
        if (stageRoundInfo == null)
            init();
        rwLock.writeLock().lock();
        try
        {
            if (stageRoundInfo.containsKey(stageId))
                return stageRoundInfo.get(stageId);
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
    
    
    public static Map<Integer, StageInfo> getAllStage()
    {

        rwLock.writeLock().lock();
        try
        {
        	return stageInfo;
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
    
    
    public static StageRoundInfo getStageRound(int stageId, int index)
    {
    	StageRoundInfo stageRound = null;
        rwLock.writeLock().lock();
        try
        {
        	if(stageRoundInfo.containsKey(stageId))
        	{
        		for(StageRoundInfo round : stageRoundInfo.get(stageId))
        		{
        			if(round.getRoundIndex() == index)
        			{
        				stageRound = round;
        				break;
        			}
        		}
        	}
        }
        catch (Exception e)
        {
        }
        finally
        {
            rwLock.writeLock().unlock();
        }
        return stageRound;
    }
}
