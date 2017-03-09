/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * the command of dealing with user entering room.
 * when userID in the packet does not equal -1,it's [follow me] logic.
 * 
 * @author shanfeng.cao
 * @date 2012-09-18
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.ROOM_LOCK, desc = "房间上锁，解锁")
public class UserSetLockRoomCmd extends AbstractUserCmd
{
	public String lockPwd = "";
	@Override
    public int execute(GamePlayer player, Packet packet)
    { 
		//0 表示无锁  1 表示上锁  2 不能上锁 
		int result = -1;
		
		int roomState = packet.getInt();
		BaseRoom room = player.getCurrentRoom();
		if(room != null && room.getHost() == player) {
//			room.setLock(roomState);
			//表示要上锁
			if(roomState == 1){
				
				if(room.getPlayerCount() <=1 )//房间人数少于1个时 不能上锁
				{
					result = 2;
				}else{
					result = 1;
					room.setPassword(BaseRoom.PWD_LOCK);
				}
				
			}else{
				result = 0;
				room.setPassword(BaseRoom.UN_LOCK);
			}
			
			Packet respose = new Packet(UserCmdOutType.ROOM_LOCK);
			respose.putInt(result);
			room.sendToAll(respose);
		}
		
		return 0;
    }
}
