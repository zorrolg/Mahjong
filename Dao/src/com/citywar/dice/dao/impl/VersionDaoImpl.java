package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.VersionDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.Version;

public class VersionDaoImpl extends BaseDaoImpl implements VersionDao {
	
    private static final Logger logger = Logger.getLogger(VersionDaoImpl.class.getName());

	private final static String TABLE_NAME = "t_s_version2";
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		Version data = new Version();
		data.setVersionId(rs.getInt("versionId"));
		data.setVersionCode(rs.getString("versionCode"));
		data.setVersionType(rs.getInt("versionType"));
		data.setVersionDesc(rs.getString("versionDesc"));
		data.setRiseState(rs.getInt("riseState"));
		data.setServerIp(rs.getString("serverIp"));
		data.setServerPort(rs.getInt("serverPort"));
		data.setServerState(rs.getInt("serverState"));
		data.setServerDesc(rs.getString("serverDesc"));
		data.setUpdateTime(rs.getString("updateTime"));
		return data;
	}
	
	

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		Version entity = (Version) obj;
        String sqlText = " INSERT INTO " + TABLE_NAME + "(`versionCode`,`versionType`,`versionDesc`,  `riseState`,`serverIp`,`serverPort`,`serverState`,`serverDesc`,`updateTime`) values (?,?,?,?,?,?,?,?,?); ";
        
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, entity.getVersionCode());
        params.put(Types.INTEGER, entity.getVersionType());
        params.put(Types.VARCHAR, entity.getVersionDesc());
        params.put(Types.INTEGER, entity.getRiseState());
        params.put(Types.VARCHAR, entity.getServerIp() );
        
        params.put(Types.INTEGER, entity.getServerPort());
        params.put(Types.INTEGER, entity.getServerState());
        params.put(Types.VARCHAR, entity.getServerDesc());
        params.put(Types.VARCHAR, entity.getUpdateTime());
        
        try
        {
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (SQLException e)
        {
            logger.error("[ VersionDaoImpl : insert ]", e);
        }
        return intResult;
	}
	
	  @Override
	    public int update(Object obj)
	    { 
			int intResult = -1;
			Version entity = (Version) obj;
	        String sqlText = " UPDATE " + TABLE_NAME + " SET `versionCode` = ?,`versionType` = ?,`versionDesc` = ?,`riseState` = ?,`serverIp` = ?,`serverPort` = ?,`serverState` = ?,`serverDesc` = ?,`updateTime` = ? where versionId = ?; ";

	        DBParamWrapper params = new DBParamWrapper();
	        params.put(Types.VARCHAR, entity.getVersionCode());
	        params.put(Types.INTEGER, entity.getVersionType());
	        params.put(Types.VARCHAR, entity.getVersionDesc());
	        params.put(Types.INTEGER, entity.getRiseState());
	        params.put(Types.VARCHAR, entity.getServerIp() );
	        
	        params.put(Types.INTEGER, entity.getServerPort());
	        params.put(Types.INTEGER, entity.getServerState());
	        params.put(Types.VARCHAR, entity.getServerDesc());
	        params.put(Types.VARCHAR, entity.getUpdateTime());
	        params.put(Types.INTEGER, entity.getVersionId());
	        
	        try
	        {
	            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
	        }
	        catch (SQLException e)
	        {
	            logger.error("[ VersionDaoImpl : update ]", e);
	        }
	        return intResult;
	    }
	  
	  
	  public Version getMaxVersionByType(int type)
	  {
		  String sqlText = "select * from " +TABLE_NAME+" where  versionType = ? ORDER BY versionCode  desc limit 1";
		  DBParamWrapper params = new DBParamWrapper();
	      params.put(Types.INTEGER, type);
		  return  getOneVersionBySql(sqlText, params);
	  }
	  
	    
		public Version getOneVersionBySql(String sql,DBParamWrapper params){

	        String sqlText = sql;

	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try
	        {
	            pstmt = getDbManager().executeQuery(sqlText, params != null?params.getParams():null);

	            if (null != pstmt)
	            {
	                rs = pstmt.executeQuery();
	                while (rs.next())
	                {
	                	return (Version)getTemplate(rs);
	                }
	            }
	        }
	        catch (SQLException e)
	        {
	            logger.error("[ VersionDaoImpl : getFriend ]", e);
	        }
	        finally
	        {
	            getDbManager().closeConnection(pstmt, rs);
	        }
	        return null;
		}

}
