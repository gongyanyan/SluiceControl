package com.example.pc.sluicecontrol.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 2015年10月14日
 * <p/>
 * AppManager.java
 *
 * @author wudu
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
        activityStack = new Stack<Activity>();
    }


    /**
     * 单例，不包含synchronized
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 单例，包含synchronized
     *
     * @return instance
     */
    public static AppManager getInstance() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    /**
     * 栈中是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return activityStack.isEmpty();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 获取前一个activity，便于返回
     *
     * @return
     */
    public Activity lastActivity() {
        if (activityStack.size() < 2) {
            return null;
        }
        return activityStack.get(activityStack.size() - 1);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack.empty()) {
            return;
        }
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 从栈中移除指定的Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (activityStack.remove(activity)) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        ArrayList<Activity> activityList = new ArrayList<Activity>(
                activityStack);
        for (int i = 0, size = activityList.size(); i < size; i++) {
            if (null != activityList.get(i)) {
                activityList.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity保留主界面
     */
    public void finishAllActivityExcludeMain() {
        int stackSize = activityStack.size();
        if (stackSize >= 1) {
            ArrayList<Activity> activityList = new ArrayList<Activity>(
                    activityStack.subList(1, stackSize));
            for (int i = 0, size = activityList.size(); i < size; i++) {
                Activity activity = activityList.get(i);
                if (activity != null) {
                    activity.finish();
                    activityStack.remove(activity);
                }
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            //退出时将登陆状态置为0
//            Config.Config(context).setLoginStatus(0);
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    /**
     * 退出应用程序并停止服务
     */
    public void AppExit(Context context, Intent intent) {
        try {
            context.stopService(intent);
            finishAllActivity();
        } catch (Exception e) {
        }
    }




    /**
     * 相册添加
     */
    private List<Activity> billList = new ArrayList<Activity>();

    public void addBillActivity(Activity activity) {
        billList.add(activity);
    }

    public void finishBill() {
        for (Activity activity : billList) {
            activity.finish();
        }
    }

    /**
     * 相册添加
     */
    private List<Activity> billShowList = new ArrayList<Activity>();

    public void addBillShowActivity(Activity activity) {
        billList.add(activity);
    }

    public void finishBillShow() {
        for (Activity activity : billList) {
            activity.finish();
        }
    }
    private List<Activity> chooseBarCodeList = new ArrayList<Activity>();
    public void addChooseBarCodeActivity(Activity activity){
        chooseBarCodeList.add(activity);
    }
    public void finishChooseBarCode(){
        for (Activity activity:chooseBarCodeList){
            activity.finish();
        }
    }
}
