/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.dice.entity.ShopGoodInfo;
import com.citywar.game.GamePlayer;
import com.citywar.manager.ShopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 游戏中显示商品信息
 * 
 * @author shanfeng.cao
 * @date 2012-07-13
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.SHOP_OUTSIDE_GOODS_SHOW, desc = "游戏中显示商品信息")
public class ShopOutsideGoodsShowCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	
        byte itemType = packet.getByte();
        // 商品类型
        //byte goodType = packet.getByte();

        Packet pkg = new Packet(UserCmdOutType.SHOP_OUTSIDE_GOODS_SHOW);
    	pkg.putInt(itemType);//为了配合客户端，好区分是哪一次的返回
        List<ShopGoodInfo> goodsList = ShopMgr.getGoodsByGoodsItemType(itemType);
        if (null != goodsList && goodsList.size() > 0) {
        	pkg.putInt(goodsList.size());
            for (ShopGoodInfo info : goodsList)
            {
                pkg.putByte(info.getShopType());
                pkg.putByte(info.getGoodType());
                pkg.putInt(info.getTemplateId());
                pkg.putStr(info.getShopPicPath());
                pkg.putStr(info.getName());
                pkg.putStr(info.getNamePicPath());
                pkg.putStr(info.getDescription());
                pkg.putFloat(info.getPrice());
            }
        } else {
        	pkg.putInt(0);
        }
        player.getOut().sendTCP(pkg);
        return 0;
    }

}
