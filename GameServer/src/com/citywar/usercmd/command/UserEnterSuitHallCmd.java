package com.citywar.usercmd.command;

import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.game.GamePlayer;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.USER_ENTER_SUIT_HALL, desc = "玩家进入适合的大厅")
public class UserEnterSuitHallCmd extends AbstractUserCmd{
	
	/**
	 * 进入大厅的方式改为随机28个房间给用户,然后用户通过每次请求四个房间ID来返回房间数据
	 */
	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		
		int hallTypeId = packet.getInt();
		
		
		boolean isCanFirstEnterHall = false;
		int userMaxHallId = player.getPlayerInfo().getMaxHallId();
		
		Packet response = new Packet(UserCmdOutType.USER_ROOMS_LIST);
		BaseHall hall = HallMgr.getQiuckGameHall(hallTypeId, player);
		if (null != hall) { //能够获取到大厅
			hall.playerInHall(player);//加入大厅
//			player.setCurrenetHallGameType(hall.getHallType().getHallType());
			
			//获取到这个大厅所有存在
//			List<Integer> list = HallMgr.getTheNewestRoomsList(hall.getHallId(), 12);
//			String roomList = "";
//			if (null != list && list.size() > 0) {
//				response.putInt(list.size());
//				for (Integer i : list) {
//					response.putInt(i);
//					roomList = roomList + i + ",";
//				}
//			}
			HallTypeInfo hallType = hall.getHallType();
			if(null != hallType) {
				response.putInt(hallType.getHallTypeId());//类型ID
				response.putStr(hallType.getHallTypeName());//名字
				response.putStr(hallType.getNameImage());//名字URL
				response.putInt(hallType.getWager());//大厅的底注
				
				HallTypeInfo maxHall = HallMgr.getFirstEnterHall(player.getPlayerInfo().getCharmValve());
				if(maxHall.getHallTypeId()>1    //新手场不用提示                       
						&&  maxHall.getHallTypeId() >= userMaxHallId ){//用户进过的最大的房间大于当前能进入的房间不用提示
					
					isCanFirstEnterHall = true;
					userMaxHallId = maxHall.getHallTypeId();
				}
				
				/**==============下面三个字段是V2.2版本新增 =======================*/
				response.putBoolean(isCanFirstEnterHall); //首次开启大厅的 ID; 
				response.putInt((byte)maxHall.getHallTypeId()); //首次开启大厅的 ID; 
				response.putInt(maxHall.getFirstCoins());  //首次开启大厅需要多少钱
				
				//如果有提示 则更新大厅
				if(isCanFirstEnterHall){
					player.getPlayerInfo().setMaxHallId(userMaxHallId);
//					PlayerBussiness.updateAll(player.getUserId(), player.getPlayerInfo());
				}
			}
			//System.out.println("roomList:====" + roomList);
			player.getOut().sendTCP(response);
		}
		return 0;
	}
	
}
