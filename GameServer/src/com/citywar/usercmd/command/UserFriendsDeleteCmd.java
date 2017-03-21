/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import org.apache.log4j.Logger;

import com.citywar.bll.FriendsBussiness;
import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 删除好友
 * 
 * @author charles
 * @date 2011-12-19
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.FRIENDS_DELETE, desc = "删除好友")
public class UserFriendsDeleteCmd extends AbstractUserCmd
{
    private static final Logger logger = Logger.getLogger(UserFriendsDeleteCmd.class.getName());

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        try
        {
            int userId = player.getUserId();

            /**
             * 从packet中取得要删除的好友ID.
             */
            int friendUserId = packet.getInt();

            if (userId <= 0 || friendUserId <= 0)
            {
                return 1;
            }
            /**
             * 调用Bussiness层接口删除好友.
             */
            if (FriendsBussiness.deleteFriend(userId, friendUserId))
            {
                /**
                 * 更新内存中的数据
                 */
                player.friendsRemove(friendUserId);                

                /**
                 * 构造响应数据包.
                 */
                Packet response = new Packet(UserCmdOutType.FRIENDS_DELETE_RESP);
                response.putInt(friendUserId);

                /**
                 * 发送响应消息给客户端.
                 */
                player.getOut().sendTCP(response);
            }
        }
        catch (Exception e)
        {
            logger.error("[ UserFriendsDeleteCmd : execute ]", e);
        }

        return 0;
    }
}
