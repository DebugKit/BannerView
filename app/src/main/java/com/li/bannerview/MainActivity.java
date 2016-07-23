package com.li.bannerview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ViewPager mViewpager;

    LinearLayout mPointContainer;

    private AutoBroadcastPicTask mAutoTask;
    private LunboAdapter mAdapter;

    private List<Integer> mDatas;

    private Handler handler = new Handler();

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mViewpager = (ViewPager) findViewById(R.id.lunbo_viewpager);
        initListener();
        mPointContainer = (LinearLayout) findViewById(R.id
                .lunbo_point_container);
        mAdapter = new LunboAdapter();
        mDatas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mDatas.add(i);
        }
        mViewpager.setAdapter(mAdapter);
        //添加点
        for (int i = 0; i < mDatas.size(); i++) {
            ImageView point = new ImageView(this);
            if (i == 0) {
                point.setImageResource(R.drawable.shape_indicator_select);
            } else {
                point.setImageResource(R.drawable.shape_indicator_normal);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 5;
            layoutParams.bottomMargin = 5;
            point.setLayoutParams(layoutParams);
            mPointContainer.addView(point);
        }

        //从中间开始轮播
        int centerCount = Integer.MAX_VALUE / 2;
        centerCount = centerCount - centerCount % mDatas.size();
        Log.d("MainActivity", "centerCount:" + centerCount);
        mViewpager.setCurrentItem(centerCount);

        //开始自动轮播
        if (mAutoTask == null) {
            mAutoTask = new AutoBroadcastPicTask();
        }
        mAutoTask.start();
    }

    /**
     * 自动轮播任务
     */
    private class AutoBroadcastPicTask implements Runnable {

        @Override
        public void run() {
            int currentItem = mViewpager.getCurrentItem();
            mViewpager.setCurrentItem(++currentItem);
            handler.postDelayed(this, 2000);
        }

        /**
         * 开始轮播
         */
        public void start() {
            stop();//开始之前先停止，以免重复开始
            handler.postDelayed(this, 2000);
        }

        /**
         * 停止轮播
         */
        public void stop() {
            handler.removeCallbacks(this);
        }
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener
                () {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % mDatas.size();
                selectPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mAutoTask.stop();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        mAutoTask.start();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 根据角标设置选中的点
     *
     * @param index
     */
    private void selectPoint(int index) {
        for (int i = 0; i < mPointContainer.getChildCount(); i++) {
            ImageView point = (ImageView) mPointContainer.getChildAt(i);
            if (index == i) {
                point.setImageResource(R.drawable.shape_indicator_select);
            } else {
                point.setImageResource(R.drawable.shape_indicator_normal);
            }
        }
    }

    /**
     * 轮播图适配器
     */
    class LunboAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mDatas.size();

            ImageView iv = new ImageView(context);

            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            iv.setImageResource(R.mipmap.ic_launcher);

            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object
                object) {
            container.removeView((View) object);
        }
    }
}
