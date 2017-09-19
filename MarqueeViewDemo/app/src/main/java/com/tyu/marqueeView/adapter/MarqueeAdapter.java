package com.tyu.marqueeView.adapter;


import android.content.Context;
import android.view.View;

import com.tyu.marqueeView.R;
import com.tyu.marqueeView.entity.MarqueeBean;

import java.util.List;

public class MarqueeAdapter extends RVBaseAdapter<MarqueeBean> {

    @Override
    public int getItemCount() {
        return datas.size() == 0 ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public MarqueeBean getItem(int position) {
        return datas.get(position % datas.size());
    }

    public MarqueeAdapter(Context context, List<MarqueeBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void initialHolder(MyViewHolder holder, MarqueeBean item, final int position) {
        holder.setText(R.id.tv_desc, item.getDesc());
        holder.setText(R.id.tv_content, item.getContent());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickCallback!=null){
                    itemClickCallback.onItemClicked(position);
                }
            }
        });
    }
}
