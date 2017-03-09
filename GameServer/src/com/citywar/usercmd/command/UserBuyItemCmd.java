/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.citywar.common.BaseItem;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.ShopGoodInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.ShopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.PropType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 玩家购买商品
 * 
 * @author tracy
 * @date 2011-12-31
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.BUY_ITEM, desc = "购买商品")
public class UserBuyItemCmd extends AbstractUserCmd
{
    // TODO 存在购物车购买的形式, 怎样处理部分购买成功, 拉上客户端讨论 

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        boolean isUsedNow = packet.getBoolean(); // true 为游戏中购买立即使用,
                                                 // false为商城购买暂不使用
        int templateId = -1;
        List<BaseItem> buyItemList = new ArrayList<BaseItem>(); // 购买的物品列表
        int totalMoney = 0;
        int totalCoins = 0;
        /**
         * 购买了几种类型的商品<br>
         * 考虑每一种商品详细信息都是一样, 只需记录购买数量即可
         */
        int typeCount = 1;
        Map<Integer, Integer> goodsMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < typeCount; i++)
        {
            int goodId = packet.getInt();//templateId
            int count = 1;
            
            ShopGoodInfo shopGood = ShopMgr.getShopGoodById(goodId); // 获取商品信息 templateId
            if (shopGood == null)
                continue;
            templateId = shopGood.getTemplateId();
            switch (shopGood.getShopType())
            {
                case PropType.REMOVE_COINS:
                    totalCoins += shopGood.getPrice() * count;
                    break;
                case PropType.REMOVE_MONEY:
                    totalMoney += shopGood.getPrice() * count;
                default:
                    break;
            }
            // 计算当前类型商品的总价

            BaseItem baseItem = ShopMgr.createBaseItem(shopGood);// 构建商品信息
            // 将商品添加到购买列表中
            if (null != baseItem && player.getPropBag().canAddCount(baseItem.getItemTemplateInfo().getType(),count)) {
            	buyItemList.add(baseItem);
            	goodsMap.put(goodId, count);
            }
        }
        
        Packet pkg = new Packet(UserCmdOutType.BUY_ITEM);
        
        if (null != buyItemList && buyItemList.size() > 0) {
        	boolean removeResult = false;
        	boolean addItem = true;
            // 扣除银子 (可能是游戏币, 也可能是RMB),考虑是一起扣除还是分开
            removeResult = (player.removeMoney(totalMoney, totalCoins) == (totalMoney + totalCoins));
            if (removeResult)
            {
                for (BaseItem item : buyItemList)
                {
                    // 如果是放背包内的商品
                    if (item.getItemTemplateInfo().getType() == 0)
                    // 游戏币, 直接更改人物属性值
                    {
                        player.addCoins(item.getUserItemInfo().getCount(), false);
                        player.getPlayerInfo().setOp(Option.UPDATE);
                    }
                    else if (item.getItemTemplateInfo().getType() == -1)
                    // 钻石, 直接更改人物属性值
                    {
                        player.addMoney(item.getUserItemInfo().getCount());
                        player.getPlayerInfo().setOp(Option.UPDATE);
                    }
                    else
                    // 处理购买的物品
                    {
                        item.getUserItemInfo().setUserId(player.getUserId());
                        addItem = player.getPropBag().addItemAndUse(item,item.getUserItemInfo().getCount());
                    }

                }

            }
            
            if (!removeResult || !addItem)
            {
                pkg.putBoolean(false);
            }
            else
            {
                player.saveIntoDatabase(true);
                pkg.putBoolean(true);
//                pkg.putInt(goodsMap.size());
                for (Entry<Integer, Integer> entry : goodsMap.entrySet())
                {
                    pkg.putInt(entry.getKey());
//                    pkg.putInt(entry.getValue());
                }
            }
            player.getOut().sendTCP(pkg);
            player.getOut().sendUpdateBaseInfo();
            player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(), (byte)0);

            if(player.getCurrentRoom() != null)
    			player.getCurrentRoom().sendRoomPlayerCoin(player,totalCoins * -1);
            
            if (removeResult && isUsedNow) // 如果游戏中购买, 则立即使用
            {
            	for (BaseItem item : buyItemList) {
            		player.usePropItemByItemId(item.getUserItemInfo().getItemId(), 1);
            		int itemTypeOruserItemId = item.getUserItemInfo().getItemId();
                	if(null != player.getPropBag() && null != player.getPropBag().getBaseItemByItemId(itemTypeOruserItemId)
                			&& null != player.getPropBag().getBaseItemByItemId(itemTypeOruserItemId).getItemTemplateInfo()) {
                		itemTypeOruserItemId = player.getPropBag().getBaseItemByItemId(itemTypeOruserItemId).getItemTemplateInfo().getType();
                	}
                    player.updatePropUse(itemTypeOruserItemId, 1);
            	}
            }
        } else {
        	pkg.putInt(0);
        }
        // TODO 购买成功啥的, 放到 GamePlayer 里去提示客户端?
        return 0;
    }
}
