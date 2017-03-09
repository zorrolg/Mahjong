/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.gameutil;

//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//import org.apache.log4j.Logger;

//import com.citywar.dice.bll.ItemBussiness;
//import com.citywar.dice.bll.PlayerBussiness;
//import com.citywar.dice.common.BaseItem;
//import com.citywar.dice.entity.ItemInfo;
//import com.citywar.dice.entity.Option;
//import com.citywar.dice.gameobjects.GamePlayer;

/**
 * @author : Cookie
 * @date : 2011-5-10
 * @version 人物主背包
 */
public class PlayerInventory extends AbstractInventory
{
//    private static Logger logger = Logger.getLogger(PlayerInventory.class.getName());
//
//    protected GamePlayer player;
//
//    private boolean saveToDb;
//
//    private List<BaseItem> removedList = new LinkedList<BaseItem>();
//
//    public PlayerInventory(GamePlayer player, boolean saveTodb, int capibility,
//            int type, int beginSlot, boolean autoStack)
//    {
//        super(capibility, (byte) type, beginSlot, autoStack);
//        this.player = player;
//        saveToDb = saveTodb;
//    }
//
//    @Override
//    public boolean AddItem(BaseItem item, int minSlot)
//    {
//        if (item == null)
//            return false;
//
//        byte place = FindFirstEmptySlot(minSlot);
//
//        return AddItemTo(item, place);
//    }
//
//    public boolean AddItemTo(BaseItem item, byte place)
//    {
//        try
//        {
//            if (super.AddItemTo(item, place))
//            {
//                item.getItemInfo().setIsExist(true);
//                item.getItemInfo().setUserId(player.getPlayerInfo().getUserId());
//                return true;
//            }
//            else
//            {
//                return false;
//            }
//        }
//        catch (Exception e)
//        {
//            logger.error("addItemTo error");
//        }
//        return false;
//    }
//
//    public GamePlayer getPlayer()
//    {
//        return player;
//    }
//
//    /**
//     * 从数据库中加载物品，到指定的格子。 目前物品加载已经不通过此方法
//     */
//    public void LoadFromDatabase()
//    {
//        // if (saveToDb)
//        // {
//        // PlayerBussiness pb = new PlayerBussiness();
//        // {
//        // BaseItem[] list =
//        // PlayerBussiness.GetUserBagByType(player.getUserId(),
//        // bagType);
//        //
//        // BeginChanges();
//        //
//        // try
//        // {
//        // for (BaseItem item : list)
//        // {
//        // AddItemTo(item, (byte) item.getItemInfo().getPlace());
//        // }
//        // }
//        // finally
//        // {
//        // CommitChanges();
//        // }
//        // }
//        // }
//    }
//
//    public void LoadItems(List<BaseItem> baseItems)
//    {
//        if (baseItems == null || baseItems.size() == 0)
//            return;
//
//        List<BaseItem> temp = new ArrayList<BaseItem>();
//
//        BeginChanges();
//        try
//        {
//            for (BaseItem item : baseItems)
//            {
//                if (item.getTemplate() == null)
//                {
//                    logger.error(item.getItemInfo().getItemId()
//                            + " Item's Template is null! Item's TemplateID:"
//                            + item.getItemInfo().getTemplateId());
//                    continue;
//                }
//                if (item.getItemInfo().getBagType() == bagType
//                        && item.getItemInfo().getCount() <= item.getTemplate().getMaxCount()
//                        && item.getItemInfo().getCount() > 0)
//                {
//                    if (!AddItemTo(item, (byte) item.getItemInfo().getPlace()))
//                    {
//                        temp.add(item);
//                    }
//                }
//            }
//
//            if (temp.size() > 0)
//            {
//                player.SendItemsToMail(temp, null, null, MailType.ITEMOVERDUE);
//                player.getOut().SendMailResponse(player.getPlayerInfo().getUserId(),
//                                                 MailRespose.RECEIVER);
//
//                for (BaseItem info : temp)
//                {
//                    info.getItemInfo().setPlace((short) -1);
//                    info.getItemInfo().setBagType((short) -1);
//                    PlayerBussiness.updateGoods(info.getItemInfo());
//                }
//
//            }
//
//        }
//        catch (Exception e)
//        {
//            logger.error("LoadItems error");
//        }
//        finally
//        {
//            CommitChanges();
//        }
//    }
//
//    public boolean RemoveItem(BaseItem item, int itemRemoveType)
//    {
//        if (super.RemoveItem(item, itemRemoveType))
//        {
//            item.getItemInfo().setIsExist(false);
//            item.getItemInfo().setRemoveType(itemRemoveType);
//
//            if (saveToDb)
//            {
//                synchronized (removedList)
//                {
//                    removedList.add(item);
//                }
//            }
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }
//
//    public void SaveToDatabase()
//    {
//        if (saveToDb)
//        {
//            List<ItemInfo> tempInfos = new ArrayList<ItemInfo>();
//            synchronized (lock)
//            {
//                for (int i = 0; i < items.length; i++)
//                {
//                    BaseItem item = items[i];
//                    if (item != null
//                            && item.getItemInfo().getOp() == Option.Update)
//                    {
//                        if (item.getItemInfo().getItemId() > 0)
//                        {
//                            item.getItemInfo().setOp(Option.Update);
//                        }
//                        else
//                        {
//                            item.getItemInfo().setOp(Option.Insert);
//                        }
//                        tempInfos.add(item.getItemInfo());
//                    }
//                }
//
//                ItemBussiness.getUpdateItem(player.getUserId(), tempInfos);
//            }
//
//            synchronized (removedList)
//            {
//                tempInfos.clear();
//                for (BaseItem item : removedList)
//                {
//                    if (item.getItemInfo().getItemId() > 0 && item.isDirty())
//                    {
//                        item.getItemInfo().setOp(Option.Update);
//                    }
//                    else
//                    {
//                        item.getItemInfo().setOp(Option.Insert);
//                    }
//                    tempInfos.add(item.getItemInfo());
//                }
//
//                ItemBussiness.getUpdateItem(player.getUserId(), tempInfos);
//                removedList.clear();
//            }
//        }
//    }
//
//    // / <summary>
//    // / 群发邮件
//    // / </summary>
//    public boolean SendAllItemsToMail(String sender, String title, int mailType)
//    {
//        boolean result = true;
//        boolean sentMail = false;
//
//        if (saveToDb)
//        {
//            BeginChanges();
//            try
//            {
//                synchronized (lock)
//                {
//                    List<BaseItem> items = GetItems();
//                    int count = items.size();
//
//                    if (count > 0)
//                        sentMail = true;
//
//                    result = player.SendItemsToMail(items,
//                                                    "Game.Server.GameUtils.CommonBag.Sender",
//                                                    title, mailType);
//                }
//            }
//            catch (Exception ex)
//            {
//                logger.error("Send Items Mail Error:", ex);
//            }
//            finally
//            {
//                SaveToDatabase();
//                CommitChanges();
//            }
//            if (sentMail)
//            {
//                player.getOut().SendMailResponse(player.getPlayerInfo().getUserId(),
//                                                 MailRespose.RECEIVER);
//            }
//        }
//        return result;
//    }
//
//    public void UpdateAllPlaces()
//    {
//
//        if (player.getOut() != null)
//        {
//            int[] changedPlaces = new int[capalility];
//            for (int i = 0; i < capalility; i++)
//            {
//                changedPlaces[i] = i;
//            }
//            player.getOut().SendUpdateInventorySlot(this, changedPlaces);
//        }
//
//    }
//
//    public void UpdateChangedPlaces()
//    {
//        if (player.getOut() != null)
//        {
//            // Integer[] places = null;
//            // synchronized (changedPlaces)
//            // {
//            // places = (Integer[]) changedPlaces.toArray();
//            // }
//            if (changedPlaces != null)
//            {
//                player.getOut().SendUpdateInventorySlot(this, changedPlaces);
//            }
//            super.UpdateChangedPlaces();
//        }
//
//    }
//
//    @Override
//    public boolean TakeOutItem(BaseItem item)
//    {
//        if (super.TakeOutItem(item))
//        {
//            if (saveToDb)
//            {
//                PlayerBussiness.updateGoods(item.getItemInfo());
//            }
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }

}
