package com.citywar.usercmd.command;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.manager.WorldMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.DirtyCharUtil;

@UserCmdAnnotation(code = UserCmdType.USE_TRUMPET, desc = "玩家使用大喇叭")
public class UserUseTrumpet extends AbstractUserCmd
{
    @Override
    public int execute(GamePlayer player, Packet packet)
    {
        
    	Packet response = new Packet(UserCmdOutType.USE_TRUMPET);
       
    	String msg = packet.getStr();    	
        int validateResult = validate(msg, player);
       
        response.putByte((byte)validateResult);//验证结果
        response.putInt(2);   //优先级
        response.putInt(player.getPlayerInfo().getUserId());   //发送者的ID
        response.putStr(player.getPlayerInfo().getUserName()); //发送者的名称
        response.putStr(msg);//喇叭内容
        
        
        if( validateResult == ValidateResult.PASS )
        {
             WorldMgr.sendToAll(response);
             player.getOut().sendUpdateBaseInfo();
        }else
        {
        	player.getOut().sendTCP(response);
        }
        return 0;
    }
    
    public int validate(String msg, GamePlayer sender){
    	
    	if(msg.isEmpty())
    		return ValidateResult.MES_EMPTY;
    	
    	if(DirtyCharUtil.checkIllegalChar(msg))
    		return ValidateResult.WORD_DIRTY;
    	
    	if(!sender.usePropItemByType(15001, 1))
    		return ValidateResult.LACK_TRUMPET;
    	
    	return ValidateResult.PASS;	
    }
}

class  ValidateResult{
	
	/** 验证通过 */
	public  static byte PASS   = 0;
	/** 消息为空 */
	public  static byte MES_EMPTY = 1;
	/** 使用喇叭失败   */
	public  static byte LACK_TRUMPET = 2;
	/** 使用喇叭失败   */
	public  static byte WORD_DIRTY = 3;
 }
