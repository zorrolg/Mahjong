/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import org.apache.log4j.Logger;

import com.citywar.GameServer;
import com.citywar.bll.FriendsBussiness;
import com.citywar.dice.entity.FriendInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserLetter;
import com.citywar.game.GamePlayer;
import com.citywar.gameutil.GamePlayerUtil;
import com.citywar.manager.KKMgr;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 添加好友
 * 
 * @author charles
 * @date 2011-12-19
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.FRIENDS_ADD, desc = "添加好友")
public class UserFriendsAddCmd extends AbstractUserCmd
{
    private static final Logger logger = Logger.getLogger(UserFriendsAddCmd.class.getName());

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        try
        {
            int userId = player.getUserId();

            /**
             * 从packet中取得需要添加为好友的用户ID.
             */
            int friendUserId = packet.getInt();

            if (userId <= 0 || friendUserId <= 0)
            {
                return 1;
            }

            /**
             * 如果不是好友且不是自己，调用Bussiness层接口添加好友.
             */
            if (userId != friendUserId
                    && !FriendsBussiness.isFriend(userId, friendUserId))
            {
                FriendsBussiness.addFriendsInfos(userId, friendUserId);

                // 更新内存中的好友信息
                FriendInfo friend = FriendsBussiness.getFriend(userId,
                                                               friendUserId);
                player.friendsAdd(friendUserId, friend);
                
                //这里特殊逻辑检测是否完成新手任务5 isCallOne = 4 gameResult = 0
                player.isFinishTask(TaskConditionType.AddFriend, 0, 0);
                
                //系统自动帮用户发送一条私信
                sendAddFriendLetter(player,friendUserId);
                KKMgr.AddFriend(player, friendUserId);
            }

            /**
             * 构造响应数据包.(跟客户端约定，不论是否加成功，都需要发送响应包给客户端)
             */
            Packet response = new Packet(UserCmdOutType.FRIENDS_ADD_RESP);
            response.putInt(friendUserId);
            response.putInt(userId);

            /**
             * 发送响应消息给客户端.
             */
            
            
            BaseRoom room = player.getCurrentRoom();
    		if (null != room)    		
    			room.sendToAll(response);
    		else
    			player.getOut().sendTCP(response);
    		
    		
    		
        }
        catch (Exception e)
        {
            logger.error("[ UserFriendsAddCmd : execute ]", e);
        }

        return 0;
    }
    private String getSexDesc(int sex){
    	return sex==(byte)1?"他":"她";
    }
    private void sendAddFriendLetter(GamePlayer sender,int receiverId){
    	
    	PlayerInfo receiverPlayerInfo = GamePlayerUtil.getPlayerInfo(receiverId, true);
    	
    	String sendCount = "";
    	int type = 0;
    	String sexStr = getSexDesc(sender.getPlayerInfo().getSex());
    	if(GamePlayerUtil.isFriend( receiverPlayerInfo.getUserId(),sender.getUserId())){
    		sendCount = "已加你为"+sexStr+"的好友";
    		type = UserLetter.HAS_FRIEND;
    	}else{
    		sendCount = "把你加为了"+sexStr+"的好友,是否加"+sexStr+"为好友";
    		type  = UserLetter.ADD_FRIEND;
    	}
    	Packet packet = new Packet(20);
    	packet.setCode(UserCmdType.USER_SEND_LETTER);
    	packet.putInt(receiverId);
    	packet.putStr(receiverPlayerInfo.getUserName());
    	packet.putStr(sendCount);
    	packet.putInt(type);
    	packet.position(0);
    	GameServer.getInstance().getHandler().handlePacket(sender.getSession(), packet);
    }
}
