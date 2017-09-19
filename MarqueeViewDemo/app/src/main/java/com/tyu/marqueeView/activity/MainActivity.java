package com.tyu.marqueeView.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.tyu.marqueeView.R;
import com.tyu.marqueeView.adapter.MarqueeAdapter;
import com.tyu.marqueeView.adapter.RVBaseAdapter;
import com.tyu.marqueeView.entity.AdData;
import com.tyu.marqueeView.entity.MarqueeBean;
import com.tyu.marqueeView.view.ADTextView;
import com.tyu.marqueeView.view.AutoTextView;
import com.tyu.marqueeView.view.MarqueeView;
import com.tyu.marqueeView.view.OnAdConetentClickListener;
import com.tyu.marqueeView.view.VerticalScrolledListview;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final int FLAG_TEXTVIEW = 0;

    private ADTextView ad_textview;
    private MarqueeView mMarqueeView;
    private AutoTextView mAutoTextView;
    private VerticalScrolledListview scrolledListview;

    private ArrayList<AdData> adDatas = new ArrayList<>();
    private List<String> lvDatas = new ArrayList<>();
    private List<MarqueeBean> rvDatas = new ArrayList<>();
    private List<String> tvDatas = new ArrayList<>();

    private MarqueeAdapter marqueeAdapter;
    private int currentSelectPosition = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FLAG_TEXTVIEW:
                    mAutoTextView.setText(tvDatas.get(currentSelectPosition % tvDatas.size()));
                    currentSelectPosition++;
                    mHandler.sendEmptyMessageDelayed(FLAG_TEXTVIEW, 2500);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initData();
        initViews();

    }

    private void findViews() {
        ad_textview = (ADTextView) findViewById(R.id.ad_textview);
        scrolledListview = (VerticalScrolledListview) findViewById(R.id.v_scroll_listview);
        mMarqueeView = ((MarqueeView) findViewById(R.id.recycle_textview));
        mAutoTextView = ((AutoTextView) findViewById(R.id.scroll_textview));
    }

    private void initData() {

        for (int i = 0; i < 3; i++) {
            AdData data = new AdData();
            data.head = "劲爆！";
            data.content = "降价了！！！！" + i;
            adDatas.add(data);
        }

        for (int i = 0; i < 20; i++) {
            String content = "Android" + i;
            lvDatas.add(content);
        }

        for (int i = 0; i < 10; i++) {
            MarqueeBean bean = new MarqueeBean();
            bean.setDesc("描述------->" + i);
            bean.setContent("内容------->" + i);
            rvDatas.add(bean);
        }

        for (int i = 0; i < 10; i++) {
            tvDatas.add("数据" + i);
        }

    }

    private void initViews() {

        ad_textview.setData(adDatas);
        ad_textview.setMode(ADTextView.RunMode.UP);
        ad_textview.setOnAdConetentClickListener(new OnAdConetentClickListener() {
            @Override
            public void OnAdConetentClickListener(int index, AdData data) {
                Toast.makeText(MainActivity.this, data.content, Toast.LENGTH_SHORT).show();
            }
        });

        scrolledListview.setData(lvDatas);
        scrolledListview.setOnItemClickListener(new VerticalScrolledListview.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, lvDatas.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        marqueeAdapter = new MarqueeAdapter(this, rvDatas, R.layout.scroll_recycle_item);
        mMarqueeView.setAdapter(marqueeAdapter);
        mMarqueeView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMarqueeView.startScroll();
        marqueeAdapter.setItemClickCallback(new RVBaseAdapter.InnerItemClickCallback() {
            @Override
            public void onItemClicked(int position) {
                Toast.makeText(MainActivity.this, marqueeAdapter.getItem(position).getContent(), Toast.LENGTH_SHORT).show();
            }
        });

        mHandler.sendEmptyMessageDelayed(FLAG_TEXTVIEW, 1000);

    }
}
