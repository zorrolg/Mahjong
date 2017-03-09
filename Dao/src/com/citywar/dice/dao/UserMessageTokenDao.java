package com.citywar.dice.dao;

public interface UserMessageTokenDao extends BaseDao {
	
	public boolean isExistUserMessageToken(String tokenId);
}
