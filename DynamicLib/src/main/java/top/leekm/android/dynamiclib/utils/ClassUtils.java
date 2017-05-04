package top.leekm.android.dynamiclib.utils;

import java.io.IOException;

import dalvik.system.DexClassLoader;

/**
 * Created by lkm on 2017/4/5.
 */

public class ClassUtils {

    public static ClassLoader create(String dexPath, String optimizedDirectory,
                                     String librarySearchPath, ClassLoader parent) throws IOException {
        return new DexClassLoader(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

}
