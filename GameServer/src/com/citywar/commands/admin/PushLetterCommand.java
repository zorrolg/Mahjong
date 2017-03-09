/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.commands.admin;

import java.sql.Timestamp;
import java.util.List;

import com.citywar.commands.AbstractCommandHandle;
import com.citywar.commands.ConsoleCmdAnnotation;
import com.citywar.commands.PrivLevel;
import com.citywar.dice.entity.UserLetter;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.ManagerClient;
import com.citywar.manager.WorldMgr;
import com.citywar.util.CommonUtil;

/**
 * @author tracy
 * @date 2012-1-16
 * @version
 * 
 */
@ConsoleCmdAnnotation(cmdString = "&push", description = "   服务器信息大全.", level = PrivLevel.Player, usage = {
        "       /push -alltrumpet [msg]:                    		列举所有正在游戏的 room 对象",
        "       /push -allletter [msg]:                    			列举所有正在游戏的 room 对象",
        "       /push -piletter [userid] [msg]:                  	列举指定 userid 的玩家信息"})
public class PushLetterCommand extends AbstractCommandHandle
{

    @Override
    public boolean onCommand(ManagerClient client, List<String> args)
    {

        if (args.size() > 1)
        {
            if ("-alltrumpet".equalsIgnoreCase(args.get(1)))
            {
            	
            	String strMsg = CommonUtil.isNullOrEmpty(args.get(2)) ? "-1": args.get(2);
                // 列出正在使用的房间信息

                displayMessage(client, "-------------------------------");
                WorldMgr.sendSystemTrumpet(strMsg);
                displayMessage(client, "-------------------------------");
                
            }
            if ("-allletter".equalsIgnoreCase(args.get(1)))
            {
            	
            	String strMsg = CommonUtil.isNullOrEmpty(args.get(2)) ? "-1": args.get(2);
            	
            	
            	
            	
            	
                // 列出正在使用的房间信息
            	displayMessage(client, "player list:");
                displayMessage(client, "-------------------------------");
                List<GamePlayer> ps = WorldMgr.getAllPlayers();
                int playerSize = ps.size();
                for (GamePlayer player : ps)
                {
                	
                	UserLetter newLetter = new UserLetter();
                	newLetter.setSenderId(2000);
                	newLetter.setSenderVip(0);
                	newLetter.setSenderName("");
                	newLetter.setSenderPic("");
                	newLetter.setReceiverId(player.getUserId());
                	newLetter.setReceiverName(player.getPlayerInfo().getUserName());
                	newLetter.setContent(strMsg);
                	newLetter.setType(UserLetter.SYSTEM_LETTER);                
                	newLetter.setSendTime(new Timestamp(System.currentTimeMillis()));
                	
                	if(player != null) //私信接收者在线 
            		{                		
                		player.getOut().sendOnLineLetter(newLetter);        			
            			//发送者和接收者 不处于即时聊天状态时  作为未读私信放入内存
            			if(player.getMsgBox().getLetterUserId() != newLetter.getSenderId())
            			{
            				player.getMsgBox().putLetter(newLetter);
            			}
            		}
            		
                }
                displayMessage(client, "-------------------------------");
                displayMessage(client, String.format("total:%s", playerSize+""));
                
            }
            else if ("-pitrumpet".equalsIgnoreCase(args.get(1)))
            {
                // 踢出指定玩家
                int userId = Integer.parseInt(CommonUtil.isNullOrEmpty(args.get(2)) ? "-1" : args.get(2));
                String strMsg = CommonUtil.isNullOrEmpty(args.get(3)) ? "-1": args.get(3);
                
                GamePlayer player = WorldMgr.getPlayerByID(userId);
            	if(player != null) //私信接收者在线 
        		{
            		UserLetter newLetter = new UserLetter();
                	newLetter.setSenderId(2000);
                	newLetter.setSenderName("");
                	newLetter.setSenderPic("");
                	newLetter.setReceiverId(player.getUserId());
                	newLetter.setReceiverName(player.getPlayerInfo().getUserName());
                	newLetter.setContent(strMsg);
                	newLetter.setType(UserLetter.SYSTEM_LETTER);                        
                	newLetter.setSendTime(new Timestamp(System.currentTimeMillis()));
                	
            		player.getOut().sendOnLineLetter(newLetter);        			
        			//发送者和接收者 不处于即时聊天状态时  作为未读私信放入内存
        			if(player.getMsgBox().getLetterUserId() != newLetter.getSenderId())
        			{
        				player.getMsgBox().putLetter(newLetter);
        			}
        		}
        		
            }
        }
        else
        {
            displaySyntax(client);
        }
        return true;
    }
}
