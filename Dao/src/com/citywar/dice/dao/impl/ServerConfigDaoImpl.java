package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.ServerConfigDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.ServerConfig;

public class ServerConfigDaoImpl extends BaseDaoImpl implements ServerConfigDao {
	
    private static final Logger logger = Logger.getLogger(ServerConfigDaoImpl.class.getName());

	private final static String TABLE_NAME = "t_s_server";
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		ServerConfig data = new ServerConfig();
		data.setId(rs.getInt("server_id"));
		data.setIp(rs.getString("server_ip"));
		data.setPort(rs.getInt("server_port"));
		data.setDesc(rs.getString("server_desc"));
		data.setState(rs.getInt("server_state"));
		data.setName(rs.getString("server_name"));
		data.setVersionId(rs.getInt("version_id"));
		return data;
	}

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		ServerConfig entity = (ServerConfig) obj;
        String sqlText = " INSERT INTO " + TABLE_NAME + "(`server_ip`,`server_port`,`server_desc`,`server_state`,`server_name`,`version_id`) values (?,?,?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, entity.getIp());
        params.put(Types.INTEGER, entity.getPort());
        params.put(Types.VARCHAR, entity.getDesc());
        params.put(Types.INTEGER, entity.getState());
        params.put(Types.VARCHAR, entity.getName());
        params.put(Types.INTEGER, entity.getVersionId());
        
        try
        {
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (SQLException e)
        {
            logger.error("[ ServerConfigDaoImpl : insert ]", e);
        }
        return intResult;
	}
	
	  @Override
	    public int update(Object obj)
	    { 
			
			int intResult = -1;
			ServerConfig info = (ServerConfig) obj;
	        String sqlText = " UPDATE " + TABLE_NAME + " SET `server_ip`=?, `server_port`=?, `server_desc`=?, `server_state`=?, `server_name`=?, `version_id`=? where server_id = ?; ";

	        DBParamWrapper params = new DBParamWrapper();
	        params.put(Types.VARCHAR, info.getIp());
	        params.put(Types.INTEGER, info.getPort());
	        params.put(Types.VARCHAR, info.getDesc());
	        params.put(Types.INTEGER, info.getState());
	        params.put(Types.VARCHAR, info.getName());
	        params.put(Types.INTEGER, info.getVersionId());
	        
	        params.put(Types.INTEGER, info.getId());
	        
	        try
	        {
	            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
	        }
	        catch (SQLException e)
	        {
	            logger.error("[ ServerConfigDaoImpl : update ]", e);
	        }
	        return intResult;
	    }
}
