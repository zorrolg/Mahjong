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

import com.citywar.bll.ItemBussiness;
import com.citywar.dice.entity.ItemTemplateInfo;


/**
 * 物品管理
 * 
 * @author tracy
 * @date 2011-12-30
 * @version
 * 
 */
public class ItemMgr
{
    private static final Logger LOGGER = Logger.getLogger(ItemMgr.class.getName());

    private static Map<Integer, ItemTemplateInfo> templatesMap;
    
    



	private static ReadWriteLock rwLock;

    public static boolean reload()
    {
        try
        {
            Map<Integer, ItemTemplateInfo> tempMap = new HashMap<Integer, ItemTemplateInfo>();
            if (loadFromDb(tempMap))
            {
                rwLock.writeLock().lock();

                try
                {
                    templatesMap = tempMap;
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
        templatesMap = new HashMap<Integer, ItemTemplateInfo>();
        return reload();
    }

    private static boolean loadFromDb(Map<Integer, ItemTemplateInfo> tempMap)
    {
        List<ItemTemplateInfo> tempList = ItemBussiness.getAllTemplateInfos();
        for (ItemTemplateInfo itemTempInfo : tempList)
        {
            Integer iTemplateId = itemTempInfo.getTemplateId();
            if (!tempMap.containsKey(iTemplateId))
            {
                tempMap.put(iTemplateId, itemTempInfo);
            }
        }
        return true;
    }

    /**
     * 根据模板 Id 取得模板信息
     * 
     * @param templateId
     * @return
     */
    public static ItemTemplateInfo findItemTemplate(int templateId)
    {
        if (templatesMap == null)
            init();
        rwLock.writeLock().lock();
        try
        {
            if (templatesMap.containsKey(templateId))
                return templatesMap.get(templateId);
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
     * 根据模板Type类型取得模板信息
     * 
     * @param templateId
     * @return
     */
    public static ItemTemplateInfo findItemTemplateByType(int templateType)
    {
        if (templatesMap == null)
            init();
        rwLock.writeLock().lock();
        try
        {
            for(ItemTemplateInfo template : templatesMap.values()) {
            	if(template.getType() == templateType) {
                    return template;
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
        return null;

    }
    

    

    

}
