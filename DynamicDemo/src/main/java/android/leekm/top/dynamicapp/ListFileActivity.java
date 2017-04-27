package android.leekm.top.dynamicapp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
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

    public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjPM0AXTiUikmYuolaZnelqEhaD3pjbGnQ67mFoBgL6ujHIvryUCo+7ZiRErWSTkUkxi9NWZtKZz1MZJe72RjffllN4u06KZmYlNa/DzOpmsALNgSVi+AarRzeKZKgGgMtstyuJQ9Sm0PeMOicp9j8c0DpfZYcpBPQrJRzuRDR2CENMGVDt/JifWGIiFKzv9rxRpGGAJVD2Zj2t8VCZcuuN3Orr5UE67BdQTOnh4yBEoV/rRBWJFvrLGeXbPklkODD2VJ0F9ab/TdK3OdyvlNDNT/1TEpnPVcOBJIR/CAb2IpgW+T50fNYZfMuOeZxWOezF31kry7zTQBFNGrNKWkOwIDAQAB";
    public static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCM8zQBdOJSKSZi6iVpmd6WoSFoPemNsadDruYWgGAvq6Mci+vJQKj7tmJEStZJORSTGL01Zm0pnPUxkl7vZGN9+WU3i7TopmZiU1r8PM6mawAs2BJWL4BqtHN4pkqAaAy2y3K4lD1KbQ94w6Jyn2PxzQOl9lhykE9CslHO5ENHYIQ0wZUO38mJ9YYiIUrO/2vFGkYYAlUPZmPa3xUJly643c6uvlQTrsF1BM6eHjIEShX+tEFYkW+ssZ5ds+SWQ4MPZUnQX1pv9N0rc53K+U0M1P/VMSmc9Vw4EkhH8IBvYimBb5PnR81hl8y455nFY57MXfWSvLvNNAEU0as0paQ7AgMBAAECggEAB81+14XM8ZdtlJQ5tfrao6QP4WFsCHgJNmn8OsqoiHsL8IyeZOm7cRPLpNQvwKX0SaBY+RfOeztBJ5lewAniqBqiS8zkGuz7Rf4vTz9egT/zJohfm33NshqDOe6auTkJWyUwGJ+8NN6MuLa7y3SfZ8ddqFZW1yC+Aoc0mCtyvgS4Ua60U5EqENvtOIXHPiYniXZEHlcOsYb1APpdm+oriYv2mM1zev7YU/ADoncSMP+n1Cbh9qHU+mszYUOK9AxWM4QGs8HfUyrWO7H14sUzkzUJ03YJ21gpDEiC7dD+pHcSahLujS2WFvCQetj48ynFcDPNt/Q89Y5q4s63grOf4QKBgQDpg4K0EHoNKpd6Wc4Io6DzIpn446mHIMKEGyOQNs5yDh+g1na6f7LZOeC9F9NSFZhT19IiGXL9NlfARIRsYjUpl0eKxkpkv8MLcMm9w8LvTkqGaVvs1rS0SAnkkCgPj+MXS4hngYB5VmhjqiyhMoSEODo7upHA6pY2T8FHeJLcnQKBgQCahdkgnyybNOOvdJFeroQsdF4JtEOQFBAUQHuMSH9kD4qEcPkdW169+sORxePOtdT7LgfLT8k8QLYJP+348BWHfHmaXL2G+SUE63qgwLboBH7URSykQyWIS18YebGL3a6RbJB6kqWCk+OGvDYLwdCqqzJEciNLe1tuq4xqaJawtwKBgE662EZ9h6kIwymtPZoMESfocqFHoNC+ata/j63/zHsH6AICTOV0SIiUywD2F3mFbmZWTbLRgcN9nvhkIYGpWuM67hQL5ukw/kHNNTeV2RN2AgpvW17uxN5Dq5I0SE125cnk3YiIURiX2XpA1etDbQpnKrsYnCu+7AZDvZarQFjBAoGAZMlMt+RaedljcLt9Xd5CQ6Sa+YVkVANiAXOVoghfXupsFLqDIczI3O1Exj3mupsLEdISL2AZB33Fvf6+vxIJWNetkStQS0sSBEV7NIK+SlM0TkvYNEO/pUhT/GjTj0DGbf/7tJcV8Td4vhbgwYZnDgqfvNkdgEwzdNnbukQBlb0CgYEA3d0XW7s03LL1dHELJJTJshvDYOwrBEoiDYYY+urYbMxkhjIjkDEX48BzBBBv/dm1A04rvPWwsI9JrUKB5nj0BVkM5uy8gwa+3oqd3lfriT7wVQ6y+/CRxxbsFb3qI9n5yyQmHbrG0ljp+mzDJcHSwl+qOXok343XfjUpDKj7aNE=";

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            startActivity(new Intent(this, DynamicActivity.class));
        } else if (v.getId() == R.id.testButton) {
            executor.execute(new Run() {
                @Override
                protected void todo() throws Throwable {
                    String libDir = getApplicationInfo().nativeLibraryDir;
                    String srcPath = libDir + File.separatorChar + "libplugin.so";
                    DynamicSDK sdk = DynamicSDK.sharedInstance(ListFileActivity.this, publicKey);
                    DynamicBundle bundle = new DynamicBundle();
                    bundle.filePath = srcPath;
                    SecureUtil.getBundleDigest(privateKey, bundle);
                    sdk.registBundle(bundle, true);
                }
            });
        }
    }

    Executor executor = Executors.newSingleThreadExecutor();
}
