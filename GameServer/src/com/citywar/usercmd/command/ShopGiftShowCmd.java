/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.dice.entity.GiftTemplateInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.GiftTemplateMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 查看礼品信息
 * 
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.GIFT_SHOW, desc = "查看礼品信息")
public class ShopGiftShowCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        // gift_buy_type（礼品的购买类型（在哪里可以购买）扩展用）
        byte giftBuyType = packet.getByte();

        Packet pkg = new Packet(UserCmdOutType.GIFT_SHOW);
        List<GiftTemplateInfo> giftList = GiftTemplateMgr.getGiftTemplatesByBuyType(giftBuyType);
        if (null != giftList && giftList.size() > 0) {
        	pkg.putInt(giftList.size());//总的礼品数量
            for (GiftTemplateInfo info : giftList)
            {
                pkg.putInt(info.getGiftId());//礼品ID
                pkg.putByte((byte)info.getGiftType());//礼品类型
                pkg.putStr(info.getGiftName());//礼品名字
                pkg.putInt(info.getGiftPrice());//礼品的价格
                pkg.putInt(info.getGiftCharmValue());//礼品的魅力值
                pkg.putStr(info.getGiftDescribe());//赠送礼品时候提一句提示语
                pkg.putStr(info.getGiftShowPicPath());//礼品图片地址
                pkg.putStr(info.getGiftEffectPath());//礼品的效果地址
                //pkg.putByte((byte)info.getGiftBuyType());//礼品的购买地址
            }
        } else {
        	pkg.putInt(0);
        }
        player.getOut().sendTCP(pkg);
        return 0;
    }

}
