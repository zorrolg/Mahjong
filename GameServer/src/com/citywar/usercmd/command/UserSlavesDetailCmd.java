package com.citywar.usercmd.command;

import com.citywar.dice.entity.PlayerInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameutil.GamePlayerUtil;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.ReferenceMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.DistanceUtil;
import com.citywar.util.HeadPicUtil;

@UserCmdAnnotation(code = UserCmdType.USER_SLAVES_DETAIL, desc = "用户奴隶的详细信息")
public class UserSlavesDetailCmd extends AbstractUserCmd {

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		int slavesUserId = packet.getInt();
		
		if (slavesUserId <= 0) {
			return 0;
		}
		double[] userPosArray = DistanceUtil.getLatAndLon(player.getPlayerInfo().getPos());
		
		Packet response = new Packet(UserCmdOutType.USER_SLAVES_DETAIL);
		//奴隶数
	    response.putInt(ReferenceMgr.getSlaveCount(slavesUserId));
		// 获取对应等级的赎金
		int ransomMoney = LevelMgr.getRansomMoney(player.getPlayerInfo().getLevel());
		response.putInt(ransomMoney);//获取对应等级的赎金
		
		// 加上奴隶数和赎金信息之后,添加个人信息
		PlayerInfo user = GamePlayerUtil.getPlayerInfo(slavesUserId,true);
    	
    	double[] userPos = DistanceUtil.getLatAndLon(user.getPos());
        double userDistance = DistanceUtil.distanceByLatLon(userPosArray[0],
                                                            userPosArray[1],
                                                            userPos[0],
                                                            userPos[1]);
        response.putInt(user.getUserId());
		String str = "";
		if (!user.getPicPath().isEmpty()) {
			str = HeadPicUtil.getRealPicPath(slavesUserId, user.getPicPath());
		}
		response.putStr(str);//玩家图片
        response.putStr(user.getUserName());
        response.putInt(user.getLevel());
        response.putInt(user.getWin());
        response.putInt(user.getLose());
        response.putInt((int) userDistance);
        response.putStr(user.getCity());
        response.putBoolean(false);
        response.putInt(user.getSex());
        response.putInt(LevelMgr.getUserUpgradeGp(user));
        response.putInt(LevelMgr.getUpgradeGp(user.getLevel()));
		player.getOut().sendTCP(response);

		return 0;
	}
}
