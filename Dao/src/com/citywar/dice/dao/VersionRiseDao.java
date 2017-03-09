package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.VersionRise;

public interface VersionRiseDao extends BaseDao {
	
	public  List<VersionRise> doSearchByVerionCode(String versionCode);

}
