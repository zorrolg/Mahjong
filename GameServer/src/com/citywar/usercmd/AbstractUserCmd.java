/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd;

import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.citywar.GameServer;
import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;
import com.citywar.socket.WebCommand;

/**
 * 抽象的玩家命令类，所有玩家协议处理命令必须继承此类
 * 
 * @author sky
 * @date 2011-04-28
 * @version
 * 
 */
public abstract class AbstractUserCmd implements WebCommand
{
    protected static Logger logger = Logger.getLogger(AbstractUserCmd.class.getName());

    /**
     * 玩家协议处理接口，对玩家的指令进行分发
     * 
     * @see com.citywar.socket.Command#execute(com.citywar.dice.socket.ConnetionOwner,
     *      com.citywar.socket.Packet)
     */
    @Override
    public final void execute(Session session, Packet packet)
    {
    	
    	
        GamePlayer player = (GamePlayer) session.getUserProperties().get(GamePlayer.class.toString());
        UserCmdWrapper cmdWrapper = new UserCmdWrapper(this, session, packet);

        if (player == null)
        {
            /* 未登录玩家，直接提交到线程池执行 */
            GameServer.getServerCmdExecutor().execute(cmdWrapper);
        }
        else
        {
        	player.getOut().SetPingTime();
            /* 已登录玩家，提交到玩家指令执行队列 */
            player.getCmdQueue().add(cmdWrapper);
        }
    }

    /**
     * 玩家协议执行方法
     * 
     * @param player
     *            玩家对象
     * @param packet
     *            协议数据包
     * @return 执行结果
     */
    public abstract int execute(GamePlayer player, Packet packet);
    
    /**
     * 由于玩家发送登录协议时尚未创建GamePlayer对象，因此只能使用session进行处理
     * 因此次接口专为登录协议准备，其他协议不需要重载本接口
     * @param session
     * @param packet
     * @return
     */
    public int excuteSession(Session session, Packet packet)
    {
        return 0;
    }
}
