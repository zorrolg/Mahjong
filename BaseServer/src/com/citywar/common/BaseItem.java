/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.common;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.citywar.dice.entity.CardInfo;
import com.citywar.dice.entity.ItemTemplateInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserItemInfo;

/**
 * 物品的内存表示, 主要处理逻辑方面, 解除 物品, 物品模板, 逻辑之间的耦合
 * 
 * @author tracy
 * @date 2011-12-19
 * @version
 * 
 */
public class BaseItem implements Cloneable
{
    private static final Logger logger = Logger.getLogger(BaseItem.class.getName());

    /**
     * 玩家身上物品信息
     */
    private UserItemInfo userItemInfo = null;
    /**
     * 物品模板信息
     */
    private ItemTemplateInfo itemTemplateInfo = null;
    /**
     * 物品模板信息
     */
    private CardInfo itemCardInfo = null;
    /**
     * 玩家信息
     */
    private PlayerInfo playerInfo = null;
    /**
     * 用于一些信息的缓存
     */
    private HashMap<String, Object> tempInfoMap = null;

    /**
     * @param userItemInfo
     * @param itemTemplateInfo
     * @param playerInfo
     */
    public BaseItem(UserItemInfo userItemInfo,
            ItemTemplateInfo itemTemplateInfo, PlayerInfo playerInfo)
    {
        super();
        this.userItemInfo = userItemInfo;
        this.itemTemplateInfo = itemTemplateInfo;
        this.playerInfo = playerInfo;
        this.userItemInfo.setUserId(playerInfo.getUserId());
        this.tempInfoMap = new HashMap<String, Object>();
    }
    
    public BaseItem(UserItemInfo userItemInfo,
    		CardInfo cardInfo, PlayerInfo playerInfo)
    {
        super();
        this.userItemInfo = userItemInfo;
        this.itemCardInfo = cardInfo;
        this.playerInfo = playerInfo;
        this.userItemInfo.setUserId(playerInfo.getUserId());
        this.tempInfoMap = new HashMap<String, Object>();
    }

    public BaseItem(ItemTemplateInfo templateInfo)
    {
        this.itemTemplateInfo = templateInfo;
        userItemInfo = new UserItemInfo();
        playerInfo = new PlayerInfo();
        if (templateInfo != null)
        {
            userItemInfo.setTemplateId(templateInfo.getTemplateId());
        }
        if (tempInfoMap == null)
        {
            tempInfoMap = new HashMap<String, Object>();
        }
    }

    public BaseItem(CardInfo cardInfo)
    {
        this.itemCardInfo = cardInfo;
        userItemInfo = new UserItemInfo();
        playerInfo = new PlayerInfo();
        if (cardInfo != null)
        {
            userItemInfo.setTemplateId(cardInfo.getCardTypeId());
        }
        if (tempInfoMap == null)
        {
            tempInfoMap = new HashMap<String, Object>();
        }
    }
    
    public BaseItem()
    {
        this.userItemInfo = new UserItemInfo();
        this.itemTemplateInfo = new ItemTemplateInfo();
        this.playerInfo = new PlayerInfo();
    }

    /**
     * @return the userItemInfo
     */
    public UserItemInfo getUserItemInfo()
    {
        return userItemInfo;
    }

    /**
     * @param userItemInfo
     *            the userItemInfo to set
     */
    public void setUserItemInfo(UserItemInfo userItemInfo)
    {
        this.userItemInfo = userItemInfo;
    }

    /**
     * @return the itemTemplateInfo
     */
    public ItemTemplateInfo getItemTemplateInfo()
    {
        return itemTemplateInfo;
    }

    /**
     * @param itemTemplateInfo
     *            the itemTemplateInfo to set
     */
    public void setItemTemplateInfo(ItemTemplateInfo itemTemplateInfo)
    {
        this.itemTemplateInfo = itemTemplateInfo;
    }

    /**
     * @return the itemTemplateInfo
     */
    public CardInfo getCardInfo()
    {
        return itemCardInfo;
    }

    /**
     * @param itemTemplateInfo
     *            the itemTemplateInfo to set
     */
    public void setCardInfo(CardInfo itemCardInfo)
    {
        this.itemCardInfo = itemCardInfo;
    }
    
    
    
    /**
     * @return the playerInfo
     */
    public PlayerInfo getPlayerInfo()
    {
        return playerInfo;
    }

    /**
     * @param playerInfo
     *            the playerInfo to set
     */
    public void setPlayerInfo(PlayerInfo playerInfo)
    {
        this.playerInfo = playerInfo;
    }

    /**
     * @return the tempInfoMap
     */
    public HashMap<String, Object> getTempInfoMap()
    {
        return tempInfoMap;
    }

    /**
     * @param tempInfoMap
     *            the tempInfoMap to set
     */
    public void setTempInfoMap(HashMap<String, Object> tempInfoMap)
    {
        this.tempInfoMap = tempInfoMap;
    }

    public static BaseItem createFromTemplate(ItemTemplateInfo template,
            int count)
    {
        BaseItem baseItem = null;
        try
        {
            baseItem = new BaseItem(template);
            UserItemInfo userItem = baseItem.getUserItemInfo();
            userItem.setCount(count);
            userItem.setItemTypeId(1);
            userItem.setTemplateId(template.getTemplateId());
            userItem.setUsed(false);
            userItem.setExist(true);
            
        }
        catch (Exception e)
        {
            logger.error("[ BaseItem : createFromTemplate ]", e);
        }
        return baseItem;
    }
    
    public static BaseItem createFromCard(CardInfo card,
            int count)
    {
        BaseItem baseItem = null;
        try
        {
            baseItem = new BaseItem(card);
            UserItemInfo userItem = baseItem.getUserItemInfo();
            userItem.setCount(count);
            userItem.setItemTypeId(2);
            userItem.setTemplateId(card.getCardTypeId());
            userItem.setUsed(false);
            userItem.setExist(true);
            
        }
        catch (Exception e)
        {
            logger.error("[ BaseItem : createFromTemplate ]", e);
        }
        return baseItem;
    }
    
    
}
