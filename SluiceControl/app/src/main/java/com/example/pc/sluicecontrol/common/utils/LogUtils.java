package com.example.pc.sluicecontrol.common.utils;

import android.util.Log;

/**
 * 2015年10月15日
 * <p/>
 * LogUtils.java
 *
 * @author wudu
 */
public class LogUtils {

    /**
     * log分割
     */
    public static final String SEPARATOR = ",";
    /**
     * 是否输出log
     */
    private static final boolean isPrintLog = true;

    /**
     * 默认输出方式
     *
     * @param message ：log信息
     */
    public static void print(String message) {
        if (isPrintLog) {
            StackTraceElement stackTraceElement = Thread.currentThread()
                    .getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);

            Log.i(tag, message + getLogInfo(stackTraceElement));
        }
    }

    /**
     * 打印日志
     *
     * @param message 日志信息
     * @param type    日志级别 1 info 2 error 默认info；
     */
    public static void print(String message, int type) {

        if (isPrintLog) {
            StackTraceElement stackTraceElement = Thread.currentThread()
                    .getStackTrace()[3];
            String tag = getDefaultTag(stackTraceElement);
            switch (type) {
                case 1:
                    Log.i(tag, message + getLogInfo(stackTraceElement));
                    break;

                case 2:
                    Log.e(tag, message + getLogInfo(stackTraceElement));
                    break;

                default:
                    Log.i(tag, message + getLogInfo(stackTraceElement));
                    break;

            }

        }
    }

    public static void e(String message) {
        print(message, 2);
    }

    /**
     * 输出日志所包含的信息
     */
    public static String getLogInfo(StackTraceElement stackTraceElement) {
        StringBuilder logInfoStringBuilder = new StringBuilder();
        // 获取线程名
        String threadName = Thread.currentThread().getName();
        // 获取线程ID
        @SuppressWarnings("unused")
        long threadID = Thread.currentThread().getId();
        // 获取文件名.即xxx.java
        String fileName = stackTraceElement.getFileName();
        // 获取类名.即包名+类名
        String className = stackTraceElement.getClassName();
        // 获取方法名称
        String methodName = stackTraceElement.getMethodName();
        // 获取输出行数
        int lineNumber = stackTraceElement.getLineNumber();

        logInfoStringBuilder.append("[ ");
        // logInfoStringBuilder.append("threadID=" +
        // threadID).append(SEPARATOR);
        logInfoStringBuilder.append("threadName=" + threadName).append(
                SEPARATOR);
        logInfoStringBuilder.append("fileName=" + fileName).append(SEPARATOR);
        logInfoStringBuilder.append("className=" + className).append(SEPARATOR);
        logInfoStringBuilder.append("methodName=" + methodName).append(
                SEPARATOR);
        logInfoStringBuilder.append("lineNumber=" + lineNumber);
        logInfoStringBuilder.append(" ] ");
        return logInfoStringBuilder.toString();
    }

    /**
     * 获取默认的TAG名称. 比如在MainActivity.java中调用了日志输出. 则TAG为MainActivity
     */
    public static String getDefaultTag(StackTraceElement stackTraceElement) {
        String fileName = stackTraceElement.getFileName();
        String stringArray[] = fileName.split("\\.");
        String tag = stringArray[0];
        return tag;
    }

    /**
     * 一般的输出类，发布时改成false，不输出打印
     *
     * @param mess
     */
    public static void prints(String mess) {
        if (false) {
            Log.d("SCALPER_TAG", mess);
        }
    }

    /**
     * 是否弹出toast
     *
     * @return
     */
    public static boolean isToast() {
        return true;
    }

}
