package top.leekm.android.dynamiclib.utils;

import android.os.Build;

import java.io.File;
import java.io.IOException;

import dalvik.system.DexClassLoader;

/**
 * Created by lkm on 2017/4/5.
 */

public class ClassUtils {

    private final static String APK_SUBFIX = ".apk";

    public static ClassLoader create(String dexPath, String optimizedDirectory,
                                     String librarySearchPath, ClassLoader parent) throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            dexPath = fixForAndroidKiKit(dexPath, optimizedDirectory);
        }
        return new DexClassLoader(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

    private static String fixForAndroidKiKit(String dexPath, String optimizedDirectory) throws IOException {
        String optimizedPath = optimizedPathFor(dexPath, optimizedDirectory, APK_SUBFIX);
        FileUtils.linkToFile(optimizedPath, dexPath);
        return optimizedPath;
    }

    private static String optimizedPathFor(String dexPath, String optimizedDir, String subfix) {
        String name = new File(dexPath).getName();
        int dotIndex = name.indexOf('.');
        if (dotIndex < 0) {
            name += subfix;
        } else {
            StringBuffer sbf = new StringBuffer(dotIndex + subfix.length());
            sbf.append(name, 0, dotIndex);
            sbf.append(subfix);
            name = sbf.toString();
        }
        return new File(optimizedDir, name).getAbsolutePath();
    }

}
