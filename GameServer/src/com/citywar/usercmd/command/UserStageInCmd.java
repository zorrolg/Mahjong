package com.citywar.usercmd.command;

import java.sql.Timestamp;

import com.citywar.dice.entity.StageInfo;
import com.citywar.dice.entity.StageRoundInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.manager.StageMgr;
import com.citywar.manager.UserTopMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
import com.citywar.util.TimeUtil;

@UserCmdAnnotation(code = UserCmdType.STAGE_IN, desc = "玩家当前正在做的任务")
public class UserStageInCmd extends AbstractUserCmd{

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		int stageid = packet.getInt();
		
		if(stageid == 0)
			return 0;
		
		
		 	StageInfo stageInfo = StageMgr.findStage(stageid);
			BaseHall baseHall = HallMgr.getHallById(stageInfo.getHallTypeId());
			
			int StageState = baseHall.getStageState();
			
			long time = 0;
			int isInStage = 0;
			int allPlayerCount = 0;
			int nowPlayerCount = 0;
			if(StageState == 0)
			{
				StageInfo stageNextInfo = baseHall.getNextStage();
				Timestamp timeEnd = stageNextInfo.getStartTime();
				time = TimeUtil.getTimeSpanInDay(timeEnd, TimeUtil.getSysteCurTime());

			}
			else if(StageState == 1)
			{
				Timestamp timeEnd = stageInfo.getStartTime();
				time = TimeUtil.getTimeSpanInDay(timeEnd, TimeUtil.getSysteCurTime());
				
				StageRoundInfo round = baseHall.getCurrentStageRound();
				if(round != null)
					allPlayerCount = round.getPlayerCount();
				
				nowPlayerCount = UserTopMgr.getStageTopList(stageid).size();
				
				if(baseHall.isInStagePlayer(player))
					isInStage = 1;
				
			}
			else if(StageState == 2)
			{
				Timestamp timeEnd = stageInfo.getEndTime();
				time = TimeUtil.getTimeSpanInDay(timeEnd, TimeUtil.getSysteCurTime());
			}
			
			
			
			
			
			
			Packet pkg = new Packet(UserCmdOutType.STAGE_IN);
			pkg.putInt(StageState);
	    	pkg.putLong(time);	    	
	    	pkg.putInt(isInStage);
	    	pkg.putInt(allPlayerCount);	 
	    	pkg.putInt(nowPlayerCount);	 
	    	pkg.putStr(stageInfo.getDescript());
	    	pkg.putStr(StageMgr.findStagePrizeByIndex(stageInfo.getStageId(), 1).getPrizeName());
	    	
	        player.getOut().sendTCP(pkg);
         
         
     
		return 0;
	}
}
