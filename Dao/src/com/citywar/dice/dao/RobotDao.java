/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.dice.dao;

import java.util.List;
import java.util.Map;

import com.citywar.dice.entity.PlayerInfo;

/**
 * 机器人DAO
 * @author charles
 * @date 2012-1-5
 * @version 
 *
 */
public interface RobotDao extends BaseDao
{
	/**
	 * 获取机器人概率信息
	 * @return
	 */
	Map<String, String> getRobotProbilityInfo();
	
	/**
	 * 获取机器人信息
	 * @return
	 */
	List<PlayerInfo> getRobotPlayerInfo();
	
	/**
	 * 获取房间内机器人信息
	 * @return
	 */
	List<PlayerInfo> getRoomRobPlayerInfo();
	
	
	 /**
     * 获取机器人信息
     * @return
     */
    List<PlayerInfo> getPressPlayerInfo();
    
    /**
     * 将内存中的机器人信息入库,只更新胜负场次信息
     */
    int updateRobotPlayer(PlayerInfo p);
    
    List<PlayerInfo> getRobotPlayerInfo(byte isRobot);
}
