/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.util.Date;
import java.util.List;

import com.citywar.dice.entity.GiftTemplateInfo;
import com.citywar.dice.entity.UserGiftInfo;
import com.citywar.game.GamePlayer;
import com.citywar.gameutil.GamePlayerUtil;
import com.citywar.gameutil.PlayerGift;
import com.citywar.manager.GiftTemplateMgr;
import com.citywar.manager.UserGiftMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 查看用户接受礼品的记录
 * 
 * @author shanfeng.cao
 * @date 2012-06-27
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.SEND_GIFT_LOG, desc = "查看用户接受礼品的记录")
public class ShowUserGiftLogCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        // userId 查看的用户ID
        int userId = packet.getInt();
        Packet pkg = new Packet(UserCmdOutType.SEND_GIFT_LOG);
        List<UserGiftInfo> userGiftList = null;//用户接受礼品的记录
        GamePlayer showPlayer = GamePlayerUtil.getOnLineGamePlayer(userId);
		if(null != showPlayer) {//玩家在线
			userGiftList = showPlayer.getPlayerGift().getUserAllGift();
		} else {//玩家不在线
			PlayerGift playerGift = UserGiftMgr.getUserGiftByUserId(userId, false);
			if(null == playerGift) {
	            return 0;
			}
			userGiftList = playerGift.getUserAllGift();
		}
        if (null != userGiftList && userGiftList.size() > 0) {
        	pkg.putInt(userGiftList.size());//赠送记录的条数
            for (UserGiftInfo info : userGiftList)
            {
            	pkg.putInt(info.getId());//礼品的ID
                pkg.putInt(info.getGiftId());//礼品的ID
                pkg.putInt(info.getPresentUserId());//礼品是谁送的
                pkg.putStr(info.getPresentUserName());//礼品的是谁送的名字
                pkg.putDate(new Date(info.getGiveTime().getTime()));//礼品的赠送的时间
            	GiftTemplateInfo giftTemplateInfo = GiftTemplateMgr.getGiftTemplateById(info.getGiftId());
            	if(null != giftTemplateInfo) {
                    pkg.putStr(giftTemplateInfo.getGiftName());//礼品的名字
                    pkg.putInt(giftTemplateInfo.getGiftPrice());//礼品的图片
                    pkg.putInt(giftTemplateInfo.getGiftCharmValue());//礼品的魅力值
                    pkg.putStr(giftTemplateInfo.getGiftDescribe());//礼品的一句话
                    pkg.putStr(giftTemplateInfo.getGiftShowPicPath());//礼品的图片
            	} else {//容错处理
                    pkg.putStr("");
                    pkg.putInt(0);
                    pkg.putInt(0);
                    pkg.putStr("");
                    pkg.putStr("");
            	}
            }
        } else {
        	pkg.putInt(0);//赠送记录的条数
        }
        player.getOut().sendTCP(pkg);
        return 0;
    }

}
