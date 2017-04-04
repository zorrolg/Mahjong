package com.citywar.game.action;

import java.util.List;

import com.citywar.bll.common.LanguageMgr;
import com.citywar.game.BaseGame;
import com.citywar.game.DiceGame;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.GameState;
import com.citywar.type.HallGameType;
import com.citywar.type.PlayerState;
import com.citywar.type.UserCmdOutType;
import com.citywar.util.ThreadSafeRandom;

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
public class StartPrepareAction extends AbstractDelayAction
{
    public StartPrepareAction(int delay)
    {
        super(delay);
    }

    @Override
    public void handle(BaseGame game)
    {
    	

    	if (game.getGameState() == GameState.GameOver)
        {
	    	game.setGameState(GameState.Prepared);
	        
	    	
	    	game.setKickPlayerTime(1000*5);//Integer.MAX_VALUE
	        
	        // check whether game player is offline,if player is offline then remove
	        BaseHall hall = HallMgr.getHallById(game.getHallId());
	    	BaseRoom room = game.getBaseRoom();
	        List<Player> tempPlayerList = game.getAllPlayers();
	        ThreadSafeRandom random = new ThreadSafeRandom();
	        for (Player p : tempPlayerList)
	        {
	            GamePlayer gamePlayer = p.getPlayerDetail();
	
	            // [Trustee] function is canceled.
	            if(room.getHallType().getHallType() == HallGameType.SOCIAL)
	            {
	            	boolean isRemove = false;
	            	if (p.getPlayerDetail().getIsRobotState())
		            {
	            		isRemove = true;
		            }
	            	
	            	if (!gamePlayer.getIsRobot() && !gamePlayer.isCoinsEnough())
		            {
	            		isRemove = true;
	            		String msg = LanguageMgr.getTranslation("CityWar.StayRoom.NoEnoughCoin");
	                	gamePlayer.getOut().sendRoomReturnMessage(false,msg);
		            }
	            	
	            	if(isRemove)
	            	{
	            		room.removePlayer(p.getPlayerDetail());
		                continue;
	            	}
	            }
	            
	
	            
	            
	            
	            
	            
	            // update player game state.
	            p.setPlayerState(PlayerState.Idle);	
	            Packet packet = new Packet(UserCmdOutType.GAME_OVER);
	            p.getPlayerDetail().sendTcp(packet);
	

	            
	            
	            
	            
	            
	            // if the player is robot, will shake after waiting 5 seconds.
	            if (p.getPlayerDetail().getIsRobot())
	            {
	            	if(hall.getHallType().getHallType() == HallGameType.SOCIAL)
	            	{	            		
	            			            	
	            	
		            	boolean isExitRoom = false;//机器人是否退出房间
		            	
		//            	if(AwardMgr.palyerCoinsLess(p.getPlayerDetail())){
		//            		isExitRoom = true;
		//            		gamePlayer.isCoinsEnough();//给机器人加钱
		//            	}
		//            	else 
		            	if(!gamePlayer.isCanStayRoom()){
		            		
		//	            		gamePlayer.setIsRobotSleep(true);
		            		isExitRoom = true;
		            	}
		            	else if (gamePlayer.isFullDrunk())
		                {
		                	isExitRoom = true;
		                }
		
		                // if the size of players have up to 4, then remove the robot
		                // player. 加上一定概率
		            	
		            	if (isExitRoom == false)
		            	{
		            		
		            		int countMax = hall.getHallType().getPlayerCountMax();
		            		int countMin = hall.getHallType().getPlayerCountMin();
		            		
			            	int probability = random.next(0, 50);	            		
		            		//System.out.println("StartPrepareAction=========================================" + probability);
		            		
		            		if(game instanceof DiceGame)
		                	{	            			
		            			if(game.getAllPlayers().size() == countMax || (game.getAllPlayers().size() > countMin && probability % 2 == 0))
			            		{
			            			isExitRoom = true;
			            		}
		                	}
		            		
		            			            		
		            	}
		            	
		            	
		                
		                if(isExitRoom){
		                	 p.setIsPlaying(false);
		                     room.removePlayer((GamePlayer) p.getPlayerDetail());
		                     continue;
		                }	                
	            	}
	            	
//	                int probabilityDrunk = random.next(0, 100);
//	                float drunkPercent = (float)gamePlayer.getDrunkLevel() / gamePlayer.getPlayerInfo().getDrunkLevelLimit();
//	            	if(gamePlayer.getIsRobot() && drunkPercent < 0.3 && probabilityDrunk < 8)
//	            	{
//	                	ItemTemplateInfo item = ItemMgr.findItemTemplate(PropType.WAKE_DRUG);                    	
//	                	if(item != null)
//	                	{
//	                		gamePlayer.addDrunkLevel(item.getPrar1());
//	                    	gamePlayer.updatePropUse(item.getTemplateId(), 1);
//	                	}
//	            	}
	            	
//	            	System.out.println("StartPrepareAction========" + gamePlayer.getUserId());
	            	
	            	
	            	
	                //room.getGame().AddAction(new UserShakeDicePointAction(p.getPlayerDetail(), 5 * 1000));
	                if (null != game) {
	                	UserShakeAction action = new UserShakeAction(p.getPlayerDetail(), random.next(1, 2) * 1000);
	                	game.AddAction(action);
	                }
	            }
	        }
	        
	        if(hall.getHallType().getHallType() == HallGameType.SOCIAL)
        	{
	        	if(game.getAllPlayers().size() < hall.getHallType().getPlayerCountMax())
	            {            
		        	int addMaxCount = hall.getHallType().getPlayerCountMax() - hall.getHallType().getPlayerCountMin() -  game.getAllPlayers().size();
		        	int addCount = random.next(1, addMaxCount);
		        	
//		        	System.out.println("StartPrepareAction＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝" + addCount + "========" + addCount);
		        	
		        	for(int i =0;i<addCount;i++)
		        		game.getBaseRoom().onStartAddRobot();
	            }
        	}
	        
	        
	        // notify client players' state.
	        if (null != game) {
	        	game.sendUpdatePlayerState();
	        	game.sendShakePrepare();
	        }
        }
    }
}
