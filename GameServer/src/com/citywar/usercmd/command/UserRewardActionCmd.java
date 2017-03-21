package com.citywar.usercmd.command;

import com.citywar.dice.entity.UserReward;
import com.citywar.game.GamePlayer;
import com.citywar.manager.AwardMgr;
import com.citywar.socket.Packet;
import com.citywar.type.RewardActionType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_REWARD_ACTION, desc = "玩家奖励")
public class UserRewardActionCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	int actionId = packet.getByte();
    	RewardActionType action =RewardActionType.valueOf(actionId);
    	
    	UserReward userReward = player.getUserReward();
    	
    	if(userReward == null){
    		return 0;//登陆就会有的
    	}
    	
    	
//    	System.out.println("USER_REWARD_ACTION=========" + actionId);
    	//执行操作
    	boolean isSuccess = action.excute(player);
    	
    	
    	
    	   	
    	
    	int newLoginAward = userReward.getLoginAward();
    	int newLoginIndex = Math.abs(newLoginAward);
    	int[][] rewardList = AwardMgr.getRewardList();
    	
    	Packet response = new Packet(UserCmdOutType.USER_REWARD_ACTION);
    	response.putBoolean(isSuccess); //操作是否成功
    	response.putInt(newLoginAward);
    	response.putInt(rewardList[newLoginIndex][0]);
    	response.putInt(rewardList[newLoginIndex][1]);
    	player.getOut().sendTCP(response);
    	
        return 0;
    }
}

