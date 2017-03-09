package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.HallTypeDao;
import com.citywar.dice.entity.HallTypeInfo;

/**
 * @author shanfeng.cao
 * @date 2012-07-25
 * @version
 * 
 */
public class HallTypeDaoImpl extends BaseDaoImpl implements HallTypeDao {
	
	@Override
	public Object getTemplate(ResultSet rs) throws SQLException {
		HallTypeInfo HallTypeInfo = new HallTypeInfo(rs);
		return HallTypeInfo;
	}
}