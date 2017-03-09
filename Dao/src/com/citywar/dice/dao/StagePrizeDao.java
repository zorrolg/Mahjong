package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.StagePrizeInfo;

public interface StagePrizeDao extends BaseDao{

	List<StagePrizeInfo> getStagePrizeList();
	
}
