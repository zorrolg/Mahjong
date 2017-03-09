package com.citywar.dice.dao;

import com.citywar.dice.entity.Version;


public interface VersionDao extends BaseDao {
	
	
	public Version getMaxVersionByType(int type);
	
	

}
