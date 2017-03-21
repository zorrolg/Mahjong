/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import com.citywar.bll.common.LanguageMgr;
import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;

/**
 * 玩家使用道具
 * 
 * @author tracy
 * @date 2012-1-4
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.PROP_USE, desc = "玩家使用道具")
public class UserPropUseCmd extends AbstractUserCmd
{
//	private  static final String UNSUCCEESSFUL_RESULT = "使用失败！";
//	private  static final String SUCCEED_RESULT = "使用成功！";
//	private  static final String IS_USED_RESULT = "当前还有酒未使用完，用完再来开下一瓶吧";
	
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
    	byte type = packet.getByte();//1: 代表通过道具类型 2：代表通过用户的道具ID（扩展用，现在默认为1）
    	int itemTypeOruserItemId = packet.getInt();//道具类型ID或者用户的道具ID
    	int useCount = packet.getInt();//使用的数量（扩展用，现在默认为1）
    	boolean result = false;
    	String resultMessage = LanguageMgr.getTranslation("CityWar.UseItem.Failed");
    	
    	
    	//System.out.println("UserPropUseCmd====================1============");
    	
    	
    	switch (type)
        {
	        case 1:

	        	result = player.usePropItemByType(itemTypeOruserItemId, useCount); // 使用道具的相关逻辑
	            break;
	        case 2:
	            result = player.usePropItemByItemId(itemTypeOruserItemId, useCount); // 使用道具的相关逻辑
	            break;
	        default:
	            break;
        }

        if (!result)
        {
            // TODO 处理使用失败的逻辑PropType
        } else {
        	if(resultMessage.equals(LanguageMgr.getTranslation("CityWar.UseItem.Failed"))) {
            	resultMessage = LanguageMgr.getTranslation("CityWar.UseItem.Success");
        	}
        	if(type == 2 && null != player.getPropBag() && null != player.getPropBag().getBaseItemByItemId(itemTypeOruserItemId)
        			&& null != player.getPropBag().getBaseItemByItemId(itemTypeOruserItemId).getItemTemplateInfo()) {
        		itemTypeOruserItemId = player.getPropBag().getBaseItemByItemId(itemTypeOruserItemId).getItemTemplateInfo().getType();
        	}
        	player.updatePropUse(itemTypeOruserItemId, useCount);
        }
//        PacketLib pkg = new PacketLib(player);
        Packet response = new Packet(UserCmdOutType.PROP_USE);
        response.putInt(itemTypeOruserItemId);
        response.putBoolean(result);
        response.putStr(resultMessage);
        player.getOut().sendTCP(response);
        return 0;
    }
}
