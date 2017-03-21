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
 * 查看商店商品信息
 * 
 * @author tracy
 * @date 2011-12-20
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.SHOP_GOODS_SHOW, desc = "查看商店商品信息")
public class ShopGoodsShowCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        // 商店类型
        byte goodsType = packet.getByte();
        // 商品类型
        //byte goodType = packet.getByte();

        Packet pkg = new Packet(UserCmdOutType.SHOP_GOODS_SHOW);
        List<ShopGoodInfo> goodsList = ShopMgr.getShopGoodsByGoodType(goodsType);      
        pkg.putByte(goodsType);
  
        if (null != goodsList && goodsList.size() > 0) {
        	pkg.putInt(goodsList.size());
            for (ShopGoodInfo info : goodsList)
            {
                pkg.putInt(info.getId());
                pkg.putStr(info.getItemCode());
                pkg.putByte(info.getShopType());
                pkg.putByte(info.getGoodType());
                pkg.putInt(info.getTemplateId());
                pkg.putStr(info.getShopPicPath());
                pkg.putStr(info.getName());
                pkg.putStr(info.getNamePicPath());
                pkg.putStr(info.getDescription());
                pkg.putDouble(info.getPrice());
            }
        } else {
        	pkg.putInt(0);
        }
        player.getOut().sendTCP(pkg);
        return 0;
    }
}
