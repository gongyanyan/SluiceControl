package com.example.pc.sluicecontrol.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/8/31.
 */
public class MD5Utils {

    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    protected   static   char  hexDigits[] = {  '0' ,  '1' ,  '2' ,  '3' ,  '4' ,  '5' ,  '6' ,
            '7' ,  '8' ,  '9' ,  'a' ,  'b' ,  'c' ,  'd' ,  'e' ,  'f'  };

    protected   static MessageDigest messagedigest =  null ;
    static  {
        try  {
            messagedigest = MessageDigest.getInstance("MD5" );
        } catch  (NoSuchAlgorithmException nsaex) {
            System.err.println(MD5Utils.class .getName()
                    + "初始化失败，MessageDigest不支持MD5Util。" );
            nsaex.printStackTrace();
        }
    }

    /**
     * 生成字符串的md5校验值
     *
     * @param s
     * @return
     */
    public   static String getMD5String(String s) throws UnsupportedEncodingException {
        return  getMD5String(s.getBytes("UTF-8"));
    }

    /**
     * 判断字符串的md5校验码是否与一个已知的md5码相匹配
     *
     * @param password 要校验的字符串
     * @param md5PwdStr 已知的md5校验码
     * @return
     */
    public static boolean checkPassword(String password, String md5PwdStr) throws UnsupportedEncodingException {
        String s = getMD5String(password);
        return  s.equals(md5PwdStr);
    }

    /**
     * 生成文件的md5校验值
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) throws IOException {
        InputStream fis;
        fis = new FileInputStream(file);
        byte [] buffer =  new   byte [ 1024 ];
        int  numRead =  0 ;
        while((numRead = fis.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        fis.close();
        return  bufferToHex(messagedigest.digest());
    }

    /**
     * JDK1.4中不支持以MappedByteBuffer类型为参数update方法，并且网上有讨论要慎用MappedByteBuffer，
     * 原因是当使用 FileChannel.map 方法时，MappedByteBuffer 已经在系统内占用了一个句柄，
     * 而使用 FileChannel.close 方法是无法释放这个句柄的，且FileChannel有没有提供类似 unmap 的方法，
     * 因此会出现无法删除文件的情况。
     *
     * 不推荐使用
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String_old(File file)  throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0 ,
                file.length());
        messagedigest.update(byteBuffer);
        return  bufferToHex(messagedigest.digest());
    }

    public static String getMD5String(byte [] bytes) {
        messagedigest.update(bytes);
        return  bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte  bytes[]) {
        return  bufferToHex(bytes, 0, bytes.length);
    }

    private   static String bufferToHex(byte  bytes[], int  m, int  n) {
        StringBuffer stringbuffer = new StringBuffer( 2  * n);
        int  k = m + n;
        for  ( int  l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return  stringbuffer.toString();
    }

    private   static   void  appendHexPair( byte  bt, StringBuffer stringbuffer) {
        char  c0 = hexDigits[(bt &  0xf0 ) >>  4 ]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char  c1 = hexDigits[bt &  0xf ]; // 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

//    public   static   void  main(String[] args)  throws  IOException {
//        long  begin = System.currentTimeMillis();
//
//        File file = new  File( "C:/12345.txt" );
//        String md5 = getFileMD5String(file);
//
////      String md5 = getMD5String("a");
//
//        long  end = System.currentTimeMillis();
//        System.out.println("md5:"  + md5 +  " time:"  + ((end - begin) /  1000 ) +  "s" );
//    }



    /**
     * 加密写法1
     */

    public static String toMD5(String plainText) {    //plainText可为密码
        String result=null;
        //生成实现指定摘要算法的 MessageDigest 对象。
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(plainText.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest(plainText.getBytes("UTF-8"));
            //生成具体的md5密码到buf数组
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
           // System.out.println("32位: " + buf.toString());// 32位的加密
           // System.out.println("16位: " + buf.toString().substring(8, 24));// 16位的加密，其实就是32位加密后的截取
            //result=buf.toString().substring(8, 24);
            result=buf.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 加密写法2
     * @param original
     * @param separator
     * @return
     */
    public String toMd5(String original, String separator) {
        try {
            String result;
            byte[] bytes = original.getBytes();
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            result = toHexString(algorithm.digest(), separator);
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String toHexString(byte[] bytes, String separator) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", 0xFF & b)).append(separator);
        }
        return hexString.toString();
    }


    /**
     * 给文件加密
     *
     */

    /** Calculate MD5 sum of a file */
    static final public String calcMD5(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            FileInputStream input = new FileInputStream(file);
            byte[] buf = new byte[1024];

            while (input.available() > 0) {
                int res = input.read(buf, 0, buf.length);

                md.update(buf, 0, res);
            }
            input.close();

            byte[] md5 = md.digest();

            return bytesToHexString(md5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "" + file.length();
    }

    /**
     * Convert an array of bytes to a string of hexadecimal numbers
     */
    static final private String bytesToHexString(byte[] array) {
        StringBuffer res = new StringBuffer();

        for (int i = 0; i < array.length; i++) {
            int val = array[i] + 256;
            String b = "00" + Integer.toHexString(val);
            int len = b.length();
            String sub = b.substring(len - 2);

            res.append(sub);
        }

        return res.toString();
    }

}
