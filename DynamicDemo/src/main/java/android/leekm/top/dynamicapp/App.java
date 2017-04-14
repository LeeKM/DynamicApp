package android.leekm.top.dynamicapp;

import android.app.Application;
import android.content.Context;

import top.leekm.android.dynamiclib.InstrumentationHook;

/**
 * Created by lkm on 2017/4/3.
 */

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        InstrumentationHook.hook();
        super.attachBaseContext(base);
//        R.onResourcesLoaded(0x6f);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
