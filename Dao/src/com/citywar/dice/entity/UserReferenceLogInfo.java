package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserReferenceLogInfo extends DataObject {
	
	/**
	 * 日志ID
	 */
	private int logId;
	/**
	 * 日志属于的用户ID
	 */
	private int ownerUserId;
	/**
	 * 日志被动者的（另外一个）用户ID
	 */
	private int passivesUserId;
	/**
	 * 日志对应关系的ID
	 */
	private int refId;
	/**
	 * 日志类型
	 * 0：表示谁成为您的奴隶
	 * 1：表示谁抢走了您的奴隶某某
	 * 2：某某奴隶为您带来了多少金币
	 * 3：某某奴隶被您抽打一遍，带来了多少经验
	 * 4：主人奖励你，并为主人带来了多少金币
	 * 5：主人折磨了你，并为主人带来了多少经验
	 */
	private int logType;
	
	private int incomeExp;
	
	private int incomeCoins;
	/**
	 * 日志创建时间
	 */
	private Timestamp logCreateTime;
	/**
	 * 日志的主动者名字
	 */
	private String initiativeUserName;
	/**
	 * 日志的被动者名字
	 */
	private String passivesUserName;
	/**
	 * 日志的内容
	 */
	private String logContent;


	public UserReferenceLogInfo(){
		this.logId = 0;
	}

	public UserReferenceLogInfo(ResultSet rs) throws SQLException {
		this.logId = rs.getInt("log_id");
		this.ownerUserId = rs.getInt("owner_user_id");
		this.passivesUserId = rs.getInt("passives_user_id");
		this.refId = rs.getInt("ref_id");
		this.logType = rs.getInt("log_type");
		this.incomeExp = rs.getInt("income_exp");
		this.incomeCoins = rs.getInt("income_coins");
		this.logCreateTime = rs.getTimestamp("log_create_time");
		this.initiativeUserName = rs.getString("initiative_user_name");
		this.passivesUserName = rs.getString("passives_user_name");
		this.logContent = rs.getString("log_content");
	}
	
	public UserReferenceLogInfo(int logId, int ownerUserId, int passivesUserId,
			int refId, int logType, int incomeExp, int incomeCoins,
			Timestamp logCreateTime, String initiativeUserName,
			String passivesUserName, String logContent) {
		super();
		this.logId = logId;
		this.ownerUserId = ownerUserId;
		this.passivesUserId = passivesUserId;
		this.refId = refId;
		this.logType = logType;
		this.incomeExp = incomeExp;
		this.incomeCoins = incomeCoins;
		this.logCreateTime = logCreateTime;
		this.initiativeUserName = initiativeUserName;
		this.passivesUserName = passivesUserName;
		this.logContent = logContent;
	}

	public void formatToContent(int logType, String slavesUserName, int number) {
		formatToContent(logType, slavesUserName, null, number);
	}
		
	public void formatToContent(int logType, String slavesUserName) {
			formatToContent(logType,slavesUserName, null, 0);
	}
	public void formatToContent(int logType, String slavesUserName, String takeUserName) {
		formatToContent(logType,slavesUserName, takeUserName, 0);
	}
	public void formatToContent(int logType,String slavesUserName, String takeUserName, int number) {
		if(null == slavesUserName || slavesUserName.isEmpty()) {
			return ;
		}
		this.logType = logType;
		switch (logType) {
			case 0://表示谁成为您的奴隶
				initiativeUserName = slavesUserName;
				logContent = slavesUserName + "成为您的奴隶";
				break;
			case 1://表示谁抢走了您的奴隶某某
				if(null == takeUserName || takeUserName.isEmpty()) {
					return ;
				}
				initiativeUserName = takeUserName;
				passivesUserName = slavesUserName;
				logContent = takeUserName + "人品大爆发，趁我不注意抢走了我心爱的奴隶"
						+ slavesUserName;
				break;
			case 2://某某奴隶为您带来了多少金币
				initiativeUserName = slavesUserName;
				incomeCoins = number;
				logContent = slavesUserName + "真心不听话，我拎起骰盅狠狠敲打了他一顿，他乖乖地交出了" + number + "金币";
				break;
			case 3://某某奴隶被您抽打一遍，带来了多少经验
				initiativeUserName = slavesUserName;
				incomeExp = number;
				if(incomeExp == 0) {
					logContent = slavesUserName + "装可怜呀，一定没打够，我上去又是一拳头，竟然被他躲过了";
				} else {
					logContent = slavesUserName + "装可怜呀，一定没打够，我上去又是一拳头，没想到还赚得了" + number + "点经验";
				}
				break;
			case 4://主人奖励你，并为主人带来了多少金币
				initiativeUserName = slavesUserName;
				incomeCoins = number;
				logContent = slavesUserName + "拎起硕大的骰盅敲我，我不得已交出了" + number + "金币的午餐费";
				break;
			case 5://主人折磨了你，并为主人带来了多少经验
				initiativeUserName = slavesUserName;
				incomeExp = number;
				if(incomeExp == 0) {
					logContent = slavesUserName + "又赏了我一拳头，嘿嘿，被我幸运地躲过了!";
				} else {
					logContent = slavesUserName + "又赏了我一拳头，居然还得了" + number + "经验，我暗自发誓：一定当上主人！";
				}
				break;
			default:
				return ;
		}
	}

	@Override
    public String toString()
    {
    	String userReferenceLogInfoString = String.format(
                "logId: %d,ownerUserId: %d,passivesUserId: %d,refId: %d,logType: %d,incomeExp: %d,incomeCoins: %d",
                getLogId(), getOwnerUserId(),
                getPassivesUserId(),getRefId(), getLogType(),
                getIncomeExp(),getIncomeCoins());
        return userReferenceLogInfoString + "logCreateTime: " + getLogCreateTime().toString() + 
        		"initiativeUserName: " + getInitiativeUserName() + "passivesUserName: " + getPassivesUserName() + 
        		"logContent: " + getLogContent() + "Op: " + getOp();
    }

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public int getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(int ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public int getPassivesUserId() {
		return passivesUserId;
	}

	public void setPassivesUserId(int passivesUserId) {
		this.passivesUserId = passivesUserId;
	}

	public int getRefId() {
		return refId;
	}

	public void setRefId(int refId) {
		this.refId = refId;
	}

	public int getLogType() {
		return logType;
	}

	public void setLogType(int logType) {
		this.logType = logType;
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

	public Timestamp getLogCreateTime() {
		return logCreateTime;
	}

	public void setLogCreateTime(Timestamp logCreateTime) {
		this.logCreateTime = logCreateTime;
	}

	public String getInitiativeUserName() {
		return initiativeUserName;
	}

	public void setInitiativeUserName(String initiativeUserName) {
		this.initiativeUserName = initiativeUserName;
	}

	public String getPassivesUserName() {
		return passivesUserName;
	}

	public void setPassivesUserName(String passivesUserName) {
		this.passivesUserName = passivesUserName;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
}
