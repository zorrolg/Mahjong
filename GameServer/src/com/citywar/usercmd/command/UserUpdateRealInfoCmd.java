/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd.command;

import com.citywar.game.GamePlayer;
import com.citywar.gameutil.UserProperty;
import com.citywar.socket.Packet;
import com.citywar.type.PropType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.IDCard;

/**
 * 玩家更新注册信息, 完善实名制信息。为防沉迷等等系统提供资料
 * 
 * @author shanfeng.cao
 * @date 2012-09-05
 * @version
 * 
 */
@UserCmdAnnotation(code = UserCmdType.USER_REAL_REG, desc = "实名制注册")
public class UserUpdateRealInfoCmd extends AbstractUserCmd
{

    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        boolean isSucceed = false;
        byte resulteState = 2;
        String identityCard = packet.getStr();//身份证ID
        String realName = packet.getStr();//真实姓名
        UserProperty userProperty = player.getUserProperty();
        if(userProperty == null) {
        	return 0;
        }
        String resultStr = IDCard.IDCardValidate(identityCard);
        if(null == resultStr || resultStr.isEmpty()) {
        	isSucceed = player.getUserProperty().insertUserProperty(identityCard, realName);
        }
        if(isSucceed) {
    		resulteState = 1 ;
        	resultStr = "恭喜您，通过实名验证，获得奖励大喇叭*2！";
        	player.getPropBag().addPrizeItemCount(PropType.TRUMPET, 2);
        	player.getOut().sendUpdateBaseInfo();
    	}
        player.getOut().sendAntiAddiction(resulteState,resultStr);
        return 0;
    }
}
