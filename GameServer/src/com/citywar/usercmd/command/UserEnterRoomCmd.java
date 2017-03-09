/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.util.ArrayList;
import java.util.List;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.RoomMgr;
import com.citywar.socket.Packet;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * the command of dealing with user entering room.
 * when userID in the packet does not equal -1,it's [follow me] logic.
 * 
 * @author Dream
 * @date 2011-12-19
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.USER_ENTER_ROOM, desc = "玩家进入房间处理/玩家跟随")
public class UserEnterRoomCmd extends AbstractUserCmd
{
	@Override
    public int execute(GamePlayer player, Packet packet)
    {
		byte cmdType = packet.getByte();
		
		
		// 由于现在玩家进入房间的方式比较多--为了扩展方便可以加入命令类型来判断
		int roomId = -1;		
		String password = "";
		
		
		int followPlayerId = -1;
		List<Integer> paraList = new ArrayList<Integer>();
		
		switch (cmdType) {
			case 0://表示创建房间
				password = packet.getStr();
				break;
			case 1://表示加入指定roomId的房间
				roomId = packet.getInt();
				password = packet.getStr();
				break;
			case 2://表示跟随用户(好友)加入其所在房间
				followPlayerId = packet.getInt();
				player.isFinishTask(TaskConditionType.InviteGame, 0, 0);//完成6号任务。邀请好友
				break;
			case 3://快速加入房间
				break;
			case 4://快速加入快醉的玩家房间
				break;
			case 5://快速加入当前大厅房间
				break;
				
			case 10://快速加入当前大厅房间
								
				int count = packet.getInt();
				for(int i = 0;i<count;i++)
					paraList.add(packet.getInt());
				
				break;				
			case 11://快速加入当前大厅房间
				
				roomId = packet.getInt();
				password = packet.getStr();
				
				break;
			default:
				return 0;
		}
        RoomMgr.enterRoom(player, roomId, cmdType, password, followPlayerId, paraList);
        return 0;
    }
}
