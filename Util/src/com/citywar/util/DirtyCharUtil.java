/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Dream
 * @date 2011-5-5
 * @version
 * 
 */
public class DirtyCharUtil
{
    private static List<String> ContentList = new ArrayList<String>();
    private static String[] SQLKEYWORD = ";|and|1=1|exec|insert|select|delete|update|like|count|chr|mid|master|or|truncate|char|declare|join".split("\\|");
    private static Logger log = Logger.getLogger(DirtyCharUtil.class.getName());

    public static boolean illegalcharacterInit(String path)
    {
        File file = new File(path);
        try
        {
            if (!file.exists())
            {
                log.error("DirtyChar file : " + path + " not found !");
                return false;
            }
            if (file.isFile())
                ContentList = FileOperate.readLines(file.getPath(), "UTF-8");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean checkIllegalChar(String strRegName)
    {
        boolean flag = false;

        if (!strRegName.isEmpty())
        {
            flag = checkChar(strRegName);
        }

        return flag;
    }

    private static boolean checkChar(String strRegName)
    {
        boolean flag = false;
        for (String strLine : ContentList)
        {
            // 校验非法字符
            if (!strLine.startsWith("GM"))
            {
                for (char charl : strLine.toCharArray())
                {
                    if (strRegName.contains(String.valueOf(charl)))
                    {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                {
                    break;
                }
            }
            else
            {
                // 校验非法词组
                String[] keyword = strLine.split("\\|");
                for (String key : keyword)
                {
                    if (strRegName.contains(key) && !key.isEmpty() && key != "")
                    {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                {
                    break;
                }
            }
        }

        return flag;
    }

    public static String convertSql(String inputString)
    {
        // if (!inputString.isEmpty())
        if (!CommonUtil.isNullOrEmpty(inputString))
        {
            inputString = inputString.trim().toLowerCase();
            inputString = inputString.replace("'", "''");
            inputString = inputString.replace(";--", "");
            inputString = inputString.replace("=", "");
            inputString = inputString.replace(" or", "");
            inputString = inputString.replace(" or ", "");
            inputString = inputString.replace(" and", "");
            inputString = inputString.replace("and ", "");
            if (!sqlChar(inputString))
            {
                inputString = "";
            }
        }
        return inputString;
    }

    /**
     * 和方法convertSql(String inputString)作用一致，只是不做toLowCase操作
     * 
     * @param inputString
     * @return
     */
    public static String convertSqlNotToLowCase(String inputString)
    {
        // if (!inputString.isEmpty())
        if (!CommonUtil.isNullOrEmpty(inputString))
        {
            inputString = inputString.replace("'", "''");
            inputString = inputString.replace(";--", "");
            inputString = inputString.replace("=", "");
            inputString = inputString.replace(" or", "");
            inputString = inputString.replace(" or ", "");
            inputString = inputString.replace(" and", "");
            inputString = inputString.replace("and ", "");
            if (!sqlChar(inputString))
            {
                inputString = "";
            }
        }
        return inputString;
    }

    public static boolean sqlChar(String v)
    {
        if (!v.trim().equals(""))
        {
            for (String a : SQLKEYWORD)
            {
                if (v.indexOf(a + " ") > -1 || v.indexOf(" " + a) > -1)
                {
                    return false;
                }
            }
        }
        return true;
    }
}
