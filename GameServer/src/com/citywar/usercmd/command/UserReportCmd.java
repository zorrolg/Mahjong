package com.citywar.usercmd.command;

import com.citywar.bll.UserReportBussiness;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_REPORT_PIC, desc = "玩家返回操作类型")
public class UserReportCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	
    	
    	int ReportUserId = packet.getInt();
    	int UserId = packet.getInt();
    	String PicName = packet.getStr();
    	
    	int row = UserReportBussiness.addUserReport(ReportUserId, UserId, PicName);
    	
    	
    	
    	
    	Packet pkg = new Packet(UserCmdOutType.USER_REPORT_PIC);
        pkg.putBoolean(row > 0 ? true : false);
        player.getOut().sendTCP(pkg);
    		
    		
        return 0;
    }
}

