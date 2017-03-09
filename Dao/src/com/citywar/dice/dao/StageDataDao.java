package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.StageDataInfo;

public interface StageDataDao extends BaseDao{

	List<StageDataInfo> getStageDataList();
	
}
