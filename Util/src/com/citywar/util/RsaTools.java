package com.citywar.util;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Encoder;

public class RsaTools
{
    public static void main(String[] args) throws Exception
    {
        test1();

        // 定义变量
        KeyPair keyPair = RSAUtil.generateKeyPair();
        RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();
        byte[] inputData = "测试数据".getBytes();

        // 获取PublicKey、PrivateKey参数
        String pubModString = Base64.encodeBase64String(pubKey.getModulus().toByteArray());
        String pubPubExpString = Base64.encodeBase64String(pubKey.getPublicExponent().toByteArray());
        //System.out.println("PublicKey Modulus:" + pubModString);
        //System.out.println("publicExponent:" + pubPubExpString);

        String priModString = Base64.encodeBase64String(priKey.getModulus().toByteArray());
        String priPriExpString = Base64.encodeBase64String(priKey.getPrivateExponent().toByteArray());
        //System.out.println("PrivateKey Modulus:" + priModString);
        //System.out.println("privateExponent:" + priPriExpString);

        byte[] pubModBytesNew = Base64.decodeBase64(pubModString);
        byte[] pubPubExpBytesNew = Base64.decodeBase64(pubPubExpString);
        byte[] priModBytesNew = Base64.decodeBase64(priModString);
        byte[] priPriExpBytesNew = Base64.decodeBase64(priPriExpString);
        RSAPublicKey recoveryPubKey = RSAUtil.generateRSAPublicKey(pubModBytesNew,
                                                                   pubPubExpBytesNew);
        RSAPrivateKey recoveryPriKey = RSAUtil.generateRSAPrivateKey(priModBytesNew,
                                                                     priPriExpBytesNew);

        // 用Public加密 用PrivateKey解密
        byte[] raw = RSAUtil.encrypt(recoveryPubKey, inputData);
        BASE64Encoder encoder = new BASE64Encoder();
        String rawString = encoder.encode(raw);
        //System.out.println("加密后的数据：" + rawString);

        byte[] enRaw = Base64.decodeBase64(rawString.getBytes());
        byte[] data = RSAUtil.decrypt(recoveryPriKey, enRaw);
        System.out.println("解密后的数据:" + new String(data));
        /*
         * PublicKey
         * Modulus:AIuIGBlr0q59NL8q7SfAslO948LmrgmbdPt3GwqzfByx9AWMPMN+
         * cSsXr4pKnYc412MGhBxF40x/BEJuGljEEnDQeFngVqacDIHMisbhgTL/+z8j+
         * cCnwYDy9j2e23e7VPT27i5rRdamiqD29dbFI2TohH8zoea713lvouktBWNJ
         * publicExponent:AQAB
         * 
         * PrivateKey
         * Modulus:AIuIGBlr0q59NL8q7SfAslO948LmrgmbdPt3GwqzfByx9AWMPMN
         * +cSsXr4pKnYc412MGhBxF40x/BEJuGljEEnDQeFngVqacDIHMisbhgTL/+z8j+
         * cCnwYDy9j2e23e7VPT27i5rRdamiqD29dbFI2TohH8zoea713lvouktBWNJ
         * privateExponent
         * :UaJbLyxVQ/8/CAWOYO6tpq73fYVHF5nbVHB34sfl+kfnFAPbXSkx/ttJuag0B
         * /Qql+YtDvmYpZ2+mTsfsQVuH69/BBnClwMwgBuHzy+niSAmZXQKb0aWjlg1SWb+3
         * ABQpauPlVVMGQLTrhwXSg34fg6BAfca/osJ7HAX9TCmIQE=
         * 
         * 加密后的数据：
         * hZtbXFnO4LBmwsMrCiGoqjq0kVcRMhFSDsMDEaNI9OdiyqJr5cGLtCY9W7zIKFbA7qXFBfSiycLLrcDiya3is
         * +lAnRTImGqNXwxmeiLS3THrmZbbmxQwfs00Sv+kqfW2B84V80lsKsv/
         * CSQPFdwHPLASDLzzerJIcEHjgK7CTxM= 解密后的数据:测试数据
         */
    }

