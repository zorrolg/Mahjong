/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import org.apache.log4j.Logger;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 查看快喝醉的用户
 * 
 * @author shanfeng.cao
 * @date 2011-12-20
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.USER_DRUNK_LOCK, desc = "查看快喝醉的用户")
public class UserDrunklockCmd extends AbstractUserCmd {
	
	private static final Logger logger = Logger.getLogger(UserDrunklockCmd.class.getName());

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		return 0;
	}

}

