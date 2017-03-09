/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.citywar.dice.dao.GiftTemplateDao;
import com.citywar.dice.entity.GiftTemplateInfo;

/**
 * 礼品模板 DAO -- 实现类
 * 
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version
 * 
 */
public class GiftTemplateDaoImpl extends BaseDaoImpl implements GiftTemplateDao
{ 
    @Override
	public Object getTemplate(ResultSet rs) throws SQLException {
    	GiftTemplateInfo giftTemplateInfo = new GiftTemplateInfo(rs);
		return giftTemplateInfo;
	}
}
