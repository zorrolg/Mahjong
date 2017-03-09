package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class CardTypeInfo {
	

	
	private int cardTypeId;
	
	private String buildTypeName;
	

	private int gameType;
	private int useType;
	private int para;
	
	
	
	public CardTypeInfo() {}
	
	public CardTypeInfo(ResultSet rs) throws SQLException{
		
		this.setCardTypeId(rs.getInt("card_type_id"));
		this.setCardTypeName(rs.getString("name"));
		this.setGameType(rs.getInt("game_type"));
		this.setUserType(rs.getInt("use_type"));
		this.setPara(rs.getInt("para"));
		
	}


	public int getCardTypeId() {
		return cardTypeId;
	}
	
	public void setCardTypeId(int cardTypeId) {
		this.cardTypeId = cardTypeId;
	}
	
	public String getCardTypeName() {
		return buildTypeName;
	}
	
	public void setCardTypeName(String buildTypeName) {
		this.buildTypeName = buildTypeName;
	}
	

	
	public int getGameType() {
		return gameType;
	}
	
	public void setGameType(int gameType) {
		this.gameType = gameType;
	}
	
	
	public int getUserType() {
		return useType;
	}
	
	public void setUserType(int useType) {
		this.useType = useType;
	}
	
	public int getPara() {
		return para;
	}
	
	public void setPara(int para) {
		this.para = para;
	}
	
	
}
