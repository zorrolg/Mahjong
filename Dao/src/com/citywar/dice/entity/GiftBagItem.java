package com.citywar.dice.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/** 
 *礼包 
 **/
public class GiftBagItem {
	
	private int id;
	/** 物品ID*/
	private int itemId;
	/** 礼包Type*/
	private int bagType;
	/** 礼包count*/
	private int count;

	public GiftBagItem(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.bagType = rs.getInt("gift_bag_type");
		this.itemId = rs.getInt("item_id");
		this.count = rs.getInt("count");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getBagType() {
		return bagType;
	}

	public void setBagType(int bagType) {
		this.bagType = bagType;
	}
	
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
