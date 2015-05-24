package com.cursonjiang.weather;

import android.app.Application;
import android.content.Context;

import com.cursonjiang.weather.util.logger.Logger;

/**
 * Created by Curson on 15/5/23.
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Logger.init("weather").hideThreadInfo();

    }

    public static Context getContext() {
        return mContext;
    }
}
