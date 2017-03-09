/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;


import java.util.List;

import com.citywar.bll.FriendsBussiness;
import com.citywar.dice.entity.FriendInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.DirtyCharUtil;

/**
 * 查找好友(暂时不需要用到)
 * 
 * @author charles
 * @date 2011-12-19
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.FRIENDS_FIND, desc = "查找好友")
public class UserFriendsFindCmd extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        int userId = player.getUserId();
        
        /**
         * 从packet中取得好友的模糊昵称.
         */
        String friendNickname = packet.getStr();
        int pageNo = packet.getInt(); 
        
        if(userId <= 0 || pageNo <= 0 || DirtyCharUtil.checkIllegalChar(friendNickname))
        {
            return 1;
        }
        
        /**
         * 调用Bussiness层接口查找好友.
         */
        @SuppressWarnings("unused")
        List<FriendInfo> friendInfos = FriendsBussiness.findFriends(userId, friendNickname, pageNo);
        
        /**
         * 构造响应数据包.
         */
        Packet response = new Packet(UserCmdOutType.FRIENDS_FIND_RESP);
        
        /**
         * 发送响应消息给客户端.
         */
        player.getOut().sendTCP(response);
        
        return 0;
    }
}
