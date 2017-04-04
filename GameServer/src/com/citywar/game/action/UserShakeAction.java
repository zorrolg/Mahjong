package com.citywar.game.action;

import com.citywar.game.BaseGame;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.type.GameState;
import com.citywar.type.PlayerState;

/**
 * user prepares to play game by shaking smartphone. the server handle two thing
 * for the action. 1.change player game state and broadcast. 2.create dice point
 * randomly.
 * 
 * @author Dream
 * @date 2012-2-7
 * @version
 * 
 */
public class UserShakeAction extends AbstractDelayAction
{
    private GamePlayer player;
    
    public UserShakeAction(GamePlayer player,int delay)
    {
        super(delay);
        this.player=player;
    }

    @Override
    public void handle(BaseGame game)
    {

//    	System.out.println("UserShakeAction=================11==========" + player.getUserId());
        // action is executed only when game is not start.
        if ((game.getGameState() == GameState.Prepared || game.getGameState() == GameState.Created)
        		|| game.getGameState() == GameState.Beting)
        {
            // change user game state.
            Player p = game.getPlayerById(player.getUserId());
            
            //if player had shaked cellphone and player's state is [EndShaked],return.
            if(p==null || p.getPlayerState()>=PlayerState.EndShaked)
                return;

            
            p.setPlayerState(PlayerState.EndShaked);
            System.out.println("UserShakeAction=================22==========" + p.getPlayerID());
            // broadcast player game state.
            game.sendUpdatePlayerState();

            // return dice points to client.
            game.sendShakeGame(p);

            // check whether all player had shaken cellphone,if not ,sending message
            // check the game player ,if player is robot,add action to shake.
            if (!game.isAllComplete())
            {
            	game.sendNotShakeUserPrepare();
            	game.setKickPlayerTime(1000*5);//为配合客户端修改踢出玩家的时间,服务器和客户端有2秒的差距
            }
            else
            {
            	game.setKickPlayerTime(Integer.MAX_VALUE);
                //game.WaitTime(2 * 1000);
            }
        }
    }
}
