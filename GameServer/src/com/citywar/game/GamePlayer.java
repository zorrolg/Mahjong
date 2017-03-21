/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.game;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.AttributeKey;

import com.citywar.GameServer;
import com.citywar.bll.FriendsBussiness;
import com.citywar.bll.ItemBussiness;
import com.citywar.bll.PlayerBussiness;
import com.citywar.bll.StageBussiness;
import com.citywar.bll.TaskBussiness;
import com.citywar.bll.UserAwardBussiness;
import com.citywar.bll.UserRewardBussiness;
import com.citywar.common.BaseItem;
import com.citywar.dice.entity.FriendInfo;
import com.citywar.dice.entity.ItemTemplateInfo;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.PlayerStage;
import com.citywar.dice.entity.UserAwardInfo;
import com.citywar.dice.entity.UserItemInfo;
import com.citywar.dice.entity.UserReferenceInfo;
import com.citywar.dice.entity.UserReward;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.gameutil.PlayerAroudUser;
import com.citywar.gameutil.PlayerDayActivity;
import com.citywar.gameutil.PlayerGift;
import com.citywar.gameutil.PlayerItemBag;
import com.citywar.gameutil.PlayerMessagegBox;
import com.citywar.gameutil.PlayerReference;
import com.citywar.gameutil.PlayerTask;
import com.citywar.gameutil.UserProperty;
import com.citywar.hall.BaseHall;
import com.citywar.manager.AwardMgr;
import com.citywar.manager.ItemMgr;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.MessgageOperateMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.RoomMgr;
import com.citywar.manager.SystemAwardMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.packet.PacketLib;
import com.citywar.queue.SelfDrivenRunnableQueue;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.HallGameType;
import com.citywar.type.MessageType;
import com.citywar.type.PropType;
import com.citywar.type.TaskConditionType;
import com.citywar.type.UserCmdOutType;
import com.citywar.usercmd.UserCmdWrapper;
import com.citywar.util.Config;
import com.citywar.util.TimeUtil;

/**
 * 玩家对象
 * 
 * @author Dream
 * @date 2011-12-19
 * @version
 * 
 */
public class GamePlayer
{
    private static Logger logger = Logger.getLogger(GamePlayer.class.getName());

    public static final AttributeKey GAME_PLAYER_CONTEXT = new AttributeKey(
            GamePlayer.class, "GamePlayer Context");

    /**
     * 登录session会话
     */
    private Session session;

    /**
     * 玩家基本信息
     */
    private PlayerInfo playerCharacter;

    /**
     * 玩家实名制等信息
     */
    private UserProperty userProperty;

    /**
     * 用户ID
     */
    private int userId;

    private PacketLib out;

    private SelfDrivenRunnableQueue<UserCmdWrapper> cmdQueue = new SelfDrivenRunnableQueue<UserCmdWrapper>(
            GameServer.getUserCmdExecutor());

    private AtomicInteger changeCount = new AtomicInteger(0);

    private BaseRoom currentRoom;
    
    private BaseHall currentHall;//添加用户当前所在大厅
    
    private int currentHallGameType;

    // 房间中的位置
    private int currentRoomPos;

    private long pingStart;

    private long pingTime;

    private boolean isPlaying;

    private boolean isRobot;

    private boolean isRobotSleep;
    
    private boolean isRobotState;
    
    private int pengConfirmState;
    
    private boolean isLogin;
    
    /** 是否开了透视眼 */
    private boolean isOpenEye = true;

    private String UserKey;

    private Map<Integer, FriendInfo> friends = new LinkedHashMap<Integer, FriendInfo>();
    
    private Map<Integer, PlayerStage> stages = new HashMap<Integer, PlayerStage>();

    /**
     * 玩家持有的道具背包对象
     */
    private PlayerItemBag propBag;
//    private PlayerCardBag cardBag;
    private PlayerTask playerTask;
    
 
    private PlayerMessagegBox msgBox;
    
    private UserReward userReward;
    
    private UserAwardInfo userAward;
    
    private PlayerDayActivity dayActivity;
    
    private PlayerAroudUser playerAroudUser;
    /** 当前用户登录的客户端是否是免费版   */ 
    private boolean isFeeVesion;
    
    private int currentStage = 0;
    
    private byte playerEndState;
    
    private int pengWinCount;
    private int pengLoseCount;
    
//    private PlayerBuild playerBuild;
//    private PlayerCard playerCard;
    
//    List<Integer> listGameCard;
//    private Map<Integer, Integer> robotCard = new LinkedHashMap<Integer, Integer>();
    private Map<Integer, Timestamp> alreadyRoom = new HashMap<Integer, Timestamp>();
    
   
    
    
//    private Object lockStage = new Object();
    
    public void setPlayerEndState(byte state)
    {
        this.playerEndState = state;
    }

    public byte getPlayerEndState()
    {
        return this.playerEndState;
    }
    
    public int getPengWinCount()
    {
        return pengWinCount;
    }

    public void setPengWinCount(int pengWinCount)
    {
    	this.pengWinCount = pengWinCount;
    }
    
    public int getPengLoseCount()
    {
        return pengLoseCount;
    }

    public void setPengLoseCount(int pengLoseCount)
    {
    	this.pengLoseCount = pengLoseCount;
    }
    
    public void setCurrentStage(int currentStage)
    {
        this.currentStage = currentStage;
    }

    public int getCurrentStage()
    {
        return currentStage;
    }
    
    /**
     * 玩家拥有的奴隶关系
     */
	public PlayerMessagegBox getMsgBox() {
		return msgBox;
	}

	public void setMsgBox(PlayerMessagegBox msgBox) {
		this.msgBox = msgBox;
	}

	private PlayerReference playerReference;
	/**
     * 玩家拥有的礼品
     */
	private PlayerGift playerGift;
	
    public PlayerGift getPlayerGift() {
		return playerGift;
	}

	public void setPlayerGift(PlayerGift playerGift) {
		this.playerGift = playerGift;
	}

	public PlayerReference getPlayerReference() {
		return playerReference;
	}

	public void setPlayerReference(PlayerReference playerReference) {
		this.playerReference = playerReference;
	}

//	public PlayerBuild getPlayerBuild() {
//		return playerBuild;
//	}
//
//	public PlayerCard getPlayerCard() {
//		return playerCard;
//	}
//	
//	public List<Integer> getListGameCard() {
//		return listGameCard;
//	}
//
//	public void setListGameCard(List<Integer> listGameCard) {
//		this.listGameCard = listGameCard;
//	}
//	
//	public int getRobotCardId(int cardType)
//    {
//		
//		int cardId = 0;
//        if (robotCard.containsKey(cardType))
//        {
//        	cardId = robotCard.get(cardType);
//        }
//        else
//        {
//        	CardInfo card = CardMgr.findCardByType(cardType, 1);
//        	if(card != null)
//				cardId = card.getCardId();
//        }
//        return cardId;
//    }
//	
//	
//	public void setRobotCardId(String cardList)
//    {		
//		String[] listDevelop = cardList.split("-");
//		for(String str : listDevelop)
//		{
//			String[] listKey = str.split("=");
//			if(listKey.length == 2)
//				robotCard.put(Integer.parseInt(listKey[0]), Integer.parseInt(listKey[1]));
//		}
//    }
	
	
	public PlayerTask getPlayerTask() {
		return playerTask;
	}

