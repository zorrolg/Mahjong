package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.PushInfoDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.PushInfo;
import com.citywar.dice.entity.PushLogInfo;
import com.citywar.dice.entity.PushPlayerInfo;

public class PushInfoDaoImpl extends BaseDaoImpl implements PushInfoDao {
	
	private static final Logger logger = Logger.getLogger(UserReferenceDaoImpl.class.getName());
	
	private final static String PLAYER_TABLE = "t_u_player";//静态的任务数据表
	
//	private final static String PUSH_TABLE = "t_s_push";//静态的任务数据表
	
	private final static String PUSH_LOG_TABLE = "t_s_push_log";//随时更新的用户已经完成任务表

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		PushInfo info = new PushInfo(rs);
		return info;
	}
	
	
	
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		PushLogInfo pushInfo = (PushLogInfo) obj;
        String sqlText = " INSERT INTO " + PUSH_LOG_TABLE + "(`deviceToken`,`msg`,`result`,`time`) values (?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, pushInfo.getDeviceToken());
        params.put(Types.VARCHAR, pushInfo.getMsg());
        params.put(Types.INTEGER, pushInfo.getResult());
        params.put(Types.TIMESTAMP, pushInfo.getTime());

       
        try
        {
            intResult = getDbManager().executeLastId(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ PushInfoDaoImpl : insert ]", e);
        }
        return intResult;
	}
	
	
	
	
	@Override
	public List<PushPlayerInfo> getPushPlayerList()
	{
		String sql = "select UserId,machinery_id from " + PLAYER_TABLE + " where machinery_id != '';";
		
		List<PushPlayerInfo> result = null;

		
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try
        {
//        	DBParamWrapper params = new DBParamWrapper();
    		
            pstmt = getDbManager().executeQuery(sql , null);
            if (null != pstmt)
            {
            	result = new ArrayList<PushPlayerInfo>();
            	
            	rs = pstmt.executeQuery();
            	
            	while (rs.next()) 
            	{
            		PushPlayerInfo info = new PushPlayerInfo(rs);
            		result.add(info);
            	}
            }
        } catch (Exception e) {
        	e.printStackTrace();
		} finally {
			getDbManager().closeConnection(pstmt, rs);
		}
		return result;
	}
	
	
}
