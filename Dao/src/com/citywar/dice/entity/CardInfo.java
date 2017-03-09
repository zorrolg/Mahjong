package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jacky.zheng
 * @date 2012-03-23
 * @desc 任务信息对象模型类
 */
public class CardInfo {
	
	private int cardId;
	
	private int cardTypeId;
	
	private int cardFightId;
	
	private String cardName;
	
	private int cardLevel;
	
	private int cardStar;
	
	private int cardStarLevel;
	
	private int cardPlace;
	
	private String cardImg;
	
	private String descript;
	
	private int para1;
	
	private int para2;
	
	private int buildDevelopLevel;
	
	private float developTime;
	
	private int developCoin;
	
	private int developMoney;
	
	private int buildFactoryLevel;
	
	private int factoryTime;
	
	private int factoryCoin;
	
	
	public CardInfo() {}
	
	public CardInfo(ResultSet rs) throws SQLException{
		
		this.setCardId(rs.getInt("card_id"));
		this.setCardTypeId(rs.getInt("card_type_id"));
		this.setCardFightId(rs.getInt("card_fight_id"));
		
		this.setCardName(rs.getString("name"));
		this.setCardLevel(rs.getInt("level"));
		this.setCardStar(rs.getInt("star"));
		this.setCardStarLevel(rs.getInt("star_level"));
		this.setCardPlace(rs.getInt("place"));
		this.setCardImg(rs.getString("image"));
		this.setDescript(rs.getString("descript"));
		this.setPara1(rs.getInt("para_1"));
		this.setPara2(rs.getInt("para_2"));
		
		this.setBuildDevelopLevel(rs.getInt("build_develop_level"));
		this.setDevelopTime (rs.getFloat("develop_time"));
		this.setDevelopCoin(rs.getInt("develop_coin"));
		this.setDevelopMoney(rs.getInt("develop_money"));
		
		this.setBuildFactoryLevel(rs.getInt("build_factory_level"));
		this.setFactoryTime(rs.getInt("factory_time"));
		this.setFactoryCoin(rs.getInt("factory_coin"));
	
		
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getCardTypeId() {
		return cardTypeId;
	}
	
	public void setCardTypeId(int cardTypeId) {
		this.cardTypeId = cardTypeId;
	}
	
	public int getCardFightId() {
		return cardFightId;
	}
	
	public void setCardFightId(int cardFightId) {
		this.cardFightId = cardFightId;
	}
	
	public String getCardName() {
		return cardName;
	}
	
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	public int getCardLevel() {
		return cardLevel;
	}
	
	public void setCardLevel(int cardLevel) {
		this.cardLevel = cardLevel;
	}
	
	public int getCardStar() {
		return cardStar;
	}
	
	public void setCardStar(int cardStar) {
		this.cardStar = cardStar;
	}
	
	public int getCardStarLevel() {
		return cardStarLevel;
	}
	
	public void setCardStarLevel(int cardStarLevel) {
		this.cardStarLevel = cardStarLevel;
	}
	
	
	public int getCardPlace() {
		return cardPlace;
	}
	
	public void setCardPlace(int cardPlace) {
		this.cardPlace = cardPlace;
	}
	
	public String getCardImg() {
		return cardImg;
	}
	
	public void setCardImg(String cardImg) {
		this.cardImg = cardImg;
	}
	
	public String getDescript() {
		return descript;
	}
	
	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	public int getPara1() {
		return para1;
	}
	
	public void setPara1(int para1) {
		this.para1 = para1;
	}
	
	public int getPara2() {
		return para2;
	}
	
	public void setPara2(int para2) {
		this.para2 = para2;
	}
	
	
	
	public int getBuildDevelopLevel() {
		return buildDevelopLevel;
	}
	
	public void setBuildDevelopLevel(int buildDevelopLevel) {
		this.buildDevelopLevel = buildDevelopLevel;
	}
	
	public float getDevelopTime() {
		return developTime;
	}
	
	public void setDevelopTime(float developTime) {
		this.developTime = developTime;
	}
	
	public int getDevelopCoin() {
		return developCoin;
	}
	
	public void setDevelopCoin(int developCoin) {
		this.developCoin = developCoin;
	}
	
	public int getDevelopMoney() {
		return developMoney;
	}
	
	public void setDevelopMoney(int developMoney) {
		this.developMoney = developMoney;
	}
	
	public int getBuildFactoryLevel() {
		return buildFactoryLevel;
	}
	
	public void setBuildFactoryLevel(int buildFactoryLevel) {
		this.buildFactoryLevel = buildFactoryLevel;
	}
	
	public int getFactoryTime() {
		return factoryTime;
	}
	
	public void setFactoryTime(int factoryTime) {
		this.factoryTime = factoryTime;
	}
	
	public int getFactoryCoin() {
		return factoryCoin;
	}
	
	public void setFactoryCoin(int factoryCoin) {
		this.factoryCoin = factoryCoin;
	}
	

	
	
	
	
	
	
	

}