	public void setPlayerTask(PlayerTask playerTask) {
		this.playerTask = playerTask;
	}

	public void setIsRobot(boolean isRobot)
    {
        this.isRobot = isRobot;
    }

    public boolean getIsRobot()
    {
        return isRobot;
    }

    public void setIsRobotSleep(boolean isRobotSleep)
    {
        this.isRobotSleep = isRobotSleep;
    }

    public boolean getIsRobotSleep()
    {
        return isRobotSleep;
    }
    
    public void setIsRobotState(boolean isRobotState)
    {
        this.isRobotState = isRobotState;
    }

    public boolean getIsRobotState()
    {
        return isRobotState;
    }
    
    public int getPengConfirmState()
    {
        return pengConfirmState;
    }

    public void setPengConfirmState(int pengConfirmState)
    {
        this.pengConfirmState = pengConfirmState;
    }
    
    public boolean getIsLogin()
    {
        return isLogin;
    }
    
    public void setPlaying(boolean isPlaying)
    {
        this.isPlaying = isPlaying;
    }

    public boolean getIsPlaying()
    {
        return isPlaying;
    }

    /**
     * @return the playerBag
     */
    public PlayerItemBag getPropBag()
    {
        return propBag;
    }

    /**
     * @param propBag
     *            the playerBag to set
     */
    public void setPropBag(PlayerItemBag propBag)
    {
        this.propBag = propBag;
    }

//    /**
//     * @return the playerBag
//     */
//    public PlayerCardBag getCardBag()
//    {
//        return cardBag;
//    }
//
//    /**
//     * @param propBag
//     *            the playerBag to set
//     */
//    public void setCardBag(PlayerCardBag cardBag)
//    {
//        this.cardBag = cardBag;
//    }
    
    
    
    public Map<Integer, FriendInfo> getFriends()
    {
        return friends;
    }

    public void friendsAdd(int playerID, FriendInfo info)
    {
        friends.put(playerID, info);
    }

    public void friendsRemove(int playerID)
    {
        if (friends.containsKey(playerID))
        {
            friends.remove(playerID);
        }
    }
    
    
    public void addAlreadyRoomId(int roomId, Timestamp info)
    {
    	alreadyRoom.put(roomId, info);
    }

    public void removeAlreadyRoomId(int roomId)
    {
        if (alreadyRoom.containsKey(roomId))
        {
        	alreadyRoom.remove(roomId);
        }
    }
    
    public void clearAlreadyRoom()
    {
    	
    	Timestamp now = new Timestamp(System.currentTimeMillis());
    	long clearTime = 1000 * 50 * 5;
    	
    	List<Integer> removeList = new ArrayList<Integer>();
    	for(Integer roomId : alreadyRoom.keySet())
    	{
    		if(now.getTime() - alreadyRoom.get(roomId).getTime() > clearTime)
    			removeList.add(roomId);    			
    	}
    	
    	for(Integer roomId : removeList)
    		alreadyRoom.remove(roomId);
    	
    }

    public boolean isAlreadyRoom(int roomId)
    {
    	return alreadyRoom.containsKey(roomId);
    }
    
    
    public Map<Integer, PlayerStage> getStages()
    {
        return stages;
    }

    public int getStage(int stageId)
    {
    	int charmValve = 0;
    	if (stages.containsKey(stageId))
        {
    		charmValve = stages.get(stageId).getCharmValve();
        }
    	return charmValve;
    }
    
    public void stageAdd(int stageId, PlayerStage charmvalve)
    {
    	stages.put(stageId, charmvalve);
    }

    public void stageRemove(int stageId)
    {
        if (stages.containsKey(stageId))
        {
        	stages.remove(stageId);
        }
    }
    
    
    public long getPingStart()
    {
    	System.out.println("getPingStart=================" + pingStart);
        return pingStart;
    }

    public void setPingStart(long time)
    {
    	System.out.println("getPingStart=================" + time);
        pingStart = time;
    }

    public long getPingTime()
    {
        return pingTime;
    }

    public void setPingTime(long value)
    {
        pingTime = value;
        Packet pkg = getOut().SendNetWork(getPlayerInfo().getUserId(), pingTime);
        if (getCurrentRoom() != null)
        {
            getCurrentRoom().sendToAll(pkg, this);
        }
    }

    /**
     * @return
     */
    public BaseRoom getCurrentRoom()
    {
        return currentRoom;
    }
    
    public BaseHall getCurrentHall()
    {
    	return currentHall;
    }
    
    /**
     * 玩家设置当前大厅，并退出原来大厅
     * 这里强调，需要调用removeIdlePlayer方法，不能调用playerOutHall方法
     * 移除出去大厅
     * @param hall
     */
    public void setCurrenetHall(BaseHall hall) 
    {
    	
//    	if(hall == null && this.getUserId() == 321938)
//    		System.out.println("setCurrenetHall===================null===" + this.getUserId());
    	
    	
    	if(null != currentHall && currentHall != hall) {
    		currentHall.removeIdlePlayer(this);
    	}
    	this.currentHall = hall;
    }


    
//    public int getCurrentHallGameType()
//    {
//    	return currentHallGameType;
//    }
//    
//    /**
//     * 玩家设置当前大厅，并退出原来大厅
//     * 这里强调，需要调用removeIdlePlayer方法，不能调用playerOutHall方法
//     * 移除出去大厅
//     * @param hall
//     */
//    public void setCurrenetHallGameType(int currentHallType) 
//    {
//    	this.currentHallGameType = currentHallType;
//    }
    
    
    
    
    
    
    public GamePlayer(int playerid)
    {
        super();

        this.userId = playerid;
        this.out = new PacketLib(this);

        // 初始化道具背包
        this.setPropBag(new PlayerItemBag(this));
//        this.setCardBag(new PlayerCardBag(this));

        // 玩家礼品
        this.playerGift = new PlayerGift(this);
        
        

        this.isPlaying = false;
        this.isLogin = false;
        this.isRobotState = false;

        
        pingStart = TimeUtil.getSysCurTimeMillis();
//        playerBuild = new PlayerBuild(this);        
//        playerCard = new PlayerCard(this);        
//        listGameCard = new ArrayList<Integer>();        
        playerTask = new PlayerTask(this);        
        playerReference = new PlayerReference(this);        
        msgBox = new PlayerMessagegBox(this);        
        dayActivity = new PlayerDayActivity(this);        
        playerAroudUser = new PlayerAroudUser(this);        
        userProperty = new UserProperty(this);
        
    }
    
    /**
     * 构建奴隶主对象
     * caosf
     * @return
     */
	public GamePlayer(int playerid, boolean isMaster) {
        this.userId = playerid;
        
	}

	public PacketLib getOut()
    {
        return out;
    }

    // 发包
    public void sendTcp(Packet pack)
    {
        if (out != null)
            out.sendTCP(pack);
    }
    
