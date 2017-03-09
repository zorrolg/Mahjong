package com.citywar.dice.entity;

import java.util.ArrayList;
import java.util.List;


/** 
 *礼包 
 **/
public class GiftBag {
	
	private int type;
	
	private List<ItemTemplateInfo> giftBagItems;
	
	private int count;
	
	public GiftBag(int type,int count)
	{
		this.type = type;
		this.count = count;
	}
	
	public void addItem(ItemTemplateInfo item)
	{
		if(item == null ){
			return ;
		}
		if(giftBagItems == null )
		{
			giftBagItems = new ArrayList<ItemTemplateInfo>();
		}
		giftBagItems.add(item);
		
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public List<ItemTemplateInfo> getGiftBagItems() {
		return giftBagItems;
	}
	
	
}
