package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.GameTypeCountDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.GameTypeCountInfo;

public class GameTypeCountDaoImpl extends BaseDaoImpl implements GameTypeCountDao  {

	private final static String GAME_TYPE_COUNT_TABLE = "t_s_game_type_count";
	
    private static final Logger logger = Logger.getLogger(GameTypeCountDaoImpl.class.getName());

	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		GameTypeCountInfo info = new GameTypeCountInfo(rs);
		return info;
	}
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		GameTypeCountInfo GameTypeCountInfo = (GameTypeCountInfo) obj;
        String sqlText = "insert into " + GAME_TYPE_COUNT_TABLE + "(count_date, sub_id , game_2_player_count, game_3_player_count, game_4_player_count) values(?,?,?,?,?)";
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.TIMESTAMP, GameTypeCountInfo.getCountDate());
        params.put(Types.INTEGER, GameTypeCountInfo.getSubId());
        params.put(Types.INTEGER, GameTypeCountInfo.getGameTypeTwoPlayer());
        params.put(Types.INTEGER, GameTypeCountInfo.getGameTypeThreePlayer());
        params.put(Types.INTEGER, GameTypeCountInfo.getGameTypeFourPlayer());
        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ GameTypeCountDaoImpl : insert ]", e);
        }
		return intResult;
	}
}