    /**
     * 玩家在游戏中聊天
     */
    public boolean onGameSendChat(int voiceId,String massge)
    {
        BaseRoom room = this.getCurrentRoom();
        if(null == room) {
        	return false;
        }
                
        Packet response = new Packet(UserCmdOutType.USER_CHAT);
        response.putInt(10);
        response.putInt(userId);
        response.putInt(voiceId);
        response.putStr("房间");
        response.putStr(playerCharacter.getUserName());
        response.putStr(massge);
	    //（1：为增加金币2：为皮肤（梦幻紫）3：对话框（绯红流云）4：为醒酒茶）
//	  	if(this.getPropBag().isExistAndIsUsedTypeEffect(PropType.CHAT_BUBBLE_TYPE)) {
//	  		response.putByte((byte)1);//对话框（绯红流云）
//	  	} else {
//	  		response.putByte((byte)0);//默认皮肤
//	  	}
        room.sendToAll(response);
        return true;
    }
    
    private int gameResultState = 0;
    
	public void setGameResultState(int gameResultState) {
		this.gameResultState = gameResultState;
	}

	public int getGameResultState() {
		return gameResultState;
	}

    public void beginAllChanges()
    {
        BeginChanges();
    }

    public void commitAllChanges()
    {
        CommitChanges();
    }

    /**
     * @param
     * @return
     * @exception
     */
    public void BeginChanges()
    {
        changeCount.incrementAndGet();
    }

    public void setChange(int value)
    {
        changeCount.set(value);
    }

    /**
     * @param
     * @return
     * @exception
     */
    public void CommitChanges()
    {
    }

    /**
     * 玩家退出时的处理
     */
    public void quit()
    {
    	if(!isLogin) {
    		return;
    	}
    	isLogin = false;
    	
    	System.out.println("quit==================" + playerCharacter.getUserName());
    	
        // cut off the network connection.
        if (session != null && !session.isOpen())
        {
        	try 
        	{
				session.close();
			} 
        	catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();				
			}
        }
			

