package top.leekm.android.homeplugin;

import android.view.View;

/**
 * Created by lkm on 2017/5/6.
 */

public class HomeToolBarHolder {

    private ToolBarListener mToolBarListener;

    public HomeToolBarHolder(TabHomeActivity activity) {
        activity.findViewById(R.id.qrScan).setOnClickListener(mListener);
        activity.findViewById(R.id.searchBox).setOnClickListener(mListener);
        activity.findViewById(R.id.userCenter).setOnClickListener(mListener);
    }

    public void setToolBarListener(ToolBarListener listener) {
        this.mToolBarListener = listener;
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != mToolBarListener) {
                final int id = v.getId();
                if (R.id.qrScan == id) {
                    mToolBarListener.onQrScanClick(v);
                } else if (R.id.searchBox == id) {
                    mToolBarListener.onSearchClick(v);
                } else if (R.id.userCenter == id) {
                    mToolBarListener.onUserCenterClick(v);
                }
            }
        }
    };

    public interface ToolBarListener {
        void onQrScanClick(View view);

        void onSearchClick(View view);

        void onUserCenterClick(View view);
    }
}
