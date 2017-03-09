/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.game.action;

import com.citywar.game.BaseGame;

/**
 * 游戏中玩家的动作
 * @author Dream
 * @date 2011-12-21
 * @version 
 *
 */
public interface IAction
{
    public void execute(BaseGame game, long tick);

    public boolean isFinished(BaseGame game, long tick);
}
