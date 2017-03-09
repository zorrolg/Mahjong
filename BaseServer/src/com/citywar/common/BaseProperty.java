/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.common;

/**
 * @author : Cookie
 * @date : 2011-5-9
 * @version
 * 
 */
public class BaseProperty
{

    private int attack;
    private int defence;
    private int agility;
    private int lucky;
    private int guard;
    private int damage;

    public void reset()
    {
        attack = 0;
        defence = 0;
        agility = 0;
        lucky = 0;
        guard = 0;
        setDamage(0);
    }

    /**
     * @return the attack
     */
    public int getAttack()
    {
        return attack;
    }

    /**
     * @param attack
     *            the attack to set
     */
    public void setAttack(int attack)
    {
        this.attack = attack;
    }

    /**
     * @return the defence
     */
    public int getDefence()
    {
        return defence;
    }

    /**
     * @param defence
     *            the defence to set
     */
    public void setDefence(int defence)
    {
        this.defence = defence;
    }

    /**
     * @return the agility
     */
    public int getAgility()
    {
        return agility;
    }

    /**
     * @param agility
     *            the agility to set
     */
    public void setAgility(int agility)
    {
        this.agility = agility;
    }

    /**
     * @return the lucky
     */
    public int getLucky()
    {
        return lucky;
    }

    /**
     * @param lucky
     *            the lucky to set
     */
    public void setLucky(int lucky)
    {
        this.lucky = lucky;
    }

    /**
     * @return the guard
     */
    public int getGuard()
    {
        return guard;
    }

    /**
     * @param guard
     *            the guard to set
     */
    public void setGuard(int guard)
    {
        this.guard = guard;
    }

    /**
     * @param damage
     *            the damage to set
     */
    public void setDamage(int damage)
    {
        this.damage = damage;
    }

    /**
     * @return the damage
     */
    public int getDamage()
    {
        return damage;
    }
}
