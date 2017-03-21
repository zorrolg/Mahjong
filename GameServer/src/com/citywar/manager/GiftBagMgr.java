package com.citywar.manager;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.citywar.bll.GiftBagBussiness;
import com.citywar.bll.UserVIPEditionBussiness;
import com.citywar.dice.entity.GiftBag;
import com.citywar.dice.entity.GiftBagItem;
import com.citywar.dice.entity.UserVIPEditionInfo;
import com.citywar.game.GamePlayer;


/** 
 * 
 * 礼包管理类
 * 
 * @author zhiyun.peng
 *
 */
public class GiftBagMgr {
	
	/** 所有 礼包 */
	private static Map<Integer,GiftBag> giftBags ;
	
	/** 登录大礼包ID */
	public static final int  LOGIN_GIFT_BAG = 2;
    
    static boolean STRAT_LOGIN_GIFT = true; //是否启动登录大礼包
	
    public static boolean init()
    {
    	giftBags = new HashMap<Integer, GiftBag>();
        
    	return load();
    }
    
    /** 在初始化礼包前 必须先初始化 物品表*/
    private static boolean load()
    {
    	boolean result = true;
    	List<GiftBagItem> allbag = GiftBagBussiness.getAllGiftBag();
    	
    	if(allbag != null && allbag.size() >0 )
    	{
    		for(GiftBagItem bagItem : allbag)
    		{
    			GiftBag currentBag ;
    			if(giftBags.containsKey(bagItem.getBagType()))
    			{
    				currentBag = giftBags.get(bagItem.getBagType());
    			}
    			else{
    				currentBag = new GiftBag(bagItem.getBagType(),bagItem.getCount());
    				giftBags.put(currentBag.getType(), currentBag);
    			}
    			currentBag.addItem(ItemMgr.findItemTemplate(bagItem.getItemId()));
    		}
    	}
    	return result;
    }
    /**
     * 用户领取礼包
     * @param player 领取的用户
     * @param giftBagId 礼包ID
     */
    public static void acceptGiftBag(GamePlayer player,int giftBagId)
    {
//    	UserVIPEditionInfo userVIPEditionInfo = player.getUserVIPEdition();
//    	if(player == null || player.getPlayerInfo() == null
//    			|| null ==  userVIPEditionInfo){
//    		return ;
//    	}
//    	//查看玩家是否领取过了
//    	if(checkUserVIPEditionIsHave(userVIPEditionInfo)) {
//    		return ;
//    	}
//		GiftBag userBag = GiftBagMgr.getGiftBagById(giftBagId);
//		List<ItemTemplateInfo> items = userBag.getGiftBagItems();
//		if(items == null ){
//			return ;
//		}
//		for (ItemTemplateInfo item:items) 
//		{
//			player.getPropBag().addOneItem(item,userBag.getCount());
//		}
//		userVIPEditionInfo.setAwardTime(new Timestamp(System.currentTimeMillis()));
//		UserVIPEditionBussiness.insertUserVIPEditionInfo(userVIPEditionInfo);
//		
    	
    }
    
    /**
     * 根据指定的礼包ID得到礼包数据
     * 
     * @param giftBagId 礼包的ID
     * 
     * 如果找不到对应的礼包 返回null;
     * 
     */
    public static GiftBag getGiftBagById(int giftBagId)
    {
    	return giftBags.get(giftBagId);
    }
    
    /**
     * 获得玩家领取过了，和网卡ID领取过没
     */
    public static List<UserVIPEditionInfo> getUserVIPEdition(UserVIPEditionInfo info)
    {
    	if(null == info) {
    		return null;
    	}
    	int userId = info.getUserId();
//    	String machineryId = info.getMachineryId();
    	List<UserVIPEditionInfo> userVIPEditionInfos = UserVIPEditionBussiness.getUserVIPEdition(userId);
    	//TODO 暂时 送礼包 不需要避免网卡重复
//    	if(userVIPEditionInfos == null || userVIPEditionInfos.size() == 0) {
//    		userVIPEditionInfos = UserVIPEditionBussiness.getUserVIPEdition(machineryId);
//    	}
		return userVIPEditionInfos;
    }
    
    /**
     * 查看是否玩家领取过了，和网卡ID领取过没
     */
    public static boolean checkUserVIPEditionIsHave(UserVIPEditionInfo info)
    {
    	if( ! STRAT_LOGIN_GIFT) {
    		return true;
    	}
    	List<UserVIPEditionInfo> userVIPEditionInfos = getUserVIPEdition(info);
    	if(userVIPEditionInfos == null || userVIPEditionInfos.size() == 0) {
    		return false;
    	}
		return true;
    }

	public static void stratLoginGift() {
		init();
		STRAT_LOGIN_GIFT = true;
	}

	public static void stopLoginGift() {
		STRAT_LOGIN_GIFT = false;
	}
}
