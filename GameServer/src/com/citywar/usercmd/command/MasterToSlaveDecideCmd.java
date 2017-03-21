/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.sql.Timestamp;

import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.UserReferenceInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.ReferenceMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.Config;

/**
 * 
 * @author shanfeng.cao
 * @date 2012-05-22
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.MASTER_TO_SLAVE_DECIDE, desc = "主人去收取税收/主人去给奴隶惩罚")
public class MasterToSlaveDecideCmd extends AbstractUserCmd {
	@Override
	public int execute(GamePlayer player, Packet packet) {


		int UserReferenceId = packet.getInt();
		UserReferenceInfo info = ReferenceMgr.getUserReferenceInfoByUserReferenceId(UserReferenceId);
		boolean isGet = false;
		int addManyOrGp = 0;
		if (info != null) {

				addManyOrGp = info.getIncomeCoins();
				if (addManyOrGp < 0) {
				
					if( player.getUserReward().getLoginAward() < Integer.parseInt(Config.getValue("ref_getcoins_max")))
					{
						
						Timestamp timeNow = new Timestamp(System.currentTimeMillis());
						
						info.setIncomeCoins(addManyOrGp * -1);
						info.setRemoveTime(timeNow);	
						info.setOp(Option.UPDATE);
						
						
						player.addCoins(addManyOrGp*-1);
						player.getUserReward().setLoginAward(player.getUserReward().getLoginAward() + addManyOrGp*-1);
						player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
						isGet = true;			
					}
					else
					{						

						Timestamp timeNow = new Timestamp(System.currentTimeMillis());
						
						info.setIncomeCoins(addManyOrGp * -1);
						info.setRemoveTime(timeNow);	
						info.setOp(Option.UPDATE);
						
						addManyOrGp = addManyOrGp * -1;
					}
			}
		}

		System.out.println("MASTER_TO_SLAVE_DECIDE==================" + info.getId() + "===========" + info.getIncomeExp() + "===========" + info.getIncomeCoins());
		
		Packet response = new Packet(UserCmdOutType.MASTER_TO_SLAVE_DECIDE);
		response.putInt(UserReferenceId);
		response.putBoolean(isGet);
		response.putInt(addManyOrGp*-1);
		
        player.getOut().sendTCP(response);
		return 0;
	}
	

}
