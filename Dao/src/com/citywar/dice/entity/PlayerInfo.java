/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;

import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.citywar.util.HeadPicUtil;

/**
 * 玩家信息实体
 * 
 * @author tracy
 * @date 2011-12-15
 * @version
 * 
 */
public class PlayerInfo extends DataObject implements Cloneable 
{
    /**
     * 用户 id
     */
    private int userId;

    /**
     * 用户登陆名
     */
    private String userName;

    /**
     * 性别
     */
    private byte sex;
    
    /**
     * 是否为机器人
     */
    private byte isRobot;


	/**
     * 用户密码
     */
    private String userPwd;

    /**
     * 银子
     */
    private int money;
    
    /**
     * 银子
     */
    private int fortune;

    /**
     * 个性签名
     */
    private String sign;

    /**
     * 地理位置
     */
    private String pos;

    /**
     * 头像地址
     */
    private String picPath;
    /**
     * 相册集合路径
     */
    private List<String> picPathList;

    /**
     * win次数
     */
    private int win;

    /**
     * lose次数
     */
    private int lose;

    /**
     * 城市信息
     */
    private String city;

    /**
     * 用户等级
     */
    private int level;

    /**
     * 经验
     */
    private int gp;

    /**
     * 游戏币(筹码)
     */
    private int coins;
    
    private int coins_temp;
    
    private int coins_pengyou;
    /**
     * 注册时间
     */
    private Timestamp registerDate;
    /**
     * 上次登陆时间(登陆的时候保存一次, 下线的时候再保存一次)
     */
    private Timestamp LastLoginDate;
    /**
     * 上次下线时间(下线的时候再保存一次)
     */
    private Timestamp LastQiutDate;

    /**
     * 在线状态
     */
    private boolean isOnline;

    /**
     * 当前醉酒程度
     */
    private int drunkLevelContest;
    /**
     * 当前醉酒程度
     */
    private int drunkLevelSocial;
    

    /**
     * 最近的一次 恢复一点醉酒度 的时间
     */
    private Date LastWakeTime;

    /**
     * 最近的一次完全醉酒的时间
     */
    private Date DeepDrunkTime;

    /**
     * 玩家等级对应的醉酒度（逻辑字段，从等级模板表上取得）
     */
    private int DrunkLevelLimit;

    /**
     * 总的游戏场次, 胜+负+平
     */
    private int total;

    /**
     * 玩家账号, 如果为临时性, 用 userId, 注册后为邮箱账号
     */
    private String account;
    /**
     * 玩家设备的唯一标识,当玩家为临时账号的时候需要用此来标识
     */
    private String UDID;
    /**
     * 玩家是否为注册用户的标识0-临时用户,1-注册用户
     */
    private int UserType;
    /**
     * 玩家魅力值
     */
    private int charmValve;
    /**
     * 玩家的奴隶数
     */
    private int slaveCount;
    /**
     * 昨天玩家的金币数
     */
    private int yesterdayCoins;
    /**
     * 今天玩家用于购买道具的金币数
     */
    private int buyItemCoins;
    
    /**
     * 设备的唯一标示(目前是网卡ID)
     */
    private String machineryId;
    
    /**
     * 该账号进入的最大的大厅ID
     */
    private int maxHallId;
    
    /**
     * 渠道名称
     */
    private String machineType;
    
    /**
     * 渠道名称
     */
    private String sdkType;
    
    
    /**
     * 渠道玩家账号
     */
    private String sdkUserName;
    
    
    /**
     * 该账号进入的最大的大厅ID
     */
    private int stageId;
    
    /**
     * 该账号进入的最大的大厅ID
     */
    private int vipLevel;
    
    /**
     * 该账号进入的最大的大厅ID
     */
    private Timestamp vipDate;
    
    /**
     * 该账号进入的最大的大厅ID
     */
    private int buyFlag;
    
    /**
     * 该账号进入的最大的大厅ID
     */
    private int cardCount;
    
