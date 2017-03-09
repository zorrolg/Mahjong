/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.dao.manager;

import com.citywar.dice.dao.AroundDao;
import com.citywar.dice.dao.BuildDao;
import com.citywar.dice.dao.CardDao;
import com.citywar.dice.dao.CityDao;
import com.citywar.dice.dao.CodeDao;
import com.citywar.dice.dao.DataStatisticsDao;
import com.citywar.dice.dao.DayRecordDao;
import com.citywar.dice.dao.DayRetainedRateDao;
import com.citywar.dice.dao.DrawGoodDao;
import com.citywar.dice.dao.FeedbackDao;
import com.citywar.dice.dao.FiveMinSummaryDao;
import com.citywar.dice.dao.FriendsDao;
import com.citywar.dice.dao.GameTypeCountDao;
import com.citywar.dice.dao.GiftBagDao;
import com.citywar.dice.dao.GiftTemplateDao;
import com.citywar.dice.dao.HallTypeDao;
import com.citywar.dice.dao.ItemTemplateDao;
import com.citywar.dice.dao.LevelDao;
import com.citywar.dice.dao.PengConfigDao;
import com.citywar.dice.dao.PengGameDao;
import com.citywar.dice.dao.PengGameDetailDao;
import com.citywar.dice.dao.PlayerInfoDao;
import com.citywar.dice.dao.PushInfoDao;
import com.citywar.dice.dao.RobotChatDao;
import com.citywar.dice.dao.RobotDao;
import com.citywar.dice.dao.RobotLevelDao;
import com.citywar.dice.dao.ServerConfigDao;
import com.citywar.dice.dao.ServerUserDao;
import com.citywar.dice.dao.ShopGoodDao;
import com.citywar.dice.dao.StageDao;
import com.citywar.dice.dao.StageDataDao;
import com.citywar.dice.dao.StagePlayerDao;
import com.citywar.dice.dao.StagePrizeDao;
import com.citywar.dice.dao.TaskDao;
import com.citywar.dice.dao.UserAwardDao;
import com.citywar.dice.dao.UserBuildDao;
import com.citywar.dice.dao.UserCardDao;
import com.citywar.dice.dao.UserDayActivityDao;
import com.citywar.dice.dao.UserGiftDao;
import com.citywar.dice.dao.UserItemDao;
import com.citywar.dice.dao.UserLetterDao;
import com.citywar.dice.dao.UserMessageTokenDao;
import com.citywar.dice.dao.UserOrderDao;
import com.citywar.dice.dao.UserPropertyDao;
import com.citywar.dice.dao.UserReferenceDao;
import com.citywar.dice.dao.UserReferenceLogDao;
import com.citywar.dice.dao.UserRegisterDao;
import com.citywar.dice.dao.UserReportDao;
import com.citywar.dice.dao.UserRewardDao;
import com.citywar.dice.dao.UserTaskDao;
import com.citywar.dice.dao.UserVIPEditionDao;
import com.citywar.dice.dao.VersionDao;
import com.citywar.dice.dao.VersionRiseDao;
import com.citywar.dice.dao.impl.AroundDaoImpl;
import com.citywar.dice.dao.impl.BuildDaoImpl;
import com.citywar.dice.dao.impl.CardDaoImpl;
import com.citywar.dice.dao.impl.CityDaoImpl;
import com.citywar.dice.dao.impl.CodeDaoImpl;
import com.citywar.dice.dao.impl.DataStatisticsDaoImpl;
import com.citywar.dice.dao.impl.DayRecordDaoImpl;
import com.citywar.dice.dao.impl.DayRetainedRateDaoImpl;
import com.citywar.dice.dao.impl.DrawGoodDaoImpl;
import com.citywar.dice.dao.impl.FeedbackDaoImpl;
import com.citywar.dice.dao.impl.FiveMinSummaryDaoImpl;
import com.citywar.dice.dao.impl.FriendsDaoImpl;
import com.citywar.dice.dao.impl.GameTypeCountDaoImpl;
import com.citywar.dice.dao.impl.GiftBagDaoImpl;
import com.citywar.dice.dao.impl.GiftTemplateDaoImpl;
import com.citywar.dice.dao.impl.HallTypeDaoImpl;
import com.citywar.dice.dao.impl.ItemTemplateDaoImpl;
import com.citywar.dice.dao.impl.LevelDaoImpl;
import com.citywar.dice.dao.impl.PengConfigDaoImpl;
import com.citywar.dice.dao.impl.PengGameDaoImpl;
import com.citywar.dice.dao.impl.PengGameDetailDaoImpl;
import com.citywar.dice.dao.impl.PlayerInfoDaoImpl;
import com.citywar.dice.dao.impl.PushInfoDaoImpl;
import com.citywar.dice.dao.impl.RobotChatDaoImpl;
import com.citywar.dice.dao.impl.RobotDaoImpl;
import com.citywar.dice.dao.impl.RobotLevelDaoImpl;
import com.citywar.dice.dao.impl.ServerConfigDaoImpl;
import com.citywar.dice.dao.impl.ServerUserDaoImpl;
import com.citywar.dice.dao.impl.ShopGoodDaoImpl;
import com.citywar.dice.dao.impl.StageDaoImpl;
import com.citywar.dice.dao.impl.StageDataDaoImpl;
import com.citywar.dice.dao.impl.StagePlayerDaoImpl;
import com.citywar.dice.dao.impl.StagePrizeDaoImpl;
import com.citywar.dice.dao.impl.TaskDaoImpl;
import com.citywar.dice.dao.impl.UserAwardDaoImpl;
import com.citywar.dice.dao.impl.UserBuildDaoImpl;
import com.citywar.dice.dao.impl.UserCardDaoImpl;
import com.citywar.dice.dao.impl.UserDayActivityDaoImpl;
import com.citywar.dice.dao.impl.UserGiftDaoImpl;
import com.citywar.dice.dao.impl.UserItemDaoImpl;
import com.citywar.dice.dao.impl.UserLetterDaoImpl;
import com.citywar.dice.dao.impl.UserMessageTokenDaoImpl;
import com.citywar.dice.dao.impl.UserOrderDaoImpl;
import com.citywar.dice.dao.impl.UserPropertyDaoImpl;
import com.citywar.dice.dao.impl.UserReferenceDaoImpl;
import com.citywar.dice.dao.impl.UserReferenceLogDaoImpl;
import com.citywar.dice.dao.impl.UserRegisterDaoImpl;
import com.citywar.dice.dao.impl.UserReportDaoImpl;
import com.citywar.dice.dao.impl.UserRewardDaoImpl;
import com.citywar.dice.dao.impl.UserTaskDaoImpl;
import com.citywar.dice.dao.impl.UserVIPEditionDaoImpl;
import com.citywar.dice.dao.impl.VersionDaoImpl;
import com.citywar.dice.dao.impl.VersionRiseDaoImpl;

