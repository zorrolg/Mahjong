package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.AntiAddictionMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/** 
 * 
 * 目前是客户端每30分钟想服务器
 * @author zhiyun.peng
 *
 */
@UserCmdAnnotation(code = UserCmdType.USER_ONLINE_TIME, desc = "在线时长")
public class AntiAddictionCmd extends AbstractUserCmd{

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		if(!AntiAddictionMgr.isStartAntiAddictionSystem())
		{
			return 0;
		}
		int hour = packet.getInt();
		
		player.getUserProperty().setIncomeRatio(AntiAddictionMgr.getAntiAddictionIncomeRatio(hour*60*60));
		
		player.getOut().sendAntiAddiction(3,"");
		
		return 0;
	}
	
}
