package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.StagePlayerDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.StagePlayerInfo;

public class StagePlayerDaoImpl extends BaseDaoImpl implements StagePlayerDao
{
	
	private static final Logger logger = Logger.getLogger(StagePlayerDaoImpl.class.getName());
	

	private final static String TABLE_Name = "t_s_stage_player";//静态的任务数据表
	

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		StagePlayerInfo info = new StagePlayerInfo(rs);
		return info;
	}
	
	@Override
	public List<StagePlayerInfo> getStagePlayerList()
	{
		String sql = "select * from " + TABLE_Name;
		
		List<StagePlayerInfo> result = null;

		
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try
        {
//        	DBParamWrapper params = new DBParamWrapper();
    		
            pstmt = getDbManager().executeQuery(sql , null);
            if (null != pstmt)
            {
            	result = new ArrayList<StagePlayerInfo>();
            	
            	rs = pstmt.executeQuery();
            	
            	while (rs.next()) 
            	{
            		StagePlayerInfo info = new StagePlayerInfo(rs);
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
        StagePlayerInfo StageDataInfo = (StagePlayerInfo) obj;
        String sqlText = " INSERT INTO `db_citywar`.`t_s_stage_player`(`stage_data_id`, `stage_prize_id`, `player_id`, `score`, `index`, `desc`, `leagueDate`)"
        		+ "VALUES(?,?,?,?,?,?,?);";
       
           
        
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, StageDataInfo.getStageDataId());
        params.put(Types.INTEGER, StageDataInfo.getStagePrizeId());
        params.put(Types.INTEGER, StageDataInfo.getPlayerId());
        params.put(Types.INTEGER, StageDataInfo.getScore());
        params.put(Types.INTEGER, StageDataInfo.getIndex());
        params.put(Types.VARCHAR, StageDataInfo.getDesc());
        params.put(Types.TIMESTAMP, StageDataInfo.getLeagueDate());
                
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ PlayerInfoDaoImpl : insert ]", e);
        }
        return intResult;
    }
	
	
}
