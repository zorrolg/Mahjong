package com.citywar.usercmd.command;


import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;



@UserCmdAnnotation(code = UserCmdType.USER_DRAW_STATE, desc = "玩家奖励")
public class UserDrawStateCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	
    	int typeId = packet.getByte();
    	int paraId = packet.getByte();
    	
    	
    	player.getOut().sendDrawState(typeId, paraId);
    	
    	System.out.println("UserDrawStateCmd=========" + typeId);
    	//执行操作
//    	Packet pkg = new Packet(UserCmdOutType.USER_DRAW_STATE);
//    	
//    	
//    	UserReward responseData = player.getUserReward();   	
//    	if(responseData == null )    	
//    		pkg.putInt(0);    	
//    	else    	
//    	{
//    		pkg.putInt(responseData.getDayDraw());
//    	}
//    		
//    	
//    	   
//    	
//    	int[][] rewardList = AwardMgr.getRewardList();
//    	pkg.putInt(rewardList.length);
//		for(int i = 0;i<rewardList.length;i++)
//		{
//			pkg.putInt(rewardList[i][0]);
//	    	pkg.putInt(rewardList[i][1]);
//		}
//    	player.getOut().sendTCP(pkg);
    	
        return 0;
    }
}

