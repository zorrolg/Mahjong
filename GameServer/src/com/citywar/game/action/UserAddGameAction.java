package com.citywar.game.action;

import com.citywar.game.BaseGame;
import com.citywar.game.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.type.GameState;

/**
 * user is added to game. 
 * 1.send all players' state. 
 * 2.if the game is playing when user is added to game,send the game message to new added player.
 * 3.check the player count,kick off the robot. 
 * @author Dream
 * @date 2012-2-9
 * @version
 * 
 */
public class UserAddGameAction implements IAction
{
    private GamePlayer player;

    public UserAddGameAction(GamePlayer player)
    {
        this.player = player;
    }

    @Override
    public void execute(BaseGame game, long tick)
    {
        // player add to the game.
        Player gp = new Player(player, game);
        game.addGamePlayer(player.getUserId(), gp);

        // update player's game state.
        game.sendUpdatePlayerState();
        
        // notify players to shake smart phone.
        game.sendShakePrepare();
        

        if (game.getGameState() == GameState.Prepared) {
        	 game.setKickPlayerTime(1000*5);//如果一个新用户进来了要重置踢人的标志时间
        } else if (game.getGameState() == GameState.Playing) {
        	 game.setKickPlayerTime(Integer.MAX_VALUE);
        }
        
        // if game is playing,new player will obtain game message.
        if (game.getCurrentTurnPlayer() != null
                && game.getGameState() == GameState.Playing)
            game.sendGameNextTurn(game.getCurrentTurnPlayer(),game, gp);
    }

    @Override
    public boolean isFinished(BaseGame game, long tick)
    {
        return true;
    }

}
