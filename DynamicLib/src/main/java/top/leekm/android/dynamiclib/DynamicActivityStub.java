package top.leekm.android.dynamiclib;

import android.content.Intent;

/**
 * Created by lkm on 2017/4/3.
 */

public class DynamicActivityStub extends ActivitySupportImpl {
    @Override
    public void startActivity(Intent intent) {
        startActivity(((DynamicActivity) getActivity()).bundleName, intent);
    }
}
