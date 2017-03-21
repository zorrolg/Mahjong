package com.citywar.game.action;

import com.citywar.game.BaseGame;
import com.citywar.game.GamePlayer;

/**
 * when the game over,adds action to prepare new game
 * 1.update players' state.
 * 2.notify client game result.
 * 3.prepare next turn.
 * 
 * @author Dream
 * @date 2012-2-8
 * @version
 * 
 */
public class RobotSendChatAction extends AbstractDelayAction
{
    private GamePlayer robotPlayer;
    private String massge;

    public RobotSendChatAction(int delay, GamePlayer player, String massge)
    {
        super(delay);
        robotPlayer = player;
        this.massge = massge;
    }

    @Override
    public void handle(BaseGame game)
    {
    	robotPlayer.onGameSendChat(0,massge);
    }
    
    
}
