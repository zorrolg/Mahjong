package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.RobotChatDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.RobotChatInfo;

public class RobotChatDaoImpl extends BaseDaoImpl implements RobotChatDao {

    private static final Logger logger = Logger.getLogger(RobotChatDaoImpl.class.getName());
	private final static String USER_CHAT = "t_u_chat";
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		RobotChatInfo robotChatInfo = new RobotChatInfo(rs);
		return robotChatInfo;
	}
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		RobotChatInfo robotChatInfo = (RobotChatInfo) obj;
		String sqlText = " INSERT INTO "
				+ USER_CHAT
				+ "(`game_state`,`weight`,`probability`,`type`, `regex`, `answer`) values (?,?,?,?,?,?); ";

		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, robotChatInfo.getGameState());
		params.put(Types.INTEGER, robotChatInfo.getWeight());
		params.put(Types.INTEGER, robotChatInfo.getProbability());
		params.put(Types.INTEGER, robotChatInfo.getType());
		params.put(Types.VARCHAR, robotChatInfo.getRegex());
		params.put(Types.VARCHAR, robotChatInfo.getAnswer());

		try {
			intResult = getDbManager().executeLastId(sqlText,
					params.getParams());// 获得id号
		} catch (SQLException e) {
			logger.error("[ RobotChatDaoImpl : insert ]", e);
		}
		return intResult;
	}
	
	public int getRobotChatCount()
    {
    	int result = 0;
    	String sqlText = "SELECT count(*) as count FROM `t_u_chat`;";
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	try {
    		pstmt = getDbManager().executeQuery(sqlText);
    		if (null != pstmt)
    		{
    			rs = pstmt.executeQuery();
    			while (rs.next())
    			{
    				result = rs.getInt("count");
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
}