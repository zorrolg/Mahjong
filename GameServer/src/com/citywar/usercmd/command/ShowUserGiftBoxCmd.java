/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.dice.entity.GiftTemplateInfo;
import com.citywar.dice.entity.UserGiftIntegrationInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.gameobjects.Player;
import com.citywar.gameutil.PlayerGift;
import com.citywar.manager.GiftTemplateMgr;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.UserGiftMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 查看用户礼品盒信息
 * 
 * @author shanfeng.cao
 * @date 2012-06-27
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.GIFT_BOX, desc = "查看用户礼品盒信息")
public class ShowUserGiftBoxCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        // userId 查看的用户ID
        int userId = packet.getInt();
        Packet pkg = new Packet(UserCmdOutType.GIFT_BOX);
        List<UserGiftIntegrationInfo> userGiftIntegrationList = null;//玩家的所有礼品整合信息
        GamePlayer showPlayer = WorldMgr.getPlayerByID(userId);
		if (null == showPlayer) {
			//查看的用户如果是机器人
			Player robot = RobotMgr.getOnDutyRobotByID(userId);
			if (null != robot) {
				showPlayer = robot.getPlayerDetail();
			}
		}
		if(null != showPlayer) {//玩家在线
			userGiftIntegrationList = showPlayer.getPlayerGift().getUserAllGiftIntegration();
		} else {//玩家不在线
			PlayerGift playerGift = UserGiftMgr.getUserGiftByUserId(userId, false);
			if(null == playerGift) {
	            return 0;
			}
			userGiftIntegrationList = playerGift.getUserAllGiftIntegration();
		}
        if (null != userGiftIntegrationList && userGiftIntegrationList.size() > 0) {
        	pkg.putInt(userGiftIntegrationList.size());//礼品的种类数
            for (UserGiftIntegrationInfo info : userGiftIntegrationList)
            {
            	GiftTemplateInfo giftTemplateInfo = GiftTemplateMgr.getGiftTemplateById(info.getGiftId());
            	if(null != giftTemplateInfo) {
                    pkg.putInt(info.getGiftId());//礼品的ID
                    pkg.putByte((byte)giftTemplateInfo.getGiftType());//礼品的类型
                    pkg.putStr(giftTemplateInfo.getGiftName());//礼品的名字
                    pkg.putInt(giftTemplateInfo.getGiftPrice());//礼品的价格
                    pkg.putInt(giftTemplateInfo.getGiftCharmValue());//礼品的魅力值
                    pkg.putStr(giftTemplateInfo.getGiftDescribe());//礼品的一句话
                    pkg.putStr(giftTemplateInfo.getGiftShowPicPath());//礼品的图片地址
                    pkg.putStr(giftTemplateInfo.getGiftEffectPath());//礼品的效果地址
                    //pkg.putByte((byte)giftTemplateInfo.getGiftBuyType());
                    pkg.putInt(info.getSumCount());//礼品的数量
            	} else {//容错处理
                    pkg.putInt(0);
                    pkg.putByte((byte)0);
                    pkg.putStr("");
                    pkg.putInt(0);
                    pkg.putInt(0);
                    pkg.putStr("");
                    pkg.putStr("");
                    pkg.putStr("");
                    pkg.putByte((byte)0);
                    pkg.putInt(0);
            	}
            }
        } else {
        	pkg.putInt(0);
        }
        player.getOut().sendTCP(pkg);
        return 0;
    }

}
