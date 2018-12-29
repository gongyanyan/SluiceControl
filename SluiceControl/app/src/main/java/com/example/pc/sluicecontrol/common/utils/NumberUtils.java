package com.example.pc.sluicecontrol.common.utils;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class NumberUtils {

    public static String getNumber(String str) {
        str = str.trim();
        String str2 = "";
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 += str.charAt(i);
                }
            }
        }
        return str2;
    }
    /**
     * 将时间添加年月日
     */
    public static String getDate(String str){
        String string = "";
        if (str.length()>6){
            string = str.substring(0,4)+"年"+str.substring(4,6)+"月"+str.substring(6)+"日";
        }else {
            string =str;
        }
        return string;

    }
    /**
     * 将时间添加年月日
     */
    public static String getMyDate(String str){
        String string = "";
        if (str.length()>6){
            string = str.substring(0,4)+"-"+str.substring(4,6)+"-"+str.substring(6);
        }else {
            string =str;
        }
        return string;

    }
    /**
     * 获取前三天时间
     */
    public static String getBefordate(String specifiedDay, int fixedTime){

        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yyyy年MM月dd日").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-(fixedTime-1));

        String dayBefore1=new SimpleDateFormat("yyyy年MM月dd日").format(c.getTime());
        return dayBefore1;
    }


    /**
     * 获取前三天时间
     */
    public static String getBefordate2(String specifiedDay, int fixedTime){

        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-(fixedTime-1));

        String dayBefore1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.getTime());
        return dayBefore1;
    }


    /**
     * 获取前30天时间
     */
    public static String getBefordate(String specifiedDay){

        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = new SimpleDateFormat("yyyy年MM月dd日").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day- 29);

        String dayBefore1=new SimpleDateFormat("yyyy年MM月dd日").format(c.getTime());
        return dayBefore1;
    }

    public static boolean isDate(String beginTime, String endTime){
        boolean isdate = false;
        String str1 = getNumber(beginTime);
        String str2 = getNumber(endTime);
        if (Integer.valueOf(str2)>= Integer.valueOf(str1)){
            isdate = true;
        }else {
            isdate = false;
        }
        return isdate;
    }


    /**
     * 获得单据后四位
     * @param number    流水号
     * @return
     */
    public static String getEndString(int number){
        String str = "0001";
        if (number==0){
            return str ;
        }else if (number/10==0){
            str = "000"+number;
            return str;
        }else if (number/100==0){
            str = "00"+number;
            return str ;
        }else if (number/1000==0){
            str = "0"+number ;
            return str ;
        }else {
            return ""+number;
        }
    }

    /**
     * 单据流水号码
     * @param str   单据号
     * @return
     */
    public static int getEndNumber(String str){
            String string = str.substring(str.length()-4);
        int number = 1;
        if (string.substring(0,3).equals("000")){
            return Integer.valueOf(string.substring(3));
        }else if (string.substring(0,2).equals("00")){
            return Integer.valueOf(string.substring(2));
        }else if (str.substring(0,1).equals("0")){
            return Integer.valueOf(string.substring(1));
        }else {
            return Integer.valueOf(string);
        }
    }

    public static int getBestNumber(List<String> endNumberString){
        int endNumber = 0;
        for (String s:endNumberString){
            if (endNumber<getEndNumber(s)){
                endNumber = getEndNumber(s);

            }
        }
        return endNumber;
    }

    public static int getSL(String number){
        int s = (int)Double.parseDouble(number);
        return s ;
    }
    /**
     *  final Activity activity  ：调用该方法的Activity实例       
     * long milliseconds ：震动的时长，单位是毫秒      
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     *   boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次      
     *   
     */

    public static void Vibrate(final Activity activity, long milliseconds) {
        	        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        	        vib.vibrate(milliseconds);
        	    }
    	    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        	        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        	        vib.vibrate(pattern, isRepeat ? 1 : -1);
        	    }




}
