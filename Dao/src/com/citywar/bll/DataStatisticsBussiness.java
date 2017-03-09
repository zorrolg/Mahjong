/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll;

import com.citywar.dice.dao.manager.DaoManager;

/**
 * @author shenfeng.cao
 * @date 2012-08-14
 * @version
 * 
 */
public class DataStatisticsBussiness
{
    /**
     * 获取五分钟内的注册量
     * @return
     */
    public static int getFiveMinRegisterCount()
    {
    	return DaoManager.getDataStatisticsDao().getFiveMinSummaryRegisterCount();
    }
    
    /**
     * 获得当天注册玩家数（包括游客注册或者注册会员）
     * @return
     */
	public static int getDayRegUserCount() {
		return DaoManager.getDataStatisticsDao().getDayRegUserCount();
	}
	
	/**
	 * 日登录用户数。包括新用户，老用户都算
	 * @return
	 */
	public static int getDayLoginUserCount() {
		return DaoManager.getDataStatisticsDao().getDayLoginUserCount();
	}

	/**
	 * 日在线峰值。日在线的最高记录
	 * @return
	 */
	public static int getDayMaxOnlineCount() {
		return DaoManager.getDataStatisticsDao().getDayMaxOnlineCount();
	}
	
	/**
	 * 日注册会员数。新用户下载后，注册为会员的人数
	 * @return
	 */
	public static int getRegMemberCount() {
		return DaoManager.getDataStatisticsDao().getRegMemberCount();
	}
	
	/**
	 * 新用户7天内每天留存率。当天注册的用户，在第二天，第三天，第四天，直到第7天各天留存监控
	 * @param countDay
	 * @return
	 */
	public static int getRetainedRateDayLoginCount(int countDay) {
		return DaoManager.getDataStatisticsDao().getRetainedRateDayLoginCount(countDay);
	}

	public static long getPerCapitaTime() {
		return DaoManager.getDataStatisticsDao().getPerCapitaTime();
	}

	public static int getUsageCounterTeaCount() {
		return DaoManager.getDataStatisticsDao().getUsageCounterTeaCount();
	}

	public static int getUsageCounterHornCount() {
		return DaoManager.getDataStatisticsDao().getUsageCounterHornCount();
	}
	
	/**
	 * 把用户每天活动表里面的信息统计到ETL需要保存的里面去
	 * @return
	 */
	public static int copyInsertETL() {
		return DaoManager.getDataStatisticsDao().copyInsertETL();
	}
}
