/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.StageDataDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.StageDataInfo;

/**
 * 好友相关DAO实现
 * 
 * @author charles
 * @date 2011-12-16
 * @version
 * 
 */
public class StageDataDaoImpl extends BaseDaoImpl implements StageDataDao
{
    
	private static final Logger logger = Logger.getLogger(StageDataDaoImpl.class.getName());
	
	private final static String TABLE_Name = "t_s_stage_data";//静态的任务数据表
	

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		StageDataInfo info = new StageDataInfo(rs);
		return info;
	}
	
	@Override
	public List<StageDataInfo> getStageDataList()
	{
		String sql = "select * from " + TABLE_Name;
		
		List<StageDataInfo> result = null;

		
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try
        {
//        	DBParamWrapper params = new DBParamWrapper();
    		
            pstmt = getDbManager().executeQuery(sql , null);
            if (null != pstmt)
            {
            	result = new ArrayList<StageDataInfo>();
            	
            	rs = pstmt.executeQuery();
            	
            	while (rs.next()) 
            	{
            		StageDataInfo info = new StageDataInfo(rs);
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
	
	
	
	@Override
    public int insert(Object obj)
    {
        int intResult = -1;
        StageDataInfo stageDataInfo = (StageDataInfo) obj;
        String sqlText = " INSERT INTO `db_citywar`.`t_s_stage_data`(`stage_data_id`, `stage_id`, `stage_title`, `stage_time`)"
        		+ "VALUES(?,?,?,?);";
       
        
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, stageDataInfo.getStageDataId());
        params.put(Types.INTEGER, stageDataInfo.getStageId());
        params.put(Types.VARCHAR, stageDataInfo.getStageTitle());
        params.put(Types.TIMESTAMP, stageDataInfo.getStageTime());
        
        try
        {
            intResult = getDbManager().executeLastId(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ PlayerInfoDaoImpl : insert ]", e);
        }
        return intResult;
    }
	
	

}
