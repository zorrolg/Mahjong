package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserMessageTokenDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserMessageTokenInfo;

public class UserMessageTokenDaoImpl extends BaseDaoImpl implements UserMessageTokenDao {

	private final static String USER_MESSAGE_TOKEN = "t_u_token";//
	
    private static final Logger logger = Logger.getLogger(UserMessageTokenDaoImpl.class.getName());
	
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserMessageTokenInfo info = new UserMessageTokenInfo(rs);
		return info;
	}
	
	public int insert(Object obj) {
		int intResult = -1;
		UserMessageTokenInfo userMessageTokenInfo = (UserMessageTokenInfo) obj;
        String sqlText = "insert into " + USER_MESSAGE_TOKEN + "(user_id, token_id) values(?,?)";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userMessageTokenInfo.getUserId());
        params.put(Types.VARCHAR, userMessageTokenInfo.getTokenId());
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ FiveMinSummaryDaoImpl : insert ]", e);
        }
		return intResult;
	}

	@Override
	public boolean isExistUserMessageToken(String tokenId) {
		String sqlText = "select count(1) as count from " + USER_MESSAGE_TOKEN + " where token_id = ?";
		int result = 0;
		PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
        	DBParamWrapper params = new DBParamWrapper();
    		params.put(Types.VARCHAR, tokenId);
            pstmt = getDbManager().executeQuery(sqlText , params.getParams());
            
            if (null != pstmt)
            {
            	pstmt.setString(1, tokenId);
            	rs = pstmt.executeQuery();
            	while (rs.next()) 
            	{
            		result = rs.getInt("count");
            	}
            }
        } catch (Exception e) {
        	e.printStackTrace();
		} finally {
			getDbManager().closeConnection(pstmt, rs);
		}
        return result > 0 ? true : false;
	}
}