        if (Config.getValue("open_trustee_swich").equals("1"))
        {
            try
            {
                playerCharacter.setOnline(false);                
                playerCharacter.setLastQiutDate(new Timestamp(System.currentTimeMillis()));              
//                System.out.println("player (" +playerCharacter.getUserName()+ ")	quite time:" + playerCharacter.getLastQiutDate());

                

                // when the player is still playing in the room ,Trustee action
                // added.
                if (currentRoom != null)
                {
                    RoomMgr.handlePlayerTrustee(currentRoom, this, true);
                    return;
                }

                saveIntoDatabase(true);
                
                /**
                 * 移除附近用户中的当前用户信息
                 */
                WorldMgr.removeAroundUserInfo(userId);  
                WorldMgr.removePlayer(userId);

                // 移除好友信息
                clearFriends();                                
                clearStages();
                
                // 移除奴隶关系信息
                clearUserReference();
                
                // 移除用户礼品信息
                clearPlayerGift();
                
                //保存玩家的活动记录
                dayActivity.updateOnQiut();
                
                //玩家更新玩家属性
                userProperty.clearAntiAddiction();
            }
            catch (Exception e)
            {
                logger.error("Player Quit Exception:" + e);
            }

        }
        else
        {
            try
            {
                if (currentRoom != null)
                {
                    RoomMgr.exitRoom(currentRoom, this, false, 0);
                }

                currentRoom = null;
                if (playerCharacter != null)
                {
                    playerCharacter.setOnline(false);
                    playerCharacter.setLastQiutDate(new Timestamp(System.currentTimeMillis()));
                    //System.out.println("玩家" +playerCharacter.getUserName()+ "退出，登录时间为" + playerCharacter.getLastLoginDate());
                    //System.out.println("玩家" +playerCharacter.getUserName()+ "退出，退出时间为" + playerCharacter.getLastQiutDate());
                    saveIntoDatabase(true);
                }
            }
            catch (Exception e)
            {
                logger.error("Player Quit Exception:" + e);
            }
            finally
            {
                /**
                 * 移除附近用户中的当前用户信息
                 */
                WorldMgr.removeAroundUserInfo(userId);

                WorldMgr.removePlayer(userId);

                // 移除好友信息
                clearFriends();
                
                clearStages();
                
                // 移除奴隶关系信息
                clearUserReference();
                
                // 移除用户礼品信息
                clearPlayerGift();
                
                //保存玩家的活动记录
                dayActivity.updateOnQiut();

                //玩家更新玩家属性
                userProperty.clearAntiAddiction();
            }
        }
    }

	/***
     * initialize player information in server,and send the message to
     * initialize client player.
     * 
     * @param isExist
     *            :player whether is exist.
     * @return
     */
    public boolean Login(boolean isExist)
    {
    	this.isLogin = true;
        try
        {
        	
        	System.out.println("Login==================" + playerCharacter.getUserName());
        	
//        	out.setShowCode(true);
            // if player exists,send login award,otherwise,loading player
            // information from database.
            if (isExist){
                out.sendLoginAward(AwardMgr.getLoginAwardCoins(playerCharacter));
                msgBox.loadDataFromDB();
            }
            else
                LoadFromDatabase();
            
            dayActivity.updateOnLogin();

            Timestamp time = TimeUtil.getSysteCurTime();
            playerCharacter.setLastLoginDate(time);
            addCharmValve(0, 0);
            checkTempCoin();
            
            System.out.println("player (" +playerCharacter.getUserName()+ ")	login time:" + playerCharacter.getLastLoginDate());
            //System.out.println("玩家" +playerCharacter.getUserName()+ "登录，退出时间为" + playerCharacter.getLastQiutDate());

                    
         // 发送登陆成功协议到客户端
            out.sendLoginSuccess(); 
            
            
            
//            getPlayerBuild().update();
//            getPlayerCard().updateDevelop();
            
//            out.sendBuild();
            out.sendStageCharmValve();
//            out.sendCardDevelop();
//            out.sendCardFactory();
            out.sendUpdateBaseInfo();
            out.sendUpdatePrivateInfo(playerCharacter, (byte)0);
            out.sendDrawState(10, 1);
            out.SendPingTime();
            
            
            PlayerTask playerTask = this.getPlayerTask();
            if (null != playerTask) {
            	List<UserTaskInfo> list = playerTask.getUserALLTasks();
            	//发送任务信息
            	out.sendUserCurrentTask(list, false);
            }
            
            //TODO 是否有私信
            if(msgBox.hasNewLetter())
            	 out.sendHasNoReadLetterTips();
            
            
            
            
            
            
            
            PlayerReference playerReference = this.getPlayerReference();
            if (null != playerReference) {
            	UserReferenceInfo masterInfo = playerReference.getMasterUserReferenceInfo();
            	int masterUserId = 0;
            	if (null != masterInfo) {
            		masterUserId = masterInfo.getMasterUserId();
            	} else {
//            		//System.out.println("通过masterUserReferenceId==查询出来的对象为null....有问题===");
            	}
            	//添加主人信息
//            	//System.out.println("登陆用户的主人id是masterUserId==" + masterUserId);
            	out.sendUserMasterInfo(masterUserId);
            }

            out.sendAntiAddiction(3,"");
            
            WorldMgr.addPlayer(userId, this);
            /**
             * 往全局的附近用户Map中添加此登录用户的信息
             */
            WorldMgr.addAroundPlayers(userId);
            
            
//            out.setShowCode(false);
            isFinishTask(TaskConditionType.LoginGame, 0, 0);
            
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            logger.error("send login success error");
        }

        return true;
    }

    private boolean LoadFromDatabase()
    {
        int userId = this.userId;
        PlayerInfo playerInfo = playerCharacter;
        if (playerInfo == null)
            return false;
        // 初始化最大醉酒度这个逻辑字段
        playerInfo.setDrunkLevelLimit(LevelMgr.getDrunkLevel(playerInfo.getLevel()));
        
        loadRewardAndAward();
        //查看连续登陆奖励
    	AwardMgr.awardLoginCoins(this);
    	AwardMgr.checkCoinsLessAwardCoins(this);
        // 登陆发奖
        out.sendLoginAward(AwardMgr.getLoginAwardCoins(playerInfo));

        
        //
        playerCharacter = playerInfo;
//        playerBuild.loadPlayerBuild();        
//        playerCard.loadPlayerCard();
        
        
        
        /******* 处理背包相关 *********/
        List<UserItemInfo> userItemList = ItemBussiness.getItemInfos(userId);
        List<BaseItem> baseItemList = new ArrayList<BaseItem>();
//        List<BaseItem> baseCardList = new ArrayList<BaseItem>();

        for (UserItemInfo userItemInfo : userItemList)
        { 
        	if(userItemInfo.getItemTypeId() == 1)
        	{
        		ItemTemplateInfo templateInfo = ItemMgr.findItemTemplate(userItemInfo.getTemplateId());
        		baseItemList.add(new BaseItem(userItemInfo, templateInfo, playerInfo));
        	}
//        	else if(userItemInfo.getItemTypeId() == 2)
//        	{
//        		
//        		int level = playerCard.getCardDeveopLevel(userItemInfo.getTemplateId());
//        		CardInfo cardInfo = CardMgr.findCardByType(userItemInfo.getTemplateId(),level);
//        		baseCardList.add(new BaseItem(userItemInfo, cardInfo, playerInfo));
//        	}
        }

        /******* end 处理背包相关 end *********/

        
        // 初始化背包物品
        propBag.loadItems(baseItemList);
//        cardBag.loadItems(baseCardList);
        
        
        
        
        //初始化用户任务模块
        List<UserTaskInfo> taskList = TaskBussiness.getUserYesterdayTasks(userId);
        if(null == taskList || taskList.size() <= 0) {//特别重要 csf
        	taskList = TaskBussiness.getAllUserTasks(userId);
        }
        
//        for(UserTaskInfo info : taskList)
//        {
//        	if(info.getId() > 1000)
//        		//System.out.println("LoadFromDatabase==================" + info.getId());        	
//        }
        
        checkVipInfo();
        
        // 关卡信息
//        initStagesInfo();
               
        
        // 添加好友信息
        initFriendsInfo(userId);
        
        playerTask.loadPlayerTask(taskList);
        //初始化用户奴隶模块
        playerReference.loadUserReferences();

        //初始化用户礼品模块
        playerGift.loadUserGifts();

        //初始化用户每日统计信息
        dayActivity.loadUserDayActivity();

        //初始化用户实名制防沉迷信息
        userProperty.loadUserProperty();
        
        //查看用户是否收到过礼品，没有则赠送
        SystemAwardMgr.awardUserGift(this);
        
        msgBox.loadDataFromDB();
        return true;
    }

    /**
     * 保存数据到数据库
     * 
     * @param all
     *            all为true保存所有,all为false只保存核心数据 money,背包,礼品
     * @return
     */
    public boolean saveIntoDatabase(boolean all)
    {
        try
        {
            if (all)
            {
                // 立即保存玩家的金钱和金币
                // PlayerBussiness.updateCoins(userId,
                // playerCharacter.getCoins());
                // PlayerBussiness.updateMoney(userId,
                // playerCharacter.getMoney());
                PlayerBussiness.updateAll(userId, playerCharacter);
            }
            propBag.saveIntoDb();
//            cardBag.saveIntoDb();
            playerGift.saveIntoDb();

            //添加任务入库代码
//            saveStage();
//            playerBuild.saveIntoDB();
//            playerCard.saveIntoDB();
            playerTask.saveIntoDB();
            msgBox.saveDataToDB();
            
            saveRewardAndAward();
            
            return true;
        }
        catch (Exception e)
        {
            logger.error("Error saving player " + playerCharacter.getUserName()
                    + "!", e);
            return false;
        }
    }

    public PlayerInfo getPlayerInfo()
    {
        return playerCharacter;
    }

    public void setPlayerInfo(PlayerInfo playerInfo)
    {
        playerCharacter = playerInfo;
        userId = playerInfo.getUserId();
    }

    public boolean canUseProp()
    {
        return true;
    }

    public int getUserId()
    {
        return userId;
    }

    public String toString()
    {
        return String.format("isPlayer:%s   Id:%d   nickname:%s  isPlayer:%s  hallId:%s  charmvalve:%s  coins:%s  money:%s  room:%s ",
        					 getPlayerInfo().getIsRobot(),
                             getPlayerInfo().getUserId(),
                             getPlayerInfo().getUserName(), 
                             isPlaying,
                             getCurrentHall() == null ? 0 : getCurrentHall().getHallId(),
                             getPlayerInfo().getCharmValve(),
                             getPlayerInfo().getCoins(),
                             getPlayerInfo().getMoney(),
                             getCurrentRoom() == null ? "NOT IN A ROOM"
                                     : getCurrentRoom().toString());
    }

    /**
     * 更新玩家的加解密密钥
     * 
     * @param newKey
     *            新密钥
     */
