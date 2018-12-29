package com.example.pc.sluicecontrol.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;


import java.util.ArrayList;
import java.util.List;


/**
 * 2015年10月14日
 * <p/>
 * ManagerApplication.java
 *
 * @author wudu
 */
public class ManagerApplication extends Application {



    //登录
    private List<Activity> loginList = new ArrayList<Activity>();

    private List<Activity> saveList = new ArrayList<Activity>();

    private static ManagerApplication instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /**
     * 单例，不包含synchronized
     *
     * @return instance
     */
    public static ManagerApplication getInstance() {
        if (instance == null) {
            instance = new ManagerApplication();
        }
        return instance;
    }

    /**
     * 单例，包含synchronized
     *
     * @return instance
     */
    public synchronized static ManagerApplication getInstance2() {
        if (instance == null) {
            instance = new ManagerApplication();
        }
        return instance;
    }

    /**
     * 获取上下文
     *
     * @return context
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        }
        return null;
    }

}
