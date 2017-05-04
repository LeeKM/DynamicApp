package top.leekm.android.dynamiclib;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.lang.reflect.Method;

import top.leekm.android.dynamiclib.utils.ClassUtils;
import top.leekm.android.dynamiclib.utils.FileUtils;

/**
 * Created by lkm on 2017/4/3.
 */

public class DynamicActivity extends Activity {

    String activityClazz;
    String bundleName;
    DynamicSDK sdk;

    DynamicResources mResources;
    AssetManager mAssetManager;
    DynamicActivityStub mStub;
    ClassLoader mClassLoader;
    Resources.Theme mTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.configBundleInfo(getIntent());
        sdk = DynamicSDK.sharedInstance(this);
        sdk.mHelper.configActivityStub(this);
        super.onCreate(savedInstanceState);
        mStub.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStub.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mStub.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mStub.onCreateOptionsMenu(menu)) {
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mStub.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mStub.onStart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        mStub.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mStub.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mStub.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mStub.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStub.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mStub.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null == mClassLoader ? super.getClassLoader() : mClassLoader;
    }

    @Override
    public Resources.Theme getTheme() {
        return null == mTheme ? super.getTheme() : mTheme;
    }

    private void configBundleInfo(Intent data) {
        String activityClazz = null == data ? null : data.getStringExtra(DynamicSDK.ACTIVITY_TAG);
        String bundleName = null == data ? null : data.getStringExtra(DynamicSDK.BUNDLE_TAG);
        Exception exc = null;
        if (null == activityClazz && null == bundleName) {
            try {
                android.content.pm.ActivityInfo info = getPackageManager()
                        .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                activityClazz = info.metaData.getString(DynamicSDK.ACTIVITY_TAG);
                bundleName = info.metaData.getString(DynamicSDK.BUNDLE_TAG);
            } catch (PackageManager.NameNotFoundException e) {
                exc = e;
            }
        }
        if (null == activityClazz || null == bundleName) {
            throw new RuntimeException("missing dyn:Activity or dyn:Bundle", exc);
        } else {
            this.activityClazz = activityClazz;
            this.bundleName = bundleName;
        }
    }
}
