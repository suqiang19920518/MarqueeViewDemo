package com.tyu.marqueeView.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author: sq
 * @date: 2017/9/19
 * @corporation: 深圳市思迪信息技术股份有限公司
 * @description: 自定义TextView，用于显示跑马灯效果
 */
public class FocusedTextView extends TextView {
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写isFocused()方法，使其一直获取焦点
    @Override
    public boolean isFocused() {
        return true;
    }
}
