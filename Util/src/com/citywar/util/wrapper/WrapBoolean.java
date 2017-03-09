/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.util.wrapper;

import java.io.Serializable;

/**
 * @author Dream
 * @date 2011-5-12
 * @version
 * 
 */
public class WrapBoolean implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -2175612070245972483L;
    private boolean param;

    public WrapBoolean()
    {
    }

    public WrapBoolean(boolean parm)
    {
        this.param = parm;
    }

    public boolean isParam()
    {
        return param;
    }

    public void setParam(boolean param)
    {
        this.param = param;
    }
}
