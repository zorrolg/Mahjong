/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.sql.Timestamp;

import com.citywar.GameServer;
import com.citywar.dice.entity.GiftTemplateInfo;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.PlayerInfo;
import com.citywar.dice.entity.UserGiftInfo;
import com.citywar.dice.entity.UserLetter;
import com.citywar.game.GamePlayer;
import com.citywar.gameutil.GamePlayerUtil;
import com.citywar.manager.GiftTemplateMgr;
import com.citywar.manager.UserGiftMgr;
import com.citywar.room.BaseRoom;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 赠送礼品
 * 
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version
 *  
 */
@UserCmdAnnotation(code = UserCmdType.SEND_GIFT, desc = "赠送礼品")
public class SendGiftCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	//返回值：0 代表成功，1 代表金钱不足
    	byte result = 0;
        // 接受礼品的玩家ID
        int userId = packet.getInt();
        // 礼品ID
        int giftId = packet.getInt();
        //备用（留言等）
        String remark = packet.getStr();
        GiftTemplateInfo info = GiftTemplateMgr.getGiftTemplateById(giftId);
        if(null == info) {
            return 0;
        }
        int giftPrice = info.getGiftPrice();//礼品的价格
        int giftCharmValue = info.getGiftCharmValue();//礼品的价格
        Packet pkg = new Packet(UserCmdOutType.SEND_GIFT);
        if(player.getPlayerInfo().getCoins() < giftPrice) {//没那么多钱
        	result = 1;
        } else {//有那么多钱
        	
        	player.addCoins(-giftPrice);
        	player.getPlayerInfo().setBuyItemCoins(player.getPlayerInfo().getBuyItemCoins() + giftPrice);//都买道具花的钱不算
            UserGiftInfo userGiftInfo = new UserGiftInfo();
            userGiftInfo.setOwnerUserId(userId);
            userGiftInfo.setGiftId(giftId);
            userGiftInfo.setPresentUserId(player.getUserId());
            userGiftInfo.setCount(1);//数量
            userGiftInfo.setGiveTime(new Timestamp(System.currentTimeMillis()));
            userGiftInfo.setExist(true);
            userGiftInfo.setUsed(true);
            userGiftInfo.setRemark(remark);
            userGiftInfo.setPresentUserName(player.getPlayerInfo().getUserName());
            userGiftInfo.setPresentUserPicPath(player.getPlayerInfo().getPicPath());
            userGiftInfo.setOp(Option.INSERT);
            player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)2);
            GamePlayer receivePlayer = GamePlayerUtil.getOnLineGamePlayer(userId);
    		if(null != receivePlayer) {//玩家在线
    			receivePlayer.getPlayerGift().receiveGift(userGiftInfo);
    			receivePlayer.getOut().sendUpdatePrivateInfo(receivePlayer.getPlayerInfo(), (byte)2);
    			BaseRoom receiveRoom = receivePlayer.getCurrentRoom();
    			BaseRoom sendRoom = player.getCurrentRoom();
    			if(sendRoom == receiveRoom && null != receiveRoom 
    					&& null != receiveRoom.getGame()) {//赠送者和接受者都在游戏中
    		        Packet gamePkg = new Packet(UserCmdOutType.GIFT_USE);
    		        gamePkg.putInt(player.getUserId());//赠送者玩家ID
    		        gamePkg.putInt(userId);//接受者玩家ID
    		        gamePkg.putInt(giftId);//礼品的ID
    		        gamePkg.putStr(info.getGiftEffectPath());//礼品的效果地址
    		        gamePkg.putInt(info.getGiftCharmValue());//礼品模板魅力值
    		        receiveRoom.getGame().sendToAll(gamePkg);
    			}
    		} else {//玩家不在线
    			UserGiftMgr.addUserGift(userGiftInfo);
    		}
    		sendGiftLetter(player, userId,info);
        }
        pkg.putByte(result);
        pkg.putInt(giftCharmValue);//物品的魅力值
        player.getOut().sendTCP(pkg);
        return 0;
    }
    
    public void sendGiftLetter(GamePlayer sender,int receiverId,GiftTemplateInfo info){
        
    	PlayerInfo receiverPlayerInfo = GamePlayerUtil.getPlayerInfo(receiverId, true);
    	
    	String sendCount = "赠送了一个"+info.getGiftName()+"给你";
    	int type = UserLetter.SEND_GIFT;
    	Packet packet = new Packet(20);
    	packet.setCode(UserCmdType.USER_SEND_LETTER);
    	packet.putInt(receiverId);
    	packet.putStr(receiverPlayerInfo.getUserName());
    	packet.putStr(sendCount);
    	packet.putInt(type);
    	packet.position(0);
    	GameServer.getInstance().getHandler().handleWebPacket(sender.getSession(), packet);
    	
    }

}
