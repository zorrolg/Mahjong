/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.db;

import java.util.HashMap;

/**
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public class DBParamWrapper
{
    private HashMap<Integer, DBParameter> params = null;
    private int p = 0;

    {
        this.params = new HashMap<Integer, DBParameter>();
    }

    public void put(int type, Object o)
    {
        params.put(++p, new DBParameter(type, o));
    }

    public HashMap<Integer, DBParameter> getParams()
    {
        return params;
    }

    public void clear()
    {
        params.clear();
        p = 0;
    }
    
}
