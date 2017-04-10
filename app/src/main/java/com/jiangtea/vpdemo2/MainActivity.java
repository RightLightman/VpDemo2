package com.jiangtea.vpdemo2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private List<NewItemBean.NewsItem> mNews = new ArrayList<>();//新闻列表的数据集合
    private List<String> mNewsTitles = new ArrayList<>();//热门新闻的标题
    private List<String> mNewsImages = new ArrayList<>();//热门新闻的图片地址url
    private List<ImageView> dots = new ArrayList<>();//页面指示器，小圆点集合
    private LinearLayout dots_ll;//用来展示小圆点的容器
    private TextView top_news_title;//用来展示轮播图的标题
    private LinearLayout top_news_viewpager;//用来展示轮播图的容器

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //轮播图控件
            RollViewPager rollViewPager = new RollViewPager(MainActivity.this,dots,mNewsTitles,mNewsImages,top_news_title);
            //将标题数据集合和控件交由轮播图viewpager管理
//            rollViewPager.setTitles(mNewsTitles,top_news_title);
//            //将热门新闻的图片地址集合给viewpager管理
//            rollViewPager.setImages(mNewsImages);
            rollViewPager.setOnClickItemListener(new RollViewPager.OnClickItemListener() {
                @Override
                public void onClick(int position) {
                    Toast.makeText(MainActivity.this,"轮播图的点击" + position,Toast.LENGTH_SHORT).show();
                }
            });
            rollViewPager.start();

            //将轮播图rollviewpager添加到线性布局中进行展示
            top_news_viewpager.addView(rollViewPager);
//            //设置是配置
//            if (mNewItemAdapter == null) {
//                mNewItemAdapter = new NewItemAdapter(mNews, mContext);
//                mLv.setAdapter(mNewItemAdapter);
//            } else {
//                mNewItemAdapter.notifyDataSetChanged();
//            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_roll_view);

        dots_ll = (LinearLayout)findViewById(R.id.dots_ll);
        top_news_title = (TextView)findViewById(R.id.top_news_title);
        top_news_viewpager = (LinearLayout)findViewById(R.id.top_news_viewpager);


        //动态创建小圆点initDots(newItemBean.data.topnews.size());
//        initDots(4);
        parseJson("");
    }

    //动态创建轮播图的点
    private void initDots(int size) {
        dots.clear();
        dots_ll.removeAllViews();
        for (int i = 0; i < size; i++) {
            ImageView dot = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px
                    (this, 5), DensityUtil.dip2px(this, 5));
            if (i == 0) {
                dot.setImageResource(R.drawable.dot_focus);
            } else {
                dot.setImageResource(R.drawable.dot_normal);
                params.leftMargin = DensityUtil.dip2px(this, 5);
            }
            dot.setLayoutParams(params);
            dots.add(dot);
            //展示在界面上
            dots_ll.addView(dot);
        }
    }

    private void parseJson(String result) {

        //获取新闻列表的数据集合
//        mNews.clear();
        //newItemBean.data.news
//        mNews.addAll();

        //动态创建小圆点newItemBean.data.topnews.size()
        initDots(4);
        //获取标题与图片数据
        mNewsTitles.clear();
        mNewsImages.clear();
        for (int i = 0; i < 4; i++) {
            mNewsTitles.add("标题" + (i + 1));
            mNewsImages.add("http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg");
        }
        mHandler.sendEmptyMessage(0);
    }
}
