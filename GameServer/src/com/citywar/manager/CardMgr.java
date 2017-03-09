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

import com.citywar.bll.CardBussiness;
import com.citywar.dice.entity.CardInfo;
import com.citywar.dice.entity.CardTypeInfo;


/**
 * 发奖管理类
 * 
 * @author tracy
 * @date 2011-12-27
 * @version
 * 
 */
public class CardMgr
{
	private static final Logger LOGGER = Logger.getLogger(ItemMgr.class.getName());

    private static Map<Integer,CardInfo> cardInfo = new HashMap<Integer, CardInfo>();
    private static Map<Integer,CardTypeInfo> cardTypeInfo = new HashMap<Integer, CardTypeInfo>();
    
    private static ReadWriteLock rwLock;
    

    private static String firstDevelopCard = new String();
    
    private static String firstFactoryCard = new String();
    

    public static boolean reload()
    {
        try
        {
            Map<Integer, CardInfo> tempMapCard = new HashMap<Integer, CardInfo>();
            Map<Integer, CardTypeInfo> tempMapCardType = new HashMap<Integer, CardTypeInfo>();
            if (loadFromDb(tempMapCard, tempMapCardType))
            {
                rwLock.writeLock().lock();

                try
                {
                	cardInfo = tempMapCard;
                	cardTypeInfo = tempMapCardType;
                	
                	
                	List<Integer> firstDevelopCardList = new ArrayList<Integer>();
                	List<Integer> firstFactoryCardList = new ArrayList<Integer>();
                	
                	for (CardInfo itemCardInfo : cardInfo.values())
                    {
                		if(itemCardInfo.getBuildDevelopLevel() == 1 && !firstDevelopCardList.contains(itemCardInfo.getCardTypeId()))
                			firstDevelopCardList.add(itemCardInfo.getCardTypeId());
                		
                		
                		if(itemCardInfo.getBuildFactoryLevel() == 1 && !firstFactoryCardList.contains(itemCardInfo.getCardTypeId()))
                			firstFactoryCardList.add(itemCardInfo.getCardTypeId());
                    }
                	
                	
                	for (Integer typeId : firstDevelopCardList)
                    {
                		firstDevelopCard += (typeId + "=1-");
                    }
                    
                	for (Integer typeId : firstFactoryCardList)
                    {
                		firstFactoryCard += (typeId + "=1-");
                    }
                	
                    
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

    private static boolean loadFromDb(Map<Integer, CardInfo> tempMap, Map<Integer, CardTypeInfo> tempMapType)
    {
    	
        List<CardInfo> tempList = CardBussiness.getAllCardsInfo();        
        for (CardInfo itemCardInfo : tempList)
        {
            Integer iCardId = itemCardInfo.getCardId();
            if (!tempMap.containsKey(iCardId))
            {
                tempMap.put(iCardId, itemCardInfo);
            }
        }
        
        
        List<CardTypeInfo> tempListType = CardBussiness.getAllCardTypeInfo();
        for (CardTypeInfo itemCardInfo : tempListType)
        {
            Integer iCardId = itemCardInfo.getCardTypeId();
            if (!tempMapType.containsKey(iCardId))
            {
            	tempMapType.put(iCardId, itemCardInfo);
            }
        }
        return true;
    }
    
    
    public static List<CardTypeInfo> getCardTypeList()
    {
        
    	List<CardTypeInfo> cardList = new ArrayList<CardTypeInfo>();
    	        
    	for (CardTypeInfo itemCardInfo : cardTypeInfo.values())
        {   		
    		cardList.add(itemCardInfo);
        }

        return cardList;

    }
    
    
    /**
     * 根据建筑 Id 取得
     * 
     * @param cardId
     * @return
     */
    public static CardInfo findCard(int cardId)
    {
        if (cardInfo == null)
            init();
        rwLock.writeLock().lock();
        try
        {
            if (cardInfo.containsKey(cardId))
                return cardInfo.get(cardId);
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
     * @param cardId
     * @return
     */
    public static CardInfo findCardByType(int cardTypeId,int level)
    {
        if (cardInfo == null)
            init();
        rwLock.writeLock().lock();
        try
        {
        	for (CardInfo itemCardInfo : cardInfo.values())
            {
        		if(itemCardInfo.getCardTypeId() == cardTypeId && itemCardInfo.getCardLevel() == level)
        			return itemCardInfo;
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
     * 根据建筑 Id 取得建筑信息
     * 
     * @param cardId
     * @return
     */
    public static List<Integer> findCardByFactoryLevel(int factoryLevel)
    {
        
    	List<Integer> cardList = new ArrayList<Integer>();
    	
        rwLock.writeLock().lock();
        try
        {
        	for (CardInfo itemCardInfo : cardInfo.values())
            {
        		if(itemCardInfo.getBuildFactoryLevel() == factoryLevel && !cardList.contains(itemCardInfo.getCardTypeId()))
        			cardList.add(itemCardInfo.getCardTypeId());
            }
        }
        catch (Exception e)
        {
        }
        finally
        {
            rwLock.writeLock().unlock();
        }
        return cardList;

    }
    
    

    public static String getFirstDevelopCard()
    {
    	return firstDevelopCard;
    }

    public static String getFirstFactoryCard()
    {
    	return firstFactoryCard;
    }
    
    
    
    
    
    
}
