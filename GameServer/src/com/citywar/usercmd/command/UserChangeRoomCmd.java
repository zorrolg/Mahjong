/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.manager.RoomMgr;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 用户离开房间
 * 
 * @author Dream
 * @date 2011-12-28
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.ROOM_CHANGE, desc = "用户离开房间")
public class UserChangeRoomCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        BaseRoom room = player.getCurrentRoom();
        if (room == null)
            return 0;

        RoomMgr.exitRoom(room, player, false, 0);
        RoomMgr.enterRoom(player, -1, (byte)3, "", -1, null);
        
        return 0;
    }
}
