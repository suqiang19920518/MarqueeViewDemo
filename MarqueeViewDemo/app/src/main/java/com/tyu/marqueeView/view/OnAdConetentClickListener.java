package com.tyu.marqueeView.view;

import com.tyu.marqueeView.entity.AdData;

/**
 * @author: sq
 * @date: 2017/9/19
 * @corporation: 深圳市思迪信息技术股份有限公司
 * @description: 广告被点击时回调
 */
public interface OnAdConetentClickListener {
     void OnAdConetentClickListener(int index, AdData data);
}
