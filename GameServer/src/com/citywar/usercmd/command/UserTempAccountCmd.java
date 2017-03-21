/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.io.IOException;
import java.sql.Timestamp;

import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.citywar.bll.PlayerBussiness;
import com.citywar.bll.StageBussiness;
import com.citywar.bll.TaskBussiness;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.PlayerStage;
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
@UserCmdAnnotation(code = UserCmdType.USER_TEMP, desc = "临时玩家处理")
public class UserTempAccountCmd extends AbstractUserCmd
{

    private static final Logger logger = Logger.getLogger(UserTempAccountCmd.class.getName());

    @Override
    public int excuteSession(Session session, Packet packet)
    {
    	
    	
//    	System.out.print("玩家注册=====1=====" +  TimeUtil.getSysteCurTime());
    	
    	String UDID = packet.getStr();
        String deviceType = packet.getStr();
        
        
        String SdkType = "";
        String SdkUserName = "";
        String SdkNickName = "";
        String[] list = deviceType.split("[|]", -1); 
        if(list.length == 4)
        {
        	deviceType = list[0];
        	SdkType = list[1];
        	SdkUserName = list[2].isEmpty() ? "" : list[1] + "_" + list[2];
        	SdkNickName = list[3];
        }

        
//        byte[] src = packet.getBytes();
        int userId = -1;
        try
        {
            PlayerInfo info = null;
            String userPwd = String.valueOf(new ThreadSafeRandom().next(100000,
                                                                        1000000));
            userId = PlayerBussiness.getNewUserId();
            
            String userAccount = "Player" + (userId);
            if(!SdkUserName.isEmpty() && !SdkUserName.equals(SdkType))
            	userAccount = SdkUserName;
            	
            String userName = userAccount;
            if(!SdkNickName.isEmpty())
            	userName = SdkNickName;
                                  
         
            
            info =  PlayerBussiness.getPlayerInfoByAccount(userAccount);
        	if(info == null)
        	{
        		info = new PlayerInfo();
        		
        		info.setUserId(userId);
                info.setAccount(userAccount);//String.valueOf(userId)//UDID
                info.setUserPwd(userPwd);
                info.setUserName(userName);// userName // deviceName
                info.setSex((byte) 0); // 性别保密
                info.setLevel(1);
                info.setGp(1);
                info.setPos("22.544493451628323,113.92373362192693");
                info.setCharmValve(100);
                info.setStageId(1);
                info.setDrunkLevelContest(LevelMgr.getDrunkLevel(1));
                info.setDrunkLevelSocial(LevelMgr.getDrunkLevel(1));
                info.setUDID(UDID);//设置唯一标识
                
                info.setMachineType(deviceType);
                info.setSdkType(SdkType);
                info.setSdkUserName(SdkUserName);
                info.setUserType(0);//临时账号
                info.setRegisterDate(new Timestamp(System.currentTimeMillis()));
                PlayerBussiness.addPlayerInfo(info);
        	}        	
        	else
        	{
	        	sendRegsResult(session);
	        	return 0;
        	}
            

            GamePlayer player = new GamePlayer(info.getUserId());
            player.setPlayerInfo(info);
            player.setSession(session);

            // 读取新的加密密钥
//            int[] newKey = new int[8];
//
//            for (int i = 0; i < 8; i++)
//            {
//                // 因为java的byte类型默认为有符号类型，而加解密算法实际使用的是无符号数，因为做长度提升
//                newKey[i] = src[7 + i] & 0xff;
//            }
//            player.updateKey(newKey);
            
            sendRegsResult(player);
            
            
            
            //建筑及新手任务
//            List<BuildTypeInfo> listBuildType = BuildBussiness.getAllBuildsTypeInfo();
//            for(BuildTypeInfo buildType : listBuildType)
//            {
//            	UserBuildInfo userBuildInfo = new UserBuildInfo(userId,buildType.getBuildTypeId());
//            	
//            	if(buildType.getBuildTypeId() == 1)
//            		userBuildInfo.setBuildLevel(1);
//            	
//            	UserBuildBussiness.insertUserBuild(userBuildInfo);
//            }
            
//            UserCardInfo userCardInfo = new UserCardInfo(userId,CardMgr.getFirstDevelopCard(),CardMgr.getFirstFactoryCard());
//            UserCardBussiness.insertUserCard(userCardInfo);
            
            
            
            
            //task
//            UserTaskInfo userTaskInfoCharge = new UserTaskInfo(userId, TaskMgr.getTaskInfoByTaskId(2101));
//            TaskBussiness.insertUserCounldCompletedTask(userTaskInfoCharge);
            
            UserTaskInfo userTaskInfo = new UserTaskInfo(userId, TaskMgr.getTaskInfoByTaskId(TaskMgr.freshUserTaskIds[0]));
            TaskBussiness.insertUserCounldCompletedTask(userTaskInfo);
            
//            List<TaskInfo> taskStage = TaskMgr.getALLStageTasks(1);
//            for(TaskInfo task : taskStage)
//            {
//            	UserTaskInfo userTask = new UserTaskInfo(userId, task);
//                TaskBussiness.insertUserCounldCompletedTask(userTask);
//            }
            
            
            StageBussiness.addStages(new PlayerStage(player.getUserId(),1,100));
            LetterMgr.add(SystemAwardMgr.createWelComeLetter(player.getPlayerInfo()));
            
            
            
//            System.out.println("=====2=====" +  TimeUtil.getSysteCurTime());
        }
        catch (Exception e)
        {
            logger.error("[ UserTempAccountCmd : excuteSession ]", e);
        }

        return 0;
    }
    
    private void sendRegsResult(GamePlayer player)
    {
        Packet packet = new Packet(UserCmdOutType.USER_TEMP);
        packet.putStr(player.getPlayerInfo().getAccount());
        packet.putStr(player.getPlayerInfo().getUserPwd());
        packet.putStr(player.getPlayerInfo().getUserName());
        packet.putByte(player.getPlayerInfo().getSex());
        player.getOut().sendTCP(packet);
        
//        System.out.println("USER_TEMP=======1===" + player.getPlayerInfo().getAccount() + "=====" + player.getPlayerInfo().getUserName());
    }

    
    private void sendRegsResult(Session session)
    {
    	try {
    		
	    	Packet packet = new Packet(UserCmdOutType.USER_TEMP);
	    	packet.putStr("");
	        packet.putStr("");
	        packet.putStr("");
	        packet.putByte((byte)0);
	        
	        if (session != null && session.isOpen())
					session.getBasicRemote().sendText(packet.toString());
				
	        
	        System.out.print("=======USER_TEMP2===");
        
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
