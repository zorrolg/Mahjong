/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.gameutil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.citywar.bll.ItemBussiness;
import com.citywar.bll.common.LanguageMgr;
import com.citywar.common.BaseItem;
import com.citywar.dice.entity.ItemTemplateInfo;
import com.citywar.dice.entity.Option;
import com.citywar.dice.entity.ShopGoodInfo;
import com.citywar.dice.entity.UserItemInfo;
import com.citywar.game.Bag;
import com.citywar.game.GamePlayer;
import com.citywar.manager.ItemMgr;
import com.citywar.manager.LetterMgr;
import com.citywar.manager.ShopMgr;
import com.citywar.manager.SystemAwardMgr;
import com.citywar.manager.WorldMgr;
import com.citywar.type.PropType;
import com.citywar.util.TimeUtil;

/**
 * 玩家背包, 存放道具(考虑背包应该不会多, 暂不用抽象及分类型的背包处理)
 * 
 * 特别注意，本
 * @author tracy
 * @date 2011-12-30
 * @version
 * 
 */
public class PlayerItemBag implements Bag
{

	
    /**
     * 道具类型是 已 时间来标示 有效期的类型列表
     */
	public static int[] itemTypeIsTime = {2,3,7,8,9,10,15,16,17,18,19,20,21,22};//类型数组
	
	public static int maxItemCount = 999;
	
    private static final Logger logger = Logger.getLogger(PlayerItemBag.class.getName());

    /**
     * 数组形式存储背包物品, 每个数组元素对应一个背包格子<br>
     * (应该可以固定死, 某个格子放某种类型的道具, 在初始化时候就确定好)
     */
    private Map<Integer, BaseItem> itemMap;

    private GamePlayer gamePlayer;

    private Object lock = new Object();

    /**
     * @param gamePlayer
     */
    public PlayerItemBag(GamePlayer gamePlayer)
    {
        itemMap = new HashMap<Integer, BaseItem>();
        this.gamePlayer = gamePlayer;
    }

