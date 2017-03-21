package com.citywar.usercmd.command;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

import javax.websocket.Session;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.citywar.ServerClient;
import com.citywar.bll.PlayerBussiness;
import com.citywar.bll.TaskBussiness;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.KKMgr;
import com.citywar.manager.LetterMgr;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.SystemAwardMgr;
import com.citywar.manager.TaskMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;




@UserCmdAnnotation(code = UserCmdType.SDK_LOGIN, desc = "")
public class UserH5LoginCmd extends AbstractUserCmd{

    private static final Logger logger = Logger.getLogger(UserH5LoginCmd.class.getName());
    
	@Override
	public int execute(GamePlayer player, Packet packet) {
		
        
		return 0;
	}
		
	public int excuteSession(Session session, Packet packet){

				
		
		try
        {
			String userId = packet.getStr();
            String userKey = packet.getStr();
            
            String channelId = "yiteng";
            if(packet.getLength() > 2)
            	channelId = packet.getStr();
            
            
            if (userId != null && userKey != null)
            {
            	                
                if (userId.isEmpty() || userId.isEmpty()
                		||userId.indexOf("%") != -1 || userKey.indexOf("%") != -1) {
                	sendLoginFail(session,2);
                	return 0;
                }
                
                
                Map<String, String> tempMap = KKMgr.GetUserInfo(channelId, userId, userKey);
                if(tempMap.size() == 0)
                {
                	sendLoginFail(session,2);
                	return 0;
                }
                
                
                boolean isNew = false;
                int iUserId = Integer.valueOf(tempMap.get("UserId"));                
                PlayerInfo info = PlayerBussiness.getPlayerInfoById(Integer.valueOf(iUserId));// getPlayerInfoByAccount(account);
                if (info == null) // Login Fail, return
                {
                	info = new PlayerInfo();            		
//                	int userId = PlayerBussiness.getNewUserId();
//                	String userAccount = "Dicer" + (userId);
                	String account = "Guest" + (iUserId);
                	
            		info.setUserId(iUserId);
                    info.setAccount(account);//String.valueOf(userId)//UDID
                    info.setUserPwd(userKey);
                    info.setUserName(account);
                    info.setSex((byte) 0); // 性别保密
                    info.setLevel(1);
                    info.setGp(1);
                    info.setPos("22.544493451628323,113.92373362192693");
                    info.setCharmValve(100);
                    info.setStageId(1);
                    info.setDrunkLevelContest(LevelMgr.getDrunkLevel(1));
                    info.setDrunkLevelSocial(LevelMgr.getDrunkLevel(1));
                    info.setUserType(0);//临时账号
                    info.setRegisterDate(new Timestamp(System.currentTimeMillis()));
                    
                    
//                    Object strIP = session.getUserProperties().get("javax.websocket.endpoint.remoteAddress");
//                    info.setCity(strIP.toString());
                    isNew = true;
                }
                
                
                
                
                if(tempMap.containsKey("Account"))
                	info.setAccount(java.net.URLDecoder.decode(tempMap.get("Account"), "UTF-8"));// userName // deviceName  
                if(tempMap.containsKey("Password"))
                	info.setUserPwd(java.net.URLDecoder.decode(tempMap.get("Password"), "UTF-8"));// userName // deviceName  
                if(tempMap.containsKey("NickName"))
                	info.setUserName(java.net.URLDecoder.decode(tempMap.get("NickName"), "UTF-8"));// userName // deviceName     
                if(tempMap.containsKey("PicPath"))
                	info.setPicPath(java.net.URLDecoder.decode(tempMap.get("PicPath"), "UTF-8"));
                if(tempMap.containsKey("City"))
                	info.setCity(java.net.URLDecoder.decode(tempMap.get("City"), "UTF-8"));                
                if(tempMap.containsKey("Sign"))
                	info.setSign(java.net.URLDecoder.decode(tempMap.get("Sign"), "UTF-8"));
                if(tempMap.containsKey("Fortune"))
                	info.setFortune(Integer.parseInt(java.net.URLDecoder.decode(tempMap.get("Fortune"), "UTF-8")));
                if(tempMap.containsKey("Sex"))
                	info.setSex(Byte.parseByte(java.net.URLDecoder.decode(tempMap.get("Sex"), "UTF-8")));
                
                
                
                if(tempMap.containsKey("Loc") && tempMap.get("Loc") != "null" && tempMap.get("Loc") != "0,0")
                	info.setPos(tempMap.get("Loc"));
                else
                	info.setPos("22.544493451628323,113.92373362192693");
                
                
                
                if(isNew)
                {
                	PlayerBussiness.addPlayerInfo(info);                    
                    initUserData(info);  	        
                }
                else
                {
                	PlayerBussiness.updateAll(iUserId, info);
                }
                
                if (session != null && session.isOpen())		
    	        {
    	        	Packet pkg = new Packet(UserCmdOutType.SDK_LOGIN);
    	        	pkg.putStr(info.getAccount());
    	        	pkg.putStr(info.getUserPwd());
    	            session.getBasicRemote().sendText(pkg.getJsonDate());
    	        }
                
                
                
                
                
                
                GamePlayer currentPlayer = null;
                currentPlayer = WorldMgr.getPlayerByID(info.getUserId());
                if (currentPlayer == null)// 全局用户集合没有这个用户
                {
                	currentPlayer = new GamePlayer(info.getUserId());
                	currentPlayer.setPlayerInfo(info);
                	currentPlayer.setSession(session);
                	currentPlayer.setFeeVesion(false);
                	currentPlayer.getPlayerInfo().setMachineType("");
                	currentPlayer.setUserKey(userKey);
                    session.getUserProperties().put(GamePlayer.class.toString(), currentPlayer);


                    ServerClient client = (ServerClient) session.getUserProperties().get(ServerClient.class.toString());
                    client.setPlayer(currentPlayer);
                    currentPlayer.Login(false);
                    
                }
                else // 老用户登录
                {
                	currentPlayer.getSession().close();
                	
                	session.getUserProperties().put(GamePlayer.class.toString(), currentPlayer);
                    currentPlayer.setSession(session);
                    currentPlayer.setFeeVesion(false); 
                    currentPlayer.getPlayerInfo().setMachineType("");
                    currentPlayer.setUserKey(userKey);
                    ServerClient client = (ServerClient) session.getUserProperties().get(ServerClient.class.toString());
                    client.setPlayer(currentPlayer);
                    
                    
                    currentPlayer.Login(true);
                    // player enter the playing room.
//                    currentPlayer.sendEnterTrusteedRoom();
                    
                }
            }
            
        }
        catch (Exception e)
        {
            logger.error("[ UserLoginCmd : excuteSession ]", e);
        }
		
		return 0;
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
	
	
	
	

	private void initUserData(PlayerInfo player)
	{
		
		int userId = player.getUserId();
		
		UserTaskInfo userTaskInfo = new UserTaskInfo(userId, TaskMgr.getTaskInfoByTaskId(TaskMgr.freshUserTaskIds[0]));
        TaskBussiness.insertUserCounldCompletedTask(userTaskInfo);
        
        LetterMgr.add(SystemAwardMgr.createWelComeLetter(player));
//        StageBussiness.addStages(new PlayerStage(player.getUserId(),1,100));
        
        
        
		//建筑及新手任务
//        List<BuildTypeInfo> listBuildType = BuildBussiness.getAllBuildsTypeInfo();
//        for(BuildTypeInfo buildType : listBuildType)
//        {
//        	UserBuildInfo userBuildInfo = new UserBuildInfo(userId,buildType.getBuildTypeId());
//        	
//        	if(buildType.getBuildTypeId() == 1)
//        		userBuildInfo.setBuildLevel(1);
//        	
//        	UserBuildBussiness.insertUserBuild(userBuildInfo);
//        }
        
//        UserCardInfo userCardInfo = new UserCardInfo(userId,CardMgr.getFirstDevelopCard(),CardMgr.getFirstFactoryCard());
//        UserCardBussiness.insertUserCard(userCardInfo);
        
        
        
        
        //task
//        UserTaskInfo userTaskInfoCharge = new UserTaskInfo(userId, TaskMgr.getTaskInfoByTaskId(2101));
//        TaskBussiness.insertUserCounldCompletedTask(userTaskInfoCharge);
        
        
        
//        List<TaskInfo> taskStage = TaskMgr.getALLStageTasks(1);
//        for(TaskInfo task : taskStage)
//        {
//        	UserTaskInfo userTask = new UserTaskInfo(userId, task);
//            TaskBussiness.insertUserCounldCompletedTask(userTask);
//        }
        
        
        
	}


	public static String getParamWithDefaultValue(JSONObject json, String keyName)
	{
		String result = "";
		if (null != json && json.containsKey(keyName))
		{
			result = json.getString(keyName);
		}
		return null != result ? result : "";
	}
	
	
}
