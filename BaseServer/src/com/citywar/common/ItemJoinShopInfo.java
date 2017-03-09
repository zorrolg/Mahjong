/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.common;

/**
 * @author : Cookie
 * @date : 2011-6-29
 * @version
 * 
 */
public class ItemJoinShopInfo
{
    public int templateID;
    public int data;
    public int moneys;
    public int gold;
    public int giftToken;
    public int offer;
    public String otherPay;
    public int medal;

    public ItemJoinShopInfo(int templateID, int data, int moneys, int gold,
            int giftToken, int offer, int medal, String otherPay)
    {
        this.templateID = templateID;
        this.data = data;
        this.moneys = moneys;
        this.gold = gold;
        this.giftToken = giftToken;
        this.offer = offer;
        this.otherPay = otherPay;
        this.medal = medal;
    }

    /**
     * @return the templateID
     */
    public int getTemplateID()
    {
        return templateID;
    }

    /**
     * @param templateID
     *            the templateID to set
     */
    public void setTemplateID(int templateID)
    {
        this.templateID = templateID;
    }

    /**
     * @return the data
     */
    public int getData()
    {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(int data)
    {
        this.data = data;
    }

    /**
     * @return the moneys
     */
    public int getMoneys()
    {
        return moneys;
    }

    /**
     * @param moneys
     *            the moneys to set
     */
    public void setMoneys(int moneys)
    {
        this.moneys = moneys;
    }

    /**
     * @return the gold
     */
    public int getGold()
    {
        return gold;
    }

    /**
     * @param gold
     *            the gold to set
     */
    public void setGold(int gold)
    {
        this.gold = gold;
    }

    /**
     * @return the giftToken
     */
    public int getGiftToken()
    {
        return giftToken;
    }

    /**
     * @param giftToken
     *            the giftToken to set
     */
    public void setGiftToken(int giftToken)
    {
        this.giftToken = giftToken;
    }

    /**
     * @return the offer
     */
    public int getOffer()
    {
        return offer;
    }

    /**
     * @param offer
     *            the offer to set
     */
    public void setOffer(int offer)
    {
        this.offer = offer;
    }

    /**
     * @return the otherPay
     */
    public String getOtherPay()
    {
        return otherPay;
    }

    /**
     * @param otherPay
     *            the otherPay to set
     */
    public void setOtherPay(String otherPay)
    {
        this.otherPay = otherPay;
    }

    /**
     * @return the medal
     */
    public int getMedal()
    {
        return medal;
    }

    /**
     * @param medal
     *            the medal to set
     */
    public void setMedal(int medal)
    {
        this.medal = medal;
    }
}
