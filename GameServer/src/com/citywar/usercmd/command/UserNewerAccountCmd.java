/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.sql.Timestamp;

import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.citywar.bll.PlayerBussiness;
import com.citywar.bll.TaskBussiness;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.LetterMgr;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.SystemAwardMgr;
import com.citywar.manager.TaskMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.ThreadSafeRandom;

/**
 * 客户端请求一个临时账户, 服务端完成实际的注册, 但返回服务端生成的账户相关信息
 * 
 * @author tracy
 * @date 2012-2-13
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.USER_NEWER_ACCOUNT, desc = "获取用户最近一次登录的账号")
public class UserNewerAccountCmd extends AbstractUserCmd
{

    private static final Logger logger = Logger.getLogger(UserTempAccountCmd.class.getName());

    @Override
    public int excuteSession(Session session, Packet packet)
    {
        int userId = -1;
    	String UDID = packet.getStr();//网卡ID
    	byte type = packet.getByte();//登录类型
        byte[] src = packet.getBytes();
        boolean isOldPlayer = true;
        PlayerInfo info = null;
        if(type == 0) {//去最近的
        	info = PlayerBussiness.getLastLoginAcount(UDID);
        } else {
        	info = PlayerBussiness.getLastLoginTempAcount(UDID);
        }
        GamePlayer player = null;
        
        if(info != null)
        {
        	isOldPlayer = true;
        	player = new GamePlayer(info.getUserId());
        	
        }
        else//服务器生成一个临时账户给客户端
        {
        	isOldPlayer = false;
        	info = new PlayerInfo();
            String userPwd = String.valueOf(new ThreadSafeRandom().next(100000, 1000000));
            userId = PlayerBussiness.getNewUserId();
            String userName = "Player" + (userId);
            info.setUserId(userId);
            info.setAccount(UDID);//String.valueOf(userId)//UDID
            info.setUserPwd(userPwd);
            info.setUserName(userName);// userName // deviceName
            info.setSex((byte) 0); // 性别保密
            info.setLevel(1);
            info.setGp(1);
            info.setDrunkLevelContest(LevelMgr.getDrunkLevel(1));
            info.setDrunkLevelSocial(LevelMgr.getDrunkLevel(1));
//            info.setMachineryId(UDID);
            info.setUDID(UDID);//设置唯一标识
            info.setUserType(0);//临时账号
            info.setRegisterDate(new Timestamp(System.currentTimeMillis()));
            PlayerBussiness.addPlayerInfo(info);

            player = new GamePlayer(info.getUserId());
        }
        
        player.setPlayerInfo(info);
        player.setSession(session);

        // 读取新的加密密钥
//        int[] newKey = new int[8];

//        for (int i = 0; i < 8; i++)
//        {
//            // 因为java的byte类型默认为有符号类型，而加解密算法实际使用的是无符号数，因为做长度提升
//            newKey[i] = src[7 + i] & 0xff;
//        }
//        player.updateKey(newKey);
        
        sendRegsResult(player);
        
        if(!isOldPlayer)
        {
	        //新用户进来自动接受第一个新手任务
	        UserTaskInfo userTaskInfo = new UserTaskInfo(userId, TaskMgr.getTaskInfoByTaskId(TaskMgr.freshUserTaskIds[0]));
	        TaskBussiness.insertUserCounldCompletedTask(userTaskInfo);
	        LetterMgr.add(SystemAwardMgr.createWelComeLetter(player.getPlayerInfo()));
        }
        return 0;
    }
    
    private void sendRegsResult(GamePlayer player)
    {
        Packet packet = new Packet(UserCmdOutType.USER_NEWER_ACCOUNT);
        packet.putStr(player.getPlayerInfo().getAccount());
        packet.putStr(player.getPlayerInfo().getUserPwd());
        byte playerType = (byte)(player.getPlayerInfo().isTempAccount() ? 1 : 0);//1为临时账号 0 为会员
        packet.putByte(playerType);
        player.getOut().sendTCP(packet);
    }

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        return 0;
    }
}
