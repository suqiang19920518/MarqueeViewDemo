package com.tyu.marqueeView.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tyu.marqueeView.R;
import com.tyu.marqueeView.entity.AdData;
import com.tyu.marqueeView.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: sq
 * @date: 2017/9/19
 * @corporation: 深圳市思迪信息技术股份有限公司
 * @description: 文字广告轮播控件
 */
public class ADTextView extends View {
    private ArrayList<AdData> data;//用于展示的数据
    private int HeadColor;//头部文字颜色
    private int ContentColor;//内容文字颜色
    private int HeadSize;//头部文字大小
    private int ContentSize;//内容文字大小
    private Paint HeadPaint;//内容头部画笔
    private Paint ContentPaint;//内容画笔
    private int index;//当前广告内容的下标
    private long interval = 2000;//广告文字固定不动显示的时间
    private int speed = 1;//文字滚动速度
    private boolean isMove = true;//文字是否在滚动状态
    private boolean isPause = false;//文字是否处于停滞状态
    private boolean hasInit = false;//是否已经初始化
    private int mX, mY;//文字绘制位置
    private String head, content;//当前轮播中的广告内容
    private OnAdConetentClickListener listener;//广告被点击回调
    private float ContentPadding;//头部和内容之间的间距
    private float paddingLeft;//左边距

    private RunMode mode=RunMode.UP;//广告滚动方向

    public enum RunMode{
        UP,DOWN;
    }

    public ADTextView(Context context) {
        this(context, null);
    }

    public ADTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ADTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs);
        init();
    }

    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ADTextView);
        speed = array.getInt(R.styleable.ADTextView_ad_text_view_speed, 1);
        interval = array.getInt(R.styleable.ADTextView_ad_text_view_interval, 2000);
        HeadColor = array.getColor(R.styleable.ADTextView_ad_text_front_color, Color.RED);
        ContentColor = array.getColor(R.styleable.ADTextView_ad_text_content_color, Color.BLACK);
        HeadSize = (int) array.getDimension(R.styleable.ADTextView_ad_text_front_size, SizeUtils.sp2px(getContext(), 15));
        ContentSize = (int) array.getDimension(R.styleable.ADTextView_ad_text_content_size, SizeUtils.sp2px(getContext(), 15));
        ContentPadding = array.getDimension(R.styleable.ADTextView_ad_text_content_padding, SizeUtils.sp2px(getContext(), 10));
        paddingLeft = array.getDimension(R.styleable.ADTextView_ad_text_padding_left, SizeUtils.sp2px(getContext(), 10));
        array.recycle();
    }

    private void init() {
        HeadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        HeadPaint.setColor(HeadColor);
        HeadPaint.setTextSize(HeadSize);
        HeadPaint.setDither(true);

        ContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ContentPaint.setColor(ContentColor);
        ContentPaint.setTextSize(ContentSize);
        ContentPaint.setDither(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureWidth(widthMeasureSpec), MeasureHeight(heightMeasureSpec));
    }

    /**
     * 高度计算
     */
    private int MeasureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {//固定高度
            result = size;
        } else {
            int HeadHeight = (int) (HeadPaint.descent() - HeadPaint.ascent());//头文字高度
            int ContentHeight = (int) (ContentPaint.descent() - ContentPaint.ascent());//内容高度
            result = Math.max(HeadHeight, ContentHeight) * 2;

            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(HeadHeight, ContentHeight);
            }
        }
        return result;
    }

    /**
     * 宽度计算
     */
    private int MeasureWidth(int widthMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            String str = "十个字十个字十个字十";
            Rect rect = new Rect();
            ContentPaint.getTextBounds(str, 0, str.length(), rect);
            result = rect.right - rect.left;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (data != null && data.size() > 0) {
            //当前需要绘制的文字内容
            head = data.get(index).head;
            content = data.get(index).content;
            //头部文字的测量
            Rect headRect = new Rect();
            HeadPaint.getTextBounds(head, 0, head.length(), headRect);
            //内容文字的测量
            Rect contentRect = new Rect();
            ContentPaint.getTextBounds(content, 0, content.length(), contentRect);
            //初始化mY
            if (mY == 0 && !hasInit) {
                //这里的mY的值实际上是文字的基线值
                mY = getMeasuredHeight() / 2 - (headRect.bottom + headRect.top) / 2;
                hasInit = true;
            }
            if(mode==RunMode.UP){
                //移动到顶部
                if (mY <= -headRect.bottom) {
                    mY = getMeasuredHeight() - headRect.top;//回到最底下
                    isPause = false;
                    index++;
                }
            }else{
                //移动到底部
                if (mY >= getMeasuredHeight() - headRect.top) {
                    mY = -headRect.bottom;
                    isPause = false;
                    index++;
                }
            }
            canvas.drawText(content, 0, content.length(), headRect.right - headRect.left + ContentPadding + paddingLeft, mY, ContentPaint);
            canvas.drawText(head, 0, head.length(), paddingLeft, mY, HeadPaint);
            //移动到中间
            boolean isCenter=false;
            if(mode==RunMode.UP)
                isCenter=mY <= getMeasuredHeight() / 2 - (headRect.bottom + headRect.top) / 2;
            else
                isCenter=mY >= getMeasuredHeight() / 2 - (headRect.bottom + headRect.top) / 2;
            if (!isPause && isCenter) {
                isMove = false;
                isPause = true;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        postInvalidate();
                        isMove = true;
                    }
                }, interval);
            }
            if(mode==RunMode.UP){
                mY -= speed;
            }else{
                mY+=speed;
            }
            //数据循环
            if (index == data.size()) {
                index = 0;
            }
            if (isMove) {
                postInvalidateDelayed(2);
            }
        }
    }

    public void setMode(RunMode mode) {
        this.mode = mode;
    }

    public ArrayList<AdData> getData() {
        return data;
    }

    public void setData(ArrayList<AdData> data) {
        if (data == null)
            data = new ArrayList<AdData>();
        this.data = data;
    }

    public void setHeadColor(int headColor) {
        HeadColor = headColor;
    }

    public void setContentColor(int contentColor) {
        ContentColor = contentColor;
    }

    public void setHeadSize(int headSize) {
        HeadSize = headSize;
    }

    public void setContentSize(int contentSize) {
        ContentSize = contentSize;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setContentPadding(float contentPadding) {
        ContentPadding = contentPadding;
    }

    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public void setOnAdConetentClickListener(OnAdConetentClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (listener != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            listener.OnAdConetentClickListener(index, data.get(index));
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }
}
