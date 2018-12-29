package com.example.pc.sluicecontrol.common.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2015年10月14日
 * <p/>
 * Tools.java
 *
 * @author wudu
 */
public class Tools {

    /**
     * 压缩图片
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth1 = newWidth;
        int newHeight1 = newHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth1) / width;
        float scaleHeight = ((float) newHeight1) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(0);

        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
    }

    /**
     * 格式化价格
     *
     * @param argStr
     * @return
     */
    public static String getFloatDotStr(String argStr) {
        float arg = Float.valueOf(argStr);
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(arg);
    }

    public static void Log(String s) {
        if (s == null) {
            s = "传进来的是null";
        }

        Log.i("info", s);
    }

    public static void Toast(Context context, String s) {
        // if (context == null)
        // context = ShiQiangApplication.getInstance().getApplicationContext();
        if (s != null) {
            android.widget.Toast.makeText(context, s,
                    android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    public static void Toast(Context context, int res) {
        if (context != null) {
            Toast(context, context.getString(res));
        }
    }

    public static boolean IsHaveInternet(final Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        } catch (Exception e) {
            return false;
        }
    }

    // 得到versionName
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;

    }

    public static String millisToString(long millis) {
        boolean negative = millis < 0;
        millis = Math.abs(millis);

        millis /= 1000;
        int sec = (int) (millis % 60);
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat
                .getInstance(Locale.US);
        format.applyPattern("00");
        if (millis > 0) {
            time = (negative ? "-" : "")
                    + (hours == 0 ? 00 : hours < 10 ? "0" + hours : hours)
                    + ":" + (min == 0 ? 00 : min < 10 ? "0" + min : min) + ":"
                    + (sec == 0 ? 00 : sec < 10 ? "0" + sec : sec);
        } else {
            time = (negative ? "-" : "") + min + ":" + format.format(sec);
        }
        return time;
    }

    // 得到versionName
    public static int getVerCode(Context context) {
        int verCode = 0;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;

    }

    /**
     * 判断 多个字段的值否为空
     *
     * @return true为null或空; false不null或空
     * @author Michael.Zhang 2013-08-02 13:34:43
     */
    public static boolean isNull(String... ss) {
        for (int i = 0; i < ss.length; i++) {
            if (null == ss[i] || ss[i].equals("")
                    || ss[i].equalsIgnoreCase("null")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(String s) {
        if (null == s || s.equals("") || s.equalsIgnoreCase("null")||s.equals("null")) {
            return true;
        }

        return false;
    }

    /**
     * 判断 多个字段的值否为空
     *
     * @return true为null或空; false不null或空
     * @author Michael.Zhang 2013-08-02 13:34:43
     */
    public static boolean isNull(TextView... vv) {
        for (int i = 0; i < vv.length; i++) {
            if (null == vv[i] || Tools.isNull(Tools.getText(vv[i]))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(TextView v) {
        if (null == v || Tools.isNull(Tools.getText(v))) {
            return true;
        }

        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(EditText v) {
        if (null == v || Tools.isNull(Tools.getText(v))) {
            return true;
        }

        return false;
    }

    /**
     * 判断两个字段是否一样
     *
     * @author Michael.Zhang 2013-08-02 13:32:51
     */
    public static boolean judgeStringEquals(String s0, String s1) {
        return s0 != null && s0.equals(s1);
    }

    /**
     * 将dp类型的尺寸转换成px类型的尺寸
     *
     * @param f
     * @param context
     * @return
     */
    public static int DPtoPX(float f, Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return (int) ((float) f * metrics.density + 0.5);
    }

    /**
     * @return int 返回类型
     * @Title: sp2px
     * @Description: 将sp值转换为px值，保证文字大小不变
     * @author lory
     */
    public static int SPtoPX(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 屏幕宽高
     *
     * @param context
     * @return 0:width，1:height
     * @author TangWei 2013-11-5上午10:27:54
     */
    public static int[] ScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    /**
     * double 整理
     *
     * @return
     */
    public static Double roundDouble(double val, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = ((0 == val) ? new BigDecimal("0.0") : new BigDecimal(
                Double.toString(val)));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmptyList(List list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmptyList(List... list) {
        for (int i = 0; i < list.length; i++) {
            if (isEmptyList(list[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    public static boolean isEmptyList(Object[] list) {
        return list == null || list.length == 0;
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    public static boolean isEmptyList(Object[]... list) {
        for (int i = 0; i < list.length; i++) {
            if (isEmptyList(list[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断sd卡是否存在
     *
     * @return
     * @author Michael.Zhang 2013-07-04 11:30:54
     */
    public static boolean judgeSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断 http 链接
     *
     * @param url
     * @return
     * @author Michael.Zhang
     */
    public static boolean isUrl(String url) {
        return url != null && url.startsWith("http://");
    }

    /**
     * 获取保存到View的Tag中的字符串
     *
     * @param v
     * @return
     */
    public static String getTagString(View v) {
        try {
            return v.getTag().toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取文本控件上显示的文字
     *
     * @param tv
     * @return
     * @author TangWei 2013-9-29下午2:40:52
     */
    public static String getText(TextView tv) {
        if (tv != null)
            return tv.getText().toString().trim();
        return "";
    }

    /**
     * 获取文本控件上显示的文字
     *
     * @param tv
     * @return
     * @author TangWei 2013-9-29下午2:40:52
     */
    public static String getText(EditText tv) {
        if (tv != null)
            return tv.getText().toString().trim();
        return "";
    }

    /**
     * 隐藏键盘
     *
     * @author TangWei 2013-9-13下午7:51:32
     */
    public static void hideKeyboard(Activity activity) {
        ((InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // public static void playSound(int raw, Context context) {
    // SoundPool sp;
    // sp = new SoundPool(1000, AudioManager.STREAM_SYSTEM, 5);
    // int task = sp.load(context, raw, 1);
    // sp.play(task, 1, 1, 0, 0, 1);
    // }

    /**
     * 显示纯汉字的星期名称
     *
     * @param i 星期：1,2,3,4,5,6,7
     * @return
     * @author TangWei 2013-10-25上午11:31:51
     */
    public static String changeWeekToHanzi(int i) {
        switch (i) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "";
        }
    }

    /**
     * 验证身份证号码
     *
     * @param idCard
     * @return
     * @author TangWei
     */
    public static boolean validateIdCard(String idCard) {
        if (isNull(idCard))
            return false;
        String pattern = "^[0-9]{17}[0-9|xX]{1}$";
        return idCard.matches(pattern);
    }

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     * @author TangWei
     */
    public static boolean validatePhone(String phone) {
        if (isNull(phone))
            return false;
        String pattern = "^1[3,4,5,6,8]+\\d{9}$";
        return phone.matches(pattern);
    }

    /**
     * 简单的验证一下银行卡号
     *
     * @param bankCard 信用卡是16位，其他的是13-19位
     * @return
     */
    public static boolean validateBankCard(String bankCard) {
        if (isNull(bankCard))
            return false;
        String pattern = "^\\d{13,19}$";
        return bankCard.matches(pattern);
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     * @author TangWei 2013-12-13下午2:33:16
     */
    public static boolean validateEmail(String email) {
        if (isNull(email))
            return false;
        String pattern = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        return email.matches(pattern);
    }

    public static String trimString(String str) {
        if (!Tools.isNull(str)) {
            return str.trim();
        }
        return "";
    }

    public static int StringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float StringToFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return 0.00f;
        }
    }

    public static String formatString(Object obj) {
        try {
            if (!Tools.isNull(obj.toString())) {
                return obj.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化money，当返回数据为空时，返回0.00
     *
     * @param obj
     * @return
     * @author TangWei 2013-11-23上午11:42:33
     */
    public static String formatMoney(Object obj) {
        String money = formatString(obj);
        if (money.length() == 0) {
            money = "0.00";
        }
        return money;
    }

    /**
     * 格式化日期，针对于传过来的日期是毫秒数
     *
     * @param date   日期毫秒数
     * @param format 格式化样式 示例：yyyy-MM-dd HH:mm:ss
     * @return
     * @author TangWei 2013-11-29上午11:31:49
     */
    public static String formatDate(Object date, String format) {
        try {
            return new SimpleDateFormat(format, Locale.CHINA).format(new Date(
                    Long.parseLong(formatString(date)) * 1000));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化日期，针对于传过来的日期是毫秒数<br>
     * 转换样式：2013-11-12 11:12:13
     *
     * @param date 日期毫秒数
     * @return
     * @author TangWei 2013-11-22上午11:38:13
     */
    public static String formatTime(Object date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期，针对于传过来的日期是毫秒数<br>
     * 转换样式：2013-11-12
     *
     * @param date 日期毫秒数
     * @return
     * @author TangWei 2013-11-22上午11:38:13
     */
    public static String formatDate(Object date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    /**
     * 获取屏幕像素尺寸
     *
     * @return 数组：0-宽，1-高
     * @author TangWei 2013-10-31下午1:08:22
     */
    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);
        size[0] = metrics.widthPixels;
        size[1] = metrics.heightPixels;
        return size;
    }

    /**
     * 设置圆角的图片
     *
     * @param bitmap 图片
     * @param pixels 角度
     * @return
     * @author TangWei 2013-12-10下午4:43:33
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        try {
            if (bitmap != null) {
                Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                        bitmap.getHeight(), Config.ARGB_8888);
                Canvas canvas = new Canvas(output);

                final int color = 0xff424242;
                final Paint paint = new Paint();
                final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                        bitmap.getHeight());
                final RectF rectF = new RectF(rect);
                final float roundPx = pixels;

                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(color);
                canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                canvas.drawBitmap(bitmap, rect, rect, paint);

                return output;
            }
        } catch (Exception e) {
        }

        return bitmap;
    }

    /**
     * 将图片转换为圆形的
     *
     * @param bitmap
     * @return
     * @author TangWei 2013-12-10下午4:45:47
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap = cutSquareBitmap(bitmap);
            return toRoundCorner(bitmap, bitmap.getWidth() / 2);
        }
        return bitmap;
    }

    /**
     * 把图片切成正方形的
     *
     * @param bitmap
     * @return
     * @author TangWei 2013-12-10下午5:16:18
     */
    public static Bitmap cutSquareBitmap(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                Bitmap result;
                int w = bitmap.getWidth();// 输入长方形宽
                int h = bitmap.getHeight();// 输入长方形高
                int nw;// 输出正方形宽
                if (w > h) {
                    // 宽大于高
                    nw = h;
                    result = Bitmap.createBitmap(bitmap, (w - nw) / 2, 0, nw,
                            nw);
                } else {
                    // 高大于宽
                    nw = w;
                    result = Bitmap.createBitmap(bitmap, 0, (h - nw) / 2, nw,
                            nw);
                }
                return result;
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    /**
     * 获取在GridView中一行中一张正方形图片的尺寸大小
     *
     * @param context 上下文，用于计算屏幕的宽度
     * @param offset  界面上左右两边的偏移量，dp值
     * @param spac    水平方向，图片之间的间距，dp值
     * @param count   一行中图片的个数
     * @return
     * @author TangWei 2013-12-12下午1:15:49
     */
    public static int getImageSize(Context context, int offset, int spac,
                                   int count) {
        int width = getScreenSize(context)[0] - Tools.DPtoPX(offset, context)
                - (Tools.DPtoPX(spac, context) * (count - 1));
        return width / count;
    }

    /**
     * 获取一个圆弧上等分点的坐标列表
     *
     * @param radius      半径
     * @param count       等分点个数
     * @param start_angle 开始角度
     * @param end_angle   结束角度
     * @return
     * @author TangWei 2013-12-16下午5:06:31
     */
    public static ArrayList<String[]> getDividePoints(double radius, int count,
                                                      double start_angle, double end_angle) {
        ArrayList<String[]> list = new ArrayList<String[]>();
        double sub_angle = (start_angle - end_angle) / ((double) (count - 1));
        for (int i = 0; i < count; i++) {
            double angle = (start_angle - sub_angle * i) * Math.PI / 180;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            list.add(new String[]{x + "", y + ""});
        }
        return list;
    }

    /**
     * 判断字符串是邮箱还是手机号码
     *
     * @param str
     * @return 1-手机号码，2-邮箱，如果都不是则返回0
     * @author TangWei 2013-12-19下午1:59:16
     */
    public static int validatePhoneOrEmail(String str) {
        if (validatePhone(str))
            return 1;
        if (validateEmail(str))
            return 2;
        return 0;
    }

    /**
     * 播放动画
     *
     * @param layout
     * @param img
     * @param
     */
    public static void startAnimation(final View layout, ImageView img,
                                      int drawableBefore, int drawableClick, boolean isClicked) {
        if (isClicked) {
            img.setBackgroundResource(drawableClick);
        } else {
            img.setBackgroundResource(drawableBefore);
        }

        // 播放动画
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(1, 1.2f, 1, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.2f, 1, 1.2f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnimation1.setStartOffset(0);
        scaleAnimation1.setDuration(50);
        scaleAnimation2.setStartOffset(50);
        scaleAnimation2.setDuration(50);
        animationSet.addAnimation(scaleAnimation1);
        animationSet.addAnimation(scaleAnimation2);
        animationSet.setFillAfter(true);
        img.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layout.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * SD卡是否存在
     */
    public static boolean existSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 创建文件夹
     */
    public static void makeDir(String path) {
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            rootFile.mkdir();
        }
    }

    /**
     * 根据Uri返回文件路径
     *
     * @param mUri
     * @return String
     * @author gdpancheng@gmail.com 2013-3-18 上午10:17:55
     */
    public static String getFilePath(ContentResolver mContentResolver, Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return mUri.getPath();
            } else {
                return getFilePathByUri(mContentResolver, mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * 将100以内的阿拉伯数字转换成中文汉字（15变成十五）
     *
     * @param round 最大值50
     * @return >99的，返回“”
     */
    public static String getHanZi1(int round) {
        if (round > 99 || round == 0) {
            return "";
        }
        int ge = round % 10;
        int shi = (round - ge) / 10;
        String value = "";
        if (shi != 0) {
            if (shi == 1) {
                value = "十";
            } else {
                value = getHanZi2(shi) + "十";
            }

        }
        value = value + getHanZi2(ge);
        return value;
    }

    /**
     * 将0-9 转换为 汉字（ _一二三四五六七八九）
     *
     * @param round
     * @return
     */
    public static String getHanZi2(int round) {
        String[] value = {"", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        return value[round];
    }

    /**
     * 将content: 开通的系统uri转换成绝对路径
     *
     * @param mContentResolver
     * @param mUri
     * @return
     * @throws FileNotFoundException
     */
    public static String getFilePathByUri(ContentResolver mContentResolver,
                                          Uri mUri) throws FileNotFoundException {

        String imgPath;
        Cursor cursor = mContentResolver.query(mUri, null, null, null, null);
        cursor.moveToFirst();
        imgPath = cursor.getString(1); // 图片文件路径
        return imgPath;
    }

    /**
     * 去除字符串中的 ":"
     *
     * @param str
     * @return
     */
    public static String deleteColon(String str) {
        if (str == null) {
            return null;
        } else {
            return str.replace(":", "");
        }
    }

    /**
     * 将 1800 加个":",变成 18:00
     *
     * @param str
     * @return
     */
    public static String addColon(String str) {
        if (str == null || str.length() != 4) {
            return null;
        }
        return str.substring(0, 2) + ":" + str.substring(2, 4);
    }

    /**
     * @return boolean 返回类型
     * @Title: is_Email
     * @Description: 验证邮箱，PHP方式修改，与后台统一
     * @author lory
     */
    public boolean is_Email(String email_str) {

        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = pattern.matcher(email_str);
        return m.matches();
    }

    /**
     * 验证手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        // Pattern p = Pattern
        // .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        // Pattern p = Pattern.compile("^1[3,5,8]\\d{9}$");
        Pattern p = Pattern
                .compile("^13[0-9]{9}$|^15[0-9]{9}$|^17[0-9]{9}$|^18[0-9]{9}$|^14[57][0-9]{8}$/");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * @return String 返回类型
     * @Title: getKey
     * @Description: 获得 {"key":"value"}类型hashmap中 的key
     * @author lory
     */
    public static String getKey(HashMap<String, Object> hashMap) {
        if (hashMap != null) {
            Iterator<Entry<String, Object>> iterator = hashMap.entrySet()
                    .iterator();
            if (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                Object key = entry.getKey();
                return Tools.formatString(key);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @return String 返回类型
     * @Title: getValue
     * @Description: 获得 {"key":"value"}类型hashmap中 的value
     * @author lory
     */
    public static String getValue(HashMap<String, Object> hashMap) {
        if (hashMap != null) {
            Iterator<Entry<String, Object>> iterator = hashMap.entrySet()
                    .iterator();
            if (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                Object val = entry.getValue();
                return formatString(val);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @return String 返回类型
     * @Title: getValue
     * @Description: 获得 {"key":"value"}类型hashmap中 的value
     * @author lory
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object> getValueMap(
            HashMap<String, Object> hashMap) {
        if (hashMap != null) {
            Iterator<Entry<String, Object>> iterator = hashMap.entrySet()
                    .iterator();
            if (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                Object val = entry.getValue();
                return (HashMap<String, Object>) val;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @return String 返回类型
     * @Title: mapToString
     * @Description: 将{87=罗店镇, 92=月浦镇, 91=杨行镇}的map转换成"罗店镇、月浦镇、杨行镇"
     * @author lory
     */
    public static String mapToString(HashMap<String, Object> hashMap,
                                     String divider) {
        StringBuilder stringBuilder = new StringBuilder();
        if (hashMap != null) {
            Iterator<Entry<String, Object>> iterator = hashMap.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                Object val = entry.getValue();
                stringBuilder.append(formatString(val)).append(divider);
            }
        } else {
            return "";
        }
        return stringBuilder.toString()
                .substring(0, stringBuilder.length() - 1);
    }

    /**
     * @return String 返回类型
     * @Title: arrayToString
     * @Description: 将ArrayList<String> arrayList 转换成String，中间以","隔开
     * @author lory
     */
    public static String arrayToString(ArrayList<String> arrayList) {
        if (arrayList != null && arrayList.size() > 0) {
            StringBuilder sBuilder = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                sBuilder.append(arrayList.get(i));
                sBuilder.append(",");
            }
            String result = sBuilder.toString();
            return result.substring(0, result.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * @return String 返回类型
     * @Title: arrayToString
     * @Description: 将ArrayList<String> arrayList 转换成String，中间以divider隔开
     * @author lory
     */
    public static String arrayToString(ArrayList<?> arrayList, String divider) {
        if (arrayList != null && arrayList.size() > 0) {
            StringBuilder sBuilder = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                sBuilder.append(arrayList.get(i));
                sBuilder.append(divider);
            }
            String result = sBuilder.toString();
            return result.substring(0, result.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * @return ArrayList<HashMap<String,Object>> 返回类型
     * @Title: mapToArrayList
     * @Description: 从hashmap中获得arraylArrayList<HashMap<String, Object>>，从而可以排序
     * @author lory
     */
    public static ArrayList<HashMap<String, Object>> mapToArrayList(
            HashMap<String, Object> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        List<Entry<String, Object>> infoIds = new ArrayList<Entry<String, Object>>(
                map.entrySet());

        // 排序前

        // 排序
        Collections.sort(infoIds, new Comparator<Entry<String, Object>>() {
            public int compare(Entry<String, Object> o1,
                               Entry<String, Object> o2) {
                return Integer.valueOf(o1.getKey())
                        - Integer.valueOf(o2.getKey());
            }
        });
        for (int i = 0; i < infoIds.size(); i++) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put(infoIds.get(i).getKey(), infoIds.get(i).getValue());
            arrayList.add(hashMap);
        }

        return arrayList;
    }

    /**
     * @return Bitmap 返回类型
     * @Title: stringToBitmap
     * @Description: 字符串转bitmap
     * @author yan
     */
    public static Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            Log.e("TAG", "图片转换失败" + e.getMessage());
        }
        return bitmap;
    }

    /**
     * @return boolean 返回类型
     * @Title: getString
     * @Description:是否有sim卡
     * @author yan
     */
    public static boolean isHaveSIMCard(Context context) {
        String imsi = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        if (Tools.isNull(imsi)) {
            Log.e("TAG", "没有卡");
            return false;
        } else {
            Log.e("TAG", "有卡");
            return true;
        }
    }

    /**
     * @return String 返回类型
     * @Title: getYjzPhone
     * @Description: 获得格式为"100 0000 0000"的电话号码
     * @author lory
     */
    public static String getYjzPhone(String phone) {
        if (isMobileNO(phone)) {
            return phone.substring(0, 3) + " " + phone.substring(3, 7) + " "
                    + phone.substring(7, 11);
        }
        return "";
    }

    /**
     * @param dataOfWeek Calendar.DAY_OF_WEEK
     * @return String 返回类型
     * @Title: getWeekText
     * @Description: 返回周几信息
     * @author lory
     */
    public static String getWeekText(int dataOfWeek) {
        switch (dataOfWeek) {
            case 1:
                return "（周日）";
            case 2:
                return "（周一）";
            case 3:
                return "（周二）";
            case 4:
                return "（周三）";
            case 5:
                return "（周四）";
            case 6:
                return "（周五）";
            case 7:
                return "（周六）";

            default:
                return "";
        }
    }

    /**
     * @param dataOfWeek Calendar.DAY_OF_WEEK
     * @return String 返回类型
     * @Title: getWeekCalendarText
     * @Description: 返回周几信息
     * @author lory
     */
    public static String getWeekCalendarText(int dataOfWeek) {
        switch (dataOfWeek) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";

            default:
                return "";
        }
    }

    /**
     * @return ArrayList<String> 返回类型
     * @Title: getSeekbarNum
     * @Description: 将“23”“4.5k”等统一转化为
     * @author lory
     */
    public static ArrayList<String> getSeekbarNum(ArrayList<String> arrayList) {
        ArrayList<String> list = new ArrayList<String>();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            String str = arrayList.get(i);
            if (str.contains("k")) {
                String newStr = str.replace("k", "");
                int num = (int) (Double.valueOf(newStr) * 1000);
                list.add(String.valueOf(num));
            } else {
                list.add(str);
            }
        }
        return list;
    }


    /**
     * 将金额转换成带小数点后两位的字符
     *
     * @param number
     * @return
     */
    public static String formatToDouble(String number) {

        number = formatMoney(number);

        Double numberd = Double.parseDouble(number.trim());

        return new DecimalFormat("#0.00").format(numberd);
    }

    /**
     * 将bitmap转换成byte数组
     *
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * byte转bitmap
     *
     * @param b
     * @return
     */
    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    /**
     * scrollview嵌套listview高度设置
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    //可以参考，但是该方法有错误，暂时不鞥使用
    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns = 0;
        int horizontalBorderHeight = 0;
        Class<?> clazz = gridView.getClass();
        try {
            //利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(gridView);
            //利用反射，取得横向分割线高度
            Field horizontalSpacing = clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        //判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { //只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + horizontalBorderHeight * (rows - 1);//最后加上分割线总高度
        gridView.setLayoutParams(params);
    }
}
