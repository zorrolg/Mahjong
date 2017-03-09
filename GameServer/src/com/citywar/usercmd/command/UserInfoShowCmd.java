/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 查看用户个人信息
 * 
 * @author tracy
 * @date 2011-12-21
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.USER_INFO_SHOW, desc = "查看用户个人信息")
public class UserInfoShowCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        int userId = packet.getInt();
        GamePlayer playerToShow = WorldMgr.getPlayerByID(userId);
        PlayerInfo info = null;
        if (playerToShow != null)
        {
            info = playerToShow.getPlayerInfo();
        }
        else
        {
            info = PlayerBussiness.getPlayerInfoById(userId);
            if (info == null)
                return 0;
            // load 其他信息
        }
        if (info != null)
            player.getOut().sendUserInfo(info);
        return 0;
    }

}
