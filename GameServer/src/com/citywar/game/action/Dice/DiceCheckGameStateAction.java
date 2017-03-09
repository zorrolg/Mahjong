/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.game.action.Dice;

import com.citywar.game.BaseGame;
import com.citywar.game.DiceGame;
import com.citywar.game.action.AbstractDelayAction;

/**
 * 
 * @author Dream
 * @date 2011-12-15
 * @version
 * 
 */
public class DiceCheckGameStateAction extends AbstractDelayAction
{
    public DiceCheckGameStateAction(int delay)
    {
        super(delay);
    }

    @Override
    public void handle(BaseGame game)
    {
        DiceGame diceGame = null;
        if (game instanceof DiceGame)
            diceGame = (DiceGame) game;

        if (diceGame != null)
        {
            switch (diceGame.getGameState())
            {
                case Created:
                    break;

                case Prepared:
                    if (diceGame.isAllComplete())
                        diceGame.start();
                    else
                        diceGame.checkComplete(super.tick);
                    break;

                case Playing:
                    if (diceGame.canGameOver())
                        diceGame.gameOver(false);
                    else
                        diceGame.nextTurn();
                    break;

                case GameOver:
                    if (diceGame.getCurrentActionCount() == 1)
                        diceGame.gameOver(false);
                    break;

                case Stopped:
                    diceGame.stop();
                    break;
                                        
                default:                    	
                	break;
            }
        }
    }
}
