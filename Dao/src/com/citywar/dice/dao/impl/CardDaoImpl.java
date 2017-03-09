package com.citywar.dice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.citywar.dice.dao.CardDao;
import com.citywar.dice.entity.CardInfo;
import com.citywar.dice.entity.CardTypeInfo;

public class CardDaoImpl extends BaseDaoImpl implements CardDao {
	
//	private final static String BUILD_TABLE = "t_c_card";//静态的任务数据表
	
	private final static String BUILD_TYPE_TABLE = "t_c_card_type";//随时更新的用户已经完成任务表

	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		CardInfo info = new CardInfo(rs);
		return info;
	}
	
	@Override
	public List<CardTypeInfo> getCardTypeList()
	{
		String sql = "select * from " + BUILD_TYPE_TABLE;
		
		List<CardTypeInfo> result = null;

		
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        
        try
        {
//        	DBParamWrapper params = new DBParamWrapper();
    		
            pstmt = getDbManager().executeQuery(sql , null);
            if (null != pstmt)
            {
            	result = new ArrayList<CardTypeInfo>();
            	
            	rs = pstmt.executeQuery();
            	
            	while (rs.next()) 
            	{
            		CardTypeInfo info = new CardTypeInfo(rs);
            		result.add(info);
            	}
            }
        } catch (Exception e) {
        	e.printStackTrace();
		} finally {
			getDbManager().closeConnection(pstmt, rs);
		}
		return result;
	}
	
}
