package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserReferenceDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.PlayerSlaveCount;
import com.citywar.dice.entity.UserRefWorkInfo;
import com.citywar.dice.entity.UserReferenceInfo;

public class UserReferenceDaoImpl extends BaseDaoImpl implements UserReferenceDao {
	
    private static final Logger logger = Logger.getLogger(UserReferenceDaoImpl.class.getName());


	private final static String USER_REF_TABLE = "t_u_ref";//
	private final static String USER_REF_WORK_TABLE = "t_s_ref_work";//
	
	
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserReferenceInfo userReferenceInfo = new UserReferenceInfo(rs);
		return userReferenceInfo;
	}
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserReferenceInfo userReferenceInfo = (UserReferenceInfo) obj;//TODO
        String sqlText = " INSERT INTO " + USER_REF_TABLE + "(`master_user_id`,`master_user_name`,`master_user_picpath`,`slaves_user_id`,`create_time`, `remove_time`, `take_user_id`, `take_user_name`, `take_user_picpath`, `income_exp`, `income_coins`, `income_charmvalve`) values (?,?,?,?,?,?,?,?,?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userReferenceInfo.getMasterUserId());
        params.put(Types.VARCHAR, userReferenceInfo.getMasterUserName());
        params.put(Types.VARCHAR, userReferenceInfo.getMasterPicPath());
        params.put(Types.INTEGER, userReferenceInfo.getSlavesUserId());
        params.put(Types.TIMESTAMP, userReferenceInfo.getCreateTime());
        params.put(Types.TIMESTAMP, userReferenceInfo.getRemoveTime());
        params.put(Types.INTEGER, userReferenceInfo.getTakeUserId());
        params.put(Types.VARCHAR, userReferenceInfo.getTakeUserName());
        params.put(Types.VARCHAR, userReferenceInfo.getTakeUserPicPath());
        params.put(Types.INTEGER, userReferenceInfo.getIncomeExp());
        params.put(Types.INTEGER, userReferenceInfo.getIncomeCoins());
        params.put(Types.INTEGER, userReferenceInfo.getIncomeCharmValve());
        
        try
        {
            //intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (SQLException e)
        {
            logger.error("[ UserReferenceDaoImpl : insert ]", e);
        }
        return intResult;
	}
	

    @Override
    public int update(Object obj)
    { 
		int intResult = -1;
		UserReferenceInfo userReferenceInfo = (UserReferenceInfo) obj;//TODO csf
        String sqlText = " UPDATE " + USER_REF_TABLE + " SET `master_user_id` = ?,`slaves_user_id` = ?,`create_time` = ?, `remove_time` = ?, `take_user_id` = ?, `take_user_name` = ?, `take_user_picpath` = ?, `income_exp` = ?, `income_coins` = ? where id = ?; ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userReferenceInfo.getMasterUserId());
        params.put(Types.INTEGER, userReferenceInfo.getSlavesUserId());
        params.put(Types.TIMESTAMP, userReferenceInfo.getCreateTime());
        params.put(Types.TIMESTAMP, userReferenceInfo.getRemoveTime());
        params.put(Types.INTEGER, userReferenceInfo.getTakeUserId());
        params.put(Types.VARCHAR, userReferenceInfo.getTakeUserName());
        params.put(Types.VARCHAR, userReferenceInfo.getTakeUserPicPath());
        params.put(Types.INTEGER, userReferenceInfo.getIncomeExp());
        params.put(Types.INTEGER, userReferenceInfo.getIncomeCoins());
        params.put(Types.INTEGER, userReferenceInfo.getId());
        
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ UserReferenceDaoImpl : update ]", e);
        }
        return intResult;
    }
    
    @Override
	public List<PlayerSlaveCount> selectUseReferencesCount(String SQLStr) {
    	List<PlayerSlaveCount> result = new ArrayList<PlayerSlaveCount>();
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	try {
    		pstmt = getDbManager().executeQuery(SQLStr);
    		if (null != pstmt)
    		{
    			rs = pstmt.executeQuery();
    			PlayerSlaveCount info = null;
    			while (rs.next())
    			{
    				info = new PlayerSlaveCount();
    				info.setSlaveCount(rs.getInt("count"));
    				info.setUserId(rs.getInt("userId"));
    				result.add(info);
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
    	return result;
	}
    
    @Override
	public List<PlayerSlaveCount> getUseReferencesCount() {
    	String SQLStr = "SELECT master_user_id AS userId, count(*) as count from t_u_ref where take_user_id=0 GROUP BY master_user_id";
    	return selectUseReferencesCount(SQLStr);
	}

	@Override
	public List<PlayerSlaveCount> getTopUseReferencesCount(int topCount) {
		String SQLStr = "SELECT master_user_id AS userId, count(*) as count from t_u_ref where take_user_id=0 GROUP BY master_user_id ORDER BY count DESC LIMIT " + topCount;
    	return selectUseReferencesCount(SQLStr);
	}
	
	
	
	
	
	@Override
	public List<UserRefWorkInfo> getRefWorkList()
	{
		String sql = "select * from " + USER_REF_WORK_TABLE;
		
		List<UserRefWorkInfo> result = null;

		
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try
        {
//        	DBParamWrapper params = new DBParamWrapper();
    		
            pstmt = getDbManager().executeQuery(sql , null);
            if (null != pstmt)
            {
            	result = new ArrayList<UserRefWorkInfo>();
            	
            	rs = pstmt.executeQuery();
            	
            	while (rs.next()) 
            	{
            		UserRefWorkInfo info = new UserRefWorkInfo(rs);
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
