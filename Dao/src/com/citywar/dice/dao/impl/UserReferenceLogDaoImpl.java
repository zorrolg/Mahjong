package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserReferenceLogDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserReferenceLogInfo;

public class UserReferenceLogDaoImpl extends BaseDaoImpl implements UserReferenceLogDao {
	
    private static final Logger logger = Logger.getLogger(UserReferenceLogDaoImpl.class.getName());


	private final static String USER_REF_LOG_TABLE = "t_u_ref_log";//
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserReferenceLogInfo userReferenceLogInfo = new UserReferenceLogInfo(rs);
		return userReferenceLogInfo;
	}

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserReferenceLogInfo userReferenceLogInfo = (UserReferenceLogInfo) obj;
        String sqlText = " INSERT INTO " + USER_REF_LOG_TABLE + "(`owner_user_id`,`passives_user_id`,`ref_id`,`log_type`, `income_exp` ,`income_coins`, `log_create_time`,`initiative_user_name` ,`passives_user_name` , `log_content`) values (?,?,?,?,?,?,?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userReferenceLogInfo.getOwnerUserId());
        params.put(Types.INTEGER, userReferenceLogInfo.getPassivesUserId());
        params.put(Types.INTEGER, userReferenceLogInfo.getRefId());
        params.put(Types.INTEGER, userReferenceLogInfo.getLogType());
        params.put(Types.INTEGER, userReferenceLogInfo.getIncomeExp());
        params.put(Types.INTEGER, userReferenceLogInfo.getIncomeCoins());
        params.put(Types.TIMESTAMP, userReferenceLogInfo.getLogCreateTime());
        params.put(Types.VARCHAR, userReferenceLogInfo.getInitiativeUserName());
        params.put(Types.VARCHAR, userReferenceLogInfo.getPassivesUserName());
        params.put(Types.VARCHAR, userReferenceLogInfo.getLogContent());
        
        try
        {
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (SQLException e)
        {
            logger.error("[ UserReferenceLogDaoImpl : insert ]", e);
        }
        return intResult;
	}
	

    @Override
    public int update(Object obj)
    { 
		int intResult = -1;
		UserReferenceLogInfo userReferenceLogInfo = (UserReferenceLogInfo) obj;
        String sqlText = " UPDATE " + USER_REF_LOG_TABLE + " SET `owner_user_id`=? ,`passives_user_id`=? ,`ref_id`=? ,`log_type`=? , `income_exp`=? ,`income_coins`=? , `log_create_time`=? ,`initiative_user_name`=? ,`passives_user_name`=? , `log_content`=?  where log_id = ?; ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userReferenceLogInfo.getOwnerUserId());
        params.put(Types.INTEGER, userReferenceLogInfo.getPassivesUserId());
        params.put(Types.INTEGER, userReferenceLogInfo.getRefId());
        params.put(Types.INTEGER, userReferenceLogInfo.getLogType());
        params.put(Types.INTEGER, userReferenceLogInfo.getIncomeExp());
        params.put(Types.INTEGER, userReferenceLogInfo.getIncomeCoins());
        params.put(Types.TIMESTAMP, userReferenceLogInfo.getLogCreateTime());
        params.put(Types.VARCHAR, userReferenceLogInfo.getInitiativeUserName());
        params.put(Types.VARCHAR, userReferenceLogInfo.getPassivesUserName());
        params.put(Types.VARCHAR, userReferenceLogInfo.getLogContent());
        params.put(Types.INTEGER, userReferenceLogInfo.getLogId());
        
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ UserReferenceLogDaoImpl : update ]", e);
        }
        return intResult;
    }
}
