package com.citywar.usercmd.command;

import java.sql.Timestamp;

import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.FeedbackInfo;
import com.citywar.dice.entity.Option;
import com.citywar.game.GamePlayer;
import com.citywar.manager.FeedbackMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_FEEDBACK, desc = "用户反馈信息")
public class UserFeedbackCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	
    	String feedbackContents = "";
    	int length = packet.getInt();
    	for(int i = 0;i<length;i++)
    		feedbackContents += packet.getStr();//反馈的内容
    	
    	
    	boolean isSuccess = true;
		if (LanguageMgr.getStringLength(feedbackContents) <= 300) {
		   	if (feedbackContents.isEmpty()) {
		   		isSuccess = false;
		    }
		}
		else {
			 isSuccess = false;
		}
    	if(isSuccess) {
        	int userId = player.getUserId();//反馈属于的用户ID
        	int feedbackType = 1;//反馈类型（默认的类型）
        	Timestamp feedbackCreateTime = new Timestamp(System.currentTimeMillis());//反馈时间
        	FeedbackInfo info = new FeedbackInfo();
        	info.setUserId(userId);
        	info.setFeedbackContents(feedbackContents);
        	info.setFeedbackCreateTime(feedbackCreateTime);
        	info.setFeedbackType(feedbackType);
        	info.setOp(Option.INSERT);
        	FeedbackMgr.addFeedback(info);
    	}
    	/**
         * 构造反馈响应数据包
         */
        Packet response = new Packet(UserCmdOutType.USER_FEEDBACK);
        response.putBoolean(isSuccess);//是否反馈成功
        player.getOut().sendTCP(response);
        return 0;
    }

}
