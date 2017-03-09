package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserCardDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserCardInfo;

public class UserCardDaoImpl extends BaseDaoImpl implements UserCardDao {
	
	private final static String BUILD_TABLE = "t_u_card";//静态的任务数据表
	
	private static final Logger logger = Logger.getLogger(AroundDaoImpl.class.getName());
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		UserCardInfo info = new UserCardInfo(rs);
		return info;
	}


	
	
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserCardInfo userCard = (UserCardInfo) obj;
        String sqlText = " INSERT INTO " + BUILD_TABLE + "(`UserId`,`card_develop_list`,`card_isdevelop`,`card_develop_id`,`card_develop_time`,`card_factory_list`,`card_isfactory`,`card_factory_time`) values (?,?,?,?,?,?,?,?); ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userCard.getPlayerId());
        
        params.put(Types.VARCHAR, userCard.getCardDevelopList());
        params.put(Types.INTEGER, userCard.getCardIsDevelop());
        params.put(Types.INTEGER, userCard.getCardDevelopId());
        params.put(Types.TIMESTAMP, userCard.getCardDevelopTime());
        
        params.put(Types.VARCHAR, userCard.getCardFactoryList());
        params.put(Types.INTEGER, userCard.getCardIsFactory());
        params.put(Types.TIMESTAMP, userCard.getCardFactoryTime());
        
        
        
       
        try
        {
            intResult = getDbManager().executeLastId(sqlText,params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ UserCardDaoImpl : insert ]", e);
        }
        return intResult;
	}
	
	
	
	@Override
    public int update(Object obj)
    {
        int intResult = -1;
        UserCardInfo userCard = (UserCardInfo) obj; 
        String sqlText = " UPDATE `" + BUILD_TABLE + "` SET `card_develop_list` = ?,`card_isdevelop` = ?,`card_develop_id` = ?,`card_develop_time` = ?,`card_factory_list` = ?,`card_isfactory` = ?,`card_factory_time` = ? where UserId=?; ";

        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, userCard.getCardDevelopList());
        params.put(Types.INTEGER, userCard.getCardIsDevelop());
        params.put(Types.INTEGER, userCard.getCardDevelopId());
        params.put(Types.TIMESTAMP, userCard.getCardDevelopTime());
        
        params.put(Types.VARCHAR, userCard.getCardFactoryList());
        params.put(Types.INTEGER, userCard.getCardIsFactory());
        params.put(Types.TIMESTAMP, userCard.getCardFactoryTime());
        
        params.put(Types.INTEGER, userCard.getPlayerId());

        try
        {
            intResult = getDbManager().executeNoneQuery(sqlText,
                                                        params.getParams());
        }
        catch (SQLException e)
        {
            logger.error("[ UserCardDaoImpl : insert ]", e);
        }
        return intResult;
    }
	
	
}
