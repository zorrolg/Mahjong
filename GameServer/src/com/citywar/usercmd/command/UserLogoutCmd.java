/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 玩家退出或者掉线
 * 
 * @author tracy
 * @date 2011-12-20
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.LOGOUT, desc = "玩家退出或者掉线")
public class UserLogoutCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        if (player != null)
            player.quit();
        return 0;
    }

}
