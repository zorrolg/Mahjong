package com.citywar.usercmd.command;

import org.apache.log4j.Logger;

import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_ACCOUNT_CHECK, desc = "帐号验证")
public class UserAccountCheckCmd extends AbstractUserCmd
{
	private static Logger logger = Logger.getLogger(UserChatCmd.class
			.getName());

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	String account = packet.getStr();
        String userPwd = packet.getStr();
        
        
        
        boolean isExist = false;
        PlayerInfo info = PlayerBussiness.checkAccoutAndPwd(account, userPwd);
        if(info != null && info.getUserId() > 0)
        {
        	isExist = true;
        }
        
        
        
        Packet pkg = new Packet(UserCmdOutType.USER_ACCOUNT_CHECK);
        pkg.putBoolean(isExist);
        player.getOut().sendTCP(pkg);
        
        
        return 0;
    }

}
