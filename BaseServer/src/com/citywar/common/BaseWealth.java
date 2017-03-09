/*
 * @author : Cookie
 * @date : 2011-04-28
 * @description : 封装�?��小类，传递参数用
 */

package com.citywar.common;

public class BaseWealth
{
    private int gold;
    private int money;
    private int giftToken;
    private int medal;
    private int gp;

    public BaseWealth(int gold, int money, int giftToken, int medal, int gp)
    {
        super();
        this.gold = gold;
        this.money = money;
        this.giftToken = giftToken;
        this.medal = medal;
        this.gp = gp;
    }

    public int getGold()
    {
        return gold;
    }

    public void setGold(int gold)
    {
        this.gold = gold;
    }

    public int getMoney()
    {
        return money;
    }

    public void setMoney(int money)
    {
        this.money = money;
    }

    public int getGiftToken()
    {
        return giftToken;
    }

    public void setGiftToken(int giftToken)
    {
        this.giftToken = giftToken;
    }

    public int getMedal()
    {
        return medal;
    }

    public void setMedal(int medal)
    {
        this.medal = medal;
    }

    public void setGp(int gp)
    {
        this.gp = gp;
    }

    public int getGp()
    {
        return gp;
    }

}
