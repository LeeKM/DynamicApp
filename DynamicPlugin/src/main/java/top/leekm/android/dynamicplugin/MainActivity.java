package top.leekm.android.dynamicplugin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import top.leekm.android.dynamiclib.DynamicActivity;
import top.leekm.android.dynamiclib.DynamicActivityStub;

public class MainActivity extends DynamicActivityStub {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.appName) {
            Toast.makeText(this, "App名字", Toast.LENGTH_LONG).show();
        } else if (itemId == R.id.dynamic) {
            Toast.makeText(this, "动态的", Toast.LENGTH_LONG).show();
        } else if (itemId == R.id.my_collect) {
            Intent intent = new Intent(this, DynamicActivity.class);
            intent.putExtra(DynamicActivity.BUNDLE_TAG, "libcollection.so");
            intent.putExtra(DynamicActivity.ACTIVITY_TAG, "top.leekm.android.dynamicplugin.CollectionActivity");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
