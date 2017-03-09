/**
 *All rights reserved. This material is confidential and proprietary to CityWar
 */
package com.citywar.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.log4j.Logger;

/**
 * @author Dream
 * @date 2011-5-12
 * @version
 * 
 */
public class CompressUtil
{
    private static final Logger log = Logger.getLogger(CompressUtil.class.getName());

    /**
     * 压缩数据
     * 
     * @param str
     * @return
     */
    public static byte[] compress(String str)
    {
        byte[] input;
        try
        {
            byte[] output = null;
            input = str.getBytes("UTF-8");

            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.finish();
            ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
            byte[] buf = new byte[1024 * 10];
            while (!compresser.finished())
            {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();

            try
            {
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            compresser.end();
            return output;
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("数据压缩错误:" + StackMessagePrint.printErrorTrace(e));
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] compress(byte[] src, int start, int len)
    {
        byte[] input = new byte[len];
        System.arraycopy(src, start, input, 0, len);

        byte[] output = null;

        Deflater compresser = new Deflater();
        compresser.setInput(input);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        byte[] buf = new byte[1024 * 10];
        while (!compresser.finished())
        {
            int i = compresser.deflate(buf);
            bos.write(buf, 0, i);
        }
        output = bos.toByteArray();

        try
        {
            bos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        compresser.end();
        return output;
    }

    /**
     * 创建压缩XML文件
     * 
     * @param path
     *            文件路径
     * @param str
     *            xml字符串
     * @param fileName
     *            文件名称
     * @param isCompress
     *            是否压缩
     * @param message
     *            是否成功消息
     * @return
     */
    public static String createCompressXml(String path, String str,
            String fileName, boolean isCompress, String message)
    {
        String filePath = path + fileName + ".xml";
        try
        {
            File myFilePath = new File(filePath);

            myFilePath.delete();
            myFilePath.createNewFile();

            FileOutputStream writer = new FileOutputStream(myFilePath);
            writer.write(isCompress ? compress(str) : str.getBytes());
            writer.close();

            return "Build:" + filePath + "," + message;
        }
        catch (IOException e)
        {
            log.error("CreateCompressXml " + filePath + " is fail!", e);
            return "Build:" + filePath + ",Fail!";
        }
    }

    /**
     * 解压缩数据
     * 
     * @param compressed
     * @return
     */
    public static String decompress(byte[] compressed)
    {
        try
        {
            Inflater decompresser = new Inflater();
            decompresser.setInput(compressed, 0, compressed.length);
            byte[] result = new byte[1024];
            int resultLength;
            resultLength = decompresser.inflate(result);
            decompresser.end();
            return new String(result, 0, resultLength, "UTF-8");
        }
        catch (DataFormatException e)
        {
            log.error("数据解压缩错误:" + StackMessagePrint.printErrorTrace(e));
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("数据解压缩错误:" + StackMessagePrint.printErrorTrace(e));
            e.printStackTrace();
        }

        return null;
    }

    // public static void main(String[] t)
    // {
    // // String ss = decompress(compress("<xml>test</xml>"));
    // // //System.out.println(ss);
    //
    // Document result = DocumentHelper.createDocument();
    // Element element = result.addElement("Result");
    // element.addAttribute("test","dsssd");
    // element.addAttribute("ids","12df3");
    // createCompressXml("D:\\",result,"test",false,"success");
    //
    // FileInputStream fis;
    // try
    // {
    // fis = new FileInputStream("D:\\test.xml");
    // byte[] b1=new byte[fis.available()];
    // fis.read(b1);
    // String dString=decompress(b1);
    // //System.out.println(dString);
    // }
    // catch (FileNotFoundException e)
    // {
    // e.printStackTrace();
    // }
    // catch (IOException e)
    // {
    // e.printStackTrace();
    // }
    // }
}
