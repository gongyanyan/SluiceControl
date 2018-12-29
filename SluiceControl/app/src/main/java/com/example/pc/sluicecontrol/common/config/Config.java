package com.example.pc.sluicecontrol.common.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * Created by lmx on 2015/10/30.
 */
public class Config {
    private static Config instance = null;

    private static SharedPreferences sharedPreferences = null;

    private String APP_CONFIG_FILE_NAME = "zcl_config";

    private Config(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_CONFIG_FILE_NAME,
                Context.MODE_PRIVATE);

    }

    public static synchronized Config Config(Context context) {
        if (instance == null)
            instance = new Config(context);
        return instance;
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void CleanString(String key) {   //清除指定数据
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }


    private static String AUTOUPDATE = "AutoUpdate" ; //更新版本
    private static String IS_FIRST = "isFirst";//是不是第一次登陆
    private static String URL = "url";//实际使用的url
    private static String URLSC = "urlsc";//生产环境的url
    private static String URLKF = "urlkf";//开发环境的url
    private static String PORT = "port";//实际的端口
    private static String PORTSC = "portsc";//生产环境的端口
    private static String PORTKF = "portkf";//生产环境的端口
    private static String EVTNAME = "evt";//使用环境的名称

    //更新版本
    public void setAutoUpdate( long autoUpdate){
        this.setLong(AUTOUPDATE,autoUpdate);
    }
    public  long getAutoUptade(){
        return sharedPreferences.getLong(AUTOUPDATE,Long.valueOf("2016103101"));
    }

    //是否是第一次进
    public void setIsFirst(boolean isFirst){
        this.setBoolean(IS_FIRST,isFirst);
    }
    public boolean getIsFirst(){
        return sharedPreferences.getBoolean(IS_FIRST,false);
    }
    //实际使用的URL
    public void setURL(String url){
        this.setString(URL,url);
    }
    public String getURL(){
        return sharedPreferences.getString(URL,"");
    }

    public void setURLSC(String urlsc){
        this.setString(URLSC,urlsc);
    }
    public String getURLSC(){
        return sharedPreferences.getString(URLSC,"");
    }

    public void setURLKF(String urlkf){
        this.setString(URLKF,urlkf);
    }
    public String getURLKF(){
        return sharedPreferences.getString(URLKF,"");
    }


    //PORT
    public void setPORT(String port){
        this.setString(PORT,port);
    }
    public String getPORT(){
        return sharedPreferences.getString(PORT,"");
    }

    public void setPORTSC(String portsc){
        this.setString(PORTSC,portsc);
    }
    public String getPORTSC(){
        return sharedPreferences.getString(PORTSC,"");
    }

    public void setPORTKF(String portkf){
        this.setString(PORTKF,portkf);
    }
    public String getPORTKF(){
        return sharedPreferences.getString(PORTKF,"");
    }


    //////使用什么环境
    public void setEVTNAME(String evtname){
        this.setString(EVTNAME,evtname);
    }
    public String getEVTNAME(){
        return sharedPreferences.getString(EVTNAME,"");
    }
}
