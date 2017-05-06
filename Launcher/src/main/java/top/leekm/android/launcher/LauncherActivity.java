package top.leekm.android.launcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import top.leekm.android.dynamiclib.DynamicActivityStub;
import top.leekm.android.dynamiclib.DynamicSDK;
import top.leekm.android.dynamiclib.utils.Run;

/**
 * Created by lkm on 2017/5/4.
 */

public class LauncherActivity extends DynamicActivityStub implements View.OnClickListener {

    private ViewPager mSlash;

    @Override
    public void onCreate(Bundle savedInstance) {
        if (firstLaunch()) {
            onCreateFirstLaunch(savedInstance);
        } else {
            onCreateMain(savedInstance);
        }
    }

    public void onCreateFirstLaunch(Bundle savedInstance) {
        setContentView(R.layout.activity_launcher_slash);
        mSlash = (ViewPager) findViewById(R.id.launcher_slash);
        mSlash.setAdapter(mAdapter);
    }

    private PagerAdapter mAdapter = new PagerAdapter() {

        int[] pageIds = {R.mipmap.launcher_slash_1,
                R.mipmap.launcher_slash_2,
                R.mipmap.launcher_slash_3,
                R.mipmap.launcher_slash_4};

        @Override
        public int getCount() {
            return pageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
         public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rootView = inflater.inflate(R.layout.activity_launcher_main, container, false);
            container.addView(rootView);
            ((ImageView) rootView.findViewById(R.id.background)).setImageResource(pageIds[position]);
            if (position == getCount() - 1) {
                View button = rootView.findViewById(R.id.start_shopping);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(LauncherActivity.this);
            }
            return rootView;
        }
    };

    public void onCreateMain(Bundle savedInstance) {
        setContentView(R.layout.activity_launcher_main);
        ((ImageView) findViewById(R.id.background)).setImageResource(R.mipmap.launcher_main);
        handler.postDelayed(gotoHomePlugin, 3000);
    }

    private boolean firstLaunch() {
        final int thisLastCode = 1;
        SharedPreferences sharedPreferences = getSharedPreferences("cat_dog_city", MODE_PRIVATE);
        int lastCode = sharedPreferences.getInt("last_code", 0);
        sharedPreferences.edit().putInt("last_code", thisLastCode).commit();
        return lastCode < thisLastCode;
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onClick(View v) {
        handler.post(gotoHomePlugin);
    }

    private Run gotoHomePlugin = new Run() {
        @Override
        protected void todo() throws Throwable {
            DynamicSDK sdk = DynamicSDK.sharedInstance(getActivity());
            sdk.startActivity(getActivity(), "homeplugin", "top.leekm.android.homeplugin.TabHomeActivity", null);
            getActivity().finish();
        }
    };
}
