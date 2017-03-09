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
public class DrawGoodInfo
{
    /**
     * 对应表中自增主键
     */
    private int id;
    /**
     * 商店 id, 保留字段
     */
    private int drawType;
    /**
     * 商店类型(礼品商店, 道具商店等)
     */
    private int drawPara;
    /**
     * 商品类型(醒酒类, 表情类等)
     */
    private int goodType;
    
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
     * 数量
     */
    private int count;
                
    /**
     * 商品图片Path
     */
    private String picPath;
    

    
    public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String PicPath) {
		this.picPath = PicPath;
	}


    /**
     * 
     */
    public DrawGoodInfo()
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
     * @return the shopId
     */
    public int getDrawPara()
    {
        return drawPara;
    }

    /**
     * @param shopId
     *            the shopId to set
     */
    public void setDrawPara(int drawPara)
    {
        this.drawPara = drawPara;
    }

    /**
     * @return the shopType
     */
    public int getDrawType()
    {
        return  this.drawType;
    }

    /**
     * @param shopType
     *            the shopType to set
     */
    public void setDrawType(int drawType)
    {
        this.drawType = drawType;
    }

    /**
     * @return the goodsType
     */
    public int getGoodType()
    {
        return this.goodType;
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



}
