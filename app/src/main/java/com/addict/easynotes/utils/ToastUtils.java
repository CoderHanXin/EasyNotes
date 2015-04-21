/*
 * Copyright (c) 2015 Coder.HanXin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.addict.easynotes.utils;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.addict.easynotes.App;

public class ToastUtils {
    private static Toast mToast = null;
    private static Context mContext = App.getContext();
    private static String mText = "";
    private static int mDuration = Toast.LENGTH_SHORT;

    private ToastUtils() {
    }

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
