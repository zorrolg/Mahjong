package com.citywar.usercmd.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.citywar.dice.entity.UserLetter;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_READ_LETTER, desc = "玩家打开与某人的私信")
public class UserOpenLetter extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	Packet response = new Packet(UserCmdOutType.USER_READ_LETTER);
    	
    	int senderId =  packet.getInt();
    	List<UserLetter> resposeData = player.getMsgBox().readLetter(senderId);
    	player.getMsgBox().setLetterUserId(senderId);
    	
    	if(resposeData == null || resposeData.size() ==0)
    	{
    		response.putInt(0);
    	}else{
    		
        	int dataSize = resposeData.size();
        	response.putInt(dataSize);
        	
        	String tsStr = "";
        	DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");  
        	
        	for(UserLetter currentDate:resposeData)
        	{
        		tsStr = sdf.format(currentDate.getSendTime());   
        		response.putStr(currentDate.getContent());
        		response.putStr(tsStr);
        	}
    	}
    	player.getOut().sendTCP(response);
        return 0;
    }

}