    /**
     * 该账号进入的最大的大厅ID
     */
    private int useCardCount;
    
    
	@Override
	public String toString() {
		String userStr = String
				.format("userId: %d,userName: %s,picPath: %s,coins: %d",
						getUserId(), userName, picPath,
						coins);
		return userStr + "LastQiutDate" + LastQiutDate;
	}
	
    public int getDrunkLevelLimit()
    {
        return DrunkLevelLimit;
    }

    public void setDrunkLevelLimit(int drunkLevelLimit)
    {
        DrunkLevelLimit = drunkLevelLimit;
    }

    /**
     * 
     */
    public PlayerInfo()
    {
        super();
        this.sign = "";
        this.pos = "0,0";
        this.picPath = "";
        this.city = "";
    }
    
    public PlayerInfo(ResultSet rs) throws SQLException
    {
    	super();
        this.sign = "";
        this.pos = "0,0";
        this.picPath = "";
        this.city = "";
        this.setUserId(rs.getInt("UserId"));
        this.setUserName(rs.getString("UserName"));
        this.setUserPwd(rs.getString("UserPwd"));
        this.setMoney(rs.getInt("Money"));
        this.setSign(rs.getString("Sign"));
        this.setPos(rs.getString("Pos"));
        //从数据库中取出的相册集合字符串,通过逗号分割
        String picPathFromDB = rs.getString("PicPath");
        //有多张图片时,默认第一张为头像地址,后面分别是相册图片
        List<String> picPathList = new ArrayList<String>();
        if (null != picPathFromDB)
        {
           String[] result = picPathFromDB.split(",");
           if (result.length >= 1) 
            {
            	//如果长度大于1,有多长图片
            	for (int i = 0; i < result.length; i++)
            	{
            		result[i] = result[i].replaceAll("_180x180", "");
            		if (i == 0)
            		{
            			this.setPicPath(result[i]);
            		}
            		else
            		{
            			picPathList.add(result[i]);
            		}
            	}
            } 
            else
            {
            	//其他情况说明有异常
            	this.setPicPath("");
            }
        }
        else 
        {
        	//从数据库中取出的头像地址为空
        	this.setPicPath("");
        }
        this.setPicPathList(picPathList);
        this.setWin(rs.getInt("Win"));
        this.setLose(rs.getInt("Lose"));
        this.setCity(rs.getString("City"));
        this.setLevel(rs.getInt("Level"));
        this.setGp(rs.getInt("GP"));
        this.setCoins(rs.getInt("Coins"));        
        this.setLastLoginDate(rs.getTimestamp("LastLoginDate"));
        this.setLastQiutDate(rs.getTimestamp("LastQiutDate"));
        this.setOnline(true);
        
        
        this.setDrunkLevelContest(rs.getInt("DrunkLevel")%10000);
        this.setDrunkLevelSocial(rs.getInt("DrunkLevel")/10000);
        
        this.setLastWakeTime(rs.getTimestamp("LastWakeTime"));
        this.setDeepDrunkTime(rs.getTimestamp("DeepDrunkTime"));
        this.setSex(rs.getByte("Sex"));
        this.setIsRobot(rs.getByte("IsRobot"));
        this.setTotal(rs.getInt("Total"));
        this.setAccount(rs.getString("Account"));
        this.setUDID(rs.getString("UDID"));
        this.setUserType(rs.getInt("UserType"));
        this.setRegisterDate(rs.getTimestamp("RegisterDate"));
        this.setCharmValve(rs.getInt("CharmValve"));
        this.setMachineryId(rs.getString("machinery_id"));
        this.setMaxHallId(rs.getInt("maxHallId"));
        
        this.setMachineType(rs.getString("MachineType"));
        this.setSdkType(rs.getString("SdkType"));
        this.setSdkUserName(rs.getString("SdkUserName"));
        this.setStageId(rs.getInt("stage"));
        
        this.setVipLevel(rs.getInt("VipLevel"));
        this.setVipDate(rs.getTimestamp("VipDate"));        
        
        
        
        if(rs.getMetaData().getColumnCount() > 35)
        	this.setTempCoins(rs.getInt("Coins_Temp"));
        
        if(rs.getMetaData().getColumnCount() > 36)
        	this.setFortune(rs.getInt("Fortune"));
        
        if(rs.getMetaData().getColumnCount() > 37)
        	this.setBuyFlag(rs.getInt("buyflag"));
        
        if(rs.getMetaData().getColumnCount() > 38)
        	this.setCardCount(rs.getInt("cardCount"));
        
        if(rs.getMetaData().getColumnCount() > 39)
        	this.setUseCardCount(rs.getInt("useCardCount"));
        
    }

