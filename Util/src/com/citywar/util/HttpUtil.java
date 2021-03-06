package com.citywar.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil
{

    /**
     * 连接超时
     */
    private static int connectTimeOut = 5000;

    /**
     * 读取数据超时
     */
    private static int readTimeOut = 10000;

    /**
     * 请求编码
     */
    private static String requestEncoding = "UTF-8";

    /**
     * @return 连接超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
     */
    public static int getConnectTimeOut()
    {
        return HttpUtil.connectTimeOut;
    }

    /**
     * @return 读取数据超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
     */
    public static int getReadTimeOut()
    {
        return HttpUtil.readTimeOut;
    }

    /**
     * @return 请求编码
     * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
     */
    public static String getRequestEncoding()
    {
        return requestEncoding;
    }

    /**
     * @param connectTimeOut
     *            连接超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#connectTimeOut
     */
    public static void setConnectTimeOut(int connectTimeOut)
    {
        HttpUtil.connectTimeOut = connectTimeOut;
    }

    /**
     * @param readTimeOut
     *            读取数据超时(毫秒)
     * @see com.hengpeng.common.web.HttpRequestProxy#readTimeOut
     */
    public static void setReadTimeOut(int readTimeOut)
    {
        HttpUtil.readTimeOut = readTimeOut;
    }

    /**
     * @param requestEncoding
     *            请求编码
     * @see com.hengpeng.common.web.HttpRequestProxy#requestEncoding
     */
    public static void setRequestEncoding(String requestEncoding)
    {
        HttpUtil.requestEncoding = requestEncoding;
    }

    /**
     * <pre>
     * 发送带参数的GET的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @param parameters
     *            参数映射表
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, Map<?, ?> parameters, String recvEncoding)
    {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try
        {
            StringBuffer params = new StringBuffer();
            for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
            {
                Entry<?, ?> element = (Entry<?, ?>) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), HttpUtil.requestEncoding));
                params.append("&");
            }

            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpUtil.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpUtil.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            // url_con.setConnectTimeout(5000);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            // url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            // String crlf = System.getProperty("line.separator");
            while (tempLine != null)
            {
                temp.append(tempLine);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            //System.out.println("当前请求地址错误：" + e.toString());
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

    /**
     * <pre>
     * 发送不带参数的GET的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, String recvEncoding)
    {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try
        {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");

            if (paramIndex > 0)
            {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1, reqUrl.length());
                String[] paramArray = parameters.split("&");
                for (int i = 0; i < paramArray.length; i++)
                {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0)
                    {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1, string.length());
                        params.append(parameter);
                        params.append("=");
                        params.append(URLEncoder.encode(value, HttpUtil.requestEncoding));
                        params.append("&");
                    }
                }

                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpUtil.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpUtil.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            // url_con.setConnectTimeout(5000);//（单位：毫秒）jdk
            // 1.5换成这个,连接超时
            // url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null)
            {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        }
        catch (IOException e)
        {
            //System.out.println(e.getMessage());
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

    /**
     * <pre>
     * 下载文件信息到指定路径
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, String recvEncoding, String path)
    {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try
        {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");

            if (paramIndex > 0)
            {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1, reqUrl.length());
                String[] paramArray = parameters.split("&");
                for (int i = 0; i < paramArray.length; i++)
                {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0)
                    {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1, string.length());
                        params.append(parameter);
                        params.append("=");
                        params.append(URLEncoder.encode(value, HttpUtil.requestEncoding));
                        params.append("&");
                    }
                }

                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpUtil.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpUtil.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            byte[] result = new byte[1024];
            in.read(result);

            File myFilePath = new File(path);
            myFilePath.delete();
            myFilePath.createNewFile();

            FileOutputStream writer = new FileOutputStream(myFilePath);
            writer.write(result);
            writer.close();
            in.close();
        }
        catch (IOException e)
        {
            //System.out.println(e.getMessage());
        }
        catch (Exception e)
        {
            //System.out.println(e.getMessage());
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

    /**
     * <pre>
     * 发送带参数的POST的HTTP请求
     * </pre>
     * 
     * @param reqUrl
     *            HTTP请求URL
     * @param parameters
     *            参数映射表
     * @return HTTP响应的字符串
     */
    public static String doPost(String reqUrl, Map<String, String> parameters, String recvEncoding)
    {
        HttpURLConnection url_con = null;
        String responseContent = "0";
        try
        {
            StringBuffer params = new StringBuffer();
            if (parameters != null)
            {
                for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();)
                {
                    Entry<?, ?> element = (Entry<?, ?>) iter.next();
                    params.append(element.getKey().toString());
                    params.append("=");
                    /**
                     * 防止空指针异常.
                     */
                    params.append(URLEncoder.encode(element.getValue() == null ? "" : element.getValue().toString(), HttpUtil.requestEncoding));
                    params.append("&");
                }
            }

            if (params.length() > 0)
            {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpUtil.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpUtil.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
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

    /**
     * 通过HTTP下载文档，保存
     * 
     * @param xmlUrl
     * @param path
     */
    public static void getUrlXmlFile(String xmlUrl, String path)
    {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[1024 * 10];
        int size = 0;
        try
        {
            url = new URL(xmlUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            // 连接指定的资源
            httpUrl.connect();
            // 获取网络输入流
            InputStream i=httpUrl.getInputStream();
            bis = new BufferedInputStream(i);
            // 建立文件
            fos = new FileOutputStream(path);

            // 保存文件
            while ((size = bis.read(buf)) != -1)
            {
                fos.write(buf, 0, size);
            }
            fos.close();
            bis.close();
            httpUrl.disconnect();
 
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String a[])
    {
        HttpUtil.getUrlXmlFile("http://10.10.4.121:728/CelebByConsortiaWeekRiches_Out.xml", "C:\\aa.xml");
    }
}
