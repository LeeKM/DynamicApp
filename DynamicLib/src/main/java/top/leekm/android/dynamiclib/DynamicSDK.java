package top.leekm.android.dynamiclib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
        mAppContext = context.getApplicationContext();
        mHelper = new DynamicHelper(this);
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

    public void startActivityForResult(Activity context, String bundleName,
                                       Class<? extends DynamicActivityStub> targetStub, int reqCode, Bundle ext) {
        startActivityForResult(context, bundleName, DynamicActivity.class, targetStub, reqCode, ext);
    }

    public void startActivityForResult(Activity context, String bundleName, Class<? extends Activity> targetActivity,
                                       Class<? extends DynamicActivityStub> targetStub, int reqCode, Bundle ext) {
        startActivityForResult(context, bundleName, targetActivity.getName(), targetStub.getName(), reqCode, ext);
    }

    public void startActivityForResult(final Activity context, final String bundleName,
                                       final String targetActivity, final String targetStub, int reqCode, final Bundle ext) {
        startActivityInternal(context, bundleName, targetActivity, targetStub, reqCode, ext, true);
    }

    public void startActivity(Activity context, String bundleName, Class<? extends DynamicActivityStub> targetStub, Bundle ext) {
        startActivity(context, bundleName, DynamicActivity.class, targetStub, ext);
    }

    public void startActivity(Activity context, String bundleName, String targetStub, Bundle ext) {
        startActivity(context, bundleName, DynamicActivity.class.getName(), targetStub, ext);
    }

    public void startActivity(Activity context, String bundleName, Class<? extends Activity> targetActivity,
                              Class<? extends DynamicActivityStub> targetStub, Bundle ext) {
        this.startActivity(context, bundleName, targetActivity.getName(), targetStub.getName(), ext);
    }

    public void startActivity(final Activity context, final String bundleName,
                              final String targetActivity, final String targetStub, final Bundle ext) {
        startActivityInternal(context, bundleName, targetActivity, targetStub, -1, ext, false);
    }

    private void startActivityInternal(final Activity context, final String bundleName,
                                       final String targetActivity, final String targetStub,
                                       final int reqCode, final Bundle ext, final boolean needResult) {
        RunOrThrow.wrapper(new RunOrThrow() {
            @Override
            protected void todo() throws Throwable {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(context.getPackageName(), targetActivity));
                if (null != ext) {
                    intent.putExtras(ext);
                }
                intent.putExtra(BUNDLE_TAG, bundleName);
                intent.putExtra(ACTIVITY_TAG, targetStub);
                if (needResult) {
                    context.startActivityForResult(intent, reqCode);
                } else {
                    context.startActivity(intent);
                }
            }
        });
    }

    public void registBundle(DynamicBundle bundle) throws IOException {
        registBundle(bundle, true);
    }

    public void registBundle(DynamicBundle bundle, boolean preload) throws IOException {
        if (SecureUtil.checkBundleDigest(bundle, decrypter)) {
            mHelper.registBundle(bundle, preload);
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
