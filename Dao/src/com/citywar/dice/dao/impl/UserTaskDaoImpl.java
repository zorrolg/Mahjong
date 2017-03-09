package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserTaskDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserTaskInfo;

public class UserTaskDaoImpl extends BaseDaoImpl implements UserTaskDao {
	
    private static final Logger logger = Logger.getLogger(UserTaskDaoImpl.class.getName());


	private final static String USER_TASK_TABLE = "t_u_task";//随时更新的用户已经完成任务表
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserTaskInfo userTaskInfo = new UserTaskInfo(rs);
		return userTaskInfo;
	}

	/**
	 * 查询用户已经完成的任务id
	 */
	@Override
	public List<Integer> getUserAlreadyCompletedTaskIds(int userId) {
		String sql = "select t.task_id from " + USER_TASK_TABLE + " t where user_id = ? and task_status = 1";
		List<Integer> result = null;
		PreparedStatement pstmt = null;
		
        ResultSet rs = null;
        try
        {
        	DBParamWrapper params = new DBParamWrapper();
    		params.put(Types.INTEGER, userId);
            pstmt = getDbManager().executeQuery(sql , params.getParams());
            
            if (null != pstmt)
            {
            	pstmt.setInt(1, userId);
            	
            	result = new ArrayList<Integer>();
            	
            	rs = pstmt.executeQuery();
            	
            	while (rs.next()) 
            	{
            		result.add(rs.getInt("task_id"));
            	}
            }
        } catch (Exception e) {
        	e.printStackTrace();
		} finally {
			getDbManager().closeConnection(pstmt, rs);
		}
        return result;
	}
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
        UserTaskInfo userTaskInfo = (UserTaskInfo) obj;
        String sqlText = " INSERT INTO " + USER_TASK_TABLE + "(`user_id`,`task_id`,`task_status`,`finish_time`, `task_finish_num`, `get_time`) values (?,?,?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userTaskInfo.getUserId());
        params.put(Types.INTEGER, userTaskInfo.getTaskInfo().getTaskId());
        params.put(Types.INTEGER, userTaskInfo.getTaskStatus());
        params.put(Types.TIMESTAMP, userTaskInfo.getFinishTime());
        params.put(Types.INTEGER, userTaskInfo.getTaskFinishNum());
        params.put(Types.TIMESTAMP, userTaskInfo.getGetTime());
        
        try
        {
            //intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
            intResult = getDbManager().executeLastId(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ UserTaskDaoImpl : insert ]", e);
        }
        return intResult;
	}
}
