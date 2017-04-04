/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.packet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.citywar.common.BaseItem;
import com.citywar.dice.entity.DrawGoodInfo;
import com.citywar.dice.entity.FriendInfo;
import com.citywar.dice.entity.ItemTemplateInfo;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.PlayerStage;
import com.citywar.dice.entity.TaskPrize;
import com.citywar.dice.entity.UserLetter;
import com.citywar.dice.entity.UserReferenceInfo;
import com.citywar.dice.entity.UserReward;
import com.citywar.dice.entity.UserTaskInfo;
import com.citywar.game.GamePlayer;
import com.citywar.game.Player;
import com.citywar.gameutil.UserProperty;
import com.citywar.manager.AwardMgr;
import com.citywar.manager.DrawMgr;
import com.citywar.manager.ItemMgr;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.ReferenceMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.RoomMgr;
import com.citywar.manager.TaskMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.MessageType;
import com.citywar.type.PropType;
import com.citywar.type.UserCmdOutType;
import com.citywar.util.DistanceUtil;
import com.citywar.util.HeadPicUtil;
import com.citywar.util.TimeUtil;

/**
 * GS向客户端下发的各种数据包
 * 
 * @author : Cookie
 * @date : 2011-5-4
 * @version
 * 
 */
public class PacketLib
{
    private GamePlayer user;

    private final Object sendLock = new Object();

    public PacketLib(GamePlayer player)
    {
        this.user = player;
    }


