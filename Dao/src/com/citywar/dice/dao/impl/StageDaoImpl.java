package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.StageDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.PlayerStage;
import com.citywar.dice.entity.StageInfo;
import com.citywar.dice.entity.StageRoundInfo;



public class StageDaoImpl extends BaseDaoImpl implements StageDao {
	
	private final static String STAGE_TABLE = "t_s_stage";//静态的任务数据表	

	private final static String STAGE_ROUND_TABLE = "t_s_stage_round";//静态的任务数据表
	
	private static final Logger logger = Logger.getLogger(StageDaoImpl.class.getName());

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		StageInfo info = new StageInfo(rs);
		return info;
	}
	
	@Override
	public List<StageInfo> getStageList()
	{
		String sql = "select * from " + STAGE_TABLE;
		
		List<StageInfo> result = null;

		
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try
        {

            pstmt = getDbManager().executeQuery(sql , null);
            if (null != pstmt)
            {
            	result = new ArrayList<StageInfo>();
            	
            	rs = pstmt.executeQuery();
            	
            	while (rs.next()) 
            	{
            		StageInfo info = new StageInfo(rs);
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
	public List<StageRoundInfo> getStageRoundList()
	{
		String sql = "select * from " + STAGE_ROUND_TABLE;
		
		List<StageRoundInfo> result = null;

		
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try
        {

            pstmt = getDbManager().executeQuery(sql , null);
            if (null != pstmt)
            {
            	result = new ArrayList<StageRoundInfo>();
            	
            	rs = pstmt.executeQuery();
            	
            	while (rs.next()) 
            	{
            		StageRoundInfo info = new StageRoundInfo(rs);
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
	
	
	
	
	
	
	
	
	
	/**
     * 获取用户的好友信息列表实现
     * 
     * @param userId
     *            当前用户ID
     * @param pageNo
     *            加载的页码
     * @return 好友信息列表
     */
    public List<PlayerStage> getUserStegesInfo(int userId)
    {
        if (userId <= 0)
        {
            return null;
        }

        String sqlText = "select * from t_u_stage WHERE user_id = ? "
                + "ORDER BY stage_id asc";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PlayerStage> stageInfos = null;

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);


        try
        {
            pstmt = getDbManager().executeQuery(sqlText, params.getParams());

            if (null != pstmt)
            {
            	stageInfos = new ArrayList<PlayerStage>();
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                	PlayerStage info = new PlayerStage(rs);
                	stageInfos.add(info);
                }
            }
        }
        catch (SQLException e)
        {
        	e.printStackTrace();
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return stageInfos;
    }
    
    
    
    @Override
    public int insert(Object obj)
    {
        int intResult = -1;
        PlayerStage info = (PlayerStage) obj;
        String sqlText = " INSERT INTO `t_u_stage` (`user_id`,`stage_id`, `charmvalve`) VALUES (?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, info.getUserId());
        params.put(Types.INTEGER, info.getStageId());
        params.put(Types.INTEGER, info.getCharmValve());

        try
        {
            //intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (Exception e)
        {
            logger.error("[ UserItemDaoImpl : insert ]", e);
        }
        return intResult;
    }
    
    @Override
    public int update(Object obj)
    { 
		int intResult = -1;
		PlayerStage info = (PlayerStage) obj;
        String sqlText = " UPDATE `t_u_stage` SET `charmvalve`=?  where `user_id`=? and `stage_id`=?; ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, info.getCharmValve());
        params.put(Types.INTEGER, info.getUserId());
        params.put(Types.INTEGER, info.getStageId());
        
        
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
        }
        catch (Exception e)
        {
            logger.error("[ UserReferenceDaoImpl : update ]", e);
        }
        return intResult;
    }
    
    
    
    
    @Override
    public List<PlayerStage> getTopStageAll()
    {
    	List<PlayerStage> playerList = new ArrayList<PlayerStage>();
    	String sqlText = "CALL `db_citywar`.`p_charm_stage_all`();";
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	try {
    		pstmt = getDbManager().executeQuery(sqlText);
    		if (null != pstmt)
    		{
    			rs = pstmt.executeQuery();
    			while (rs.next())
    			{
    				PlayerStage info = new PlayerStage(rs);
    				playerList.add(info);
    			}
    		}
		} 
    	catch (SQLException e) 
    	{
			e.printStackTrace();
		}
		finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }
    	return playerList;
    }
    
    
}