//    public void updateKey(int[] newKey)
//    {
//        int[] temp;
//
//        if (session != null)
//        {
//            temp = new int[newKey.length];
//            System.arraycopy(newKey, 0, temp, 0, newKey.length);
//            session.setAttribute(PacketStrictDecoder.class, temp);
//
//            temp = new int[newKey.length];
//            System.arraycopy(newKey, 0, temp, 0, newKey.length);
//            session.setAttribute(PacketStrictEncoder.class, temp);
//        }
//    }

    public void clearBag()
    {

    }

    public void disconnect()
    {
        quit();
    }

    public void sendMessage(MessageType type, String message)
    {
        Packet packet = new Packet(0);
        packet.putByte(type.getValue());
        packet.putStr(message);
        sendTcp(packet);
    }

    /**
     * @return mina登录会话
     */
    public Session getSession()
    {
        return session;
    }

    /**
     * @param session
     *            mina登录会话
     */
    public void setSession(Session session)
    {
        this.session = session;
    }

    /**
     * @param currentRoom
     * 
     */
    public void setCurrentRoom(BaseRoom currentRoom)
    {
    	if(currentRoom == null)
    		System.out.println("setCurrentRoom=========null======");
    	
        this.currentRoom = currentRoom;
    }

    public SelfDrivenRunnableQueue<UserCmdWrapper> getCmdQueue()
    {
        return cmdQueue;
    }

    public void onGameOver(BaseGame game, boolean isWin, int gainGP)
    {

    }

    /**
     * 返回总场次
     */

    public int getTotal()
    {
        return playerCharacter.getLose() + playerCharacter.getWin();
    }

    /**
     * 失败场次添加
     */

    public int addLose()
    {
        playerCharacter.setLose(playerCharacter.getLose() + 1);
        playerCharacter.setOp(Option.UPDATE);
        return playerCharacter.getLose();
    }

    /**
     * 胜利场次添加
     */

    public int addWin()
    {
        playerCharacter.setWin(playerCharacter.getWin() + 1);
        playerCharacter.setOp(Option.UPDATE);
        return playerCharacter.getWin();
    }

    public int addTotal()
    {
        playerCharacter.setTotal(playerCharacter.getTotal() + 1);
        playerCharacter.setOp(Option.UPDATE);
        return playerCharacter.getTotal();
    }

    /**
     * 玩家增加金钱
     * @param money 增加值
     * @param isNeed 是否需要考虑防沉迷
     * @return
     */
    public int addCoins(int coins, boolean isNeed)
    {
    	if(null != userProperty && isNeed && coins > 0) {//防沉迷
    		
    		coins = (coins * userProperty.getIncomeRatio()) / 100;    		
    		isFinishTask(TaskConditionType.WinCoin, 0, 0);
    		
    	}
        if (playerCharacter.getCoins() + coins < 0)
        {
            playerCharacter.setCoins(0);
            playerCharacter.setOp(Option.UPDATE);
            return 0;
        }

        if(coins != 0)
        {
        	playerCharacter.setCoins(playerCharacter.getCoins() + coins);
        	playerCharacter.setLevel(LevelMgr.getLevel(playerCharacter));
            playerCharacter.setOp(Option.UPDATE);
            
            this.getOut().sendUpdateBaseInfo();
        }
                
        return playerCharacter.getCoins();
    }

    /**
     * 玩家金钱设置
     */
    public int addCoins(int coins)
    {
    	return addCoins(coins, true);
    }
    
    public int updateTempCoin(int hallCoin, int forcedExitCoins)
    {
    	
    	if(this.getPlayerInfo().getTempCoins() < forcedExitCoins && this.getPlayerInfo().getCoins() > 0)
    	{
    		int addCoins = hallCoin - this.getPlayerInfo().getTempCoins();
    		if(addCoins >  this.getPlayerInfo().getCoins())
    			addCoins = this.getPlayerInfo().getCoins();
    		
    		this.getPlayerInfo().setTempCoins(this.getPlayerInfo().getTempCoins() + addCoins);
    		this.addCoins(addCoins*-1);    	
    		 		    		
//    		System.out.println("updateTempCoin==================2=======" + this.getUserId() + "========" + this.getPlayerInfo().getTempCoins());
    		
    	}
    	
    	return this.getPlayerInfo().getTempCoins();    	
    }
    
    public int addTempCoins(int coins)
    {
    	if (playerCharacter.getTempCoins() + coins < 0)
        {
            playerCharacter.setTempCoins(0);
            playerCharacter.setOp(Option.UPDATE);
            return 0;
        }

        if(coins != 0)
        {
        	playerCharacter.setTempCoins(playerCharacter.getTempCoins() + coins);
            playerCharacter.setOp(Option.UPDATE);
        }
        
        return playerCharacter.getTempCoins();
    }
    
    public void checkTempCoin()
    {
    	if(playerCharacter.getTempCoins() > 0)
    	{
    		this.addCoins(playerCharacter.getTempCoins());   
    		playerCharacter.setTempCoins(0);
    	}
    }
    
    
    public void addPengYouCoin(int addCoin)
    {
    	playerCharacter.setPengYouCoins(playerCharacter.getPengYouCoins() + addCoin);
    }
    
    /**
     * 玩家魅力值设置
     */

    public int addCharmValve(int charmValve, int stageId)
    {
    	if(charmValve == 0)
    		return 0;
    	   	
    	
    	if(null == playerCharacter) {
    		playerCharacter = PlayerBussiness.getPlayerInfoById(userId);
    	}
    	
    	
    	int stageCharmValve = 0;   
    	if(stageId > 0)
    	{
    		PlayerStage currentStage =  stages.get(stageId);
        	
        	if(currentStage == null)
        	{
        		currentStage = new PlayerStage(getUserId(),stageId,0);        		       	
            	stages.put(stageId, currentStage);
//            	System.out.println("StagesInfo==========222=====1=========" + getUserId() + "=========" + stageId);
        	}
        	
        	int addCharmValve = (currentStage.getCharmValve() + charmValve) < 0 ? 0 : charmValve;
        	currentStage.setCharmValve(addCharmValve);
        	
        	stageCharmValve = currentStage.getCharmValve();
        	out.sendStageCharmValve(stageId, stageCharmValve);
    	}
    	
    	

    	
    	
    	if(charmValve != 0)
    		isFinishTask(TaskConditionType.CharmValue, 0, playerCharacter.getCharmValve());
    	
        playerCharacter.setOp(Option.UPDATE);
        return stageCharmValve;
    }

    
    
    
    
    
    /**
     * 醉酒度的设置
     */

    public int addDrunkLevel(int level)
    {
    	
    	if(currentHallGameType == HallGameType.CONTEST)
    	{
    		 if (playerCharacter.getDrunkLevelContest() + level < 0)
    	        {
    	            playerCharacter.setDrunkLevelContest(0);
    	            playerCharacter.setOp(Option.UPDATE);
    	            return 0;
    	        }
    	        else if (playerCharacter.getDrunkLevelContest() + level > playerCharacter.getDrunkLevelLimit())
    	        {
    	            // 醉酒度满时，设置处理
    	            playerCharacter.setDrunkLevelContest(playerCharacter.getDrunkLevelLimit());
    	            return playerCharacter.getDrunkLevelLimit();
    	        }

    	        playerCharacter.setDrunkLevelContest(playerCharacter.getDrunkLevelContest() + level);
    	        playerCharacter.setOp(Option.UPDATE);
    	        return playerCharacter.getDrunkLevelContest();
    	}    		
    	else
    	{
    		 if (playerCharacter.getDrunkLevelSocial() + level < 0)
    	        {
    	            playerCharacter.setDrunkLevelSocial(0);
    	            playerCharacter.setOp(Option.UPDATE);
    	            return 0;
    	        }
    	        else if (playerCharacter.getDrunkLevelSocial() + level > playerCharacter.getDrunkLevelLimit())
    	        {
    	            // 醉酒度满时，设置处理
    	            playerCharacter.setDrunkLevelSocial(playerCharacter.getDrunkLevelLimit());
    	            return playerCharacter.getDrunkLevelLimit();
    	        }

    	        playerCharacter.setDrunkLevelSocial(playerCharacter.getDrunkLevelSocial() + level);
    	        playerCharacter.setOp(Option.UPDATE);
    	        return playerCharacter.getDrunkLevelSocial();
    	}
    	
    }

    public int getDrunkLevel()
    {
    	if(currentHallGameType == HallGameType.CONTEST)
    		return playerCharacter.getDrunkLevelContest();
    	else
    		return playerCharacter.getDrunkLevelSocial();
    }

    
    /**
     * 得到玩家在房间里的位置
     * 
     * @return
     */
    public int getCurrentRoomPos()
    {
        return currentRoomPos;
    }

    /**
     * 设置玩家在房间里的位置
     * 
     * @param currentRoomPos
     */
    public void setCurrentRoomPos(int currentRoomPos)
    {
        this.currentRoomPos = currentRoomPos;
    }

    public int addGpDirect(int gp)
    {
    	if(isRobot) {//机器人经验加的慢一些
    		gp = gp / 3;
    	} else if(null != userProperty && gp > 0) {//防沉迷
    		gp = (gp * userProperty.getIncomeRatio()) / 100;
    	}
    	
        // 未处理 gp 过大等边界问题
    	int maxLevel = LevelMgr.getMaxLevel();
        if (gp >= 0 && playerCharacter.getLevel() < maxLevel)//避免玩家等级过高
        {
            playerCharacter.setGp(playerCharacter.getGp() + gp);
            if (playerCharacter.getGp() < 1)
                playerCharacter.setGp(1);
//            setLevel(LevelMgr.getLevel(playerCharacter.getGp()));
            // UpdateFightPower();
            // OnPropertiesChanged(PlayerProperties.GP, gp); 属性改变,
            // 可在这个方法调用里处理成就系统
            // if (gp > 0)
            // {
            // // 此处是否需要立即保存入库?
            // PlayerBussiness.updatePlayerGpLevel(userId,
            // playerCharacter.getGp(),
            // playerCharacter.getLevel());
            // }
            playerCharacter.setOp(Option.UPDATE);
        } else {
        	gp = 0;
        }
        if(playerCharacter.getLevel() >= maxLevel) {
        	playerCharacter.setGp(LevelMgr.getGp(maxLevel) - 1);//注意一定要小点
        }
        return gp;
    }

    /**
     * 处理等级变化的相关事件, 可在这里唤醒对玩家等级进行监听的所有监听者
     * 
     * @param value
     */
    public void setLevel(int newLevel)
    {
        int oldLevel = playerCharacter.getLevel();
        int drunkValueContest = playerCharacter.getDrunkLevelLimit()
                - playerCharacter.getDrunkLevelContest();
        int drunkValueSocial = playerCharacter.getDrunkLevelLimit()
                - playerCharacter.getDrunkLevelSocial();
        if (newLevel != oldLevel)
        {
//            if (newLevel > oldLevel)
//            {
                playerCharacter.setLevel(newLevel);
                int newDrunkLimit = LevelMgr.getDrunkLevel(newLevel);
                playerCharacter.setDrunkLevelContest(newDrunkLimit - drunkValueContest);
                playerCharacter.setDrunkLevelSocial(newDrunkLimit - drunkValueSocial);
                playerCharacter.setDrunkLevelLimit(newDrunkLimit);
//                saveIntoDatabase(true);
                for (int i = oldLevel + 1; i <= newLevel; i++)
                    onLevelUp(i); // 触发事件撒
//            }
        }
    }

    /**
     * 用户升级<客户端触发>
     * 
     * @param grade
     */
    private void onLevelUp(int grade)
    {
        // PlayerEventArgs param = new PlayerEventArgs(this,
        // GamePlayerEventType.PlayerValueEvent, grade);
        // gamePlayerEvent.notifyListener(GamePlayerEventType.PlayerValueEvent,
        // param);
    }

    public int addMoney(int money)
    {

        if (playerCharacter.getMoney() + money < 0)
        {
            playerCharacter.setMoney(0);
            playerCharacter.setOp(Option.UPDATE);
            return 0;
        }
        
//        if(money < 0)
//        	isFinishTask(TaskConditionType.UseMoney, money, 0);

        playerCharacter.setMoney(playerCharacter.getMoney() + money);
        playerCharacter.setOp(Option.UPDATE);
        return playerCharacter.getMoney();
        
    }
    
    
    public void addVip(int day)
    {

    	long vipTime = (long)day;
    	vipTime *= 86400000;
        if(playerCharacter.getVipLevel() > 0)
        {
        	Timestamp timeEnd = new Timestamp(playerCharacter.getVipDate().getTime() +  vipTime);
        	playerCharacter.setVipDate(timeEnd);
        }
        else
        {
        	Timestamp timeNow = TimeUtil.getSysteCurTime();
    		Timestamp timeEnd = new Timestamp(timeNow.getTime() + vipTime);
    		
    		getPlayerInfo().setVipLevel(1);
    		getPlayerInfo().setVipDate(timeEnd);
        }
        
        isFinishTask(TaskConditionType.LoginGame, 0, 0);
    }
    
    
    public int removeMoney(int money, int coins) // 扣除的可以合并起来
    {
        int result = 0;
        if (money > 0 && money <= playerCharacter.getMoney())
        {
            playerCharacter.setMoney(playerCharacter.getMoney() - money);
            result += money;
        }
        if (coins > 0 && coins <= playerCharacter.getCoins())
        {
        	this.addCoins(coins*-1);
//            playerCharacter.setCoins(playerCharacter.getCoins() - coins);
            playerCharacter.setBuyItemCoins(playerCharacter.getBuyItemCoins() + coins);
            
            result += coins;
        }
        
        if(money != 0)
        	isFinishTask(TaskConditionType.UseMoney, money, 0);
        
        logger.debug("【removeMoney】" + "[money:" + money + "] [coins:" + coins + "] [result:" + result + "]");
        
        if (result > 0) {// 考虑只成功了 money 或者 只成功了 coins 的情况
        	saveIntoDatabase(true);
        }
        return result;
    }
    
    
    public int addCoinByMoney(int coins) // 扣除的可以合并起来
    {
    	
    	int ExchangeRate = Integer.parseInt(Config.getValue("money_coin_exchangerate"));
    	if(ExchangeRate == 0)
    	{
    		logger.error("ExchangeRate is Invilad");
    		return 0;
    	}
    	
    	
    	int money = coins / ExchangeRate;
    	if((coins % ExchangeRate) > 0)
    	{
    		money += 1;
    		coins += ExchangeRate - coins;
    	}
    	
    	
    	boolean result = false;
        if (money > 0 && money <= playerCharacter.getMoney())
        {
            playerCharacter.setMoney(playerCharacter.getMoney() - money);
            playerCharacter.setCoins(playerCharacter.getCoins() + coins);
            
            result = true;
        }
        
        logger.debug("【addCoinByMoney】" + "[money:" + money + "] [coins:" + coins + "] [result:" + result + "]");
        
        if (result) // 考虑只成功了 money 或者 只成功了 coins 的情况
        	saveIntoDatabase(true);
        else
        	money = 0;
        
        return money;
    }
    
    
    /**
     * 初始化好友信息 一次性取所有好友并置于内存，避免查看好友详情和好友翻页时的多余数据库IO及连接消耗.
     * 注意：需要在用户增加及删除好友时更新内存中的值.
     * 
     * @param userId
     */
    private void initFriendsInfo(int userId)
    {
        List<FriendInfo> friends = FriendsBussiness.getAllFriendsList(userId);
        for (FriendInfo info : friends)
        {
            friendsAdd(info.getFriendId(), info);
        }
    }

     public void initStagesInfo()
     {
    	 List<PlayerStage> stageList = StageBussiness.getUserStegesInfo(userId);
         for (PlayerStage info : stageList)
         {
             stageAdd(info.getStageId(), info);
             
//             System.out.println("StagesInfo==========111==============" + info.getUserId() + "=========" + info.getStageId());
         }
     }
    
     public void checkVipInfo()
     {
    	 Timestamp timeNow = TimeUtil.getSysteCurTime();
    	 
    	 if(playerCharacter.getVipDate() != null && timeNow.after(playerCharacter.getVipDate()))
    		 playerCharacter.setVipLevel(0);
     }
     
    /**
     * 取好友分页列表信息
     * 
     * @param pageNo
     * @param pageSize
     * @return
     */
    public List<FriendInfo> getPagedList(int pageNo, int pageSize)
    {
        List<FriendInfo> pagedFriends = new ArrayList<FriendInfo>();

        if (null != friends && friends.size() > 0)
        {
            // 至少大于多少条记录才能完成此次分页
            int minRecords = (pageNo - 1) * pageSize;

            List<Map.Entry<Integer, FriendInfo>> list = new ArrayList<Map.Entry<Integer, FriendInfo>>(
                    friends.entrySet());
            int friendSize = friends.size();
            if (friendSize > minRecords)
            {
                // 取得开始和结束位置，为了是玩家的好友按照添加时间来倒序
                int startIndex = friendSize - (pageNo - 1) * pageSize;
                int endIndex = friendSize - (list.size() < (pageNo * pageSize) ? list.size()
                        : pageNo * pageSize);

                for (int i = startIndex - 1; i > endIndex - 1; i--)
                {
                    pagedFriends.add(0, list.get(i).getValue());
                }
            }
        }
        return pagedFriends;
    }

    /**
     * 取得好友列表总页数
     * 
     * @param pageSize
     * @return
     */
    public int getTotalPage(int pageSize)
    {
        int totalPage = 0;
        int totalRecords = this.friends.size();
        if (totalRecords > 0)
        {
            if (totalRecords % pageSize == 0)
            {
                totalPage = totalRecords / pageSize;
            }
            else
            {
                totalPage = (totalRecords / pageSize) + 1;
            }
        }
        return totalPage;
    }

    public void clearFriends()
    {
        this.friends.clear();
    }
    
    public void clearStages()
    {
        this.stages.clear();
    }
    
    private void clearUserReference()
    {
    	playerReference.clearUserReference();
	}
    
    private void clearPlayerGift()
    {
    	playerGift.clearPlayerGift();
	}

    /**
     * 好友信息每页记录条数
     * 
     * @return
     */
    public int getFriendPageSize()
    {
        return 20;
    }

    public boolean isFullDrunk()
    {
        return getDrunkLevel() == 0; // >=
        // playerCharacter.getDrunkLevelLimit();
    }

    /**
     * 玩家在游戏中醉酒的处理, 现在为道具的提示购买或提示使用
     */
    public void processDrunk()
    {
        // 是否有道具可用
        // 提示使用道具(以后可以扩展为其他道具的提示)
        boolean hasProp = propBag.getPropCountByType(PropType.WAKE_TYPE) > 0 ? true
                : false;
//        logger.error("【processDrunk】" + hasProp);
        // if (currentRoom != null && currentRoom.removePlayer(this)) // T出房间
        out.sendDrunkUseProp(this, PropType.WAKE_TEA, hasProp);
    }
    
    /**
     * 查看玩家的金币是否满足条件
     * 
     * @return
     */
    public boolean isCoinsEnough()
    {
    	boolean result = true;
    	
//    	result = trySendCoins();
//    	if(result){
//    		result = isCanStayRoom();
//    	}
    	result = isCanStayRoom();
    	
        return result;
    }
    /** 尝试给玩家送钱 如果玩家没钱可送 返回false  送了钱返回true*/
    public boolean trySendCoins(){
    	
     	if(AwardMgr.palyerCoinsLess(this)) {//需要系统送金币
    		return processCoinsLess();//登记并送金币
    	}
     	return true;
    }
    /** 是否可以留着当前房间*/
    public boolean isCanStayRoom() {
    	boolean result = true;
//    	if(null != currentHall && result) {//在玩家进了房间才判断
//    		result = currentHall.canStayHere(this);//返回是否满足条件
//    	}
    	if(null != currentRoom && result) {
    		result = currentRoom.canStayHere(this);
    	}
    	
    	
    	
    	return result;
    }

    public boolean processCoinsLess()
    {
    	boolean haveAward = AwardMgr.palyerCoinsLessAwardCoins(this);
//        logger.error("【processCoinsLess】" + (currentRoom != null));
        return haveAward;
    }

