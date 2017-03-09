/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.citywar.dice.dao.StagePrizeDao;
import com.citywar.dice.entity.StagePrizeInfo;

/**
 * 统计DAO -- 实现
 * 
 * @author shanfeng.cao
 * @date 2012-08-14
 * @version
 * 
 */
public class StagePrizeDaoImpl extends BaseDaoImpl implements StagePrizeDao
{
    
	
	private final static String TABLE_Name = "t_s_stage_prize";//静态的任务数据表
	

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		StagePrizeInfo info = new StagePrizeInfo(rs);
		return info;
	}
	
	@Override
	public List<StagePrizeInfo> getStagePrizeList()
	{
		String sql = "select * from " + TABLE_Name;
		
		List<StagePrizeInfo> result = null;

		
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try
        {
//        	DBParamWrapper params = new DBParamWrapper();
    		
            pstmt = getDbManager().executeQuery(sql , null);
            if (null != pstmt)
            {
            	result = new ArrayList<StagePrizeInfo>();
            	
            	rs = pstmt.executeQuery();
            	
            	while (rs.next()) 
            	{
            		StagePrizeInfo info = new StagePrizeInfo(rs);
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
