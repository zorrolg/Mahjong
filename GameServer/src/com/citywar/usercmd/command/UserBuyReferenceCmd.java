package com.citywar.usercmd.command;

import com.citywar.dice.entity.UserReferenceInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.LevelMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_BUY_REFERENCE, desc = "用户通过赎金来解除奴隶关系")
public class UserBuyReferenceCmd extends AbstractUserCmd {

	@Override
	public int execute(GamePlayer player, Packet packet) {
		UserReferenceInfo info = player.getPlayerReference().getMasterUserReferenceInfo();
		Packet response = new Packet(UserCmdOutType.USER_BUY_REFERENCE);
		boolean isBuySuccess = false;
		if (null != info) {
			// 判断这条主人的信息是否是有效的,还没有被抢走
			if (info.getTakeUserId() == 0 || info.getRemoveTime() == null) {
				int ransomMoney = LevelMgr.getRansomMoney(player.getPlayerInfo().getLevel());
				if (player.getPlayerInfo().getCoins() >= ransomMoney) {
					player.getPlayerReference().setMasterReferenceId(0);
					isBuySuccess = true;
					// 获取对应等级的赎金
					player.addCoins(-ransomMoney);//可以解除关系,扣除赎金
					player.getPlayerInfo().setBuyItemCoins(player.getPlayerInfo().getBuyItemCoins() + ransomMoney);
					player.getPlayerReference().updatePlayerMaster(player, info);
					player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
				}
			}
		}
		response.putBoolean(isBuySuccess);
		player.getOut().sendTCP(response);
		return 0;
	}

}
