/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
//import com.citywar.dice.room.RoomMgr;
//import com.citywar.dice.usercmd.UserCmdType;
/**
 * @author : Cookie
 * @date : 2011-7-14
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.SCENE_SMILE, desc = "泡泡表情")
public class SceneSmileCmd extends AbstractUserCmd
{

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.citywar.dice.usercmd.AbstarctUserCmd#execute(com.citywar.dice.gameobjects
     * .GamePlayer, com.citywar.dice.socket.Packet)
     */
    @Override
    public int execute(GamePlayer player, Packet pkg)
    {
        // packet.ClientID = (player.PlayerCharacter.ID);
//        Packet packet = pkg.clone();
//        packet.setClientId(player.getUserId());
//        if (player.getCurrentSpaRoom() != null)
//        {
//            player.getCurrentSpaRoom().sendToRoomPlayer(packet,player,false);
//        }
//        else
//        {
//            if (player.getCurrentRoom() != null)
//            {
//                if (player.getCurrentRoom().getGame() != null)
//                {
//                    player.getCurrentRoom().getGame().sendToAll(packet);
//                }
//                else
//                {
//                    player.getCurrentRoom().sendToAll(packet);
//                }
//            }
//            else
//            {
//                if (player.getCurrentMarryRoom() != null)
//                {
//                    // 0 client.Player.CurrentMarryRoom.SendToAll(packet);
//                    player.getCurrentMarryRoom().sendToAllForScene(packet,
//                                                                   player.getMarryMap());
//                }
//                else
//                {
//                    RoomMgr.getWaitingRoom().sendToALL(packet);
//                }
//            }
//        }
        return 1;
    }

}
