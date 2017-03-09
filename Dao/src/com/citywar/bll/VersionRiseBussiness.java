package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.VersionRiseDao;
import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.VersionRise;

public class VersionRiseBussiness {
	
	public static List<VersionRise> doSearchByVersionCode(String versionCode)
	{
		return DaoManager.getVersionRiseDao().doSearchByVerionCode(versionCode);
	}
	
	public static VersionRise selectByVersionCode(String lowCode,String highCode)
	{
		DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, lowCode);
        params.put(Types.VARCHAR, highCode);
        
        return DaoManager.getVersionRiseDao().query("t_s_version_rise", "low_version_code = ? and high_version_code = ?", params);
        
	}
	

	public static void reset(List<VersionRise> data){
		
		if(data == null  || data.size() ==0){
			return;
		}
		deleteByVersionCode(data.get(0).getHighVersionCode());
		insert(data);
	}
	
	private static boolean insert(List<VersionRise> insertData) {
		boolean result = true;
		VersionRiseDao dao = DaoManager.getVersionRiseDao();
		for (VersionRise info : insertData) {
			dao.insert(info);
		}
		return result;
	}
	
	private static void deleteByVersionCode(String versionCode){
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.VARCHAR, versionCode);
		DaoManager.getVersionRiseDao().delete("t_s_version_rise", "high_version_code = ?", params);
	}
	
}
