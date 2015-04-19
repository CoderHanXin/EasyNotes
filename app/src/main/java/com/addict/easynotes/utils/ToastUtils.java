package com.addict.easynotes.utils;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.addict.easynotes.App;

public class ToastUtils {
    private ToastUtils() {
    }

    private static Toast mToast = null;
    private static Context mContext = App.getContext();
    private static String mText = "";
    private static int mDuration = Toast.LENGTH_SHORT;

    private static void showToast() {
        if (null == mContext || TextUtils.isEmpty(mText)) {
            return;
        }

        if (null == mToast) {
            mToast = Toast.makeText(mContext, mText, mDuration);
        } else {
            mToast.setDuration(mDuration);
            mToast.setText(mText);
        }

        mToast.show();
    }

    public static void hideToast() {
        mToast.cancel();
    }

    public static void showShort(int resId) {
        mText = mContext.getResources().getString(resId);
        mDuration = Toast.LENGTH_SHORT;
        showToast();
    }

    public static void showShort(String message) {
        mText = message;
        mDuration = Toast.LENGTH_SHORT;
        showToast();
    }

    public static void showLong(int resId) {
        mText = mContext.getResources().getString(resId);
        mDuration = Toast.LENGTH_LONG;
        showToast();
    }

    public static void showLong(String message) {
        mText = message;
        mDuration = Toast.LENGTH_LONG;
        showToast();
    }
}
