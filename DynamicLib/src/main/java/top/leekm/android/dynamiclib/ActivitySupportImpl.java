package top.leekm.android.dynamiclib;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by lkm on 2017/4/3.
 */

public class ActivitySupportImpl extends ContextWrapper
        implements ActivitySupport {

    private Activity mActivity;

    public ActivitySupportImpl() {
        super(null);
    }

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

    @Override
    public void startActivity(Intent intent) {
        throw new RuntimeException("unspecify bundleName");
    }

    @Override
    public void startActivity(String bundleName, Intent intent) {
        DynamicSDK sdk = DynamicSDK.sharedInstance(getActivity());
        ComponentName component = intent.getComponent();
        sdk.startActivity(getActivity(), bundleName, DynamicActivity.class.getName(),
                component.getClassName(), intent.getExtras());
    }

    /**************Activity Life-Circle**************/

    public void onCreate(Bundle savedInstance) {

    }

    public void onSaveInstanceState(Bundle outState) {

    }

    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    public void onStart() {

    }

    public void onRestart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }

    public void onDestroy() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
