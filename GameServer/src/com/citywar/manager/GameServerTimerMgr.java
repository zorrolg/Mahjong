/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.citywar.bll.DataStatisticsBussiness;
import com.citywar.bll.DayRecordBussiness;
import com.citywar.bll.DayRetainedRateBussiness;
import com.citywar.bll.FiveMinSummaryBussiness;
import com.citywar.bll.GameTypeCountBussiness;
import com.citywar.bll.UserDayActivityBussiness;
import com.citywar.dice.entity.DayRecordInfo;
import com.citywar.dice.entity.DayRetainedRateInfo;
import com.citywar.dice.entity.FiveMinSummaryInfo;
import com.citywar.dice.entity.GameTypeCountInfo;
import com.citywar.game.GamePlayer;
import com.citywar.gameutil.GamePlayerUtil;
import com.citywar.util.Config;
import com.citywar.util.TimeUtil;

/**
 * @author Dream
 * @date 2011-8-2
 * @version
 * 
 */
public class GameServerTimerMgr
{
    private static final Logger log = Logger.getLogger(GameServerTimerMgr.class.getName());
    private static ScheduledExecutorService m_saveDbService;
    private static ScheduledExecutorService m_pingCheckService;
    private static ScheduledExecutorService m_updateRobotService;
    private static ScheduledExecutorService m_oneMinSummaryService;
    private static ScheduledExecutorService m_fiveMinSummaryService;
    private static ScheduledExecutorService m_everyHourSummaryService;
    private static Timer m_saveDbTimer;

    public static void setup()
    {
    	
    	
    	
    	
        // 玩家角色数据定时保存
        int interval = Integer.valueOf(Config.getValue("DBSaveInterval"));
        m_saveDbService = Executors.newScheduledThreadPool(2);
        m_saveDbService.scheduleAtFixedRate(new SaveServiceTask(), 0, interval, TimeUnit.MINUTES);
        
        // Ping检查网关
        interval = Integer.valueOf(Config.getValue("PingCheckInterval"));
        m_pingCheckService = Executors.newScheduledThreadPool(2);
        m_pingCheckService.scheduleAtFixedRate(new PingCheckTask(), 0, interval, TimeUnit.MINUTES);
        
        // 定时保存机器人信息  
        interval = Integer.valueOf(Config.getValue("RobotSaveInterval"));
        m_updateRobotService = Executors.newScheduledThreadPool(2);
        m_updateRobotService.scheduleAtFixedRate(new UpdateRobotTask(), 0, interval, TimeUnit.MINUTES);
        
        //五分钟统计定时保存
        interval = Integer.valueOf(Config.getValue("FiveMinSummaryInterval"));
        m_fiveMinSummaryService = Executors.newScheduledThreadPool(2);
        m_fiveMinSummaryService.scheduleAtFixedRate(new FiveMinSummaryTask(), 0, interval, TimeUnit.MINUTES);
        
        //每分钟任务
        interval = 1 * 60;//每分钟
        long delayTime = 60 - System.currentTimeMillis() / 1000 % 60;
        System.out.println("GameServerTimerMgr=======================" + System.currentTimeMillis() + "=====" + delayTime);
        m_oneMinSummaryService = Executors.newScheduledThreadPool(2);
        m_oneMinSummaryService.scheduleAtFixedRate(new OneMinSummaryTask(), delayTime, interval, TimeUnit.SECONDS);
        
        // 每个小时执行的任务
        interval = Integer.valueOf(Config.getValue("everyHourTaskInterval"));
        m_everyHourSummaryService = Executors.newScheduledThreadPool(2);
        Date firstTime = TimeUtil.addSpecialCurTime(new Date(), Calendar.DATE, 1);
        m_everyHourSummaryService.scheduleAtFixedRate(new EveryHourTask(), 0, interval, TimeUnit.MINUTES);
            
        // 每日执行的任务
        interval = Integer.valueOf(Config.getValue("everydayTaskInterval"));
        m_saveDbTimer = new Timer();
        firstTime = GamePlayerUtil.getResetTime();
        firstTime = TimeUtil.addSpecialCurTime(firstTime, Calendar.DATE, 1);
        m_saveDbTimer.schedule(new EverydayTask(), interval * 60 * 60 * 1000, interval * 60 * 60 * 1000);
    }

