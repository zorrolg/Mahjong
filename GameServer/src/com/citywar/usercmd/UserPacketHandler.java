package com.citywar.usercmd;

import java.util.Set;

import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.citywar.socket.BasePacketHandler;
import com.citywar.socket.Packet;
import com.citywar.socket.WebCommand;
import com.citywar.util.ClassUtil;

public class UserPacketHandler extends BasePacketHandler
{
    private static final Logger logger = Logger.getLogger(UserPacketHandler.class.getName());

    public UserPacketHandler()
    {
        super();

        // 动态注册具体的协议处理指令，所有处理玩家消息的指令必须
        // 使用UserCmdAnnotation进行标记，否则将无法被识别

        // Package pack = GameServer.class.getPackage();
        Package pack = Package.getPackage("com.citywar.usercmd");
        Set<Class<?>> allClasses = ClassUtil.getClasses(pack);

        for (Class<?> class1 : allClasses)
        {
            UserCmdAnnotation annotation = class1.getAnnotation(UserCmdAnnotation.class);

            if (annotation != null)
            {
                try
                {
                	webCommands.put(annotation.code(),
                                 (WebCommand) class1.newInstance());
                }
                catch (InstantiationException e)
                {
                    logger.error("[ UserPacketHandler : UserPacketHandler ]", e);
                }
                catch (IllegalAccessException e)
                {
                    logger.error("[ UserPacketHandler : UserPacketHandler ]", e);
                }
            }
        }
    }

    public String getHandlerName()
    {
        return "User packet handler";
    }
    
    
    public final void handleWebPacket(Session session, Packet packet)
    {
        int code = packet.getCode();
        
        if (!webCommands.containsKey(code))
        {
            System.err.println(getHandlerName() + "没有找到对应的Command code : "
                    + code + "(0x" + Integer.toHexString(code) + ")");
            return;
        }
        webCommands.get(code).execute(session, packet);
    }
}
