package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.PushPlayerInfo;

public interface PushInfoDao extends BaseDao{
	
	
	public List<PushPlayerInfo> getPushPlayerList();
	
}
