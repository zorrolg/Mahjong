/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.gameutil;

//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import com.citywar.dice.common.BaseItem;
//import com.citywar.dice.util.StackMessagePrint;

/**
 * @author dansen
 * @date 2011-4-23 @
 */
public abstract class AbstractInventory
{
//    private static Logger logger = Logger.getLogger(AbstractInventory.class.getName());
//
//    protected Object lock = new Object();
//
//    protected byte bagType;
//
//    protected int capalility;
//
//    private int beginSlot;
//
//    private boolean autoStack;
//
//    protected List<Integer> changedPlaces = new ArrayList<Integer>();
//
//    private int changeCount;
//
//    public AbstractInventory(int capability, byte type, int beginSlot,
//            Boolean autoStack)
//    {
//        items = new BaseItem[capability];
//        this.capalility = capability;
//        this.bagType = type;
//        this.beginSlot = beginSlot;
//        this.autoStack = autoStack;
//    }
//
//    public boolean AddCountToStack(BaseItem item, short count)
//    {
//        if (item == null)
//            return false;
//        if (count <= 0 || item.getItemInfo().getBagType() != bagType)
//            return false;
//
//        if (item.getItemInfo().getCount() + count > item.getTemplate().getMaxCount())
//            return false;
//
//        item.getItemInfo().setCount((short) (count + item.getItemInfo().getCount()));
//
//        OnPlaceChanged(item.getItemInfo().getPlace());
//
//        return true;
//    }
//
//    public boolean AddItem(BaseItem item)
//    {
//        boolean newitemFlag = false;
//
//        if (item.getItemInfo().getPlace() < 1
//                && item.getItemInfo().getUserId() == 0)
//        {
//            newitemFlag = true;
//        }
//
//        if (AddItem(item, getBeginSlot()))
//        {
//            if (newitemFlag)
//            {
//                OnPlayerObtainitem(item);
//            }
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }
//
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
//        if (item == null || place >= capalility || place < 0)
//            return false;
//
//        synchronized (lock)
//        {
//            if (items[place] != null)
//            {
//                place = -1;
//            }
//            else
//            {
//                items[place] = item;
//                item.getItemInfo().setPlace(place);
//                item.getItemInfo().setBagType(bagType);
//            }
//        }
//        if (place != -1)
//            OnPlaceChanged(place);
//
//        return place != -1;
//    }
//
//    public boolean AddTemplate(BaseItem cloneItem, int count)
//    {
//        return AddTemplate(cloneItem, count, beginSlot, capalility - 1);
//    }
//
//    public boolean AddTemplate(BaseItem cloneItem, int count, int minSlot,
//            int maxSlot)
//    {
//        if (cloneItem == null)
//            return false;
//
//        ItemTempInfo template = cloneItem.getTemplate();
//
//        if (template == null)
//            return false;
//        if (count <= 0)
//            return false;
//        if (minSlot < beginSlot || minSlot > capalility - 1)
//            return false;
//        if (maxSlot < beginSlot || maxSlot > capalility - 1)
//            return false;
//        if (minSlot > maxSlot)
//            return false;
//
//        synchronized (lock)
//        {
//            List<Integer> changedSlot = new LinkedList<Integer>();
//
//            int itemcount = count;
//
//            for (int i = minSlot; i <= maxSlot; i++)
//            {
//                BaseItem item = items[i];
//
//                if (item == null)
//                {
//                    itemcount -= template.getMaxCount();
//                    changedSlot.add(i);
//                }
//                else if (autoStack && cloneItem.canStackedTo(item))
//                {
//                    itemcount -= (template.getMaxCount() - item.getItemInfo().getCount());
//                    changedSlot.add(i);
//                }
//                if (itemcount <= 0)
//                    break;
//            }
//
//            if (itemcount <= 0)
//            {
//                BeginChanges();
//                try
//                {
//                    itemcount = count;
//                    for (int i : changedSlot)
//                    {
//                        BaseItem item = items[i];
//                        if (item == null)
//                        {
//                            item = (BaseItem) cloneItem.clone();
//
//                            item.getItemInfo().setCount((short) (itemcount < template.getMaxCount() ? itemcount
//                                                                : template.getMaxCount()));
//
//                            itemcount -= item.getItemInfo().getCount();
//
//                            AddItemTo(item, (byte) i);
//                        }
//                        else
//                        {
//                            if (item.getTemplate().getTemplateId() == template.getTemplateId())
//                            {
//                                ItemInfo itemInfo = item.getItemInfo();
//                                int add = (itemInfo.getCount() + itemcount < template.getMaxCount() ? itemcount
//                                        : template.getMaxCount()
//                                                - itemInfo.getCount());
//                                itemInfo.setCount((short) (itemInfo.getCount() + add));
//                                itemcount -= add;
//
//                                OnPlaceChanged(i);
//                            }
//                            else
//                            {
//                                logger.error("Add template erro: select slot's TemplateId not equest templateId");
//                            }
//                        }
//                    }
//
//                    if (itemcount != 0)
//                    {
//                        logger.error("Add template error: last count not equal Zero.");
//                    }
//                }
//                finally
//                {
//                    CommitChanges();
//                }
//
//                return true;
//            }
//            else
//            {
//                return false;
//            }
//        }
//    }
//
//    public synchronized void BeginChanges()
//    {
//        ++changeCount;
//    }
//
//    // 清除指定位置的物品
//    public void Clear(int minSlot, int maxSlot)
//    {
//        BeginChanges();
//        synchronized (lock)
//        {
//            for (int i = minSlot; i <= maxSlot; i++)
//            {
//                items[i] = null;
//                OnPlaceChanged(i);
//            }
//        }
//        CommitChanges();
//    }
//
//    public void ClearBag(int itemRemoveType)
//    {
//        BeginChanges();
//        synchronized (lock)
//        {
//            for (int i = beginSlot; i < capalility; i++)
//            {
//                if (items[i] != null)
//                {
//                    RemoveItem(items[i], bagType);
//                }
//            }
//        }
//
//        CommitChanges();
//    }
//
//    protected boolean CombineItems(int fromSlot, int toSlot)
//    {
//        if (fromSlot == toSlot)
//            return false;
//        return false;
//    }
//
//    public synchronized void CommitChanges()
//    {
//        int changes = EndChanges();
//
//        if (changes < 0)
//        {
//            if (logger.isInfoEnabled())
//                logger.error("Inventory changes counter is bellow zero (forgot to use BeginChanges?)!\n\n"
//                        + StackMessagePrint.captureStackTrace());
//
//            VolatileWrite(0);
//        }
//        if (changes <= 0 && changedPlaces.size() > 0)
//        {
//            UpdateChangedPlaces();
//        }
//    }
//
//    public synchronized int EndChanges()
//    {
//        --changeCount;
//        return changeCount;
//    }
//
//    protected boolean ExchangeItems(int fromSlot, int toSlot)
//    {
//        if (fromSlot == toSlot)
//            return false;
//        BaseItem fromItem = items[toSlot];
//        BaseItem toItem = items[fromSlot];
//
//        items[fromSlot] = fromItem;
//        items[toSlot] = toItem;
//
//        if (fromItem != null)
//            fromItem.getItemInfo().setPlace((byte) fromSlot);
//
//        if (toItem != null)
//            toItem.getItemInfo().setPlace((byte) toSlot);
//
//        return true;
//    }
//
//    public int FindFirstEmptySlot()
//    {
//        return FindFirstEmptySlot(beginSlot);
//    }
//
//    public byte FindFirstEmptySlot(int minSlot)
//    {
//        if (minSlot >= capalility)
//            return -1;
//
//        synchronized (lock)
//        {
//            for (int i = minSlot; i < capalility; i++)
//            {
//                if (items[i] == null)
//                {
//                    return (byte) i;
//                }
//            }
//            return -1;
//        }
//    }
//
//    // / 查找最后一个空位
//    public int FindLastEmptySlot()
//    {
//        synchronized (lock)
//        {
//            for (int i = capalility - 1; i >= 0; i--)
//            {
//                if (items[i] == null)
//                {
//                    return i;
//                }
//            }
//            return -1;
//        }
//    }
//
//    public int GetAllItemCount()
//    {
//        int count = 0;
//        synchronized (lock)
//        {
//            for (int i = beginSlot; i < capalility; i++)
//            {
//                if (items[i] != null)
//                {
//                    count += items[i].getItemInfo().getCount();
//                }
//            }
//        }
//        return count;
//    }
//
//    public int getBeginSlot()
//    {
//        return beginSlot;
//    }
//
//    public int getCapalility()
//    {
//        return capalility;
//    }
//
//    public int GetEmptyCount()
//    {
//        return GetEmptyCount(beginSlot);
//    }
//
//    public int GetEmptyCount(int minSlot)
//    {
//        if (minSlot < 0 || minSlot > capalility - 1)
//            return 0;
//
//        int count = 0;
//        synchronized (lock)
//        {
//            for (int i = minSlot; i < capalility; i++)
//            {
//                if (items[i] == null)
//                {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//
//    public BaseItem GetItemAt(int slot)
//    {
//        if (slot < 0 || slot >= capalility)
//            return null;
//
//        return items[slot];
//    }
//
//    public BaseItem GetItemByCategoryID(int minSlot, int categoryID,
//            int property)
//    {
//        synchronized (lock)
//        {
//            for (int i = minSlot; i < capalility; i++)
//            {
//                if (items[i] != null
//                        && items[i].getTemplate().getCategoryId() == categoryID)
//                {
//                    if (property != -1
//                            && items[i].getTemplate().getProperty1() != property)
//                        continue;
//                    return items[i];
//                }
//            }
//            return null;
//        }
//    }
//
//    public BaseItem GetItemByTemplateID(int minSlot, int templateId)
//    {
//        synchronized (lock)
//        {
//            for (int i = minSlot; i < capalility; i++)
//            {
//                if (items[i] != null
//                        && items[i].getTemplate().getTemplateId() == templateId)
//                {
//                    return items[i];
//                }
//            }
//            return null;
//        }
//    }
//
//    public int GetItemCount(boolean isBinds, int templateId)
//    {
//        return GetItemCount(0, isBinds, templateId);
//    }
//
//    public int GetItemCount(int templateId)
//    {
//        return GetItemCount(0, templateId);
//    }
//
//    // / 查找从Start开始的第一个空位
//    public int GetItemCount(int minSlot, boolean isBinds, int templateId)
//    {
//        int count = 0;
//        synchronized (lock)
//        {
//            for (int i = minSlot; i < capalility; i++)
//            {
//                if (items[i] != null
//                        && items[i].getTemplate().getTemplateId() == templateId
//                        && items[i].getItemInfo().getIsBinds() == isBinds)
//                {
//                    count += items[i].getItemInfo().getCount();
//                }
//            }
//        }
//        return count;
//    }
//
//    public int GetItemCount(int minSlot, int templateId)
//    {
//        int count = 0;
//        synchronized (lock)
//        {
//            for (int i = minSlot; i < capalility; i++)
//            {
//                if (items[i] != null
//                        && items[i].getTemplate().getTemplateId() == templateId)
//                {
//                    count += items[i].getItemInfo().getCount();
//                }
//            }
//        }
//        return count;
//    }
//
//    public int GetItemCountEx(boolean isBinds, int minTemplate, int maxTemplate)
//    {
//        return GetItemCountEx(0, isBinds, minTemplate, maxTemplate);
//    }
//
//    public int GetItemCountEx(int minSlot, boolean isBinds, int minTemplateId,
//            int maxTemplateId)
//    {
//        int count = 0;
//        synchronized (lock)
//        {
//            for (int i = minSlot; i < capalility; i++)
//            {
//                BaseItem item = items[i];
//                if (item != null && item.getItemInfo().getIsBinds() == isBinds
//                        && item.getTemplate().getTemplateId() >= minTemplateId
//                        && item.getTemplate().getTemplateId() <= maxTemplateId)
//                {
//                    count += item.getItemInfo().getCount();
//                }
//            }
//        }
//        return count;
//    }
//
//    public int GetItemCountEx(int minTemplateId, int maxTemplateId)
//    {
//        return GetItemCountEx(0, minTemplateId, maxTemplateId);
//    }
//
//    public int GetItemCountEx(int minSlot, int minTemplateId, int maxTemplateId)
//    {
//        int count = 0;
//        synchronized (lock)
//        {
//            for (int i = minSlot; i < capalility; i++)
//            {
//                BaseItem item = items[i];
//                if (item != null
//                        && item.getTemplate().getTemplateId() >= minTemplateId
//                        && item.getTemplate().getTemplateId() <= maxTemplateId)
//                {
//                    count += items[i].getItemInfo().getCount();
//                }
//            }
//        }
//        return count;
//    }
//
//    public int GetItemMaxStrengthenLevel(boolean isBinds)
//    {
//        return GetItemMaxStrengthenLevel(0, isBinds);
//    }
//
//    public int GetItemMaxStrengthenLevel(int minSlot, boolean isBinds)
//    {
//        int max = 0;
//        synchronized (lock)
//        {
//            for (int i = minSlot; i < capalility; i++)
//            {
//                if (items[i] != null
//                        && items[i].getItemInfo().getStrengthenLevel() > 0
//                        && items[i].getItemInfo().getIsBinds() == isBinds)
//                {
//                    if (max < items[i].getItemInfo().getStrengthenLevel())
//                    {
//                        max = items[i].getItemInfo().getStrengthenLevel();
//                    }
//                }
//            }
//        }
//        return max;
//    }
//
//    public List<BaseItem> GetItems()
//    {
//        return GetItems(0, capalility - 1);
//    }
//
//    public List<BaseItem> GetItems(int minSlot, int maxSlot)
//    {
//        List<BaseItem> list = new LinkedList<BaseItem>();
//
//        synchronized (lock)
//        {
//            for (int i = minSlot; i <= maxSlot; i++)
//            {
//                if (items[i] != null)
//                {
//                    list.add(items[i]);
//                }
//            }
//        }
//        return list;
//    }
//
//    public HashMap<Integer, BaseItem> GetRawSpaces(
//            HashMap<Integer, Integer> counts)
//    {
//        HashMap<Integer, BaseItem> dics = new HashMap<Integer, BaseItem>();
//
//        synchronized (lock)
//        {
//            for (int i = 0; i < items.length; i++)
//            {
//                if (items[i] != null)
//                {
//                    dics.put(i, items[i]);
//                    counts.put(i, (int) (items[i].getItemInfo().getCount()));
//                }
//            }
//        }
//        return dics;
//    }
//
//    public int getType()
//    {
//        return bagType;
//    }
//
//    public boolean IsEmpty(int slot)
//    {
//        if (slot >= 0 && slot < capalility)
//            return items[slot] == null;
//        else
//            return true;
//    }
//
//    public boolean IsSolt(int slot)
//    {
//        return slot >= 0 && slot < capalility;
//    }
//
//    public boolean MoveItem(int fromSlot, int toSlot, int count)
//    {
//        if (fromSlot < 0 || toSlot < 0 || fromSlot >= capalility
//                || toSlot >= capalility || count < 0)
//            return false;
//        if (fromSlot == toSlot)
//            return false;
//
//        boolean result = false;
//        synchronized (lock)
//        {
//            if (!CombineItems(fromSlot, toSlot)
//                    && !StackItems(fromSlot, toSlot, count))
//            {
//                result = ExchangeItems(fromSlot, toSlot);
//            }
//            else
//            {
//                result = true;
//            }
//        }
//
//        if (result)
//        {
//            BeginChanges();
//            try
//            {
//                OnPlaceChanged(fromSlot);
//                OnPlaceChanged(toSlot);
//            }
//            finally
//            {
//                CommitChanges();
//            }
//        }
//
//        return result;
//    }
//
//    protected void OnPlaceChanged(int place)
//    {
//        synchronized (changedPlaces)
//        {
//            if (changedPlaces.contains(place) == false)
//                changedPlaces.add(place);
//        }
//
//        if (changeCount <= 0 && changedPlaces.size() > 0)
//        {
//            UpdateChangedPlaces();
//        }
//    }
//
//    public void OnPlayerObtainitem(BaseItem item)
//    {
//        /*
//         * if (PlayerGottenItem != null) { PlayerGottenItem(item); }
//         */
//    }
//
//    /*
//     * 
//     */
//    public boolean RemoveCountFromStack(BaseItem item, int count,
//            int itemRemoveType)
//    {
//        if (item == null)
//            return false;
//
//        ItemInfo itemInfo = item.getItemInfo();
//
//        if (count <= 0 || itemInfo.getBagType() != bagType)
//            return false;
//        if (itemInfo.getCount() < count)
//            return false;
//        if (itemInfo.getCount() == count && RemoveItem(item, bagType))
//            itemInfo.setCount((short) 0);
//        else
//            itemInfo.setCount((short) (itemInfo.getCount() - count));
//
//        OnPlaceChanged(itemInfo.getPlace());
//
//        return true;
//    }
//
//    public boolean RemoveItem(BaseItem item, int itemRemoveType)
//    {
//        if (item == null)
//            return false;
//        int place = -1;
//        synchronized (lock)
//        {
//            for (int i = 0; i < capalility; i++)
//            {
//                if (items[i] == item)
//                {
//                    place = i;
//                    items[i] = null;
//
//                    break;
//                }
//            }
//        }
//
//        if (place != -1)
//        {
//            OnPlaceChanged(place);
//            if (item.getItemInfo().getBagType() == this.bagType
//                    && item.getItemInfo().getPlace() == place)
//            {
//                item.getItemInfo().setPlace((byte) -1);
//                item.getItemInfo().setBagType((byte) -1);
//            }
//        }
//
//        return place != -1;
//    }
//
//    public boolean RemoveItemAt(int place, int itemRemoveType)
//    {
//        return RemoveItem(GetItemAt(place), itemRemoveType);
//    }
//
//    public boolean RemoveTemplate(int templateId, int count,
//            short itemRemoveType)
//    {
//        return RemoveTemplate(templateId, count, 0, capalility - 1,
//                              itemRemoveType);
//    }
//
//    public boolean RemoveTemplate(int templateId, int count, int minSlot,
//            int maxSlot, short itemRemoveType)
//    {
//        if (count <= 0)
//            return false;
//        if (minSlot < 0 || minSlot > capalility - 1)
//            return false;
//        if (maxSlot <= 0 || maxSlot > capalility - 1)
//            return false;
//        if (minSlot > maxSlot)
//            return false;
//
//        synchronized (lock)
//        {
//            List<Integer> changedSlot = new LinkedList<Integer>();
//
//            int itemcount = count;
//
//            for (int i = minSlot; i <= maxSlot; i++)
//            {
//                BaseItem item = items[i];
//                if (item != null
//                        && item.getTemplate().getTemplateId() == templateId)
//                {
//                    changedSlot.add(i);
//                    itemcount -= item.getItemInfo().getCount();
//                    if (itemcount <= 0)
//                        break;
//                }
//            }
//
//            if (itemcount <= 0)
//            {
//                BeginChanges();
//                itemcount = count;
//
//                try
//                {
//                    for (int i : changedSlot)
//                    {
//                        BaseItem item = items[i];
//                        ItemInfo itemInfo = item.getItemInfo();
//
//                        if (item != null
//                                && item.getTemplate().getTemplateId() == templateId)
//                        {
//                            if (item.getItemInfo().getCount() <= itemcount)
//                            {
//                                RemoveItem(item, bagType);
//                                itemcount -= item.getItemInfo().getCount();
//                            }
//                            else
//                            {
//                                int dec = itemInfo.getCount() - itemcount < itemInfo.getCount() ? itemcount
//                                        : 0;
//                                itemInfo.setCount((short) (itemInfo.getCount() - dec));
//                                itemcount -= dec;
//                                OnPlaceChanged(i);
//                            }
//                        }
//                    }
//
//                    if (itemcount != 0)
//                    {
//                        logger.error("Remove template error:last item cout not equal Zero.");
//                    }
//                }
//                finally
//                {
//                    CommitChanges();
//                }
//
//                return true;
//            }
//            else
//            {
//                return false;
//            }
//        }
//    }
//
//    public void setCapalility(int value)
//    {
//        capalility = value < 0 ? 0 : (value > items.length ? items.length
//                : value);
//    }
//
//    public boolean StackAllItems()
//    {
//        synchronized (lock)
//        {
//            try
//            {
//                for (int i = beginSlot; i <= capalility - 1; i++)
//                {
//                    if (items[i] != null)
//                    {
//                        for (int j = i; j <= capalility - 1; j++)
//                        {
//                            if (items[j] != null
//                                    && items[i] != null
//                                    && items[i] != items[j]
//                                    && items[j].canStackedTo(items[i])
//                                    && items[i].getItemInfo().getCount()
//                                            + items[j].getItemInfo().getCount() <= items[j].getTemplate().getMaxCount())
//                            {
//                                items[i].getItemInfo().setCount((short) (items[i].getItemInfo().getCount() + items[j].getItemInfo().getCount()));
//                                RemoveItem(items[j], ItemRemoveType.STACK);
//                                UpdateItem(items[i]);
//                            }
//
//                        }
//                        for (int place = beginSlot; place < items[i].getItemInfo().getPlace(); place++)
//                        {
//                            if (items[place] == null
//                                    && items[place] != items[i])
//                            {
//                                ExchangeItems(i, place);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//            catch (Exception e)
//            {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    protected boolean StackItems(int fromSlot, int toSlot, int itemCount)
//    {
//        if (fromSlot == toSlot)
//            return false;
//        BaseItem fromItem = items[fromSlot];
//        BaseItem toItem = items[toSlot];
//        if (fromItem == null)
//            return false;
//        if (itemCount < 0)
//            return false;
//
//        if (itemCount == 0)
//        {
//            if (fromItem.getItemInfo().getCount() > 0)
//                itemCount = fromItem.getItemInfo().getCount();
//            else
//                itemCount = 1;
//        }
//
//        if (fromItem.getItemInfo().getCount() < itemCount)
//            return false;
//
//        if (toItem != null
//                && toItem.getTemplate().getTemplateId() == fromItem.getTemplate().getTemplateId()
//                && toItem.canStackedTo(fromItem))
//        {
//            if (itemCount + toItem.getItemInfo().getCount() > fromItem.getTemplate().getMaxCount())
//            {
//                fromItem.getItemInfo().setCount((short) (fromItem.getItemInfo().getCount()
//                                                        - toItem.getTemplate().getMaxCount() - toItem.getItemInfo().getCount()));
//                toItem.getItemInfo().setCount((short) (toItem.getTemplate().getMaxCount()));
//            }
//            else
//            {
//                toItem.getItemInfo().setCount((short) (toItem.getItemInfo().getCount() + itemCount));
//
//                if (itemCount == fromItem.getItemInfo().getCount())
//                {
//                    RemoveItem(fromItem, ItemRemoveType.STACK);
//                }
//                else
//                {
//                    fromItem.getItemInfo().setCount((short) (fromItem.getItemInfo().getCount() - itemCount));
//                    UpdateItem(fromItem);
//                }
//            }
//            return true;
//        }
//        else if (toItem == null
//                && fromItem.getItemInfo().getCount() > itemCount)
//        {
//            BaseItem newItem = (BaseItem) fromItem.clone();
//            ItemInfo newInfo = newItem.getItemInfo();
//            newInfo.setCount((short) itemCount);
//
//            if (AddItemTo(newItem, (byte) toSlot))
//            {
//                fromItem.getItemInfo().setCount((short) (fromItem.getItemInfo().getCount() - itemCount));
//                return true;
//            }
//            else
//            {
//                return false;
//            }
//        }
//        return false;
//    }
//
//    public boolean StackItemToAnother(BaseItem item)
//    {
//        synchronized (lock)
//        {
//            ItemInfo itemInfo = item.getItemInfo();
//
//            for (int i = capalility - 1; i >= 0; i--)
//            {
//                if (item != null
//                        && items[i] != null
//                        && items[i] != item
//                        && item.canStackedTo(items[i])
//                        && items[i].getItemInfo().getCount()
//                                + item.getItemInfo().getCount() <= item.getTemplate().getMaxCount())
//                {
//                    items[i].getItemInfo().setCount((short) (items[i].getItemInfo().getCount() + itemInfo.getCount()));
//                    itemInfo.setIsExist(false);
//                    itemInfo.setRemoveType(ItemRemoveType.STACK);
//
//                    if (itemInfo.getPlace() < 1 && itemInfo.getUserId() == 0)
//                    {
//                        OnPlayerObtainitem(item);
//                    }
//                    UpdateItem(items[i]);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public boolean TakeOutItem(BaseItem item)
//    {
//        if (item == null)
//            return false;
//        int place = -1;
//        synchronized (lock)
//        {
//            for (int i = 0; i < capalility; i++)
//            {
//                if (items[i] == item)
//                {
//                    place = i;
//                    items[i] = null;
//
//                    break;
//                }
//            }
//        }
//
//        if (place != -1)
//        {
//            OnPlaceChanged(place);
//            if (item.getItemInfo().getBagType() == this.bagType)
//            {
//                item.getItemInfo().setPlace((byte) -1);
//                item.getItemInfo().setBagType((byte) -1);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean TakeOutItemAt(byte place)
//    {
//        return TakeOutItem(GetItemAt(place));
//    }
//
//    public void UpdateChangedPlaces()
//    {
//        synchronized (changedPlaces)
//        {
//            changedPlaces.clear();
//        }
//    }
//
//    public void UpdateItem(BaseItem item)
//    {
//        if (item.getBagType().getValue() == bagType)
//        {
//            if (item.getItemInfo().getCount() <= 0)
//                RemoveItem(item, ItemRemoveType.OTHER);
//            else
//            {
//                OnPlaceChanged(item.getItemInfo().getPlace());
//            }
//        }
//    }
//
//    // 使用物品
//    public void UseItem(BaseItem item)
//    {
//        boolean changed = false;
//        if (item.getItemInfo().getIsBinds() == false
//                && (item.getTemplate().getBindType() == 2 || item.getTemplate().getBindType() == 3))
//        {
//            item.getItemInfo().setIsBinds(true);
//            changed = true;
//        }
//
//        if (item.getItemInfo().getIsUsed() == false)
//        {
//            item.getItemInfo().setIsUsed(true);
//            item.getItemInfo().setBeginDate(new Date());
//            changed = true;
//        }
//
//        if (changed)
//        {
//            OnPlaceChanged(item.getItemInfo().getPlace());
//        }
//    }
//
//    // ## public delegate void PlayerObtainItemHandle(BaseItem item);
//
//    // ## public event PlayerObtainItemHandle PlayerGottenItem;
//
//    private synchronized void VolatileWrite(int val)
//    {
//        changeCount = val;
//    }

 }