    private static class PingCheckTask implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                long interval = (long) Integer.valueOf(Config.getValue("DBSaveInterval")) * 1000 * 60;
                long maxInterval = interval * 2;
                long now = TimeUtil.getSysCurTimeMillis();
                
                
                List<GamePlayer> list = WorldMgr.getAllPlayers();
                if (list != null)
                {
                    // 注意清理断开连接的客户端，以及 连接上长时间不发送登陆包的客户端。
                    for (GamePlayer player : list)
                    {
                    	if(player.getIsRobot())
                    		continue;

                    	
                    	if (player.getPingStart() + maxInterval < now)
	                    {
                    		player.disconnect();
	                    }
                    	
                    	
//                        if (player.getSession() != null
//                                && player.getPingStart() + maxInterval > now)
//                        {
//                        	player.getOut().SendPingTime();
//                        	                        
//                            if (AntiAddictionMgr.getIsASSon()
//                                    && player.getASSonSend())
//                            {
//                                if (player.getPlayerInfo().getAccountInfo().getIsActive() == 0
//                                        && TimeUtil.timeSpan(player.getPlayerInfo().getExtendedInfo().getAntiDate(),
//                                                             TimeUtil.getSysteCurTime(),
//                                                             "min") >= 30)
//                                    player.getOut().sendAASState(true);
//                                player.setASSonSend(false);
//                            }
//                        }
//                        else if (player.getPingStart() + interval < now)
//                        {
//                        	player.disconnect();   
//                        }
                    }
                }
            }
            catch (Exception e)
            {
                // log.error("PingCheck callback", e);
            }
        }
    }
    
    private static class SaveServiceTask implements Runnable
    {
    	@Override
        public void run()
        {
            try
            {
                long startTick = System.currentTimeMillis();

                @SuppressWarnings("unused")
                int saveCount = 0;
                int oldprio = Thread.currentThread().getPriority();
                Thread.currentThread().setPriority(1);

                // 保存奴隶关系
                ReferenceMgr.saveIntoDB();
                // 保存用户反馈
                FeedbackMgr.saveIntoDB();
                // 移除保存的可能用到的用户礼品信息
                UserGiftMgr.clearAlluserPlayerGift();
                
                //保存离线私信
                LetterMgr.saveToDB();
                
                //替换占位机器人头像
                RobRoomMgr.replaceRoomRobHead();
                
                // 保存人物
                List<GamePlayer> list = WorldMgr.getAllPlayers();
                for (GamePlayer p : list)
                {
                    p.saveIntoDatabase(true);
                    p.getPlayerAroudUser().setNearRefresh(true);//更新附近玩家列表
                    saveCount++;
                }

                Thread.currentThread().setPriority(oldprio);

                startTick = System.currentTimeMillis() - startTick;

                if (startTick > 2 * 60 * 1000)
                {
                    // log.warn(String.format("Saved all databases and %d players in %d ms",
                    // saveCount, startTick));
                }
            }
            catch (Exception e1)
            {
                // log.error("SaveServiceProc", e1);
            }
            finally
            {
                // GameEventMgr.Notify(GameServerEvent.WorldSave);
            }
        }
    }
    
    private static class UpdateRobotTask implements Runnable
    {
    	@Override
		public void run() {
			try
            {
                long startTick = System.currentTimeMillis();

                int saveCount = 0;
                int oldprio = Thread.currentThread().getPriority();
                Thread.currentThread().setPriority(1);

                // 入库机器人的胜负场次信息
                saveCount = RobotMgr.updateAllRobotPlayer();

                // 保存机器人聊天库信息
                RobotChatMgr.saveMessage();
                
                
                Thread.currentThread().setPriority(oldprio);

                long executeTime = System.currentTimeMillis() - startTick;

                log.debug(String.format("Save robot into db : %d players in %d ms", saveCount, executeTime));
            }
            catch (Exception e1)
            {
                log.error("UpdateRobotTask", e1);
            }
		}
    }
    
    private static class FiveMinSummaryTask implements Runnable
    {
    	@Override
		public void run() {
			try
            {
				int oldprio = Thread.currentThread().getPriority();
                Thread.currentThread().setPriority(1);
                
                //五分钟统计数据
				FiveMinSummaryInfo info = new FiveMinSummaryInfo();
				info.setCountDate(new Timestamp(System.currentTimeMillis()));
				info.setSubId(Integer.valueOf(Config.getValue("FiveMinSummarySubId")));
				info.setOnlineCount(WorldMgr.getWorldPlayerCount());
				info.setRegCount(DataStatisticsBussiness.getFiveMinRegisterCount());
				FiveMinSummaryBussiness.addFiveMinSummaryInfo(info);
				
				RobotChatMgr.sendWorld();
				
				Thread.currentThread().setPriority(oldprio);
            }
			catch (Exception e1)
            {
                log.error("FiveMinSummaryTask", e1);
            }
		}
    }
    
    private static class OneMinSummaryTask implements Runnable
    {
    	@Override
		public void run() {
			try
            {
				int oldprio = Thread.currentThread().getPriority();
                Thread.currentThread().setPriority(1);
                
              //重新计算今日财富的排名
//                UserTopMgr.reloadIncreasecoins();
				//一分钟检查防沉迷系统
				//AntiAddictionMgr.checkPlayeAntiAddiction();
				
                
//                UserPushMgr.reload();
//                System.out.println("=======OneMinSummaryTask=================================================" + System.currentTimeMillis() );
                HallMgr.checkHallStage();
                

				Thread.currentThread().setPriority(oldprio);
            }
			catch (Exception e1)
            {
                log.error("OneMinSummaryTask", e1);
            }
		}
    }
    
    /**
     * 每小时执行的任务
     * @author shanfeng.cao
     *
     */
    private static class EveryHourTask implements Runnable
    {
    	@Override
		public void run() {
			try
            {
				int oldprio = Thread.currentThread().getPriority();
                Thread.currentThread().setPriority(1);
                
//                UserTopMgr.reload();//重新统计玩家的排名（除了今日财富的）
                List<GamePlayer> list = WorldMgr.getAllPlayers();
                for (GamePlayer p : list)
                {
                	if(null != p.getPlayerAroudUser()) {
                        p.getPlayerAroudUser().setChartsRefresh(true);//更新附近玩家列表
                	}
                }
                
                UserTopMgr.reload();
                UserPushMgr.reload();
                KKMgr.addRobotInfo();
                
                
				Thread.currentThread().setPriority(oldprio);
            }
			catch (Exception e1)
            {
                log.error("EveryHourTask", e1);
            }
		}
    }
    
    /**
     * 每天执行的任务
     * @author shanfeng.cao
     *
     */
    private static class EverydayTask extends TimerTask
    {
    	@Override
    	public void run() {
			try
            {
                long startTick = System.currentTimeMillis();

                int oldprio = Thread.currentThread().getPriority();
                Thread.currentThread().setPriority(1);
                
                executeEverydayTask();//执行每日任务
                
                Thread.currentThread().setPriority(oldprio);

                long executeTime = System.currentTimeMillis() - startTick;

                log.debug(String.format("Execute everyday in %d ms", executeTime));
            }
            catch (Exception e)
            {
                log.error("EverydayTask", e);
            }
		}
    	
    	private boolean executeEverydayTask() {
    		try {
                
                //更新机器人聊天库信息
                RobotChatMgr.reload();

                UserPushMgr.reloadPushPlayer();
                
        		Calendar todayCal = Calendar.getInstance();
        		todayCal.set(Calendar.MINUTE, 0);
        		todayCal.set(Calendar.SECOND, 0);
        		todayCal.set(Calendar.MILLISECOND, 0);
                Timestamp countDate = new Timestamp(todayCal.getTimeInMillis());

                int subId = Integer.valueOf(Config.getValue("FiveMinSummarySubId"));
                
                //统计各类游戏的次数
                GameTypeCountInfo gameTypeCountInfo = new GameTypeCountInfo();
                gameTypeCountInfo.setCountDate(countDate);
                gameTypeCountInfo.setSubId(subId);
                int gameTypePlayerCount = GameMgr.getGameTypeCount(2);
                gameTypeCountInfo.setGameTypeTwoPlayer(gameTypePlayerCount);
                GameMgr.clearGameTypeCount(2);
                gameTypePlayerCount = GameMgr.getGameTypeCount(3);
                gameTypeCountInfo.setGameTypeThreePlayer(gameTypePlayerCount);
                GameMgr.clearGameTypeCount(3);
                gameTypePlayerCount = GameMgr.getGameTypeCount(4);
                gameTypeCountInfo.setGameTypeFourPlayer(gameTypePlayerCount);
                GameMgr.clearGameTypeCount(4);
                GameTypeCountBussiness.addGameTypeCountInfo(gameTypeCountInfo);
                
                //统计每日注册登录等等统计数据
                List<GamePlayer> players = WorldMgr.getAllPlayers();
                for(GamePlayer player : players) {
                	if(null != player.getDayActivity()) {//所有玩家保存统计数据
                		player.getDayActivity().updateOnQiut();
                	}
                }
				DayRecordInfo dayRecordInfo = new DayRecordInfo();
				dayRecordInfo.setCountDate(countDate);
				dayRecordInfo.setSubId(subId);
				int dayRegMemberCount = DataStatisticsBussiness.getRegMemberCount();
				dayRecordInfo.setRegMemberCount(dayRegMemberCount);
				int dayRegUserCount = DataStatisticsBussiness.getDayRegUserCount();
				dayRecordInfo.setRegUserCount(dayRegUserCount);
				int number = DataStatisticsBussiness.getDayLoginUserCount();
				dayRecordInfo.setLoginUserCount(number);
				number = DataStatisticsBussiness.getDayMaxOnlineCount();
				dayRecordInfo.setMaxOnlineCount(number);
				long perCapitaTime = DataStatisticsBussiness.getPerCapitaTime();
				dayRecordInfo.setPerCapitaTime(perCapitaTime);
				number = DataStatisticsBussiness.getUsageCounterTeaCount();
				dayRecordInfo.setUsageCounterTea(number);
				number = DataStatisticsBussiness.getUsageCounterHornCount();
				dayRecordInfo.setUsageCounterHorn(number);
				DataStatisticsBussiness.copyInsertETL();//保存每天玩家的活动数据
				UserDayActivityBussiness.clearUserDayActivity();//清除记录
				AllAroundPlayerInfoMgr.clearAllPlayerIncreaseCoins();//清除所有玩家今日在线属性
				DayRecordBussiness.addDayRecordInfo(dayRecordInfo);
                for(GamePlayer player : players) {//所有玩家重新统计统计数据
                	if(null != player.getDayActivity()) {
                		player.getDayActivity().updateOnLogin();
                	}
                }
                
				//统计玩家注册一周留存率统计数据
				DayRetainedRateInfo dayRetainedRateInfo = new DayRetainedRateInfo();
				dayRetainedRateInfo.setCountDate(countDate);
				dayRetainedRateInfo.setSubId(subId);
				dayRetainedRateInfo.setDayRegCount(dayRegUserCount + dayRegMemberCount);
				DayRetainedRateBussiness.addDayRetainedRateInfo(dayRetainedRateInfo);
				statisticsDayRetainedRate(subId, todayCal, 1);
				statisticsDayRetainedRate(subId, todayCal, 2);
				statisticsDayRetainedRate(subId, todayCal, 3);
				statisticsDayRetainedRate(subId, todayCal, 4);
				statisticsDayRetainedRate(subId, todayCal, 5);
				statisticsDayRetainedRate(subId, todayCal, 6);
				statisticsDayRetainedRate(subId, todayCal, 7);
				
				//移除离线超时的玩家
				AllAroundPlayerInfoMgr.clearOffLineOverTimePlayer();
    		} catch(Exception e) {
    			log.error("executeEverydayTask", e);
    		}
			return false;
    	}
    	
    	private void statisticsDayRetainedRate(int subId, Calendar cal, int countDay) {
    		cal.add(Calendar.DATE, - 1);//减号 间隔一天
            Timestamp retainedRateDay = new Timestamp(cal.getTimeInMillis());
			DayRetainedRateInfo dayRetainedRateInfo = 
					DayRetainedRateBussiness.getDayRetainedRate(retainedRateDay, subId);
			if(dayRetainedRateInfo != null) {
				int number = DataStatisticsBussiness.getRetainedRateDayLoginCount(countDay);
				dayRetainedRateInfo.setSomeDayLoginCount(countDay, number);
				DayRetainedRateBussiness.updateDayRetainedRateInfo(dayRetainedRateInfo);
			}
    	}
    }

    public static void stop()
    {
        m_saveDbService.shutdown();
        m_pingCheckService.shutdown();
        m_updateRobotService.shutdown();
        m_fiveMinSummaryService.shutdown();
        m_oneMinSummaryService.shutdown();
        m_saveDbTimer.cancel();
    }
}
