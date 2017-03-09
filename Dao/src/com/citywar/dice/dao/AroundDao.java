/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.PlayerInfo;

/**
 * 附近好友相关DAO层入口
 * @author charles
 * @date 2011-12-20
 * @version 
 *
 */
public interface AroundDao extends BaseDao
{
    /**
     * 根据用户IDs查找用户列表
     * @param ids 符合条件的用户IDs
     * @return 用户信息列表
     */
    List<PlayerInfo> getAroundPlayers(List<Integer> ids);
}
