/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 礼品模板信息
 * 
 * @author shanfeng.cao
 * @date 2012-06-26
 * @version
 * 
 */
public class GiftTemplateInfo {
	/**
	 * 礼品模板 Id
	 */
	private int giftId;
	/**
	 * 礼品模板类型（1鲜花，2宠物，3跑车，4别墅，5雪茄）
	 */
	private int giftType;
	/**
	 * 礼品模板名称
	 */
	private String giftName;
	/**
	 * 礼品模板价格
	 */
	private int giftPrice;
	/**
	 * 礼品模板魅力值
	 */
	private int giftCharmValue;
	/**
	 * 礼品的描述语句
	 */
	private String giftDescribe;
	/**
	 * 礼品的展示图片地址
	 */
	private String giftShowPicPath;
	/**
	 * 礼品的效果地址
	 */
	private String giftEffectPath;
	/**
	 * 礼品的购买类型（在哪里可以购买）
	 */
	private int giftBuyType;

	/**
     * 
     */
	public GiftTemplateInfo() {
		super();
	}

	public GiftTemplateInfo(int giftId, int giftType, String giftName,
			int giftPrice, int giftCharmValue, String giftDescribe,
			String giftShowPicPath, String giftEffectPath, int giftBuyType) {
		super();
		this.giftId = giftId;
		this.giftType = giftType;
		this.giftName = giftName;
		this.giftPrice = giftPrice;
		this.giftCharmValue = giftCharmValue;
		this.giftDescribe = giftDescribe;
		this.giftShowPicPath = giftShowPicPath;
		this.giftEffectPath = giftEffectPath;
		this.giftBuyType = giftBuyType;
	}

	public GiftTemplateInfo(ResultSet rs) throws SQLException {
		this.giftId = rs.getInt("gift_id");
		this.giftType = rs.getInt("gift_type");
		this.giftName = rs.getString("gift_name");
		this.giftPrice = rs.getInt("gift_price");
		this.giftCharmValue = rs.getInt("gift_charm_value");
		this.giftDescribe = rs.getString("gift_describe");
		this.giftShowPicPath = rs.getString("gift_show_picPath");
		this.giftEffectPath = rs.getString("gift_effect_path");
		this.giftBuyType = rs.getInt("gift_buy_type");
	}

	@Override
	public String toString() {
		String GiftTemplateInfoString = String
				.format("giftId: %d,giftType: %d,giftPrice: %d,giftCharmValue: %d,giftBuyType: %d",
						getGiftId(), getGiftType(), getGiftPrice(),
						getGiftCharmValue(), getGiftBuyType());
		return GiftTemplateInfoString + "giftName: " + getGiftName()
				+ "giftDescribe: " + getGiftDescribe() + "giftShowPicPath: "
				+ getGiftShowPicPath() + "giftEffectPath: "
				+ getGiftEffectPath();
	}

	public int getGiftId() {
		return giftId;
	}

	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}

	public int getGiftType() {
		return giftType;
	}

	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public int getGiftPrice() {
		return giftPrice;
	}

	public void setGiftPrice(int giftPrice) {
		this.giftPrice = giftPrice;
	}

	public int getGiftCharmValue() {
		return giftCharmValue;
	}

	public void setGiftCharmValue(int giftCharmValue) {
		this.giftCharmValue = giftCharmValue;
	}

	public String getGiftDescribe() {
		return giftDescribe;
	}

	public void setGiftDescribe(String giftDescribe) {
		this.giftDescribe = giftDescribe;
	}

	public String getGiftShowPicPath() {
		return giftShowPicPath;
	}

	public void setGiftShowPicPath(String giftShowPicPath) {
		this.giftShowPicPath = giftShowPicPath;
	}

	public String getGiftEffectPath() {
		return giftEffectPath;
	}

	public void setGiftEffectPath(String giftEffectPath) {
		this.giftEffectPath = giftEffectPath;
	}

	public int getGiftBuyType() {
		return giftBuyType;
	}

	public void setGiftBuyType(int giftBuyType) {
		this.giftBuyType = giftBuyType;
	}
}