    /**
     * 商城购买物品后添加到背包, 现在改成了都是新生成数据
     * 重点：两种类型：1 数量来表示有效否  2 有效期来表示有效否 
     * @param baseItem
     * @return
     */
    public boolean addItemAndUse(BaseItem baseItem,int Count)
    {
        if(null == baseItem || null == baseItem.getItemTemplateInfo())
            return false;

        try
        {
        	synchronized (lock)
            {
        		
        		boolean isAdd = false;
        		BaseItem baseItemTemp = null;
            	for (Entry<Integer, BaseItem> entry : itemMap.entrySet())
                {
            		baseItemTemp = entry.getValue();            		
            		if(null != baseItemTemp && null != baseItemTemp.getUserItemInfo()
                			&& baseItemTemp.getItemTemplateInfo().getType() == baseItem.getItemTemplateInfo().getType()) {
            			
            			
                		int NewCount = baseItemTemp.getUserItemInfo().getCount() + Count;                		
                		if(NewCount > maxItemCount)
                			NewCount = maxItemCount;
                		                		
                		baseItemTemp.getUserItemInfo().setCount(NewCount);
                		baseItemTemp.getUserItemInfo().setUsed(true);
                		baseItemTemp.getUserItemInfo().setOp(Option.UPDATE);
        				                		
                		isAdd = true;
                		break;
                	}            		
                }
        		
        		
            	
        		
        		if(!isAdd)
        		{
        			if(ItemBussiness.addUserItem(baseItem.getUserItemInfo())) {
                        
                        int itemId = baseItem.getUserItemInfo().getItemId();
                    	baseItem.getUserItemInfo().setExist(true);//是存在的
                        if(baseItem.getItemTemplateInfo().getType() == PropType.WAKE_TYPE) {//解酒类
                            baseItem.getUserItemInfo().setUsed(false);
                        } else if(baseItem.getItemTemplateInfo().getType() == PropType.TRUMPET_TYPE) {//大喇叭
                            baseItem.getUserItemInfo().setUsed(false);
                        }  
                        else {// 2 有效期来表示有效否
                            baseItem.getUserItemInfo().setUsed(true);
                            Timestamp lastValidityTime = new Timestamp(System.currentTimeMillis());
                            long validityTime = (long)(baseItem.getUserItemInfo().getCount()) * 1000 * 60 * 60 * 24;//使用期限
                            Timestamp itemTypeLastValidityTime = getItemTypeLastValidityTime(baseItem);//获取这种效果的物品是否在使用
                            if(null == itemTypeLastValidityTime || lastValidityTime.after(itemTypeLastValidityTime)) {//现在没有使用
                            	lastValidityTime = new Timestamp(lastValidityTime.getTime() + validityTime);
                            } else {//现在使用,把当前使用这一类物品的最晚时间加上使用期限
                            	lastValidityTime = new Timestamp(itemTypeLastValidityTime.getTime() + validityTime);
                                baseItem.getUserItemInfo().setLastValidityTime(lastValidityTime);
                            }
                        }
                        baseItem.getUserItemInfo().setOp(Option.UPDATE);
                        itemMap.put(itemId, baseItem);
                    }
        		}
        		
        		
                return true;
            }
        }
        catch (Exception e)
        {
            logger.error("[ PlayerBag : addItemAndUse ]", e);
        }
        finally
        {
        }
        return false;
    }
    
    
    public boolean canAddCount(int typeId,int count)
    {
    	
    	boolean canAdd = true;
    	synchronized (lock)
        {
	    	BaseItem baseItemTemp = null;
	    	for (Entry<Integer, BaseItem> entry : itemMap.entrySet())
	        {
	    		baseItemTemp = entry.getValue();            		
	    		if(null != baseItemTemp && null != baseItemTemp.getUserItemInfo()
	        			&& baseItemTemp.getItemTemplateInfo().getType() == typeId) {
	    			
	    			
	        		if(baseItemTemp.getUserItemInfo().getCount() >= maxItemCount)
	        		{
	        			canAdd = false;
	        			break;
	        		}
	        			        		
	        	}            		
	        }
        }
    	return canAdd;
    }
    
    /** 返回用户身上指定 类型ID最大的物品*/
//    public ItemTemplateInfo getMaxTempByGoodType(int tempType )
//    {
//    	ItemTemplateInfo  result = null;
//    	
//    	for (Entry<Integer, BaseItem> entry : itemMap.entrySet())
//        {
//    		BaseItem  item = entry.getValue();
//    		
////			UserItemInfo info = entry.getValue().getUserItemInfo();
//            if(null != item.getItemTemplateInfo()
//            		&& tempType == item.getItemTemplateInfo().getType()//是这种类型
//            		&& item.getUserItemInfo().isExist()
//            		&& item.getUserItemInfo().getCount() > 0) {
//            	
//            	if(result == null 
//            			|| item.getItemTemplateInfo().getTemplateId() > result.getTemplateId()){
//            		result = item.getItemTemplateInfo();
//            	}
//            }
//        }
//    	
//    	return result;
//    }
    
    /** 
     * 
     * 获取用户用户身上最贵的透视眼 
     * 
     * @param eyeType 透视眼类型(越到后面的越贵)
     * */
    public ItemTemplateInfo getMaxEye(int[] eyeType)
    {
    	
    	for (int i = eyeType.length -1 ; i >= 0; i--) {
    		
			if(isExistAndIsUsedTypeEffect(eyeType[i])){
				return ItemMgr.findItemTemplateByType(eyeType[i]);
			}
		}
    	return null;
    }
    
