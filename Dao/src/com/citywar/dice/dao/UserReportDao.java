package com.citywar.dice.dao;


public interface UserReportDao extends BaseDao{
	
	
	int addUserReport(int ReportUserId,int UserId,String PicName);
    
}
