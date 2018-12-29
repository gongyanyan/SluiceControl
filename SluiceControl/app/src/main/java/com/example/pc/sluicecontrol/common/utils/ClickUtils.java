package com.example.pc.sluicecontrol.common.utils;

/**
 * author:wudu
 * <p/>
 * 2016/1/21.
 * <p/>
 * 防止重复点击
 */
public class ClickUtils {
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 3000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
