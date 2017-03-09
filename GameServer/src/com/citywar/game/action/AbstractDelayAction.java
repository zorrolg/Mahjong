package com.citywar.game.action;

import com.citywar.game.BaseGame;
import com.citywar.util.TickHelper;

public abstract class AbstractDelayAction implements IAction
{

    protected long tick;
    
    public AbstractDelayAction(int delay)
    {
        tick= TickHelper.GetTickCount() + delay;
    }
    
    @Override
    public void execute(BaseGame game, long tick)
    {
        if (this.tick<= tick)
        {
            handle(game);
        }
    }
    
    public abstract void handle(BaseGame game);

    @Override
    public boolean isFinished(BaseGame game, long tick)
    {
        return this.tick <= tick;
    }

}
