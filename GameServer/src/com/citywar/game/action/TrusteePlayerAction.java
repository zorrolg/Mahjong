package com.citywar.game.action;

import com.citywar.game.BaseGame;
import com.citywar.gameobjects.Player;

public class TrusteePlayerAction extends AbstractDelayAction
{
    private Player trusteePlayer;

    public TrusteePlayerAction(int delay, Player player)
    {
        super(delay);
        trusteePlayer = player;
    }

    @Override
    public void handle(BaseGame game)
    {
        trusteePlayer.robotTurn();
    }
}
