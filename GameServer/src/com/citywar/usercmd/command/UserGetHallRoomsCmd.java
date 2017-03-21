package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.game.GamePlayer;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
/**
 * 一登陆进来加入大厅之前需要获取大厅的房间列表
 * 首先请求 UserGetHallRoomsCmd --> 之后请求 UserEnterHallCmd
 * @author jacky.zheng
 *
 */
@UserCmdAnnotation(code = UserCmdType.USER_ROOMS_LIST, desc = "获取当前大厅最新的房间列表")
public class UserGetHallRoomsCmd extends AbstractUserCmd {

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		String roomList = "";
		boolean isCanFirstEnterHall = false;
		int userMaxHallId = player.getPlayerInfo().getMaxHallId();
		
		Packet response = new Packet(UserCmdOutType.USER_ROOMS_LIST);
		BaseHall hall = player.getCurrentHall();
		//如果当前的用户没有进入大厅,则需要去找一个空闲的大厅让玩家进入
		if (null == hall) {
			//System.out.println("UserGetHallRoomsCmd----------低级版本的登录，玩家还没有进入大厅！！！");
			hall = HallMgr.getQiuckGameHall(player); //兼容低级版本
		}
		if (null != hall) { //能够获取到大厅
			hall.playerInHall(player);//加入大厅
			//获取到这个大厅所有存在
			List<Integer> list = HallMgr.getTheNewestRoomsList(hall.getHallId(), 12);
			
			if (null != list && list.size() > 0) {
				response.putInt(list.size());
				for (Integer i : list) {
					response.putInt(i);
					roomList = roomList + i + ",";
				}
			}
			HallTypeInfo hallType = hall.getHallType();
			if(null != hallType) {
				response.putInt(hallType.getHallTypeId());//类型ID
				response.putStr(hallType.getHallTypeName());//名字
				response.putStr(hallType.getNameImage());//名字URL
				response.putInt(hallType.getWager());//大厅的底注
				
				HallTypeInfo maxHall = HallMgr.getFirstEnterHall(player.getPlayerInfo().getCharmValve());
				
				
				if(maxHall.getHallTypeId()>1    //新手场不用提示                       
						&&  maxHall.getHallTypeId() > userMaxHallId ){//用户进过的最大的房间大于当前能进入的房间不用提示	
					
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
			player.getOut().sendTCP(response);
		}
		return 0;
	}
}
