package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.PlayerStage;
import com.citywar.dice.entity.StageInfo;
import com.citywar.dice.entity.StageRoundInfo;


public interface StageDao extends BaseDao{
	
	
	public List<PlayerStage> getTopStageAll();
	public List<StageInfo> getStageList();
	
	public List<PlayerStage> getUserStegesInfo(int userId);
	List<StageRoundInfo> getStageRoundList();
}
