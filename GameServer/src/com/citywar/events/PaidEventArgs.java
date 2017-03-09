/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.events;

import com.citywar.event.BaseEventArg;

/**
 * @author Dream
 * @date 2011-6-21
 * @version
 * 
 */
public class PaidEventArgs extends BaseEventArg
{
    private int money;
    private int gold;
    private int offer;
    private int gifttoken;
    private int medal;
    private String payGoods;

    public int getMoney()
    {
        return money;
    }

    public void setMoney(int money)
    {
        this.money = money;
    }

    public int getGold()
    {
        return gold;
    }

    public void setGold(int gold)
    {
        this.gold = gold;
    }

    public int getOffer()
    {
        return offer;
    }

    public void setOffer(int offer)
    {
        this.offer = offer;
    }

    public int getGifttoken()
    {
        return gifttoken;
    }

    public void setGifttoken(int gifttoken)
    {
        this.gifttoken = gifttoken;
    }

    public int getMedal()
    {
        return medal;
    }

    public void setMedal(int medal)
    {
        this.medal = medal;
    }

    public String getPayGoods()
    {
        return payGoods;
    }

    public void setPayGoods(String payGoods)
    {
        this.payGoods = payGoods;
    }

    /**
     * @param object
     * @param eventType
     */
    public PaidEventArgs(Object object, int eventType)
    {
        super(object, eventType);
        // TODO Auto-generated constructor stub
    }

    public PaidEventArgs(Object object, int eventType, int money, int gold,
            int offer, int gifttoken, int medal, String payGoods)
    {
        super(object, eventType);
        this.medal = medal;
        this.payGoods = payGoods;
        this.gifttoken = gifttoken;
        this.offer = offer;
        this.gold = gold;
        this.money = money;
    }

}
