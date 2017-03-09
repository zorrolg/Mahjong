package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.BuildTypeInfo;

public interface BuildDao extends BaseDao{
	
	
	public List<BuildTypeInfo> getBuildTypeList();
}
