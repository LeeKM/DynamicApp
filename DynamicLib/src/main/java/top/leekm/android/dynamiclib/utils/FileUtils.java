package top.leekm.android.dynamiclib.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Stack;

/**
 * Created by lkm on 2017/4/3.
 */

public class FileUtils {

    private final static String BASE_DIR = "dynSdk";

    public static File getFile(String path) {
        return new File(path);
    }

    public static boolean fileExist(String file) {
        return getFile(file).exists();
    }

    public static boolean linkToFile(String symble, String target) throws IOException {
        return linkToFile(symble, target, false);
    }

    public static boolean linkToFile(String symble, String target, boolean force) throws IOException {
        if (!fileExist(target)) {
            throw new FileNotFoundException("target file not found: " + target);
        }
        return execAndWait(String.format("ln -s %s %s %s", force ? "-f" : "", target, symble));
    }

    public static void deleteFile(String target) throws IOException {
        execAndWait(String.format("rm -rf %s", target));
    }

    public static byte[] getFileSHA256Digest(File file) throws IOException {
        try {
            return getFileDigest(file, "SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getFileDigest(File file, String algorithm) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        DigestInputStream dis = new DigestInputStream(new FileInputStream(file), digest);
        while (dis.read() >= 0) {
            // do-nop
        }
        return digest.digest();
    }

    private static boolean execAndWait(String cmd) throws IOException {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            return 0 == process.waitFor();
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static void close(Object closeable) {
        if (null != closeable) {
            if (closeable instanceof Closeable) {
                try {
                    ((Closeable) closeable).close();
                } catch (Throwable throwable) {
                    // ignore
                    throwable.printStackTrace();
                }
            } else {
                try {
                    ReflectUtils.invokeMethod(closeable, "close");
                } catch (Throwable throwable) {
                    // ignore
                    throwable.printStackTrace();
                }
            }
        }
    }
}
