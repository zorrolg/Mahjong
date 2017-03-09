package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.citywar.util.HeadPicUtil;

/**
 * 用户所拥有的奴隶entity
 * @author Jacky.zheng
 *
 */
public class UserReferenceInfo extends DataObject implements Cloneable {
	
	private int id;
	
	private int masterUserId;
	
	private String masterUserName;
	
	private String masterPicPath;
	
	private int slavesUserId;
	
	private String slavesUserName;
	
	private String slavesPicPath;
	
	private int slavesMoney;
	
	private int slavesCoins;
	
	private String slavesSign;
	
	private String slavesPos;
	
	private int win;
	
	private int lose;
	
    private int drunkLevelLimit;
    
	private String city;
	
	private int level;
	
	private short sex;
	
	private int slavesGP;
	
	private int slavesCharmValve;
	
	private Timestamp slavesLastLoginDate;
	
	private Timestamp createTime;
	
	private Timestamp removeTime;
	
	private int incomeExp;
	
	private int incomeCoins;
	
	private int incomeCharmValve;
	
	private int takeUserId;
	
	private String takeUserName;
	
	private String takeUserPicPath;
	
	private int vipLevel;
	
	public UserReferenceInfo(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.masterUserId = rs.getInt("master_user_id");
		this.masterUserName = rs.getString("master_user_name");
		//从数据库中取出的相册集合字符串,通过逗号分割
        String picPathFromDB = rs.getString("master_user_picpath");
		this.masterPicPath = getUserPicPath(picPathFromDB);
		this.slavesUserId = rs.getInt("slaves_user_id");
		this.slavesUserName = rs.getString("slaves_user_name");
        picPathFromDB = rs.getString("slaves_picpath");
		this.slavesPicPath = getUserPicPath(picPathFromDB);
		this.slavesMoney = rs.getInt("slaves_money");
		this.slavesCoins = rs.getInt("slaves_coins");
		this.slavesSign = rs.getString("slaves_sign");
		this.slavesPos = rs.getString("slaves_pos");
		this.win = rs.getInt("win");
		this.lose = rs.getInt("lose");
//		this.drunkLevelLimit= rs.getInt("drunkLevelLimit");
		this.city = rs.getString("City");
		this.level = rs.getInt("Level");
		this.sex = rs.getByte("Sex");
		this.slavesGP = rs.getInt("GP");
		this.slavesCharmValve = rs.getInt("CharmValve");
		this.slavesLastLoginDate = rs.getTimestamp("LastLoginDate");
		this.createTime = rs.getTimestamp("create_time");
		this.removeTime = rs.getTimestamp("remove_time");
		this.incomeExp = rs.getInt("income_exp");
		this.incomeCoins = rs.getInt("income_coins");
		this.incomeCharmValve = rs.getInt("income_charmvalve");
		this.takeUserId = rs.getInt("take_user_id");
		this.takeUserName = rs.getString("take_user_name");
        picPathFromDB = rs.getString("take_user_picpath");
        this.vipLevel = rs.getInt("VipLevel");
        
		this.takeUserPicPath = getUserPicPath(picPathFromDB);
	}
	
	private String getUserPicPath(String picPathFromDB) {
        if (null != picPathFromDB)
        {
           String[] result = picPathFromDB.split(",");
           if (result.length >= 1) 
            {
               return result[0];
            } 
        }
        return "";
	}
	
//	public UserReferenceInfo(int id, int masterUserId, int slavesUserId,
//			String slavesUserName, String slavesPicPath, Timestamp createTime,
//			Timestamp removeTime, int incomeExp, int incomeCoins,
//			int takeUserId) {
//		super();
//		this.id = id;
//		this.masterUserId = masterUserId;
//		this.slavesUserId = slavesUserId;
//		this.slavesUserName = slavesUserName;
//		this.slavesPicPath = slavesPicPath;
//		this.createTime = createTime;
//		this.removeTime = removeTime;
//		this.incomeExp = incomeExp;
//		this.incomeCoins = incomeCoins;
//		this.takeUserId = takeUserId;
//	}

