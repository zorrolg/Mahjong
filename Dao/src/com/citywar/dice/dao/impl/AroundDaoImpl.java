/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.AroundDao;
import com.citywar.dice.entity.PlayerInfo;

/**
 * 旁边用户DAO层实现
 * 
 * @author charles
 * @date 2011-12-20
 * @version
 * 
 */
public class AroundDaoImpl extends BaseDaoImpl implements AroundDao
{
    private static final Logger logger = Logger.getLogger(AroundDaoImpl.class.getName());

    /**
     * 根据用户IDs查找用户列表实现
     * 
     * @param ids
     *            符合条件的用户IDs
     * @return 用户信息列表
     */
    @Override
    public List<PlayerInfo> getAroundPlayers(List<Integer> ids)
    {
        if (null == ids || ids.size() <= 0)
        {
            return null;
        }

        StringBuffer buffer = new StringBuffer();
        for (Integer id : ids)
        {
            buffer.append(id);
            buffer.append(",");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        String playerIds = buffer.toString();

        String sqlText = "select * from t_u_player where userid in ("
                + playerIds + ")" + " order by field(userid," + playerIds + ")";
        List<PlayerInfo> playerInfos = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try
        {
            pstmt = getDbManager().executeQuery(sqlText);
            if (null != pstmt)
            {
                playerInfos = new ArrayList<PlayerInfo>();
                rs = pstmt.executeQuery();
                while (rs.next())
                {
                    PlayerInfo info = new PlayerInfo(rs);
                    playerInfos.add(info);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("[ AroundDaoImpl : getAroundPlayers ]", e);
        }
        finally
        {
            getDbManager().closeConnection(pstmt, rs);
        }

        return playerInfos;
    }

    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

}
