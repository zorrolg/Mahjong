package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_LEFT_LETTERUI, desc = "用户离开私信界面")
public class UserLeftLetterUI extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	player.getMsgBox().setLetterUserId(-1);
    	player.getMsgBox().cleanLetter(player.getUserId());
        return 0;
    }

}
