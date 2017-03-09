/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.bll;

import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.entity.PlayerInfo;

/**
 * 附近用户的business入口
 * @author charles
 * @date 2011-12-20
 * @version 
 *
 */
public class AroundBussiness
{
    /**
     * 获取附近用户列表
     * @param ids 用户的ID列表(取哪些ID在UserAroundCmd里面做判断)
     * @return 用户信息列表
     */
    public static List<PlayerInfo> getAroundPlayers(List<Integer> ids)
    {
        return DaoManager.getAroundDao().getAroundPlayers(ids);
    }
}
