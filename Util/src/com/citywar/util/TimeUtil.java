package com.citywar.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil
{
    public static final Date NOW = getSysteCurTime();

    /**
     * 获取系统距1970年1月1日总毫秒
     * 
     * @return
     */
    public static long getSysCurTimeMillis()
    {
        return getCalendar().getTimeInMillis();
    }

    /**
     * 获取系统距1970年1月1日总秒
     * 
     * @return
     */
    public static long getSysCurSeconds()
    {
        return getCalendar().getTimeInMillis() / 1000;
    }

    /**
     * 获取系统当前时间
     * 
     * @return
     */
    public static Timestamp getSysteCurTime()
    {
        Timestamp ts = new Timestamp(getCalendar().getTimeInMillis());
        return ts;
    }

    /**
     * 获取指定日期距1970年1月1日总秒
     * 
     * @param date
     * @return
     */
    public static long getDateToSeconds(Date date)
    {
        return getCalendar(date).getTimeInMillis() / 1000;
    }

    public static Date getCurrentDate()
    {
    	return new Date(getSysteCurTime().getTime());  
    }
    
    /**
     * 指定的毫秒long值转成Timestamp类型
     * 
     * @param value
     * @return
     */
    @SuppressWarnings("deprecation")
	public static long getTimeSpanInDay(Timestamp timeEnd, Timestamp timeNow)
    {
    	long t1 = timeEnd.getHours()*60*60+timeEnd.getMinutes()*60 + timeEnd.getSeconds();
    	long t2 = timeNow.getHours()*60*60+timeNow.getMinutes()*60 + timeNow.getSeconds();
    	long totalSecond = t1 - t2;
        return totalSecond;
    }
    
    @SuppressWarnings("deprecation")
    public static boolean isTimeBigInDay(Timestamp ts1, Timestamp ts2)
	{
		
		boolean isBig = false;
		if(ts1.getHours() > ts2.getHours())
		{
			isBig = true;
		}
		else if(ts1.getHours() == ts2.getHours())
		{
			if(ts1.getMinutes() > ts2.getMinutes())
				isBig = true;
			else if(ts1.getMinutes() == ts2.getMinutes())
			{
				if(ts1.getSeconds() >= ts2.getSeconds())
					isBig = true;
			}
		}
		return isBig;
		
	}
    public static java.sql.Timestamp getMillisToDate(long value)
    {
        return new java.sql.Timestamp(value);
    }
    
    /**
     * 当前系统时间增加值
     * 
     * @param type
     * @param value
     * @return
     */
    public static java.util.Date addSystemCurTime(int type, int value)
    {
        Calendar cal = getCalendar();
        switch (type)
        {
            case Calendar.DATE:// 增加天数
                cal.add(Calendar.DATE, value);
                break;
            case Calendar.HOUR:// 增加小时
                cal.add(Calendar.HOUR, value);
                break;
            case Calendar.MINUTE:// 增加分钟
                cal.add(Calendar.MINUTE, value);
                break;
            case Calendar.SECOND:// 增加秒
                cal.add(Calendar.SECOND, value);
                break;
            case Calendar.MILLISECOND:// 增加毫秒
                cal.add(Calendar.MILLISECOND, value);
                break;
            default:
                System.err.println("当前类型不存在！");
                break;
        }
        return new java.util.Date(cal.getTimeInMillis());
    }

    /**
     * 特定时间增加值
     * 
     * @param type
     * @param value
     * @return
     */
    public static java.util.Date addSpecialCurTime(Date date, int type,
            int value)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (type)
        {
            case Calendar.DATE:// 增加天数
                cal.add(Calendar.DATE, value);
                break;
            case Calendar.HOUR:// 增加小时
                cal.add(Calendar.HOUR, value);
                break;
            case Calendar.MINUTE:// 增加分钟
                cal.add(Calendar.MINUTE, value);
                break;
            case Calendar.SECOND:// 增加秒
                cal.add(Calendar.SECOND, value);
                break;
            case Calendar.MILLISECOND:// 增加毫秒
                cal.add(Calendar.MILLISECOND, value);
                break;
            default:
                System.err.println("当前类型不存在！");
                break;
        }
        return new java.util.Date(cal.getTimeInMillis());
    }

    /**
     * 格式化日期
     * 
     * @param date
     * @return
     */
    public static String getDateFormat(java.util.Date date)
    {
        return getDateFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期
     * 
     * @param date
     * @param format
     * @return
     */
    public static String getDateFormat(Date date, String format)
    {
        SimpleDateFormat dateFm = new SimpleDateFormat(format);
        return dateFm.format(date);
    }

    /**
     * 获取默认日期2000-01-01
     * 
     * @return 返回默认起始时间
     */
    public static java.sql.Timestamp getDefaultDate()
    {
        java.util.Date defaultDate = null;
        try
        {
            defaultDate = (java.util.Date) new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss").parseObject("2000-01-01 00:00:00");

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return new java.sql.Timestamp(defaultDate.getTime());
    }

    /**
     * 获取指定日期格式
     * 
     * @return 返回日期格式变量
     */
    public static java.sql.Timestamp getDefaultDate(String time)
    {
        java.util.Date tempDate = null;
        try
        {
            tempDate = (java.util.Date) new SimpleDateFormat("yyyy-MM-dd").parseObject(time);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return new java.sql.Timestamp(tempDate.getTime());
    }

    /**
     * 返回日期类型的字符串
     * 
     * @return
     */
    public static String getSimpleDate()
    {
        return getSimpleDate(new Date());
    }

    public static String getSimpleDate(Date date)
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 获取默认目上限日期2999-01-01
     * 
     * @return 返回默认上限时间
     */
    public static java.sql.Timestamp getDefaultMaxDate()
    {
        java.util.Date defaultDate = null;
        try
        {
            defaultDate = (java.util.Date) new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss").parseObject("2999-01-01 00:00:00");
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return new java.sql.Timestamp(defaultDate.getTime());
    }

    /**
     * 比较日期是否同一天
     * 
     * @param date
     * @return
     */
    public static boolean dateCompare(Date date)
    {
        java.util.Calendar now = getCalendar();
        java.util.Calendar other = getCalendar(date);
        return dateCompare(now, other) == 0 ? true : false;
    }

    /**
     * 返回两个日期相差天数
     * 
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public static int dateCompare(java.util.Calendar startDate,
            java.util.Calendar endDate)
    {
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        endDate.set(Calendar.HOUR_OF_DAY, 0);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);

        int day = (int) ((endDate.getTimeInMillis() - startDate.getTimeInMillis()) / 1000 / 3600 / 24);
        return day;
    }
    
    
    
    
    /**
     * 返回两个日期相差天数
     * 
     * @param startDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public static int dateCompareDay(java.util.Calendar startDate,
            java.util.Calendar endDate)
    {
    	
    	int day = 100;
    	if(startDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR))
    	{
    		int day1 =  startDate.get(Calendar.DAY_OF_YEAR);
        	int day2 =  endDate.get(Calendar.DAY_OF_YEAR);
        	
        	day = day2 - day1;
    	}
    	else
    	{
    		if(startDate.get(Calendar.YEAR) + 1 == endDate.get(Calendar.YEAR))
    		{
    			int month1 =  startDate.get(Calendar.MONTH);
            	int month2 =  endDate.get(Calendar.MONTH);
            	
    			int day1 =  startDate.get(Calendar.DAY_OF_YEAR);
            	int day2 =  endDate.get(Calendar.DAY_OF_YEAR);
            	
            	if(month1 == 12 && month2 == 1 && day1 == 31 && day2 == 1)
            		day = 1;
    		}
    	}

        return day;
    }
    
    
    

    public static int calcDistanceMillis(Date startTime, Date endTime)
    {
        long startSecond = getDateToSeconds(startTime);
        long endSecond = getDateToSeconds(endTime);
        return (int) (endSecond - startSecond) * 1000;
    }

    /**
     * 间隔时间以小时为单位
     * 
     * @param startDate
     * @param interval
     * @return
     */
    @SuppressWarnings("static-access")
    public static boolean isInterval(Date startDate, int interval)
    {
        // 开始日期
        java.util.Calendar startCalendar = java.util.Calendar.getInstance();
        startCalendar.setTime(startDate);
        // 结束日期
        java.util.Calendar endCalendar = getCalendar();
        if (dateCompare(startCalendar, endCalendar) == 0)
        {
            if (endCalendar.get(endCalendar.HOUR_OF_DAY) / interval == startCalendar.get(startCalendar.HOUR_OF_DAY)
                    / interval)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回两个日期相隔的（小时，分钟，秒）
     * 
     * @param startTime
     * @param endTime
     * @param type
     *            类型：hour、day、min、sec
     * @return
     */
    public static int timeSpan(Timestamp startTime, Timestamp endTime,
            String type)
    {

        long span = endTime.getTime() - startTime.getTime();

        if (type.equalsIgnoreCase("day"))
            return (int) (span / (24 * 60 * 60 * 1000));
        else if (type.equalsIgnoreCase("hour"))
            return (int) (span / (60 * 60 * 1000));
        else if (type.equalsIgnoreCase("min"))
            return (int) (span / (60 * 1000));
        else if (type.equalsIgnoreCase("sec"))
            return (int) (span / 1000);
        else
            return (int) span;
    }

    /**
     * 返回两个日期相隔的（小时，分钟，秒）
     * 
     * @param startTime
     * @param endTime
     * @param type
     *            [day,hour,min,sec]
     * @return
     */
    public static int timeSpan(Date startTime, Date endTime, String type)
    {

        long span = endTime.getTime() - startTime.getTime();

        if (type.equalsIgnoreCase("day"))
            return (int) (span / (24 * 60 * 60 * 1000));
        else if (type.equalsIgnoreCase("hour"))
            return (int) (span / (60 * 60 * 1000));
        else if (type.equalsIgnoreCase("min"))
            return (int) (span / (60 * 1000));
        else if (type.equalsIgnoreCase("sec"))
            return (int) (span / 1000);
        else
            return (int) span;
    }

    public static int timeToFrame(int secondTime)
    {
        return (secondTime * 25) / 1000;
    }

    public static String getSignStr()
    {
        String[] strs = new String[] { "a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9" };

        ThreadSafeRandom random = new ThreadSafeRandom();
        StringBuilder signStr = new StringBuilder("");
        for (int i = 0; i < 6; i++)
        {
            int j = random.next(strs.length);
            signStr.append(strs[j]);

        }
        return signStr.toString();
    }

    /**
     * 获取系统时间
     * 
     * @return
     */
    public static java.util.Calendar getCalendar()
    {
        java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
        nowCalendar.setTime(new java.util.Date());
        return nowCalendar;
    }

    /**
     * 获取系统时间
     * 
     * @return
     */
    public static Calendar getCalendar(Date date)
    {
        java.util.Calendar nowCalendar = java.util.Calendar.getInstance();
        nowCalendar.setTime(date);
        return nowCalendar;
    }

    /**
     * 比较两个时间（不包括日期）的相隔
     * 
     * @param startTime
     * @param endTime
     * @param type
     *            (hour,min,sec)
     * @return
     */
    public static int compareTimeSpan(Date startTime, Date endTime, String type)
    {

        Calendar start = new GregorianCalendar();
        start.setTime(startTime);

        Calendar end = new GregorianCalendar();
        end.setTime(endTime);

        if (type.equalsIgnoreCase("hour"))
            return end.get(Calendar.HOUR_OF_DAY)
                    - start.get(Calendar.HOUR_OF_DAY);

        if (type.equalsIgnoreCase("min"))
        {
            return (end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY))
                    * 60
                    + (end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE));
        }

        if (type.equalsIgnoreCase("sec"))
        {
            return (end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY))
                    * 60
                    * 60
                    + (end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE))
                    * 60
                    + (end.get(Calendar.SECOND) - start.get(Calendar.SECOND));
        }

        return (end.get(Calendar.HOUR_OF_DAY) - start.get(Calendar.HOUR_OF_DAY))
                * 60
                * 60
                * 1000
                + (end.get(Calendar.MINUTE) - start.get(Calendar.MINUTE))
                * 60
                * 1000
                + (end.get(Calendar.SECOND) - start.get(Calendar.SECOND))
                * 1000;
    }

    /**
     * 获取一周的第几天
     */
    public static int getDayOfWeek()
    {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 提供给 flash xml 的日期格式
     * 
     * @param date
     * @return
     */
    public static String getFlashDate(Date date)
    {
        return getDateFormat(date, "yyyy-MM-dd") + "T"
                + getDateFormat(date, "HH:mm:ss");
    }

    /**
     * 用于保存玩家图片的格式 userId_yyyyMMddHHmmss
     * 
     * @param date
     * @return
     */
    public static String getImageName()
    {
        return getDateFormat(getSysteCurTime(), "yyyyMMddHHmmss");
    }

}
