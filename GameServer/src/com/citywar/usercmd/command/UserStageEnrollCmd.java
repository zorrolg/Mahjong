package com.citywar.usercmd.command;



import com.citywar.bll.common.LanguageMgr;
import com.citywar.dice.entity.StageInfo;
import com.citywar.game.GamePlayer;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.manager.StageMgr;
import com.citywar.manager.UserTopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;



@UserCmdAnnotation(code = UserCmdType.STAGE_ENROLL, desc = "玩家当前正在做的任务")
public class UserStageEnrollCmd extends AbstractUserCmd{

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		int stageid = packet.getInt();
		
		if(stageid == 0)
			return 0;
		
		int isInStage = 0;
		String strMsg;
		
		
		
		StageInfo stageInfo = StageMgr.findStage(stageid);
		BaseHall baseHall = HallMgr.getHallById(stageInfo.getHallTypeId());
		
		
		int StageState = baseHall.getStageState();
		if(StageState == 1)
		{

			if(!UserTopMgr.isStageEnroll(stageInfo.getStageId(), player.getUserId()))
			{
				if(baseHall.addStagePlayer(player))
				{
//					UserTopMgr.addStageTop(stageInfo.getStageId(), player.getPlayerInfo(), stageInfo.getDefaultScore());
//					player.setCurrentStage(stageid);
					
					isInStage = 1;
					strMsg = LanguageMgr.getTranslation("CityWar.StageEnroll.Sucess");	
				}
				else
				{
					strMsg = LanguageMgr.getTranslation("CityWar.StageEnroll.Enough");		
				}
					
			}
			else
			{
				strMsg = LanguageMgr.getTranslation("CityWar.StageEnroll.Already");		
			}
						
		}
		else
		{
			strMsg = LanguageMgr.getTranslation("CityWar.StageEnroll.NotTime");			
		}
		
		
		Packet pkg = new Packet(UserCmdOutType.STAGE_ENROLL);
		pkg.putInt(isInStage);
		pkg.putStr(strMsg);
        player.getOut().sendTCP(pkg);
         
         
		return 0;
	}
}
