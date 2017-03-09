package com.citywar.usercmd.command;

import java.util.List;

import com.citywar.dice.entity.HallTypeInfo;
import com.citywar.dice.entity.StageInfo;
import com.citywar.gameobjects.GamePlayer;
import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;
import com.citywar.manager.StageMgr;
import com.citywar.socket.Packet;
import com.citywar.type.UserCmdOutType;
import com.citywar.type.UserCmdType;
import com.citywar.usercmd.AbstractUserCmd;
import com.citywar.usercmd.UserCmdAnnotation;
/**
 * 为了与的老版本兼容，还有满足需求
 * 这里增加了获得大厅类型列表的协议，主要是显示当前大厅的类型
 * @author shanfeng.cao
 *
 */
@UserCmdAnnotation(code = UserCmdType.HALL_TYPE_LIST, desc = "获得大厅类型列表")
public class UserGetHallTypesCmd extends AbstractUserCmd {

	@Override
	public int execute(GamePlayer player, Packet packet) {
		
		byte type = packet.getByte();//扩展的需要。也许将来还有分类型的多种大厅列表
		
		Packet response = new Packet(UserCmdOutType.HALL_TYPE_LIST);
		response.putInt(type);//大厅数量
		List<BaseHall> list = HallMgr.getHallTypesList((int)type);
		if (null != list && list.size() > 0) {
			response.putInt(list.size());//大厅数量
			for (BaseHall hall : list) {
				response.putInt(hall.getHallId());//大厅ID
				response.putInt(hall.getHallPlayerCount());//大厅在线人数
				HallTypeInfo hallType = hall.getHallType();
				if(null !=  hallType) {
					response.putInt(hallType.getHallTypeId());//大厅类型ID
					response.putStr(hallType.getHallTypeName());//大厅名字
					response.putInt(hallType.getWager());//大厅赌注
					response.putInt(hallType.getLowestCoins());//大厅最低用户金币数
					response.putInt(hallType.getForcedExitCoins());//强退扣除的金币数
					response.putStr(hallType.getNameImage());//大厅名字URL
				}
				
				StageInfo stage = hall.getNextStage();//getLastStage();
				response.putInt(hall.getStageState());
				response.putInt(hall.getStageCount());
				if(stage != null)
				{					
					response.putInt(stage.getStageId());
					response.putInt(stage.getStageType());
					response.putStr(stage.getPic());
					response.putStr(stage.getStartTime().getHours() + ":" + stage.getStartTime().getMinutes());
					response.putStr(stage.getEndTime().getHours() + ":" + stage.getEndTime().getMinutes());
					response.putStr(StageMgr.findStagePrizeByIndex(stage.getStageId(), 1).getPrizeName());
				}
				
				
			}
		}
		player.getOut().sendTCP(response);
		return 0;
	}
}
