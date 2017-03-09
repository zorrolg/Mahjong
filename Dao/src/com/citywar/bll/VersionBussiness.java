package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.VersionDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.Version;

public class VersionBussiness {
	
	public static List<Version> doSearchAll(){
	     return DaoManager.getVersionDao().queryList("t_s_version2",
                 "",
                 null,
                 "versionCode",
                 null);
	}
	
//	public static List<Version> doSearchEqualsVesion(String code){
//		DBParamWrapper params = new DBParamWrapper();
//        params.put(Types.VARCHAR, code);
//		 return DaoManager.getVersionDao().queryList("t_s_version",
//                 "version_code <= ?",
//                 params,
//                 "version_code",
//                 null);
//	}
//	public static List<Version> doSearchLowVesion(String code){
//		List<Version> result = doSearchEqualsVesion(code);
//		if(result != null && result.size()>0){
//			result.remove(result.size()-1);
//		}
//		return result;
//	}
	
	public static boolean insertEntity(Version data){
		
		int id = DaoManager.getVersionDao().insert(data);
		data.setVersionId(id);
		return true;
	}
	
	public static boolean updateEntity(Version data){
		
		return  DaoManager.getVersionDao().update(data)>0;
	}

	public static boolean delete(int id) {
		VersionDao dao = DaoManager.getVersionDao();
		DBParamWrapper params = null;
		params = new DBParamWrapper();
		params.put(Types.INTEGER, id);
		dao.delete("t_s_version2", "versionId =?", params);
		return true;
	}
	
	public static Version selectByVersionId(int versionId)
	{
		DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, versionId);
        return DaoManager.getVersionDao().query("t_s_version2", "versionId=?", params);
	}
	public static Version selectByVersionCode(String versionCode)
	{
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR, versionCode);
		return DaoManager.getVersionDao().query("t_s_version2", "versionCode=?", params);
	}
	
	/**
	 * 根据类型查询 该类型的最高版本
	 * @param type
	 * @return
	 */
	public static Version getMaxVersionCodeByType(int type)
	{
		return DaoManager.getVersionDao().getMaxVersionByType(type);
	}
}
