package com.drxx.drfilemanager.utils;

import android.os.Build;

/**
 * 类描述：
 * 创建人：wfy
 * 创建时间：2018/11/21
 * 邮箱：cugb_feiyang@163.com
 */
public class Utils {

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

}
