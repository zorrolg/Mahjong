/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import com.citywar.bll.PlayerBussiness;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.CommonUtil;

/**
 * 玩家更新注册信息, 对于客户端表现则为从游客身份到注册
 * 
 * @author tracy
 * @date 2011-12-21
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.USER_UPDATE, desc = "玩家更新注册信息")
public class UserUpdateCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        boolean isExsit = true;
//        int changeType = 0;
        String account = packet.getStr();
        String userPwd = packet.getStr();
//        String userName = packet.getStr();
//        byte sex = packet.getByte();
//        /*
//         * 添加玩家头像信息的入库
//         */
//        String picPathUrl = packet.getStr();
//        byte picNum = packet.getByte();//玩家更新的照片数量
//        List<String> list = new ArrayList<String>();
//        for (byte i = 0; i < picNum; i++)
//        {
//        	list.add(packet.getStr());
//        }
//        //System.out.println("------->" + picPathUrl + "---->length--" + picPathUrl.length());
        PlayerInfo playerInfo = PlayerBussiness.getPlayerInfoByAccount(account);
        if (CommonUtil.isNullOrEmpty(account)
                || CommonUtil.isNullOrEmpty(userPwd) )
            return 0;
        if (playerInfo != null)
        {
            isExsit = false;
            playerInfo = player.getPlayerInfo();
            playerInfo.setAccount(account);
            playerInfo.setUserPwd(userPwd);
//            playerInfo.setUserName(userName);
//            playerInfo.setSex(sex);
//            playerInfo.setUserPwd(userPwd);
            
//            if (!picPathUrl.isEmpty()) {
//            	playerInfo.setPicPath(picPathUrl);
//            	changeType = 2;
//            }
//            
//            if (null != list)
//            {
//            	playerInfo.setPicPathList(list);
//            }
            
//            playerInfo.setUserType(1); //注册成功后变为注册用户
            
            player.setPlayerInfo(playerInfo);
            // 是否需要立即保存入库来确定 boolean
        }
        player.getOut().SendUpdatePublicPlayer(isExsit, playerInfo);
        //这里特殊逻辑检测是否完成新手任务3 isCallOne = 3 gameResult = 0
//        player.isFinishTask(TaskType.UpdateInfo, changeType, 0);
        return 0;
    }
}
