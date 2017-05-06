package top.leekm.android.homeplugin;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lkm on 2017/5/6.
 */

public class HomeListAdapter extends BaseAdapter {

    private ArrayList<Commmodity> mCommodities = new ArrayList<>(10);

    View headerView;
    View catagrayView;

    public HomeListAdapter() {
        mCommodities.add(new Commmodity("华为nova手机 玫瑰金", 1680, R.mipmap.image_huawei_nova));
        mCommodities.add(new Commmodity("希捷(SEAGATE) 1TB", 449, R.mipmap.image_seagate_disk));
        mCommodities.add(new Commmodity("Apple Iphone SE", 2399, R.mipmap.image_apple_se));
        mCommodities.add(new Commmodity("飞鱼星路由器", 249, R.mipmap.image_flyflish_router));
        mCommodities.add(new Commmodity("凯瑞蒂现代双人床", 5200, R.mipmap.image_some_bed));
        mCommodities.add(new Commmodity("华硕(ASUS)超极本", 4299, R.mipmap.image_asus_notebook));
        mCommodities.add(new Commmodity("TP-Link 路由器", 499, R.mipmap.image_tp_router));
        mCommodities.add(new Commmodity("Dell xps超极本", 10999, R.mipmap.image_dell_notebook));
        mCommodities.add(new Commmodity("华硕(ASUS)路由器", 2399, R.mipmap.image_asus_router));
        mCommodities.add(new Commmodity("英特尔(INTEL) NUC", 3599, R.mipmap.image_intel_nuc));
    }

    @Override
    public int getCount() {
        return mCommodities.size() / 2 + 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position << 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            return headerView;
        } else if (position == 1) {
            return catagrayView;
        }
        TextView textView = new TextView(parent.getContext());
        textView.setText("a\nb\n" + position);
        return textView;
    }
}

class Commmodity {

    public String name;
    public double price;
    public int imageId;

    public Commmodity(String name, double price, int imageId) {
        this.name = name;
        this.price = price;
        this.imageId = imageId;
    }

}