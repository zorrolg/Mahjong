package com.citywar.bll;

import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.PengConfigInfo;

public class PengConfigBussiness {
	
	private final static String BUILD_TABLE = "t_s_peng_config";//静态的任务数据表
	
//	private final static String BUILD_TYPE_TABLE = "t_c_build_type";//随时更新的用户已经完成任务表
	
	/**
	 * 获取到所有数据库里存在的任务
	 * @return
	 */
	public static List<PengConfigInfo> getAllConfigsInfo()
	{
		return DaoManager.getPengConfigDao().queryList(BUILD_TABLE, null, null);
	}
		
}
