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
@ConsoleCmdAnnotation(cmdString = "&?", description = "/?     List all commands", level = PrivLevel.Admin)
public class ListAllCommand extends AbstractCommandHandle
{
    @Override
    public boolean onCommand(ManagerClient client, List<String> args)
    {
        ConsoleCommandMgr.displaySyntax(client);
        return true;
    }
    
}
