package com.citywar.usercmd.command;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;

import com.citywar.game.GamePlayer;
import com.citywar.manager.RobotChatMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_CHAT, desc = "聊天")
public class UserChatCmd extends AbstractUserCmd
{
	private static Logger logger = Logger.getLogger(UserChatCmd.class
			.getName());

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        int userId = player.getUserId();
        String userName = player.getPlayerInfo().getUserName();
        int voiceId = packet.getInt();
        String massge = packet.getStr();
        
        
        boolean isRoomSend = player.onGameSendChat(voiceId,massge);
        if(!isRoomSend)
        {
        	
        	Packet response = new Packet(UserCmdOutType.USER_CHAT);
        	response.putInt(1);
            response.putInt(userId);
            response.putInt(voiceId);
            response.putStr(player.getPlayerInfo().getCity());
            response.putStr(player.getPlayerInfo().getUserName());
            response.putStr(massge);
        	WorldMgr.sendToAll(response);
        	return 0;
        }
        
        
        if (massge.isEmpty() || massge.indexOf("%") != -1) {
        	return 0;
        }
        try {
			massge = new String(massge.getBytes("utf8"), "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("UserChatCmd to utf8 error:", e);
			return 0;
		}
        
        if(massge.startsWith("Expression")
        		|| "快点叫啊，怕输呀？".equals(massge)
        		|| "诶哟我去，这运气，忒好啦!".equals(massge)
        		|| "今天太倒霉了，哎！！！".equals(massge)
        		|| "五个一呀五个一~~！！！！".equals(massge)
        		|| "今天不是你倒下就是我倒下！".equals(massge)
        		|| "想赢我？也不看看这是谁的地盘。".equals(massge)) {//测试用的
        	
        } else {
        	RobotChatMgr.addMessage(massge);
        }
        
        BaseRoom room = player.getCurrentRoom();
        if(null != room && null != room.getGame()) {
            List<GamePlayer> playerList = room.getPlayers();
            for(GamePlayer robot : playerList) {
            	if(robot.getIsRobot()) {
            		RobotChatMgr.playerOnGameSendChat(robot, massge);
            	}
            }
        }
        return 0;
    }

}
