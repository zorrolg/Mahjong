/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao;

import java.util.List;

import com.citywar.dice.entity.UserGiftIntegrationInfo;

/**
 * 礼品模板 DAO -- 接口
 * 
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version
 * 
 */
public interface UserGiftDao extends BaseDao
{

	List<UserGiftIntegrationInfo> selectUserGiftIdAndSumCount(int userId);
    
}
