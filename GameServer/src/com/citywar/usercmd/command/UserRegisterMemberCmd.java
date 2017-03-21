/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.sql.Timestamp;
import java.util.List;

import com.citywar.bll.BuildBussiness;
import com.citywar.bll.PlayerBussiness;
import com.citywar.bll.StageBussiness;
import com.citywar.bll.TaskBussiness;
import com.citywar.bll.UserBuildBussiness;
import com.citywar.bll.UserCardBussiness;
import com.citywar.bll.UserRegisterBussiness;
import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.BuildTypeInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.PlayerStage;
import com.citywar.dice.entity.UserBuildInfo;
import com.citywar.dice.entity.UserCardInfo;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.CardMgr;
import com.citywar.manager.LetterMgr;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.SystemAwardMgr;
import com.citywar.manager.TaskMgr;
import com.citywar.socket.Packet;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.DirtyCharUtil;

/**
 * 
 * @author zhiyun.peng
 * 
 *@since   V2.2
 *
 */
@UserCmdAnnotation(code = UserCmdType.USER_REG_MEMBER, desc = "玩家注册处理")
public class UserRegisterMemberCmd extends AbstractUserCmd
{
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
           
    		PlayerInfo info = null;
    		if(checkClientParam(account, pwd, userName, sex, player))
    		{
    			
    			PlayerInfo oldInfo = PlayerBussiness.getPlayerInfoByAccount(account);
                if (oldInfo != null)
                {
                	message = LanguageMgr.getTranslation("CityWar.NickNameCheck.Exist");
                	sendRegsResult(player, true, message);
                    return 0;
                }
                
    			//如果用户是临时用户则转为会员
                if(player.getPlayerInfo().getUserType() == 0)
                {                	
                	 info = player.getPlayerInfo();
                     info.setAccount(account);
                     info.setUserPwd(pwd);
                     info.setUserName(userName);
                     info.setSex(sex);
                     info.setUDID(null);
                     info.setUserType(1);
                	
                     //更新用户属性
                     PlayerBussiness.updateAll(info.getUserId(), info);
                     //发送成功协议包
                     sendRegsResult(player, false, message);
                     
                     
                     player.isFinishTask(TaskConditionType.UpdateInfo, 1, 0);
                }
                //如果当前用户是会员 则重新创建一个新的会员
                else
                {
                	info = creatTempAccount(account, pwd, userName, sex);
                	GamePlayer regPlayer = new GamePlayer(info.getUserId());
                    regPlayer.setPlayerInfo(info);
                    regPlayer.setSession(player.getSession());

                    
//                    List<BuildTypeInfo> listBuildType = BuildBussiness.getAllBuildsTypeInfo();
//                    for(BuildTypeInfo buildType : listBuildType)
//                    {
//                    	UserBuildInfo userBuildInfo = new UserBuildInfo(info.getUserId(),buildType.getBuildTypeId());
//                    	
//                    	if(buildType.getBuildTypeId() == 1)
//                    		userBuildInfo.setBuildLevel(1);
//                    	
//                    	UserBuildBussiness.insertUserBuild(userBuildInfo);
//                    }
//                    
//                    UserCardInfo userCardInfo = new UserCardInfo(info.getUserId(),CardMgr.getFirstDevelopCard(),CardMgr.getFirstFactoryCard());
//                    UserCardBussiness.insertUserCard(userCardInfo);
//                    
//                    
//                    //直接注册账户时候也要初始化新手任务一
//                    UserTaskInfo userTaskInfo = new UserTaskInfo(info.getUserId(), TaskMgr.getTaskInfoByTaskId(TaskMgr.freshUserTaskIds[0]));
//                    TaskBussiness.insertUserCounldCompletedTask(userTaskInfo);
//                    
//                    //注册成功发送一条系统私信
//                    LetterMgr.add(SystemAwardMgr.createWelComeLetter(regPlayer));
                    
                    
                    
                    List<BuildTypeInfo> listBuildType = BuildBussiness.getAllBuildsTypeInfo();
                    for(BuildTypeInfo buildType : listBuildType)
                    {
                    	UserBuildInfo userBuildInfo = new UserBuildInfo(info.getUserId(),buildType.getBuildTypeId());
                    	
                    	if(buildType.getBuildTypeId() == 1)
                    		userBuildInfo.setBuildLevel(1);
                    	
                    	UserBuildBussiness.insertUserBuild(userBuildInfo);
                    }
                    
                    UserCardInfo userCardInfo = new UserCardInfo(info.getUserId(),CardMgr.getFirstDevelopCard(),CardMgr.getFirstFactoryCard());
                    UserCardBussiness.insertUserCard(userCardInfo);
                                                           
                    
                    //task
//                    UserTaskInfo userTaskInfoCharge = new UserTaskInfo(info.getUserId(), TaskMgr.getTaskInfoByTaskId(2101));
//                    TaskBussiness.insertUserCounldCompletedTask(userTaskInfoCharge);
                    
                    UserTaskInfo userTaskInfo = new UserTaskInfo(info.getUserId(), TaskMgr.getTaskInfoByTaskId(TaskMgr.freshUserTaskIds[0]));
                    TaskBussiness.insertUserCounldCompletedTask(userTaskInfo);
                    
                    
                    
                    
                    StageBussiness.addStages(new PlayerStage(info.getUserId(),1,100));
                    LetterMgr.add(SystemAwardMgr.createWelComeLetter(player.getPlayerInfo()));
                    
                    
                    //发送成功协议包
                    sendRegsResult(regPlayer,false, message);
                }
                UserRegisterBussiness.addOneCount(String.valueOf(player.getPlayerInfo().getUserId()));
    		}
    	} catch (Exception e) 
    	{
    		e.printStackTrace();
    	} 
        return 0;
    }
    /**
     * 验证客户端传过来的参数
     * @return
     */
    private boolean checkClientParam(String account,String pwd,String userName,byte sex,GamePlayer player)
    {
    	String message = "";
        if (LanguageMgr.getStringLength(userName) > 15 ||
        		account.isEmpty() || pwd.isEmpty()
        		||account.indexOf("%") != -1 || pwd.indexOf("%") != -1)//14 //50
        {
        	message = LanguageMgr.getTranslation("CityWar.NickNameCheck.Long");
        	sendRegsResult(player, true, message);
            return false;
        }
        
        if (userName.isEmpty() || DirtyCharUtil.checkIllegalChar(userName))
        {
            message = LanguageMgr.getTranslation("CityWar.VisualizeRegister.Illegalcharacters");
            sendRegsResult(player, true, message);
            return false;
        }
        
        if ( userName.indexOf("大话骰") != -1)
        {
        	message = LanguageMgr.getTranslation("CityWar.VisualizeRegister.Illegalcharacters");
        	sendRegsResult(player, true, message);
            return false;
        }
        return true;
    }
    /**
     * 在数据库中 新增一个会员用户并返回
     * @return 会员用户信息
     */
    private PlayerInfo creatTempAccount(String account,String pwd,String userName,byte sex)
    {
    	 PlayerInfo info = new PlayerInfo();
         int userId = PlayerBussiness.getNewUserId();
         String userNameNew = "Dicer" + (userId);
         
         info.setUserId(userId);
         info.setAccount(account);
         info.setUserPwd(pwd);
         info.setUserName(userNameNew);
         info.setSex(sex);
         info.setLevel(1);
         info.setGp(1);
         info.setCharmValve(100);
         info.setStageId(1);
         info.setDrunkLevelContest(LevelMgr.getDrunkLevel(1));
         info.setDrunkLevelSocial(LevelMgr.getDrunkLevel(1));
         info.setRegisterDate(new Timestamp(System.currentTimeMillis()));
         info.setUserType(1);
         PlayerBussiness.addPlayerInfo(info);
         return info;
    }

    /**
     * 返回客户端注册结果
     * 
     * @param player
     * @param isExistAccount
     */
    private void sendRegsResult(GamePlayer player, boolean isExistAccount, String message)
    {
    	if(null == player) {
    		return ;
    	}
        Packet packet = new Packet(UserCmdOutType.USER_REG_MEMBER);
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
        else
        {
        	packet.putStr(message);
        }
        //=================end=====================
        player.getOut().sendTCP(packet);
    }
    
    public void addFreshUserTask() {
    	
    }

}
