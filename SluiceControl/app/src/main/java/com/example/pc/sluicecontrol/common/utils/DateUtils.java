package com.example.pc.sluicecontrol.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bs on 2016/12/21.
 */

public class DateUtils {

    private static SimpleDateFormat sf = null;


    public static String getCurrentDate(String s) {

        Date d = new Date(s);

        sf = new SimpleDateFormat("yyyy-MM-dd");

        return sf.format(d);

        }

    public static String getCurrentDate() {

        Date d = new Date();

        sf = new SimpleDateFormat("yyyy-MM-dd");

        return sf.format(d);

        }
    public static String getCurrentDate1() {

        Date d = new Date();

        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

        return sf.format(d);

        }
    public static String getCurrentDate3() {

        Date d = new Date();

        sf = new SimpleDateFormat("yyyyMMddHHmmss");

        return sf.format(d);

        }
    public static String getCurrentDate2() {

        Date d = new Date();

        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sf.format(d);

        }

//            　　/*时间戳转换成字符窜*/
        public static String getDateToString(long time) {

        Date d = new Date(time);

        sf = new SimpleDateFormat("yyyy-MM-dd");

        return sf.format(d);

        }

//            　　/*将字符串转为时间戳*/
        public static String getStringToDate(String time) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try{
            date = sdf.parse(time);
            } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        return ""+(date.getTime()/1000);
//        return ""+(date.getTime());
        }
//            　　/*将字符串转为时间戳*/
        public static String getStringToDate1(String time) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try{
            date = sdf.parse(time);
            } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            }
        return ""+(date.getTime()/1000);
//        return ""+(date.getTime());
        }

    /**
     * yyyy-MM-dd HH:mm:ss转换为yyyy-MM-dd
     * @author 刘鹏
     * @throws ParseException
     */
    public static String transferFormat(String inTime) {
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd");
        Date tempDate =null;
        String outTime = null;
        try {
            tempDate = s1.parse(inTime);
            outTime = s2.format(s2.parse(s1.format(tempDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outTime;
    }

}
