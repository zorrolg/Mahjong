package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.citywar.dice.dao.UserRegisterDao;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.UserRegister;
 
public class UserRegisterDaoImpl extends BaseDaoImpl implements UserRegisterDao {
	
    private static final Logger logger = Logger.getLogger(UserRegisterDaoImpl.class.getName());

	private final static String TABLE_NAME = "t_u_register";
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {

		return new UserRegister(rs);
	}
	@Override
	public int insert(Object obj) {
		int intResult = -1;
		UserRegister entity = (UserRegister) obj;
        String sqlText = " INSERT INTO " + TABLE_NAME + "(`machinery_id`,`has_count`) values (?,?); ";
        
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, entity.getMachineryId());
        params.put(Types.INTEGER, entity.getHasResCount());
        
        try
        {
        	intResult = getDbManager().executeLastId(sqlText,params.getParams());//获得id号
        }
        catch (SQLException e)
        {
            logger.error("[ UserRegisterDaoImpl : insert ]", e);
        }
        return intResult;
	}
	  @Override
	    public int update(Object obj)
	    { 
			int intResult = -1;
			UserRegister entity = (UserRegister) obj;
	        String sqlText = " UPDATE " + TABLE_NAME + " SET `machinery_id` = ?,`has_count` = ? where id = ?; ";

	        DBParamWrapper params = new DBParamWrapper();
	        params.put(Types.VARCHAR, entity.getMachineryId());
	        params.put(Types.INTEGER, entity.getHasResCount());
	        params.put(Types.INTEGER, entity.getId());
	        
	        try
	        {
	            intResult = getDbManager().executeNoneQuery(sqlText,params.getParams());
	        }
	        catch (SQLException e)
	        {
	            logger.error("[ UserRegisterDaoImpl : update ]", e);
	        }
	        return intResult;
	    }
}
