package com.citywar.room.action;



import com.citywar.game.BaseGame;
import com.citywar.game.GamePlayer;
import com.citywar.room.BaseRoom;
import com.citywar.type.GameState;

public class UpdatePlayerStateAction extends AbstractRoomAction
{
    BaseRoom room;
    GamePlayer player;
    boolean isRobotState;
//    private static Logger logger = Logger.getLogger(UpdatePlayerStateAction.class.getName());

    public UpdatePlayerStateAction(BaseRoom room, GamePlayer player, boolean isRobotState)
    {
        this.room = room;
        this.player = player;
        this.isRobotState = isRobotState;
    }

    @Override
    public void execute()
    {
    	
        if(room==null) {
//            player.quit();
            return ;
        }
        
        
        BaseGame game = room.getGame();
        
        if(game != null && player != null)
        {
        	if(player.getIsRobotState() != this.isRobotState)
        	{
            	game.changePlayerRobotState(player.getUserId(), this.isRobotState);
            	game.SendPlayerRobotState(player, this.isRobotState);
            	
                
                if ((game.getGameState() == GameState.Playing || game.getGameState() == GameState.Beting))
                {
                	if(this.isRobotState && game.getCurrentTurnPlayer() != null 
                			&& game.getCurrentTurnPlayer().getPlayerID() == player.getUserId())
                		game.checkState(0);
                }
        	}

        }
        
        
        
//        BaseGame game = room.getGame();
//        if (game == null || (game.getGameState() != GameState.Playing && game.getGameState() != GameState.Beting))
//        {
//        	StringBuffer sb = new StringBuffer();
//        	logger.error(sb.append("[UpdatePlayerStateAction - execute()] : remove player  ")
//        			.append(player.getPlayerInfo().getUserName()));
//        	//logger.debug("[BaseRoom - updatePosUnsafe()] : remove player [(" + player.getPlayerInfo().getUserId() + ") " + player.getPlayerInfo().getUserName() + "] in Room [" + room + "]");
//            room.removePlayer(player);
//            player.quit();
//            return ;
//        }



    }
    
}
