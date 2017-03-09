package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserLetterDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserLetter;

public class UserLetterDaoImpl extends BaseDaoImpl implements UserLetterDao {
	
    private static final Logger logger = Logger.getLogger(UserLetterDaoImpl.class.getName());

	private final static String T_U_FEEDBACK_TABLE = "t_u_letter";//
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException 
	{
		UserLetter userLetter = new UserLetter();
		
		userLetter.setId(rs.getInt("id"));
		userLetter.setSenderId(rs.getInt("send_id"));
		userLetter.setSenderVip(rs.getInt("send_vip"));
		userLetter.setSenderPic(rs.getString("send_pic"));
		userLetter.setSenderName(rs.getString("send_name"));
		userLetter.setContent(rs.getString("send_content"));
		userLetter.setSendTime(rs.getTimestamp("send_time"));
		
		userLetter.setReceiverId(rs.getInt("receiver_id"));
		userLetter.setReceiverName(rs.getString("receiver_name"));
		userLetter.setType(rs.getInt("type"));
		return userLetter;
	}
	

	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserLetter insertEntity = (UserLetter) obj;
        String sqlText = " INSERT INTO " + T_U_FEEDBACK_TABLE + "(`send_id`,`send_vip`,`send_name`,`send_time`, `send_content`, `receiver_id`, `receiver_name`,`send_pic`,`type`) values (?,?,?,?,?,?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, insertEntity.getSenderId());
        params.put(Types.INTEGER, insertEntity.getSenderVip());
        params.put(Types.VARCHAR, insertEntity.getSenderName());
        params.put(Types.TIMESTAMP, insertEntity.getSendTime());
        params.put(Types.VARCHAR, insertEntity.getContent());
        params.put(Types.INTEGER, insertEntity.getReceiverId());
        params.put(Types.VARCHAR, insertEntity.getReceiverName());
        params.put(Types.VARCHAR, insertEntity.getSenderPic());
        params.put(Types.INTEGER, insertEntity.getType());
        
        try
        {
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (SQLException e)
        {
            logger.error("[ feedbackDaoImpl : insert ]", e);
        }
        return intResult;
	}

}
