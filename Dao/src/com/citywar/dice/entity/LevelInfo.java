/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;

/**
 * 等级信息
 * 
 * @author tracy
 * @date 2011-12-27
 * @version
 * 
 */
public class LevelInfo extends DataObject
{
    /**
     * 等级
     */
    private int level;
    /**
     * 等级对应所需的最小经验值
     */
    private int gp;
    /**
     * 等级的称号
     */
    private String title;
    /**
     * 等级对应的醉酒值
     */
    private int drunkFullLevel;
    /**
     * 等级对应的赎金
     */
    private int RansomMoney;

    /**
     * 
     */
    public LevelInfo()
    {
        super();
    }

	/**
     * @return the level
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level)
    {
        this.level = level;
    }

    /**
     * @return the gp
     */
    public int getGp()
    {
        return gp;
    }

    /**
     * @param gp
     *            the gp to set
     */
    public void setGp(int gp)
    {
        this.gp = gp;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return the drunkFullLevel
     */
    public int getDrunkFullLevel()
    {
        return drunkFullLevel;
    }

    /**
     * @param drunkFullLevel
     *            the drunkFullLevel to set
     */
    public void setDrunkFullLevel(int drunkFullLevel)
    {
        this.drunkFullLevel = drunkFullLevel;
    }

    public int getRansomMoney() {
		return RansomMoney;
	}

	public void setRansomMoney(int ransomMoney) {
		RansomMoney = ransomMoney;
	}

}
