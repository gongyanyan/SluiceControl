package com.example.pc.sluicecontrol.common.utils;

import android.util.Log;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by shanrong.ai on 2018/1/25.
 */

public class SignUtil {

    public static String signTopRequest(Map<String, String> params, String secret) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();

        query.append(secret);

        for (String key : keys) {
            String value = params.get(key);
            if (!Tools.isNull(value)) {
                query.append(key).append(value);
            }
        }

        // 第三步：使用MD5

        query.append(secret);
//        byte[] bytes = encryptMD5(query.toString());
         Log.i("123","签名的字符串"+query.toString());


        // 第四步：把二进制转化为大写的十六进制(正确签名应该为32大写字符串，此方法需要时使用)
       // return byte2hex(bytes);
        return MD5Utils.toMD5(query.toString());
    }



    public static byte[] encryptMD5(String data) throws IOException {
        return data.getBytes("UTF-8");
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
}
