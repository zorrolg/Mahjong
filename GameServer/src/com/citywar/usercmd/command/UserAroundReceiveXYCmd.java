/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import org.apache.log4j.Logger;

import com.citywar.game.GamePlayer;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 接受用户地理位置信息,并更新至内存
 * 
 * @author charles
 * @date 2011-12-22
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.AROUND_RECEIVE_XY, desc = "接受用户地理位置信息")
public class UserAroundReceiveXYCmd extends AbstractUserCmd
{
    private static final Logger logger = Logger.getLogger(UserAroundReceiveXYCmd.class.getName());

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        try
        {
            if (null == player)
            {
                return 1;
            }

            int userId = packet.getInt();

            // 纬度，经度
            double x = packet.getDouble();
            double y = packet.getDouble();


            // 更新内存中的数据
	        String hashKey = WorldMgr.updatePlayerLocationInfo(userId, x, y);
	        WorldMgr.updateAroundPlayerLocationInfo(hashKey, userId, x, y);

        }
        catch (Exception e)
        {
            logger.error("[ UserAroundReceiveXYCmd : XXX ]", e);
        }

        return 0;
    }
}
