/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.sql.Timestamp;

import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.UserRefWorkInfo;
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
 * 查看快喝醉的用户
 * 
 * @author shanfeng.cao
 * @date 2011-12-20
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.USER_DRUNK_UNLOCK, desc = "酒奴赎身")
public class UserDrunkUnlockCmd extends AbstractUserCmd {
	
//	private static final Logger logger = Logger.getLogger(UserDrunkUnlockCmd.class.getName());

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		
		boolean isSuccess = false;
		int needMoney = 0;
		UserReferenceInfo ref = player.getPlayerReference().getMasterUserReferenceInfo();
		int needSecond = player.getPlayerReference().checkRefWork(ref);
		if(needSecond > 0 && ref.getIncomeExp() > 0)
		{
			int needMinute = (int)(needSecond/60) + (needSecond%60 > 0 ? 1 : 0);
			int minuteRate = Integer.parseInt(Config.getValue("ref_minute_exchangerate"));
			needMoney = needMinute / minuteRate + (needMinute%minuteRate > 0 ? 1 : 0);
			
			if(player.getPlayerInfo().getMoney() > needMoney)
			{
				player.getPlayerInfo().setDrunkLevelSocial(player.getPlayerInfo().getDrunkLevelLimit());
				player.removeMoney(needMoney, 0);				
				
				Timestamp timeNow = new Timestamp(System.currentTimeMillis());
				int workHour = (int)(timeNow.getTime() - ref.getCreateTime().getTime()) / 1000 / 3600;
				if(workHour < 1)
					workHour = 1;
				
				UserRefWorkInfo work = ReferenceMgr.getWorkById(ref.getIncomeExp());
				ref.setIncomeCoins(workHour * work.getWorkCoin() * -1);
				ref.setIncomeExp(ref.getIncomeExp()*-1);
//				ref.setRemoveTime(timeNow);	
				ref.setOp(Option.UPDATE);
				
				player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
				player.getOut().sendUpdateBaseInfo();
				
				
				
	            
	            if(player.getCurrentRoom() != null)
	            {
	            	Packet pkg = new Packet(UserCmdOutType.BROC_PROP_USE);
		            pkg.putInt(player.getUserId());
		            pkg.putInt(0);
		            pkg.putInt(player.getDrunkLevel()); // 当前醉酒度
		            pkg.putInt(player.getPlayerInfo().getDrunkLevelLimit()); // 最大醉酒度
		            player.getCurrentRoom().sendToAll(pkg);
	            }
	            	
	            
	            
				
				isSuccess = true;
				System.out.println("USER_DRUNK_UNLOCK==================" + ref.getId() + "===========" + ref.getIncomeExp() + "===========" + ref.getIncomeCoins());
			}
		}
		
		
		
		Packet pkg = new Packet(UserCmdOutType.USER_DRUNK_UNLOCK);
		pkg.putBoolean(isSuccess);
		pkg.putInt(needMoney);
		player.getOut().sendTCP(pkg);
		return 0;
	}

}

