package com.citywar.bll;

import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.CityInfo;

public class CityBussiness {
	
	private final static String BUILD_TABLE = "t_s_city";//静态的任务数据表
	
	
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<CityInfo> getAllCitysInfo()
	{
		return DaoManager.getCityDao().queryList(BUILD_TABLE, null, null);
	}
	
	
	
}
