package android.leekm.top.dynamicapp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import top.leekm.android.dynamiclib.DynamicActivity;
import top.leekm.android.dynamiclib.DynamicBundle;
import top.leekm.android.dynamiclib.DynamicSDK;
import top.leekm.android.dynamiclib.utils.Run;
import top.leekm.android.dynamiclib.utils.SecureUtil;

/**
 * Created by lkm on 2017/4/3.
 */

public class ListFileActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_file);
        ((TextView) findViewById(R.id.files)).setText(getDirInfo());
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.testButton).setOnClickListener(this);
    }

    private String getDirInfo() {
        ApplicationInfo info = getApplicationInfo();

        StringBuffer stringBuffer = new StringBuffer();
        String root = getCacheDir().getParent();
        travel(new File(root).listFiles(), stringBuffer, "");
        stringBuffer.append("\n\n");
        root = info.nativeLibraryDir;
        travel(new File(root).listFiles(), stringBuffer, "");
        return stringBuffer.toString();
    }

    private void travel(File[] files, StringBuffer buffer, String prefix) {
        if (null == files) {
            return;
        }
        for (File file : files) {
            buffer.append(prefix);
            buffer.append(file.getName());
            if (file.isFile()) {
                buffer.append(" (");
                buffer.append(fileSize(file));
                buffer.append(")");
            }
            buffer.append('\n');
            if (file.isDirectory()) {
                travel(file.listFiles(), buffer, prefix + "\t");
            }
        }
    }

    private String fileSize(File file) {
        long len = file.length();
        StringBuffer buffer = new StringBuffer();
        if (len / (1024 * 1024 * 1024) > 0) {
            buffer.append(len / (1024 * 1024 * 1024));
            buffer.append("G ");
            len %= 1024 * 1024 * 1024;
        }
        if (len / (1024 * 1024) > 0) {
            buffer.append(len / (1024 * 1024));
            buffer.append("M ");
            len %= 1024 * 1024;
        }
        if (len / (1024) > 0) {
            buffer.append(len / 1024);
            buffer.append("K ");
            len %= 1024;
        }
        buffer.append(len);
        buffer.append("B");
        return buffer.toString();
    }

    public static String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM5wG8nFtp11ukWDlesZggUUpF4Wo6nRMoxyULLXY+pEv4hdHroaTKfPQkETy64wxVf+BvQTjTP6lGIGP6Qp7SMCAwEAAQ==";
    public static String privateKey = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAznAbycW2nXW6RYOV6xmCBRSkXhajqdEyjHJQstdj6kS/iF0euhpMp89CQRPLrjDFV/4G9BONM/qUYgY/pCntIwIDAQABAkEAhgYaxGBsMsYf7MmFJ2EkHJBONheUoceDfdMpuTJFV+2nt7KAlMvMSrw989YjGgrCE4oHwc9RGV7RTREOxT7YIQIhAOhD0VzvagVxP/dWr3JGOTmbU3CIXkkL913I1qD0yR5vAiEA44ikWgsdDd7VIXjLpqLGfykD2ygoYox36/3YBGbzdo0CIQDQ6c8zTJWMd6UVkH8WxltKmY+ftMk54u2JCdEjzYnpOQIhAIwwdWrRLB6Oq+yKm5dY9UoFwE14aaGGgKDTbxQPRnRFAiBychowLtk81Uly6oUPoic7ts6l6RgyDAPNSQZKqOhZGQ==";

    boolean installed = false;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            startActivity(new Intent(this, DynamicActivity.class));
        } else if (v.getId() == R.id.testButton) {
            executor.execute(new Run() {
                @Override
                protected void todo() throws Throwable {
                    if (!installed) {
                        DynamicSDK sdk = DynamicSDK.sharedInstance(ListFileActivity.this, publicKey);
                        File libDir = new File(Environment.getExternalStorageDirectory(), "plugin");
                        String srcPath = new File(libDir, "libcollection_1_1.so").getAbsolutePath();
                        DynamicBundle bundle = new DynamicBundle();
                        bundle.bundleName = "collection";
                        bundle.filePath = srcPath;
                        SecureUtil.getBundleDigest(privateKey, bundle);
                        sdk.registBundle(bundle);
                    } else {

                        DynamicSDK sdk = DynamicSDK.sharedInstance(ListFileActivity.this, publicKey);
                        String srcPath = new File(getApplicationInfo().nativeLibraryDir, "libcollection.so").getAbsolutePath();
                        DynamicBundle bundle = new DynamicBundle();
                        bundle.bundleName = "collection";
                        bundle.filePath = srcPath;
                        SecureUtil.getBundleDigest(privateKey, bundle);
                        sdk.registBundle(bundle);

                    }

                    installed = !installed;
                }
            });
        }
    }

    Executor executor = Executors.newSingleThreadExecutor();
}
