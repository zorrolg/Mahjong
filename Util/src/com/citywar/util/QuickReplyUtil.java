/**
*All rights reserved. This material is confidential and proprietary to CityWar
*/
package com.citywar.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 快捷回复工具类
 * @author charles
 * @date 2011-12-27
 * @version 
 *
 */
public class QuickReplyUtil
{
    private static List<String> quickReplys = new ArrayList<String>();
    
    public static void quickReplyInit(String path)
    {
        try
        {
            File folder = new File(path + Config.getValue("quickreply"));
            if(folder.exists() && folder.isDirectory())
            {
                File[] files = folder.listFiles();
                for(File f : files)
                {
                    if(f.isFile())
                    {
                        quickReplys = FileOperate.readLines(f.getPath(), "UTF-8");
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static List<String> getQuickReplyMessage()
    {
        return quickReplys;
    }
}
