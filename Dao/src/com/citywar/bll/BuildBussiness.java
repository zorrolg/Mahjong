package com.citywar.bll;

import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.BuildInfo;
import com.citywar.dice.entity.BuildTypeInfo;

public class BuildBussiness {
	
	private final static String BUILD_TABLE = "t_c_build";//静态的任务数据表
	
//	private final static String BUILD_TYPE_TABLE = "t_c_build_type";//随时更新的用户已经完成任务表
	
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<BuildInfo> getAllBuildsInfo()
	{
		return DaoManager.getBuildsDao().queryList(BUILD_TABLE, null, null);
	}
	
	
	public static List<BuildTypeInfo> getAllBuildsTypeInfo()
	{
		return DaoManager.getBuildsDao().getBuildTypeList();
	}
	
}
