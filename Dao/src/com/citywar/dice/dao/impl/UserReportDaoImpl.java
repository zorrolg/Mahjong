package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserReportDao;
import com.citywar.dice.db.DBParamWrapper;

public class UserReportDaoImpl extends BaseDaoImpl implements UserReportDao {
	
	private static final Logger logger = Logger.getLogger(UserReportDaoImpl.class.getName());
	
	private final static String USER_REPORT_TABLE = "t_s_report";

	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		return null;
	}
	

	
	@Override
	public int addUserReport(int ReportUserId, int UserId, String PicName) {
		
		
		int intResult = -1;
        String sqlText = " INSERT INTO " + USER_REPORT_TABLE + "(`ReportUserId`,`UserId`,`Pic_Name`)" 
        + "values (?,?,?);";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, ReportUserId);
        params.put(Types.INTEGER, UserId);
        params.put(Types.VARCHAR, PicName);

        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ UserReportDaoImpl : insert ]", e);
        }
        return intResult;		
	}
}
