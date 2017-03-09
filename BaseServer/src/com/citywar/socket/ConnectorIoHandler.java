/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.socket;

import org.apache.mina.core.session.IoSession;

/**
 * 连接器专用IoHandler
 * @author sky
 * @date 2011-11-24
 * @version 
 *
 */
public class ConnectorIoHandler extends BaseIoHandler
{

    /**
     * @param owner
     */
    public ConnectorIoHandler(SessionOwner owner)
    {
        super(owner);
    }

    /**
     * 重载异常回调，因为Connector一般均是服务器间连接的抽象，不能简单做断线处理，仅日志记录
     */
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception
    {
        if (LOGGER.isErrorEnabled())
            LOGGER.error("BaseIoHandler caught exception:", cause);
    }
}
