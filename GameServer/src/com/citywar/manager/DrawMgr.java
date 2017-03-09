/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.citywar.bll.DrawGoodBussiness;
import com.citywar.dice.entity.DrawGoodInfo;


/**
 * 商店商品缓存管理
 * 
 * @author tracy
 * @date 2011-12-20
 * @version
 * 
 */
public class DrawMgr
{
	
    private final static Logger LOGGER = Logger.getLogger(DrawMgr.class.getName());
    /**
	 * Key--模板ID(TemplateId)	value--对应的商品信息	对应的数据库表为：t_s_shop
	 */
    private static Map<Integer, DrawGoodInfo> drawGoodsMap = new HashMap<Integer, DrawGoodInfo>();
    

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
        Map<Integer, DrawGoodInfo> tempMap = loadFromDataBase();
       
        if (tempMap.size() > 0)
        {
            rwLock.writeLock().lock();
            drawGoodsMap = tempMap;
            rwLock.writeLock().unlock();
        }
        return true;
    }

    /**
     * 从数据库加载
     * 
     * @return
     */
    private static Map<Integer, DrawGoodInfo> loadFromDataBase()
    {
        Map<Integer, DrawGoodInfo> tempMap = new LinkedHashMap<Integer, DrawGoodInfo>();
        List<DrawGoodInfo> tempList = DrawGoodBussiness.getAllDrawGoods();
        for (DrawGoodInfo info : tempList)
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
    public static DrawGoodInfo getShopGoodById(int id)
    {
        if (drawGoodsMap.containsKey(id))
            return drawGoodsMap.get(id);
        LOGGER.warn("Can not find DrawGoodInfo by given id : [ " + id + " ]");
        return null;
    }



    /**
     * 获取某类商品的信息(shpoType为商店类型：1为使用RMB的,2为使用游戏币)
     * 
     * @param shpoType
     * @return List<DrawGoodInfo>
     */
    public static List<DrawGoodInfo> getShopGoodsByType(int drawType, int drawPara)
    {
    	List<DrawGoodInfo> list = new ArrayList<DrawGoodInfo>();
    	Collection<DrawGoodInfo> values = drawGoodsMap.values();
    	for(DrawGoodInfo info : values) {
    		if(info.getDrawType() == drawType && info.getDrawPara() == drawPara) {
        		list.add(info);
    		}
    	}
        if (list.size()>0) {
            return list;
        }
        LOGGER.warn("Can not find DrawGoodInfos by given shpoType : [ "
                + drawType + " ]");
        return null;
    }

 


}


