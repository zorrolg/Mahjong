package com.citywar.room.action;

import com.citywar.game.GamePlayer;
import com.citywar.game.action.UserShakeAction;
import com.citywar.gameobjects.Player;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.room.BaseRoom;
import com.citywar.util.ThreadSafeRandom;

public class RobotEnterRoomAction extends AbstractRoomAction
{
    private BaseRoom room;
    
    private Player robot;
    
    public RobotEnterRoomAction(BaseRoom room)
    {
        this.room = room;
    }

    public RobotEnterRoomAction(BaseRoom room, Player robot)
    {
    	this.room = room;
        this.robot = robot;
    }
    
    @Override
    public void execute()
    {
    	
        if (room != null && room.getPlayers().size() < this.room.getHallType().getPlayerCountMax())//room != null && room.getGame() != null && room.getPlayers().size() == 1
        {
        	Player player = null;
        	if (null == robot) {
        		player = RobotMgr.getRobot(room.getHallId());//TODO 多大厅
        	} else {
        		player = robot;
        	}
            if (player == null)
                return;
        	if(null == player.getPlayerDetail()) {
	       		return;
	       	}
            int hallId = room.getHallId();//机器人设置大厅
            BaseHall hall = HallMgr.getHallById(hallId);
            boolean canInToHall = false;
            if(null != hall) {
            	canInToHall = hall.playerInHall(player.getPlayerDetail());//机器人加入大厅
            } else {
            	//System.out.println("机器人加入大厅为NULL----");
            }
            if( ! canInToHall) {
            	//System.out.println("机器人不满足进入大厅的条件。。");
            	return ;//机器人不能进入大厅
            }
            //先要才看机器人是否能够进入大厅
	   		BaseRoom curRoom = player.getPlayerDetail().getCurrentRoom();
	   		if (curRoom != null) {//不管怎么样 有房间就先从房间移除
	   			curRoom.removeRobotPlayer(player.getPlayerDetail());
	   		}
        	if(room.getGame() == null) {
        		room.createGame();
        		//System.out.println("机器人加入游戏的时候创建了游戏对象BaseGame==id=" + room.getGame().getId());
        	}
            player.setIsPlaying(true);
            // add RobotPlayer to the room.
            room.addNpcPlayer((GamePlayer) player.getPlayerDetail());
            // add RobotPlayer to the game.
            room.getGame().addGamePlayer(player.getPlayerID(),player);

            player.setGame(room.getGame());
            
            player.getPlayerDetail().setCurrentRoom(room);
            // update player's game state.
            room.getGame().sendUpdatePlayerState();

  
            
            // notify players to shake smart phone.
            room.getGame().sendShakePrepare();
        	ThreadSafeRandom random = new ThreadSafeRandom();
            room.getGame().AddAction(new UserShakeAction(player.getPlayerDetail(), random.next(1, 3) * 1000));//csf
        }
    }
}