    /** 
     * 
     * 获取用户用户身上最贵的道具
     * 
     * @param toolType 道具类型(越到后面的越贵)
     * */
    public BaseItem getMaxTool(int[] toolType)
    {
    	  for (int i = toolType.length -1 ; i >= 0; i--) 
    	  {
    		  int currentType = toolType[i];
      		
    		     synchronized (lock)
    	            {
    		    	 BaseItem baseItem = null;
    	            	for (Entry<Integer, BaseItem> entry : itemMap.entrySet())
    	                {
    	            		baseItem = entry.getValue();
    	        	        Timestamp lastValidityTime = new Timestamp(System.currentTimeMillis());
    	                    if(null != entry.getValue().getItemTemplateInfo()
    	                    		&& currentType == baseItem.getItemTemplateInfo().getType()//是这种类型
    	                    		&& baseItem.getUserItemInfo().getCount() > 0//有数量
    	        	        		&& baseItem.getUserItemInfo().isExist()//还是存在的
    	        	        		&& null != baseItem.getUserItemInfo().getLastValidityTime()
    	        	        		&& lastValidityTime.before(baseItem.getUserItemInfo().getLastValidityTime())) {//有效期来定的
    	                    	return baseItem;
    	        	        }
    	                }
    	            }
  		}
    	  return null;
    }
    /**
     * 获取用户用户身上指定类型当中最贵的一个物品模板(
     * 
     * @param eyeType 物品模板类型(越到后面的越贵)
     * @return
     */
//    private ItemTemplateInfo findMaxItemByTypes(int[] eyeType)
//    {
//        for (int i = eyeType.length -1 ; i >= 0; i--) {
//    		
//			if(isExistAndIsUsedTypeEffect(eyeType[i])){
//				return ItemMgr.findItemTemplateByType(eyeType[i]);
//			}
//		}
//    	return null;
//    }
    
    
    /**
     * 取得用户在使用某种效果(类型的)最晚有效期
     * 
     * @param baseItem
     * @return
     */
    public Timestamp getItemTypeLastValidityTime (BaseItem baseItem)
    {
        Timestamp lastValidityTime = new Timestamp(System.currentTimeMillis());
        if(null == baseItem || null == baseItem.getItemTemplateInfo())
            return lastValidityTime;

        try
        {
            synchronized (lock)
            {
            	for (Entry<Integer, BaseItem> entry : itemMap.entrySet())
                {
                    if(null != entry.getValue().getItemTemplateInfo()
                    		&& baseItem.getItemTemplateInfo().getType() == entry.getValue().getItemTemplateInfo().getType()//是这种类型
                    		&& isExistAndIsUsedItemEffect(entry.getValue())
        	        		&& null != entry.getValue().getUserItemInfo().getLastValidityTime()
                    		&& lastValidityTime.before(entry.getValue().getUserItemInfo().getLastValidityTime())) {//
                    	lastValidityTime = entry.getValue().getUserItemInfo().getLastValidityTime();
                    }
                }
            }
            return lastValidityTime;
        }
        catch (Exception e)
        {
            logger.error("[ PlayerBag : getItemTypeLastValidityTime ]", e);
        }
        finally
        {

        }
        return null;
    }
    
