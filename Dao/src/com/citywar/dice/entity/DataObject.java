/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;


/**
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public class DataObject
{
    private short op = 0;

    public final void setOp(short option)
    {
        if ((this.op == Option.INSERT) && (option == Option.UPDATE))
        {
            return;
        }
        this.op = option;
    }

    public final short getOp()
    {
        return this.op;
    }
}
