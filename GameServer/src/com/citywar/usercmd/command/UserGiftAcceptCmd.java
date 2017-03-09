package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.dice.entity.GiftBag;
import com.citywar.dice.entity.ItemTemplateInfo;
import com.citywar.dice.entity.ShopGoodInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.GiftBagMgr;
import com.citywar.manager.ShopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

@UserCmdAnnotation(code = UserCmdType.GET_GIFT_BAG, desc = "用户查看礼包")
public class UserGiftAcceptCmd extends AbstractUserCmd{

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
//		int giftBagId = packet.getInt();
		int giftBagId = GiftBagMgr.LOGIN_GIFT_BAG;
		  
		
		//给用户加上礼包物品
		GiftBagMgr.acceptGiftBag(player, giftBagId);
		
		//告诉客户端礼包物品内容
		GiftBag bag = GiftBagMgr.getGiftBagById(giftBagId);
		
		if(bag != null )
		{
			List<ItemTemplateInfo> items = bag.getGiftBagItems();
			
			Packet response = new Packet(UserCmdOutType.GET_GIFT_BAG);
			response.putInt(bag.getType());
			if(items == null || items.size() == 0 )
			{
				response.putInt(0);
			}else{
				response.putInt(items.size());
				List<ShopGoodInfo> list = null;
				ShopGoodInfo shop = null;
				for(ItemTemplateInfo item :items)
				{
					list = ShopMgr.getGoodsByGoodsItemType(item.getType());
					if(null != list && list.size() > 0) {
						shop = list.get(0);
					}
					response.putStr(item.getName());//物品名称
					response.putStr(item.getPropertyDesc());//数量描述
					response.putStr(null != shop ? shop.getShopPicPath() : "");//物品图片
					response.putStr(null != shop ? shop.getDescription() : "");//物品描述
				}
			}
			player.sendTcp(response);
			
			//更新玩家物品
			 player.getOut().sendUpdateBaseInfo();
			 player.getOut().sendUpdatePrivateInfo(player.getPlayerInfo(),(byte)2);
		}
		
		return 0;
	}

}
