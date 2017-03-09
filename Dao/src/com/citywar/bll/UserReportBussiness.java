package com.citywar.bll;

import com.citywar.dice.dao.manager.DaoManager;

public class UserReportBussiness {
	
//	private final static String REPORT_TABLE = "t_s_report";//静态的任务数据表
	
	
	
    public static int addUserReport(int ReportUserId,int UserId,String PicName)
    {
        return DaoManager.getUserReportDao().addUserReport(ReportUserId,UserId,PicName);
    }
    
}
