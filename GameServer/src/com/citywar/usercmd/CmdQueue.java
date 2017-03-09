/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.usercmd;

import com.citywar.socket.Packet;

/**
 * 协议包队列接口
 * 
 * @author sky
 * @date 2011-04-28
 * @version
 * 
 */
public interface CmdQueue
{
    public void add(AbstractUserCmd cmd, Packet packet);

    public void remove();
}
