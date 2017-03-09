package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.FeedbackDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.FeedbackInfo;

public class FeedbackDaoImpl extends BaseDaoImpl implements FeedbackDao {
	
    private static final Logger logger = Logger.getLogger(FeedbackDaoImpl.class.getName());

	private final static String T_U_FEEDBACK_TABLE = "t_u_feedback";//
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		FeedbackInfo feedbackInfoInfo = new FeedbackInfo(rs);
		return feedbackInfoInfo;
	}

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		FeedbackInfo feedbackInfoInfo = (FeedbackInfo) obj;
        String sqlText = " INSERT INTO " + T_U_FEEDBACK_TABLE + "(`user_id`,`feedback_type`,`feedback_create_time`, `feedback_contents`) values (?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, feedbackInfoInfo.getUserId());
        params.put(Types.INTEGER, feedbackInfoInfo.getFeedbackType());
        params.put(Types.TIMESTAMP, feedbackInfoInfo.getFeedbackCreateTime());
        params.put(Types.VARCHAR, feedbackInfoInfo.getFeedbackContents());
        
        try
        {
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (SQLException e)
        {
            logger.error("[ feedbackDaoImpl : insert ]", e);
        }
        return intResult;
	}
    @Override
    public int update(Object obj)
    { 
		int intResult = -1;
		FeedbackInfo feedbackInfoInfo = (FeedbackInfo) obj;
        String sqlText = " UPDATE " + T_U_FEEDBACK_TABLE + " SET `user_id`=?,`feedback_type`=?,`feedback_create_time`=?, `feedback_contents`=?  where id = ?; ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, feedbackInfoInfo.getUserId());
        params.put(Types.INTEGER, feedbackInfoInfo.getFeedbackType());
        params.put(Types.TIMESTAMP, feedbackInfoInfo.getFeedbackCreateTime());
        params.put(Types.VARCHAR, feedbackInfoInfo.getFeedbackContents());
        params.put(Types.INTEGER, feedbackInfoInfo.getId());
        
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ feedbackDaoImpl : update ]", e);
        }
        return intResult;
    }
}
