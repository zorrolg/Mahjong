/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.ShopGoodBussiness;
import com.citywar.common.BaseItem;
import com.citywar.dice.entity.ItemTemplateInfo;
import com.citywar.dice.entity.ShopGoodInfo;


/**
 * 商店商品缓存管理
 * 
 * @author tracy
 * @date 2011-12-20
 * @version
 * 
 */
public class ShopMgr
{
    private final static Logger LOGGER = Logger.getLogger(ShopMgr.class.getName());
    /**
	 * Key--模板ID(TemplateId)	value--对应的商品信息	对应的数据库表为：t_s_shop
	 */
    private static Map<Integer, ShopGoodInfo> shopGoodsMap = new HashMap<Integer, ShopGoodInfo>();
    /**
	 * Key--商品类型(醒酒类, 表情类等)(goodType)	value--对应的商品信息List
	 */
    private static Map<Byte, List<ShopGoodInfo>> typeGoodsMap = new HashMap<Byte, List<ShopGoodInfo>>();

    private static ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 初始化商品信息
     * 
     * @return
     */
    public static boolean init()
    {
        return reload();
    }

    /**
     * 重新加载商品信息
     * 
     * @return
     */
    public static boolean reload()
    {
        Map<Integer, ShopGoodInfo> tempMap = loadFromDataBase();

        Map<Byte, List<ShopGoodInfo>> tempTypeGoodsMap = new HashMap<Byte, List<ShopGoodInfo>>();

        List<ShopGoodInfo> infoList;
        for (Entry<Integer, ShopGoodInfo> entry : tempMap.entrySet())
        {
            byte goodType = entry.getValue().getGoodType();
            ShopGoodInfo tempInfo = entry.getValue();
            if (!tempTypeGoodsMap.containsKey(goodType))
            {
                infoList = new LinkedList<ShopGoodInfo>();
                infoList.add(tempInfo);//关键
                tempTypeGoodsMap.put(goodType, infoList);
            }
            else
            {
                tempTypeGoodsMap.get(goodType).add(tempInfo);
            }
        }
        for(List<ShopGoodInfo> list : tempTypeGoodsMap.values()) {
        	Collections.sort(list, new ShopSortComparator());
        }
        if (tempMap.size() > 0)
        {
            rwLock.writeLock().lock();
            shopGoodsMap = tempMap;
            typeGoodsMap = tempTypeGoodsMap;
            rwLock.writeLock().unlock();
        }
        return true;
    }

    /**
     * 从数据库加载
     * 
     * @return
     */
    private static Map<Integer, ShopGoodInfo> loadFromDataBase()
    {
        Map<Integer, ShopGoodInfo> tempMap = new LinkedHashMap<Integer, ShopGoodInfo>();
        List<ShopGoodInfo> tempList = ShopGoodBussiness.getAllShopGoods();
        for (ShopGoodInfo info : tempList)
        {
            if (!tempMap.containsKey(info.getTemplateId()))//TemplateId来唯一标识
            {
                tempMap.put(info.getId(), info);
            }
        }
        return tempMap;
    }

    /**
     * 获取单个商品的信息
     * 
     * @param id
     * @return
     */
    public static ShopGoodInfo getShopGoodById(int id)
    {
        if (shopGoodsMap.containsKey(id))
            return shopGoodsMap.get(id);
        LOGGER.warn("Can not find ShopGoodInfo by given id : [ " + id + " ]");
        return null;
    }

    public static List<ShopGoodInfo> getShopGoodsByGoodType(byte goodType)
    {
        if (typeGoodsMap.containsKey(goodType))
            return typeGoodsMap.get(goodType);
        LOGGER.warn("Can not find ShopGoodInfos by given goodType : [ "
                + goodType + " ]");
        return null;
    }

    /**
     * 获取某类商品的信息(shpoType为商店类型：1为使用RMB的,2为使用游戏币)
     * 
     * @param shpoType
     * @return List<ShopGoodInfo>
     */
    public static List<ShopGoodInfo> getShopGoodsByShopType(byte shpoType)
    {
    	List<ShopGoodInfo> list = new ArrayList<ShopGoodInfo>();
    	Collection<ShopGoodInfo> values = shopGoodsMap.values();
    	for(ShopGoodInfo info : values) {
    		if(info.getShopType() == shpoType) {
        		list.add(info);
    		}
    	}
        if (list.size()>0) {
            return list;
        }
        LOGGER.warn("Can not find ShopGoodInfos by given shpoType : [ "
                + shpoType + " ]");
        return null;
    }

    /**
     * 构建BaseItem，通过ItemTemplateInfo
     * @param template
     * @return
     */
    public static BaseItem createBaseItem(ItemTemplateInfo template,int count)
    {
    	if(null == template) {
            return null;
    	}
        // TODO 可在此处理有效期, 购买数量之类的逻辑
        BaseItem baseItem = null;
        if (null != template) {
//        	switch (template.getTemplateId())
//            {
//                case PropType.WAKE_DRUG:
//                    count *= 20;
//                    break;
//                default:
//                    break;
//            }
        	baseItem = BaseItem.createFromTemplate(template, count);
        }
        return baseItem;
    }

    /**
     * 构建BaseItem，通过ShopGoodInfo
     * @param template
     * @return
     */
    public static BaseItem createBaseItem(ShopGoodInfo goodInfo)
    {
    	if(null == goodInfo) {
            return null;
    	}
        ItemTemplateInfo template = ItemMgr.findItemTemplate(goodInfo.getTemplateId());
        return createBaseItem(template,goodInfo.getCount());
    }

	public static List<ShopGoodInfo> getGoodsByGoodsItemType(int itemType) {
		List<ShopGoodInfo> list = new ArrayList<ShopGoodInfo>();
    	Collection<ShopGoodInfo> values = shopGoodsMap.values();
    	for(ShopGoodInfo info : values) {
    		ItemTemplateInfo itemInfo = ItemMgr.findItemTemplate(info.getTemplateId());
    		if(null != itemInfo) {
        		int tempItemType = itemInfo.getType();
        		if(itemType == tempItemType) {
            		list.add(info);
        		}
    		}
    	}
        if (list.size()>0) {
            return list;
        }
        LOGGER.warn("Can not find ShopGoodInfos by given itemType : [ "
                + itemType + " ]");
        return null;
	}
}

class ShopSortComparator implements Comparator<Object> 
{
    @Override
    public int compare(Object o1, Object o2)
    {
    	int result = 0;
    	int o1haveSort = 0;
    	int o2haveSort = 0;
    	if(o1 instanceof ShopGoodInfo && o2 instanceof ShopGoodInfo) {
    		ShopGoodInfo p1 = (ShopGoodInfo)o1;
    		ShopGoodInfo p2 = (ShopGoodInfo)o2;
    		if(p1 != null && p2 != null) {
    			o1haveSort = p1.getId();
    			o2haveSort = p2.getId();
    			result = o1haveSort - o2haveSort;
    		}
    	}
		return result;
    }
}
