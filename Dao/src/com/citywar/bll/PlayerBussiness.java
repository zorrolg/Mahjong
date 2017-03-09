/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.bll;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.citywar.dice.dao.manager.DaoManager;
import com.citywar.dice.db.DBParamWrapper;
import com.citywar.dice.entity.LevelInfo;
import com.citywar.dice.entity.PlayerInfo;

/**
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public class PlayerBussiness
{
    /**
     * 添加一个玩家信息入库
     * 
     * @param playerInfo
     * @return
     */
    public static int addPlayerInfo(PlayerInfo playerInfo)
    {
        return DaoManager.getPlayerInfoDao().insert(playerInfo);
    }

    /**
     * 根据 Id 查询玩家信息
     * 
     * @param userId
     * @return
     */
    public static PlayerInfo getPlayerInfoById(int userId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        return DaoManager.getPlayerInfoDao().query("t_u_player", "UserId=?",
                                                   params);
    }
    
    /**
     * 得到设备ID得到最后登录的用户信息
     * 
     * @return null表示 该设备没有用户记录
     */
    public static PlayerInfo getLastLoginAcount(String UDID)
    {
    	DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, UDID);
        List<PlayerInfo> list = DaoManager.getPlayerInfoDao().queryList("t_u_player",
                 "machinery_id  = ?",
                 params, 
                 "LastQiutDate desc",
                 null);
        if(list != null && list.size() >0 ){
        	return list.get(0);
        }
        return null;
    }
    
    /**
     * 得到设备ID得到临时用户信息
     * 
     * @return null表示 该设备没有用户记录
     */
	public static PlayerInfo getLastLoginTempAcount(String UDID) {
		DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, UDID);
        List<PlayerInfo> list = DaoManager.getPlayerInfoDao().queryList("t_u_player",
                 "machinery_id  = ? AND t_u_player.UDID is not null ",
                 params);
        if(list != null && list.size() >0 ){
        	return list.get(0);
        }
        return null;
	}

    /**
     * 根据 昵称 查询玩家信息
     * 
     * @param userName
     * @return
     */
    public static PlayerInfo getPlayerInfoByName(String userName)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, userName);
        return DaoManager.getPlayerInfoDao().query("t_u_player", "UserName=?",
                                                   params);
    }

    /**
     * 检查是否可登陆
     * 
     * @param account
     * @param userPwd
     * @return
     */
    public static PlayerInfo checkAccoutAndPwd(String account, String userPwd)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, account);
        params.put(Types.VARCHAR, userPwd);
        return DaoManager.getPlayerInfoDao().query("t_u_player",
                                                   "IsEnable = 1 and Account=? and UserPwd=?",
                                                   params);
    }

    /**
     * 根据邮箱返回用户信息
     * 
     * @param account
     * @return
     */
    public static PlayerInfo getPlayerInfoByAccount(String account)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, account);
        return DaoManager.getPlayerInfoDao().query("t_u_player", "Account=?",
                                                   params);
    }

    /**
     * 取得相册图片路径
     * 
     * @param userId
     * @return
     */
    public static String getImagePath(int userId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        return DaoManager.getPlayerInfoDao().getOneValue("t_u_player",
                                                         "PicPath", "UserId=?",
                                                         params);
    }

    /**
     * 更新相册图片路径地址
     * 
     * @param newPath
     * @param userId
     * @return
     */
    public static boolean updateImagePath(String newPath, int userId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, newPath);
        params.put(Types.INTEGER, userId);
        return DaoManager.getPlayerInfoDao().update("t_u_player", "PicPath=?",
                                                    "userId=?", params) != -1;
    }

    /**
     * 更新相册图片路径地址
     * 
     * @param newPath
     * @param userId
     * @return
     */
    public static boolean updateCardCount(int cardCount, int userId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, cardCount);
        params.put(Types.INTEGER, userId);
        return DaoManager.getPlayerInfoDao().update("t_u_player", "cardCount=cardCount+?",
                                                    "userId=?", params) != -1;
    }
    
    /**
     * 更新最后一次登陆时间
     * 
     * @param userId
     * @param date
     * @return
     */
    public static boolean updateLastLoginDate(int userId, Date date)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.TIMESTAMP, date);
        params.put(Types.INTEGER, userId);
        return DaoManager.getPlayerInfoDao().update("t_u_player",
                                                    "LastLoginDate=?",
                                                    "userId=?", params) != 0;
    }

    /**
     * 更新游戏币的数量
     * 
     * @param userId
     * @param coins
     * @return
     */
    public static boolean updateCoins(int userId, int coins)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, coins);
        params.put(Types.INTEGER, userId);
        return DaoManager.getPlayerInfoDao().update("t_u_player", "Coins=?",
                                                    "userId=?", params) != 0;
    }

    /**
     * 更新玩家RMB数量
     * 
     * @param userId
     * @param money
     * @return
     */
