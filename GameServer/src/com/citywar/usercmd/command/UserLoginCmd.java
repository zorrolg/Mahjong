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
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 玩家登录指令
 * 
 * @author tracy
 * @date 2011-12-16
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.LOGIN, desc = "玩家请求登录处理")
public class UserLoginCmd extends AbstractUserCmd
{
    private static final Logger logger = Logger.getLogger(UserLoginCmd.class.getName());

    public int excuteSession(Session session, Packet packet)
    {
        try
        {
        	      	
        	
        	GamePlayer player = null;
           
                        
            String tempString = packet.getStr();
            String[] temp = tempString.split(",",-1);
            if (temp.length >= 5 )
            {
                String account = temp[0];
                String userPwd = temp[1];
                String isUpdate = temp[2];
                boolean isFeeVesion = temp[3].equals("1") ? true:false;
                String machineryId = temp[4].replace("<", "").replace(">", "").replace(" ", "");

                String deviceType = "";
                if(temp.length >= 6)
                	deviceType = temp[5];
                
                
                
                if (account.isEmpty() || userPwd.isEmpty()
                		||account.indexOf("%") != -1 || userPwd.indexOf("%") != -1) {
                	sendLoginFail(session,2);
                	return 0;
                }

                PlayerInfo info = PlayerBussiness.checkAccoutAndPwd(account, userPwd);

                if (info == null) // Login Fail, return
                {
                    sendLoginFail(session,3);
                    return 0;
                }
            	info.setMachineryId(machineryId);
            	
                GamePlayer currentPlayer = null;
                currentPlayer = WorldMgr.getPlayerByID(info.getUserId());                                                                                             
                if (currentPlayer == null)// 全局用户集合没有这个用户
                {
                	player = new GamePlayer(info.getUserId());
                	player.setPlayerInfo(info);
                	player.setSession(session);
                	player.setFeeVesion(isFeeVesion);
                	player.getPlayerInfo().setMachineType(deviceType);
//                    session.setAttribute(GamePlayer.class, player);
                    session.getUserProperties().put(GamePlayer.class.toString(), player);
                    
                    // update session key.

                    ServerClient client = (ServerClient) session.getUserProperties().get(ServerClient.class.toString());
                    client.setPlayer(player);
                    
                    
                    player.Login(false);
                }
                else // 老用户登录
                {
                	
//                	currentPlayer.sendMessage(MessageType.ALERT, "您的账号在其他的地方登录");                	
                	currentPlayer.getSession().close();
                	  
                	
                	
                	
                	session.getUserProperties().put(GamePlayer.class.toString(), currentPlayer);
                    currentPlayer.setSession(session);
                    currentPlayer.setFeeVesion(isFeeVesion); 
                    currentPlayer.getPlayerInfo().setMachineType(deviceType);
                    ServerClient client = (ServerClient) session.getUserProperties().get(ServerClient.class.toString());
                    client.setPlayer(currentPlayer);
                    
                    
                    currentPlayer.Login(true);
                    // player enter the playing room.
                    currentPlayer.sendEnterTrusteedRoom();
                }
            }
            
        }
        catch (Exception e)
        {
            logger.error("[ UserLoginCmd : excuteSession ]", e);
        }
        return 1;
    }

    
    


    private void sendLoginFail(Session session,int type)
    {
    	try {
	        Packet pkg = new Packet(UserCmdOutType.LOGIN_ACK);
	        pkg.putByte((byte) type); // 0表示登陆失败
	        if (session != null && session.isOpen())			
					session.getBasicRemote().sendText(pkg.getJsonDate());
        
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
    }

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        return 0;
    }
}
