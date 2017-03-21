/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.manager.SystemContorlMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 系统玩家控制请求处理
 * 
 * @author shanfeng.cao
 * @date 2012-07-20
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.SYSTEM_USER_CONTROL, desc = "系统玩家控制请求处理")
public class SystemContorl extends AbstractUserCmd
{
	private int userId;
	private int code;
	private GamePlayer player;
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	this.player = player;
    	userId = packet.getInt();
    	code = packet.getInt();
    	executeContorl(packet);
    	//System.out.println(userId + "---" + code);
		return 0;
    }
    
    public void executeContorl(Packet packet) {
    	Packet result = new Packet(UserCmdType.SYSTEM_USER_CONTROL);
    	result.putInt(userId);
    	result.putInt(code);
    	switch(code) {
    	case ContorlType.CHECK_WORL_DETAIL:
    		checkWorl(result);
    		break ;
    	case ContorlType.UPDATE_PLAYER:
    		updatePlayer(result, packet);
    		break ;
    	case ContorlType.CHECK_HALL_ROOMS:
    		checkHallRooms(result, packet);
    		break ;
    	case ContorlType.REMOVE_ROOM_PLAYERS:
    		removeRoomPlayers(result, packet);
    		break ;
    	case ContorlType.UPDATE_SYSTEM_STATE:
    		updateSystemState(result, packet);
    		break ;
    	}
    }
    
    private void updateSystemState(Packet result, Packet packet) {
    	SystemContorlMgr sytem = new SystemContorlMgr(player);
		sytem.updateSystemState(result, packet);
		player.sendTcp(result);
	}

	private void removeRoomPlayers(Packet result, Packet packet) {
    	SystemContorlMgr sytem = new SystemContorlMgr(player);
		sytem.removeRoomPlayers(result, packet);
		player.sendTcp(result);
	}

	private void checkHallRooms(Packet result, Packet packet) {
    	SystemContorlMgr sytem = new SystemContorlMgr(player);
		sytem.checkHallRooms(result, packet);
		player.sendTcp(result);
	}

	public void checkWorl(Packet result) {
		SystemContorlMgr sytem = new SystemContorlMgr(player);
		sytem.checkWorl(result);
		player.sendTcp(result);
    }
    
    public void updatePlayer(Packet result, Packet packet) {
		SystemContorlMgr sytem = new SystemContorlMgr(player);
		sytem.updatePlayer(result, packet);
		player.sendTcp(result);
    }
    
	class ContorlType {
		
	    //----------------系统控制相关 begin ----------------
	    /**
	     * 查看所有玩家详细信息
	     */
	    public final static short CHECK_WORL_DETAIL = 0x01;
	    /**
	     * 更新玩家详细信息
	     */
	    public final static short UPDATE_PLAYER = 0x02;
	    /**
	     * 查看大厅房间详细信息
	     */
	    public final static short CHECK_HALL_ROOMS = 0x03;
	    /**
	     * 移除房间所有的玩家
	     */
	    public final static short REMOVE_ROOM_PLAYERS = 0x04;
	    /**
	     * 移除房间所有的玩家
	     */
	    public final static short UPDATE_SYSTEM_STATE = 0x05;
	    
	    
	    //----------------系统控制相关 end ----------------
	}
}