//    public static boolean updateMoney(int userId, int money)
//    {
//        DBParamWrapper params = new DBParamWrapper();
//        params.put(Types.INTEGER, money);
//        params.put(Types.INTEGER, userId);
//        return DaoManager.getPlayerInfoDao().update("t_u_player", "Money= Money+?",
//                                                    "userId=?", params) != 0;
//    }
    
    
//  update db_dice.t_u_player set money = money + 300 ,VipDate = if(VipLevel > 0, 
//	DATE_ADD(now(),INTERVAL 31 DAY), DATE_ADD(VipDate,INTERVAL 31 DAY)) ,VipLevel = 1 where userid = 258001;

	public static boolean updateMoney(int userId, int money, int templateId, int validDay)
	{
		int vipLevel = (templateId == -2 ? 1 : 0);
		DBParamWrapper params = new DBParamWrapper();
		params.put(Types.INTEGER, money);
		
		String update = "Money= Money+?";
		if(vipLevel > 0)
		{
			update = "Money= Money+?, VipDate = if(VipLevel > 0, DATE_ADD(VipDate,INTERVAL ? DAY), DATE_ADD(now(),INTERVAL ? DAY)) ,VipLevel = 1";
			params.put(Types.INTEGER, validDay);
			params.put(Types.INTEGER, validDay);
		}
		
		params.put(Types.INTEGER, userId);
		return DaoManager.getPlayerInfoDao().update("t_u_player", update,
		                                            "userId=?", params) != 0;
	}



    /**
     * 更新玩家魅力值
     * 
     * @param userId
     * @param charmValve
     * @return
     */
    public static boolean updateCharmValve(int userId, int charmValve)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, charmValve);
        params.put(Types.INTEGER, userId);
        return DaoManager.getPlayerInfoDao().update("t_u_player", "CharmValve=?",
                                                    "userId=?", params) != 0;
    }

    /**
     * 查询等级信息列表
     * 
     * @return
     */
    public static List<LevelInfo> getAllLevelInfos()
    {
        return DaoManager.getLevelDao().queryList("t_s_level", null, null);
    }

    /**
     * 更新玩家经验值和等级信息
     * 
     * @param userId
     * @param gp
     * @param level
     * @return
     */
    public static boolean updatePlayerGpLevel(int userId, int gp, int level)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, gp);
        params.put(Types.INTEGER, level);
        params.put(Types.INTEGER, userId);
        return DaoManager.getPlayerInfoDao().update("t_u_player",
                                                    "GP=?,Level=?", "userId=?",
                                                    params) != 0;
    }
    
    /**
     * 查出离线没超时的玩家
     * @return
     */
	public static List<PlayerInfo> slecetOffLineOverTimePlayer() {
		DBParamWrapper params = new DBParamWrapper();
        return DaoManager.getPlayerInfoDao().queryList("t_u_player",
                                                       " DATE_SUB(NOW(),INTERVAL 7 DAY) < LastQiutDate AND isRobot=0", params);
	}

    public static boolean updateAll(int userId, PlayerInfo info)
    {
        return DaoManager.getPlayerInfoDao().update(info) > 0;
    }

    public static int getNewUserId()
    {
        return DaoManager.getCodeDao().getUserId();
    }
    
    public static List<PlayerInfo> getCharts(String strWhere, String orderField,int topCount)
    {
    	 DBParamWrapper params = new DBParamWrapper();
         params.put(Types.VARCHAR, orderField);
         return DaoManager.getPlayerInfoDao().queryList("t_u_player",strWhere, null, orderField, String.valueOf(topCount));//isRobot=0
    }
    
    public static List<PlayerInfo> getCharmValvesTopByStage()
    {
    	return DaoManager.getPlayerInfoDao().getTopStage();
//         return DaoManager.getPlayerInfoDao().queryList("v_stages","isRobot < 2",null);//isRobot=0
    }

    public static List<PlayerInfo> getCharmValvesTopByLevel()
    {
    	return DaoManager.getPlayerInfoDao().getTopLevel();
//         return DaoManager.getPlayerInfoDao().queryList("v_user_charm_top","isRobot < 2",null);//isRobot=0
    }
    
    // 测试用
    public static List<PlayerInfo> getPlayerInfosByName(String userName)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.VARCHAR, userName);
        return DaoManager.getPlayerInfoDao().queryList("t_u_player",
                                                       "UserName=?", params);
    }

    // 测试用
    public static List<String> getUserNamesById(int[] ids)
    {
        StringBuilder sb = new StringBuilder(100);
        for (int id : ids)
        {
            sb.append(",");
            sb.append(id);
        }
        String strWhere = String.format("userid in (%s)",
                                        sb.replace(0, 1, "").toString());
        return DaoManager.getPlayerInfoDao().getOneValueList("t_u_player",
                                                             "UserName",
                                                             strWhere, null,
                                                             null, null);
    }

    // 测试用
    public static String getUserNameById(int userId)
    {
        DBParamWrapper params = new DBParamWrapper();
        params.put(Types.INTEGER, userId);
        return DaoManager.getPlayerInfoDao().getOneValue("t_u_player",
                                                         "UserName",
                                                         "userid=?", params);
    }

    // 同步用
    public static List<PlayerInfo> getAllPlayer(String table)
    {
        DBParamWrapper params = new DBParamWrapper();
        return DaoManager.getPlayerInfoDao().queryList(table,
                                                         "", params);
    }
}
