package com.example.pc.sluicecontrol.common.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/8/31.
 */
public class VerifyUtil {


    /***
     * 验证编码
     * @param originStr 要编码的原字符串
     * @param devId		设备id
     * @return
     */
    public static String Verify_EFast365(String originStr, String devId)
    {

        StringBuffer strBuf = new StringBuffer(devId);
        if(originStr != null)
        {
            strBuf.append(originStr);
        }
        strBuf.append("BSMC3");
        String md5Str = null;
        try {
            md5Str = MD5Utils.getMD5String(strBuf.toString().toUpperCase()).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuffer result = new StringBuffer();

        for(int i = 0; i < devId.length(); ++i)
        {
            String indexStr = devId.substring(i, i+1);
            int index = i + 3;
            try
            {
                index = Integer.parseInt(indexStr);
            }
            catch(Exception e)
            {
            }
            if(index + i < 30)
            {
                index += i;
            }
            String str = md5Str.substring(index, index+1);
            result.append(str);
        }

//		System.out.println("\n devId = " + devId + " \nmd5Str = " + md5Str + "\nresult = " + result + "\noriginStr = " + originStr);

        return result.toString();
    }

}
