package com.citywar.bll;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.GameTypeCountInfo;

public class GameTypeCountBussiness {
	
	public static int addGameTypeCountInfo(GameTypeCountInfo info)
	{
		return DaoManager.getGameTypeCountDao().insert(info);
	}
}
