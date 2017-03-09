/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.common.BaseItem;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 查看用户背包信息
 * 
 * @author shanfeng.cao
 * @date 2012-07-30
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.USER_ITEM_BAG_SHOW, desc = "查看用户背包信息")
public class ShowUserItemBagCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        // userId 查看的用户ID（扩展用）
        int userId = packet.getInt();
        // 构建道具  背包相关
        
        List<BaseItem> itemList = player.getPropBag().getAllItems();
        Packet pkg = new Packet(UserCmdOutType.USER_ITEM_BAG_SHOW);
        pkg.putInt(userId);//现在是查看自己的
        if (null != itemList && itemList.size() > 0) {
        	pkg.putInt(itemList.size()); // 几个类型的道具
        	for (BaseItem item : itemList)
        	{
        		pkg.putInt(item.getUserItemInfo().getTemplateId()); // 道具模板id
        		pkg.putInt(item.getUserItemInfo().getCount()); // 拥有此类型道具的数量
        	}
        } else {
        	pkg.putInt(0);
        }
        player.getOut().sendTCP(pkg);
      
//        List<Integer> propTypeList = player.getPropBag().getAllItemTypes();
//        Packet pkg = new Packet(UserCmdOutType.USER_ITEM_BAG_SHOW);
//        pkg.putInt(userId);//现在是查看自己的
//        if (null != propTypeList && propTypeList.size() > 0) {
//            pkg.putInt(propTypeList.size()); // 几个类型的道具
//            for (Integer propType : propTypeList)
//            {
//                pkg.putInt(propType); // 道具模板id
//                pkg.putInt(player.getPropBag().getPropCountByType(propType)); // 拥有此类型道具的数量
//                List<ShopGoodInfo> tpyeList = ShopMgr.getGoodsByGoodsItemType(propType);
//                ShopGoodInfo shopGoods = null;
//                if(null != tpyeList) {
//                	shopGoods = tpyeList.get(0);//取其中一个
//                    pkg.putByte(shopGoods.getShopType());//商店类型(礼品商店, 道具商店等)
//                    pkg.putByte(shopGoods.getGoodType());//商品类型(醒酒类, 表情类等)
//                    pkg.putStr(shopGoods.getShopPicPath());//商品图片Path
//                    pkg.putStr(shopGoods.getName());//商品名称
//                    pkg.putStr(shopGoods.getNamePicPath());//商品名字图片Path
//                    pkg.putStr(shopGoods.getDescription());//商品描述
//                    pkg.putFloat(shopGoods.getPrice());//商品价格
//                    pkg.putStr(shopGoods.getUsePresentation());//商品使用提示语
//                }
//            }
//        } else {
//        	pkg.putInt(0);
//        }
//        player.getOut().sendTCP(pkg);
        return 0;
    }

}
