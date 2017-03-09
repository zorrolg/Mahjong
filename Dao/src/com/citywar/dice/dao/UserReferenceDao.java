package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.PlayerSlaveCount;
import com.citywar.dice.entity.UserRefWorkInfo;


public interface UserReferenceDao extends BaseDao{
	
	List<PlayerSlaveCount> getUseReferencesCount();

	List<PlayerSlaveCount> selectUseReferencesCount(String SQLStr);

	List<PlayerSlaveCount> getTopUseReferencesCount(int topCount);

	List<UserRefWorkInfo> getRefWorkList();
	
}
