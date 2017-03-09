/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao;

/**
 * 玩家信息 DAO -- 接口
 * 
 * @author shanfeng.cao
 * @date 2012-08-14
 * @version
 * 
 */
public interface DataStatisticsDao extends BaseDao
{
	public int getFiveMinSummaryRegisterCount();
	
	public int getRegMemberCount();

	public int getDayRegUserCount();

	public int getDayLoginUserCount();

	public int getDayMaxOnlineCount();

	public int getSQLCount(String SQLString);

	public String getRetainedRateDaySQLString(int countDay);

	public int getRetainedRateDayLoginCount(int countDay);

	public long getPerCapitaTime();

	public int getUsageCounterTeaCount();

	public int getUsageCounterHornCount();

	public int copyInsertETL();
}
