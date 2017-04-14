package top.leekm.android.dynamiclib;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by lkm on 2017/4/3.
 */

public interface ActivitySupport {

    void setContentView(int resId);

    View findViewById(int id);

    Intent getIntent();

    MenuInflater getMenuInflater();

    boolean onCreateOptionsMenu(Menu menu);

    boolean onOptionsItemSelected(MenuItem item);

}
