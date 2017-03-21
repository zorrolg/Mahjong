package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.manager.MessgageOperateMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.MESSAGE_AND_OPERATE, desc = "玩家返回操作类型")
public class UserReturnAndOperate extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	short operateMgrCode = packet.getShort();
    	MessgageOperateMgr.executeOperate(player, operateMgrCode);
        return 0;
    }
}

