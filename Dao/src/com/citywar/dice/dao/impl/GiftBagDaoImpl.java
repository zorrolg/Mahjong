/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.GiftBagDao;
import com.citywar.dice.entity.GiftBagItem;

/**
 * 礼品模板 DAO -- 实现类
 * 
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version
 * 
 */
public class GiftBagDaoImpl extends BaseDaoImpl implements GiftBagDao
{ 
    @Override
	public Object getTemplate(ResultSet rs) throws SQLException {
    	GiftBagItem giftBag = new GiftBagItem(rs);
		return giftBag;
	}
}
