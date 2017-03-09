/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

import com.citywar.util.wrapper.WrapBoolean;
import com.citywar.util.wrapper.WrapDate;
import com.citywar.util.wrapper.WrapDouble;
import com.citywar.util.wrapper.WrapInteger;

/**
 * @author dansen
 * @date 2011-7-1
 * @version
 * 
 */
public class CommonUtil
{
    private static ThreadSafeRandom random = new ThreadSafeRandom();

    private static final Logger log = Logger.getLogger(CommonUtil.class.getName());

    /**
     * 获取年月日等
     */
    public static int getTimeField(Date date, int calendarField)
    {
        return TimeUtil.getCalendar(date).get(calendarField);
    }

    /**
     * 四舍五入
     */
    public static int round(float value)
    {
        return (int) (value + 0.5f);
    }

    /**
     * 采用Math.random函数（线程安全）产生一个随机数 注意：包含下界，不包含上界
     */
    public static int random(int begin, int end)
    {
        int interval = end - begin;
        float value = (float) (Math.random() * interval + begin);
        return (int) value;
    }

    public static boolean isInTheSameWeek(Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        // subYear==0,说明是同一年
        if (subYear == 0)
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        else if (subYear == 1 && cal2.get(Calendar.MONTH) + 1 == 12)
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        else if (subYear == -1 && cal1.get(Calendar.MONTH) + 1 == 12)
        {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(String s)
    {
        if (s == null)
        {
            return true;
        }
        if (s.isEmpty())
        {
            return true;
        }
        return false;
    }

    public static String replaceNull(String s)
    {
        return s == null ? "" : s;
    }

    public static Object firstOrDefault(List<?> list)
    {
        if (list.size() > 0)
        {
            return list.get(0);
        }

        return null;
    }

    public static String join(String sep, List<String> strList)
    {
        StringBuilder ret = new StringBuilder("");
        for (String s : strList)
        {
            ret.append(s);
            ret.append(sep);
        }
        ret.delete(ret.length() - sep.length(), ret.length());
        return ret.toString();
    }

    public static void main(String[] args)
    {
        ByteBuffer buffer = ByteBuffer.allocate(100);

        byte[] result = compress(buffer, 0, 100);

        for (byte data : result)
        {
            System.out.println(data);
        }
    }

    /**
     * @param
     * @return
     * @exception
     * @param buffer
     * @param hdrSize
     * @param i
     * @return
     */
    public static byte[] compress(ByteBuffer buffer, int start, int len)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(out);
        ZipEntry entry = new ZipEntry("");

        try
        {
            zos.putNextEntry(entry);
            zos.setLevel(9);
            zos.write(buffer.array(), start, len);
            zos.closeEntry();
            zos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    /**
     * 生成一个位于指定区间，指定数量的不重复的随机数序列
     * 
     * @param minValue
     *            区间下限
     * @param maxValue
     *            区间上限
     * @param count
     *            随机数数量
     * @return 随机数序列，无法生成时返回null
     */
    public static int[] getRandomUnrepeatArray(int minValue, int maxValue,
            int count)
    {
        if (maxValue - minValue + 1 < count)
            // 区间本身数量不足, 无法生成指定的序列
            return null;

        int j;
        int[] resultRound = new int[count];
        for (j = 0; j < count;)
        {
            int i = random.next(minValue, maxValue + 1);
            boolean exist = false;

            for (int k = 0; k < j; k++)
            {
                if (resultRound[k] == i)
                {
                    // 存在重复值，终止
                    exist = true;
                    break;
                }
            }

            if (!exist)
                resultRound[j++] = i;
        }

        return resultRound;
    }

    /**
     * 
     * @param obj
     * @param clzType
     * @param source
     * @return
     */
    public static boolean objectTryParse(Object obj, Class<?> clzType,
            String source)
    {
        if (isNullOrEmpty(source))
            return false;
        try
        {
            if (clzType == Integer.class)
            {
                ((WrapInteger) obj).setParam(Integer.parseInt(source));
            }
            else if (clzType == Double.class)
            {
                ((WrapDouble) obj).setValue(Double.parseDouble(source));
            }
            else if (clzType == Boolean.class)
            {
                ((WrapBoolean) obj).setParam(Boolean.parseBoolean(source));
            }
            else if (clzType == Date.class)
            {
                ((WrapDate) obj).setParam(new SimpleDateFormat("yyyy-MM-dd").parse(source));
            }
            else if (clzType == String.class)
            {

            }
            else
            {

            }
            return true;
        }
        catch (Exception e)
        {
            // TODO: handle exception
            log.warn(StackMessagePrint.printErrorTrace(e));
            // e.printStackTrace();
            return false;
        }
    }
}
