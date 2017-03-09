package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.RoomMgr;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_OPERATION_PLACE, desc = "房主开关房间位置操作")
public class UserOperatePlaceCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        BaseRoom room=player.getCurrentRoom();
        if(room==null || room.getHost()!=player)
            return 0;
        
        int index=packet.getInt();
        boolean isOpen=packet.getBoolean();
        if(index<0 || index>3)
            return 0;
        
        RoomMgr.updateRoomPlace(room,index,isOpen);
        
        return 0;
    }
}
