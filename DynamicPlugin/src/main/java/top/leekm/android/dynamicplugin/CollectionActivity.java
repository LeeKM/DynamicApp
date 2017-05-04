package top.leekm.android.dynamicplugin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import top.leekm.android.dynamiclib.DynamicActivityStub;
import top.leekm.android.dynamiclib.DynamicSDK;

/**
 * Created by lkm on 2017/4/14.
 */

public class CollectionActivity extends DynamicActivityStub
        implements View.OnClickListener {

    ListView listView;
    DynamicSDK sdk;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        sdk = DynamicSDK.sharedInstance(this);
        setContentView(R.layout.collection_activity);
        findViewById(R.id.ant_me).setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("text", view.toString() + " - " + position);
                sdk.startActivity(getActivity(), "collection", MainActivity.class, bundle);
            }
        });
    }

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 50;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position ^ 0x10F01;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_question_list, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.content)).setText("position: " + position);
            return convertView;
        }
    };

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "你好！ant me", Toast.LENGTH_LONG).show();
    }
}
