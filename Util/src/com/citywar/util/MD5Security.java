package com.citywar.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Security
{
    private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static String bytesToHex(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer();
        int t;
        for (int i = 0; i < 16; i++)
        {
            t = bytes[i];
            if (t < 0)
                t += 256;
            sb.append(hexDigits[(t >>> 4)]);
            sb.append(hexDigits[(t % 16)]);
        }
        return sb.toString();
    }

    public static String md5(String input) throws Exception
    {
        return code(input, 32);
    }

    public static String code(String input, int bit) throws Exception
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm",
                                                                            "MD5"));
            if (bit == 16)
                return bytesToHex(md.digest(input.getBytes("utf-8"))).substring(8,
                                                                                24);
            return bytesToHex(md.digest(input.getBytes("utf-8")));
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            throw new Exception("Could not found MD5 algorithm.", e);
        }
    }

    public static String md5_3(String b) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance(System.getProperty("MD5.algorithm",
                                                                        "MD5"));
        byte[] a = md.digest(b.getBytes());
        a = md.digest(a);
        a = md.digest(a);

        return bytesToHex(a);
    }

    public static void main(String[] tes)
    {
        // dream123|dream123|1236319807954|e33cef81927de69c05b8553eaf567e82
        try
        {
            String ss = md5("dream123!QY-16-WAN-0668-2555555-citywar-dice-love777");
            System.out.println(ss);
            
            //System.out.println(md5_3(ss));

        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
