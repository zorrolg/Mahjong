package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.dice.entity.UserLetter;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameobjects.UserNoReadyLetter;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.HeadPicUtil;

@UserCmdAnnotation(code = UserCmdType.USER_REQUEST_NOREADLETTER, desc = "用户读取未读私信列表")
public class UserReadLetterList extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	
    	 List<UserNoReadyLetter> resposeData = player.getMsgBox().getNoReadLetter();
    	 
    	 Packet response = new Packet(UserCmdOutType.USER_REQUEST_NOREADLETTER);
    	 
    	 if(resposeData == null ){
    		 
    		 response.putInt(0);//列表数据项
    		 response.putInt(0);//总未读私信
    		 
    	 }else{
    		 
    		 response.putInt(resposeData.size());
    		 int noReadCount = 0;
	    	 for(UserNoReadyLetter currentData:resposeData)
	    	 {
	    		 response.putInt(currentData.getLastLetter().getSenderId());
	    		 response.putByte((byte)currentData.getLastLetter().getSenderVip());
	    		 response.putStr(currentData.getLastLetter().getSenderName());
	    		 response.putStr(HeadPicUtil.getRealSmallPicPath(currentData.getLastLetter().getSenderId(),
	    						 currentData.getLastLetter().getSenderPic()));
	    		 response.putStr(currentData.getLastLetter().getSendTime().toString());
	    		 response.putStr(currentData.getLastLetter().getContent());	    		 
	    		 response.putInt(currentData.getLastLetter().getType());
	    		 response.putInt(currentData.getNoReayCount());
	    	
	    		 
	    		 noReadCount+=currentData.getNoReayCount();
	    	 }
	    	 response.putInt(noReadCount);
    	 }
    	 
    	 //发送添加好友的私信内容
    	 List<UserLetter> addFriendTips = player.getMsgBox().readAddFriend();
    	 if(addFriendTips == null ){
    		 
    		 response.putInt(0);//列表数据项
    		 
    	 }else{
    		 response.putInt(addFriendTips.size());
    		 for (UserLetter  currentData:addFriendTips) {
    			 response.putInt(currentData.getSenderId());
	    		 response.putStr(currentData.getSenderName());
	    		 response.putStr(HeadPicUtil.getRealSmallPicPath(currentData.getSenderId(), currentData.getSenderPic()));
	    		 response.putStr(currentData.getSendTime().toString());
	    		 response.putStr(currentData.getContent());	    		 
	    		 response.putInt(currentData.getType());
			}
    	 }
    	 player.sendTcp(response);
        return 0;
    }

}
