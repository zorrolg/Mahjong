package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.CardTypeInfo;

public interface CardDao extends BaseDao{
	
	
	public List<CardTypeInfo> getCardTypeList();
}
