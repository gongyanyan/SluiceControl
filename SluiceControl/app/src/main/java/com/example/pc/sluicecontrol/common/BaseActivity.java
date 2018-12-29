package com.example.pc.sluicecontrol.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pc.sluicecontrol.R;
import com.example.pc.sluicecontrol.common.utils.AppManager;
import com.example.pc.sluicecontrol.common.utils.HomeUtil;
import com.example.pc.sluicecontrol.common.utils.LogPrintUtils;
import com.example.pc.sluicecontrol.common.utils.PingYinUtil;
import com.example.pc.sluicecontrol.common.utils.Tools;

import java.io.File;



/**
 * 2015年10月14日
 * <p/>
 * BaseActivity.java
 *
 * @author wudu
 */

public abstract class BaseActivity extends FragmentActivity implements
        OnClickListener {
    /**
     * Log标识
     */
    public static String TAG = "BaseActivity";

    /**
     * 是否隐藏公共的标题栏，默认false，不隐藏
     */
    public static boolean isHidePublicTitle = false;

    /**
     * 限制view标识
     */
    public static final int FLAG_UNABLE_VIEW = 2;

    /**
     * 限制view时间长度
     */
    public static final int FLAG_UNABLE_TIME = 500;

    /**
     * 关闭加载进度条的时间和标志
     */
    public static final int FLAG_CLOSE_LODING = 60 * 1000;
    public static final int FLAG_CLOSE_LODING_TAG = 60 * 1000;
    public static final int FINAL_TIME_UPDATE = 30 * 1000;
    public static final int FINAL_TIME_DOWN = 10 * 1000;

    /**
     * 上下文环境
     */
    public Context mContext;

    /**
     * 资源对象
     */
    public Resources mResources;

    /**
     * 布局资源对象
     */
    public LayoutInflater mInflater;

    /**
     * 加载动画对象
     */
    private AnimationDrawable animationDrawable;

    /**
     * 设置view不可快速点击
     */
    private View mView;

    /**
     * toast交互,系统
     */
    public static Toast mToast;

    /**
     * 进度条动画背景是否透明，默认不透明
     */
    private boolean isAnimationBackgroundTransparent = false;

    /**
     * 标识：是否loading页正在显示
     */
    protected boolean isAnimationShow = false;

    /**
     * 当前窗口是否销毁
     */
    public boolean isWindowFinished = false;

    /**
     * 捕获返回键,默认不捕获
     */
    private boolean needCatchKeycodeBack = false;

    /**
     * 当前点击返回键次数
     */
    private int clickKeycodeBackNum = 0;

    /**
     * title的整个父布局
     */
    private LinearLayout baseac_top_frame;

    /**
     * 子类填充布局
     */
    private RelativeLayout baseac_content;

    /**
     * 获取标题栏左边的ImageView
     */
    private ImageView baseac_title_left_img;

    /**
     * 获取标题栏右边的ImageView
     */
    private ImageView baseac_title_right_img;

    /**
     * 中间的标题TextView
     */
    private TextView baseac_title_tv;

    /**
     * 左边的标题TextView
     */
    private TextView baseac_title_left_tv;

    /**
     * 右边的标题TextView
     */
    private TextView baseac_title_right_tv;

    /**
     * loading动画父布局
     */
    private RelativeLayout base_loading_rl;

    /**
     * loading动画显示区域
     */
    private ImageView base_loading_img;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_UNABLE_VIEW:
                    if (isWindowFinished) {
                        return;
                    }
                    if (mView instanceof Button || mView instanceof ListView) {
                        mView.setEnabled(true);
                    } else {
                        mView.setClickable(true);
                    }
                    handler.removeMessages(FLAG_UNABLE_VIEW);
                    break;
                case FLAG_CLOSE_LODING_TAG:
                    endLoadingView();
                    handler.removeMessages(FLAG_CLOSE_LODING_TAG);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        // 默认竖屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 没有标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 加载布局
        setContentView(R.layout.ac_base);
        // 初始化base中的view
        initBaseView();

        mContext = ManagerApplication.getContext();
        mResources = mContext.getResources();
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化initView
     */
    protected abstract void initView();

    /**
     * 初始化initData
     */
    protected abstract void initData();

    /**
     * 初始化initClickListener
     */
    protected abstract void initClickListener(View v);

    /**
     * 初始化Base中的一些控件
     */
    private void initBaseView() {
        /**
         * loading动画显示区域
         *
         */
        base_loading_img = (ImageView) findViewById(R.id.base_loading_img);

        /**
         * loading动画父布局
         */
        base_loading_rl = (RelativeLayout) findViewById(R.id.base_loading_rl);

        /**
         * title的整个父布局
         */
        baseac_top_frame = (LinearLayout) findViewById(R.id.baseac_top_frame);
        baseac_top_frame.setVisibility(View.VISIBLE);

        /**
         * 子类填充布局
         */
        baseac_content = (RelativeLayout) findViewById(R.id.baseac_content);

        /**
         * 获取标题栏左边的ImageView,默认设置监听
         */
        baseac_title_left_img = (ImageView) findViewById(R.id.baseac_title_left_img);
        baseac_title_left_img.setOnClickListener(this);

        /**
         * 获取标题栏右边的ImageView
         */
        baseac_title_right_img = (ImageView) findViewById(R.id.baseac_title_right_img);

        /**
         * 中间的标题TextView
         */
        baseac_title_tv = (TextView) findViewById(R.id.baseac_title_tv);

        /**
         * 左边的标题TextView
         */
        baseac_title_left_tv = (TextView) findViewById(R.id.baseac_title_left_tv);

        /**
         * 右边的标题TextView
         */
        baseac_title_right_tv = (TextView) findViewById(R.id.baseac_title_right_tv);
    }

    /**
     * 设置填充区域的布局
     *
     * @param layoutId :填充区域的布局的id
     */
    public void setMainContentLayout(int layoutId) {
        baseac_content = (RelativeLayout) findViewById(R.id.baseac_content);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainView = inflater.inflate(layoutId, null);
        // 设置全部填充
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mainView.setLayoutParams(params);
        baseac_content.addView(mainView);
    }

    /**
     * 隐藏公共的title布局
     *
     * @param isHidePublicTitle
     */
    public void setHidePublicTitle(boolean isHidePublicTitle) {
        if (isHidePublicTitle) {
            baseac_top_frame.setVisibility(View.GONE);
        } else {
            baseac_top_frame.setVisibility(View.VISIBLE);
            baseac_top_frame.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取标题栏左边的ImageView
     */
    public ImageView getLeftImageView() {
        return (ImageView) findViewById(R.id.baseac_title_left_img);
    }

    /**
     * 设置标题栏左边 的img是否显示，并设置监听，使用的默认图片
     *
     * @param isHide :true-显示，false-隐藏
     */
    public void setLeftImageView(boolean isHide) {
        hideView(baseac_title_left_img, isHide);
        //baseac_title_left_img.setOnClickListener(this);
    }

    /**
     * 设置标题栏左边 的img是否显示，并设置监听，使用imgResourceId图片
     *
     * @param isHide        :true-显示，false-隐藏
     * @param imgResourceId :图片的id
     */
    public void setLeftImageView(boolean isHide, int imgResourceId) {
        hideView(baseac_title_left_img, isHide);
        baseac_title_left_img.setOnClickListener(this);
        // 如果不为空，设置图片
        if (String.valueOf(imgResourceId) != null) {
            baseac_title_left_img.setImageResource(imgResourceId);
        }
    }

    /**
     * 设置标题栏左边 的img是否显示，并设置监听，使用drawable
     *
     * @param isHide   :true-显示，false-隐藏
     * @param drawable ：新的图片
     */
    public void setLeftImageView(boolean isHide, Drawable drawable) {
        hideView(baseac_title_left_img, isHide);
        baseac_title_left_img.setOnClickListener(this);
        // 如果不为空，设置图片
        if (drawable != null) {
            baseac_title_left_img.setImageDrawable(drawable);
        }
    }

    /**
     * 获取标题栏右边的ImageView
     */
    public ImageView getRightImageView() {
        return (ImageView) findViewById(R.id.baseac_title_right_img);
    }

    /**
     * 设置标题栏右边 的img是否显示，并设置监听，使用的默认图片
     *
     * @param isHide :true-显示，false-隐藏
     */
    public void setRightImageView(boolean isHide) {
        hideView(baseac_title_right_img, isHide);
        baseac_title_right_img.setOnClickListener(this);
    }

    /**
     * 设置标题栏右边 的img是否显示，并设置监听，使用的默认图片
     *
     * @param isHide        :true-显示，false-隐藏
     * @param imgResourceId :图片的id
     */
    public void setRightImageView(boolean isHide, int imgResourceId) {
        hideView(baseac_title_right_img, isHide);
        baseac_title_right_img.setOnClickListener(this);
        // 如果不为空，设置图片
        if (String.valueOf(imgResourceId) != null) {
            baseac_title_right_img.setImageResource(imgResourceId);
            baseac_title_right_img.setOnClickListener(this);
        }
    }

    /**
     * 当右侧图片已经显示的时候，改变图片的设置
     *
     * @param imgResourceId
     */
    public void setRightImageView(int imgResourceId) {
        baseac_title_right_img.setImageResource(imgResourceId);
    }

    /**
     * 设置标题栏左边 的img是否显示，并设置监听，使用drawable
     *
     * @param isHide   :true-显示，false-隐藏
     * @param drawable ：新的图片
     */
    public void setRightImageView(boolean isHide, Drawable drawable) {
        hideView(baseac_title_right_img, isHide);
        baseac_title_right_img.setOnClickListener(this);
        // 如果不为空，设置图片
        if (drawable != null) {
            baseac_title_right_img.setImageDrawable(drawable);
        }
    }

    /**
     * 设置中间的标题
     *
     * @param title
     */
    public void setMiddleTitle(String title) {
        if (title != null) {
            baseac_title_tv.setText(title);
        }
    }

    /**
     * 设置中间的标题
     *
     * @param _id
     */
    public void setMiddleTitleForId(int _id) {
        baseac_title_tv.setText(mResources.getText(_id));
    }

    /**
     * 设置标题的字体颜色和大小,不需要改变时传入null
     *
     * @param colorId ：字体颜色id
     * @param sizeId  ：字体大小id
     */
    public void setMiddleTitleColorSize(int colorId, int sizeId) {
        if (String.valueOf(colorId) != null) {
            baseac_title_tv.setTextColor(colorId);
        }

        if (String.valueOf(sizeId) != null) {
            baseac_title_tv.setTextSize(sizeId);
        }
    }

    /**
     * 设置左边的标题
     *
     * @param leftTitle
     */
    public void setLeftTitle(String leftTitle) {
        if (leftTitle != null) {
            baseac_title_left_tv.setVisibility(View.VISIBLE);
            baseac_title_left_tv.setText(leftTitle);
        }
    }

    /**
     * 设置标题的字体颜色和大小,不需要改变时传入null
     *
     * @param colorId ：字体颜色id
     * @param sizeId  ：字体大小id
     */
    public void setLeftTitleColorSize(int colorId, int sizeId) {
        if (String.valueOf(colorId) != null) {
            baseac_title_left_tv.setTextColor(colorId);
        }

        if (String.valueOf(sizeId) != null) {
            baseac_title_left_tv.setTextSize(sizeId);
        }
    }

    /**
     * 设置右边的标题
     *
     * @param rightTitle
     */
    public void setRightTitle(String rightTitle) {
        if (rightTitle != null) {
            baseac_title_right_tv.setVisibility(View.VISIBLE);
            baseac_title_right_tv.setText(rightTitle);
            baseac_title_right_tv.setOnClickListener(this);
        }
    }

    /**
     * 设置标题的字体颜色和大小,不需要改变时传入null
     *
     * @param colorId ：字体颜色id
     * @param sizeId  ：字体大小id
     */
    public void setRightTitleColorSize(int colorId, int sizeId) {
        if (String.valueOf(colorId) != null) {
            baseac_title_right_tv.setTextColor(colorId);
        }

        if (String.valueOf(sizeId) != null) {
            baseac_title_right_tv.setTextSize(sizeId);
        }
    }

    /**
     * 设置标题的字体颜色和大小,不需要改变时传入null
     *
     * @param colorId ：字体颜色id
     */
    public void setRightTitleColorSize(int colorId) {
        if (String.valueOf(colorId) != null) {
            baseac_title_right_tv.setTextColor(colorId);
        }
    }

    /**
     * 隐藏view
     *
     * @param view
     * @param flag :true-显示，false-隐藏
     */
    public void hideView(View view, boolean flag) {
        if (flag) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 监听返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isAnimationShow) {
                    if (!isAnimationBackgroundTransparent) {
                        onKeyDownBack();
                    }
                } else if (needCatchKeycodeBack) {
                    if (clickKeycodeBackNum >= 1) {
                        // 这里finish所有Activity，不会走正常的Activity生命周期（onPause-onStop-onDestroy）
                        // AppManager.getAppManager().deleteSDKInfo(mContext);
                        AppManager.getAppManager().AppExit(mContext);
                        return false;
                    } else {
                        clickKeycodeBackNum++;
                        toastMsg("再次点击将退出应用~");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 将回退键点击次数置为0
                                clickKeycodeBackNum = 0;
                            }
                        }, 3000);
                    }
                    return true;// 不再传递该事件
                }
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * @return void 返回类型
     * @Title: onKeyDownBack
     * @Description: 当播放进度动画并且动画背景不透明时，点击“返回键”处理函数，子类根据界面情境进行覆盖
     * @author wudu
     */
    public abstract void onKeyDownBack();

    /**
     * @param needCatch 是否捕获，true为捕获
     * @return void
     * @Title: setCatchKeycodeBack
     * @Description: 设置当前界面是否捕获 返回键
     * @author wudu
     */
    public void setCatchKeycodeBack(boolean needCatch) {
        needCatchKeycodeBack = needCatch;
    }

    /**
     * @return void 返回类型
     * @Title: onClick
     * @Description: 标题栏左边返回，子类根据界面情境进行覆盖
     * @author wudu
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.baseac_title_left_img:// 左侧的返回
                HomeUtil.hideKeyWord(BaseActivity.this, baseac_title_left_img);
                finishPage();
                break;
            case R.id.baseac_title_right_img://右侧图片
                rightImgClick();
                break;
            default:
                break;
        }
        initClickListener(v);
    }

    /**
     * @return void 返回类型
     * @Title: finishPage
     * @Description: 标题栏左边返回，默认关闭当前页面，子类根据界面情境进行覆盖，不需要调用父类的super即可
     * @author wudu
     */
    public void finishPage() {
        finish();
    }

    /**
     * @return void 返回类型
     * @Title: finishPage
     * @Description: 右侧的图片的点击事件，子类覆盖即可
     * @author wudu
     */
    public void rightImgClick() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isWindowFinished = true;
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * @param msg 弹出的内容
     * @return void 返回类型
     * @Title: toastMsg
     * @Description: toast用户交互, 字符串来源于string
     * @author wudu
     */
    public void toastMsg(String msg) {
        toastMsg(msg, Toast.LENGTH_SHORT);
    }

    /**
     * @param strId 弹出的内容
     * @return void 返回类型
     * @Title: toastMsg
     * @Description: toast用户交互, 字符串来源于xml文件strId
     * @author wudu
     */
    public void toastMsg(int strId) {
        toastMsg(strId, Toast.LENGTH_SHORT);
    }

    /**
     * @param msg      交互内容
     * @param showTime 显示时间长度
     * @return void 返回类型
     * @Title: toastMsg
     * @Description: toast用户交互, 系统
     * @author wudu
     */
    @SuppressLint("InflateParams")
    public void toastMsg(String msg, int showTime) {
        if (Tools.isNull(msg)) {
            return;
        }
        if (mToast != null) {
            View toastRoot = getLayoutInflater().inflate(R.layout.toast_my,
                    null);
            TextView message = (TextView) toastRoot
                    .findViewById(R.id.tv_toast_my_content);
            message.setText(msg);
            mToast.setView(toastRoot);
        } else {
            View toastRoot = getLayoutInflater().inflate(R.layout.toast_my,
                    null);
            TextView message = (TextView) toastRoot
                    .findViewById(R.id.tv_toast_my_content);
            message.setText(msg);
            mToast = new Toast(this);
            mToast.setDuration(showTime);
            mToast.setView(toastRoot);
        }
        mToast.show();
    }

    /**
     * @param strId    交互内容
     * @param showTime 显示时间长度
     * @return void 返回类型
     * @Title: toastMsg
     * @Description: toast用户交互, 系统
     * @author wudu
     */
    @SuppressLint("InflateParams")
    public void toastMsg(int strId, int showTime) {
        if (Tools.isNull(String.valueOf(strId))) {
            return;
        }
        if (mToast != null) {
            View toastRoot = getLayoutInflater().inflate(R.layout.toast_my,
                    null);
            TextView message = (TextView) toastRoot
                    .findViewById(R.id.tv_toast_my_content);
            message.setText(mResources.getString(strId));
            mToast.setView(toastRoot);
        } else {
            View toastRoot = getLayoutInflater().inflate(R.layout.toast_my,
                    null);
            TextView message = (TextView) toastRoot
                    .findViewById(R.id.tv_toast_my_content);
            message.setText(mResources.getString(strId));
            mToast = new Toast(this);
            mToast.setDuration(showTime);
            mToast.setView(toastRoot);
        }
        mToast.show();
    }

    /**
     * @return void 返回类型
     * @Title: setLoadingAnimTransparent
     * @Description: 将加载页背景设置成透明色
     * @author wudu
     */
    public void setLoadingAnimTransparent() {
        isAnimationBackgroundTransparent = true;
        base_loading_rl.setBackgroundColor(mResources
                .getColor(R.color.transparent));
    }

    /**
     * @return void 返回类型
     * @Title: setLoadingAnimTransparent
     * @Description: 将加载页背景设置成背景色
     * @author wudu
     */
    public void setLoadingAnimGray() {
        base_loading_rl.setBackgroundColor(mResources.getColor(R.color.bg_all));
    }

    /**
     * @return void 返回类型
     * @Title: setViewNotClickable
     * @Description: 防止多次触view的发点击事件
     * @author wudu
     */
    public void setViewNotClickable(final View view) {
        // 解除上次控件的锁定
        if (mView != null) {
            if (mView instanceof Button || mView instanceof ListView) {
                mView.setEnabled(true);
            } else {
                mView.setClickable(true);
            }
        }

        // 锁住当前view
        mView = view;
        if (mView instanceof Button || mView instanceof ListView) {
            mView.setEnabled(false);
        } else {
            mView.setClickable(false);
        }
        Message msg = Message.obtain();
        msg.what = FLAG_UNABLE_VIEW;
        handler.sendMessageDelayed(msg, FLAG_UNABLE_TIME);
    }

    /**
     * @return void 返回类型
     * @Title: showLoadingView
     * @Description: 播放加载动画
     * @author wudu
     */
    public void showLoadingView() {
        base_loading_rl.setVisibility(View.VISIBLE);
        if (animationDrawable == null) {
            animationDrawable = (AnimationDrawable) base_loading_img
                    .getBackground();
//            LayoutParams layoutParams = base_loading_img.getLayoutParams();
//            layoutParams.width = (int) (ManagerApplication.screenWidth * 80d / 720);
//            layoutParams.height = layoutParams.width;
        }
        animationDrawable.setOneShot(false);
        animationDrawable.start();
        isAnimationShow = true;

//        //加载5秒之后，关闭
//        Message msg = Message.obtain();
//        msg.what = FLAG_CLOSE_LODING_TAG;
//        handler.sendMessageDelayed(msg, FLAG_CLOSE_LODING);
    }

    /**
     * @return void 返回类型
     * @Title: endLoadingView
     * @Description: 播放加载动画
     * @author wudu
     */
    public void endLoadingView() {
        if (animationDrawable != null) {
            animationDrawable.stop();
            animationDrawable = null;
        }
        isAnimationShow = false;
        base_loading_rl.setVisibility(View.GONE);
        base_loading_rl.setBackgroundColor(mResources
                .getColor(R.color.transparent));
        isAnimationBackgroundTransparent = false;
    }

    /**
     * 启动 Activity *
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, 0, 0);
    }

    /**
     * 启动 Activity 含Bundle *
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        openActivity(pClass, pBundle, 0, 0);
    }

    /**
     * 启动 Activity 含Bundle *
     */
    public void openActivity(Class<?> pClass, Bundle pBundle, int in, int out) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        if (in != 0 || out != 0) {
            overridePendingTransition(in, out);
        } else {
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    /**
     * @param title 标题，字体大，为null时，title显示content内容且字体大
     * @param type  按钮类型 FLAG_BTN_BLUE:白-蓝；FLAG_BTN_GRAY：白-灰
     * @return void 返回类型
     * @Title: showAlertDialog
     * @Description: 显示提示对话框，带一个确认按钮
     * @author wudu
     */
    public void showAlertDialog(String title, int type) {
        showAlertDialog(title, null, type);
    }

    /**
     * @param title   标题，字体大，为null时，title显示content内容且字体大
     * @param content 内容，字体小，为null时不显示内容
     * @param type    按钮类型 FLAG_BTN_BLUE:白-蓝；FLAG_BTN_GRAY：白-灰
     * @return void 返回类型
     * @Title: showAlertDialog
     * @Description: 显示提示对话框，带一个"确认"按钮
     * @author wudu
     */
    @SuppressLint("InflateParams")
    public void showAlertDialog(String title, String content, int type) {
        showAlertDialog(title, content, mResources.getString(R.string.confirm),
                type);
    }

    /**
     * @param title       标题，字体大，为null时，title显示content内容且字体大
     * @param content     内容，字体小，为null时不显示内容
     * @param textContent 按钮显示字体
     * @param type        按钮类型 FLAG_BTN_BLUE:白-蓝；FLAG_BTN_GRAY：白-灰
     * @return void 返回类型
     * @Title: showAlertDialog
     * @Description: 带title，content和一个按钮的dialog ,全部是灰色
     * @author wudu
     */
    public void showAlertDialog(String title, String content,
                                String textContent, final int type) {

        final Dialog dialog = new Dialog(BaseActivity.this, R.style.dialog);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        dialog.setCancelable(false);
        View contentView = null;
        // if (type == FLAG_BTN_BLUE) {
        // contentView = mInflater.inflate(R.layout.dg_blue, null);
        // } else {
        // contentView = mInflater.inflate(R.layout.dg_gray, null);
        // }
        contentView = mInflater.inflate(R.layout.dg_gray, null);

        // 初始化控件
        TextView tv_title = (TextView) contentView
                .findViewById(R.id.tv_dg_double_title);
        TextView tv_content = (TextView) contentView
                .findViewById(R.id.tv_dg_double_content);
        View v_dg_blue_divider_20 = contentView
                .findViewById(R.id.v_dg_blue_divider_20);

        // 设置标题
        if (Tools.isNull(title) && !Tools.isNull(content)) {
            tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv_content.setVisibility(View.GONE);
            v_dg_blue_divider_20.setVisibility(View.VISIBLE);
            tv_title.setText(content);
        } else {
            tv_title.setText(title);
        }

        // 设置内容
        if (Tools.isNull(content)) {
            tv_content.setVisibility(View.GONE);
            v_dg_blue_divider_20.setVisibility(View.VISIBLE);
        } else {
            if (!Tools.isNull(title)) {
                tv_content.setText(content);
            }
        }

        // 设置按钮
        TextView tv_dg_blue_singel = (TextView) contentView
                .findViewById(R.id.tv_dg_blue_singel);
        tv_dg_blue_singel.setText(textContent);
        tv_dg_blue_singel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onAlertDialogSingleConfirm(type);
                dialog.cancel();
            }
        });
        dialog.setContentView(contentView);
        dialog.show();

    }

    /**
     * @param title 不能为空
     * @param type  按钮类型 FLAG_BTN_BLUE:白-蓝；FLAG_BTN_GRAY：白-灰
     * @return void 返回类型
     * @Title: showAlertDialogDouble
     * @Description: 显示提示对话框，带"确认按钮" + “取消按钮”，,全部是灰色
     * @author wudu
     */
    public void showAlertDialogDouble(String title, int type, int myType) {
        showAlertDialogDouble(title, null, type,myType);
    }

    /**
     * @param title   标题，字体大，为null时，title显示content内容且字体大
     * @param content 内容，字体小，为null时不显示内容
     * @param type    按钮类型 FLAG_BTN_BLUE:白-蓝；FLAG_BTN_GRAY：白-灰
     * @return void 返回类型
     * @Title: showAlertDialogDouble
     * @Description: 显示提示对话框，带"确认按钮" + “取消按钮”
     * @author wudu
     */
    @SuppressLint("InflateParams")
    public void showAlertDialogDouble(String title, String content, int type, int myType) {
        showAlertDialogDouble(title, content,
                mResources.getString(R.string.cancel),
                mResources.getString(R.string.confirm), type, myType);
    }

    /**
     * @param title     标题，字体大，为null时，title显示content内容且字体大
     * @param content   内容，字体小，为null时不显示内容
     * @param textLeft  左边按钮显示的文字
     * @param textRight 右边按钮显示的文字
     * @param type      按钮类型 FLAG_BTN_BLUE:白-蓝；FLAG_BTN_GRAY：白-灰
     * @return void 返回类型
     * @Title: showAlertDialogDouble
     * @Description: 显示提示对话框，带"确认按钮" + “取消按钮”
     * @author wudu
     */
    public void showAlertDialogDouble(String title, String content,
                                      String textLeft, String textRight, int type, final int myType) {

        final Dialog dialog = new Dialog(BaseActivity.this, R.style.dialog);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        dialog.setCancelable(true);

        View contentView = null;
        // if (type == FLAG_BTN_BLUE) {
        // contentView = mInflater.inflate(R.layout.dg_blue, null);
        // } else {
        // contentView = mInflater.inflate(R.layout.dg_gray, null);
        // }
        contentView = mInflater.inflate(R.layout.dg_gray, null);

        // 初始化控件
        TextView tv_title = (TextView) contentView
                .findViewById(R.id.tv_dg_double_title);
        TextView tv_content = (TextView) contentView
                .findViewById(R.id.tv_dg_double_content);
        View v_dg_blue_divider_20 = contentView
                .findViewById(R.id.v_dg_blue_divider_20);

        // 设置标题
        if (Tools.isNull(title) && !Tools.isNull(content)) {
            tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv_content.setVisibility(View.GONE);
            v_dg_blue_divider_20.setVisibility(View.VISIBLE);
            tv_title.setText(content);
        } else {
            tv_title.setText(title);
        }

        // 设置内容
        if (Tools.isNull(content)) {
            tv_content.setVisibility(View.GONE);
            v_dg_blue_divider_20.setVisibility(View.VISIBLE);
        } else {
            if (!Tools.isNull(title)) {
                tv_content.setText(content);
            }
        }
        // 设置按钮
        LinearLayout layout = (LinearLayout) contentView
                .findViewById(R.id.ll_dg_blue_double);
        layout.setVisibility(View.VISIBLE);
        TextView tv_dg_blue_singel = (TextView) contentView
                .findViewById(R.id.tv_dg_blue_singel);
        tv_dg_blue_singel.setVisibility(View.GONE);
        TextView tv_dg_blue_cancel = (TextView) contentView
                .findViewById(R.id.tv_dg_blue_cancel);
        TextView tv_dg_blue_confirm = (TextView) contentView
                .findViewById(R.id.tv_dg_blue_confirm);
        tv_dg_blue_cancel.setText(textLeft);
        tv_dg_blue_confirm.setText(textRight);
        tv_dg_blue_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onAlertDialogCancel(myType);
                dialog.cancel();
            }
        });
        tv_dg_blue_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onAlertDialogConfirm(myType);
                dialog.cancel();
            }
        });
        dialog.setContentView(contentView);
        dialog.show();

    }
    /**
     * @param title     标题，字体大，为null时，title显示content内容且字体大
     * @param content   内容，字体小，为null时不显示内容
     * @param textLeft  左边按钮显示的文字
     * @param textRight 右边按钮显示的文字
     * @param type      按钮类型 FLAG_BTN_BLUE:白-蓝；FLAG_BTN_GRAY：白-灰
     * @return void 返回类型
     * @Title: showAlertDialogDouble
     * @Description: 显示提示对话框，带"确认按钮" + “取消按钮”
     * @author wudu
     */
    public void showMyAlertDialogDouble(String title, String content,
                                        String textLeft, String textRight, int type, final int myType) {

        final Dialog dialog = new Dialog(BaseActivity.this, R.style.dialog);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        dialog.setCancelable(true);

        View contentView = null;
        // if (type == FLAG_BTN_BLUE) {
        // contentView = mInflater.inflate(R.layout.dg_blue, null);
        // } else {
        // contentView = mInflater.inflate(R.layout.dg_gray, null);
        // }
        contentView = mInflater.inflate(R.layout.dg_gray, null);

        // 初始化控件
        TextView tv_title = (TextView) contentView
                .findViewById(R.id.tv_dg_double_title);
        TextView tv_content = (TextView) contentView
                .findViewById(R.id.tv_dg_double_content);
        View v_dg_blue_divider_20 = contentView
                .findViewById(R.id.v_dg_blue_divider_20);

        // 设置标题
        if (Tools.isNull(title) && !Tools.isNull(content)) {
            tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv_content.setVisibility(View.GONE);
            v_dg_blue_divider_20.setVisibility(View.VISIBLE);
            tv_title.setText(content);
        } else {
            tv_title.setText(title);
        }

        // 设置内容
        if (Tools.isNull(content)) {
            tv_content.setVisibility(View.GONE);
            v_dg_blue_divider_20.setVisibility(View.VISIBLE);
        } else {
            if (!Tools.isNull(title)) {
                tv_content.setText(content);
            }
        }
        // 设置按钮
        LinearLayout layout = (LinearLayout) contentView
                .findViewById(R.id.ll_dg_blue_double);
        layout.setVisibility(View.VISIBLE);
        TextView tv_dg_blue_singel = (TextView) contentView
                .findViewById(R.id.tv_dg_blue_singel);
        tv_dg_blue_singel.setVisibility(View.GONE);
        TextView tv_dg_blue_cancel = (TextView) contentView
                .findViewById(R.id.tv_dg_blue_cancel);
        TextView tv_dg_blue_confirm = (TextView) contentView
                .findViewById(R.id.tv_dg_blue_confirm);
        tv_dg_blue_cancel.setText(textLeft);
        tv_dg_blue_confirm.setText(textRight);
        tv_dg_blue_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onAlertDialogCancel(myType);
                dialog.cancel();
            }
        });
        tv_dg_blue_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onAlertDialogConfirm(myType);
                dialog.cancel();
            }
        });
        dialog.setContentView(contentView);
        dialog.show();

    }

    /**
     * @return void 返回类型
     * @Title: onAlertDialogConfirm
     * @Description: 提示框单按钮--确认键点击事件处理函数,子类覆盖
     * @author wudu
     */
    public void onAlertDialogSingleConfirm(int type) {

    }

    /**
     * @return void 返回类型
     * @Title: onAlertDialogConfirm
     * @Description: 提示框双按钮--确认键点击事件处理函数,子类覆盖
     * @author wudu
     */
    public void onAlertDialogConfirm(int type) {

    }
    /**
     * @return void 返回类型
     * @Title: onAlertDialogConfirm
     * @Description: 提示框双按钮--确认键点击事件处理函数,子类覆盖
     * @author wudu
     */
    public void onAlertDialogConfirm() {

    }

    /**
     * @return void 返回类型
     * @Title: onAlertDialogConfirm
     * @Description: 提示框双按钮--取消键点击事件处理函数,子类覆盖
     * @author wudu
     */
    public void onAlertDialogCancel() {

    }
    /**
     * @return void 返回类型
     * @Title: onAlertDialogConfirm
     * @Description: 提示框双按钮--取消键点击事件处理函数,子类覆盖
     * @author wudu
     */
    public void onAlertDialogCancel(int type) {

    }

    /**
     * 隐藏软键盘
     * @param view 输入控件
     */
    public void hiddenKey(View view){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).

                hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 开启软键盘
     * @param view 输入控件
     */
    public void openKey(View view){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).

                showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 根据前一个输入框修改后一个输入框的内容
     * @param editText1    用户输入框
     * @param editText2     会改变的输入框
     */
    public void edtTextChange(final EditText editText1, final EditText editText2){
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    editText2.setText(PingYinUtil.converterToFirstSpell(editText1.getText().toString()));
                } catch (Exception e) {
                    LogPrintUtils.writeErrorLog(e.toString());
                    editText1.setText("");
                    toastMsg("输入非法字符");
                }
            }
        });

    }
    /***
     * 获取设备唯一号
     * @return
     */
