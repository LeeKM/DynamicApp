package top.leekm.android.dynamicplugin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import top.leekm.android.dynamiclib.DynamicActivityStub;

/**
 * Created by lkm on 2017/4/14.
 */

public class CollectionActivity extends DynamicActivityStub
        implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.collection_activity);
        findViewById(R.id.ant_me).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "你好！ant me", Toast.LENGTH_LONG).show();
    }
}