//    public void coinsLessKill(int type)
//    {
//        if (currentRoom != null && currentRoom.removePlayer(this))
//        {        	
//        	String msg = "";
//        	if(type == 1)
//        		msg = LanguageMgr.getTranslation("CityWar.Ref.ExitRoom");
//        	else
//        		msg = LanguageMgr.getTranslation("CityWar.StayRoom.NoEnoughCoin");
//        	
//        	getOut().sendRoomReturnMessage(false,msg);
//        	
////            logger.error("[GamePlayer - processCoinLess()] : remove player - " + this.getPlayerInfo().getUserName());
//        }
//    }

    /**
     * after player login,when the game is still playing,player enter the
     * trusteed room.
     */
    public void sendEnterTrusteedRoom()
    {
        if (getCurrentRoom() != null)
        {
            getOut().sendEnterRoom(getCurrentRoom().getRoomId());
            // notify client to create a room instance.

            byte createState = 0;//0表示成功
    		getOut().sendRoomCreate(createState, getCurrentRoom(), UserCmdOutType.GAME_ROOM_CREATE);
            getCurrentRoom().sendRoomInformation(this);
        }
        else
            return;

        BaseGame game = getCurrentRoom().getGame();
        if (game != null)
        {
            // update player's game state.
            game.sendUpdatePlayerState();
           
            
            // if game is playing,new player will obtain game message.
            if (game.getCurrentTurnPlayer() != null && game.getGameState().isInPlay())
            {
            	game.SendGameContinue(this);
                game.sendGameNextTurn(game.getCurrentTurnPlayer(),
                                                   game);
            }
            	 
        }
    }

    /**
     * 玩家使用道具效果
     * @param itemTypeOruserItemId 道具类型ID或者用户的道具ID
     * @param useCount 使用的数量
     */
    public void updatePropUse(int itemTypeOruserItemId, int useCount)
    {
    	for(int i=0;i<useCount;i++) {
            // 广播道具使用结果
            if (currentRoom != null)
            {
            	Packet pkg = out.brocastPorpUseByType(this, itemTypeOruserItemId);
                if(null != pkg) {
                	currentRoom.sendToAll(pkg);
                }
            }
    	}
    }
    /**
     * 玩家赠送礼物
     * 
     * @param receiverId 接受者的ID
     * @param sendCount  赠送数量
     * @param giftId     赠品ID 或者 赠品模板Id
     * @return true 赠送成功   false 赠送失败 
     */
	public boolean sendGift(int receiverId, int sendCount, int giftId) {

		boolean isSuccess = false;
		if (getPropBag().removeCount(getPropBag().getBaseItemByItemId(giftId),sendCount)) 
		{
			if (RobotMgr.isRobotByID(receiverId))// 机器人收到礼物
			{
				return sendGiftToRob( receiverId,  sendCount,  giftId);
			} else {
				GamePlayer receiver = WorldMgr.getPlayerByID(receiverId);

				if (receiver == null) // 接受者不在线
				{
					UserItemInfo info = new UserItemInfo();
				    info.setUserId(receiverId);
				    info.setItemTypeId(1);
		            info.setCount((short)sendCount);
		            info.setTemplateId(giftId);
		            info.setExist(true);
		            isSuccess =  ItemBussiness.addUserItem(info);

				} else// 接受者在线
				{
					receiver.getPropBag().addCount( receiver.getPropBag().getBaseItemByItemId(giftId),sendCount);
					isSuccess =  true;
				}
				
				//开始的时候扣了钱  如果失败 钱要补回来
				if(!isSuccess){
					getPropBag().addCount(getPropBag().getBaseItemByItemId(giftId),sendCount);
				}else{
					out.sendUpdateBaseInfo();
					receiver.out.sendUpdateBaseInfo();
				}
				
				return isSuccess;
			}
		} else {
			// TODO 赠送失败 你没有足够的礼物赠送
			return false;
		}
	}

	/**
	 * 机器人收到礼物
	 * 
	 * @return
	 */
    private boolean sendGiftToRob(int robId, int sendCount, int giftId){
    	
    	return false;
    }
	
    
    
    
    
    /**
     * 用户是否完成了任务
     * @param isCallOne--是否是斋局：0-无条件,1-斋局,2-非斋局.特殊情况:3-完善信息任务 4-加好友任务5-邀请好友玩游戏6-获得奴隶，7-完成建筑，8-金杯数量，9-充值，12-研究室研究，13-工厂生产
     * @param gameResult--用户这局游戏的结果：3胜利，1失败，2平局（与自己无关）
     * @return
     */
    public void isFinishTask(int type, int isCallOne, int gameResult) {
    	
    	//System.out.println("isFinishTask" + "========" + this.getPlayerInfo().getUserId() + "===" + type + "===" + isCallOne + "===" + gameResult);
    	this.getPlayerTask().onTaskUpdate(type, isCallOne, gameResult);
    	return;
    	
    
    }
    /**
     * 保存玩家的奖励信息
     */
    public void saveRewardAndAward() {
    	if(userReward != null) {
        	if(userReward.getOp() == Option.INSERT){
        		UserRewardBussiness.insertEntity(userReward);
        	}else if(userReward.getOp() == Option.UPDATE) {
        		UserRewardBussiness.UpdateEntity(userReward);
        	}
        	userReward.setOp(Option.NONE);
    	}
    	if(userAward != null) {
        	if(userAward.getOp() == Option.INSERT){
        		UserAwardBussiness.insertUserAward(userAward);
        	}else if(userAward.getOp() == Option.UPDATE) {
        		UserAwardBussiness.updateUserAward(userAward);
        	}
        	userAward.setOp(Option.NONE);
    	}
    }
    
    /**
     * 加载玩家的奖励信息
     */
    public void loadRewardAndAward(){
    	userReward = UserRewardBussiness.selectByUserId(userId);
    	if(userReward == null) {//如果没有则，创建
    		userReward = new UserReward();
    		userReward.setUserId(userId);
    		userReward.setAssessFlag(0);
            userReward.setShareFlag(0);
    		userReward.setLoginAward(0);
    		userReward.setDrawGameCount(0);
    		userReward.setDayDraw(0);
			userReward.setOp(Option.INSERT);
    	}
    	List<UserAwardInfo> list = UserAwardBussiness.selectUserDayCoinsLessAward(userId, 1);
    	UserAwardInfo info = null;//
    	if(null != list && list.size() > 0) {
    		info = list.get(0);
    	}
    	setUserAward(info);
    }
    
    
    /**
     * 保存玩家的奖励信息
     */
