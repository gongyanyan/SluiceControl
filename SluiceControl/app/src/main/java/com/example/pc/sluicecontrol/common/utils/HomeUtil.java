package com.example.pc.sluicecontrol.common.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by hccscs on 2016/1/9.
 */
public class HomeUtil {

    /**
     * 强制隐藏虚拟键盘
     *
     * @param context
     * @param view
     */
    public static void hideKeyWord(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
