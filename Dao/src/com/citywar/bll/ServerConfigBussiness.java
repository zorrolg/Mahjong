package com.citywar.bll;

import java.sql.Types;
import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.ServerConfig;

public class ServerConfigBussiness {
	
	public static List<ServerConfig> doSearchByVesionId(int versionId){
		
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, versionId);
	     return DaoManager.getServerConfigDao().queryList("t_s_server",
                 "version_id = ?",
                 params,
                 null,
                 null);
	}
	
	public static boolean insertEntity(ServerConfig data){
		DaoManager.getServerConfigDao().insert(data);
		return true;
	}
	
	public static boolean updateEntity(ServerConfig data){
		DaoManager.getServerConfigDao().update(data);
		return true;
	}
	public static ServerConfig selectById(int serverId)
	{
		DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, serverId);
        return DaoManager.getServerConfigDao().query("t_s_server", "server_id=?", params);
	}

}
