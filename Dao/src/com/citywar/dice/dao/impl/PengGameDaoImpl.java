/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.PengGameDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.PengGame;

/**
 * 等级 DAO -- 实现
 * 
 * @author tracy
 * @date 2011-12-29
 * @version
 * 
 */
public class PengGameDaoImpl extends BaseDaoImpl implements PengGameDao
{
	
	private static final Logger logger = Logger.getLogger(PengGameDaoImpl.class.getName());
	
    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
    	PengGame info = new PengGame();
    	info.setId(rs.getInt("id"));
    	info.setRoomid(rs.getInt("roomId"));
    	info.setGamedate(rs.getTimestamp("gameDate"));
    	info.setPlayerlist(rs.getString("playerList"));
    	info.setNamelist(rs.getString("nameList"));
    	info.setScorelist(rs.getString("scoreList"));
        return info;
    }

    
    @Override
    public int insert(Object obj)
    {
        int intResult = -1;
        PengGame playerInfo = (PengGame) obj;
        String sqlText = " INSERT INTO `db_citywar`.`t_u_peng`(`roomId`,`gameDate`,`playerList`,`nameList`,`scoreList`) VALUES (?,?,?,?,?);";
        
        
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, playerInfo.getRoomid());
        params.put(Types.TIMESTAMP, playerInfo.getGamedate());
        params.put(Types.VARCHAR, playerInfo.getPlayerlist());
        params.put(Types.INTEGER, playerInfo.getNamelist());
        params.put(Types.INTEGER, playerInfo.getScorelist());
        
        try
        {
            intResult = getDbManager().executeLastId(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ PengGameDaoImpl : insert ]", e);
        }
        return intResult;
    }
    
    
    
    
    @Override
    public int update(Object obj)
    {
        int intResult = -1;
        PengGame playerInfo = (PengGame) obj; 
        String sqlText = " UPDATE `t_u_peng` SET `scoreList` = ? where id=?; ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, playerInfo.getScorelist());        
        params.put(Types.INTEGER, playerInfo.getId());

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
