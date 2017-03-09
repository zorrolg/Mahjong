/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.socket;

import org.apache.mina.core.session.IoSession;

/**
 * Session属主通用接口，用于传递session事件
 * 
 * @author sky
 * @date 2011-9-22
 * @version
 * 
 */
public interface SessionOwner
{
    public void sessionOpened(IoSession session);

    public void sessionClosed(IoSession session);

    public void messageReceived(IoSession session, Packet packet);
}
