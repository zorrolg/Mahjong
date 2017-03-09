/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.commands.admin;

import java.util.List;

import com.citywar.GameServer;
import com.citywar.commands.AbstractCommandHandle;
import com.citywar.commands.ConsoleCmdAnnotation;
import com.citywar.commands.ConsoleCommandMgr;
import com.citywar.commands.PrivLevel;
import com.citywar.manager.BuildMgr;
import com.citywar.manager.CardMgr;
import com.citywar.manager.HallMgr;
import com.citywar.manager.ItemMgr;
import com.citywar.manager.LevelMgr;
import com.citywar.manager.ManagerClient;
import com.citywar.manager.RobotMgr;
import com.citywar.manager.ShopMgr;
import com.citywar.manager.TaskMgr;
import com.citywar.util.Config;

/**
 * @author tracy
 * @date 2012-01-09
 * @version
 * 
 */
@ConsoleCmdAnnotation(cmdString = "&load", description = "   相关文件重新加载.", level = PrivLevel.Player, usage = {
        "       /load /config     :加载配置文件.",
        "       /load /shop       :加载商城文件.", 
        "       /load /item       :加载模板.",
        "       /load /property   :刷新服务器配置.",
        "       /load /fusion     :加载熔炼相关数据",
        "       /load /award      :加载日常奖励数据",
        "       /load /propitem   :加载道具相关配置" })
public class ReloadCommand extends AbstractCommandHandle
{

    @Override
    public boolean onCommand(ManagerClient client, List<String> args)
    {
        if (args.size() > 1)
        {
            StringBuilder success = new StringBuilder();
            StringBuilder failed = new StringBuilder();
            if (args.contains("/cmd"))
            {
                ConsoleCommandMgr.loadCommand();
                displayMessage(client, "Command load success!");
                success.append("/cmd,");
            }

            if (args.contains("/config"))
            {
                Config.refreshProperties();
                displayMessage(client, "Application config file load success!");
                success.append("/config,");
            }

            if (args.contains("/property"))
            {
                GameServer.getInstance().refreshGameProperties();
                displayMessage(client, "Game properties load success!");
                success.append("/property,");
            }

            if (args.contains("/item"))
            {
                if (ItemMgr.reload())// && WorldMgr.reloadShopItem())
                {
                    displayMessage(client, "Items load success!");
                    success.append("/item,");
                }
                else
                {
                    displayMessage(client, "Items load failed!");
                    failed.append("/item,");
                }
            }
            if (args.contains("/shop"))
            {
                if (ShopMgr.reload())
                {
                    displayMessage(client, "Shops load success!");
                    success.append("/shop,");
                }
                else
                {
                    displayMessage(client, "Shops load failed!");
                    failed.append("/shop,");
                }
            }
            if (args.contains("/card"))
            {
                if (CardMgr.reload())
                {
                    displayMessage(client, "Cards load success!");
                    success.append("/card,");
                }
                else
                {
                    displayMessage(client, "Cards load failed!");
                    failed.append("/card,");
                }
            }
            if (args.contains("/build"))
            {
                if (BuildMgr.reload())
                {
                    displayMessage(client, "Builds load success!");
                    success.append("/build,");
                }
                else
                {
                    displayMessage(client, "Build load failed!");
                    failed.append("/build,");
                }
            }
            if (args.contains("/hall"))
            {
                if (HallMgr.reload())
                {
                    displayMessage(client, "Halls load success!");
                    success.append("/hall,");
                }
                else
                {
                    displayMessage(client, "Halls load failed!");
                    failed.append("/hall,");
                }
            }
            if (args.contains("/level"))
            {
                if (LevelMgr.reload())
                {
                    displayMessage(client, "Levels load success!");
                    success.append("/level,");
                }
                else
                {
                    displayMessage(client, "Halls load failed!");
                    failed.append("/level,");
                }
            }
            if (args.contains("/task"))
            {
                if (TaskMgr.reload())
                {
                    displayMessage(client, "Tasks load success!");
                    success.append("/task,");
                }
                else
                {
                    displayMessage(client, "Tasks load failed!");
                    failed.append("/task,");
                }
            }
            if (args.contains("/robot"))
            {
                if (RobotMgr.reload())
                {
                    displayMessage(client, "Robots load success!");
                    success.append("/robot,");
                }
                else
                {
                    displayMessage(client, "Robots load failed!");
                    failed.append("/robot,");
                }
            }

            
            
            
            
            
            if (success.length() == 0 && failed.length() == 0)
            {
                displayMessage(client, "Nothing executed!");
                displaySyntax(client);
            }
            else
            {
                displayMessage(client,
                               "Success Options:    " + success.toString());

                if (failed.length() > 0)
                {
                    displayMessage(client,
                                   "Faile Options:      " + failed.toString());
                    return false;
                }
            }
            return true;
        }
        else
        {
            displaySyntax(client);
        }
        return true;
    }

}
