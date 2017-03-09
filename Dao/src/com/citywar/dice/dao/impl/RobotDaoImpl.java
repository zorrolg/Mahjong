/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.RobotDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.PlayerInfo;

/**
 * 机器人DAO实现
 * 
 * @author charles
 * @date 2012-1-5
 * @version
 * 
 */
public class RobotDaoImpl extends BaseDaoImpl implements RobotDao
{
    private static final Logger logger = Logger.getLogger(RobotDaoImpl.class.getName());

    /**
     * 获取机器人所有阶段的概率信息
     */
    public Map<String, String> getRobotProbilityInfo()
    {
        Map<String, String> robotProbilityInfo = new HashMap<String, String>();

        String sqlText = "select * from t_s_robot";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = getDbManager().executeQuery(sqlText);

            if (null != pstmt)
            {
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    String key = (String) rs.getString("key");
                    String probility = (String) rs.getString("probility");
                    robotProbilityInfo.put(key, probility);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ RobotDaoImpl : getRobotProbilityInfo ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return robotProbilityInfo;
    }

    public List<PlayerInfo> getRobotPlayerInfo(){
    	return getRobotPlayerInfo((byte) 1);
    }
    
	@Override
	public List<PlayerInfo> getRoomRobPlayerInfo() {
		return getRobotPlayerInfo((byte) 2);
	}
    /**
     * 获取机器人信息
     */
	@Override
	public List<PlayerInfo> getRobotPlayerInfo(byte isRobot)
    {
        List<PlayerInfo> robots = new ArrayList<PlayerInfo>();

        String sqlText = "select * from t_u_player where IsRobot = "+isRobot;

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = getDbManager().executeQuery(sqlText);

            if (null != pstmt)
            {
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    PlayerInfo info = new PlayerInfo(rs);
                    robots.add(info);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ RobotDaoImpl : getRobotPlayerInfo ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return robots;
    }

    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PlayerInfo> getPressPlayerInfo()
    {
        List<PlayerInfo> robots = new ArrayList<PlayerInfo>();

        String sqlText = "select * from t_u_player where IsRobot = 3";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            pstmt = getDbManager().executeQuery(sqlText);

            if (null != pstmt)
            {
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    PlayerInfo info = new PlayerInfo(rs);
                    robots.add(info);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ RobotDaoImpl : getPressPlayerInfo ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return robots;
    }

    /**
     * 将机器人胜负场次信息入库
     */
	@Override
	public int updateRobotPlayer(PlayerInfo p) 
	{
		int row = 0;
		String sqlText = "update t_u_player set win = ?, lose = ?, total = ?, Coins = ? where userid = ?";

		DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, p.getWin());
        params.put(Types.INTEGER, p.getLose());
        params.put(Types.INTEGER, p.getTotal());
        params.put(Types.INTEGER, p.getCoins());
        params.put(Types.INTEGER, p.getUserId());
		
        try
        {
        	row = getDbManager().executeNoneQuery(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ RobotDaoImpl : updateRobotPlayer ]", e);
        }
        return row;
	}
}
