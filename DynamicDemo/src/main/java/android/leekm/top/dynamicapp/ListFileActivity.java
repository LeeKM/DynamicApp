package android.leekm.top.dynamicapp;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import top.leekm.android.dynamiclib.DynamicActivity;

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
        if(null == files) {
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

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, DynamicActivity.class));
    }
}
