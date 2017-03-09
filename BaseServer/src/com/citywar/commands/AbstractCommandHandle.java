/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.commands;

import java.util.List;

import com.citywar.manager.ManagerClient;

/**
 * @author Dream
 * @date 2011-9-14
 * @version
 * 
 */
public abstract class AbstractCommandHandle
{
    private String[] usage;
    private String cmd;
    private int level;
    private String desc;

    public abstract boolean onCommand(ManagerClient client, List<String> args);

    public void displayMessage(ManagerClient client, String message)
    {
        if (client != null)
        {
            client.displayMessage(message);
        }
    }

    public void displaySyntax(ManagerClient client)
    {
        if (client != null)
        {
            client.displayMessage(getDesc());
            for (String str : getUsage())
                client.displayMessage(str);
        }
    }

    public String[] getUsage()
    {
        return usage;
    }

    public void setUsage(String[] usage)
    {
        this.usage = usage;
    }

    public String getCmd()
    {
        return cmd;
    }

    public void setCmd(String cmd)
    {
        this.cmd = cmd;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
