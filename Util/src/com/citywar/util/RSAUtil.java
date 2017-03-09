package com.citywar.util;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Encoder;

public class RSAUtil
{
    /**
     * * 生成密钥对 *
     * 
     * @return KeyPair *
     * @throws EncryptException
     */
    public static KeyPair generateKeyPair() throws Exception
    {
        try
        {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",
                                                                       new BouncyCastleProvider());
            // KEY_SIZE关系到块加密的大小

            final int KEY_SIZE = 1024;
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            return keyPair;
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    // 生成公钥
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus,
            byte[] publicExponent) throws Exception
    {
        KeyFactory keyFac = null;
        try
        {
            keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        }
        catch (NoSuchAlgorithmException ex)
        {
            throw new Exception(ex.getMessage());
        }

        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try
        {
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
        }
        catch (InvalidKeySpecException ex)
        {
            throw new Exception(ex.getMessage());
        }
    }

    // 生成私钥
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus,
            byte[] privateExponent) throws Exception
    {
        KeyFactory keyFac = null;
        try
        {
            keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        }
        catch (NoSuchAlgorithmException ex)
        {
            throw new Exception(ex.getMessage());
        }

        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(
                modulus), new BigInteger(privateExponent));
        try
        {
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        }
        catch (InvalidKeySpecException ex)
        {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Encrypt String.
     * 
     * @return byte[]
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] obj)
    {
        if (publicKey != null)
        {
            try
            {
                Cipher cipher = Cipher.getInstance("RSA");
                // ENCRYPT_MODE : 用于将 cipher 初始化为加密模式的常量。
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(obj);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static String encrypt(String pubModString,String pubPubExpString,String str)
    {
        try
        {
            byte[] pubModBytesNew = Base64.decodeBase64(pubModString);
            byte[] pubPubExpBytesNew = Base64.decodeBase64(pubPubExpString);
            RSAPublicKey recoveryPubKey;
            recoveryPubKey = RSAUtil.generateRSAPublicKey(pubModBytesNew,pubPubExpBytesNew);
            byte[] raw=encrypt(recoveryPubKey, str.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(raw);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Basic decrypt method
     * 
     * @return byte[]
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] obj)
    {
        if (privateKey != null)
        {
            try
            {
                Cipher cipher = Cipher.getInstance("RSA");
                // DECRYPT_MODE : 用于将 cipher 初始化为解密模式的常量。
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(obj);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static byte[] decrypt(String priModBytesNew,
            String priPriExpBytesNew, byte[] clipherString) throws Exception
    {
        byte[] byteModBytesNew = Base64.decodeBase64(priModBytesNew);
        byte[] bytePriExpBytesNew = Base64.decodeBase64(priPriExpBytesNew);
        RSAPrivateKey recoveryPriKey = generateRSAPrivateKey(byteModBytesNew,
                                                             bytePriExpBytesNew);
        return RSAUtil.decrypt(recoveryPriKey, clipherString);
    }
    
    public static String decrypt(String priModBytesNew,String priPriExpBytesNew,String str)
    {
        try
        {
            byte[] enRaw = Base64.decodeBase64(str.getBytes());
            byte[] data = RSAUtil.decrypt(priModBytesNew, priPriExpBytesNew,enRaw);
            return new String(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        };
        return null;
    }
    
    public static void main(String[] args)
    {
        KeyPair keyPair;
        try
        {
            keyPair = generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            System.out.print(privateKey);
            //System.out.print(keyPair.getPublic());
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
