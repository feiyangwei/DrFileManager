package com.drxx.drfilemanager;

import android.app.Application;
import android.content.Context;

/**
 * 类描述：
 * 创建人：wfy
 * 创建时间：2018/11/21
 * 邮箱：cugb_feiyang@163.com
 */
public class DrApplication extends Application {

    private Context mContext;
    private static DrApplication sInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
    public static synchronized DrApplication getInstance() {
        return sInstance;
    }

}
