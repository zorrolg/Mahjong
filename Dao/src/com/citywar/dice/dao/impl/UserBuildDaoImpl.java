package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserBuildDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserBuildInfo;

public class UserBuildDaoImpl extends BaseDaoImpl implements UserBuildDao {
	
	private final static String BUILD_TABLE = "t_u_build";//静态的任务数据表
	
	private static final Logger logger = Logger.getLogger(AroundDaoImpl.class.getName());
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		 
		UserBuildInfo info = new UserBuildInfo(rs);
		return info;
	}


	
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserBuildInfo userBuild = (UserBuildInfo) obj;
        String sqlText = " INSERT INTO " + BUILD_TABLE + "(`UserId`,`build_type_id`,`build_level`,`build_isupgrade`,`build_upgrade_time`) values (?,?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userBuild.getPlayerId());
        params.put(Types.INTEGER, userBuild.getBuildTypeId());
        
        params.put(Types.INTEGER, userBuild.getBuildLevel());
        params.put(Types.INTEGER, userBuild.getBuildIsUpgrade());
        params.put(Types.TIMESTAMP, userBuild.getBuildUpgradeTime());
        
        
        
        
        try
        {
            //intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
            intResult = getDbManager().executeLastId(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ PlayerBuildDaoImpl : insert ]", e);
        }
        return intResult;
	}
	
	
	
	@Override
    public int update(Object obj)
    {
        int intResult = -1;
        UserBuildInfo userBuild = (UserBuildInfo) obj; 
        String sqlText = " UPDATE `" + BUILD_TABLE + "` SET `build_level` = ?,`build_isupgrade` = ?,`build_upgrade_time` = ? where `build_type_id` = ? and UserId = ?; ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userBuild.getBuildLevel());
        params.put(Types.INTEGER, userBuild.getBuildIsUpgrade());
        params.put(Types.TIMESTAMP, userBuild.getBuildUpgradeTime());

        params.put(Types.INTEGER, userBuild.getBuildTypeId());
        params.put(Types.INTEGER, userBuild.getPlayerId());

        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ UserCardDaoImpl : insert ]", e);
        }
        return intResult;
    }
	
	
	
}
