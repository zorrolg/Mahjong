/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Administrator
 * @date 2012-2-3
 * @version
 * 
 */
public class Test
{
    /**
     * 连接超时
     */
    private static int connectTimeOut = 5000;

    /**
     * 读取数据超时
     */
    private static int readTimeOut = 10000;

    public static void main(String[] args)
    {
        doPost("http://10.10.4.195:8080/DiceWeb/UserUploadImage", "utf-8");
        // try
        // {
        // testCopy();
        // }
        // catch (IOException e)
        // {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

    public static void testCopy() throws IOException
    {
        BufferedInputStream bis = null;
        DataOutputStream dos = null;
        try
        {
            bis = new BufferedInputStream(new FileInputStream(new File(
                    "E:/Iphone/DiceTrunk/Util/test.jpg")));
            dos = new DataOutputStream(new FileOutputStream(new File(
                    "E:/Iphone/DiceTrunk/Util/test2.jpg")));
            // dos.writeInt(1);
            byte[] bytes = new byte[1024];

            while (bis.read(bytes) != -1)
            {
                dos.write(bytes);
            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally
        {
            dos.flush();
            dos.close();
            bis.close();
        }

    }

    public static String doPost(String reqUrl, String recvEncoding)
    {
        HttpURLConnection url_con = null;
        String responseContent = "0";
        try
        {

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            System.setProperty("sun.net.client.defaultConnectTimeout",
                               String.valueOf(Test.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout",
                               String.valueOf(Test.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setDoOutput(true);

            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(new File(
                            "E:/Iphone/DiceTrunk/Util/test.jpg")));
            DataOutputStream dos = new DataOutputStream(
                    url_con.getOutputStream());
            dos.writeInt(10);
            byte[] bytes = new byte[1];

            while (bis.read(bytes) != -1)
            {
                dos.write(bytes);
            }
            dos.flush();
            dos.close();
            bis.close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            while (tempLine != null)
            {
                tempStr.append(tempLine);
                tempLine = rd.readLine();
            }
            responseContent = tempStr.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (url_con != null)
            {
                url_con.disconnect();
            }
        }
        return responseContent;
    }
}