//    public void saveStage() {
//    	List<PlayerStage> updateStages = new ArrayList<PlayerStage>();
//        List<PlayerStage> insertStages = new ArrayList<PlayerStage>();
//        synchronized (lockStage)
//        {
//            for (PlayerStage info : stages.values())
//            {
//                // TODO 可处理逻辑
//                if (info == null)
//                    continue;
//                
//                switch (info.getOp())
//                {
//                    case Option.INSERT:
//                    	insertStages.add(info);
//                        break;
//
//                    case Option.UPDATE:
//                    	updateStages.add(info);
//                        break;
//                    default:
//                        break;
//                }
//                info.setOp(Option.NONE);
//            }
//        }
//        StageBussiness.updateStages(updateStages);
//        StageBussiness.addStages(insertStages);
//    }
    
    

	public boolean usePropItemByItemId(int itemId, int count) {
		if(null != this.getPropBag() && null != this.getPropBag().getBaseItemByItemId(itemId)
    			&& null != this.getPropBag().getBaseItemByItemId(itemId).getItemTemplateInfo()) {
    		int itemType = this.getPropBag().getBaseItemByItemId(itemId).getItemTemplateInfo().getType();
    		dayActivity.recordUsePropItemByType(itemType, count);
		}
		boolean useSucceed = getPropBag().usePropItemByItemId(itemId ,count);
        // 更新玩家自身属性
        out.sendUpdateBaseInfo();
		return useSucceed;
	}

	/**
	 * 玩家使用道具
	 * @param type
	 * @param count
	 * @return
	 */
	public boolean usePropItemByType(int type, int count) {
		dayActivity.recordUsePropItemByType(type, count);
		boolean useSucceed = getPropBag().usePropItemByType(type, count);
		
        // 更新玩家自身属性
        out.sendUpdateBaseInfo();
		return useSucceed;
	}
	


	public UserReward getUserReward() {
		return userReward;
	}

	public void setUserReward(UserReward userReward) {
		this.userReward = userReward;
	}

	public UserAwardInfo getUserAward() {
		return userAward;
	}

	public void setUserAward(UserAwardInfo userAward) {
		this.userAward = userAward;
	}

	/**
	 * 检查玩家使用道具，并发送协议
	 */

	public void sendMessgageAndOperate(short operateMgrCode) {
		MessgageOperateMgr.sendMessgageAndOperate(this, operateMgrCode);
	}

	public PlayerDayActivity getDayActivity() {
		return dayActivity;
	}

	public void setDayActivity(PlayerDayActivity dayActivity) {
		this.dayActivity = dayActivity;
	}

	public PlayerAroudUser getPlayerAroudUser() {
		return playerAroudUser;
	}

	public void setPlayerAroudUser(PlayerAroudUser playerAroudUser) {
		this.playerAroudUser = playerAroudUser;
	}

	public UserProperty getUserProperty() {
		return userProperty;
	}

	public void setUserProperty(UserProperty userProperty) {
		this.userProperty = userProperty;
	}

	public boolean isFeeVesion() {
		return isFeeVesion;
	}

	public void setFeeVesion(boolean isFeeVesion) {
		this.isFeeVesion = isFeeVesion;
	}




	public boolean isOpenEye() {
		return isOpenEye;
	}

	public void setOpenEye(boolean isOpenEye) {
		this.isOpenEye = isOpenEye;
	}
	
	
	public String getUserKey() {
		return UserKey;
	}

	public void setUserKey(String UserKey) {
		this.UserKey = UserKey;
	}
	
	
}
