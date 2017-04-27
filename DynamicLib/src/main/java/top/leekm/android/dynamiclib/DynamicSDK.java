package top.leekm.android.dynamiclib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;

import javax.crypto.Cipher;

import top.leekm.android.dynamiclib.utils.RunOrThrow;
import top.leekm.android.dynamiclib.utils.SecureUtil;

/**
 * Created by lkm on 2017/4/3.
 */

public class DynamicSDK {

    public final static String ACTIVITY_TAG = "dyn:Activity";
    public final static String BUNDLE_TAG = "dyn:Bundle";

    private String mRsaPublic;
    private Cipher decrypter;

    Context mAppContext;
    DynamicHelper mHelper;

    private DynamicSDK(Context context, String rsaPublic) {
        mHelper = new DynamicHelper(this);
        mAppContext = context.getApplicationContext();
        this.mRsaPublic = rsaPublic;
        this.decrypter = SecureUtil.generateRSAPublicKeyDecryptor(mRsaPublic);
    }

    private static DynamicSDK sdk;

    public static synchronized DynamicSDK sharedInstance(Context context) {
        return DynamicSDK.sharedInstance(context, null);
    }

    public static synchronized DynamicSDK sharedInstance(Context context, String rsaPublic) {
        if (null == sdk) {
            sdk = new DynamicSDK(context, rsaPublic);
        }
        return sdk;
    }

    public void startActivity(Activity context, String bundleName, Class<? extends DynamicActivityStub> targetStub) {
        startActivity(context, bundleName, DynamicActivity.class, targetStub);
    }

    public void startActivity(Activity context, String bundleName, Class<? extends Activity> targetActivity,
                              Class<? extends DynamicActivityStub> targetStub) {
        this.startActivity(context, bundleName, targetActivity.getName(), targetStub.getName());
    }

    public void startActivity(final Activity context, final String bundleName, final String targetActivity, final String targetStub) {
        RunOrThrow.wrapper(new RunOrThrow() {
            @Override
            protected void todo() throws Throwable {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(context.getPackageName(), targetActivity));
                intent.putExtra(BUNDLE_TAG, bundleName);
                intent.putExtra(ACTIVITY_TAG, targetStub);
                context.startActivity(intent);
            }
        });
    }

    public void registBundle(DynamicBundle bundle, boolean override) throws IOException {
        if (SecureUtil.checkBundleDigest(bundle, decrypter)) {
            mHelper.registBundle(bundle);
        } else {
            throw new SecurityException("check bundle failed");
        }
    }

    static class SecurityException extends RuntimeException {
        public SecurityException(String reason) {
            super(reason);
        }
    }
}
