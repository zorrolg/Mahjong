/**
t *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.room;



import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.game.GamePlayer;

/**
 * 游戏基本房间信息
 * 
 * @author Dream
 * @date 2011-12-19
 * @version
 * 
 */
public class RobRoom extends BaseRoom {
	
//   private static final Logger logger = Logger.getLogger(RobRoom.class.getName());
	
	public RobRoom(int roomId, int hallId, HallTypeInfo hallType) {
		super(roomId, hallId, hallType);
		setIsUsing(true);
	}
	@Override
	public boolean addNpcPlayer(GamePlayer player){
		int index = -1;
		synchronized (players) {
			// 寻找空位加入
			for (int i = 0; i < initPlayerCount; i++) {
				if ((players[i] == null && placesState[i] == -1)) {
					players[i] = player;
					placesState[i] = player.getUserId();
					playerCount++;
					index = i;
					break;
				}
			}
		}
		 if (index != -1)
         {
	        // 设置房间信息
         	BaseRoom curRoom = player.getCurrentRoom();
 			if (curRoom != null) {//不管怎么样 有房间就先从房间移除
 				curRoom.removeRobotPlayer(player);
 			}
	            player.setCurrentRoom(this);
	            player.setCurrentRoomPos(index);
         }
		return index > 0;
	}
}
