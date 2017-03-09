package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.citywar.dice.dao.TaskDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.TaskInfo;

public class TaskDaoImpl extends BaseDaoImpl implements TaskDao {
	
	private final static String TASK_TABLE = "t_s_task";//静态的任务数据表
	
	private final static String USER_TASK_TABLE = "t_u_task";//随时更新的用户已经完成任务表

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		TaskInfo info = new TaskInfo(rs);
		return info;
	}

	/**
	 * 获取用户当前等级未完成的任务查询方法--(慎用涉及子查询)
	 */
	@Override
	public List<TaskInfo> getUserNotCompletedTasks(int userId, int userLevel) {
		
		String sql = "select * from " + TASK_TABLE + 
		" where task_limit_level <= ? " +
		"and task_id not in(select task_id from " + USER_TASK_TABLE + " where user_id = ? and task_status = 0)";
		
		List<TaskInfo> result = null;
		PreparedStatement pstmt = null;
		
        ResultSet rs = null;
        try
        {
        	DBParamWrapper params = new DBParamWrapper();
        	params.put(Types.INTEGER, userLevel);
    		params.put(Types.INTEGER, userId);
    		
            pstmt = getDbManager().executeQuery(sql , params.getParams());
            if (null != pstmt)
            {
            	result = new ArrayList<TaskInfo>();
            	
            	rs = pstmt.executeQuery();
            	
            	while (rs.next()) 
            	{
            		TaskInfo info = new TaskInfo(rs);
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
