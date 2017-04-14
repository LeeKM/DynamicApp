package top.leekm.android.dynamiclib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import top.leekm.android.dynamiclib.utils.DynamicClassLoader;

/**
 * Created by lkm on 2017/4/3.
 */

public class DynamicActivity extends Activity {

    public final static String ACTIVITY_TAG = "dyn:Activity";
    public final static String BUNDLE_TAG = "dyn:Bundle";

    private String activityClazz;
    private String bundleName;

    DynamicBundle bundle = new DynamicBundle();

    DynamicResources mResources;
    AssetManager mAssetManager;
    DynamicActivityStub mStub;
    DynamicClassLoader mClassLoader;
    Resources.Theme mTheme;

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(bundle.title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.configBundleInfo(getIntent());
        try {
            this.initEnvironment();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        mStub.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStub.onSaveInstanceState(outState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState,
                                    PersistableBundle outPersistentState) {
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
        String activityClazz = null == data ? null : data.getStringExtra(ACTIVITY_TAG);
        String bundleName = null == data ? null : data.getStringExtra(BUNDLE_TAG);
        Exception exc = null;
        if (null == activityClazz && null == bundleName) {
            try {
                android.content.pm.ActivityInfo info = getPackageManager()
                        .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                activityClazz = info.metaData.getString(ACTIVITY_TAG);
                bundleName = info.metaData.getString(BUNDLE_TAG);
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

    private void initEnvironment() throws Throwable {

//        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin";
//        new File(root).mkdirs();

        String root = getApplicationInfo().nativeLibraryDir;

        String path = root + "/" + bundleName;

        mAssetManager = AssetManager.class.newInstance();

        Method addAssetPath = mAssetManager.getClass().getMethod("addAssetPath", String.class);
        addAssetPath.invoke(mAssetManager, path);

        mResources = new DynamicResources(mAssetManager, super.getResources().getDisplayMetrics(),
                super.getResources().getConfiguration());
        mResources.attachHostResources(super.getResources());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());

        mClassLoader = new DynamicClassLoader(path, getDir("dynDex",
                MODE_PRIVATE).getAbsolutePath(), null, super.getClassLoader());

        Class<DynamicActivityStub> clazz = (Class<DynamicActivityStub>) mClassLoader.loadClass(activityClazz);
        mStub = clazz.newInstance();
        mStub.attachBaseContext(this);
    }

}
