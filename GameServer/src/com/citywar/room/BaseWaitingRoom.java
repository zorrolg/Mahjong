/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.room;

import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.citywar.gameobjects.GamePlayer;
import com.citywar.socket.Packet;

/**
 * 游戏大厅，未创建房间的玩家的临时住所
 * 
 * @author sky
 * @date 2011-05-04
 * @version
 * 
 */
public class BaseWaitingRoom
{
    private HashMap<Integer, GamePlayer> playerMap;

    private ReadWriteLock lock;

    public BaseWaitingRoom()
    {
        playerMap = new HashMap<Integer, GamePlayer>();
        lock = new ReentrantReadWriteLock(false);
    }

    /**
     * 玩家进入大厅
     * 
     * @param player
     *            加入的玩家对象
     * @return 成功返回true，否则返回false
     */
    public Boolean addPlayer(GamePlayer player)
    {
        Boolean result = false;

        lock.writeLock().lock();
        if (!playerMap.containsKey(player.getUserId()) || removePlayer(player))
        {
            playerMap.put(player.getUserId(), player);
            result = true;
        }
        lock.writeLock().unlock();

        if (result)
        {
            Packet pkg = player.getOut().sendSceneAddPlayer(player);
            sendToALL(pkg, player);
        }

        return result;
    }

    /**
     * 移除玩家
     * 
     * @param player
     *            玩家对象
     * @return 成功返回true，否则返回false
     */
    public Boolean removePlayer(GamePlayer player)
    {
        if (player == null)
            return false;

        lock.writeLock().lock();
        playerMap.remove(player.getUserId());
        lock.writeLock().unlock();

        return true;
    }

    /**
     * 广播消息给所有玩家
     * 
     * @param packet
     *            协议包
     */
    public void sendToALL(Packet packet)
    {
        sendToALL(packet, null);
    }

    /**
     * 广播消息给所有玩家，排除指定玩家
     * 
     * @param packet
     *            协议包
     * @param except
     *            被排除的玩家
     */
    public void sendToALL(Packet packet, GamePlayer except)
    {
        GamePlayer[] temp = null;

        lock.readLock().lock();
        temp = new GamePlayer[playerMap.size()];
        playerMap.values().toArray(temp);
        lock.readLock().unlock();

        if (temp != null)
        {
            for (GamePlayer p : temp)
            {
                if (p != null && p != except)
                {
                    p.getOut().sendTCP(packet);
                }
            }
        }
    }

    /**
     * 获取当前大厅中的所有玩家
     * 
     * @return 所有玩家对象
     */
    public GamePlayer[] getPlayersSafe()
    {
        GamePlayer[] temp = null;

        lock.readLock().lock();
        temp = new GamePlayer[playerMap.size()];
        playerMap.values().toArray(temp);
        lock.readLock().unlock();

        return temp == null ? new GamePlayer[0] : temp;
    }
}