/**
 * @author charles
 * @date 2011-12-16
 * @version
 * 
 */
public class DaoManager
{
    /**
     * 
     * @return 用户Dao实例
     */
    public static PlayerInfoDao getPlayerInfoDao()
    {
        return new PlayerInfoDaoImpl();
    }

    /**
     * 
     * @return 用户
     */
    public static CityDao getCityDao()
    {
        return new CityDaoImpl();
    }
    
    /**
     * 
     * @return 用户
     */
    public static UserBuildDao getPlayerBuildInfoDao()
    {
        return new UserBuildDaoImpl();
    }
    
    /**
     * 
     * @return 用户
     */
    public static UserCardDao getPlayerCardInfoDao()
    {
        return new UserCardDaoImpl();
    }
    
    /**
     * 获取DAO
     */
    public static BuildDao getBuildsDao()
    {
        return new BuildDaoImpl();
    }
    
    /**
     * 获取DAO
     */
    public static UserReportDao getUserReportDao()
    {
        return new UserReportDaoImpl();
    }
    
    /**
     * 获取DAO
     */
    public static CardDao getCardsDao()
    {
        return new CardDaoImpl();
    }
    
    /**
     * 获取DAO
     */
    public static StageDao getStagesDao()
    {
        return new StageDaoImpl();
    }
    
    /**
     * 获取DAO
     */
    public static PengConfigDao getPengConfigDao()
    {
        return new PengConfigDaoImpl();
    }
    
    /**
     * 获取DAO
     */
    public static PengGameDao getPengGameDao()
    {
        return new PengGameDaoImpl();
    }
    
    /**
     * 获取DAO
     */
    public static PengGameDetailDao getPengGameDetailDao()
    {
        return new PengGameDetailDaoImpl();
    }
    
    /**
     * 获取好友DAO
     */
    public static FriendsDao getFriendsDao()
    {
        return new FriendsDaoImpl();
    }

    /**
     * 玩家物品 DAO 实例
     * 
     * @return
     */
    public static UserItemDao getUserItemDao()
    {
        return new UserItemDaoImpl();
    }

    /**
     * 
     * @return 用户
     */
    public static ServerUserDao getServerUserDao()
    {
        return new ServerUserDaoImpl();
    }
    
    /**
     * 物品模板 DAO 实例
     * 
     * @return
     */
    public static ItemTemplateDao getItemTemplateDao()
    {
        return new ItemTemplateDaoImpl();
    }

    /**
     * 商店商品 DAO 实例
     * 
     * @return
     */
    public static ShopGoodDao getShopGoodDao()
    {
        return new ShopGoodDaoImpl();
    }

    /**
     * 商店商品 DAO 实例
     * 
     * @return
     */
    public static DrawGoodDao getDrawGoodDao()
    {
        return new DrawGoodDaoImpl();
    }
    /**
     * 获取附近用户DAO
     * 
     * @return
     */
    public static AroundDao getAroundDao()
    {
        return new AroundDaoImpl();
    }

