package top.leekm.android.dynamiclib;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import top.leekm.android.dynamiclib.storage.BundleTable;
import top.leekm.android.dynamiclib.storage.DynamicDatabase;
import top.leekm.android.dynamiclib.utils.ClassUtils;
import top.leekm.android.dynamiclib.utils.FileUtils;
import top.leekm.android.dynamiclib.utils.ReflectUtils;
import top.leekm.android.dynamiclib.utils.Run;
import top.leekm.android.dynamiclib.utils.RunOrThrow;

/**
 * Created by lkm on 2017/4/26.
 */
public class DynamicHelper {

    private DynamicSDK mSDK;
    private BundleTable mTable;

    DynamicHelper(DynamicSDK sdk) {
        this.mSDK = sdk;
        this.mTable = DynamicDatabase.sharedInstance(sdk.mAppContext).openTable(BundleTable.class);
    }

    public void registBundle(DynamicBundle bundle) throws IOException {
        unRegistBundle(bundle);
        FileUtils.linkToFile(bundleLinkPath(bundle), bundle.filePath);
        mTable.registBundle(bundle);
    }

    public void unRegistBundle(DynamicBundle bundle) throws IOException {
        FileUtils.deleteFile(bundleLinkPath(bundle));
        FileUtils.deleteFile(bundleDexPath(bundle));
        mTable.deleteBundleByName(bundle.bundleName);
    }

    public DynamicBundle getBundleByName(String bundleName) {
        return null;
    }

    public boolean bundleExist(DynamicBundle bundle) {
        if (!mTable.isBundleRegisted(bundle)) {
            return FileUtils.fileExist(bundleDefaultPath(bundle));
        }
        return true;
    }

    public String bundleFilePath(DynamicBundle bundle) {
        if (TextUtils.isEmpty(bundle.filePath)) {
            return bundleDefaultPath(bundle);
        }
        return bundle.filePath;
    }

    public String bundleDefaultPath(DynamicBundle bundle) {
        String libDir = mSDK.mAppContext.getApplicationInfo().nativeLibraryDir;
        String libName = String.format("lib%s.so", bundle.bundleName);
        return new File(libDir, libName).getAbsolutePath();
    }

    public String bundleLinkPath(DynamicBundle bundle) {
        File fileDir = mSDK.mAppContext.getDir("dyn_dex", Context.MODE_PRIVATE);
        String fileName = String.format("lib%s.apk", bundle.bundleName);
        return new File(fileDir, fileName).getAbsolutePath();
    }

    public String bundleDexPath(DynamicBundle bundle) {
        File fileDir = mSDK.mAppContext.getDir("dyn_dex", Context.MODE_PRIVATE);
        String fileName = String.format("lib%s.dex", bundle.bundleName);
        return new File(fileDir, fileName).getAbsolutePath();
    }

    public void configActivityStub(DynamicActivity target) {
        DynamicBundle bundle = getBundleByName(target.bundleName);
        LoadedBundle loadedBundle = getOrCreateLoadedBundle(bundle);
        target.mAssetManager = loadedBundle.assetManager;
        Resources rawResource = target.getResources();
        target.mResources = new DynamicResources(target.mAssetManager, rawResource.getDisplayMetrics(),
                rawResource.getConfiguration());
        target.mResources.attachHostResources(rawResource);
        Resources.Theme rawTheme = target.getTheme();
        target.mTheme = target.mResources.newTheme();
        target.mTheme.setTo(rawTheme);
        target.mClassLoader = loadedBundle.classLoader;
        target.mStub = (DynamicActivityStub) instanceClass(target.mClassLoader, target.activityClazz);
        target.mStub.attachBaseContext(target);
    }

    private Object instanceClass(ClassLoader classLoader, String className) {
        try {
            return classLoader.loadClass(className).newInstance();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    private LoadedBundle getOrCreateLoadedBundle(final DynamicBundle bundle) {
        final AtomicReference<LoadedBundle> ref = new AtomicReference<>(mBundleCache.get(bundle.bundleName));
        if (null == ref.get()) {
            RunOrThrow.wrapper(new Run() {
                @Override
                protected void todo() throws Throwable {
                    LoadedBundle loadedBundle = new LoadedBundle();
                    loadedBundle.assetManager = AssetManager.class.newInstance();
                    ReflectUtils.invokeMethod(loadedBundle.assetManager, "assAssetPath", bundleFilePath(bundle));
                    loadedBundle.classLoader = ClassUtils.create(bundle.filePath, mSDK.mAppContext.getDir(
                            "dyn_dex", Context.MODE_PRIVATE).getAbsolutePath(),
                            null, mSDK.mAppContext.getClassLoader());
                    ref.set(loadedBundle);
                }
            });
        }
        return ref.get();
    }

    private ConcurrentHashMap<String, LoadedBundle> mBundleCache = new ConcurrentHashMap<>();

    private class LoadedBundle {
        AssetManager assetManager;
        DynamicResources resources;
        ClassLoader classLoader;
    }
}
