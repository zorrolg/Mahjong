/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.io.IOException;

import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.citywar.ServerClient;
import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 玩家登录指令
 * 
 * @author shanfeng.cao
 * @date 2012-07-20
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.SYSTEM_USER_LOGIN, desc = "系统玩家请求登录处理")
public class SystemUserLoginCmd extends AbstractUserCmd {
	private static final Logger logger = Logger
			.getLogger(SystemUserLoginCmd.class.getName());

	public int excuteSession(Session session, Packet packet) {
		GamePlayer player = null;
		try {
			String account = packet.getStr();
			String userPwd = packet.getStr();
			//boolean isUpdate = packet.getBoolean();扩张密码

			if (account.isEmpty() || userPwd.isEmpty()) {
				sendLogin(session, (byte)1);
				return 0;
			}
			PlayerInfo info = PlayerBussiness.checkAccoutAndPwd(account, userPwd);
			if (info == null) {
				sendLogin(session, (byte)1);
				return 0;
			}
			player = new GamePlayer(info.getUserId());
			player.setPlayerInfo(info);
			player.setSession(session);
			session.getUserProperties().put(GamePlayer.class.toString(), player);
//			session.setAttribute(GamePlayer.class, player);
			ServerClient client = (ServerClient) session.getUserProperties().get(ServerClient.class.toString());
			client.setPlayer(player);
		} catch (Exception e) {
			logger.error("[ SystemUserLoginCmd : excuteSession ]", e);
//			session.close();
		}
		sendLogin(session, (byte)0);
		return 1;
	}
	
	private void sendLogin(Session session, byte result) {
		try {
			Packet pkg = new Packet(UserCmdOutType.SYSTEM_USER_LOGIN);
			pkg.putByte(result); // 1表示登陆失败
			if (session != null && session.isOpen())			
					session.getBasicRemote().sendText(pkg.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int execute(GamePlayer player, Packet packet) {
		return 0;
	}
}
