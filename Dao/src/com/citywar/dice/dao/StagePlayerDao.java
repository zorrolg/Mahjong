package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.StagePlayerInfo;

public interface StagePlayerDao extends BaseDao{

	List<StagePlayerInfo> getStagePlayerList();

	
}
