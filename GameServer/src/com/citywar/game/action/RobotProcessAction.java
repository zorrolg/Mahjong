package com.citywar.game.action;


import com.citywar.game.BaseGame;
import com.citywar.game.DiceGame;
import com.citywar.gameobjects.Player;
import com.citywar.manager.RobotChatMgr;
import com.citywar.type.GameState;
import com.citywar.util.ThreadSafeRandom;



public class RobotProcessAction extends AbstractDelayAction
{
	
	
	private static ThreadSafeRandom random = new ThreadSafeRandom();
	
	
	private Player robotPlayer;
	
	
	
    
    public RobotProcessAction(int delay, Player player)
    {
        super(delay);
        robotPlayer = player;
    }

    @Override
    public void handle(BaseGame game)
    {
    	    	
    	
    	
    	if(game instanceof DiceGame)
    	{
    		
    		if(null != robotPlayer && null != robotPlayer.getPlayerDetail()) {
            	//机器人聊天
        		RobotChatMgr.playerOnGameSendChat(robotPlayer.getPlayerDetail(), 
            			RobotChatMgr.GAME_OVER_WORD, 11);//类型为10
        	}
        	
            if (game.getGameState() != GameState.Playing)
                return;
            // 机器人开局叫情况
            if (game.getTurnIndex() == 1)
            {
                robotPlayer.startCall();
            }
            else
            {
                // 如果符合抢开条件(目前不可能出现)
                if (game.getTurnQueue().size() > 2)
                {
                    // 是否抢开
                    robotPlayer.directlyOpen();
                }
                
                robotPlayer.robotTurn();
            }
            
            game.checkState(0);
                        
    	}
    	
    	
    	
    	
    	
    	
    	
    	   	
    }
    
}
