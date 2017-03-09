/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.PengGameDetailDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.PengGameDetail;

/**
 * 等级 DAO -- 实现
 * 
 * @author tracy
 * @date 2011-12-29
 * @version
 * 
 */
public class PengGameDetailDaoImpl extends BaseDaoImpl implements PengGameDetailDao
{
	
	private static final Logger logger = Logger.getLogger(PengGameDetailDaoImpl.class.getName());
	
    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
    	PengGameDetail info = new PengGameDetail();
    	info.setPenggameid(rs.getInt("pengGameId"));
    	info.setPengid(rs.getInt("pengId"));    	
    	info.setGamedate(rs.getTimestamp("gameDate"));
    	info.setPlayerlist(rs.getString("playerList"));
    	info.setNamelist(rs.getString("nameList"));
    	info.setGamelist(rs.getString("gameList"));
        return info;
    }

    
    @Override
    public int insert(Object obj)
    {
        int intResult = -1;
        PengGameDetail playerInfo = (PengGameDetail) obj;
        String sqlText = " INSERT INTO `db_citywar`.`t_u_peng_detail`(`pengId`,`gameDate`,`playerList`,`nameList`,`gameList`) VALUES (?,?,?,?,?);";
        
        
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, playerInfo.getPengid());
        params.put(Types.TIMESTAMP, playerInfo.getGamedate());
        params.put(Types.VARCHAR, playerInfo.getPlayerlist());
        params.put(Types.VARCHAR, playerInfo.getNamelist());
        params.put(Types.VARCHAR, playerInfo.getGamelist());
        
        try
        {
            intResult = getDbManager().executeLastId(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ PengGameDetailDaoImpl : insert ]", e);
        }
        return intResult;
    }
    
    
}
