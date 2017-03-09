/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;


/**
 * 商店商品信息
 * 
 * @author tracy
 * @date 2011-12-20
 * @version
 * 
 */
public class ShopGoodInfo
{
    /**
     * 对应表中自增主键
     */
    private int id;
    
    private String itemCode;
    /**
     * 商店 id, 保留字段
     */
    private int shopId;
    /**
     * 商店类型(礼品商店, 道具商店等)
     */
    private byte shopType;
    /**
     * 商品类型(醒酒类, 表情类等)
     */
    private byte goodType;
    /**
     * 商品对应模板 id
     */
    private int templateId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品价格
     */
    private float price;

    /**
     * 数量
     */
    private int count;
    
    /**
     * 有效期
     */
    private int validDay;
        
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 商品图片Path
     */
    private String ShopPicPath;
    
    /**
     * 使用道具的提示语
     */
    private String usePresentation;
    
    public String getShopPicPath() {
		return ShopPicPath;
	}

	public void setShopPicPath(String shopPicPath) {
		ShopPicPath = shopPicPath;
	}

	public String getNamePicPath() {
		return NamePicPath;
	}

	public void setNamePicPath(String namePicPath) {
		NamePicPath = namePicPath;
	}

	/**
     * 商品名字图片Path
     */
    private String NamePicPath;

    /**
     * 
     */
    public ShopGoodInfo()
    {
        super();
    }

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    
    
    /**
     * @return the unit
     */
    public String getItemCode()
    {
        return itemCode;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setItemCode(String itemCode)
    {
        this.itemCode = itemCode;
    }
        
    /**
     * @return the shopId
     */
    public int getShopId()
    {
        return shopId;
    }

    /**
     * @param shopId
     *            the shopId to set
     */
    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }

    /**
     * @return the shopType
     */
    public byte getShopType()
    {
        return shopType;
    }

    /**
     * @param shopType
     *            the shopType to set
     */
    public void setShopType(byte shopType)
    {
        this.shopType = shopType;
    }

    /**
     * @return the goodsType
     */
    public byte getGoodType()
    {
        return goodType;
    }

    /**
     * @param goodsType
     *            the goodsType to set
     */
    public void setGoodType(byte goodType)
    {
        this.goodType = goodType;
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
     * @return the price
     */
    public float getPrice()
    {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(float price)
    {
        this.price = price;
    }

    
    
    
    /**
     * @return the templateId
     */
    public int getCount()
    {
        return count;
    }

    /**
     * @param templateId
     *            the templateId to set
     */
    public void setCount(int count)
    {
        this.count = count;
    }
    
    /**
     * @return the templateId
     */
    public int getValidDay()
    {
        return validDay;
    }

    /**
     * @param templateId
     *            the templateId to set
     */
    public void setValidDay(int validDay)
    {
        this.validDay = validDay;
    }
    
    
    /**
     * @return the unit
     */
    public String getUnit()
    {
        return unit;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

	public String getUsePresentation() {
		return usePresentation;
	}

	public void setUsePresentation(String usePresentation) {
		this.usePresentation = usePresentation;
	}

}
