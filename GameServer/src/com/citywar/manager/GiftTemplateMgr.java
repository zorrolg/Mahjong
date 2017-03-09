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

import com.citywar.bll.GiftTemplateBussiness;
import com.citywar.dice.entity.GiftTemplateInfo;

/**
 * 礼品模板管理
 * 
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version
 * 
 */
public class GiftTemplateMgr
{
    private static final Logger LOGGER = Logger.getLogger(GiftTemplateMgr.class.getName());

    private static Map<Integer, GiftTemplateInfo> giftTemplatesMap;

    private static ReadWriteLock rwLock;

    public static boolean reload()
    {
        try
        {
        	//TreeMap是排好序的。而hashmap是无序的。LinkedHashMap更加快一些
            Map<Integer, GiftTemplateInfo> tempMap = new LinkedHashMap<Integer, GiftTemplateInfo>();
            if (loadFromDb(tempMap))
            {
                rwLock.writeLock().lock();
                try
                {
                	giftTemplatesMap = tempMap;
                    return true;
                }
                catch (Exception e)
                {
                    LOGGER.error("GiftTemplateMgr reload exception:" + e.toString());
                }
                finally
                {
                    rwLock.writeLock().unlock();
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("GiftTemplateMgr reload exception:" + e);
        }
        return false;
    }

    public static boolean init()
    {
        rwLock = new ReentrantReadWriteLock();
        return reload();
    }

    private static boolean loadFromDb(Map<Integer, GiftTemplateInfo> tempMap)
    {
        List<GiftTemplateInfo> tempList = GiftTemplateBussiness.getAllGiftTemplateInfos();
        for (GiftTemplateInfo giftTempInfo : tempList)
        {
            Integer iTemplateId = giftTempInfo.getGiftId();
            if (!tempMap.containsKey(iTemplateId))
            {
                tempMap.put(iTemplateId, giftTempInfo);
            }
        }
        return true;
    }

    /**
     * 根据礼品模板 Id 取得模板信息
     * 
     * @param templateId
     * @return
     */
	public static GiftTemplateInfo getGiftTemplateById(int templateId)
    {
        if (giftTemplatesMap == null)
        	return null;
        rwLock.writeLock().lock();
        try
        {
            if (giftTemplatesMap.containsKey(templateId)) {
                return giftTemplatesMap.get(templateId);
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

    /**
     * 根据礼品模板 Id 取得礼品的价格
     * 
     * @param giftId
     * @return
     */
	public static int getGiftPriceById(int giftId)
    {
        if (giftTemplatesMap == null)
        	return Integer.MAX_VALUE;
        rwLock.writeLock().lock();
        try
        {
        	if(giftTemplatesMap.containsKey(giftId)) {
        		return giftTemplatesMap.get(giftId).getGiftPrice();
        	}
        }
        catch (Exception e)
        {
        }
        finally
        {
            rwLock.writeLock().unlock();
        }
        return Integer.MAX_VALUE;
    }

    /**
     * 根据礼品模板 Id 取得礼品的魅力值
     * 
     * @param giftId
     * @return
     */
	public static int getGiftCharmValveById(int giftId)
    {
        if (giftTemplatesMap == null)
        	return Integer.MAX_VALUE;
        rwLock.writeLock().lock();
        try
        {
        	if(giftTemplatesMap.containsKey(giftId)) {
        		return giftTemplatesMap.get(giftId).getGiftCharmValue();
        	}
        }
        catch (Exception e)
        {
        }
        finally
        {
            rwLock.writeLock().unlock();
        }
        return Integer.MAX_VALUE;
    }
	
    /**
     * 根据礼品存在的位置取得相应模板信息
     * 
     * @param giftBuyType
     * @return
     */
	public static List<GiftTemplateInfo> getGiftTemplatesByBuyType(int giftBuyType)
    {
        if (giftTemplatesMap == null)
        	return null;
        List<GiftTemplateInfo> templatesList = new ArrayList<GiftTemplateInfo>();
        try
        {
            rwLock.writeLock().lock();
        	for (GiftTemplateInfo giftTempInfo : giftTemplatesMap.values())
            {
        		if(giftTempInfo.getGiftBuyType() == giftBuyType) {
        			templatesList.add(giftTempInfo);
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
        return templatesList;
    }
}
