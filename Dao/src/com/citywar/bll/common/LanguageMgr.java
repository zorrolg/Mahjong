package com.citywar.bll.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

/**
 * 
 * @author Dream
 * @date 2011-5-3
 * @version
 * 
 */
public class LanguageMgr
{
    private static Logger logger = Logger.getLogger(LanguageMgr.class.getName());

    private static Hashtable<String, String> allMsgs = new Hashtable<String, String>();

    private static ReadWriteLock locker = new ReentrantReadWriteLock(false);

    /**
     * 初始化所有提示信息
     * 
     * @param path
     *            提示信息配置文件路径
     * @return 执行结果
     */
    public static boolean setup(String path)
    {
        return reload(path);
    }

    /**
     * 重新加载提示信息
     * 
     * @param path
     *            提示信息配置文件路径
     * @return 执行结果
     */
    public static boolean reload(String path)
    {
        try
        {
            Hashtable<String, String> temp = loadLanguage(path);

            if (temp.size() > 0)
            {
                locker.writeLock().lock();
                allMsgs = temp;
                locker.writeLock().unlock();
                return true;
            }
        }
        catch (Exception ex)
        {
            logger.error("Load language file error:", ex);
        }
        return false;
    }

    /**
     * 载人提示信息内容
     * 
     * @param path
     *            文件路径
     * @return 执行结果
     */
    private static Hashtable<String, String> loadLanguage(String path)
    {
        Hashtable<String, String> list = new Hashtable<String, String>();

        try
        {
            File file = new File(path);
            if (!file.exists())
            {
                logger.error("Language file : " + path + " not found !");
            }
            else
            {
            	
            	FileInputStream fileReader = new FileInputStream(file); 
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileReader,"utf-8"));
                String line = null;
                

                while ((line = reader.readLine()) != null)
                {
                	
                    if (line.startsWith("#"))
                        continue;

                    if (line.indexOf(':') == -1)
                        continue;

                    // 不能用split分隔，字段中可能有两个 ":"
                    String key = line.substring(0, line.indexOf(':'));
                    String value = line.substring(line.indexOf(':') + 1,
                                                  line.length());
                    list.put(key, value);

                    // String[] splitted = line.split("\\:");
                    // splitted[1] = splitted[1].replace("\t", "");
                    // list.put(splitted[0], splitted[1].trim());
                }

                reader.close();
                fileReader.close();
            }
        }
        catch (Exception ex)
        {
            logger.error("fail to load language file:", ex);
        }

        return list;
    }

    /**
     * 获取指定提示信息的内容
     * 
     * @param translateId
     *            索引id
     * @param args
     *            内容参数
     * @return 提示信息，支持格式化
     */
    public static String getTranslation(String translateId, Object... args)
    {
        if (allMsgs.containsKey(translateId))
        {

            String translated = (String) allMsgs.get(translateId);

            try
            {
                translated = String.format(translated, args);
            }
            catch (Exception ex)
            {
                // 这个异常别注释掉
                logger.error("[ LanguageMgr : getTranslation ]", ex);
            }
            return translated == null ? translateId : translated;
        }
        else
        {
            return translateId;
        }
    }

    /**
     * 获取字符串的长度 unicode编码在0~255之间算长度1，其他范围算长度2
     * 
     * @param str
     *            输入字符串
     * @return 字符串实际长度
     */
    public static int getStringLength(String str)
    {
        int length = 0;
        for (int i = 0; i < str.length(); ++i)
        {
            int code = str.charAt(i);
            if (code >= 0 && code <= 255)
                ++length;
            else
                length += 2;
        }
        return length;
    }
}
