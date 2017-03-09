package com.citywar.usercmd.command;

import java.sql.Timestamp;
import java.util.List;

import com.citywar.dice.entity.DrawGoodInfo;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.UserReward;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.DrawMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.ThreadSafeRandom;



@UserCmdAnnotation(code = UserCmdType.USER_DRAW_ACTION, desc = "玩家奖励")
public class UserDrawActionCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	
    	
    	int typeId = packet.getByte();
    	int paraId = packet.getByte();
    	List<DrawGoodInfo> rewardList = DrawMgr.getShopGoodsByType(typeId, paraId);
    	
    	
    	
    	    	
    		    	
    	System.out.println("UserDrawActionCmd==========1================" + player.getPlayerInfo().getCoins());
    	boolean canDraw = false;
		UserReward userAssess = player.getUserReward();		
		if(null != userAssess && rewardList.size() > 0)
		{
			if(rewardList.get(0).getDrawType() == 1 && player.getPlayerInfo().getCoins() >= 100000)
			{
				canDraw = true;
				player.addCoins(-100000);
			}
			if(rewardList.get(0).getDrawType() == 2 && userAssess.getDrawGameCount() > 10)
			{	
				canDraw = true;
				userAssess.setDrawGameCount(userAssess.getDrawGameCount() - 10);
				userAssess.setOp(Option.UPDATE);
			}
			if(rewardList.get(0).getDrawType() >= 10 && userAssess.getDayDraw() == 0)
			{	
				canDraw = true;
				userAssess.setDayDrawTime(new Timestamp(System.currentTimeMillis()));
				userAssess.setDayDraw(1);//加一天，并且领奖过了
				userAssess.setOp(Option.UPDATE);
			}
		}
		
		
		
		int tempId = 0;
    	int count = 0;
		boolean isSuccess = false;    
		if(canDraw)//null != userAssess && 0 <= userAssess.getDayDraw()) {
		{			
						
			ThreadSafeRandom random = new ThreadSafeRandom();
			int index = random.next(0,8);

			tempId = rewardList.get(index).getTemplateId();
			count = rewardList.get(index).getCount();
			if (rewardList.get(index).getTemplateId() == -1) {//游戏币奖励					
				player.addCoins(rewardList.get(index).getCount());
			} else if (rewardList.get(index).getTemplateId() == -2) {//游戏币奖励					
				player.addMoney(rewardList.get(index).getCount());
			}
						
			player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);
			isSuccess = true;
		}
				
		System.out.println("UserDrawActionCmd==========2================" + player.getPlayerInfo().getCoins());
		Packet response = new Packet(UserCmdOutType.USER_DRAW_ACTION);
    	response.putBoolean(isSuccess); //操作是否成功
    	response.putInt(isSuccess ? 1 : 0);
    	response.putInt(tempId);
    	response.putInt(count);
    	player.getOut().sendTCP(response);
    	
        return 0;
    }
}

