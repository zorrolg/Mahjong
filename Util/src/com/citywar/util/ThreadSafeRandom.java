package com.citywar.util;

import java.util.Random;

public class ThreadSafeRandom
{

    private Random random = new Random();

    public int next()
    {
        synchronized (this)
        {
            return random.nextInt();
        }
    }

    public int next(int maxValue)
    {
        synchronized (this)
        {
            return random.nextInt(maxValue);
        }
    }

    /**
     * 随机区间值，如 min=1 maxValue=5 随机，其结果值不包括5
     * 
     * @param minValue
     *            开始值
     * @param maxValue
     *            结束值
     * @return
     */
    public int next(int minValue, int maxValue)
    {
        synchronized (this)
        {
            if (minValue < maxValue)
            {
                return random.nextInt(maxValue - minValue) + minValue;
            }
        }
        return minValue;
    }
    
    /**
     * 取得N个随机骰子的结果
     * @param count
     * @return
     */
    public byte[] getDiceNumber(int count)
    {
        if(count<=0)
            return null;
        
        byte[] temp =new byte[count];
        for(int i=0;i<count;i++)
        {
            temp[i]=(byte)next(1,7);
        }
        return temp;
    }
}
