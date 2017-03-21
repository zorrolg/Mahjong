/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.net.URLDecoder;
import java.sql.Timestamp;

import javax.websocket.Session;

import com.citywar.bll.PlayerBussiness;
import com.citywar.bll.TaskBussiness;
import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.LetterMgr;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.SystemAwardMgr;
import com.citywar.manager.TaskMgr;
import com.citywar.socket.Packet;
import com.citywar.socket.PacketStrictDecoder;
import com.citywar.socket.PacketStrictEncoder;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.DirtyCharUtil;

/**
 * 玩家注册(2012-02-14已弃)
 * 
 * @author tracy
 * @date 2011-12-21
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.USER_REG, desc = "玩家注册处理")
public class UserRegisterCmd extends AbstractUserCmd
{

    @Override
    public int excuteSession(Session session, Packet packet)
    {
        byte[] src = packet.getBytes();
        // try
        // {
        // src = RSAUtil.decrypt(Config.getValue("priModString"),
        // Config.getValue("priExpString"), src);
        // }
        // catch (Exception e)
        // {
        // session.close(true);
        // logger.error("UserLoginHandler.RsaCryptoError" + e.toString());
        // return 0;
        // }
        try
        {
         // 读取新的加密密钥
            int[] newKey = new int[8];
            for (int i = 0; i < 8; i++)
            {
                // 因为java的byte类型默认为有符号类型，而加解密算法实际使用的是无符号数，因为做长度提升
                newKey[i] = src[7 + i] & 0xff;
            }
            updateKey(session, newKey);
            
            byte[] UserInfo = new byte[src.length - 15];
            for (int i = 0; i < UserInfo.length; i++)
            {
                UserInfo[i] = src[i + 15];
            }
            String newString = new String(UserInfo);
            String tempString;
            tempString = URLDecoder.decode(newString, "UTF-8");
            String[] temp = tempString.split(",");
            if (temp.length == 4)
            {
                String account = temp[0];
                String pwd = temp[1];
                String userName = temp[2];
                String sex = temp[3];
                String message = "";
        	
                byte userRegisterState = 0;//userRegisterState 0：成功 1：用户名太长 2：用户名有特殊字符 3：邮箱已经存在
                if (LanguageMgr.getStringLength(userName) <= 14 ||
                		account.isEmpty() || pwd.isEmpty()
                		||account.indexOf("%") != -1 || pwd.indexOf("%") != -1)//14 //50
                {
	               	if (userName.isEmpty()
	                        || DirtyCharUtil.checkIllegalChar(userName))
	                {
	               		userRegisterState = 2;
	                    message = LanguageMgr.getTranslation("CityWar.VisualizeRegister.Illegalcharacters");
	                    Packet pck = new Packet(UserCmdOutType.USER_REG);
	                    pck.putBoolean(true);
	                    if (session != null && session.isOpen())
	                        session.getBasicRemote().sendText(pck.toString());
	                    return 0;
	                }
                }
                else
                {
                	userRegisterState = 1;
                    message = LanguageMgr.getTranslation("CityWar.NickNameCheck.Long");
                    return 0;
                }

                PlayerInfo info = PlayerBussiness.getPlayerInfoByAccount(account);
                if (info != null || userName.indexOf("大话骰") != -1)
                {
                	Packet pck = new Packet(UserCmdOutType.USER_REG);
                    pck.putBoolean(true);
                    if (session != null && session.isOpen())
                        session.getBasicRemote().sendText(pck.toString());
                    return 0;
                }
                info = new PlayerInfo();
                //TODO 新注册一条 UUID deviceName UserType - 1
                int userId = PlayerBussiness.getNewUserId();
                info.setUserId(userId);
                info.setAccount(account);
                info.setUserPwd(pwd);
                info.setUserName(userName);
                info.setSex(Byte.valueOf(sex));
                info.setLevel(1);
                info.setGp(1);
                info.setDrunkLevelContest(LevelMgr.getDrunkLevel(1));
                info.setDrunkLevelSocial(LevelMgr.getDrunkLevel(1));
                info.setRegisterDate(new Timestamp(System.currentTimeMillis()));
                info.setUserType(1);
                PlayerBussiness.addPlayerInfo(info);

                GamePlayer player = new GamePlayer(info.getUserId());
                player.setPlayerInfo(info);
                player.setSession(session);
                
//                player.updateKey(newKey);

            	sendRegsResult(player, false);
                
                //直接注册账户时候也要初始化新手任务一
                UserTaskInfo userTaskInfo = new UserTaskInfo(userId, TaskMgr.getTaskInfoByTaskId(TaskMgr.freshUserTaskIds[0]));
                boolean result = TaskBussiness.insertUserCounldCompletedTask(userTaskInfo);
                
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block

        }

        return 0;
    }

    /**
     * 返回客户端注册结果
     * 
     * @param player
     * @param isExistAccount
     */
    private void sendRegsResult(GamePlayer player, boolean isExistAccount)
    {
    	if(null == player) {
    		return ;
    	}
        Packet packet = new Packet(UserCmdOutType.USER_REG);
        packet.putBoolean(isExistAccount);
        if( ! isExistAccount) {
            packet.putStr(player.getPlayerInfo().getAccount());
            packet.putStr(player.getPlayerInfo().getUserPwd());
            //=================start===================
            //@Author Jacky.zheng
            //@Date 2012-03-16
            //注册的时候新增返回用户的UserId
            packet.putInt(player.getPlayerInfo().getUserId());
        }
        //=================end=====================
        player.getOut().sendTCP(packet);
    }

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	try {
    		String account = packet.getStr();
    		String pwd = packet.getStr();
    		String userName = packet.getStr();
    		byte sex = packet.getByte();
    		String message = "";
    		userName = userName.replaceAll(" ", "");
            if (LanguageMgr.getStringLength(userName) <= 14 ||
            		account.isEmpty() || pwd.isEmpty()
            		||account.indexOf("%") != -1 || pwd.indexOf("%") != -1)//14 //50
            {
               	if (userName.isEmpty() || DirtyCharUtil.checkIllegalChar(userName))
                {
                    message = LanguageMgr.getTranslation("CityWar.VisualizeRegister.Illegalcharacters");
                    sendRegsResult(player, true);
                    return 0;
                }
            }
            else
            {
                message = LanguageMgr.getTranslation("CityWar.NickNameCheck.Long");
            }
            PlayerInfo info = PlayerBussiness.getPlayerInfoByAccount(account);
            if (info != null || userName.indexOf("大话骰") != -1)
            {
            	sendRegsResult(player, true);
                return 0;
            }
            info = new PlayerInfo();
            int userId = PlayerBussiness.getNewUserId();
            info.setUserId(userId);
            info.setAccount(account);
            info.setUserPwd(pwd);
            info.setUserName(userName);
            info.setSex(sex);
            info.setLevel(1);
            info.setGp(1);
            info.setDrunkLevelContest(LevelMgr.getDrunkLevel(1));
            info.setDrunkLevelSocial(LevelMgr.getDrunkLevel(1));
            info.setRegisterDate(new Timestamp(System.currentTimeMillis()));
            info.setUserType(1);
            PlayerBussiness.addPlayerInfo(info);

            GamePlayer regPlayer = new GamePlayer(info.getUserId());
            regPlayer.setPlayerInfo(info);
            regPlayer.setSession(player.getSession());

            sendRegsResult(player, false);
            
            //直接注册账户时候也要初始化新手任务一
            UserTaskInfo userTaskInfo = new UserTaskInfo(userId, TaskMgr.getTaskInfoByTaskId(TaskMgr.freshUserTaskIds[0]));
            boolean result = TaskBussiness.insertUserCounldCompletedTask(userTaskInfo);
            
            //注册成功发送一条系统私信
            LetterMgr.add(SystemAwardMgr.createWelComeLetter(regPlayer.getPlayerInfo()));
            
    	} catch (Exception e) {
    		e.printStackTrace();
    	} 
        return 0;
    }

    private void updateKey(Session session, int[] newKey)
    {
        int[] temp;

        if (session != null)
        {       	           
            
            temp = new int[newKey.length];
            System.arraycopy(newKey, 0, temp, 0, newKey.length);
            session.getUserProperties().put(PacketStrictDecoder.class.toString(), temp);

            temp = new int[newKey.length];
            System.arraycopy(newKey, 0, temp, 0, newKey.length);
            session.getUserProperties().put(PacketStrictEncoder.class.toString(), temp);
        }
    }
    
    public void addFreshUserTask() {
    	
    }

}
