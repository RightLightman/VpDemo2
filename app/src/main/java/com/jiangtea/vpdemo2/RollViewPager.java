package com.jiangtea.vpdemo2;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by xx on 2016/5/20.
 */
public class RollViewPager extends ViewPager{
    private List<ImageView> mDots;//点的集合
    private List<String> mTitles;//标题数据集合
    private TextView tv_title;//显示标题的文本控件
    private List<String> mImages;//热门新闻的图片地址
    private RollAdapter mAdapter;
    private static final int MAX_COUNT = 10000000;
    private int currentPostion = MAX_COUNT / 2;//当前选中脚标

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            //获取当前界面的索引
//            int currentItem = RollViewPager.this.getCurrentItem();
//            int pos = (currentItem+1)%mImages.size();
            //让viewpager切换下一个页面
            RollViewPager.this.setCurrentItem(currentPostion + 1,true);

            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    };
    private int mStartX;
    private int mStartY;
    private long mCurrentTimeMillis;//手指按下时的毫秒值
    private OnClickItemListener mOnClickItemListener;//点击事件接口回调，

    public RollViewPager(Context context) {
        super(context);
        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //修改标题
                tv_title.setText(mTitles.get(position % mImages.size()));
                //修改对应点的选中状态
                mDots.get(currentPostion % mImages.size()).setImageResource(R.drawable.dot_normal);
                mDots.get(position % mImages.size()).setImageResource(R.drawable.dot_focus);
                currentPostion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        this.setOnTouchListener(new OnTouchListener() {



            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //自动轮播停止
                        mHandler.removeCallbacksAndMessages(null);
                        mCurrentTimeMillis = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        //自动轮播开始
                        mHandler.sendEmptyMessageDelayed(0, 2000);
                        System.out.println("ACTION_UP");
                        long upTime = System.currentTimeMillis();
                        if ((upTime - mCurrentTimeMillis) < 100) {//手指按下与手指抬起之间的时间间隔小于500ms则认为是点击事件
//                            Toast.makeText(getContext(),"点击事件被响应了",Toast.LENGTH_SHORT).show();
                            mOnClickItemListener.onClick();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        System.out.println("ACTION_CANCEL");
                        mHandler.sendEmptyMessageDelayed(0, 2000);
                        break;
                }
                return false;
            }
        });
    }

    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    public RollViewPager(Context context, List<ImageView> dots) {
//        this(context);
//        this.mDots = dots;
//    }

//    public void setTitles(List<String> newsTitles, TextView top_news_title) {
//        //记录数据与控件
//        this.mTitles = newsTitles;
//        this.tv_title = top_news_title;
//        //设置默认的标题
//        this.tv_title.setText(this.mTitles.get(0));
//    }
//
//    public void setImages(List<String> newsImages) {
//        this.mImages = newsImages;
//    }

    public RollViewPager(MainActivity context, List<ImageView> dots, List<String> newsTitles, List<String> newsImages,TextView top_news_title) {
        this(context);
        this.mDots = dots;
        this.mTitles = newsTitles;
        this.mImages = newsImages;
        this.tv_title = top_news_title;
    }

    public void start() {
        //给轮播图设置适配器进行展示
        if (mAdapter == null) {
            mAdapter = new RollAdapter();
            this.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        //让ViwPager的默认值改为Integer.MAX_VALUE/2
        this.setCurrentItem(currentPostion);
        //修改标题
        tv_title.setText(mTitles.get(currentPostion % mImages.size()));
        mDots.get(currentPostion % mImages.size()).setImageResource(R.drawable.dot_focus);

        //自动轮播
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }


    private class RollAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return MAX_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = (ImageView) View.inflate(getContext(), R.layout.viewpager_item, null);
            Glide.with(getContext()).load(mImages.get(position % mImages.size())).into(view);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    //事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) ev.getX();
                mStartY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int diffX = endX - mStartX;
                int diffY = endY - mStartY;
                if (Math.abs(diffX) > Math.abs(diffY)) {//左右滑动
                    getParent().requestDisallowInterceptTouchEvent(true);//请求父控件不拦截我
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);//父控件想拦截就拦截
                }

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnClickItemListener(OnClickItemListener listener) {
        this.mOnClickItemListener = listener;
    }

    //点击事件的接口
    public interface OnClickItemListener {
        public void onClick();
    }

}

