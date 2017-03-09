/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.util.ArrayList;
import java.util.List;

import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.DirtyCharUtil;

/**
 * 玩家更新头像
 * 
 * @author tracy
 * @date 2011-12-21
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.USER_UPDATE_INFO, desc = "玩家更新头像")
public class UserUpdateInfoCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	boolean isExsit = true;
        int changeType = 0;
        
//        String account = packet.getStr();
//        String userPwd = packet.getStr();
        String userName = packet.getStr();
        byte sex = packet.getByte();
        /*
         * 添加玩家头像信息的入库
         */
        String picPathUrl = packet.getStr();
        byte picNum = packet.getByte();//玩家更新的照片数量
        List<String> list = new ArrayList<String>();
        for (byte i = 0; i < picNum; i++)
        {
        	list.add(packet.getStr());
        }
        
        if (sex < 0 || sex > 2)
        {
        	String msg = LanguageMgr.getTranslation("CityWar.UserUpdate.Wrong");
        	sendUpdateResult(player, false, msg);
            return 0;
        }
        
        PlayerInfo playerInfo = player.getPlayerInfo();//PlayerBussiness.getPlayerInfoByAccount(account);
        if(DirtyCharUtil.checkIllegalChar(userName))
        {
        	String msg = LanguageMgr.getTranslation("CityWar.VisualizeRegister.Illegalcharacters");
        	sendUpdateResult(player, false, msg);
        	return 0;
        }
        

        if (playerInfo != null)
        {
            isExsit = false;
            playerInfo = player.getPlayerInfo();
//            playerInfo.setAccount(account);
            playerInfo.setUserName(userName);
            playerInfo.setSex(sex);
//            playerInfo.setUserPwd(userPwd);
            
            if (!picPathUrl.isEmpty()) {
            	playerInfo.setPicPath(picPathUrl);
            	changeType = 2;
            }
            
            if (null != list)
            {
            	playerInfo.setPicPathList(list);
            }
            
//            playerInfo.setUserType(1); //注册成功后变为注册用户
            
            player.setPlayerInfo(playerInfo);
            // 是否需要立即保存入库来确定 boolean
        }
        sendUpdateResult(player, true, "");
        player.getOut().SendUpdatePublicPlayer(isExsit, playerInfo);
        //这里特殊逻辑检测是否完成新手任务3 isCallOne = 3 gameResult = 0
        player.isFinishTask(TaskConditionType.UpdateInfo, changeType, 0);
        return 0;
    }
    
    private void sendUpdateResult(GamePlayer player,boolean isSucc, String msg)
    {
    	Packet packet = new Packet(UserCmdOutType.USER_UPDATE_INFO);
		packet.putBoolean(isSucc);
		if(!isSucc)
			packet.putStr(msg);
		
		player.getOut().sendTCP(packet);
    }
    
   

}
