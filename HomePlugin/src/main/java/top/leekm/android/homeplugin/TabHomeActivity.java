package top.leekm.android.homeplugin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import top.leekm.android.dynamiclib.DynamicActivityStub;

/**
 * Created by lkm on 2017/5/4.
 */

public class TabHomeActivity extends DynamicActivityStub
        implements HomeToolBarHolder.ToolBarListener {

    private HomeToolBarHolder mToolBarHolder;
    private HomeListHolder mHomeListHolder;

    @Override
    public void onCreate(Bundle savedInstance) {
        setContentView(R.layout.activity_home_tab);
        mToolBarHolder = new HomeToolBarHolder(this);
        mToolBarHolder.setToolBarListener(this);
        mHomeListHolder = new HomeListHolder(this);
        mHomeListHolder.setBannerAutoScroll(true);
    }

    @Override
    public void onQrScanClick(View view) {
        Toast.makeText(this, "onQrScanClick", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSearchClick(View view) {
        Toast.makeText(this, "onSearchClick", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserCenterClick(View view) {
        Toast.makeText(this, "onUserCenterClick", Toast.LENGTH_LONG).show();
    }
}
