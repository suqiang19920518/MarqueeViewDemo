package com.tyu.marqueeView.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

/**
 * @author: sq
 * @date: 2017/8/29
 * @corporation: 深圳市思迪信息技术股份有限公司
 * @description: 基础Adapter, 适用于RecyclerView的子类, <T> 数据模型(model/JavaBean)类
 */
public abstract class RVBaseAdapter<T> extends RecyclerView.Adapter<RVBaseAdapter.MyViewHolder> {

    protected Context context;
    protected List<T> datas;
    private int layoutId;
    protected LayoutInflater mInflater;
    protected InnerItemClickCallback itemClickCallback;//点击item监听回调

    public interface InnerItemClickCallback {
        void onItemClicked(int position);
    }

    public void setItemClickCallback(InnerItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public RVBaseAdapter(Context context, List<T> datas, int layoutId) {
        this.context = context;
        this.datas = datas;
        this.layoutId = layoutId;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(layoutId, null);
        return new MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        initialHolder(holder, getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public T getItem(int position) {
        return position >= datas.size() ? null : datas.get(position);
    }

    public List<T> getDatas() {
        return datas;
    }

    /**
     * 从列表中删除某项
     *
     * @param t
     */
    public void removeItem(T t) {
        int position = datas.indexOf(t);
        datas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 从列表中删除某项
     *
     * @param position
     */
    public void removeItem(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
    }


    public void addData(List<T> datas) {
        addData(0, datas);
    }

    /**
     * 在指定位置，添加数据源
     *
     * @param position
     * @param list
     */
    public void addData(int position, List<T> list) {

        if (list != null && list.size() > 0) {

            for (T t : list) {
                datas.add(position, t);
                notifyItemInserted(position);
                position++;
            }

        }
    }

    /**
     * 数据源全部刷新，即重置
     *
     * @param list
     */
    public void refreshData(List<T> list) {

        clear();
        if (list != null && list.size() > 0) {

            int size = list.size();
            for (int i = 0; i < size; i++) {
                datas.add(i, list.get(i));
                notifyItemInserted(i);
            }

        }
    }

    /**
     * 清空数据源
     */
    public void clear() {
//        int itemCount = datas.size();
//        datas.clear();
//        this.notifyItemRangeRemoved(0,itemCount);

        if (datas == null || datas.size() <= 0)
            return;

        for (Iterator it = datas.iterator(); it.hasNext(); ) {

            T t = (T) it.next();
            int position = datas.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }
    }

    /**
     * 加载更多数据，一般用于分页加载数据
     *
     * @param list
     */
    public void loadMoreData(List<T> list) {

        if (list != null && list.size() > 0) {

            int size = list.size();
            int begin = datas.size();
            for (int i = 0; i < size; i++) {
                datas.add(list.get(i));
                notifyItemInserted(i + begin);
            }

        }

    }

    public abstract void initialHolder(MyViewHolder holder, T item, int position);

    protected static class MyViewHolder extends RecyclerView.ViewHolder {
        SparseArray<View> mViews;
        View mConvertView;

        public MyViewHolder(View convertView) {
            super(convertView);
            mConvertView = convertView;
            mViews = new SparseArray<>();
        }


        public void setText(int textViewId, String content) {
            TextView textView = getView(textViewId);
            textView.setText(content);
        }


        public <T extends View> T getView(int textViewId) {
            View view = mViews.get(textViewId);
            if (view == null) {
                view = mConvertView.findViewById(textViewId);
                mViews.put(textViewId, view);
            }
            return (T) view;
        }

        public View getConvertView() {
            return mConvertView;
        }
    }

    /**
     * 使用class来启动一个Activity
     *
     * @param activity 需要启动的Activity的.class实例
     * @param flag     启动模式
     */
    public void startActivity(Class<?> activity, int flag) {
        final Intent intent = new Intent(context, activity);
        intent.addFlags(flag);
        context.startActivity(intent);
    }

    /**
     * 使用class来启动一个Activity
     *
     * @param activity 需要启动的Activity的.class实例
     * @param bundle   需要传的参数
     */
    public void startActivity(Class<?> activity, Bundle bundle) {
        final Intent intent = new Intent(context, activity);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 使用class来启动一个Activity
     *
     * @param activity 需要启动的Activity的.class实例
     * @param bundle   需要传的参数
     * @param flag     启动模式
     */
    public void startActivity(Class<?> activity, Bundle bundle, int flag) {
        final Intent intent = new Intent(context, activity);
        intent.addFlags(flag);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
