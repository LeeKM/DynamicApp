package top.leekm.android.dynamiclib.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lkm on 2017/4/3.
 */

public class FileUtils {

    public static File getFile(String path) {
        return new File(path);
    }

    public static boolean isFileExist(String file) {
        return getFile(file).exists();
    }

    public static void fileCopy(String from, String to) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(getFile(from));
            fos = new FileOutputStream(getFile(to));
            streamCopy(fis, fos);
        } catch (IOException e) {
            throw e;
        } finally {
            close(fis);
            close(fos);
        }
    }

    public static void streamToFile(InputStream inputStream,
                                    String file) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(getFile(file));
            streamCopy(inputStream, fileOutputStream);
        } catch (IOException ex) {
            throw ex;
        } finally {
            close(fileOutputStream);
        }
    }

    public static void streamCopy(InputStream is,
                                  OutputStream os) throws IOException {
        byte[] buf = new byte[512];
        int len = -1;
        while ((len = is.read(buf)) > 0) {
            os.write(buf, 0, len);
        }
        os.flush();
    }

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Throwable throwable) {
                // ignore
            }
        }
    }
}
