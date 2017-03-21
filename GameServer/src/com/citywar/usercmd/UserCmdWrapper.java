/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd;

import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.citywar.game.GamePlayer;
import com.citywar.socket.Packet;

/**
 * 玩家指令包装器，用于包装需提交到线程池执行的玩家指令
 * 
 * @author sky
 * @date 2011-04-28
 * @version
 * 
 */
public class UserCmdWrapper implements Runnable
{
    private static Logger logger = Logger.getLogger(UserCmdWrapper.class.getName());

    private Session session;

    private AbstractUserCmd cmd;

    private Packet packet;

    public UserCmdWrapper(AbstractUserCmd cmd, Session session, Packet packet)
    {
        this.session = session;
        this.cmd = cmd;
        this.packet = packet;
    }

    /**
     * 线程池执行回调，执行线程并推动下一指令的执行
     */
    public final void run()
    {
        GamePlayer player = null;

        try
        {
            player = (GamePlayer) session.getUserProperties().get(GamePlayer.class.toString());

            if (player != null)
            {
                // session验证
                if (player.getSession() != null
                        && player.getSession() == session)
                    cmd.execute(player, packet);
                else
                    session.close();
            }
            else
            {
                cmd.excuteSession(session, packet);
            }
        }
        catch (Exception e)
        {
            logger.error("玩家指令执行异常:", e);
        }
        finally
        {
            if (player != null)
            {
                /* 移除已经执行的指令 */
                player.getCmdQueue().remove();
            }
        }
    }
}