    /**
     * 查看用户在使用某种效果(类型的)是否在使用
     * 
     * @param typeEffect
     * @return
     */
    public boolean isExistAndIsUsedTypeEffect (int typeEffect)
    {
        try
        {
            synchronized (lock)
            {
            	for (Entry<Integer, BaseItem> entry : itemMap.entrySet())
                {
                    if(null != entry.getValue().getItemTemplateInfo()
                    		&& typeEffect == entry.getValue().getItemTemplateInfo().getType()//是这种类型
                    		&& isExistAndIsUsedItemEffect(entry.getValue())) {//
                        return true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error("[ PlayerBag : getItemTypeLastValidityTime ]", e);
        }
        finally
        {

        }
        return false;
    }
    
    /**
     * 查看用户是否在使用某种效果(类型的)
     * 改变道具的数量
     * @param baseItem
     * @return
     */
    public boolean isExistAndIsUsedItemEffect(BaseItem baseItem)
    {
    	boolean isExistAndIsUsed = false;
    	if(null == baseItem || null == baseItem.getUserItemInfo()
    			|| null == baseItem.getItemTemplateInfo()) {
            return isExistAndIsUsed;
    	}
    	
    	int itemType = baseItem.getItemTemplateInfo().getType();
    	boolean isTimeType = isTimeType(itemType); 
		if(isTimeType) {
	        Timestamp lastValidityTime = new Timestamp(System.currentTimeMillis());
			if(baseItem.getUserItemInfo().getCount() > 0//有数量
	        		&& baseItem.getUserItemInfo().isExist()//还是存在的
	        		&& baseItem.getUserItemInfo().isUsed()//还是在使用
	        		&& null != baseItem.getUserItemInfo().getLastValidityTime()
	        		&& lastValidityTime.before(baseItem.getUserItemInfo().getLastValidityTime())) {//有效期来定的
	        	isExistAndIsUsed = true;
	        } else {//已经用完的
	        	removeBaseItem(baseItem);//函数重用
	            isExistAndIsUsed = false;
	        }
		} else {
			if(baseItem.getUserItemInfo().getCount() > 0//有数量
	        		&& baseItem.getUserItemInfo().isExist()//还是存在的
	        		&& baseItem.getUserItemInfo().isUsed()//还是在使用
	        		) {
				isExistAndIsUsed = true;
			}
		}
        return isExistAndIsUsed;
    }

    /**
     * 是时间类型的道具
     * @param itemType
     * @return
     */
	public boolean isTimeType(int itemType) {
		boolean isTimeType = false;
		for(int type : itemTypeIsTime) {//查看是否是 时间 来判断有效期的
			if(itemType == type) {
				isTimeType = true;
				break;
			}
		}
		return isTimeType;
	}

    /**
     * 初始化背包信息
     * 
     * @param baseItemList
     */
    public void loadItems(List<BaseItem> baseItemList)
    {
        if (baseItemList == null || baseItemList.isEmpty())
            return;

        try
        {
            for (BaseItem baseItem : baseItemList)
            {
                itemMap.put(baseItem.getUserItemInfo().getItemId(),
                            baseItem);
                isExistAndIsUsedItemEffect(baseItem);//查看有效性
            }
        }
        catch (Exception e)
        {
            logger.error("[ PlayerBag : loadItems ]", e);
        }
        finally
        {

        }
    }

    /**
     * 根据模板id 取得相应背包物品<br>
     * TODO 是否需要锁
     * 
     * @param itemId
     * @return
     */
    public BaseItem getBaseItemByItemId(int itemId)
    {
        synchronized (lock)
        {
            return itemMap.get(itemId);
        }
    }

    /**
     * 更新道具数量
     * 
     * @param item
     * @param count
     * @return
     */
    public boolean removeCount(BaseItem item, int toRemoveCount)
    {
    	if(null == item || null == item.getUserItemInfo())
            return false;
        UserItemInfo info = item.getUserItemInfo();
        int oldCount = info.getCount();
        if (toRemoveCount <= 0 || oldCount < toRemoveCount)
            return false;
        if (oldCount == toRemoveCount)
        {
        	removeBaseItem(item);
        }
        else
        {
            item.getUserItemInfo().setCount((short) (oldCount - toRemoveCount));
            item.getUserItemInfo().setOp(Option.UPDATE);
        }
        return true;
    }
    
    /**
     * 添加道具数量
     * @param item
     * @param toAddCount
     * @return
     */
    public boolean addCount(BaseItem item, int toAddCount) {
    	if(null == item || null == item.getUserItemInfo())
            return false;
    	UserItemInfo info = item.getUserItemInfo();
        int oldCount = info.getCount();
        item.getUserItemInfo().setCount((short) (oldCount + toAddCount));
        item.getUserItemInfo().setOp(Option.UPDATE);
    	return true;
    }

    /**
     * 
     * @param item
     * @return
     */
    protected boolean removeBaseItem(BaseItem item)
    {
    	if(null == item || null == item.getUserItemInfo())
            return false;
        // TODO 这个逻辑需再考虑
        item.getUserItemInfo().setCount(0);
        item.getUserItemInfo().setExist(false);
        item.getUserItemInfo().setUsed(false);
        item.getUserItemInfo().setOp(Option.UPDATE);
        return false;
    }

    /**
     * 同种类型的物件叠加
     * @param itemId count
     * @return
     */
    protected void overlyingBaseItemByType(int Type)
    {
    	synchronized (lock)
        {
    		//TODO
        }
    }

    public List<BaseItem> getAllItems()
    {
        List<BaseItem> list = new LinkedList<BaseItem>();

        synchronized (lock)
        {
            for (Entry<Integer, BaseItem> entry : itemMap.entrySet())
            {
            	if(entry.getValue().getUserItemInfo().getCount() > 0)
            		list.add(entry.getValue());
            }
        }
        return list;
    }

    public List<Integer> getAllItemTypes()
    {
        List<Integer> list = new LinkedList<Integer>();

        synchronized (lock)
        {
        	int type = 0;
            for (Entry<Integer, BaseItem> entry : itemMap.entrySet())
            {
            	type = entry.getValue().getItemTemplateInfo().getType();
                if( ! list.contains(type)) {
                	list.add(type);
                }
            }
        }
        return list;
    }

    @Override
    public int getPropCountByItemId(int itemId)
    {
        synchronized (lock)
        {
        	BaseItem item = itemMap.get(itemId);
        	if(null !=item && null != item.getUserItemInfo()) {
                return item.getUserItemInfo().getCount();
        	}
        }
        return 0;
    }

    /**
     * 将背包保存入数据库
     * 
     * @return
     */
    public boolean saveIntoDb()
    {
        List<UserItemInfo> updateItems = new ArrayList<UserItemInfo>();
        List<UserItemInfo> insertItems = new ArrayList<UserItemInfo>();
        synchronized (lock)
        {
            for (BaseItem baseItem : itemMap.values())
            {
                // TODO 可处理逻辑
                if (baseItem == null)
                    continue;
                UserItemInfo info = baseItem.getUserItemInfo();
                switch (info.getOp())
                {
                    case Option.INSERT:
                        insertItems.add(info);
                        break;

                    case Option.UPDATE:
                        updateItems.add(info);
                        break;
                    default:
                        break;
                }
                info.setOp(Option.NONE);
            }
        }
        ItemBussiness.updateItem(updateItems);
        ItemBussiness.addUserItems(insertItems);
        return true;
    }
    
    /**
     * 获取某种道具类型的数量（查看某种道具的数量） 
     * 有根据数量和有效期两种类型来计算
     * @return propCount
     */
	public int getPropCountByType(int itemType) {
		int propCount = 0;
		boolean isTimeType = false;
		try
        {
			synchronized (lock)
	        {
        		for(int type : itemTypeIsTime) {//查看是否是 时间 来判断有效期的
        			if(itemType == type) {
        				isTimeType = true;
        				break;
        			}
        		} 
        		if(isTimeType) {
        			Timestamp systemTime = new Timestamp(System.currentTimeMillis());
        			Timestamp lastValidityTime = new Timestamp(System.currentTimeMillis());
        			for (BaseItem baseItem : itemMap.values())
    	            {
    	            	if(baseItem.getItemTemplateInfo().getType() == itemType
    	            			&& baseItem.getUserItemInfo().isExist()
    	    	        		&& null != baseItem.getUserItemInfo().getLastValidityTime()
    	            			&& lastValidityTime.before(baseItem.getUserItemInfo().getLastValidityTime())) {
    	            		lastValidityTime = baseItem.getUserItemInfo().getLastValidityTime();
    	            	}
    	            }
        			Calendar lastLoginDateCalendar = Calendar.getInstance(); 
        			lastLoginDateCalendar.setTime(lastValidityTime);
        			Calendar todayCalendar = Calendar.getInstance(); 
        			todayCalendar.setTime(systemTime);
        			propCount = TimeUtil.dateCompare(todayCalendar, lastLoginDateCalendar);
        		} else {
//        			boolean isUsed = false;
    	            for (BaseItem baseItem : itemMap.values())
    	            {
    	            	if(baseItem.getItemTemplateInfo().getType() == itemType
    	            			&& baseItem.getUserItemInfo().isExist()
    	            			&& baseItem.getUserItemInfo().getCount() > 0) {
    	            		propCount += baseItem.getUserItemInfo().getCount();
    	            		if(baseItem.getUserItemInfo().isUsed()) {
//        	            		isUsed = true;
                    		}
    	            	}
    	            }
    	            
        		}
	        }
        }
        catch (Exception e)
        {
            logger.error("[ PlayerBag : getPropCountByType ]", e);
        }
        finally
        {

        }
		return propCount;
	}
	
	/**
     * 使用某种道具局（根据userItemId来确定某一个道具）
     * 
     * @return
     */
	public boolean usePropItemByItemId(int itemId, int count) {
		try
        {
			synchronized (lock)
	        {
            	BaseItem baseItem = itemMap.get(itemId);
            	if(null != baseItem && null != baseItem.getUserItemInfo()
            			&& baseItem.getUserItemInfo().getCount() >= count) {
            		int oldCount = baseItem.getUserItemInfo().getCount();
            		baseItem.getUserItemInfo().setCount(oldCount - count);
            		baseItem.getUserItemInfo().setUsed(true);
            		baseItem.getUserItemInfo().setOp(Option.UPDATE);
    				for(int i=0;i<count;i++) {
    					haveAnEffectByType(baseItem.getItemTemplateInfo().getType(),baseItem.getItemTemplateInfo().getPrar1());
    				}
            		return true;
            	}
	        }
        }
        catch (Exception e)
        {
            logger.error("[ PlayerBag : usePropItemByItemId ]", e);
        }
        finally
        {

        }
		return false;
	}
	


	

	/**
     * 使用某种道具（根据type来确定某一种道具）
     * 
     * @return
     */
	public boolean usePropItemByType(int type, int count) {

		
		try
        {
			List<BaseItem> itemList = new ArrayList<BaseItem>();
			int allCount = 0;
			synchronized (lock)
	        {
				for (Entry<Integer, BaseItem> entry : itemMap.entrySet())
                {
					UserItemInfo info = entry.getValue().getUserItemInfo();
                    if(null != entry.getValue().getItemTemplateInfo()
                    		&& type == entry.getValue().getUserItemInfo().getTemplateId()//是这种类型
                    		&& info.isExist()
                    		&& info.getCount() > 0) {//
                    	allCount += info.getCount();
                    	itemList.add(entry.getValue());
                    }
                }
	        }
			if(allCount >= count) {
				int tempCount = count;
				int itemCount = 0;
				for (BaseItem item : itemList) {//一个一个使用
					if(tempCount <= 0) {
						break ;
					}
					itemCount = item.getUserItemInfo().getCount();
					if(tempCount > itemCount) {
						tempCount -= itemCount;
						item.getUserItemInfo().setCount(0);
						item.getUserItemInfo().setUsed(false);
						item.getUserItemInfo().setExist(false);
					} else {
						itemCount -= tempCount;
						item.getUserItemInfo().setCount(itemCount);
						item.getUserItemInfo().setUsed(true);
						tempCount = 0;
					}
					item.getUserItemInfo().setOp(Option.UPDATE);
					
					haveAnEffectByType(item.getItemTemplateInfo().getType(),item.getItemTemplateInfo().getPrar1());
                }
//				for(int i=0;i<count;i++) {
//					haveAnEffectByType(type);
//				}
		        return true;
			}
        }
        catch (Exception e)
        {
            logger.error("[ PlayerBag : usePropItemByType ]", e);
        }
        finally
        {

        }
		return false;
	}

	/**
     * 使用道具产生效果
     * 对自己
     * @return
     */
	public void haveAnEffectByType(int type,int para1) {
        switch (type)
        {
            case PropType.WAKE_TYPE: // 解酒类
			    gamePlayer.addDrunkLevel(para1);
                break;
            default:
                break;
        }
	}

	/**
     * 任务领取奖励的时候给用户带来的道具（仿照商定购买）
     * 
     * @return
     */
	public void addPrizeItemCount(int templateId, int prizeNum) {
		ItemTemplateInfo template = ItemMgr.findItemTemplate(templateId);
		BaseItem baseItem = BaseItem.createFromTemplate(template, prizeNum);
		if(null == baseItem) {
			return ;
		}
		baseItem.getUserItemInfo().setUserId(gamePlayer.getUserId());
		baseItem.getUserItemInfo().setCount(prizeNum);
		addItemAndUse(baseItem,prizeNum);
	}
	
	/**
     * 用户通过苹果商店购买
     * 
     * @return
     */
	public void addOneItem(int shopId) {
		ShopGoodInfo shopGood = ShopMgr.getShopGoodById(shopId);
		BaseItem baseItem = ShopMgr.createBaseItem(shopGood);// 构建商品信息
		if(null == baseItem) {
			return ;
		}
		if (baseItem.getItemTemplateInfo().getTemplateId() == 0)
        // 游戏币等, 直接更改人物属性值
        {
            gamePlayer.addCoins(shopGood.getCount(), false);
            gamePlayer.getPlayerInfo().setOp(Option.UPDATE);
        }
		else if (baseItem.getItemTemplateInfo().getTemplateId() == -1)
	    // 游戏币等, 直接更改人物属性值
	    {
	        gamePlayer.addMoney(shopGood.getCount());
	        gamePlayer.getPlayerInfo().setOp(Option.UPDATE);
	    }
		else if (baseItem.getItemTemplateInfo().getTemplateId() == -2)
	    // 游戏币等, 直接更改人物属性值
	    {
			gamePlayer.addVip(shopGood.getValidDay());
			gamePlayer.addMoney(shopGood.getCount());
	        gamePlayer.getPlayerInfo().setOp(Option.UPDATE);
	        
	        
	        
	        String strMsg = LanguageMgr.getTranslation("CityWar.Vip.Push",gamePlayer.getPlayerInfo().getUserName());		
            WorldMgr.sendSystemTrumpet(strMsg);            
            
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            LetterMgr.send(SystemAwardMgr.createSystemLetter(gamePlayer,LanguageMgr.getTranslation("CityWar.Vip.Letter",df.format(gamePlayer.getPlayerInfo().getVipDate()))));

	    }
		else {
    		baseItem.getUserItemInfo().setUserId(gamePlayer.getUserId());
    		addItemAndUse(baseItem,shopGood.getCount());
        }
	}
	public void addOneItem(ItemTemplateInfo info,int count){
		BaseItem baseItem = ShopMgr.createBaseItem(info,count);// 构建商品信息
		if(null == baseItem) {
			return ;
		}
		if (baseItem.getItemTemplateInfo().getTemplateId() == 0)
        // 游戏币等, 直接更改人物属性值
        {
            gamePlayer.addCoins(count, false);
            gamePlayer.getPlayerInfo().setOp(Option.UPDATE);
        }
		else if (baseItem.getItemTemplateInfo().getTemplateId() == -1)
		// 游戏币等, 直接更改人物属性值
		{
		    gamePlayer.addMoney(count);
		    gamePlayer.getPlayerInfo().setOp(Option.UPDATE);
		}
		else {
    		baseItem.getUserItemInfo().setUserId(gamePlayer.getUserId());
    		addItemAndUse(baseItem,count);
        }
	}
	

}