    /**
     * @return the userId
     */
    public int getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName)
    {    	
    	try{
    		userName = URLDecoder.decode(userName, "UTF-8");
    	}
    	catch(Exception ex) {
        }
    	
        this.userName = userName;
    }

    /**
     * @return the userPwd
     */
    public String getUserPwd()
    {
        return userPwd;
    }

    /**
     * @param userPwd
     *            the userPwd to set
     */
    public void setUserPwd(String userPwd)
    {
        this.userPwd = userPwd;
    }

    /**
     * @return the money
     */
    public int getMoney()
    {
        return money;
    }

    /**
     * @param money
     *            the money to set
     */
    public void setMoney(int money)
    {
        this.money = money;
    }

    public int getFortune()
    {
        return fortune;
    }

    /**
     * @param money
     *            the money to set
     */
    public void setFortune(int fortune)
    {
        this.fortune = fortune;
    }
    
    
    /**
     * @return the sign
     */
    public String getSign()
    {
        return sign;
    }

    /**
     * @param sign
     *            the sign to set
     */
    public void setSign(String sign)
    {
        this.sign = sign;
    }

    /**
     * @return the pos
     */
    public String getPos()
    {
        return pos;
    }

    /**
     * @param pos
     *            the pos to set
     */
    public void setPos(String pos)
    {
        this.pos = pos;
    }

    /**
     * @return the picPath
     */
    public String getPicPath()
    {
    	return picPath;
    }

    /**
     * @param picPath
     *            the picPath to set
     */
    public void setPicPath(String picPath)
    {
        this.picPath = picPath;
        
//        System.out.println(picPath);
        if(picPath.length() > 70)
        	System.out.println("zzzz");
    }

    /**
     * @return the win
     */
    public int getWin()
    {
        return win;
    }

    /**
     * @param win
     *            the win to set
     */
    public void setWin(int win)
    {
        this.win = win;
    }

    /**
     * @return the lose
     */
    public int getLose()
    {
        return lose;
    }

    /**
     * @param lose
     *            the lose to set
     */
    public void setLose(int lose)
    {
        this.lose = lose;
    }

    /**
     * @return the city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * @return the level
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * @param level
     *            the level to set
     */
    public void setLevel(int level)
    {
        this.level = level;
    }

    /**
     * @return the gp
     */
    public int getGp()
    {
        return gp;
    }

    /**
     * @param gp
     *            the gp to set
     */
    public void setGp(int gp)
    {
        this.gp = gp;
    }

    /**
     * @return the coins
     */
    public int getCoins()
    {
        return coins;
    }

    /**
     * @param coins
     *            the coins to set
     */
    public void setCoins(int coins)
    {
        this.coins = coins;
    }
    
    
    public int getTempCoins()
    {
        return coins_temp;
    }

    /**
     * @param coins
     *            the coins to set
     */
    public void setTempCoins(int coins_temp)
    {
        this.coins_temp = coins_temp;
    }
    
    public int getPengYouCoins()
    {
        return coins_pengyou;
    }

    /**
     * @param coins
     *            the coins to set
     */
    public void setPengYouCoins(int coins_pengyou)
    {
        this.coins_pengyou = coins_pengyou;
    }
    
    
    public int getRoomCoins()
    {
    	if(this.coins_temp > 0)
    		return this.coins_temp;
    	else
    		return this.coins;
    }

    public Timestamp getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Timestamp registerDate) {
		this.registerDate = registerDate;
	}
	/**
     * @return the lastLoginDate
     */
    public Timestamp getLastLoginDate()
    {
        return LastLoginDate;
    }
    /**
     * @param lastLoginDate
     *            the lastLoginDate to set
     */
    public void setLastLoginDate(Timestamp lastLoginDate)
    {
        LastLoginDate = lastLoginDate;
    }
    /**
     * @return LastQiutDate
     */
    public Timestamp getLastQiutDate() {
		return LastQiutDate;
	}
    /**
     * @param lastQiutDate
     */
	public void setLastQiutDate(Timestamp lastQiutDate) {
		LastQiutDate = lastQiutDate;
	}

	/**
     * @return the isOnline
     */
    public boolean isOnline()
    {
        return isOnline;
    }

    /**
     * @param isOnline
     *            the isOnline to set
     */
    public void setOnline(boolean isOnline)
    {
        this.isOnline = isOnline;
    }

    /**
     * @return the lastWakeTime
     */
    public Date getLastWakeTime()
    {
        return LastWakeTime;
    }

    /**
     * @param lastWakeTime
     *            the lastWakeTime to set
     */
    public void setLastWakeTime(Date lastWakeTime)
    {
        LastWakeTime = lastWakeTime;
    }

    /**
     * @return the deepDrunkTime
     */
    public Date getDeepDrunkTime()
    {
        return DeepDrunkTime;
    }

    /**
     * @param deepDrunkTime
     *            the deepDrunkTime to set
     */
    public void setDeepDrunkTime(Date deepDrunkTime)
    {
        DeepDrunkTime = deepDrunkTime;
    }

    /**
     * @return the drunkLevel
     */
    public int getDrunkLevelContest()
    {
        return drunkLevelContest;
    }

    /**
     * @param drunkLevel
     *            the drunkLevel to set
     */
    public void setDrunkLevelContest(int drunkLevel)
    {
        this.drunkLevelContest = drunkLevel;
    }
    
    /**
     * @return the drunkLevel
     */
    public int getDrunkLevelSocial()
    {
        return drunkLevelSocial;
    }

    /**
     * @param drunkLevel
     *            the drunkLevel to set
     */
    public void setDrunkLevelSocial(int drunkLevel)
    {
        this.drunkLevelSocial = drunkLevel;
    }
    

    public byte getSex()
    {
        return sex;
    }

    public void setSex(byte sex)
    {
        this.sex = sex;
    }

    public byte getIsRobot() {
		return isRobot;
	}

	public void setIsRobot(byte isRobot) {
		this.isRobot = isRobot;
	}
	
    /**
     * @return the total
     */
    public int getTotal()
    {
        return total;
    }

    /**
     * @param total
     *            the total to set
     */
    public void setTotal(int total)
    {
        this.total = total;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

	public String getUDID() 
	{
		return UDID;
	}

	public void setUDID(String udid) 
	{
		UDID = udid;
	}

	public int getUserType() 
	{
		return UserType;
	}

	public void setUserType(int userType) 
	{
		UserType = userType;
	}
	
	public List<String> getPicPathList() 
	{
		return picPathList;
	}

	public void setPicPathList(List<String> picPathList) 
	{
		this.picPathList = picPathList;
	}
	
	public String getRealPicPath()
	{
		if (userId <= 0 || picPath.isEmpty())
			return "";
		return HeadPicUtil.getRealPicPath(userId, picPath);
	}
	

	public String getRealPicPath(String picpath)
	{
		if (userId <= 0 || picpath.isEmpty())
			return "";
		return HeadPicUtil.getRealPicPath(userId, picpath);
	}
	
	public List<String> getRealPicPathList() 
	{
		List<String> tempList = new ArrayList<String>();
		if (null != picPathList && !picPathList.isEmpty())
		{
			for (String s : picPathList)
			{
				tempList.add(getRealPicPath(s));
			}
		}
		return tempList;
	}

	public int getCharmValve() {
		return charmValve;
	}

	public void setCharmValve(int charmValve) {
		this.charmValve = charmValve;
	}

	public int getSlaveCount() {
		return slaveCount;
	}

	public void setSlaveCount(int slaveCount) {
		if(slaveCount < 0) {
			this.slaveCount = 0;
		} else {
			this.slaveCount = slaveCount;
		}
	}

	public int getYesterdayCoins() {
		return yesterdayCoins;
	}

	public void setYesterdayCoins(int yesterdayCoins) {
		this.yesterdayCoins = yesterdayCoins;
	}

	/**
	 * 获得玩家今日赚得的金币数
	 * 现在金币数 - 昨日金币数 + 购买道具的金币数
	 * @return
	 */
	public int getDayIncreaseCoins() {
		return coins - yesterdayCoins + buyItemCoins;
	}

	/** 是否是临时账号 */
	public boolean isTempAccount()
	{
		return UDID!=null && UDID.length()>0;
	}

	public String getMachineryId() {
		return machineryId;
	}

	public void setMachineryId(String machineryId) {
		this.machineryId = machineryId;
	}

	public int getBuyItemCoins() {
		return buyItemCoins;
	}

	public void setBuyItemCoins(int buyItemCoins) {
		this.buyItemCoins = buyItemCoins;
	}

	@Override
	public int hashCode() {
		return userId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(null != obj && obj instanceof PlayerInfo) {
			PlayerInfo info = (PlayerInfo)obj;
			return userId == info.getUserId();
		}
		return super.equals(obj);
	}

	public static void main(String[] args) {
		
		List<PlayerInfo> infos = new ArrayList<PlayerInfo>();
		for (int i = 0; i < 10; i++) {
			PlayerInfo info = new PlayerInfo();
			info.setCoins(new Random().nextInt(100)*10);
			info.setYesterdayCoins(new Random().nextInt(100)*10);
			infos.add(info);
		}
		Collections.sort(infos,new Comparator<PlayerInfo>() {

			@Override
			public int compare(PlayerInfo o1, PlayerInfo o2) {
				
				 int charm1 = ((PlayerInfo)o1).getDayIncreaseCoins();
		    	 int charm2 = ((PlayerInfo)o2).getDayIncreaseCoins();
		         return charm1 - charm2;
			}
		});
		for (int i = 0; i < infos.size(); i++) {
			//System.out.println(infos.get(i).getDayIncreaseCoins());
		}
		
	}

	public int getMaxHallId() {
		return maxHallId;
	}

	public void setMaxHallId(int maxHallId) {
		this.maxHallId = maxHallId;
	}
	
	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}
	
	public String getSdkType() {
		return sdkType;
	}

	public void setSdkType(String sdkType) {
		this.sdkType = sdkType;
	}
	
	public String getSdkUserName() {
		return sdkUserName;
	}

	public void setSdkUserName(String sdkUserName) {
		this.sdkUserName = sdkUserName;
	}
	
	
	public int getStageId() {
		return stageId;
	}

	public void setStageId(int stageId) {
		this.stageId = stageId;
	}
	
	
	
	public int getVipLevel() {
		return this.vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	
	
	/**
     * @return LastQiutDate
     */
    public Timestamp getVipDate() {
		return this.vipDate;
	}
    /**
     * @param lastQiutDate
     */
	public void setVipDate(Timestamp vipDate) {
		this.vipDate = vipDate;
	}

	public int getBuyFlag() {
		return buyFlag;
	}

	public void setBuyFlag(int buyFlag) {
		this.buyFlag = buyFlag;
	}
    
	
	
	public int getCardCount() {
		return cardCount;
	}

	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
	}
	
	
	public int getUseCardCount() {
		return useCardCount;
	}

	public void setUseCardCount(int useCardCount) {
		this.useCardCount = useCardCount;
	}

    
    
	@Override
	public PlayerInfo clone() throws CloneNotSupportedException {
		return (PlayerInfo)super.clone();
	}
	
}
