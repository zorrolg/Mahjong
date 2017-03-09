/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.citywar.manager.ManagerClient;
import com.citywar.util.ClassUtil;

/**
 * @author Dream
 * @date 2011-9-14
 * @version
 * 
 */
public class ConsoleCommandMgr
{
    private static HashMap<String, AbstractCommandHandle> commandMap = new HashMap<String, AbstractCommandHandle>();

    private static final Logger logger = Logger.getLogger(ConsoleCommandMgr.class.getName());

    /**
     * 处理命令
     * 
     * @param cmd
     */
    public static boolean handleCommand(ManagerClient server, String cmdStr)
    {
        List<String> splitStrings = parseCmdLine(cmdStr);
        AbstractCommandHandle cmd = getCommand(splitStrings.get(0));
        if (cmd == null)
            return false;

        return cmd.onCommand(server, splitStrings);
    }

    /**
     * @param cmdStr
     * @return
     */
    private static List<String> parseCmdLine(String cmdStr)
    {
        if (cmdStr == null || cmdStr.isEmpty())
            return null;

        List<String> args = new ArrayList<String>();
        int state = 0;
        StringBuilder arg = new StringBuilder(cmdStr.length() >> 1);

        for (int i = 0; i < cmdStr.length(); i++)
        {
            char c = cmdStr.charAt(i);
            switch (state)
            {
                case 0:
                    if (c == ' ')
                        continue;
                    arg.setLength(0);
                    if (c == '"')
                        state = 2;
                    else
                    {
                        state = 1;
                        i--;
                    }
                    break;
                case 1:
                    if (c == ' ')
                    {
                        args.add(arg.toString());
                        state = 0;
                    }
                    arg.append(c);
                    break;
                case 2:
                    if (c == '"')
                    {
                        args.add(arg.toString());
                        state = 0;
                    }
                    arg.append(c);
                    break;
            }
        }
        if (state != 0)
            args.add(arg.toString());

        return args;
    }

    /**
     * 取得命令字符串对应的命令
     * 
     * @param cmdStr
     * @return
     */
    public static AbstractCommandHandle getCommand(String cmdStr)
    {
        AbstractCommandHandle cmd = commandMap.get(cmdStr);

        if (cmd != null)
            return cmd;

        for (String str : commandMap.keySet())
        {
            // 忽略大小写 并且 支持简写
            if (str.equalsIgnoreCase(cmdStr)
                    || str.toLowerCase().startsWith(cmdStr.toLowerCase()))
                return commandMap.get(str);
        }
        return null;
    }

    /**
     * 载入命令
     */
    public static boolean loadCommand()
    {
        commandMap.clear();
        Package pack = Package.getPackage("com.citywar.commands");
        Set<Class<?>> allClasses = ClassUtil.getClasses(pack);
        for (Class<?> class1 : allClasses)
        {
            ConsoleCmdAnnotation annotation = class1.getAnnotation(ConsoleCmdAnnotation.class);

            if (annotation != null)
            {
                try
                {
                    AbstractCommandHandle handle = (AbstractCommandHandle) class1.newInstance();
                    handle.setCmd(annotation.cmdString());
                    handle.setDesc(annotation.description());
                    handle.setLevel(annotation.level());
                    handle.setUsage(annotation.usage());
                    commandMap.put(annotation.cmdString(), handle);
                }
                catch (InstantiationException e)
                {
                    logger.error("[ConsoleCommandMgr:loadCommand]", e);
                    return false;
                }
                catch (IllegalAccessException e)
                {
                    logger.error("[ConsoleCommandMgr:loadCommand] ", e);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 显示语法
     * 
     * @param client
     */
    public static void displaySyntax(ManagerClient client)
    {
        client.displayMessage("Commands list:");
        for (String str : getCommandList(PrivLevel.Admin, true))
        {
            client.displayMessage(" " + str);
        }
    }

    public static List<String> getCommandList(int level, boolean addDesc)
    {
        Collection<AbstractCommandHandle> iter = commandMap.values();

        List<String> list = new ArrayList<String>();
        for (AbstractCommandHandle cmd : iter)
        {
            String cmdString = cmd.getCmd();
            if (cmd == null || cmdString == null)
            {
                continue;
            }

            if (cmdString.charAt(0) == '&')
                cmdString = '/' + cmdString.substring(1, cmdString.length());
            if (level >= cmd.getLevel())
            {
                if (addDesc)
                {
                    list.add(cmdString + " - " + cmd.getDesc());
                }
                else
                {
                    list.add(cmd.getCmd());
                }
            }
        }

        return list;
    }

}
