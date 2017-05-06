package top.leekm.android.homeplugin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import top.leekm.android.dynamiclib.utils.Run;

/**
 * Created by lkm on 2017/5/6.
 */

public class HomeListHolder {

    private ListView mHomeList;
    private HomeListAdapter mListAdapter = new HomeListAdapter();
    private HomeBannerAdapter mBannerAdapter = new HomeBannerAdapter();
    private boolean bannerAutoScroll = false;
    private ViewPager mBannerView;
    private int autoScrollTravsal = 5000;

    public HomeListHolder(TabHomeActivity activity) {
        mHomeList = (ListView) activity.findViewById(R.id.homeList);
        mListAdapter.headerView = getBannerView(activity, mHomeList);
        mListAdapter.catagrayView = getCatagaryView(activity, mHomeList);
        mHomeList.setAdapter(mListAdapter);
    }

    public void setBannerAutoScroll(boolean autoScroll) {
        if (bannerAutoScroll != autoScroll) {
            if (autoScroll) {
                handler.postDelayed(autoScroller, autoScrollTravsal);
            } else {
                handler.removeCallbacks(autoScroller);
            }
        }
        bannerAutoScroll = autoScroll;
    }

    private ViewPager getBannerView(TabHomeActivity activity, ViewGroup parent) {
        mBannerView = new ViewPager(activity);
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        final int screenWidth = wm.getDefaultDisplay().getWidth();
        final int bannerHeight = (int) (340.0 / 790.0 * screenWidth);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                screenWidth, bannerHeight);
        mBannerView.setLayoutParams(params);
        mBannerView.setAdapter(mBannerAdapter);
        return mBannerView;
    }

    private GridView getCatagaryView(TabHomeActivity activity, ViewGroup parent) {
        int[] catagoryIds = {R.mipmap.icon_supermarket, R.mipmap.icon_cloth,
                R.mipmap.icon_coupon, R.mipmap.icon_fresh, R.mipmap.icon_hotels,
                R.mipmap.icon_oversea, R.mipmap.icon_tickits, R.mipmap.icon_transport,
                R.mipmap.icon_trip, R.mipmap.icon_recharge};
        String[] catagoryNames = {"超市", "时装", "优惠券", "生鲜", "旅店",
                "海外购", "电影票", "火车票", "旅行", "充值"};
        ArrayList<Map<String, ?>> source = new ArrayList<>();
        for (int i = 0; i < catagoryIds.length; ++i) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("resId", catagoryIds[i]);
            map.put("name", catagoryNames[i]);
            source.add(map);
        }
        GridView catagaryView = new GridView(activity);
        catagaryView.setNumColumns(5);
        catagaryView.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                activity.getResources().getDimensionPixelSize(
                        R.dimen.homeCatagaryVIewHeight));
        catagaryView.setLayoutParams(params);

        catagaryView.setAdapter(new SimpleAdapter(activity, source,
                R.layout.activity_home_catagary_item, new String[]
                {"resId", "name"}, new int[]{R.id.catagaryIcon, R.id.catagaryName}));

        return catagaryView;
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    private Run autoScroller = new Run() {
        @Override
        protected void todo() throws Throwable {
            int currentPage = mBannerView.getCurrentItem();
            int nextPage = currentPage + 1;
            if (currentPage == (mBannerView.getAdapter().getCount() - 1)) {
                nextPage = 0;
            }
            mBannerView.setCurrentItem(nextPage, true);
            handler.postDelayed(this, autoScrollTravsal);
        }
    };
}

class HomeBannerAdapter extends PagerAdapter {

    int[] bannerImageIds = {R.mipmap.image_banner_1, R.mipmap.image_banner_2,
            R.mipmap.image_banner_3, R.mipmap.image_banner_4,
            R.mipmap.image_banner_5};

    @Override
    public int getCount() {
        return bannerImageIds.length;
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
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(bannerImageIds[position]);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);
        return imageView;
    }
}