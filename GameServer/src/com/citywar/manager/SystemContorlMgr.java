/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import java.util.List;

import com.citywar.game.GamePlayer;
import com.citywar.hall.BaseHall;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;

/**
 * 控制管理类
 * 
 * @author shanfeng.cao
 * @date 2012-07-10
 * @version
 * 
 */
public class SystemContorlMgr
{

	/** 系统用户的ID*/
	private static int SYSTEM_PLAYER_ID = 6;
	/** 是否为系统用户*/
	private boolean isSystem = false;
	
	public SystemContorlMgr(GamePlayer gamePlayer) {
		if(null == gamePlayer) {
			return ;
		} else if(SYSTEM_PLAYER_ID == gamePlayer.getUserId()) {
			isSystem = true;
		}
	}

	public List<GamePlayer> getWorldAllPlayers() {
		return WorldMgr.getAllPlayers();
	}

	public void updatePlayer(Packet result, Packet packet) {
		if( ! isSystem) {
			return ;
		}
		int userId = packet.getInt();
		GamePlayer player = WorldMgr.getPlayerByID(userId);
		if(player != null && player.getPlayerInfo() != null) {
			player.addCoins(packet.getInt());
			player.addGpDirect(packet.getInt());
//			player.getPlayerInfo().setCoins(packet.getInt());
//			player.getPlayerInfo().setGp(packet.getInt());
			player.setPlaying(packet.getBoolean());
		}
	}

	public void checkWorl(Packet result) {
		if( ! isSystem) {
			return ;
		}
		List<GamePlayer> players = WorldMgr.getAllPlayers();
		if(null == players || players.size() == 0) {
			result.putInt(0);
		} else {
			result.putInt(players.size());
			for(GamePlayer player : players) {
				result.putInt(player.getUserId());
				result.putStr(player.getPlayerInfo().getUserName());
				BaseHall currentHall = player.getCurrentHall();
				result.putInt(currentHall == null ? 0 : currentHall.getHallId());
				result.putInt(player.getPlayerInfo().getCoins());
				result.putInt(player.getPlayerInfo().getGp());
				BaseRoom currentRoom = player.getCurrentRoom();
				result.putInt(currentRoom == null ? 0 : currentRoom.getRoomId());
				result.putInt(player.getCurrentRoomPos());
				result.putBoolean(player.getIsPlaying());
			}
		}
	}

	public void checkHallRooms(Packet result, Packet packet) {
		if( ! isSystem) {
			return ;
		}
		int hallId = packet.getInt();
		BaseHall hall = HallMgr.getHallById(hallId);
		BaseRoom rooms[] = hall.getALLRooms();
		List<GamePlayer> list = null;
		int[] placesState = null;
		result.putInt(hallId);
		if(null != rooms) {
			result.putInt(rooms.length);
			for(int i=0;i<rooms.length;i++) {
				result.putInt( rooms[i].getRoomId());
				result.putInt( rooms[i].getPlayerCount());
				list = rooms[i].getPlayers();
				result.putInt(list.size());
				for(GamePlayer player : list) {
					result.putInt(player.getUserId());
					result.putStr(player.getPlayerInfo().getUserName());
				}
				placesState = rooms[i].getPlacesState();
				result.putInt( placesState.length);
				for(int j=0;j<placesState.length;j++) {
					result.putInt(placesState[j]);
				}
			}
		}
	}

	public void removeRoomPlayers(Packet result, Packet packet) {
		int hallId = packet.getInt();
		int roomId = packet.getInt();
		HallMgr.removeRoomPlayers(hallId, roomId);
	}

	public void updateSystemState(Packet result, Packet packet) {
		if( ! isSystem) {
			return ;
		}
		boolean restartShop = packet.getBoolean();
		boolean restartItem = packet.getBoolean();
		boolean restartLeve = packet.getBoolean();
		boolean restartTask = packet.getBoolean();
		boolean isStartAntiAddiction = packet.getBoolean();
		boolean isNeedReal = packet.getBoolean();
		boolean isStratLoginGift = packet.getBoolean();
		if(restartShop) {
			ShopMgr.reload();
		}
		if(restartItem) {
			ItemMgr.reload();
		}
		if(restartLeve) {
			LevelMgr.reload();
		}
		if(restartTask) {
			TaskMgr.reload();
		}
		if(isStartAntiAddiction) {
			AntiAddictionMgr.stratAntiAddiction();
		} else {
			AntiAddictionMgr.stopAntiAddiction();
		}
		if(isNeedReal) {
			AntiAddictionMgr.stratRealInformation();
		} else {
			AntiAddictionMgr.stopRealInformation();
		}
		if(isStratLoginGift) {
			GiftBagMgr.stratLoginGift();
		} else {
			GiftBagMgr.stopLoginGift();
		}
	}
}