    private static void test1() throws Exception
    {
        String pubModString = "AIV7F1kqcDFzmMJRcBDxUXBoqLaCu6/qpGM7j02uG3CT5FGSf9Dduw2mViAGwWl1fMrdacO2d6dcMp42ob05Y2Le1zvPu57XE2Awi5UXDL9/zRodMBQ9Lb4VSxUoJA9jH6EdmdSj/2Cb9S1/3mw7PvMqZ5241p3mSeFrSveQVpTt";
        String pubPubExpString = "AQAB";
        byte[] pubModBytesNew = Base64.decodeBase64(pubModString);
        byte[] pubPubExpBytesNew = Base64.decodeBase64(pubPubExpString);

        RSAPublicKey recoveryPubKey = RSAUtil.generateRSAPublicKey(pubModBytesNew,
                                                                   pubPubExpBytesNew);
        // 用Public加密 用PrivateKey解密
        byte[] raw = RSAUtil.encrypt(recoveryPubKey, "user,sdf,2345".getBytes());
        BASE64Encoder encoder = new BASE64Encoder();
        String rawString = encoder.encode(raw);
        //System.out.println("加密后的数据：" + rawString);

        String priModString = "AIV7F1kqcDFzmMJRcBDxUXBoqLaCu6/qpGM7j02uG3CT5FGSf9Dduw2mViAGwWl1fMrdacO2d6dcMp42ob05Y2Le1zvPu57XE2Awi5UXDL9/zRodMBQ9Lb4VSxUoJA9jH6EdmdSj/2Cb9S1/3mw7PvMqZ5241p3mSeFrSveQVpTt";
        String priPriExpString = "JDmluoMz85fFq2binRaA3Zrpvq54NWPevTeayqAnvkPEwQabPZoPe8LauNJchzxlY8D3RTAuBPOWoS1+c5YxjGz/bsHj9MZ5VTYULLe9Hb94JnvMC5QBGkdJ288FHAiaARjs/avzBptL7LniP44A0j1Q+hzP1Aq/yjBv5yJaO4E=";
        byte[] enRaw = Base64.decodeBase64(rawString.getBytes("utf-8"));
        byte[] priModBytesNew = Base64.decodeBase64(priModString);
        byte[] priPriExpBytesNew = Base64.decodeBase64(priPriExpString);
        RSAPrivateKey recoveryPriKey = RSAUtil.generateRSAPrivateKey(priModBytesNew,
                                                                     priPriExpBytesNew);
        byte[] data = RSAUtil.decrypt(recoveryPriKey, enRaw);

        System.out.println("解密后的数据:"
                + new String(data).substring(7, new String(data).length()));

        // String priModString =
        // "zRSdzFcnZjOCxDMkWUbuRgiOZIQlk7frZMhElQ0a7VqZI9VgU3+lwo0ghZLU3Gg63kOY2UyJ5vFpQdwJUQydsF337ZAUJz4rwGRt/MNL70wm71nGfmdPv4ING+DyJ3ZxFawwE1zSMjMOqQtY4IV8his/HlgXuUfIHVDK87nMNLc=";
        // String priPriExpString = "AQAB";
        // byte[] enRaw =
        // Base64.decodeBase64("uzO/WsEUILxQGrYJT39eO1ahJsnfbrtB5fj01bT9+0Y0VZiEnpItpitmKJx1OhF9+j2ynZjgMaWREUhiTKiF54NDLZ4L/uWP61ytYrPokTty9XojWmiE+XQq6OYyvVnPLNV2HPsT1/o+OafB4vIDFIeCzzt1Rg+OHJhQgMgT4uk=".getBytes());
        // byte[] priModBytesNew = Base64.decodeBase64(priModString);
        // byte[] priPriExpBytesNew = Base64.decodeBase64(priPriExpString);
        // RSAPrivateKey privateKey = RSAUtil.generateRSAPrivateKey(
        // priModBytesNew, priPriExpBytesNew);
        // byte[] data = RSAUtil.decrypt(privateKey, enRaw);
        // String str = new String(data);
    }
}
