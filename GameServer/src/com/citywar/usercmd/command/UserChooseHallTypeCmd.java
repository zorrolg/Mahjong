package com.citywar.usercmd.command;

import org.apache.log4j.Logger;

import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.StageInfo;
import com.citywar.game.GamePlayer;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.manager.UserTopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.HallGameType;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
/**
 * 
 * 多种大厅供给用户选择大厅类型，单用户选择之后，需要检测用户是否满足条件
 * @author zhiyun.peng
 * 
 * @verion 2.2
 *
 */
@UserCmdAnnotation(code = UserCmdType.USER_CHOOSE_HALL_TYPE, desc = "用户选择大厅类型")
public class UserChooseHallTypeCmd extends AbstractUserCmd {

	 private static final Logger logger = Logger.getLogger(UserChooseHallTypeCmd.class.getName());
	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		int hallId = packet.getInt();//大厅Id
		BaseHall hall = HallMgr.getHallById(hallId);
		
		
		
		
		String msg = "";
		boolean isSuccess = false;					
		if(null != hall)
		{
						
			boolean isHallTypeIn = false;
			if(hall.getHallType().getHallType() == HallGameType.CONTEST)
			{
				int StageState = hall.getStageState();
				if(StageState == 0)
				{					
					msg = LanguageMgr.getTranslation("CityWar.EnterHall.NotOpenTime");				
										
				}
				else if(StageState == 1)
				{
					msg = LanguageMgr.getTranslation("CityWar.EnterHall.IsReadyTime");				
					
				}
				else if(StageState == 2)
				{
					StageInfo currentStage = hall.getCurrentStage();
					if(currentStage != null)
					{
						if(UserTopMgr.isStageEnroll(currentStage.getStageId(), player.getUserId()))
						{
							isHallTypeIn = true;
						}
						else
						{
							msg = LanguageMgr.getTranslation("CityWar.EnterHall.NotStageEnroll");				
						}
					}
					
					
				}
				
			}
			else
			{
				isHallTypeIn = true;
			}
			
			
			if(isHallTypeIn)
			{
				if(hall.isStayHall(player))												
					isSuccess = true;
				else				
					msg = LanguageMgr.getTranslation("CityWar.EnterHall.NoEnoughCoin");				
			}
					
			
			
		}
		
		if(isSuccess){//进入大厅
						
			hall.addIdlePlayer(player);
			player.setCurrenetHall(hall);
//			player.setCurrenetHallGameType(hall.getHallType().getHallType());
			sendReturnMessage(player,true,msg,hallId);
		}else{
			sendReturnMessage(player,false,msg,hallId);
		}
		return 0;
	}
	
    private void sendReturnMessage(GamePlayer player,boolean susseed, String message,int hallId) {
		Packet response = new Packet(UserCmdOutType.USER_CHOOSE_HALL_TYPE);
		response.putInt(hallId);
		response.putBoolean(susseed);//是否成功
		if( ! susseed) {
			response.putStr(message);//提示信息
		}
		player.sendTcp(response);
    }
}
