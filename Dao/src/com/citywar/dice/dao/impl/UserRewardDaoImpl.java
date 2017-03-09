package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserRewardDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserReward;

public class UserRewardDaoImpl extends BaseDaoImpl implements UserRewardDao {
	
    private static final Logger logger = Logger.getLogger(UserRewardDaoImpl.class.getName());

	private final static String TABLE_NAME = "t_u_reward";
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserReward data = new UserReward();
		data.setId(rs.getInt("id"));
		data.setUserId(rs.getInt("user_id"));
		data.setAssessFlag(rs.getInt("assess_flag"));
		data.setShareFlag(rs.getInt("share_flag"));
		data.setLastShareTime(rs.getTimestamp("share_time"));
		data.setFriendFlag(rs.getInt("friend_flag"));
		data.setLastFriendTime(rs.getTimestamp("friend_time"));
		data.setLoginAward(rs.getInt("login_award"));
		data.setLoginAwardTime(rs.getTimestamp("login_award_time"));
		
		data.setDrawGameCount(rs.getInt("draw_game_count"));
		data.setDayDraw(rs.getInt("day_draw"));
		data.setDayDrawTime(rs.getTimestamp("day_draw_time"));
		
		return data;
	}

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserReward entity = (UserReward) obj;
        String sqlText = " INSERT INTO " + TABLE_NAME + "(`user_id`,`assess_flag`,`share_flag`,`share_time`,`friend_flag`,`friend_time`,`login_award`,`login_award_time`,`draw_game_count`,`day_draw`,`day_draw_time`) values (?,?,?,?,?,?,?,?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, entity.getUserId());
        params.put(Types.INTEGER, entity.getAssessFlag());
        params.put(Types.INTEGER, entity.getShareFlag());
        params.put(Types.TIMESTAMP, entity.getLastShareTime());
        params.put(Types.INTEGER, entity.getFriendFlag());
        params.put(Types.TIMESTAMP, entity.getLastFriendTime());
        params.put(Types.INTEGER, entity.getLoginAward());
        params.put(Types.TIMESTAMP, entity.getLoginAwardTime());
        
        params.put(Types.INTEGER, entity.getDrawGameCount());
        params.put(Types.INTEGER, entity.getDayDraw());
        params.put(Types.TIMESTAMP, entity.getDayDrawTime());
        
        try
        {
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (SQLException e)
        {
            logger.error("[ UserRewardDaoImpl : insert ]", e);
        }
        return intResult;
	}
	
	  @Override
	    public int update(Object obj)
	    { 
			int intResult = -1;
			UserReward info = (UserReward) obj;
	        String sqlText = " UPDATE " + TABLE_NAME + " SET `assess_flag`=?, `share_flag`=?, `share_time`=?,`friend_flag`=?, `friend_time`=?, `login_award`=?, `login_award_time`=?, `draw_game_count`=?, `day_draw`=?, `day_draw_time`=? where id = ?; ";

	        DBParamWrapper params = new DBParamWrapper();
	        params.put(Types.INTEGER, info.getAssessFlag());
	        params.put(Types.INTEGER, info.getShareFlag());
	        params.put(Types.TIMESTAMP, info.getLastShareTime());
	        params.put(Types.INTEGER, info.getFriendFlag());
	        params.put(Types.TIMESTAMP, info.getLastFriendTime());
	        params.put(Types.INTEGER, info.getLoginAward());
	        params.put(Types.TIMESTAMP, info.getLoginAwardTime());
	        	        
	        params.put(Types.INTEGER, info.getDrawGameCount());
	        params.put(Types.INTEGER, info.getDayDraw());
	        params.put(Types.TIMESTAMP, info.getDayDrawTime());
	        
	        params.put(Types.INTEGER, info.getId());
	        
	        try
	        {
	            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
	        }
	        catch (SQLException e)
	        {
	            logger.error("[ UserRewardDaoImpl : update ]", e);
	        }
	        return intResult;
	    }
}
