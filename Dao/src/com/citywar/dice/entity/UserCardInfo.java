package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;



/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class UserCardInfo {
	
	private int PlayerId;
	
	
	private String cardDevelopList;
	
	private int cardIsDevelop;
	
	private int cardDevelopId;
	
	private Timestamp cardDevelopTime;
	
	
	private String cardFactoryList;
	
	private int cardIsFactory;
	
	private Timestamp cardFactoryTime;
	
	
	
	public UserCardInfo(int userId, String firstDevelop, String firstFactory) {
		
		this.setPlayerId(userId);
		this.setCardDevelopList(firstDevelop);
		this.setCardIsDevelop(0);
		this.setCardDevelopId(0);
		this.setCardDevelopTime(new Timestamp(System.currentTimeMillis()));
		
		this.setCardFactoryList("");
		this.setCardIsFactory(0);
		this.setCardFactoryTime(new Timestamp(System.currentTimeMillis()));
				
	}
	
	public UserCardInfo(ResultSet rs) throws SQLException{
		
		this.setPlayerId(rs.getInt("UserId"));
		this.setCardDevelopList(rs.getString("card_develop_list"));
		this.setCardIsDevelop(rs.getInt("card_isdevelop"));
		this.setCardDevelopId(rs.getInt("card_develop_id"));
		this.setCardDevelopTime(rs.getTimestamp("card_develop_time"));
		
		this.setCardFactoryList(rs.getString("card_factory_list"));
		this.setCardIsFactory(rs.getInt("card_isfactory"));
		this.setCardFactoryTime(rs.getTimestamp("card_factory_time"));
		
	}
	
	
	public int getPlayerId() {
		return PlayerId;
	}
	
	public void setPlayerId(int PlayerId) {
		this.PlayerId = PlayerId;
	}
	
	public String getCardDevelopList() {
		return cardDevelopList;
	}
	
	public void setCardDevelopList(String cardDevelopList) {
		this.cardDevelopList = cardDevelopList;
	}
	
	public int getCardIsDevelop() {
		return cardIsDevelop;
	}
	
	public void setCardIsDevelop(int cardIsDevelop) {
		this.cardIsDevelop = cardIsDevelop;
	}

	public int getCardDevelopId() {
		return cardDevelopId;
	}
	
	public void setCardDevelopId(int cardDevelopId) {
		this.cardDevelopId = cardDevelopId;
	}
	
	public Timestamp getCardDevelopTime() {
		return cardDevelopTime;
	}
	
	public void setCardDevelopTime(Timestamp cardDevelopTime) {
		this.cardDevelopTime = cardDevelopTime;
	}
	
	
	
	
	
	public String getCardFactoryList() {
		return cardFactoryList;
	}
	
	public void setCardFactoryList(String cardFactoryList) {
		this.cardFactoryList = cardFactoryList;
	}
	
	public int getCardIsFactory() {
		return cardIsFactory;
	}
	
	public void setCardIsFactory(int cardIsFactory) {
		this.cardIsFactory = cardIsFactory;
	}
	
	public Timestamp getCardFactoryTime() {
		return cardFactoryTime;
	}
	
	public void setCardFactoryTime(java.sql.Timestamp timestamp) {
		this.cardFactoryTime = timestamp;
	}
	
	
}
