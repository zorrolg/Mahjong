package com.citywar.usercmd.command;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.citywar.bll.PlayerBussiness;
import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserLetter;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.manager.ErrorMgr;
import com.citywar.manager.LetterMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.UserPushMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.HeadPicUtil;

@UserCmdAnnotation(code = UserCmdType.USER_SEND_LETTER, desc = "用户发送私信")
public class UserSendLetter extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	UserLetter newLetter = new UserLetter();
    	newLetter.setOp(Option.INSERT);
    
    	newLetter.setSenderId(player.getUserId());
    	newLetter.setSenderVip(player.getPlayerInfo().getVipLevel());
    	newLetter.setSenderName(player.getPlayerInfo().getUserName());
    	newLetter.setSenderPic(player.getPlayerInfo().getPicPath());
    	newLetter.setReceiverId(packet.getInt());
    	newLetter.setReceiverName(packet.getStr());
    	newLetter.setContent(packet.getStr());
    	newLetter.setType(packet.getInt());
    
    	newLetter.setSendTime(new Timestamp(System.currentTimeMillis()));
    	
    	
    	int result = 0;
    	boolean isPush = false;
    	//客户端程序出现的错误
    	if(newLetter.getReceiverName().startsWith("Error:"))
    	{
    		ErrorMgr.putErrorLetter(newLetter);
    	}    	
//    	else if(RobotMgr.isRobotByID(newLetter.getReceiverId()))//给机器人发送私信
//    	{
//    		
//    	}
    	else{
    		
    		
    		result = 1;
    		GamePlayer receiver = WorldMgr.getPlayerByID(newLetter.getReceiverId());
			if (null == receiver) {
				//从在值班的机器人中取
				Player robot = RobotMgr.getOnDutyRobotByID(newLetter.getReceiverId());
				if (null != robot) {
					receiver = robot.getPlayerDetail();
				}
			}
			
    		
    		if(receiver != null) //私信接收者在线 
    		{
    			sendOnLineLetter(receiver, newLetter);
    			
    			//发送者和接收者 不处于即时聊天状态时  作为未读私信放入内存
    			if(receiver.getMsgBox().getLetterUserId() != newLetter.getSenderId())
    			{
    				receiver.getMsgBox().putLetter(newLetter);
    			}
    		}
    		else {//私信接收者不在线 马上入库(否则在定时入库之前 接受者登录 将看不到信息)
    			LetterMgr.add(newLetter);
    			isPush = true;
    			
    		}
    	}
       
    	
    	Packet pkg = new Packet(UserCmdOutType.USER_SEND_LETTER);
    	pkg.putInt(result);
    	player.getOut().sendTCP(pkg);
    	
    	
    	if(isPush)
    	{
    		PlayerInfo revicer = PlayerBussiness.getPlayerInfoById(newLetter.getReceiverId());
			if(revicer != null && !revicer.getMachineryId().isEmpty())
			{
				List<String> tokens=new ArrayList<String>();    		            	
	            tokens.add(revicer.getMachineryId()); 
	            
	            String strMsg = LanguageMgr.getTranslation("CityWar.Letter.PushMsg",newLetter.getSenderName());		            
	            UserPushMgr.sendPush(tokens, strMsg, 1, "default"); 
	            
			}
    	}
    	
        return 0;
    }
    
    private void sendOnLineLetter(GamePlayer receiver,UserLetter letter){
    	
    	if(receiver != null && letter != null )
    	{
    		
    		String tsStr = "";
        	DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");  
        	tsStr = sdf.format(letter.getSendTime());   
        	
    		Packet packet = new Packet(UserCmdOutType.USER_REVICE_LETTER);
    		packet.putInt(letter.getSenderId());
    		packet.putByte((byte)letter.getSenderVip());
    		packet.putStr(letter.getSenderName());
    		packet.putStr(HeadPicUtil.getRealSmallPicPath(letter.getSenderId(),letter.getSenderPic()));
    		packet.putStr(tsStr);
    		packet.putStr(letter.getContent());
    		packet.putInt(letter.getType());
    		packet.putInt(letter.getReceiverId());
    		packet.putStr(letter.getReceiverName());
   
    		
    		receiver.getOut().sendTCP(packet);
    	}
    	
    }

}
