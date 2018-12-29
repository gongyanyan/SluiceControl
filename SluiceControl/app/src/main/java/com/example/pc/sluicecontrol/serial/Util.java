package com.example.pc.sluicecontrol.serial;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;

public class Util {

    private static String hexStr =  "0123456789ABCDEF";
    private static String[] binaryArray =
            {"0000","0001","0010","0011",
                    "0100","0101","0110","0111",
                    "1000","1001","1010","1011",
                    "1100","1101","1110","1111"};

    /**
     *
     * @param
     * @return 二进制数组转换为二进制字符串   2-2
     */
    public static String bytes2BinStr(byte[] bArray){

        String outStr = "";
        int pos = 0;
        for(byte b:bArray){
            //高四位
            pos = (b&0xF0)>>4;
            outStr+=binaryArray[pos];
            //低四位
            pos=b&0x0F;
            outStr+=binaryArray[pos];
        }
        return outStr;
    }

    /**
     *
     * @param bytes
     * @return 将二进制数组转换为十六进制字符串  2-16
     */
    public static String bin2HexStr(byte[] bytes){

        String result = "";
        String hex = "";
        for(int i=0;i<bytes.length;i++){
            //字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
            result +=hex;  //+" "
        }
        return result;
    }

    /**
     *
     * @param hexString
     * @return 将十六进制转换为二进制字节数组   16-2
     */
    public static byte[] hexStr2BinArr(String hexString){
        //hexString的长度对2取整，作为bytes的长度
        int len = hexString.length()/2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位
        for(int i=0;i<len;i++){
            //右移四位得到高位
            high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);
            low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));
            bytes[i] = (byte) (high|low);//高地位做或运算
        }
        return bytes;
    }

    /**
     *
     * @param hexString
     * @return 将十六进制转换为二进制字符串   16-2
     */
    public static String hexStr2BinStr(String hexString){
        return bytes2BinStr(hexStr2BinArr(hexString));
    }

    /**
     * 二进制转16进制
     * @param bString
     * @return
     */

    public static String hexString2binaryString(String bString) {

        bString = bString.replace(" ", "");//去掉直接从word表格内复制出来的空格
        bString = bString.replace(" ", "");//去掉英文空格
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp = new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4)  {

            iTmp = 0;
            for (int j = 0; j < 4; j++)  {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));

        }
        return "0x"+tmp.toString();   //要不要加上0x??
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStrToByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArrToHexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
     */
    private static byte[] hexStrToByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * byte数组转换为二进制字符串,每个字节以","隔开
     **/
    public static String byteArrToBinStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            result.append(Long.toString(b[i] & 0xff, 2) + ",");
        }
        return result.toString().substring(0, result.length() - 1);
    }

    /**
     * 二进制字符串转换为byte数组,每个字节以","隔开
     **/
    public static byte[] binStrToByteArr(String binStr) {
        String[] temp = binStr.split(",");
        byte[] b = new byte[temp.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = Long.valueOf(temp[i], 2).byteValue();
        }
        return b;
    }

    /**
     * 二进制字符串转换为byte
     **/
    public static byte binStrToByte(String binStr) {

        return Integer.valueOf(binStr, 2).byteValue();
    }


    /**
     * byte数组转换为普通字符串
     **/
    public static String byteArrToStr(byte[] b) throws UnsupportedEncodingException {
        String str = new String(b, "UTF-8");
        return str;
    }

    //普通字符串转换为byte数组: byte[] b = str.getBytes("UTF-8")}


    /**
     * 数据体转换(高位、低位)
     **/
    public static byte[] byteStrToBytes(byte bb) {

        String ertemp = Integer.toBinaryString((bb & 0xFF) + 0x100).substring(1);   //得到8位二进制数
        String dategaowei = "1000"+ertemp.substring(0,4);   //数据高位
        String datediwei = "1000"+ertemp.substring(4,8);   //数据低位


        byte[] b = new byte[2];

        b[0] = Util.binStrToByte(dategaowei);
        b[1] = Util.binStrToByte(datediwei);

        return b;
    }



    /**
     * 数据体转换(高位、低位)
     **/
    public static byte[] byteStrToBytes( byte[] str) {

        byte[] destArray = new byte[2*str.length];
        int destLen = 0;
        for (byte bb : str) {

            String ertemp = Integer.toBinaryString((bb & 0xFF) + 0x100).substring(1);   //得到8位二进制数
            String dategaowei = "1000"+ertemp.substring(0,4);   //数据高位
            String datediwei = "1000"+ertemp.substring(4,8);   //数据低位

            byte[] b = new byte[2];
            b[0] = Util.binStrToByte(dategaowei);
            b[1] = Util.binStrToByte(datediwei);

            System.arraycopy(b, 0, destArray, destLen, b.length);
            destLen += b.length;
        }

        return destArray;
    }




    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }

        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }
        return destArray;
    }

    //分割字符串，  1234   1 2 3 4
    public static String[] splitMothd(String test){

        String[] arr = new String[test.length()];

        for(int i = 0; i < test.length(); i++){
            arr[i] = test.substring(i, i+1);

        }
        return arr;
    }


    /***
     * 10进制转16进制
     * @param n
     * @return
     */
    private static String intToHex(int n) {
        StringBuffer s = new StringBuffer();
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            s = s.append(b[n%16]);
            n = n/16;
        }
        a = s.reverse().toString();
        return a;
    }


    /**
     * @Description:	十进制转换成二进制 ()
     * @param decimalSource
     * @return String
     */
    public static String decimalToBinary(int decimalSource) {
        BigInteger bi = new BigInteger(String.valueOf(decimalSource));	//转换成BigInteger类型
        return bi.toString(2);	//参数2指定的是转化成X进制，默认10进制
    }

    /**
     * @Description:	二进制转换成十进制
     * @param binarySource
     * @return int
     */
    public static int binaryToDecimal(String binarySource) {
        BigInteger bi = new BigInteger(binarySource, 2);	//转换为BigInteger类型
        return Integer.parseInt(bi.toString());		//转换成十进制
    }


}
