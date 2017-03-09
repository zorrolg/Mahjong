/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.PlayerInfo;

/**
 * 玩家信息 DAO -- 接口
 * 
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public interface PlayerInfoDao extends BaseDao
{
	public List<PlayerInfo> getTopStage();
	public List<PlayerInfo> getTopLevel();
}