    /**
     * 等级管理 DAO 实例
     * 
     * @return
     */
    public static LevelDao getLevelDao()
    {
        return new LevelDaoImpl();
    }
    
    /**
     * 获取机器人Dao
     * @return
     */
    public static RobotDao getRobotDao()
    {
    	return new RobotDaoImpl();
    }
    
    /**
     * 辅助用, 插入生成最新 ID
     * @return
     */
    public static CodeDao getCodeDao()
    {
        return new CodeDaoImpl();
    }
    /**
     * 获取任务信息Dao
     * @return
     */
    public static TaskDao getTaskDao() 
    {
    	return new TaskDaoImpl();
    }
    /**
     * 获取用户完成任务信息Dao
     * @return
     */
    public static UserTaskDao getUserTaskDao()
    {
    	return new UserTaskDaoImpl();
    }
    /**
     * 获取用户订单信息Dao
     * @return
     */
    public static UserOrderDao getUserOrderDao()
    {
    	return new UserOrderDaoImpl();
    }
    /**
     * 获取用户奴隶关系Dao
     * @return UserReferenceDao
     */
    public static UserReferenceDao getUserReferenceDao()
    {
    	return new UserReferenceDaoImpl();
    }
    /**
     * 获取用户奴隶关系日志Dao
     * @return UserReferenceLogDao
     */
    public static UserReferenceLogDao getUserReferenceLogDao()
    {
    	return new UserReferenceLogDaoImpl();
    }
    /**
     * 获取用户反馈信息Dao
     * @return FeedbackDao
     */
	public static FeedbackDao getFeedbackDao() {
		return new FeedbackDaoImpl();
	}
	public static UserRewardDao getUserRewardDao() {
		return new UserRewardDaoImpl();
	}
	public static UserLetterDao getUserLetterDao() {
		return new UserLetterDaoImpl();
	}
	/**
     * 获取礼品模板Dao
     * @return GiftTemplateDao
     */
	public static GiftTemplateDao getGiftTemplateDao() {
		return new GiftTemplateDaoImpl();
	}
	
	/** 
	 * 获取礼包Dao
	 * @return
	 */
	public static GiftBagDao getGiftBagDao(){
		return new GiftBagDaoImpl();
	}
	/**
     * 获取用户礼品Dao
     * @return GiftTemplateDao
     */
	public static UserGiftDao getUserGiftDao() {
		return new UserGiftDaoImpl();
	}
	
	public static FiveMinSummaryDao getFiveMinSummaryDao()
	{
		return new FiveMinSummaryDaoImpl();
	}
	
	public static DayRecordDao getDayRecordDao()
	{
		return new DayRecordDaoImpl();
	}
	
	public static VersionDao getVersionDao(){
		return new VersionDaoImpl();
	}
	public  static UserRegisterDao getUserRegisterDao(){
		return new UserRegisterDaoImpl();
	}
	
	public static ServerConfigDao getServerConfigDao(){
		return new ServerConfigDaoImpl();
	}
	
	public static VersionRiseDao getVersionRiseDao(){
		return new VersionRiseDaoImpl();
	}

	public static UserAwardDao getUserAwardDao() {
		return new UserAwardDaoImpl();
	}
	
	public static RobotChatDao getRobotChtDao() {
		return new RobotChatDaoImpl();
	}
	
	public static RobotLevelDao getRobotLevelDao() {
		return new RobotLevelDaoImpl();
	}

    public static PushInfoDao getPushInfoDao()
    {
    	return new PushInfoDaoImpl();
    }
    
	public static HallTypeDao getHallTypeDao() {
		return new HallTypeDaoImpl();
	}

	public static StageDao getStageDao() {
		return new StageDaoImpl();
	}
	
	public static StageDataDao getStageDataDao() {
		return new StageDataDaoImpl();
	}
	
	public static StagePlayerDao getStagePlayerDao() {
		return new StagePlayerDaoImpl();
	}

	public static StagePrizeDao getStagePrizeDao() {
		return new StagePrizeDaoImpl();
	}
		
	
	public static DayRetainedRateDao getDayRetainedRateDao() {
		return new DayRetainedRateDaoImpl();
	}

	public static DataStatisticsDao getDataStatisticsDao() {
		return new DataStatisticsDaoImpl();
	}

	public static GameTypeCountDao getGameTypeCountDao() {
		return new GameTypeCountDaoImpl();
	}

	public static UserDayActivityDao getUserDayActivityDao() {
		return new UserDayActivityDaoImpl();
	}
	
	public static UserMessageTokenDao getUserMessageTokenDao() {
		return new UserMessageTokenDaoImpl();
	}

	public static UserPropertyDao getUserPropertyDao() {
		return new UserPropertyDaoImpl();
	}

	public static UserVIPEditionDao getUserVIPEditionDao() {
		return new UserVIPEditionDaoImpl();
	}
}
