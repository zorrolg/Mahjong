/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.bll;

import java.util.List;
import java.util.Map;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.PlayerInfo;

/**
 * 机器人Business类
 * @author charles
 * @date 2012-1-5
 * @version 
 *
 */
public class RobotBussiness
{
    /**
     * 获取机器人各阶段概率信息
     * @return
     */
    public static Map<String, String> getRobotProbilityInfo()
    {
        return DaoManager.getRobotDao().getRobotProbilityInfo();
    }
    
    /**
     * 获取机器人信息
     * @return
     */
    public static List<PlayerInfo> getRobotPlayerInfo()
    {
        return DaoManager.getRobotDao().getRobotPlayerInfo();
    }
    
    /**
     * 获取房间内虚拟机器人信息
     * @return
     */
    public static List<PlayerInfo> getRoomRobPlayerInfo()
    {
        return DaoManager.getRobotDao().getRoomRobPlayerInfo();
    }
    
    /**
     * 获取机器人信息
     * @return
     */
    public static List<PlayerInfo> getPressPlayerInfo()
    {
        return DaoManager.getRobotDao().getPressPlayerInfo();
    }
    
    /**
     * 入库机器人的胜负场次信息
     */
    public static int updateRobotPlayer(PlayerInfo p)
    {
    	return DaoManager.getRobotDao().updateRobotPlayer(p);
    }

	public static List<PlayerInfo> getRobotPlayerInfo(byte type) {
        return DaoManager.getRobotDao().getRobotPlayerInfo(type);
	}
}
