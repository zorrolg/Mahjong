/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;

/**
 * 物品模板信息
 * 
 * @author tracy
 * @date 2011-12-19
 * @version
 * 
 */
public class ItemTemplateInfo
{
    /**
     * 模板 Id
     */
    private int templateId;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 属性值
     */
//    private int property;
    
    private String propertyDesc;
    /**
     * 模板类型（1：为增加金币2：为皮肤（梦幻紫）3：对话框（绯红流云）4：为醒酒茶）
     */
    private int type;
    /**
     * 所需等级
     */
    private int needLevel;
    /**
     * 所需等级
     */
    private int para1;
    /**
     * 资源路径(一般为图片)
     */
    private String resPath;
    
    /** 道具触发概率*/
    private int Probability;

    
    /**
     * 
     */
    public ItemTemplateInfo()
    {
        super();
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

    
//    /**
//     * @return the property
//     */
//    public int getProperty()
//    {
//        return property;
//    }
//
//    /**
//     * @param property
//     *            the property to set
//     */
//    public void setProperty(int property)
//    {
//        this.property = property;
//    }

    /**
     * @return the type
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * @return the needLevel
     */
    public int getNeedLevel()
    {
        return needLevel;
    }

    /**
     * @param needLevel
     *            the needLevel to set
     */
    public void setNeedLevel(int needLevel)
    {
        this.needLevel = needLevel;
    }

    
    
    
    
    /**
     * @return the needLevel
     */
    public int getPrar1()
    {
        return para1;
    }

    /**
     * @param needLevel
     *            the needLevel to set
     */
    public void setPrar1(int para1)
    {
        this.para1 = para1;
    }
    
    
    
    /**
     * @return the resPath
     */
    public String getResPath()
    {
        return resPath;
    }

    /**
     * @param resPath
     *            the resPath to set
     */
    public void setResPath(String resPath)
    {
        this.resPath = resPath;
    }

	public String getPropertyDesc() {
		return propertyDesc;
	}

	public void setPropertyDesc(String propertyDesc) {
		this.propertyDesc = propertyDesc;
	}

	public int getProbability() {
		return Probability;
	}

	public void setProbability(int probability) {
		Probability = probability;
	}

}
