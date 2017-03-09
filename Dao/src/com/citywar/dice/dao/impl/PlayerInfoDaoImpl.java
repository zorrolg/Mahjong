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

import com.citywar.dice.dao.PlayerInfoDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.PlayerInfo;

/**
 * 玩家信息 DAO -- 实现
 * 
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public class PlayerInfoDaoImpl extends BaseDaoImpl implements PlayerInfoDao
{
    private static final Logger logger = Logger.getLogger(PlayerInfoDaoImpl.class.getName());

    @Override
    public int insert(Object obj)
    {
        int intResult = -1;
        PlayerInfo playerInfo = (PlayerInfo) obj;
        String sqlText = " INSERT INTO `t_u_player`(`UserId`,`UserName`,`UserPwd`,`Money`,`Fortune`,`Pos`,`PicPath`,`Win`,`Lose`,`City`,`Level`,`GP`,`Coins`,`LastLoginDate`,`LastQiutDate`,`IsOnline`,`DrunkLevel`,`LastWakeTime`,`DeepDrunkTime`,`Sex`,`Total`,`Account`,`UDID`,`UserType`,`RegisterDate`,`CharmValve`,`machinery_id`,`maxHallId`,`MachineType`,`SdkType`,`SdkUserName`,`stage`,`VipLevel`,`VipDate`,`buyflag`,`cardCount`,`useCardCount`) "
        		+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, playerInfo.getUserId());
        params.put(Types.VARCHAR, playerInfo.getUserName());
        params.put(Types.VARCHAR, playerInfo.getUserPwd());
        params.put(Types.INTEGER, playerInfo.getMoney());
        params.put(Types.INTEGER, playerInfo.getFortune());
        params.put(Types.VARCHAR, playerInfo.getPos());
        params.put(Types.VARCHAR, playerInfo.getPicPath());
        params.put(Types.INTEGER, playerInfo.getWin());
        params.put(Types.INTEGER, playerInfo.getLose());
        params.put(Types.VARCHAR, playerInfo.getCity());
        
        params.put(Types.INTEGER, playerInfo.getLevel());
        params.put(Types.INTEGER, playerInfo.getGp());
        params.put(Types.INTEGER, playerInfo.getCoins());
        params.put(Types.TIMESTAMP, playerInfo.getLastLoginDate());
        params.put(Types.TIMESTAMP, playerInfo.getLastQiutDate());
        params.put(Types.TINYINT, playerInfo.isOnline() ? 1 : 0);
        params.put(Types.INTEGER, playerInfo.getDrunkLevelSocial() * 10000 + playerInfo.getDrunkLevelContest());
        params.put(Types.TIMESTAMP, playerInfo.getLastWakeTime());
        params.put(Types.TIMESTAMP, playerInfo.getDeepDrunkTime());
        params.put(Types.TINYINT, playerInfo.getSex());	
                
        params.put(Types.INTEGER, playerInfo.getTotal());
        params.put(Types.VARCHAR, playerInfo.getAccount());
        params.put(Types.VARCHAR, playerInfo.getUDID());
        params.put(Types.INTEGER, playerInfo.getUserType());
        params.put(Types.TIMESTAMP, playerInfo.getRegisterDate());
        params.put(Types.INTEGER, playerInfo.getCharmValve());
        params.put(Types.VARCHAR, playerInfo.getMachineryId());
        params.put(Types.INTEGER, playerInfo.getMaxHallId());
        params.put(Types.VARCHAR, playerInfo.getMachineType());
        params.put(Types.VARCHAR, playerInfo.getSdkType());
        
        params.put(Types.VARCHAR, playerInfo.getSdkUserName());
        params.put(Types.INTEGER, playerInfo.getStageId());
        params.put(Types.INTEGER, playerInfo.getVipLevel());
        params.put(Types.TIMESTAMP, playerInfo.getVipDate());       
        params.put(Types.INTEGER, playerInfo.getBuyFlag());
        
        params.put(Types.INTEGER, playerInfo.getCardCount());
        params.put(Types.INTEGER, playerInfo.getUseCardCount());
        
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

    @Override
    public int update(Object obj)
    {
        int intResult = -1;
        PlayerInfo playerInfo = (PlayerInfo) obj; 
        String sqlText = " UPDATE `t_u_player` SET `UserName` = ?,`UserPwd` = ?,`Money` = ?,`Fortune` = ?,`Sign` = ?,`Pos` = ?,`PicPath` = ?,`Win` = ?,`Lose` = ?,`City` = ?,`Level` = ?,`GP` = ?,`Coins` = ?,`Coins_Temp` = ?,`LastLoginDate` = ?,`LastQiutDate` = ?,`IsOnline` = ?,`DrunkLevel` = ?,`LastWakeTime` = ?,`DeepDrunkTime` = ?,`Sex` = ?,`Total` = ? ,`Account` = ?, `UserType` = ?, `CharmValve` = ? , `machinery_id` = ?,`UDID` = ? ,`maxHallId` = ?,`stage` = ?,`VipLevel` = ?,`VipDate` = ?,`buyflag` = ?,`cardCount` = ?,`useCardCount` = ? where UserId=?; ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, playerInfo.getUserName());
        params.put(Types.VARCHAR, playerInfo.getUserPwd());
        params.put(Types.INTEGER, playerInfo.getMoney());
        params.put(Types.INTEGER, playerInfo.getFortune());
        params.put(Types.VARCHAR, playerInfo.getSign());
        params.put(Types.VARCHAR, playerInfo.getPos());
        //组装头像和相册的字段集合
        StringBuilder sb = new StringBuilder(playerInfo.getPicPath());
        List<String> list = playerInfo.getPicPathList();
        if (null != list && list.size() > 0)
        {
        	if (!playerInfo.getPicPath().isEmpty())
        	{
        		sb.append(",");
        	}
        	for (int i = 0; i < list.size(); i++)
        	{
        		sb.append(list.get(i));
        		if (i != list.size() - 1)
        		{
        			sb.append(",");
        		}
        	}
        }
        params.put(Types.VARCHAR, sb.toString());
        params.put(Types.INTEGER, playerInfo.getWin());
        params.put(Types.INTEGER, playerInfo.getLose());
        params.put(Types.VARCHAR, playerInfo.getCity());
        params.put(Types.INTEGER, playerInfo.getLevel());
        params.put(Types.INTEGER, playerInfo.getGp());
        params.put(Types.INTEGER, playerInfo.getCoins());
        params.put(Types.INTEGER, playerInfo.getTempCoins());
        params.put(Types.TIMESTAMP, playerInfo.getLastLoginDate());
        params.put(Types.TIMESTAMP, playerInfo.getLastQiutDate());
        params.put(Types.TINYINT, playerInfo.isOnline() ? 1 : 0);
        params.put(Types.INTEGER, playerInfo.getDrunkLevelSocial() * 10000 + playerInfo.getDrunkLevelContest());
        params.put(Types.TIMESTAMP, playerInfo.getLastWakeTime());
        params.put(Types.TIMESTAMP, playerInfo.getDeepDrunkTime());
        params.put(Types.TINYINT, playerInfo.getSex());
        params.put(Types.INTEGER, playerInfo.getTotal());
        params.put(Types.VARCHAR, playerInfo.getAccount());
        params.put(Types.INTEGER, playerInfo.getUserType());
        params.put(Types.INTEGER, playerInfo.getCharmValve());
        params.put(Types.VARCHAR, playerInfo.getMachineryId());
        params.put(Types.VARCHAR, playerInfo.getUDID());
        params.put(Types.INTEGER, playerInfo.getMaxHallId());
        params.put(Types.INTEGER, playerInfo.getStageId());
        params.put(Types.INTEGER, playerInfo.getVipLevel());
        params.put(Types.TIMESTAMP, playerInfo.getVipDate());
        params.put(Types.INTEGER, playerInfo.getBuyFlag());
        
        params.put(Types.INTEGER, playerInfo.getCardCount());
        params.put(Types.INTEGER, playerInfo.getUseCardCount());
        
        params.put(Types.INTEGER, playerInfo.getUserId());

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

    @Override
    public Object getTemplate(ResultSet rs) throws SQLException
    {
        PlayerInfo info = new PlayerInfo(rs);
        return info;
    }
    
    
    
    @Override
    public List<PlayerInfo> getTopLevel()
    {
    	
    	List<PlayerInfo> playerList = new ArrayList<PlayerInfo>();
    	
    	String sqlText = "CALL `db_citywar`.`p_charm_level`();";
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	try {
    		pstmt = getDbManager().executeQuery(sqlText);
    		if (null != pstmt)
    		{
    			rs = pstmt.executeQuery();
    			while (rs.next())
    			{
    				PlayerInfo info = new PlayerInfo(rs);
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
    
    
    @Override
    public List<PlayerInfo> getTopStage()
    {
    	List<PlayerInfo> playerList = new ArrayList<PlayerInfo>();
    	String sqlText = "CALL `db_citywar`.`p_charm_stage`();";
    	PreparedStatement pstmt = null;
        ResultSet rs = null;
    	try {
    		pstmt = getDbManager().executeQuery(sqlText);
    		if (null != pstmt)
    		{
    			rs = pstmt.executeQuery();
    			while (rs.next())
    			{
    				PlayerInfo info = new PlayerInfo(rs);
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
