package com.citywar.usercmd.command;

import java.util.ArrayList;
import java.util.List;

import com.citywar.dice.entity.PlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.manager.RobRoomMgr;
import com.citywar.room.BaseRoom;
import com.citywar.room.RobRoom;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.HeadPicUtil;

@UserCmdAnnotation(code = UserCmdType.USER_ENTER_HALL, desc = "玩家进入大厅")
public class UserEnterHallCmd extends AbstractUserCmd{

//	@Override
//	public int execute(GamePlayer player, Packet packet) {
//		//请求参数包括大厅的id,请求房间的页数
//		int hallId = packet.getInt();
//		int pageId = packet.getByte();
//		if (hallId < 0 || pageId < 0) {
//			return 0;
//		}
//		BaseHall hall = HallMgr.getHallById(hallId);
//		
//		if (null != hall) {
//			hall.addIdlePlayer(player);
//			player.setCurrenetHall(hall);
//			Packet response = new Packet(UserCmdOutType.USER_ENTER_HALL);
//			int pageRoomSize = 4;
//			BaseRoom room = null;
//			response.putByte((byte)pageId);//页号
//			for (int i = 1; i <= pageRoomSize; i++) {
//				room = hall.getRoomById((pageId -1) * pageRoomSize + i);
//				if (null == room) {
//					continue;
//				}
//				response.putBoolean(room.isNeedPassword());//有没有密码
//				List<GamePlayer> plaers = room.getPlayers();//玩家列表
//				PlayerInfo playerInfo = null;
//				response.putByte((byte)plaers.size());//房间玩家数
//				for(GamePlayer plaer : plaers) {
//					playerInfo = plaer.getPlayerInfo();
//					response.putInt(player.getUserId());//玩家ID
//					response.putByte((byte)playerInfo.getSex());//玩家性别
//					String str = "";
//					if (!playerInfo.getPicPath().isEmpty()) {
//						str = getSmallPicPath(playerInfo.getPicPath());
//					}
//					response.putStr(str);//玩家图片
//				}
//			}//构造返回数据
//			player.getOut().sendTCP(response);
//		}
//		return 0;
//	}
	
	/**
	 * 进入大厅的方式改为随机28个房间给用户,然后用户通过每次请求四个房间ID来返回房间数据
	 */
	@Override
	public int execute(GamePlayer player, Packet packet) {
		List<BaseRoom> list = new ArrayList<BaseRoom>();
		String pageRoomList = "";
		BaseHall hall = player.getCurrentHall();
		if(null == hall) {
			//System.out.println("-----------------低级版本的登录，玩家还没有进入大厅！！！");
			hall = HallMgr.getQiuckGameHall(player);
		}
		//读取用户请求的房间数量
		int roomNum = packet.getInt();
		for (int i = 0; i < roomNum; i++) {
			int roomId = packet.getInt();
			pageRoomList = pageRoomList + roomId + ","; 
			BaseRoom room = hall.getRoomById(roomId);
			if (null != room) {
				list.add(room);
				if(room instanceof RobRoom){
					RobRoomMgr.refreshPic((RobRoom)room);
				}
			}
		}
		//System.out.println("pageRoomList=====" + pageRoomList);
		//构造返回数据
		Packet response = new Packet(UserCmdOutType.USER_ENTER_HALL);
		response.putInt(list.size());
		for (BaseRoom room : list) 
		{
			response.putInt(room.getRoomId());
			response.putBoolean(room.isNeedPassword());//有没有密码
			List<GamePlayer> players = room.getPlayers();//玩家列表
			response.putByte((byte)players.size());//房间玩家数
			for (GamePlayer temp : players) 
			{
				PlayerInfo playerInfo = temp.getPlayerInfo();
				response.putInt(player.getUserId());//玩家ID
				response.putByte((byte)playerInfo.getSex());//玩家性别
				String str = "";
				if (!playerInfo.getPicPath().isEmpty()) {
					if(playerInfo.getIsRobot() == 2)
					{
						str = HeadPicUtil.getRealRoomRobPicPath(playerInfo.getPicPath());;
					}else{
						str = HeadPicUtil.getSmallPicPath(playerInfo.getRealPicPath());
					}
				}
				response.putStr(str);//玩家图片
				response.putByte((byte)temp.getCurrentRoomPos());//玩家当前的房间位置
			}
			
			
//			System.out.println("pageRoomList=====" + room.getRoomId() + "=====" + players.size());
			
		}
		player.getOut().sendTCP(response);
		
		
		return 0;
	}
	
}
