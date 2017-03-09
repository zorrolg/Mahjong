package com.citywar.usercmd.command;

import com.citywar.game.action.UserShakeAction;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.PlayerState;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * player shaked smartphone,update the state of player in the server,
 * return dice point to player.
 * @author Dream
 * @date 2012-2-2
 * @version 
 *
 */
@UserCmdAnnotation(code = UserCmdType.SHAKE_PREPARE, desc = "Player Shakes Cellphone to Prepare Game")
public class UserShakePrepareCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        byte playerState=packet.getByte();
        
        BaseRoom room=player.getCurrentRoom();
        if(room==null)
            return 0;
        
        if(room.getGame()!=null && playerState==PlayerState.EndShaked)
        {
            // add action to game thread,return dice points to player.
            room.getGame().AddAction(new UserShakeAction(player,0));
        }
        
        return 0;
    }

}
