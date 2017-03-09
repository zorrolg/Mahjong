package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.citywar.dice.dao.BuildDao;
import com.citywar.dice.entity.BuildInfo;
import com.citywar.dice.entity.BuildTypeInfo;

public class BuildDaoImpl extends BaseDaoImpl implements BuildDao {
	
//	private final static String BUILD_TABLE = "t_c_build";//静态的任务数据表
	
	private final static String BUILD_TYPE_TABLE = "t_c_build_type";//随时更新的用户已经完成任务表

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		BuildInfo info = new BuildInfo(rs);
		return info;
	}


	@Override
	public List<BuildTypeInfo> getBuildTypeList()
	{
		String sql = "select * from " + BUILD_TYPE_TABLE;
				
				List<BuildTypeInfo> result = null;

				
		        ResultSet rs = null;
		        PreparedStatement pstmt = null;
		        
		        try
		        {
//		        	DBParamWrapper params = new DBParamWrapper();
		    		
		            pstmt = getDbManager().executeQuery(sql , null);
		            if (null != pstmt)
		            {
		            	result = new ArrayList<BuildTypeInfo>();
		            	
		            	rs = pstmt.executeQuery();
		            	
		            	while (rs.next()) 
		            	{
		            		BuildTypeInfo info = new BuildTypeInfo(rs);
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
