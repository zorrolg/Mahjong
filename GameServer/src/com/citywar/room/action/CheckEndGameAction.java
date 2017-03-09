package com.citywar.room.action;

import com.citywar.hall.BaseHall;
import com.citywar.manager.HallMgr;

public class CheckEndGameAction extends BaseDelayRoomAction
{

	private int hallId;
//    private static Logger logger = Logger.getLogger(CheckStageRoundAction.class.getName());

    
    public CheckEndGameAction(int hallId, int delay)
    {
    	super(delay);
    	this.hallId = hallId;
    	
    }


	@Override
	protected void executeImp() {
		// TODO Auto-generated method stub
					        
		System.out.println("CheckStageRoundAction=================================================================================================================");
		
		BaseHall hall = HallMgr.getHallById(this.hallId);	    	
		hall.checkStageRound(false);	        
        
	}
    
}