//    public String getDeviceID()
//    {
//        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//      String deviceId = telephonyManager.getDeviceId();
//        return deviceId;
//
//
////	    	String sn = telephonyManager.getSimSerialNumber();
////	    	String phoneNum = telephonyManager.getLine1Number();
////
////	    	WifiManager wifi = (WifiManager)this.getSystemService(Context.WIFI_SERVICE);
////	    	WifiInfo wifiInfo = wifi.getConnectionInfo();
////	    	String mac = wifiInfo.getMacAddress();
////
////	    	System.out.println("\ndeviceId = " + deviceId + "\nsn = " + sn + "\nphoneNum = " + phoneNum + "\nmac = " + mac);
//    }

    public void setMyKayInput(EditText editText){
        editText.setKeyListener(new NumberKeyListener() {

            // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
            @Override
            public int getInputType() {
                // TODO Auto-generated method stub

                return 3;
            }

            // 返回允许输入的字符
            @Override
            protected char[] getAcceptedChars() {
                // TODO Auto-generated method stub
                char[] c = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                        'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                        'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                        'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','*','#','$','-','_','.' ,'+','/'};
                return c;
            }
        });
    }

    /***
     * 获取设备唯一号
     * @return
     */
   /* public String getDeviceID()
    {
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }*/

    public String getDeviceID()
    {
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
                Log.i("123","permissionCheck"+permissionCheck);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 111);
                }else {
                     String deviceId = telephonyManager.getDeviceId();
                     return deviceId;
                }
                return "";
    }

      @Override
      public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
          switch (requestCode) {
              case 111:
                  if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                      //TODO
                      getDeviceID();
                  }
                  break;

          }

      }


      public void setMyNumberInput(EditText editText){
          editText.setKeyListener(new NumberKeyListener() {

              // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
              @Override
              public int getInputType() {
                  // TODO Auto-generated method stub

                  return 3;
              }

              // 返回允许输入的字符
              @Override
              protected char[] getAcceptedChars() {
                  // TODO Auto-generated method stub
                  char[] c = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
                  return c;
              }
          });
      }
      /***
       * 网络是否可连接
       * @return
       */
    public  boolean isConnected(Context context)
    {
        //是否有网络
        ConnectivityManager connectivityMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isAvailable())
        {
            return false;
        }

        return true;
    }


    private static String DATABASE_PATH_DB = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/BAISON/DAQ/data"+"/BASIC.db";
    private static String DATABASE_PATH_JDB = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/BAISON/DAQ/data"+"/BASIC.db-journal";
    private static String DATABASE_PATH_ZIP = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/BAISON/DAQ/data"+"/BASIC.zip";

    /**
     * 档案库是否存在
     */
    public boolean dbIsExistForKB() {

        File file = new File(DATABASE_PATH_ZIP);
        File file1 = new File(DATABASE_PATH_DB);
        if (!file.exists() || !file1.exists()) {
            toastMsg("请先进行资料下载！");
            return false;
        } else {
            return true;
        }
    }
    /**
     * 档案库是否存在
     */
    public boolean dbIsExistForKBNOMSG() {

        File file = new File(DATABASE_PATH_ZIP);
        File file1 = new File(DATABASE_PATH_DB);
        if (!file.exists() || !file1.exists()) {
            return false;
        } else {
            return true;
        }
    }
}
