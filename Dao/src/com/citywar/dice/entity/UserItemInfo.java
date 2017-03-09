/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;

import java.sql.Timestamp;

/**
 * 玩家身上物品信息表
 * 
 * @author tracy
 * @date 2011-12-19
 * @versions *
 */
public class UserItemInfo extends DataObject
{
    /**
     * 物品 id
     */
    private int itemId;
    /**
     * 用户 id
     */
    private int userId;
    /**
     * 用户 id
     */
    private int itemType;
    /**
     * 模板 id
     */
    private int templateId;
    /**
     * 拥有数量
     */
    private int count;
    /**
     * 是否存在的标识, 用于物品的逻辑删除; 0 -- 不存在, 已逻辑删除; 1 -- 存在
     */
    private boolean isExist;
    /**
     * 是否使用的标识, 用于物品使用状态的逻辑表示; 0 -- 未使用; 1 -- 使用状态
     */
    private boolean isUsed;
    /**
     * 有效期
     */
    private Timestamp lastValidityTime;

    /**
     * 
     */
    public UserItemInfo()
    {
        super();
    }
    
    public UserItemInfo(int itemType,int templateId, int userId, int count)
    {
        this.templateId = templateId;
        this.userId = userId;
        this.itemType = itemType;
        this.count = count;
        this.isExist = true;
    }

    /**
     * @return the itemId
     */
    public int getItemId()
    {
        return itemId;
    }

    /**
     * @param itemId
     *            the itemId to set
     */
    public void setItemId(int itemId)
    {
        this.itemId = itemId;
    }

    /**
     * @return the userId
     */
    public int getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    /**
     * @return the userId
     */
    public int getItemTypeId()
    {
        return itemType;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setItemTypeId(int itemType)
    {
        this.itemType = itemType;
    }
    
    
    /**
     * @return the templateId
     */
    public int getTemplateId()
    {
        return templateId;
    }

    /**
     * @param templateId
     *            the templateId to set
     */
    public void setTemplateId(int templateId)
    {
        this.templateId = templateId;
    }

    /**
     * @return the count
     */
    public int getCount()
    {
        return count;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(int count)
    {
        this.count = count;
    }

    /**
     * @return the isExist
     */
    public boolean isExist()
    {
        return isExist;
    }

    /**
     * @param isExist
     *            the isExist to set
     */
    public void setExist(boolean isExist)
    {
        this.isExist = isExist;
    }

    /**
     * @return the isUsed
     */
    public boolean isUsed()
    {
        return isUsed;
    }

    /**
     * @param isUsed
     *            the isUsed to set
     */
    public void setUsed(boolean isUsed)
    {
        this.isUsed = isUsed;
    }

	public Timestamp getLastValidityTime() {
		return lastValidityTime;
	}

	public void setLastValidityTime(Timestamp lastValidityTime) {
		this.lastValidityTime = lastValidityTime;
	}

}