    public void sendTCP(Packet packet)
    {
    	
    	    		
        if (user.getSession() != null && user.getSession().isOpen())
        {
        	try 
        	{        		
	            synchronized (sendLock)
	            {
	                packet.clearCheckSum();              
	                user.getSession().getBasicRemote().sendText(packet.getJsonDate());				
	            }
        	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    
    /**
     * 发送用户消息
     * 
     * @param msgType
     *            消息类型
     * @param message
     *            消息内容
     */
    public Packet SendMessage(MessageType msgType, String message)
    {
        // Packet pkg = new Packet(UserCmdOutType.SYS_MESS);
        // pkg.putInt((int) msgType.getValue());
        // pkg.putStr(message);
        // sendTCP(pkg);
        //
        // return pkg;
        return null;
    }

    /**
     * 更新人物信息
     * 
     * @param info
     * @return
     */
    public void SendUpdatePublicPlayer(boolean isExsit, PlayerInfo info)
    {
        Packet pkg = new Packet(UserCmdOutType.USER_UPDATE);
        pkg.putStr(info.getAccount());
        pkg.putStr(info.getUserPwd());
        pkg.putStr(info.getUserName());
        pkg.putByte(info.getSex());
        //增加头像信息返回值
        pkg.putStr(info.getRealPicPath());
        //相册包含头像的信息
        List<String> list = info.getPicPathList();
        boolean isNotEmpty = !info.getRealPicPath().isEmpty();
        if (null != list && list.size() > 0) 
        {
        	byte sum = (byte)list.size();
        	if (isNotEmpty)
        	{
        		sum++;
        	}
        	pkg.putByte(sum);
        	if (isNotEmpty)
        	{
        		pkg.putStr(info.getRealPicPath());
        	}
        	for (String s : list)
        	{
        		pkg.putStr(info.getRealPicPath(s));
        	}
        } 
        else if (isNotEmpty)
        {
        	pkg.putByte((byte)1);
        	pkg.putStr(info.getRealPicPath());
        }
        else 
        {
        	pkg.putByte((byte)0);
        }
        sendTCP(pkg);
    }
    
    

    /**
     * 发送登陆成功消息 ，客户端收到此消息进入主界面
     */
    public void sendLoginSuccess()
    {
        if (user == null)
            return;
        Packet pkg = new Packet(UserCmdOutType.LOGIN_ACK);
        pkg.putByte((byte) 1);
        PlayerInfo info = user.getPlayerInfo();
        pkg.putInt(info.getUserId());
        pkg.putStr(info.getUserName());
        pkg.putStr(info.getCity());
        pkg.putByte(info.getSex());
        pkg.putInt(LevelMgr.getUserUpgradeGp(info)); // 玩家当前升级经验
        pkg.putInt(LevelMgr.getUpgradeGp(info.getLevel()));//玩家升级经验
        byte realState = 0;
        if(null != user.getUserProperty()) {
            boolean isNeedReal = user.getUserProperty().isNeedReal();
            if(isNeedReal) {//是否需要实名制
            	realState = 1;
            }
        }
        pkg.putByte((byte)realState);
        int feeState = 0;
        if(user.isFeeVesion() ){//如果用户是收费版 并且没有领过礼包  && !GiftBagMgr.checkUserVIPEditionIsHave(user.getUserVIPEdition())
        	
//    	   if( info.isTempAccount())//提醒用户登录领取大礼包
//           {
//               feeState = 1;
//           }
//           else if(!info.isTempAccount())//给用户发送大礼包
//           {
        	   feeState = 2;
//           }
        }
        pkg.putByte((byte)feeState);
        byte playerType = (byte)(user.getPlayerInfo().getUserType());//1为会员 0 为临时账号
        pkg.putByte(playerType);
        
        //
        // // 构建道具相关
        // List<BaseItem> propList = user.getPropBag().getAllItems();
        // pkg.putInt(propList.size()); // 几个类型的道具
        // for (BaseItem prop : propList)
        // {
        // ItemTemplateInfo template = prop.getItemTemplateInfo();
        // UserItemInfo userItem = prop.getUserItemInfo();
        // pkg.putInt(template.getTemplateId()); // 道具模板id
        // // pkg.putStr(template.getName()); // 道具名称
        // // pkg.putInt(template.getType()); // 道具付费类型
        // // pkg.putInt(template.getProperty()); // 属性值
        // pkg.putInt(userItem.getCount()); // 拥有此类型道具的数量
        // }
        sendTCP(pkg);
    }
    
    /** 
     * 发送实名验证信息 
     * 
     *@param type 1 实名验证成功  2 实名验证失败  3 
     * 
     * */
    public void sendAntiAddiction(int type,String desc){
    	
    	UserProperty property = user.getUserProperty();
    	if(property != null ){
    		long onlineTime = user.getDayActivity().getAccumulateOnlineTime();
    		
    		Packet pkg = new Packet(UserCmdOutType.USER_ONLINE_TIME);
    		pkg.putBoolean(property.isNeedReal());//是否需要实名制
    		pkg.putBoolean(property.isNeedAntiAddiction());//是否需要防沉迷
    		pkg.putDouble(onlineTime);//在线时长
    		pkg.putInt(property.getIncomeRatio());//在线收益百分比
    		pkg.putInt(type);//操作类型
    		pkg.putStr(desc);//描述信息
    		sendTCP(pkg);
    	}
    }

    public void sendLoginAward(int award)
    {
        if (user == null)
            return;
        Packet pkg = new Packet(UserCmdOutType.LOGIN_AWARD);
        PlayerInfo info = user.getPlayerInfo();
        pkg.putInt(info.getUserId());
        pkg.putInt(award);
        sendTCP(pkg);
    }
    
    
    public void sendBuild()
    {
        if (user == null)
            return;
        
        
//        PlayerBuild playerBuild = user.getPlayerBuild();
//        List<UserBuildInfo> buildList = playerBuild.getBuildList();
//        
//        
//        Packet pkg = new Packet(UserCmdOutType.USER_BUILD_INFO);
//        
//        if(buildList == null)
//        	pkg.putInt(0);
//        else
//        {
//	        pkg.putInt(buildList.size());
//	        for(UserBuildInfo build : buildList)
//	        {
//	        	pkg.putInt(build.getBuildTypeId());
//	        	pkg.putInt(build.getBuildLevel());
//	        	pkg.putInt(build.getBuildIsUpgrade());
//	        	
//	        	if(build.getBuildIsUpgrade() == 1)
//	        	{
//	        		Timestamp timeEnd = build.getBuildUpgradeTime();
//	        		long wait = timeEnd.getTime() - TimeUtil.getSysteCurTime().getTime();
//	        		pkg.putInt((int)(wait/1000));
//	        	}
//	        }
//        }
//        
//        sendTCP(pkg);
    }
    

    public void sendCardDevelop()
    {
        if (user == null)
            return;
        
        
//        PlayerCard playerCard = user.getPlayerCard();
//        Map<Integer,Integer> cardDevelopList = playerCard.getCardDeveopList();
//
//        
//        Packet pkg = new Packet(UserCmdOutType.USER_CARD_DEVELOP_INFO);
//        
//                 
//        if(playerCard.getUserCardInfo().getCardIsDevelop() == 1)
//        {
//        	pkg.putBoolean(true);
//        	pkg.putInt(playerCard.getUserCardInfo().getCardDevelopId());
//        }
//        else
//        {
//        	pkg.putBoolean(false);
//        	pkg.putInt(0);
//        }
//        
//        if(cardDevelopList == null)
//        	pkg.putInt(0);
//        else
//        {
//	        pkg.putInt(cardDevelopList.size());
//	        for (int key : cardDevelopList.keySet()) {
//	        	
//	        	pkg.putInt(key);
//	        	pkg.putInt(cardDevelopList.get(key));
//	        	
//	        }
//        }
//
//        sendTCP(pkg);
        
    }
    
    
    public void sendCardFactory()
    {
        if (user == null)
            return;
        
        
//        PlayerCard playerCard = user.getPlayerCard();
//        Map<Integer,Integer> cardFactoryList = playerCard.getCardFactory();
//
//        
//        Packet pkg = new Packet(UserCmdOutType.USER_CARD_FACTORY_INFO);
//        
//        if(playerCard.getUserCardInfo().getCardIsFactory() == 1)
//        {
//        	Timestamp timeBegin = playerCard.getUserCardInfo().getCardFactoryTime();
//        	int minute = (int)(TimeUtil.getSysteCurTime().getTime() - timeBegin.getTime()) / 1000;
//            pkg.putInt(minute);
//        }
//        else
//        {
//        	pkg.putInt(0);
//        }
//        
//        
//        
//        
//        if(cardFactoryList == null)
//        	pkg.putInt(0);
//        else
//        {
//	        pkg.putInt(cardFactoryList.size());
//	        for (int key : cardFactoryList.keySet()) {
//	        	
//	        	pkg.putInt(key);
//	        	pkg.putInt(cardFactoryList.get(key));
//	        	
//	        }
//        }
//
//        sendTCP(pkg);
    }
    
    public void sendCardUsed(int cardId)
    {
    	Packet pkg = new Packet(UserCmdOutType.USER_CARD_USE);
    	
    	pkg.putInt(1);
    	pkg.putInt(cardId);
    	    	
        sendTCP(pkg);
    }
    
    public void sendCardUsedList(List<Integer> usedCardList)
    {
    	Packet pkg = new Packet(UserCmdOutType.USER_CARD_USE);
    	
    	pkg.putInt(usedCardList.size());
    	for(Integer cardId:usedCardList)
    	{
    		pkg.putInt(cardId);
    	}
    	
        sendTCP(pkg);
    }
    /**
     * 用户登录成功时，发送好友列表信息到客户端.
     */
    public void SendFriendsList()
    {
        if (user == null)
        {
            return;
        }
        List<FriendInfo> friendInfos = user.getPagedList(1,
                                                         user.getFriendPageSize());
        if (null != friendInfos)
        {
            int totalPage = user.getTotalPage(user.getFriendPageSize());
            int pageSize = (Integer) friendInfos.size();

            double[] userPos = DistanceUtil.getLatAndLon(user.getPlayerInfo().getPos());

            Packet pkg = new Packet(UserCmdOutType.FRIENDS_LOAD_RESP);
            pkg.putInt(totalPage);
            pkg.putInt(pageSize);
            pkg.putInt(1);
            for (FriendInfo info : friendInfos)
            {
                double[] pos = DistanceUtil.getLatAndLon(info.getFriendPos());
                double dis = DistanceUtil.distanceByLatLon(userPos[0],
                                                           userPos[1], pos[0],
                                                           pos[1]);

                pkg.putInt(info.getFriendId());
                String picPath = "";
				if (!info.getFriendPicPath().isEmpty()) {
					picPath = HeadPicUtil.getSmallPicPath(info.getFriendRealPicPath());
				}
				pkg.putStr(picPath);
                pkg.putStr(info.getFriendName());
                pkg.putInt(info.getLevel());
                pkg.putInt(info.getWin());
                pkg.putInt(info.getLose());
                pkg.putByte((byte)info.getVipLevel());

                pkg.putInt((int) dis);
                pkg.putBoolean(true);
                pkg.putInt(info.getSex());
                
                
                
                int isPlaying = 3;
                // 加上是否在游戏中的属性
                GamePlayer gp = WorldMgr.getPlayerByID(info.getFriendId());
                
                if(null == gp)
                {
                	// 从机器人里面取,值班的
                	Player pl = RobotMgr.getOnDutyRobotByID(info.getFriendId());
                	if(null != pl)
                	{
                		isPlaying = pl.isPlaying() ? 1 : 2;
                	}
                }
                else
                {
                	// 在线，正在游戏 1， 不在游戏 2
                	isPlaying = gp.getIsPlaying() ? 1 : 2;
                }
                pkg.putInt(isPlaying);
                pkg.putStr("");
                
            }
            sendTCP(pkg);
        }
    }

    /**
     * 同步服务器时间
     */
    public void SendDateTime()
    {
        Packet pkg = new Packet((byte) UserCmdOutType.SYS_DATE);
        pkg.putDate(Calendar.getInstance().getTime());
        sendTCP(pkg);
    }

    /**
     * 更新人物私有信息(主要是跟金钱什么的有关的)
     * 
     * @param playerCharacter
     * @param type 更改金币的类型（客户端需要）
     * 类型：1 代表游戏结算时候的更改 2 代表用户在送礼时候的更改 0 其它类型的修改
     */
    public void sendUpdatePrivateInfo(PlayerInfo playerCharacter, byte type)
    {
        if (playerCharacter == null)
            return;

        Packet pkg = new Packet(UserCmdOutType.UPDATE_PRIVATE_INFO);
        pkg.putInt(playerCharacter.getFortune());
        pkg.putInt(playerCharacter.getMoney());
        pkg.putInt(playerCharacter.getCoins());
        pkg.putInt(playerCharacter.getCardCount());
        pkg.putByte(type);
        sendTCP(pkg);
    }
    /**
     * 发送在线私信
     */
    public void sendLetter(List<UserLetter> resposeData){
    	
    	
    	Packet response = new Packet(UserCmdOutType.USER_READ_LETTER);
    	int dataSize = resposeData.size();
    	response.putInt(dataSize);
    	
    	for(UserLetter currentDate:resposeData)
    	{
    		response.putStr(currentDate.getContent());
    		response.putStr(currentDate.getSendTime().toString());
    	}
    	sendTCP(response);
    }
    
    
    public void sendOnLineLetter(UserLetter letter){
    	
    	if(letter != null )
    	{
    		String tsStr = "";
        	DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");  
        	tsStr = sdf.format(letter.getSendTime());   
        	
    		Packet packet = new Packet(UserCmdOutType.USER_REVICE_LETTER);
    		packet.putInt(letter.getSenderId());
    		packet.putByte((byte)letter.getSenderVip());
    		packet.putStr(letter.getSenderName());
    		packet.putStr(HeadPicUtil.getRealSmallPicPath(letter.getSenderId(),letter.getSenderPic()));
    		packet.putStr(tsStr);
    		packet.putStr(letter.getContent());
    		packet.putInt(letter.getType());
    		packet.putInt(letter.getReceiverId());
    		packet.putStr(letter.getReceiverName());
   
    		sendTCP(packet);
    	}
    }

    /**
     * 自上一次发送未读私信后  又有新的私信内容
     */
    public void sendHasNoReadLetterTips()
    {
    	Packet pkg = new Packet(UserCmdOutType.USER_HAS_NOREADLETTER);
    	pkg.putInt(user.getMsgBox().getNoReadCount());
    	user.sendTcp(pkg);
    }
    /**
     * 发送用户最新的评论数据
     */
    public void sendNewestRewardState(){
    	Packet pkg = new Packet(UserCmdOutType.USER_REWARD_STATE);
    	UserReward responseData = user.getUserReward();
    	
    	if(responseData == null ){
    		pkg.putInt(0);
    	}else{
    		pkg.putInt(responseData.getLoginAward());
    	}

    	
    	int[][] rewardList = AwardMgr.getRewardList();
    	pkg.putInt(rewardList.length);
		for(int i = 0;i<rewardList.length;i++)
		{
			pkg.putInt(rewardList[i][0]);
	    	pkg.putInt(rewardList[i][1]);
		}
		
		
    	user.sendTcp(pkg);
    }
    
    
    public void sendDrawState(int type, int para){
    	
    	Packet pkg = new Packet(UserCmdOutType.USER_DRAW_STATE);
    	
    	
    	
    	UserReward responseData = user.getUserReward();
    	if(responseData == null )
    	{
    		pkg.putInt(0);
    		pkg.putInt(0);
    	}else{
    		pkg.putInt(responseData.getDayDraw());
    		pkg.putInt(responseData.getDrawGameCount());
    	}
    	   
    	List<DrawGoodInfo> rewardList = DrawMgr.getShopGoodsByType(type, para);
    	pkg.putInt(rewardList.size());
		for(int i = 0;i<rewardList.size();i++)
		{
			pkg.putInt(rewardList.get(i).getTemplateId());
	    	pkg.putInt(rewardList.get(i).getCount());
		}
		
		
    	user.sendTcp(pkg);
    }

    /**
     * 更新玩家基本属性到客户端
     * 只是发送自己的属性
     * @param playerCharacter
     */
    public void sendUpdateBaseInfo()
    {
        if (null == user || null == user.getPlayerInfo())
            return;
        PlayerInfo playerCharacter = user.getPlayerInfo();
        Packet pkg = new Packet(UserCmdOutType.UPDATE_BASE_INFO);
        pkg.putInt(playerCharacter.getWin()); // 胜的场次
        pkg.putInt(playerCharacter.getLose()); // 负的场次
        pkg.putInt(playerCharacter.getLevel()); // 等级
        pkg.putStr(LevelMgr.getLevelTitle(playerCharacter.getLevel())); // 头衔
        pkg.putInt(LevelMgr.getUserUpgradeGp(playerCharacter)); // 玩家经验
        pkg.putInt(LevelMgr.getUpgradeGp(playerCharacter.getLevel()));//当前玩家升级所需经验值
        pkg.putInt(user.getDrunkLevel()); // 当前醉酒度
        pkg.putInt(playerCharacter.getDrunkLevelLimit()); // 最大醉酒度
        pkg.putInt(ReferenceMgr.getSlaveCount(playerCharacter.getUserId()));//奴隶数
        
        pkg.putStr(playerCharacter.getRealPicPath()); // 头像地址
        
    	
        List<String> list = playerCharacter.getRealPicPathList();
    	pkg.putByte((byte)list.size());
    	for(String s : list)
    	{
    		pkg.putStr(s);
    	}
        
//        boolean isNotEmpty = !playerCharacter.getRealPicPath().isEmpty();
//        if (null!= list && list.size() > 0)
//        {
//        	byte sum = (byte)list.size();
//        	if (isNotEmpty)
//        	{
//        		sum++;
//        	}
//        	pkg.putByte(sum);
//        	if (isNotEmpty)
//        	{
//        		pkg.putStr(playerCharacter.getRealPicPath());
//        	}
//        	for(String s : list)
//        	{
//        		pkg.putStr(s);
//        	}
//        }
//        else if (isNotEmpty)
//        {
//        	pkg.putByte((byte)1);
//        	pkg.putStr(playerCharacter.getRealPicPath());
//        }
//        else 
//        {
//        	pkg.putByte((byte)0);
//        }
        // 构建道具  背包相关
//        List<Integer> propTypeList = user.getPropBag().getAllItemTypes();
//        pkg.putInt(propTypeList.size()); // 几个类型的道具
//        for (Integer propType : propTypeList)
//        {
//            pkg.putInt(propType); // 道具模板id
//            pkg.putInt(user.getPropBag().getPropCountByType(propType)); // 拥有此类型道具的数量
//        }
    	
    	List<BaseItem> itemList = user.getPropBag().getAllItems();
    	pkg.putInt(itemList.size()); // 几个类型的道具
    	for (BaseItem item : itemList)
    	{
    		pkg.putInt(item.getUserItemInfo().getTemplateId()); // 道具模板id
    		pkg.putInt(item.getUserItemInfo().getCount()); // 拥有此类型道具的数量
    	}
    	
    	
        
//        List<Integer> cardTypeList = user.getCardBag().getAllCardTypes();
//        pkg.putInt(cardTypeList.size()); // 几个类型的道具
//        for (Integer cardType : cardTypeList)
//        {
//            pkg.putInt(cardType); // 道具模板id
//            pkg.putInt(user.getCardBag().getCardCountByType(cardType)); // 拥有此类型道具的数量
//            pkg.putInt(user.getPlayerCard().getCardDeveopLevel(cardType));
//        }
        
        pkg.putInt(user.getPlayerInfo().getStageId());
        pkg.putByte((byte)user.getPlayerInfo().getVipLevel());        
        if(user.getPlayerInfo().getVipLevel() > 0 && user.getPlayerInfo().getVipDate() != null)
        {
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        	pkg.putStr(df.format(user.getPlayerInfo().getVipDate()));
        }
        else
        {
        	pkg.putStr("");
        }
        	
        
        sendTCP(pkg);
    }

    public void sendStageCharmValve()
    {
    	
    	Packet pkg = new Packet(UserCmdOutType.STAGE_USER);
    	
    	pkg.putInt(user.getStages().size()); 
    	for(PlayerStage stage : user.getStages().values())
    	{
    		pkg.putInt(stage.getStageId()); 
    		pkg.putInt(stage.getCharmValve()); 
    	}
    	
    	sendTCP(pkg);
    }
    
    
    public void sendStageCharmValve(int stage, int charmValve)
    {
    	
    	Packet pkg = new Packet(UserCmdOutType.STAGE_USER);
    	
    	pkg.putInt(1);
    	pkg.putInt(stage);
    	pkg.putInt(charmValve);
    	
    	sendTCP(pkg);
    }
    
    public void sendStageIndex(int score, int index, int roundCount)
    {
    	
    	Packet pkg = new Packet(UserCmdOutType.STAGE_INDEX);
    	
    	pkg.putInt(score);
    	pkg.putInt(index);
    	pkg.putInt(roundCount);
    	
    	sendTCP(pkg);
    }
    
    public void sendStageOver(String strStageName, int index, String strPrize)
    {
    	
    	Packet pkg = new Packet(UserCmdOutType.STAGE_OVER);
    	
    	pkg.putStr(strStageName);
    	pkg.putInt(index);
    	pkg.putStr(strPrize);
    	
    	sendTCP(pkg);
    }
    
    public Packet brocastPorpUseByType(GamePlayer gamePlayer, int itemTypeOruserItemId)
    {
    	if(null == gamePlayer || null == gamePlayer.getPlayerInfo()) {
    		return null;
    	}
    	PlayerInfo playerInfo = gamePlayer.getPlayerInfo();
    	ItemTemplateInfo item = ItemMgr.findItemTemplate(itemTypeOruserItemId);
    	
    	Packet pkg = null;
    	switch (item.getType()) {
        
	        case PropType.WAKE_TYPE:
	        	pkg = new Packet(UserCmdOutType.BROC_PROP_USE);
	            pkg.putInt(playerInfo.getUserId());
	            pkg.putInt(item.getTemplateId());
	            pkg.putInt(user.getDrunkLevel()); // 当前醉酒度
	            pkg.putInt(playerInfo.getDrunkLevelLimit()); // 最大醉酒度
	            break;
	        default:
	            break;
	    }
        return pkg;
    }

    /**
     * 服务器要求断开某个玩家的链接
     */
    public void disConnect()
    {
        Packet pkg = new Packet((byte) UserCmdOutType.DISCONNECT);
        pkg.putInt(0);
        sendTCP(pkg);
    }

    /**
     * 通知玩家进入场景
     * 
     * @param player
     *            玩家对象
     * @return 发送的数据包对象
     */
    public Packet sendSceneAddPlayer(GamePlayer player)
    {
        PlayerInfo playerInfo = player.getPlayerInfo();
        Packet pkg = new Packet(UserCmdOutType.SCENE_ADD_USER,
                player.getUserId());

        pkg.putInt(playerInfo.getWin());

        sendTCP(pkg);
        return pkg;
    }

    /**
     * 
     * 踢玩家下线
     * 
     * @param string
     *            发送给玩家的消息
     * 
     */
    public void SendKitoff(String msg)
    {
        Packet pkg = new Packet(UserCmdOutType.KIT_USER);
        pkg.putStr(msg);
        sendTCP(pkg);
    }

    /**
     * @param dailyquest
     * @param title
     * @param content
     */
    public void sendSNSMsg(int dailyquest, String title, String content)
    {

    }
    
    /**
     * notify client to create a room instance.
     * 
     * createState的状态为：0表示成功1表示需要密码2表示密码错误3表示好友不在房间4表示房间已满5表示大厅已满6表示服务期已满7房间不存在
     * @param createState roomId
     *            房间对象
     * @return 协议包
     */
    public void sendRoomCreate(byte createState, BaseRoom room, short userCmdOutType)
    {	
    	if(createState <= 0 && null != room && null != room.getHallType()) {
    		
//    		int eyeType = 0;//透视眼类型
//        	int probability = 0;//透视眼成功率
//        	String path = ""; //透视眼图片
    		
    		Packet pkg = new Packet(userCmdOutType);
    		pkg.putInt(createState);
    		pkg.putInt(room.getRoomId());    		
    		pkg.putInt(room.getHallType().getWager());//赌注
    		pkg.putInt(room.getHallType().getForcedExitCoins());//强退扣除金币数
    		pkg.putInt(room.getHallType().getSystemTaxes());//每局游戏的服务费
    		pkg.putInt(room.getHallType().getHallTypeId());//大厅ID
    		pkg.putInt(room.getHallType().getHallType());
//    		pkg.putInt(room.getStageRoundIndex());
//    		pkg.putInt(room.getStageRoundScore());
//    		BaseHall hall = user.getCurrentHall();
//    		
//    		
//    		if(hall != null){
//    			ItemTemplateInfo minEye = user.getPropBag().getMinEyeInHall( hall.getHallId());
//    			eyeType = minEye.getType();
//    			probability = minEye.getProbability();
//    			path = minEye.getResPath();
//    		}
           	/** 以下3个字段自V2.2开始 */
//	       	pkg.putInt(eyeType);
//	       	pkg.putStr(path);
//	       	pkg.putInt(probability);
    		
    		sendTCP(pkg);
    	}
    }
    
    /**
     * notify client to create a room instance.
     * 
     * createState的状态为：0表示成功1表示需要密码2表示密码错误3表示好友不在房间4表示房间已满5表示大厅已满6表示服务期已满7房间不存在 10房间锁上了
     * @param createState roomId
     *            房间对象
     * @return 协议包
     */
    public void sendRoomCreateFail(byte createState, int roomId, short userCmdOutType)
    {	
    	if(createState != 0) {
    		Packet pkg = new Packet(userCmdOutType);
    		pkg.putByte(createState);
    		pkg.putInt(roomId);
    		pkg.putInt(0);
    		pkg.putInt(0);
    		pkg.putInt(0);
    		pkg.putInt(0);
    		pkg.putInt(0);
    		pkg.putInt(0);
    		pkg.putInt(0);
    		pkg.putInt(0);
            sendTCP(pkg);
    	}
    }

    /**
     *
     * 通知玩家更新房间列表
     * 
     * @param rooms
     *            房间列表
     * @return 生成的协议包
     */
    public Packet sendUpdateRoomList(List<BaseRoom> rooms)
    {
        Packet pkg = new Packet(UserCmdOutType.GAME_ROOMLIST_UPDATE);
        pkg.putInt(RoomMgr.getUsingRoomCount());
        pkg.putInt(rooms.size());
        for (BaseRoom room : rooms)
        {
            pkg.putInt(room.getRoomId());
            pkg.putByte((byte) room.getTimeMode());
            pkg.putByte((byte) room.getPlayerCount());
            pkg.putByte((byte) room.getPlacesCount());
            pkg.putBoolean(room.isNeedPassword());
            pkg.putBoolean(room.isPlaying());
            pkg.putStr(room.getName());
            pkg.putByte((byte) room.getHardType().getValue());
        }

        sendTCP(pkg);

        return pkg;
    }

    /**
     * @param baseRoom
     * @return
     */
    public Packet sendRoomChange(BaseRoom baseRoom)
    {
        Packet packet = new Packet(UserCmdOutType.GAME_ROOM_SETUP_CHANGE);

        packet.putByte(baseRoom.getTimeMode());
        packet.putByte(baseRoom.getHardType().getValue());
        sendTCP(packet);

        return packet;
    }

    /**
     * 发送房间所有位置状态
     * 
     * @param placesState
     *            位置状态
     * @return 数据包
     */
    public Packet sendRoomUpdatePlacesStates(int[] placesState, boolean isSend)
    {
        Packet pkg = new Packet(UserCmdOutType.GAME_ROOM_UPDATE_PLACE);
        pkg.putByte((byte) placesState.length);
        for (int i = 0; i < placesState.length; i++)
        {
        	pkg.putInt(i);
            pkg.putInt(placesState[i]);
        }

        if(isSend)
        	sendTCP(pkg);

        return pkg;
    }

    /**
     * 发送玩家进入房间的协议包
     * 
     * @param player
     *            玩家对象
     * @return 发送的协议包
     */
    public Packet sendRoomPlayerAdd(GamePlayer player, boolean isSend)
    {
    	    	
        Packet pkg = new Packet(UserCmdOutType.GAME_PLAYER_ENTER);
        // --------------------player detail
        // information----------------------------
        pkg.putInt(player.getUserId());
        pkg.putByte(player.getPlayerInfo().getSex());
        pkg.putStr(player.getPlayerInfo().getUserName());
        pkg.putStr(player.getPlayerInfo().getRealPicPath());
        pkg.putInt(player.getPlayerInfo().getLevel());
        pkg.putByte((byte)player.getPlayerInfo().getVipLevel());
        pkg.putInt(player.getPlayerInfo().getMoney());
        pkg.putInt(player.getPlayerInfo().getCoins());
        pkg.putInt(player.getPlayerInfo().getCharmValve());
        pkg.putByte((byte) player.getCurrentRoomPos());
        pkg.putInt(player.getDrunkLevel());
        pkg.putInt(player.getPlayerInfo().getDrunkLevelLimit());
        
        
        double[] userPos = DistanceUtil.getLatAndLon(player.getPlayerInfo().getPos());
        pkg.putDouble(userPos[0]);
        pkg.putDouble(userPos[1]);
        

        /*
         * TODO -------------加入奴隶主的信息 begin
         */
        UserReferenceInfo info = player.getPlayerReference().getMasterUserReferenceInfo();
    	pkg.putInt(null != info ? info.getMasterUserId() : 0);
    	pkg.putStr(null != info ? info.getMasterUserName() : "");
    	pkg.putStr(null != info ? info.getMasterPicPath(): "");
    	/*
         * -------------加入奴隶主的信息 end
         */

    	if(isSend)
    		sendTCP(pkg);
    	
        return pkg;
    }

    /**
     * 发送玩家离开房间消息
     * 
     * @param player
     *            离开的玩家
     * @return 数据包
     */
//    public Packet sendRoomPlayerRemove(GamePlayer player)
//    {
//        // 发送玩家退出房间的协议包
//        Packet packet = new Packet(UserCmdOutType.GAME_PLAYER_EXIT);
//        packet.putInt(player.getUserId());
//        sendTCP(packet);
//
//        return packet;
//    }
    
    /**
     * 发送玩家离开房间消息
     * 
     * @param player
     *            离开的玩家
     * @param isKicked           
     *            是否是被系统踢出
     * @return 数据包
     */
    public Packet sendRoomPlayerRemove(GamePlayer player, boolean isKicked, boolean isStageGame, int pengType)
    {
        // 发送玩家退出房间的协议包
        Packet packet = new Packet(UserCmdOutType.GAME_PLAYER_EXIT);
        packet.putInt(player.getUserId());
        packet.putBoolean(isKicked);
        packet.putBoolean(isStageGame);
        packet.putInt(pengType);
        sendTCP(packet);

        return packet;
    }


    
    /**
     * 发送玩家离开房间消息
     * 
     * @param player
     *            离开的玩家
     * @param isKicked           
     *            是否是被系统踢出
     * @return 数据包
     */
    public Packet sendPlayerDrunkNotify(int time, int money, String work, String masterName)
    {
        // 发送玩家退出房间的协议包
        Packet packet = new Packet(UserCmdOutType.USER_DRUNK_NOTIFY);
        packet.putInt(time);
        packet.putInt(money);
        packet.putStr(work);   
        packet.putStr(masterName);
        sendTCP(packet);


        return packet;
    }
    
    public Packet sendPlayerDrunkLock(boolean isFinish, int time, int coin, String work, String masterName)
    {
        // 发送玩家退出房间的协议包
        Packet packet = new Packet(UserCmdOutType.USER_DRUNK_LOCK);
        packet.putBoolean(isFinish);
        packet.putInt(time);
        packet.putInt(coin);
        packet.putStr(work);   
        packet.putStr(masterName);
        sendTCP(packet);


        return packet;
    }
    /**
     * 发送玩家离开房间消息
     * 
     * @param player
     *            离开的玩家
     * @param isKicked           
     *            是否是被系统踢出
     * @return 数据包
     */
    public Packet sendRoomPlayerCoin(int addCoin)
    {    	
        // 发送玩家退出房间的协议包
        Packet packet = new Packet(UserCmdOutType.ROOM_PLAYER_COIN);
        packet.putInt(user.getUserId());
        packet.putInt(addCoin);
        packet.putInt(user.getPlayerInfo().getCoins());
        sendTCP(packet);

        return packet;
    }
    
    /**
     * @param id
     */
    public Packet sendFriendRemove(int id)
    {
        Packet pkg = new Packet(UserCmdOutType.FRIEND_REMOVE, id);
        sendTCP(pkg);
        return pkg;
    }

    /**
     * @param playerID
     * @param state
     * @param isVIP
     * @param vIPLevel
     * @return
     */
    public Packet sendFriendState(int playerID, int state, boolean isVIP,
            int VIPLevel)
    {
        Packet pkg = new Packet(UserCmdOutType.FRIEND_STATE, playerID);
        pkg.putInt(state);
        pkg.putBoolean(isVIP);
        pkg.putInt(VIPLevel);
        sendTCP(pkg);
        return pkg;
    }

    
    /**
     * 
     */
    public void SetPingTime()
    {
    	user.setPingStart(TimeUtil.getSysCurTimeMillis());   	
    }
    
    /**
     * 
     */
    public void SendPingTime()
    {
    	user.setPingStart(TimeUtil.getSysCurTimeMillis());    	
        Packet pkg = new Packet(UserCmdOutType.PING_ACK);        
        sendTCP(pkg);
    }

    /**
     * @param userId
     * @param pingTime2
     * @return
     */
    public Packet SendNetWork(int userId, long delay)
    {
        Packet pkg = new Packet(UserCmdOutType.NETWORK, userId);
        pkg.putInt((int) delay / 1000 / 10);
        sendTCP(pkg);
        return pkg;
    }

    /**
     * 发送个人资料供显示
     * 
     * @param info
     * @return
     */
    public Packet sendUserInfo(PlayerInfo info)
    {
        Packet pkg = new Packet(UserCmdOutType.USER_INFO_SHOW);
        pkg.putStr(info.getUserName());
        pkg.putStr(info.getSign());
        pkg.putInt(info.getLevel());
        pkg.putInt(info.getWin());
        pkg.putInt(info.getLose());
        pkg.putStr(info.getCity());
        pkg.putStr(info.getRealPicPath());
        sendTCP(pkg);
        return pkg;
    }

    /**
     * 发送玩家开始游戏
     */
    public Packet sendStartGame()
    {
//        Packet packet = new Packet(UserCmdOutType.GAME_START);
//        packet.putBoolean(true);
//        sendTCP(packet);
//        return packet;
    	return null;
    }

    /**
     * 更改房主
     * 
     * @param player
     * @return
     */
    public Packet sendChangeHost(GamePlayer player)
    {
        Packet packet = new Packet(UserCmdOutType.ROOM_UPDATE_HOST);
        packet.putInt(player.getPlayerInfo().getUserId());
        sendTCP(packet);
        return packet;
    }

    /**
     * 提示玩家使用或者购买解酒道具
     * 
     * @param player
     * @param templateId
     *            道具模板ID（现在换成了道具类型）
     * @param hasProp
     *            是否拥有道具, 有则提示使用, 无则提示购买
     */
    public void sendDrunkUseProp(GamePlayer player, int type,
            boolean hasProp)
    {
        Packet packet = new Packet(UserCmdOutType.DRUNK_PROP_USE);
        packet.putInt(player.getUserId());
        packet.putInt(type);
        packet.putBoolean(hasProp);
        sendTCP(packet);
    }

    /**
     * 发送游戏币不足的提示
     * @param surplusCount 
     */
    public void sendCoinsNotEnough(boolean isCheck, int total, int surplusCount, int AwardCoin)
    {
    	
//    	System.out.println("palyerCoinsLessAwardCoins====" + total + "====" + surplusCount);
    	
        Packet packet = new Packet(UserCmdOutType.USER_BROKE_COIN);
        packet.putBoolean(isCheck);
        packet.putByte((byte)total);
        packet.putByte((byte)surplusCount);
        packet.putInt(AwardCoin);
        sendTCP(packet);
    }

    /**
     * 发送错误的提示信息
     * @param susseed
     * @param message
     */
    public void sendCannotStayHereOk() {
    	sendReturnMessage(true, "");
    }

    /**
     * 发送错误的提示信息
     * @param susseed
     * @param message
     */
    public void sendReturnMessage(boolean susseed, String message) {
		Packet response = new Packet(UserCmdOutType.RETURN_AND_MESSAGE);
		response.putBoolean(susseed);//是否成功	
		response.putStr(message);//提示信息
		
        sendTCP(response);
    }

    /**
     * 发送错误的提示信息
     * @param susseed
     * @param message
     */
    public void sendRoomReturnMessage(boolean susseed, String message) {
		Packet response = new Packet(UserCmdOutType.RETURN_AND_MESSAGE_ROOM);
		response.putBoolean(susseed);//是否成功
		if( ! susseed) {
			response.putStr(message);//提示信息
		}
        sendTCP(response);
    }
    
    /**
     * 有确定按钮的提示语,并返回客户的操作
     * @param susseed
     * @param message
     */
    public void sendMessageAndOperate(short code, String message) {
		Packet response = new Packet(UserCmdOutType.MESSAGE_AND_OPERATE);
		response.putShort(code);//类型编号，客户端收到定确定时候返回
		response.putStr(message);//提示信息
        sendTCP(response);
    }

    /**
     * when the game is playing,sending the protocol of entering room.
     * 
     * @param roomID
     */
    public void sendEnterRoom(int roomID)
    {
        Packet packet = new Packet(UserCmdOutType.USER_ENTER_ROOM);
        packet.putInt(roomID);
        sendTCP(packet);
    }
    
    /**
     * 发送完成
     * @param packet
     * @param list
     */
    public void sendUserFinishOrNextTask(Packet packet, List<UserTaskInfo> list) {
    	
    	putSomeTaskInfoIntoPacket(packet, list);
    	sendTCP(packet);
    }
    
    
    /**
     * 构造任务相关信息包工具方法
     * @param packet
     * @param infoList
     */
    public void sendUserFinishOrNextTask(Packet packet, UserTaskInfo info) {
    	
    	
    		packet.putInt(1);
    		
    		
			packet.putInt(info.getId());
			packet.putInt(info.getTaskInfo().getTaskId());
	    	packet.putStr(info.getTaskInfo().getTaskName());
	    	packet.putStr(info.getTaskInfo().getTaskDesc());
	    	packet.putInt(info.getTaskInfo().getTaskType());
	    	packet.putInt(info.getTaskInfo().getStage());
	    	packet.putInt(info.getTaskStatus());
	    	packet.putInt(info.getTaskInfo().getIsNumTask());//0-不是数值任务,1-是数值任务
	    	if (info.getTaskInfo().getIsNumTask() == 1) {
	    		packet.putInt(info.getTaskFinishNum());//当前完成的次数
    	    	packet.putInt(info.getTaskInfo().getTaskNum());//任务完全的总次数
	    	}
	    	packet.putInt(info.getTaskInfo().getPrizeList().size());//有几个奖励
	    	for (int i = 0; i < info.getTaskInfo().getPrizeList().size(); i++) {
	    		TaskPrize tp = info.getTaskInfo().getPrizeList().get(i);
	    		packet.putInt(tp.getPrizeType());//奖励类型0-金币,1-经验,2-表示醒酒茶
	    		packet.putInt(tp.getPrizeNum());//奖励数量
	    	}
        		
    		
        	sendTCP(packet);
        	
        	
        	//System.out.println("sendUserFinishOrNextTask=============================================" + info.getId() + "====" + info.getTaskInfo().getTaskName());
        	
//        	if(info.getId() > 1000)
//        	{
//        		int zz = 5;
//        		zz ++;
//        	}
    }
    
    
//    /**
//     * 用户完成任务之后
//     * 发送完成单个任务完成信息和下一个单个任务信息--重构方法
//     * @param info
//     */
//    public void sendUserFinishTask(UserTaskInfo info, UserTaskInfo nextTaskInfo) {
//    	if (null == info) {
//    		return;
//    	}
//    	List<UserTaskInfo> completedTasksList = new ArrayList<UserTaskInfo>();
//    	completedTasksList.add(info);
//    	List<UserTaskInfo> nextTasksList = null;
//    	if (null != nextTaskInfo) {
//    		nextTasksList = new ArrayList<UserTaskInfo>();
//    		nextTasksList.add(nextTaskInfo);
//    	}
//    	sendUserFinishTask(completedTasksList, nextTasksList);
//    }
//    
//    /**
//     * 发送完成多个任务的信息和接受多个任务的信息
//     * @param list
//     * @param nextList
//     */
//    public void sendUserFinishTask(List<UserTaskInfo> list, List<UserTaskInfo> nextList) {
//    	if (null == list || list.size() == 0) {
//    		return;
//    	}
//    	Packet packet = new Packet(UserCmdOutType.USER_FINISH_TASK);
//    	putSomeTaskInfoIntoPacket(packet, list);
//    	if (null != nextList && nextList.size() > 0) {
//    		putSomeTaskInfoIntoPacket(packet, nextList);
//    	}
//    	sendTCP(packet);
//    }
    
    /**
     * 构造任务相关信息包工具方法
     * @param packet
     * @param infoList
     */
    public void putSomeTaskInfoIntoPacket(Packet packet, List<UserTaskInfo> infoList) {
    	if (null != infoList) {
    		packet.putInt(infoList.size());
    		if (!infoList.isEmpty()) {
    			for (UserTaskInfo info : infoList) {
    				
    				
    				
//    				if(info.getId() == 0)
//    					System.out.println("userTaskInfo=====================================" + info.getId());
    				
    				packet.putInt(info.getId());
        			packet.putInt(info.getTaskInfo().getTaskId());
        	    	packet.putStr(info.getTaskInfo().getTaskName());
        	    	packet.putStr(info.getTaskInfo().getTaskDesc());
        	    	packet.putInt(info.getTaskInfo().getTaskType());
        	    	packet.putInt(info.getTaskInfo().getStage());
        	    	packet.putInt(info.getTaskStatus());        	    	
        	    	packet.putInt(info.getTaskInfo().getIsNumTask());//0-不是数值任务,1-是数值任务
        	    	if (info.getTaskInfo().getIsNumTask() == 1) {
        	    		packet.putInt(info.getTaskFinishNum());//当前完成的次数
            	    	packet.putInt(info.getTaskInfo().getTaskNum());//任务完全的总次数
        	    	}
        	    	packet.putInt(info.getTaskInfo().getPrizeList().size());//有几个奖励
        	    	for (int i = 0; i < info.getTaskInfo().getPrizeList().size(); i++) {
        	    		TaskPrize tp = info.getTaskInfo().getPrizeList().get(i);
        	    		packet.putInt(tp.getPrizeType());//奖励类型0-金币,1-经验,2-表示醒酒茶
        	    		packet.putInt(tp.getPrizeNum());//奖励数量
        	    	}
        	    	        	    	
        	    	//System.out.println("sendUserFinishOrNextTask=============================================" + info.getId() + "====" + info.getTaskInfo().getTaskName());
                	               	
    			}
    		}
    	}
    }
    
    public void sendUserCurrentTask(List<UserTaskInfo> list,boolean isSendFinish) {
    	if (null == list) {
    		return;
    	}
    	Packet packet = new Packet(UserCmdOutType.USER_CURRENT_TASK);
    	List<UserTaskInfo> infoList = new ArrayList<UserTaskInfo>();
    	for (UserTaskInfo userTaskInfo : list) {
    		if (isSendFinish || userTaskInfo.getTaskStatus() == TaskMgr.TASK_STATUS_NOT_FINISH
    				|| userTaskInfo.getTaskStatus() == TaskMgr.TASK_STATUS_ALREADY_FINISH) {
    			infoList.add(userTaskInfo);
    		}
    	}
    	putSomeTaskInfoIntoPacket(packet, infoList);
    	sendTCP(packet);
    }
    /**
     * 发送最新的主人信息
     */
    public void sendUserMasterInfo(int userId) {
    	if (userId < 0) {
    		return;
    	}
    	Packet packet = new Packet(UserCmdOutType.USER_MASTER_DETAIL);
    	packet.putInt(userId);
    	sendTCP(packet);
    }
}
