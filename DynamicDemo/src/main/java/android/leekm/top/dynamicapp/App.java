package android.leekm.top.dynamicapp;

import android.app.Application;
import android.content.Context;

import top.leekm.android.dynamiclib.DynamicSDK;
import top.leekm.android.dynamiclib.InstrumentationHook;

/**
 * Created by lkm on 2017/4/3.
 */

public class App extends Application {

    public static String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM5wG8nFtp11ukWDlesZggUUpF4Wo6nRMoxyULLXY+pEv4hdHroaTKfPQkETy64wxVf+BvQTjTP6lGIGP6Qp7SMCAwEAAQ==";

    @Override
    protected void attachBaseContext(Context base) {
        InstrumentationHook.hook();
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicSDK.sharedInstance(this, publicKey);
    }

}
