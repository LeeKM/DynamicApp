package top.leekm.android.dynamiclib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by lkm on 2017/4/3.
 */

public class ActivitySupportImpl extends ContextThemeWrapper
        implements ActivitySupport {

    private Activity mActivity;

    public Activity getActivity() {
        if (null == mActivity) {
            mActivity = (Activity) this.getBaseContext();
        }
        return mActivity;
    }

    @Override
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    public void setContentView(int resId) {
        getActivity().setContentView(resId);
    }

    @Override
    public View findViewById(int id) {
        return getActivity().findViewById(id);
    }

    @Override
    public Intent getIntent() {
        return getActivity().getIntent();
    }

    @Override
    public MenuInflater getMenuInflater() {
        return getActivity().getMenuInflater();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