	public UserReferenceInfo() {
		this.id = 0; // 生成的未入库新对象的ID全部为0
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMasterUserId() {
		return masterUserId;
	}

	public void setMasterUserId(int masterUserId) {
		this.masterUserId = masterUserId;
	}

	public String getMasterUserName() {
		return masterUserName;
	}

	public void setMasterUserName(String masterUserName) {
		this.masterUserName = masterUserName;
	}

	public String getMasterPicPath() {
		if (masterUserId <= 0 || masterPicPath.isEmpty())
			return "";
		return HeadPicUtil.getRealPicPath(masterUserId, masterPicPath);
	}

	public void setMasterPicPath(String masterPicPath) {
		this.masterPicPath = masterPicPath;
	}

	public int getSlavesUserId() {
		return slavesUserId;
	}

	public void setSlavesUserId(int slavesUserId) {
		this.slavesUserId = slavesUserId;
	}

	public String getSlavesUserName() {
		return slavesUserName;
	}

	public void setSlavesUserName(String slavesUserName) {
		this.slavesUserName = slavesUserName;
	}

	public String getSlavesPicPath() {
		if (slavesUserId <= 0 || slavesPicPath.isEmpty())
			return "";
		return HeadPicUtil.getRealPicPath(slavesUserId, slavesPicPath);
	}

	public void setSlavesPicPath(String slavesPicPath) {
		this.slavesPicPath = slavesPicPath;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getRemoveTime() {
		return removeTime;
	}

	public void setRemoveTime(Timestamp removeTime) {
		this.removeTime = removeTime;
	}

	public int getIncomeExp() {
		return incomeExp;
	}

	public void setIncomeExp(int incomeExp) {
		this.incomeExp = incomeExp;
	}

	public int getIncomeCoins() {
		return incomeCoins;
	}

	public void setIncomeCoins(int incomeCoins) {
		this.incomeCoins = incomeCoins;
	}

	public int getIncomeCharmValve() {
		return incomeCharmValve;
	}

	public void setIncomeCharmValve(int incomeCharmValve) {
		this.incomeCharmValve = incomeCharmValve;
	}
	
	public int getTakeUserId() {
		return takeUserId;
	}

	public void setTakeUserId(int takeUserId) {
		this.takeUserId = takeUserId;
	}

	public int getSlavesMoney() {
		return slavesMoney;
	}

	public void setSlavesMoney(int slavesMoney) {
		this.slavesMoney = slavesMoney;
	}

	public String getSlavesSign() {
		return slavesSign;
	}

	public void setSlavesSign(String slavesSign) {
		this.slavesSign = slavesSign;
	}

	public String getSlavesPos() {
		return slavesPos;
	}

	public void setSlavesPos(String slavesPos) {
		this.slavesPos = slavesPos;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}
	
    public int getDrunkLevelLimit()
    {
        return drunkLevelLimit;
    }

    public void setDrunkLevelLimit(int drunkLevelLimit)
    {
        this.drunkLevelLimit = drunkLevelLimit;
    }

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public short getSex() {
		return sex;
	}

	public void setSex(short sex) {
		this.sex = sex;
	}
	
	public int getSlavesGP() {
		return slavesGP;
	}

	public void setSlavesGP(int slavesGP) {
		this.slavesGP = slavesGP;
	}
	
	public int getSlavesCharmValve() {
		return slavesCharmValve;
	}

	public void setSlavesCharmValve(int slavesCharmValve) {
		this.slavesCharmValve = slavesCharmValve;
	}

	public Timestamp getSlavesLastLoginDate() {
		return slavesLastLoginDate;
	}

	public void setSlavesLastLoginDate(Timestamp slavesLastLoginDate) {
		this.slavesLastLoginDate = slavesLastLoginDate;
	}
	
	public String getTakeUserName() {
		return takeUserName;
	}

	public void setTakeUserName(String takeUserName) {
		this.takeUserName = takeUserName;
	}

	public String getTakeUserPicPath() {
		if (takeUserId <= 0 || takeUserPicPath.isEmpty())
			return "";
		return HeadPicUtil.getRealPicPath(takeUserId, takeUserPicPath);
	}

	public void setTakeUserPicPath(String takeUserPicPath) {
		this.takeUserPicPath = takeUserPicPath;
	}

	public int getSlavesCoins() {
		return slavesCoins;
	}

	public void setSlavesCoins(int slavesCoins) {
		this.slavesCoins = slavesCoins;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	
	@Override
	public UserReferenceInfo clone() throws CloneNotSupportedException {
		return (UserReferenceInfo)super.clone();
	}
}
